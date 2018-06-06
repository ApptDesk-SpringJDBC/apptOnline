package com.telappoint.apptonline.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.NON_NULL)
public class OnlineFlow {
	private int pageId;
	private String field;
	private String display;
	private String submitAPI;
	private String pageRedirect;
	private String logic1;
	private String page1;
	private String logic2;
	private String page2;
	private String logic3;
	private String page3;
	private String logic4;
	private String page4;
	private String logic5;
	private String page5;
	
	public int getPageId() {
		return pageId;
	}
	public void setPageId(int pageId) {
		this.pageId = pageId;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public String getSubmitAPI() {
		return submitAPI;
	}
	public void setSubmitAPI(String submitAPI) {
		this.submitAPI = submitAPI;
	}
	public String getPageRedirect() {
		return pageRedirect;
	}
	public void setPageRedirect(String pageRedirect) {
		this.pageRedirect = pageRedirect;
	}
	public String getLogic1() {
		return logic1;
	}
	public void setLogic1(String logic1) {
		this.logic1 = logic1;
	}
	public String getPage1() {
		return page1;
	}
	public void setPage1(String page1) {
		this.page1 = page1;
	}
	public String getLogic2() {
		return logic2;
	}
	public void setLogic2(String logic2) {
		this.logic2 = logic2;
	}
	public String getPage2() {
		return page2;
	}
	public void setPage2(String page2) {
		this.page2 = page2;
	}
	public String getLogic3() {
		return logic3;
	}
	public void setLogic3(String logic3) {
		this.logic3 = logic3;
	}
	public String getPage3() {
		return page3;
	}
	public void setPage3(String page3) {
		this.page3 = page3;
	}
	public String getLogic4() {
		return logic4;
	}
	public void setLogic4(String logic4) {
		this.logic4 = logic4;
	}
	public String getPage4() {
		return page4;
	}
	public void setPage4(String page4) {
		this.page4 = page4;
	}
	public String getLogic5() {
		return logic5;
	}
	public void setLogic5(String logic5) {
		this.logic5 = logic5;
	}
	public String getPage5() {
		return page5;
	}
	public void setPage5(String page5) {
		this.page5 = page5;
	}
}
