package com.telappoint.apptonline.common;


/**
 * @author Murali
 */
public enum LogicConstants {
	
	PROCIDURE_NO_MATCH_LOGIC("procedure.NO_MATCH","PROCEDURE_NO_MATCH"),
	LOCATION_CLOSED_LOGIC("location.LOC_CLOSED","LOCATION_CLOSED"),
	LOCATION_NO_APPTS_LOGIC("location.LOC_NO_APPTS","LOC_NO_APPTS"),
	NO_LOGIN_AUTHENTICATE_CUST_BLOCKED("nologin_authenticate.CUST_BLOCKED","CUST_BLOCKED"),
	NO_LOGIN_AUTHENTICATE_CUST_DELETED("nologin_authenticate.CUST_DELETED","CUST_DELETED"),
	
	CUSTOMER_TYPE_ALREADY_RCVD_FUNDING("customer_type.ALREADY_RCVD_FUNDING","CUSTOMER_TYPE_ALREADY_RCVD_FUNDING"),
	UTILITY_TYPE_RCVD_LIHEAP_FUNDING("utility_type.RCVD_LIHEAP_FUNDING","UTILITY_TYPE_RCVD_LIHEAP_FUNDING"),
	SERVICE_RCVD_2ND_GRANT("service.RCVD_2ND_GRANT","SERVICE_RCVD_2ND_GRANT"),
	SERVICE_SELECT_SINGLE_SERVICE("service.SELECT_SINGLE_SERVICE","SERVICE_SELECT_SINGLE_SERVICE"),
	SERVICE_SER_CLOSED("select_single_service.SER_CLOSED","SERVICE_SER_CLOSED"),
	SERVICE_SER_TIME_SLOTS_AVAIL("select_single_service.SER_TIME_SLOTS_AVAIL","SERVICE_SER_TIME_SLOTS_AVAIL"),
	SERVICE_SER_WARNING_MSG("select_single_service.SER_WARNING_MSG","SERVICE_SER_WARNING_MSG"),
	
	HOUSE_HOLD_MONTHLY_INCOME_GRATER("house_hold_monthly_income.INCOME_GRATER","HOUSE_HOLD_MONTHLY_INCOME_GRATER"),
	
	PERSONAL_INFO_CUST_UPDATE_FAIL("personal_Info.CUST_UPDATE_FAIL","PERSONAL_INFO_CUST_UPDATE_FAIL"),
	
	CANCEL_LOGIN_AUTH_CUSTOMER_NOT_FOUND("cancel_login_authenticate.CUSTOMER_NOT_FOUND","CUSTOMER_NOT_FOUND"),
	
	PLEDGE_AMT_LOGIN_AUTH_CUSTOMER_NOT_FOUND("pledge_amt_login_auth.CUSTOMER_NOT_FOUND","CUSTOMER_NOT_FOUND"),
	
	AUTHENTICATE_AND_UPDATE_CUSTOMER("authenticate_update_customer.CUST_BLOCKED","CUST_BLOCKED"),
	
	EXTERNAL_AUTHENTICATE("external_authenticate.CUSTOMER_NOT_FOUND","CUSTOMER_NOT_FOUND");
	
	
	private String logicWihField;
	private String logic;

	private LogicConstants(String logicWihField,String logic) {
		this.logicWihField = logicWihField;
		this.logic = logic;
	}

	public String getLogicWihField() {
		return logicWihField;
	}

	public void setLogicWihField(String logicWihField) {
		this.logicWihField = logicWihField;
	}

	public String getLogic() {
		return logic;
	}

	public void setLogic(String logic) {
		this.logic = logic;
	}
	
	public static class LogicConstantsLogic {
		public static String getLogic(String logicWihField) {
			LogicConstants[] keys = LogicConstants.values();
			for (LogicConstants key : keys) {
				if (key.getLogicWihField().equals(logicWihField)) {
					return key.getLogic();
				}
			}
			return "";
		}
	}
	
	public static void main(String[] args) {
		String result = LogicConstantsLogic.getLogic("procedure.NO_MATCH");
		System.out.println(result);
	}

}
