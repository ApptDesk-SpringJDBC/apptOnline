package com.telappoint.apptonline.model;

public class SchedulerClosedNoFunding {
	private String schedulerClosed = "N";
	private String noFunding = "N";
	private String header;
	private String body;
	public String getSchedulerClosed() {
		return schedulerClosed;
	}
	public void setSchedulerClosed(String schedulerClosed) {
		this.schedulerClosed = schedulerClosed;
	}
	public String getNoFunding() {
		return noFunding;
	}
	public void setNoFunding(String noFunding) {
		this.noFunding = noFunding;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
}
