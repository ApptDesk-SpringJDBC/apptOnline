package com.telappoint.apptonline.model;

import java.util.HashMap;
import java.util.Map;

import com.telappoint.apptonline.common.JSPPageNameConstants;

/**
 * @author Murali G
 *
 */
public class JsonResponse {
	
	private boolean validationStatus;
	private String validationResponse;
	private boolean errorStatus;
	private String errorMessage;
	private boolean responseStatus;
	private String responseMessage;
	
	private boolean authenticationStatus;
	private String authenticationMessage;
	
	private String formFields;
	private String fieldsAndMessages;
	
	private String pageRedirect;
	private boolean displayErrorPage;
	private String errorPage= JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName(); //TODO : needs to remove, after setting proper one in controller are service.
	
	private Map<String,Object> jsonMap = new HashMap<String,Object>();
	private Integer serviceId;
	private String pageContent;
	private String listOfThingToBring;
	
	//Fields for error page
	private String errorCode;
	private String errorMsg;
		
	public boolean isValidationStatus() {
		return validationStatus;
	}
	public void setValidationStatus(boolean validationStatus) {
		this.validationStatus = validationStatus;
	}
	public String getValidationResponse() {
		return validationResponse;
	}
	public void setValidationResponse(String validationResponse) {
		this.validationResponse = validationResponse;
	}
	public boolean isErrorStatus() {
		return errorStatus;
	}
	public void setErrorStatus(boolean errorStatus) {
		this.errorStatus = errorStatus;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public boolean isResponseStatus() {
		return responseStatus;
	}
	public void setResponseStatus(boolean responseStatus) {
		this.responseStatus = responseStatus;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public String getFormFields() {
		return formFields;
	}
	public void setFormFields(String formFields) {
		this.formFields = formFields;
	}
	public boolean isAuthenticationStatus() {
		return authenticationStatus;
	}
	public void setAuthenticationStatus(boolean authenticationStatus) {
		this.authenticationStatus = authenticationStatus;
	}
	public String getAuthenticationMessage() {
		return authenticationMessage;
	}
	public void setAuthenticationMessage(String authenticationMessage) {
		this.authenticationMessage = authenticationMessage;
	}
	public boolean isDisplayErrorPage() {
		return displayErrorPage;
	}
	public void setDisplayErrorPage(boolean displayErrorPage) {
		this.displayErrorPage = displayErrorPage;
	}
	
	public String getFieldsAndMessages() {
		return fieldsAndMessages;
	}
	public void setFieldsAndMessages(String fieldsAndMessages) {
		this.fieldsAndMessages = fieldsAndMessages;
	}
	public String getPageRedirect() {
		return pageRedirect;
	}
	public void setPageRedirect(String pageRedirect) {
		this.pageRedirect = pageRedirect;
	}
	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}
	public void setJsonMap(Map<String, Object> jsonMap) {
		this.jsonMap = jsonMap;
	}
	public void setJsonMapKey(String key,String value) {
		this.jsonMap.put(key, value);
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public String getPageContent() {
		return pageContent;
	}
	public void setPageContent(String pageContent) {
		this.pageContent = pageContent;
	}
	public String getListOfThingToBring() {
		return listOfThingToBring;
	}
	public void setListOfThingToBring(String listOfThingToBring) {
		this.listOfThingToBring = listOfThingToBring;
	}
	public String getErrorPage() {
		return errorPage;
	}
	public void setErrorPage(String errorPage) {
		this.errorPage = errorPage;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
