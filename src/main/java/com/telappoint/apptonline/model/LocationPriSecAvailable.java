package com.telappoint.apptonline.model;

public class LocationPriSecAvailable extends BaseResponse {
	private Integer locationId;
	private String locationName;
	private String locationClosed; 
		
	// online
	private String locationAddress;
	private String city;
	private String state;
	private String zip;
	private String workPhone;
	private String locationErrorCode;
	private String locationClosedDisplayMessage;
	private String locationNoApptsMessage;
	private String locationGoogleMap;
	private String locationGoogleMapLink;

	// ivr
	private String timeZone;
	private String locationClosedTts;
	private String locationClosedAudio;
	private String locationNameTts;
	private String locationNameAudio;
	private String errorFlag="N";
	private String errorMessage;
	
	private boolean timeSlotsAvailable = false;
	
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public Integer getLocationId() {
		return locationId;
	}
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}
	public boolean isTimeSlotsAvailable() {
		return timeSlotsAvailable;
	}
	public void setTimeSlotsAvailable(boolean timeSlotsAvailable) {
		this.timeSlotsAvailable = timeSlotsAvailable;
	}
	public String getLocationAddress() {
		return locationAddress;
	}
	public void setLocationAddress(String locationAddress) {
		this.locationAddress = locationAddress;
	}
	public String getLocationClosed() {
		return locationClosed;
	}
	public void setLocationClosed(String locationClosed) {
		this.locationClosed = locationClosed;
	}
	public String getLocationErrorCode() {
		return locationErrorCode;
	}
	public void setLocationErrorCode(String locationErrorCode) {
		this.locationErrorCode = locationErrorCode;
	}
	public String getLocationGoogleMap() {
		return locationGoogleMap;
	}
	public void setLocationGoogleMap(String locationGoogleMap) {
		this.locationGoogleMap = locationGoogleMap;
	}
	public String getLocationGoogleMapLink() {
		return locationGoogleMapLink;
	}
	public void setLocationGoogleMapLink(String locationGoogleMapLink) {
		this.locationGoogleMapLink = locationGoogleMapLink;
	}
	public String getLocationClosedDisplayMessage() {
		return locationClosedDisplayMessage;
	}
	public void setLocationClosedDisplayMessage(String locationClosedDisplayMessage) {
		this.locationClosedDisplayMessage = locationClosedDisplayMessage;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getWorkPhone() {
		return workPhone;
	}
	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}
	public String getLocationClosedTts() {
		return locationClosedTts;
	}
	public void setLocationClosedTts(String locationClosedTts) {
		this.locationClosedTts = locationClosedTts;
	}
	public String getLocationClosedAudio() {
		return locationClosedAudio;
	}
	public void setLocationClosedAudio(String locationClosedAudio) {
		this.locationClosedAudio = locationClosedAudio;
	}
	public String getLocationNameTts() {
		return locationNameTts;
	}
	public void setLocationNameTts(String locationNameTts) {
		this.locationNameTts = locationNameTts;
	}
	public String getLocationNameAudio() {
		return locationNameAudio;
	}
	public void setLocationNameAudio(String locationNameAudio) {
		this.locationNameAudio = locationNameAudio;
	}
	public String getLocationNoApptsMessage() {
		return locationNoApptsMessage;
	}
	public void setLocationNoApptsMessage(String locationNoApptsMessage) {
		this.locationNoApptsMessage = locationNoApptsMessage;
	}
	public String getErrorFlag() {
		return errorFlag;
	}
	public void setErrorFlag(String errorFlag) {
		this.errorFlag = errorFlag;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
