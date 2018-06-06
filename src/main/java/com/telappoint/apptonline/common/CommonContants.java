package com.telappoint.apptonline.common;

/**
 * @author Murali
 */
public enum CommonContants {

	EMPTY_STRING(""),
	ONLINE("online"),
	APPT_ONLINE_SESSION_DATA_SESSION_VARIABLE("sessionData"),
	SYSTME_ERROR("Some System Error. Please try again later."),
	
	//FIELD NAMES
	FIELD_COMPANY("company"),
	FIELD_PROCEDURE("procedure"),
	FIELD_LOCATION("location"),
	FIELD_DEPARTMENT("department"),
	FIELD_RESOURCE("resource"),
	FIELD_SERVICE("service"),
	FIELD_NO_LOGIN_AUTHENTICATE("nologin_authenticate"),
	FIELD_CUSTOMER_TYPE("customer_type"),
	FIELD_UTILITY_TYPE("utility_type"),
	FIELD_HOUSE_HOLD_MONTHLY_INCOME("house_hold_monthly_income"),
	FIELD_PERSONAL_INFO("personal_Info"),
	
	FIELD_LOGIC("logic"),
	FIELD_PAGE("page"),
	
	PAGE_REDIRECT_SUCCESS("SUCCESS"), //ie,flow completed
	PAGE_REDIRECT_TERMINATE("TERMINATE"),
	
	FIELD_CANCEL_LOGIN_AUTHENTICATE("cancel_login_authenticate"),
	
	FIELD_PLEDGE_AMT_LOGIN_AUTHENTICATE("pledge_amt_login_auth"),
	
	ONLINE_PAGE_CONTENT_APPT_METHOD_APPT("appt"),
	ONLINE_PAGE_CONTENT_APPT_METHOD_CANCEL("cancel"),
	ONLINE_PAGE_CONTENT_APPT_METHOD_APPT_SCHEDULER_CLOSED("appt_scheduler_closed"),
	ONLINE_PAGE_CONTENT_APPT_METHOD_APPT_NO_FUNDING("appt_no_funding"),
	ONLINE_PAGE_CONTENT_APPT_METHOD_PLEDGE_AMOUNT("pledge_amount"),
	ONLINE_PAGE_CONTENT_APPT_METHOD_UPLOAD_TRANSCRIPTS("update_transcripts"),
	
	FIELD_AUTHENTICATE_UPDATE_CUSTOMER("authenticate_update_customer"),
	
	FIELD_EXTERNAL_AUTHENTICATE("external_authenticate"),
	
	;
	
	private String value;

	private CommonContants(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
