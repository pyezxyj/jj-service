package com.cdkj.service.domain;

import com.cdkj.service.dao.base.ABaseDO;

/**
* 公司资质
* @author: shan
* @since: 2017年06月05日
* @history:
*/
public class GsQualify extends ABaseDO {

	private static final long serialVersionUID = 1L;

	// 编号
	private String code;

	// 公司编号
	private String companyCode;

	// 资质编号
	private String qualifyCode;

	// 广告语
	private String slogan;

	// 状态
	private String status;

	// 申请人
	private String applyUser;

	// 申请时间
	private String applyDatetime;

	// 审批人
	private String approver;

	// 审批时间
	private String approveDatetime;

	// 审批意见
	private String approveNote;

	// 备注
	private String remark;

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setQualifyCode(String qualifyCode) {
		this.qualifyCode = qualifyCode;
	}

	public String getQualifyCode() {
		return qualifyCode;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	public String getSlogan() {
		return slogan;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setApplyUser(String applyUser) {
		this.applyUser = applyUser;
	}

	public String getApplyUser() {
		return applyUser;
	}

	public void setApplyDatetime(String applyDatetime) {
		this.applyDatetime = applyDatetime;
	}

	public String getApplyDatetime() {
		return applyDatetime;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public String getApprover() {
		return approver;
	}

	public void setApproveDatetime(String approveDatetime) {
		this.approveDatetime = approveDatetime;
	}

	public String getApproveDatetime() {
		return approveDatetime;
	}

	public void setApproveNote(String approveNote) {
		this.approveNote = approveNote;
	}

	public String getApproveNote() {
		return approveNote;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark() {
		return remark;
	}

}