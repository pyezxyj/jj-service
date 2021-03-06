package com.cdkj.service.ao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdkj.service.ao.IDemandAO;
import com.cdkj.service.bo.ICompanyBO;
import com.cdkj.service.bo.IDemandBO;
import com.cdkj.service.bo.IQualifyBO;
import com.cdkj.service.bo.ISmsOutBO;
import com.cdkj.service.bo.IUserBO;
import com.cdkj.service.bo.base.Paginable;
import com.cdkj.service.core.EGeneratePrefix;
import com.cdkj.service.core.OrderNoGenerater;
import com.cdkj.service.domain.Company;
import com.cdkj.service.domain.Demand;
import com.cdkj.service.domain.Qualify;
import com.cdkj.service.domain.User;
import com.cdkj.service.dto.req.XN612190Req;
import com.cdkj.service.dto.req.XN612192Req;
import com.cdkj.service.enums.EBoolean;
import com.cdkj.service.exception.BizException;

/**
 * @author: xieyj 
 * @since: 2016年10月7日 下午5:16:01 
 * @history:
 */
@Service
public class DemandAOImpl implements IDemandAO {

    @Autowired
    private IDemandBO demandBO;

    @Autowired
    private ISmsOutBO smsOutBO;

    @Autowired
    private ICompanyBO companyBO;

    @Autowired
    private IQualifyBO qualifyBO;

    @Autowired
    private IUserBO userBO;

    @Override
    public String addDemand(XN612190Req req) {
        String expCompanyName = null;
        if (StringUtils.isNotBlank(req.getExpCompany())) {
            expCompanyName = companyBO.getCompany(req.getExpCompany())
                .getName();
        }
        Demand data = new Demand();
        String code = OrderNoGenerater.generateM(EGeneratePrefix.XQ.getCode());
        data.setCode(code);
        data.setName(req.getName());
        data.setQualityCode(req.getQualityCode());
        data.setExpCompany(req.getExpCompany());
        data.setExpCompanyName(expCompanyName);

        data.setUrgentLevel(req.getUrgentLevel());
        data.setDescription(req.getDescription());
        data.setStatus(EBoolean.YES.getCode());
        data.setPublisher(req.getPublisher());
        data.setPublishDatetime(new Date());
        demandBO.saveDemand(data);
        return code;
    }

    @Override
    public void editDemand(XN612192Req req) {
        demandBO.getDemand(req.getCode());
        Demand data = new Demand();
        data.setCode(req.getCode());
        data.setName(req.getName());
        data.setQualityCode(req.getQualityCode());
        data.setExpCompany(req.getExpCompany());
        data.setUrgentLevel(req.getUrgentLevel());
        data.setDescription(req.getDescription());
        data.setPublisher(req.getPublisher());
        data.setPublishDatetime(new Date());
        demandBO.refreshDemand(data);
    }

    @Override
    public void dropDemand(String code) {
        if (!demandBO.isDemandExist(code)) {
            throw new BizException("xn0000", "需求不存在");
        }
        demandBO.removeDemand(code);
    }

    @Override
    public Paginable<Demand> queryDemandPage(int start, int limit,
            Demand condition) {
        Paginable<Demand> page = demandBO.getPaginable(start, limit, condition);
        List<Demand> list = page.getList();
        for (Demand demand : list) {
            if (StringUtils.isNotBlank(demand.getExpCompany())) {
                Company company = companyBO.getCompany(demand.getExpCompany());
                demand.setExpCompanyName(company.getName());
            }
            Qualify qualify = qualifyBO.getQualify(demand.getQualityCode());
            demand.setQualifyName(qualify.getName());
            User user = userBO.getRemoteUser(demand.getPublisher());
            demand.setPublisherName(user.getNickname());
        }
        return page;
    }

    @Override
    public List<Demand> queryDemandList(Demand condition) {
        List<Demand> page = demandBO.queryDemandList(condition);
        return page;
    }

    @Override
    public Demand getDemand(String code) {
        Demand demand = demandBO.getDemand(code);
        if (StringUtils.isNotBlank(demand.getExpCompany())) {
            Company company = companyBO.getCompany(demand.getExpCompany());
            demand.setExpCompanyName(company.getName());
        }
        Qualify qualify = qualifyBO.getQualify(demand.getQualityCode());
        demand.setQualifyName(qualify.getName());
        User user = userBO.getRemoteUser(demand.getPublisher());
        demand.setPublisherName(user.getNickname());
        return demand;
    }

    /** 
     * @see com.cdkj.service.ao.IDemandAO#violationDemand(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void violationDemand(String code, String dealer, String dealNote) {
        Demand demand = demandBO.getDemand(code);
        String publisher = demand.getPublisher();
        // 发送短信
        smsOutBO.sentContent(publisher, "尊敬的用户，您所发布的需求[" + demand.getName()
                + "]已做违规处理，违规原因[" + dealNote + "]。");
        demandBO.refreshDemandStatus(demand, dealer, dealNote);
    }
}
