package com.telappoint.apptonline.model;
import java.net.URI;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * 
 * @author Murali G
 *
 */

@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class BaseResponse {
	private boolean status = true;
	private String message;
	private URI resourceUri;
	private String transId;
	private String token;
	
	//online
	private String nextBtn;
	private String backBtn;
	private String printBtn;
	private String logoutBtn;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public URI getResourceUri() {
		return resourceUri;
	}
	public void setResourceUri(URI resourceUri) {
		this.resourceUri = resourceUri;
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
	public String getNextBtn() {
		return nextBtn;
	}
	public void setNextBtn(String nextBtn) {
		this.nextBtn = nextBtn;
	}
	public String getBackBtn() {
		return backBtn;
	}
	public void setBackBtn(String backBtn) {
		this.backBtn = backBtn;
	}
	public String getPrintBtn() {
		return printBtn;
	}
	public void setPrintBtn(String printBtn) {
		this.printBtn = printBtn;
	}
	public String getLogoutBtn() {
		return logoutBtn;
	}
	public void setLogoutBtn(String logoutBtn) {
		this.logoutBtn = logoutBtn;
	}
}
