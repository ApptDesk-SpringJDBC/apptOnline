package com.telappoint.apptonline.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.telappoint.apptonline.model.ApptOnlineSessionData;
import com.telappoint.apptonline.model.BaseResponse;
import com.telappoint.apptonline.model.LandingPageInfo;
import com.telappoint.apptonline.model.LocationPriSecAvailable;
import com.telappoint.apptonline.model.OnlinePageData;
import com.telappoint.apptonline.model.ProcedureListResponse;
import com.telappoint.apptonline.model.ProcedureResponse;


/**
 * @author Murali G
 *
 */
public interface IApptOnlineService {
	
	public LandingPageInfo getLandingPageInfo(ApptOnlineSessionData sessionData) throws Exception;
	public OnlinePageData getOnlinePageData(ApptOnlineSessionData sessionData)throws Exception;
	public ProcedureResponse getProcedureId(ApptOnlineSessionData sessionData,String procedureName) throws Exception;
	public ProcedureListResponse getProcedure(ApptOnlineSessionData sessionData)throws Exception;
	public BaseResponse getProcedureNoMatch(ApptOnlineSessionData sessionData,String procedureValue) throws Exception;
	public LocationPriSecAvailable getLocationPrimarySecondaryAvailability(ApptOnlineSessionData sessionData, String procedure)throws Exception;
	public Map<String,Object> getOnlineNoLoginAuthenticate(ApptOnlineSessionData sessionData,HttpServletRequest request,OnlinePageData onlinePageData)throws Exception;
	public Map<String,Object> getCustomerType(ApptOnlineSessionData sessionData) throws Exception;
	public Map<String,Object> getUtilityType(ApptOnlineSessionData sessionData,String customerTypeId) throws Exception;
	public Map<String,Object> getServiceNameCustomerUtility(ApptOnlineSessionData sessionData,String customerTypeId, String utilityTypeId) throws Exception;
	public Map<String,Object> getServiceNameCustomerWarningPage(ApptOnlineSessionData sessionData, String serviceFundsRcvdId,String customerTypeId, String utilityTypeId, String serviceId)throws Exception;
	public Map<String,Object> getSingleServiceNotClosed(ApptOnlineSessionData sessionData, String serviceIds)throws Exception;
	public Map<String,Object> getServiceClosedStatus(ApptOnlineSessionData sessionData, String serviceIds)throws Exception;
	public Map<String,Object> getServiceTimeSlotsAvailableStatus(ApptOnlineSessionData sessionData, String serviceId)throws Exception;
	public Map<String,Object> holdFirstAvailableAppointment(ApptOnlineSessionData sessionData, String serviceId)throws Exception;
	public Map<String,Object> getHouseholdMonthlyIncome(ApptOnlineSessionData sessionData, String noOfPeople) throws Exception;
	public Map<String,Object> getCustomerInfo(ApptOnlineSessionData sessionData)throws Exception;
	public Map<String,Object> updateCustomerInfo(ApptOnlineSessionData sessionData,HttpServletRequest request,OnlinePageData onlinePageData)throws Exception;
	public Map<String,Object> getListOfThingsToBring(ApptOnlineSessionData sessionData)throws Exception;
	public Map<String,Object> confirmAppointment(ApptOnlineSessionData sessionData) throws Exception;
	
	public Map<String,Object> getCancelLoginAuthenticate(ApptOnlineSessionData sessionData, HttpServletRequest request,OnlinePageData onlinePageData)throws Exception;
	public Map<String,Object> getBookedAppointments(ApptOnlineSessionData sessionData)throws Exception;
	public Map<String,Object> cancelAppointment(ApptOnlineSessionData sessionData,String scheduleId) throws Exception;
	public Map<String,Object> getPageValidationMessages(ApptOnlineSessionData sessionData) throws Exception;
	public Map<String,Object> releaseHoldedAppointment(ApptOnlineSessionData sessionData) throws Exception;
	
	public Map<String,Object> getTransId(String clientCode,String uuid,String ipAddress,String callerId,String userName) throws Exception;
	public Map<String,Object> updateTransState(ApptOnlineSessionData sessionData, String pageId) throws Exception;
	
	public void sendErrorEmail(String clientCode, String requestURI, String inputData,Exception ex);
	public Map<String,Object> extendHoldApptTime(ApptOnlineSessionData sessionData) throws Exception;
	
	public Map<String, Object> checkDuplicateAppts(ApptOnlineSessionData sessionData,String serviceId) throws Exception;
	public Map<String, Object> getAvailableDatesCallcenter(ApptOnlineSessionData sessionData) throws Exception;
	public Map<String, Object> getAvailableTimesCallcenter(ApptOnlineSessionData sessionData, String apptDate)throws Exception;
	public Map<String, Object> holdAppointmentCallCenter(ApptOnlineSessionData sessionData, String apptDateTime)throws Exception;
	
	public Map<String, Object> getPledgeAmountLoginAuthenticate(ApptOnlineSessionData sessionData, HttpServletRequest request,OnlinePageData onlinePageData) throws Exception;
	public Map<String, Object> getPledgeHistory(ApptOnlineSessionData sessionData)throws Exception;
	public Map<String, Object> getPledgeAwardLetter(ApptOnlineSessionData sessionData,String customerPledgeId)throws Exception;
	
	public Map<String, Object> getLocations(ApptOnlineSessionData sessionData) throws Exception;
	public Map<String, Object> getServicesCallcenter(ApptOnlineSessionData sessionData)throws Exception;
	public Map<String, Object> authenticateAndUpdateCustomer(ApptOnlineSessionData sessionData, HttpServletRequest request,	OnlinePageData onlinePageData) throws Exception;
	
	public Map<String, Object> loginAuthenticateAndUpdateForExternal(ApptOnlineSessionData sessionData, HttpServletRequest request,OnlinePageData onlinePageData) throws Exception;
	public Map<String, Object> loginAuthenticateForExternal(ApptOnlineSessionData sessionData, HttpServletRequest request,OnlinePageData onlinePageData) throws Exception;
	public Map<String, Object> confirmAppointmentExternalLogic(ApptOnlineSessionData sessionData) throws Exception;
	public Map<String, Object> cancelAppointmentExternalLogic(ApptOnlineSessionData sessionData, String scheduleId)throws Exception;
	
	public Map<String, Object> getTransScriptMsgs(ApptOnlineSessionData sessionData,String scheduleId, String customerId) throws Exception;
	public Map<String, Object> saveTransScriptMsg(ApptOnlineSessionData sessionData,String filePath, String fileNames, String scheduleId)throws Exception;
	public Map<String, Object> deleteTransScriptMsg(ApptOnlineSessionData sessionData,String transScriptMSGId) throws Exception;
	
}
