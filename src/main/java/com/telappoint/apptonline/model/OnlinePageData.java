package com.telappoint.apptonline.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OnlinePageData extends BaseResponse {

	private Map<String, OnlineFlow> onlineFlows = new LinkedHashMap<String,OnlineFlow>();
	private Map<String,List<OnlinePageContent>> onlinePageContentMap = new HashMap<String, List<OnlinePageContent>>();
	private Map<String, List<OnlinePageFields>> onlinePageFieldsMap = new LinkedHashMap<String, List<OnlinePageFields>>();
	private SchedulerClosedNoFunding schedulerClosedNoFunding;
	private String logoutURL;
	private Map<String,List<String>> onlinePageContentMapIds = new HashMap<String, List<String>>();

	public Map<String, List<OnlinePageFields>> getOnlinePageFieldsMap() {
		return onlinePageFieldsMap;
	}

	public void setOnlinePageFieldsMap(Map<String, List<OnlinePageFields>> onlinePageFieldsMap) {
		this.onlinePageFieldsMap = onlinePageFieldsMap;
	}

	public SchedulerClosedNoFunding getSchedulerClosedNoFunding() {
		return schedulerClosedNoFunding;
	}

	public void setSchedulerClosedNoFunding(SchedulerClosedNoFunding schedulerClosedNoFunding) {
		this.schedulerClosedNoFunding = schedulerClosedNoFunding;
	}

	public Map<String, OnlineFlow> getOnlineFlows() {
		return onlineFlows;
	}

	public void setOnlineFlows(Map<String, OnlineFlow> onlineFlows) {
		this.onlineFlows = onlineFlows;
	}
	
	public Map<String,List<OnlinePageContent>> getOnlinePageContentMap() {
		return onlinePageContentMap;
	}

	public void setOnlinePageContentMap(Map<String,List<OnlinePageContent>> onlinePageContentMap) {
		this.onlinePageContentMap = onlinePageContentMap;
	}

	public String getLogoutURL() {
		return logoutURL;
	}

	public void setLogoutURL(String logoutURL) {
		this.logoutURL = logoutURL;
	}

	public Map<String,List<String>> getOnlinePageContentMapIds() {
		return onlinePageContentMapIds;
	}

	public void setOnlinePageContentMapIds(Map<String,List<String>> onlinePageContentMapIds) {
		this.onlinePageContentMapIds = onlinePageContentMapIds;
	}
}
