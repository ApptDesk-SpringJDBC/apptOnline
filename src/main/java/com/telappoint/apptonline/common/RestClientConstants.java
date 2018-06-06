package com.telappoint.apptonline.common;




/**
 * @author Murali G
 *
 */
public enum RestClientConstants {
	
	REST_SERVICE_GET_CLIENT_INFO("getOnlineLandingPage?clientCode=@clientCodeParam@&device=@deviceParam@&langCode=@langCodeParam@&token=@tokenParam@&transId=@transIdParam@"),
	REST_SERVICE_GET_ONLINE_PAGE_DATA("getOnlinePageData?clientCode=@clientCodeParam@&device=@deviceParam@&langCode=@langCodeParam@&token=@tokenParam@&transId=@transIdParam@"),
	REST_SERVICE_GET_ONLINE_ERROR_MSG("getOnlineErrorMessage?clientCode=@clientCodeParam@&device=@deviceParam@&langCode=@langCodeParam@&token=@tokenParam@&transId=@transIdParam@&errorCode=@errorCodeParam@"),
	REST_SERVICE_GET_PROCEDURE_ID("getProcedureId?clientCode=@clientCodeParam@&device=@deviceParam@&langCode=@langCodeParam@&token=@tokenParam@&transId=@transIdParam@&procedureName=@procedureNameParam@"),
	REST_SERVICE_GET_PROCEDURE("getProcedure?clientCode=@clientCodeParam@&device=@deviceParam@&langCode=@langCodeParam@&token=@tokenParam@&transId=@transIdParam@"),
	REST_SERVICE_GET_PROCEDURE_NO_MATCH("getProcedureNoMatch?clientCode=@clientCodeParam@&device=@deviceParam@&langCode=@langCodeParam@&token=@tokenParam@&transId=@transIdParam@&procedureValue=@procedureValueParam@"),
	REST_SERVICE_GET_LOCATION_PRIMARY_SECONDARY_AVAILABILITY("getLocationPrimarySecondaryAvailability?clientCode=@clientCodeParam@&device=@deviceParam@&langCode=@langCodeParam@&token=@tokenParam@&transId=@transIdParam@&procedureId=@procedureIdParam@"),
	REST_SERVICE_GET_ONLINE_NO_LOGIN_AUTHENTICATE("loginAuthenticate"),
	REST_SERVICE_GET_CUSTOMER_TYPE("getCustomerType?clientCode=@clientCodeParam@&device=@deviceParam@&langCode=@langCodeParam@&token=@tokenParam@&transId=@transIdParam@&customerId=@customerIdParam@"),
	REST_SERVICE_GET_UTILITY_TYPE("getUtilityType?clientCode=@clientCodeParam@&device=@deviceParam@&langCode=@langCodeParam@&token=@tokenParam@&transId=@transIdParam@&serviceFundsRcvdId=@serviceFundsRcvdIdParam@&customerTypeId=@customerTypeIdParam@"),
	REST_SERVICE_GET_SERVICE_NAME_CUSTOMER_UTILITY("getServiceNameCustomerUtility?clientCode=@clientCodeParam@&device=@deviceParam@&langCode=@langCodeParam@&token=@tokenParam@&transId=@transIdParam@&serviceFundsRcvdId=@serviceFundsRcvdIdParam@&customerTypeId=@customerTypeIdParam@&utilityTypeId=@utilityTypeIdParam@"),
	
	REST_SERVICE_GET_SERVICE_NAME_CUSTOMER_WARNING_PAGE("getServiceNameCustomerWarningPage?clientCode=@clientCodeParam@&device=@deviceParam@&langCode=@langCodeParam@&token=@tokenParam@&transId=@transIdParam@&serviceFundsRcvdId=@serviceFundsRcvdIdParam@&customerTypeId=@customerTypeIdParam@&utilityTypeId=@utilityTypeIdParam@&serviceId=@serviceIdParam@"),
	REST_SERVICE_GET_SINGLE_SERVICE_NOT_CLOSED("getSingleServiceNotClosed?clientCode=@clientCodeParam@&device=@deviceParam@&langCode=@langCodeParam@&token=@tokenParam@&transId=@transIdParam@&serviceIds=@serviceIdsParam@"),
	REST_SERVICE_GET_SERVICE_CLOSED_STATUS("getServiceClosedStatus?clientCode=@clientCodeParam@&device=@deviceParam@&langCode=@langCodeParam@&token=@tokenParam@&transId=@transIdParam@&serviceId=@serviceIdParam@"),
	REST_SERVICE_GET_SERVICE_TIME_SLOTS_AVAIL_STATUS("getServiceTimeSlotsAvailableStatus?clientCode=@clientCodeParam@&device=@deviceParam@&langCode=@langCodeParam@&token=@tokenParam@&transId=@transIdParam@&locationId=@locationIdParam@&serviceId=@serviceIdParam@"),
	
	REST_SERVICE_HOLD_FIRST_AVAILABLE_APPT("holdFirstAvailableAppointment"),
	REST_SERVICE_CONFIRM_APPT("confirmAppointment"),
	REST_SERVICE_GET_LIST_OF_THINGS_TO_BRING("getListOfThingsToBring?clientCode=@clientCodeParam@&device=@deviceParam@&langCode=@langCodeParam@&token=@tokenParam@&transId=@transIdParam@&serviceId=@serviceIdParam@"),
	REST_SERVICE_GET_HOUSE_HOLD_MONTHLY_INCOME("getHouseholdMonthlyIncome?clientCode=@clientCodeParam@&device=@deviceParam@&langCode=@langCodeParam@&token=@tokenParam@&transId=@transIdParam@&noOfPeople=@noOfPeopleParam@&serviceId=@serviceIdParam@"),
	REST_SERVICE_UPDATE_CUSTOMER_INFO("updateCustomerInfo"),
	REST_SERVICE_GET_CUSTOMER_INFO("getCustomerInfo?clientCode=@clientCodeParam@&device=@deviceParam@&langCode=@langCodeParam@&token=@tokenParam@&transId=@transIdParam@&customerId=@customerIdParam@"),
	
	REST_SERVICE_GET_CANCEL_LOGIN_AUTHENTICATE("authenticateForCancel?clientCode=@clientCodeParam@&device=@deviceParam@&langCode=@langCodeParam@&token=@tokenParam@&transId=@transIdParam@"),
	REST_SERVICE_GET_BOOKED_APPTS("getBookedAppointments?clientCode=@clientCodeParam@&device=@deviceParam@&langCode=@langCodeParam@&token=@tokenParam@&transId=@transIdParam@&customerId=@customerIdParam@"),
	REST_SERVICE_CANCEL_APPT("cancelAppointment?clientCode=@clientCodeParam@&device=@deviceParam@&langCode=@langCodeParam@&token=@tokenParam@&transId=@transIdParam@&scheduleId=@scheduleIdParam@"),
	REST_SERVICE_GET_VALIDATION_MESSAGES("getPageValidationMessages?clientCode=@clientCodeParam@&device=@deviceParam@&langCode=@langCodeParam@&token=@tokenParam@&transId=@transIdParam@"),
	REST_SERVICE_RELEASE_HOLDED_APPT("releaseHoldAppointment?clientCode=@clientCodeParam@&device=@deviceParam@&langCode=@langCodeParam@&token=@tokenParam@&transId=@transIdParam@&scheduleId=@scheduleIdParam@"),
	
	REST_SERVICE_GET_TRANS_ID("getTransId?clientCode=@clientCodeParam@&device=@deviceParam@&uuid=@uuidParam@&ipAddress=@ipAddressParam@&callerId=@callerIdParam@&userName=@userNameParam@"),
	REST_SERVICE_UPDATE_TRANS_STATE("updateTransaction?clientCode=@clientCodeParam@&device=@deviceParam@&transId=@transIdParam@&pageId=@pageIdParam@&scheduleId=@scheduleIdParam@"),
	
	REST_SERVICE_EXTEND_HOLDED_APPT_TIME("extendHoldTime?clientCode=@clientCodeParam@&device=@deviceParam@&transId=@transIdParam@&scheduleId=@scheduleIdParam@"),
	
	REST_SERVICE_CHECK_DUPLICATE_APPTS("checkDuplicateAppts?clientCode=@clientCodeParam@&device=@deviceParam@&transId=@transIdParam@&customerId=@customerIdParam@&serviceId=@serviceIdParam@&langCode=@langCodeParam@"),
	REST_SERVICE_GET_AVAIL_DATES_CALL_CENTER("getAvailableDatesCallcenter?clientCode=@clientCodeParam@&device=@deviceParam@&transId=@transIdParam@&locationId=@locationIdParam@&departmentId=@departmentIdParam@&serviceId=@serviceIdParam@"),
	REST_SERVICE_GET_AVAIL_TIMES_CALL_CENTER("getAvailableTimesCallcenter?clientCode=@clientCodeParam@&device=@deviceParam@&transId=@transIdParam@&locationId=@locationIdParam@&departmentId=@departmentIdParam@&serviceId=@serviceIdParam@&availDate=@availDateParam@"),
	REST_SERVICE_HOLD_APPT_CALL_CENTER("holdAppointmentCallCenter?clientCode=@clientCodeParam@&device=@deviceParam@&transId=@transIdParam@&locationId=@locationIdParam@&departmentId=@departmentIdParam@&serviceId=@serviceIdParam@&customerId=@customerIdParam@&procedureId=@procedureIdParam@&apptDateTime=@apptDateTimeParam@"),
	
	REST_SERVICE_GET_PLEDGE_AMT_LOGIN_AUTHENTICATE("authenticateForCancel?clientCode=@clientCodeParam@&device=@deviceParam@&langCode=@langCodeParam@&token=@tokenParam@&transId=@transIdParam@"),
	REST_SERVICE_GET_PLEDGE_AMT_DETAILS("getPledgeHistory?clientCode=@clientCodeParam@&device=@deviceParam@&langCode=@langCodeParam@&token=@tokenParam@&transId=@transIdParam@&customerId=@customerIdParam@"),
	REST_SERVICE_GET_PLEDGE_AWARD_LETER("getPledgeAwardLetter?clientCode=@clientCodeParam@&device=@deviceParam@&langCode=@langCodeParam@&token=@tokenParam@&transId=@transIdParam@&customerPledgeId=@customerPledgeIdParam@"),
	
	REST_SERVICE_GET_LOCATIONS("getLocations?clientCode=@clientCodeParam@&device=@deviceParam@&langCode=@langCodeParam@&token=@tokenParam@&transId=@transIdParam@"),
	REST_SERVICE_GET_SERVICES_CALL_CENTER("getServicesCallcenter?clientCode=@clientCodeParam@&device=@deviceParam@&langCode=@langCodeParam@&token=@tokenParam@&transId=@transIdParam@&locationId=@locationIdParam@"),
	
	REST_SERVICE_AUTHENTICATE_AND_UPDATE_CUSTOMER("authenticateAndUpdateCustomer"),	
	
	REST_SERVICE_LOGIN_AUTHENTICATE_AND_UPDATE_FOR_EXTERNAL_LOGIN("loginAuthenticateAndUpdateForExternal"),
	REST_SERVICE_LOGIN_AUTHENTICATE_FOR_EXTERNAL_LOGIN("loginAuthenticateForExternal"),
	REST_SERVICE_CONFIRM_APPT_FOR_EXTERNAL_LOGIN("confirmAppointmentExternalLogic"),
	REST_SERVICE_CANCEL_APPT_FOR_EXTERNAL_LOGIN("cancelAppointmentExternalLogic?clientCode=@clientCodeParam@&device=@deviceParam@&langCode=@langCodeParam@&token=@tokenParam@&transId=@transIdParam@&scheduleId=@scheduleIdParam@"),
	
	REST_SERVICE_SAVE_TRANS_SCRIPT_MSG("saveTransScriptMsg"),
	REST_SERVICE_GET_TRANS_SCRIPT_MSGS("getTransScriptMsgs?clientCode=@clientCodeParam@&device=@deviceParam@&langCode=@langCodeParam@&token=@tokenParam@&transId=@transIdParam@&scheduleId=@scheduleIdParam@&customerId=@customerIdParam@"),
	REST_SERVICE_DELETE_TRANS_SCRIPT_MSG("deleteTransScriptMsg?clientCode=@clientCodeParam@&device=@deviceParam@&langCode=@langCodeParam@&token=@tokenParam@&transId=@transIdParam@&transScriptMsgId=@transScriptMSGIdParam@"),
	;
	    
	private String value;
	
	RestClientConstants(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}

}
