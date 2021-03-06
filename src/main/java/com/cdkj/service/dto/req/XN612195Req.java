/**
 * @Title CD612050.java 
 * @Package com.cdkj.service.dto.req 
 * @Description 
 * @author xieyj  
 * @date 2016年10月7日 下午5:37:47 
 * @version V1.0   
 */
package com.cdkj.service.dto.req;

/** 
 * @author: xieyj 
 * @since: 2016年10月7日 下午5:37:47 
 * @history:
 */
public class XN612195Req extends APageReq {
    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
     */
    private static final long serialVersionUID = 1L;

    // 手机号(选填)
    private String mobile;

    // 发布者(选填)
    private String publisher;

    // 状态(选填)
    private String status;

    // 资质编号(选填)
    private String qualityCode;

    // 公司编号(选填)
    private String companyCode;

    // 类型(选填)
    private String type;

    // 紧急程度(选填)
    private String urgentLevel;

    // 处理人(选填)
    private String dealer;

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getQualityCode() {
        return qualityCode;
    }

    public void setQualityCode(String qualityCode) {
        this.qualityCode = qualityCode;
    }

    public String getUrgentLevel() {
        return urgentLevel;
    }

    public void setUrgentLevel(String urgentLevel) {
        this.urgentLevel = urgentLevel;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
