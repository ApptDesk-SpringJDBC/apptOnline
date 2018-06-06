package com.telappoint.apptonline.model;

public class OnlinePageContent {
	
	private int pageContentId;
	private String apptMethod;
	private String header;
	private String leftSideContent;
	private String rightSideContent;
	private String script;
	
	public int getPageContentId() {
		return pageContentId;
	}
	public void setPageContentId(int pageContentId) {
		this.pageContentId = pageContentId;
	}
	
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getLeftSideContent() {
		return leftSideContent;
	}
	public void setLeftSideContent(String leftSideContent) {
		this.leftSideContent = leftSideContent;
	}
	public String getRightSideContent() {
		return rightSideContent;
	}
	public void setRightSideContent(String rightSideContent) {
		this.rightSideContent = rightSideContent;
	}
	public String getApptMethod() {
		return apptMethod;
	}
	public void setApptMethod(String apptMethod) {
		this.apptMethod = apptMethod;
	}
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
}
