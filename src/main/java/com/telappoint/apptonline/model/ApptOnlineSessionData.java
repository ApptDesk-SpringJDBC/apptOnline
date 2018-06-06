package com.telappoint.apptonline.model;

import java.util.HashMap;
import java.util.Map;



public class ApptOnlineSessionData {

	//Client related 
	private String clientCode;
	private String clientName;
	private String cssFileName;
	private String logoFileName;
	private String footerContent;
	private String footerLinks;
	
	//Customer related
	private String transId="";
	private String token="";
	private String loginFirst;
	private String langCode;	
	private Long customerId;
	
	private int currentPageIndex = 0;
	
	//private OnlinePageData onlinePageData;
	
	//Appointment details
	private String companyId = "";
	private String procedureId = "";
	private String locationId = "";
	private String departmentId = "";
	private String resourceId = "";
	private String serviceId = "";
	private String scheduleId = "";
	
	private Map<String,Object> jsonData = new HashMap<String,Object>();
	
	private String page_method_type;
			
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getCssFileName() {
		return cssFileName;
	}
	public void setCssFileName(String cssFileName) {
		this.cssFileName = cssFileName;
	}
	public String getLogoFileName() {
		return logoFileName;
	}
	public void setLogoFileName(String logoFileName) {
		this.logoFileName = logoFileName;
	}
	public String getFooterContent() {
		return footerContent;
	}
	public void setFooterContent(String footerContent) {
		this.footerContent = footerContent;
	}
	public String getFooterLinks() {
		return footerLinks;
	}
	public void setFooterLinks(String footerLinks) {
		this.footerLinks = footerLinks;
	}
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getLoginFirst() {
		return loginFirst;
	}
	public void setLoginFirst(String loginFirst) {
		this.loginFirst = loginFirst;
	}
	public String getLangCode() {
		return langCode;
	}
	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	/*public OnlinePageData getOnlinePageData() {
		return onlinePageData;
	}
	public void setOnlinePageData(OnlinePageData onlinePageData) {
		this.onlinePageData = onlinePageData;
	}*/
	public int getCurrentPageIndex() {
		return currentPageIndex;
	}
	public void setCurrentPageIndex(int currentPageIndex) {
		this.currentPageIndex = currentPageIndex;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getProcedureId() {
		return procedureId;
	}
	public void setProcedureId(String procedureId) {
		this.procedureId = procedureId;
	}
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public Map<String, Object> getJsonData() {
		return jsonData;
	}
	public void setJsonData(Map<String, Object> jsonData) {
		this.jsonData = jsonData;
	}
	
	public void putMapDataToJSONData(Map<String, Object> jsonMap){
		if(jsonMap!=null) {
			jsonData.putAll(jsonMap);
		}
	}
	
	public void putDataToJSONData(String key, Object vaalue){
		jsonData.put(key,vaalue);
	}
	public String getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}
	public String getPage_method_type() {
		return page_method_type;
	}
	public void setPage_method_type(String page_method_type) {
		this.page_method_type = page_method_type;
	}
	
}
