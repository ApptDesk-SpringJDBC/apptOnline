package com.telappoint.apptonline.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * 
 * @author Murali G
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class LandingPageInfo extends BaseResponse {
	private String clientCode;
	private String clientName;
	private String cssFileName;
	private String logoFileName;
	private String footerContent;
	private String footerLinks;
	private String landingPageText;
	private String defaultLangCode;

	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getCssFileName() {
		return cssFileName;
	}
	public void setCssFileName(String cssFileName) {
		this.cssFileName = cssFileName;
	}
	public String getLogoFileName() {
		return logoFileName;
	}
	public void setLogoFileName(String logoFileName) {
		this.logoFileName = logoFileName;
	}
	public String getFooterContent() {
		return footerContent;
	}
	public void setFooterContent(String footerContent) {
		this.footerContent = footerContent;
	}
	public String getFooterLinks() {
		return footerLinks;
	}
	public void setFooterLinks(String footerLinks) {
		this.footerLinks = footerLinks;
	}

	public String getLandingPageText() {
		return landingPageText;
	}
	public void setLandingPageText(String landingPageText) {
		this.landingPageText = landingPageText;
	}
	public String getDefaultLangCode() {
		return defaultLangCode;
	}
	public void setDefaultLangCode(String defaultLangCode) {
		this.defaultLangCode = defaultLangCode;
	}
}
