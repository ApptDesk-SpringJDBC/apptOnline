package com.telappoint.apptonline.common;

/**
 * @author Murali G
 *
 */
public enum JSPPageNameConstants {
	
	ONLINE_APPT_LANDING_PAGE("landing"),
	ONLINE_APPT_LOGIN_PAGE("login"),
	ONLINE_APPT_ERROR_PAGE("error"),
	
	ONLINE_APPT_FRONTEND_ERROR_PAGE("error.html"),
	
	ONLINE_APPT_FRONTEND_INDEX_PAGE("../index.html"),
	
	ONLINE_APPT_PLEDGE_AWARD_LETER_PAGE("pledge_award_letter"),
	;
	
	private String viewName;

	private JSPPageNameConstants(String viewName) {
		this.viewName = viewName;
	}

	public String getViewName() {
		return viewName;
	}
}
