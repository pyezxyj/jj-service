package com.cdkj.service.ao.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdkj.service.ao.IServeAO;
import com.cdkj.service.bo.ICompanyBO;
import com.cdkj.service.bo.IServeArtBO;
import com.cdkj.service.bo.IServeBO;
import com.cdkj.service.bo.IServeCpBO;
import com.cdkj.service.bo.IServeCyyBO;
import com.cdkj.service.bo.IServeKfwbBO;
import com.cdkj.service.bo.ISmsOutBO;
import com.cdkj.service.bo.base.Paginable;
import com.cdkj.service.core.StringValidater;
import com.cdkj.service.domain.Serve;
import com.cdkj.service.dto.req.XN612126Req;
import com.cdkj.service.dto.req.XN612127Req;
import com.cdkj.service.enums.EBoolean;
import com.cdkj.service.exception.BizException;

@Service
public class ServeAOImpl implements IServeAO {

    @Autowired
    private IServeBO serveBO;

    @Autowired
    private IServeArtBO serveArtBO;

    @Autowired
    private IServeCpBO serveCpBO;

    @Autowired
    private IServeCyyBO serveCyyBO;

    @Autowired
    private IServeKfwbBO serveKfwbBO;

    @Autowired
    private ICompanyBO companyBO;

    @Autowired
    private ISmsOutBO smsOutBO;

    @Override
    public String addServe(XN612126Req req) {
        return serveBO.saveServe(req.getName(), req.getPic(), req.getAdvPic(),
            req.getCompanyCode(), StringValidater.toLong(req.getQuoteMin()),
            StringValidater.toLong(req.getQuoteMax()), req.getQualityCode(),
            req.getDescription(), req.getPublisher());
    }

    @Override
    public void editServe(XN612127Req req) {
        Serve serve = serveBO.getServe(req.getCode());
        serveBO.refreshServe(serve, req.getName(), req.getPic(),
            req.getAdvPic(), StringValidater.toLong(req.getQuoteMin()),
            StringValidater.toLong(req.getQuoteMax()), req.getDescription(),
            req.getPublisher());
    }

    @Transactional
    @Override
    public void dropServe(String code) {
        if (!serveBO.isServeExist(code)) {
            throw new BizException("xn0000", "服务不存在");
        }
        serveBO.removeServe(code);
        // 扫描各子表，若存在该服务则删除
        if (serveArtBO.isServeArtExist(code)) {
            serveArtBO.removeServeArt(code);
        } else if (serveCpBO.isServeCpExist(code)) {
            serveCpBO.removeServeCp(code);
        } else if (serveCyyBO.isServeCyyExist(code)) {
            serveCyyBO.removeServeCyy(code);
        } else if (serveKfwbBO.isServeKfwbExist(code)) {
            serveKfwbBO.removeServeKfwb(code);
        }
    }

    @Override
    public Paginable<Serve> queryServePage(int start, int limit, Serve condition) {
        Paginable<Serve> page = serveBO.getPaginable(start, limit, condition);
        List<Serve> list = page.getList();
        if (CollectionUtils.isNotEmpty(list)) {

        }
        return page;
    }

    // 添加服务详情信息
    private void addServeExt(Serve data) {
        /*
         * switch (data.getType()) { case "1": ServeTrain serveTrain =
         * serveTrainBO.getServeTrain(data .getCode());
         * data.setServeTrain(serveTrain); break; case "2": ServePhoto
         * servePhoto = servePhotoBO.getServePhoto(data .getCode());
         * data.setServePhoto(servePhoto); break; case "3": ServeArt serveArt =
         * serveArtBO.getServeArt(data.getCode()); data.setServeArt(serveArt);
         * break; case "4": ServeShop serveShop =
         * serveShopBO.getServeShop(data.getCode());
         * data.setServeShop(serveShop); break; case "5": ServeKfwb serveKfwb =
         * serveKfwbBO.getServeKfwb(data.getCode());
         * data.setServeKfwb(serveKfwb); break; case "6": ServeCp serveCp =
         * serveCpBO.getServeCp(data.getCode()); data.setServeCp(serveCp);
         * break; case "7": break; case "8": ServeCyy serveCyy =
         * serveCyyBO.getServeCyy(data.getCode()); data.setServeCyy(serveCyy);
         * break; default: throw new BizException("xn0000", "服务类型填写错误");
         */
        // }
    }

    @Override
    public List<Serve> queryServeList(Serve condition) {
        return serveBO.queryServeList(condition);
    }

    @Override
    public Serve getServe(String code) {
        Serve serve = serveBO.getServe(code);
        addServeExt(serve);
        return serve;
    }

    @Override
    public void editServeStatus(String code, String dealer, String dealNote) {
        Serve serve = serveBO.getServe(code);
        String publisher = serve.getPublisher();
        smsOutBO.sentContent(publisher, publisher,
            "尊敬的企业，您所发布的服务[" + serve.getName() + "]已做违规处理，违规原因[" + dealNote
                    + "]。");
        serveBO.refreshServeStatus(code, dealer, dealNote);
    }

    @Override
    public void editServeHot(String code, String isHot, String orderNo,
            String dealer) {
        Serve data = new Serve();
        data.setCode(code);
        data.setLocation(isHot);
        if (StringUtils.isNotBlank(orderNo)) {
            data.setOrderNo(Integer.valueOf(orderNo));
        } else {
            data.setOrderNo(0);
        }
        data.setDealer(dealer);
        serveBO.refreshServeHot(data);
    }

    @Override
    public void editServeHotLocation(String code, String action) {
        Serve data = serveBO.getServe(code);
        Integer location = data.getOrderNo();
        if (null == location) {
            location = 2;
        }
        if (EBoolean.YES.getCode().equalsIgnoreCase(action)) {
            if (location > 0) {
                location--;
            } else {
                throw new BizException("xn0000", "次序不可小于零");
            }
        } else {
            location++;
        }
        data.setOrderNo(location);
        serveBO.refreshServeHot(data);
    }
}
