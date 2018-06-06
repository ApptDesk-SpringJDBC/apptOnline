package com.telappoint.apptonline.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.telappoint.apptonline.common.CommonContants;
import com.telappoint.apptonline.common.RestClientConstants;
import com.telappoint.apptonline.model.ApptOnlineSessionData;
import com.telappoint.apptonline.model.BaseResponse;
import com.telappoint.apptonline.model.LandingPageInfo;
import com.telappoint.apptonline.model.LocationPriSecAvailable;
import com.telappoint.apptonline.model.OnlinePageData;
import com.telappoint.apptonline.model.OnlinePageFields;
import com.telappoint.apptonline.model.ProcedureListResponse;
import com.telappoint.apptonline.model.ProcedureResponse;
import com.telappoint.apptonline.model.ResponseModel;
import com.telappoint.apptonline.restclient.ApptOnlineRestClient;
import com.telappoint.apptonline.service.IApptOnlineService;
import com.telappoint.apptonline.utils.ApptOnlineUtils;
import com.telappoint.apptonline.utils.EmailComponent;
import com.telappoint.apptonline.utils.FileUtils;

/**
 *  @author Murali
 */

@Service
public class ApptOnlineServiceImpl implements IApptOnlineService {

	private Logger logger = Logger.getLogger(ApptOnlineServiceImpl.class);

	@Autowired
	private EmailComponent emailComponent;

	@Override
	public LandingPageInfo getLandingPageInfo(ApptOnlineSessionData sessionData) throws Exception {
		String endPointUrl = ApptOnlineUtils.populateBasicRequestParamsDetails(sessionData, RestClientConstants.REST_SERVICE_GET_CLIENT_INFO.getValue());
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("getLandingPageInfo : endPointUrl :  "+endPointUrl);
		
		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		Gson gson = new GsonBuilder().create();
		String response = gson.toJson(responseModel.getData());
		logger.debug("getLandingPageInfo : response :  "+response);
		LandingPageInfo landingPageInfo = gson.fromJson(response, new TypeToken<LandingPageInfo>() {
			private static final long serialVersionUID = 1L;
		}.getType());
		return landingPageInfo;
	}

	@Override
	public OnlinePageData getOnlinePageData(ApptOnlineSessionData sessionData) throws Exception {
		String endPointUrl = ApptOnlineUtils.populateBasicRequestParamsDetails(sessionData, RestClientConstants.REST_SERVICE_GET_ONLINE_PAGE_DATA.getValue());
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("getOnlinePageData : endPointUrl :  "+endPointUrl);
		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		Gson gson = new GsonBuilder().create();
		String response = gson.toJson(responseModel.getData());
		logger.debug("getOnlinePageData : response :  "+response);
		OnlinePageData onlinePageData = gson.fromJson(response, new TypeToken<OnlinePageData>() {
			private static final long serialVersionUID = 1L;
		}.getType());
		return onlinePageData;
	}

	@Override
	public ProcedureResponse getProcedureId(ApptOnlineSessionData sessionData,String procedureName) throws Exception {
		String endPointUrl = ApptOnlineUtils.populateBasicRequestParamsDetails(sessionData, RestClientConstants.REST_SERVICE_GET_PROCEDURE_ID.getValue());
		endPointUrl = endPointUrl.replaceAll("@procedureNameParam@",procedureName);
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("getProcedureId : endPointUrl :  "+endPointUrl);
		
		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		Gson gson = new GsonBuilder().create();
		String response = gson.toJson(responseModel.getData());
		logger.debug("getProcedureId : response :  "+response);
		ProcedureResponse proResponse = gson.fromJson(response, new TypeToken<ProcedureResponse>() {
			private static final long serialVersionUID = 1L;
		}.getType());
		return proResponse;
	}

	@Override
	public ProcedureListResponse getProcedure(ApptOnlineSessionData sessionData) throws Exception {
		String endPointUrl = ApptOnlineUtils.populateBasicRequestParamsDetails(sessionData, RestClientConstants.REST_SERVICE_GET_PROCEDURE.getValue());
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("getProcedure : endPointUrl :  "+endPointUrl);
		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		Gson gson = new GsonBuilder().create();
		String response = gson.toJson(responseModel.getData());
		logger.debug("getProcedure : response :  "+response);
		ProcedureListResponse pocedure = gson.fromJson(response, new TypeToken<ProcedureListResponse>() {
			private static final long serialVersionUID = 1L;
		}.getType());
		return pocedure;
	}

	@Override
	public BaseResponse getProcedureNoMatch(ApptOnlineSessionData sessionData,String procedureValue) throws Exception {
		String endPointUrl = ApptOnlineUtils.populateBasicRequestParamsDetails(sessionData, RestClientConstants.REST_SERVICE_GET_PROCEDURE_NO_MATCH.getValue());
		endPointUrl = endPointUrl.replaceAll("@procedureValueParam@",procedureValue);
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("getProcedureNoMatch : endPointUrl :  "+endPointUrl);
		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		Gson gson = new GsonBuilder().create();
		String response = gson.toJson(responseModel.getData());
		logger.debug("getProcedureNoMatch : response :  "+response);
		BaseResponse baseResponse = gson.fromJson(response, new TypeToken<BaseResponse>() {
			private static final long serialVersionUID = 1L;
		}.getType());
		return baseResponse;
	}

	@Override
	public LocationPriSecAvailable getLocationPrimarySecondaryAvailability(ApptOnlineSessionData sessionData,String procedureId) throws Exception {
		String endPointUrl = ApptOnlineUtils.populateBasicRequestParamsDetails(sessionData, RestClientConstants.REST_SERVICE_GET_LOCATION_PRIMARY_SECONDARY_AVAILABILITY.getValue());
		endPointUrl = endPointUrl.replaceAll("@procedureIdParam@",procedureId);
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("getLocationPrimarySecondaryAvailability ::::::::::: endPointUrl :  "+endPointUrl);
		
		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		Gson gson = new GsonBuilder().create();
		String response = gson.toJson(responseModel.getData());
		logger.debug("getLocationPrimarySecondaryAvailability : response :  "+response);
		LocationPriSecAvailable locPriSecAvailable = gson.fromJson(response, new TypeToken<LocationPriSecAvailable>() {
			private static final long serialVersionUID = 1L;
		}.getType());
		return locPriSecAvailable;		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> getOnlineNoLoginAuthenticate(ApptOnlineSessionData sessionData,HttpServletRequest request,OnlinePageData onlinePageData) throws Exception {
		List<OnlinePageFields> pageFieldsList = ApptOnlineUtils.getCurrentPageFieldsList(sessionData,onlinePageData);
		String loginParams = ApptOnlineUtils.getFormData(request,pageFieldsList);

		String endPointUrl = FileUtils.getEndPointURL() + RestClientConstants.REST_SERVICE_GET_ONLINE_NO_LOGIN_AUTHENTICATE.getValue();
		logger.debug("getOnlineNoLoginAuthenticate : endPointUrl :  "+endPointUrl);
		Map<String,Object> jsonMap = new HashMap<String,Object>();
		ApptOnlineUtils.populateBasicRequestData(sessionData, jsonMap);
		jsonMap.put("inputParamValues",loginParams);
		String  jsonString = ApptOnlineUtils.getJSONString(jsonMap);
		logger.debug("jsonString ::::::::: "+jsonString);

		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performPOSTRequest(endPointUrl,jsonString);
		logger.debug("getOnlineNoLoginAuthenticate : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> getCustomerType(ApptOnlineSessionData sessionData) throws Exception {
		String endPointUrl = ApptOnlineUtils.populateBasicRequestParamsDetails(sessionData, RestClientConstants.REST_SERVICE_GET_CUSTOMER_TYPE.getValue());
		endPointUrl = endPointUrl.replaceAll("@customerIdParam@",String.valueOf(sessionData.getCustomerId()));
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("getCustomerType : endPointUrl :  "+endPointUrl);
		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		logger.debug("getCustomerType : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> getUtilityType(ApptOnlineSessionData sessionData,String customerTypeId) throws Exception {
		String endPointUrl = ApptOnlineUtils.populateBasicRequestParamsDetails(sessionData, RestClientConstants.REST_SERVICE_GET_UTILITY_TYPE.getValue());
		endPointUrl = endPointUrl.replaceAll("@serviceFundsRcvdIdParam@",String.valueOf(ApptOnlineUtils.getValueIfNotNullOtherwisegetDefaultValue(sessionData.getJsonData(),"serviceFundsRcvdId", Integer.class,"")));
		endPointUrl = endPointUrl.replaceAll("@customerTypeIdParam@",customerTypeId);
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("getUtilityType : endPointUrl :  "+endPointUrl);
		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		logger.debug("getUtilityType : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> getServiceNameCustomerUtility(ApptOnlineSessionData sessionData,String customerTypeId,String utilityTypeId) throws Exception {
		String endPointUrl = ApptOnlineUtils.populateBasicRequestParamsDetails(sessionData,RestClientConstants.REST_SERVICE_GET_SERVICE_NAME_CUSTOMER_UTILITY.getValue());
		endPointUrl = endPointUrl.replaceAll("@serviceFundsRcvdIdParam@",String.valueOf(ApptOnlineUtils.getValueIfNotNullOtherwisegetDefaultValue(sessionData.getJsonData(),"serviceFundsRcvdId", Integer.class,"")));
		endPointUrl = endPointUrl.replaceAll("@customerTypeIdParam@",customerTypeId);
		endPointUrl = endPointUrl.replaceAll("@utilityTypeIdParam@",utilityTypeId);
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("getServiceNameCustomerUtility : endPointUrl :  "+endPointUrl);
		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		logger.debug("getServiceNameCustomerUtility : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> getServiceNameCustomerWarningPage(ApptOnlineSessionData sessionData,String serviceFundsRcvdId,String customerTypeId,String utilityTypeId,String serviceId) throws Exception {
		String endPointUrl = ApptOnlineUtils.populateBasicRequestParamsDetails(sessionData,RestClientConstants.REST_SERVICE_GET_SERVICE_NAME_CUSTOMER_WARNING_PAGE.getValue());
		endPointUrl = endPointUrl.replaceAll("@serviceFundsRcvdIdParam@",serviceFundsRcvdId);
		endPointUrl = endPointUrl.replaceAll("@customerTypeIdParam@",customerTypeId);
		endPointUrl = endPointUrl.replaceAll("@utilityTypeIdParam@",utilityTypeId);
		endPointUrl = endPointUrl.replaceAll("@serviceIdParam@",serviceId);
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("getServiceNameCustomerWarningPage : endPointUrl :  "+endPointUrl);

		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		logger.debug("getServiceNameCustomerWarningPage : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> getSingleServiceNotClosed(ApptOnlineSessionData sessionData,String serviceIds) throws Exception {
		String endPointUrl = ApptOnlineUtils.populateBasicRequestParamsDetails(sessionData,RestClientConstants.REST_SERVICE_GET_SINGLE_SERVICE_NOT_CLOSED.getValue());
		endPointUrl = endPointUrl.replaceAll("@serviceIdsParam@",serviceIds);
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("getSingleServiceNotClosed : endPointUrl :  "+endPointUrl);

		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		logger.debug("getSingleServiceNotClosed : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> getServiceClosedStatus(ApptOnlineSessionData sessionData,String serviceId) throws Exception {
		String endPointUrl = ApptOnlineUtils.populateBasicRequestParamsDetails(sessionData,RestClientConstants.REST_SERVICE_GET_SERVICE_CLOSED_STATUS.getValue());
		endPointUrl = endPointUrl.replaceAll("@serviceIdParam@",serviceId);
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("getServiceClosedStatus : endPointUrl :  "+endPointUrl);

		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		logger.debug("getServiceClosedStatus : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> getServiceTimeSlotsAvailableStatus(ApptOnlineSessionData sessionData,String serviceId) throws Exception {
		String endPointUrl = ApptOnlineUtils.populateBasicRequestParamsDetails(sessionData,RestClientConstants.REST_SERVICE_GET_SERVICE_TIME_SLOTS_AVAIL_STATUS.getValue());
		endPointUrl = endPointUrl.replaceAll("@locationIdParam@",sessionData.getLocationId());
		endPointUrl = endPointUrl.replaceAll("@serviceIdParam@",serviceId);
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("getServiceTimeSlotsAvailableStatus : endPointUrl :  "+endPointUrl);

		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		logger.debug("getServiceTimeSlotsAvailableStatus : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> holdFirstAvailableAppointment(ApptOnlineSessionData sessionData, String serviceId) throws Exception {
		String endPointUrl = FileUtils.getEndPointURL() + RestClientConstants.REST_SERVICE_HOLD_FIRST_AVAILABLE_APPT.getValue();
		logger.debug("holdFirstAvailableAppointment : endPointUrl :  "+endPointUrl);
		Map<String,Object> jsonMap = new HashMap<String,Object>();
		ApptOnlineUtils.populateBasicRequestData(sessionData, jsonMap);

		jsonMap.put("customerId",sessionData.getCustomerId());
		jsonMap.put("companyId",sessionData.getCompanyId());
		jsonMap.put("procedureId",sessionData.getProcedureId());
		jsonMap.put("locationId",sessionData.getLocationId());
		jsonMap.put("departmentId",sessionData.getDepartmentId());
		jsonMap.put("resourceId",sessionData.getResourceId());
		jsonMap.put("serviceId",serviceId);

		String  jsonString = ApptOnlineUtils.getJSONString(jsonMap);
		logger.debug("jsonString ::: REQUEST DATA :::::: "+jsonString);

		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performPOSTRequest(endPointUrl,jsonString);
		logger.debug(" holdFirstAvailableAppointment :::: response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> releaseHoldedAppointment(ApptOnlineSessionData sessionData) throws Exception {
		String endPointUrl = ApptOnlineUtils.populateBasicRequestParamsDetails(sessionData,RestClientConstants.REST_SERVICE_RELEASE_HOLDED_APPT.getValue());
		endPointUrl = endPointUrl.replaceAll("@scheduleIdParam@",sessionData.getScheduleId()!=null ? sessionData.getScheduleId() : "-1");
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("releaseHoldedAppointment : endPointUrl :  "+endPointUrl);

		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		logger.debug("releaseHoldedAppointment : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> getHouseholdMonthlyIncome(ApptOnlineSessionData sessionData,String noOfPeople) throws Exception {
		String endPointUrl = ApptOnlineUtils.populateBasicRequestParamsDetails(sessionData,RestClientConstants.REST_SERVICE_GET_HOUSE_HOLD_MONTHLY_INCOME.getValue());
		endPointUrl = endPointUrl.replaceAll("@noOfPeopleParam@",noOfPeople);
		endPointUrl = endPointUrl.replaceAll("@serviceIdParam@",sessionData.getServiceId());
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("getHouseholdMonthlyIncome : endPointUrl :  "+endPointUrl);

		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		logger.debug("getHouseholdMonthlyIncome : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> getCustomerInfo(ApptOnlineSessionData sessionData) throws Exception {
		String endPointUrl = ApptOnlineUtils.populateBasicRequestParamsDetails(sessionData,RestClientConstants.REST_SERVICE_GET_CUSTOMER_INFO.getValue());
		endPointUrl = endPointUrl.replaceAll("@customerIdParam@",String.valueOf(sessionData.getCustomerId()));
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("getCustomerInfo : endPointUrl :  "+endPointUrl);

		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		logger.debug("getCustomerInfo : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> updateCustomerInfo(ApptOnlineSessionData sessionData,HttpServletRequest request,OnlinePageData onlinePageData) throws Exception {
		List<OnlinePageFields> pageFieldsList = ApptOnlineUtils.getCurrentPageFieldsList(sessionData,onlinePageData);
		String loginParams = ApptOnlineUtils.getFormData(request,pageFieldsList);

		String endPointUrl = FileUtils.getEndPointURL() + RestClientConstants.REST_SERVICE_UPDATE_CUSTOMER_INFO.getValue();
		logger.debug("updateCustomerInfo : endPointUrl :  "+endPointUrl);
		Map<String,Object> jsonMap = new HashMap<String,Object>();
		ApptOnlineUtils.populateBasicRequestData(sessionData, jsonMap);
		jsonMap.put("inputParamValues",loginParams);
		jsonMap.put("customerId",sessionData.getCustomerId());
		String  jsonString = ApptOnlineUtils.getJSONString(jsonMap);
		logger.debug("jsonString ::::::::: "+jsonString);
		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performPOSTRequest(endPointUrl,jsonString);		
		logger.debug("updateCustomerInfo : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> getListOfThingsToBring(ApptOnlineSessionData sessionData) throws Exception {
		String endPointUrl = ApptOnlineUtils.populateBasicRequestParamsDetails(sessionData,RestClientConstants.REST_SERVICE_GET_LIST_OF_THINGS_TO_BRING.getValue());
		endPointUrl = endPointUrl.replaceAll("@serviceIdParam@",sessionData.getServiceId());
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("getListOfThingsToBring : endPointUrl :  "+endPointUrl);

		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		logger.debug("getListOfThingsToBring : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> confirmAppointment(ApptOnlineSessionData sessionData) throws Exception {
		String endPointUrl = FileUtils.getEndPointURL() + RestClientConstants.REST_SERVICE_CONFIRM_APPT.getValue();
		logger.debug("confirmAppointment : endPointUrl :  "+endPointUrl);
		Map<String,Object> jsonMap = new HashMap<String,Object>();
		ApptOnlineUtils.populateBasicRequestData(sessionData, jsonMap);		
		jsonMap.put("scheduleId",sessionData.getScheduleId());

		String  jsonString = ApptOnlineUtils.getJSONString(jsonMap);
		logger.debug("jsonString ::::::::: "+jsonString);

		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performPOSTRequest(endPointUrl,jsonString);
		logger.debug("confirmAppointment : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> getCancelLoginAuthenticate(ApptOnlineSessionData sessionData,HttpServletRequest request,OnlinePageData onlinePageData) throws Exception {
		String endPointUrl = ApptOnlineUtils.populateBasicRequestParamsDetails(sessionData,RestClientConstants.REST_SERVICE_GET_CANCEL_LOGIN_AUTHENTICATE.getValue());
		endPointUrl = endPointUrl+"&inputParamValues="+ApptOnlineUtils.getFormData(request,ApptOnlineUtils.getCurrentPageFieldsList(sessionData,onlinePageData));
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("getCancelLoginAuthenticate : endPointUrl :  "+endPointUrl);

		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		logger.debug("getCancelLoginAuthenticate : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> getBookedAppointments(ApptOnlineSessionData sessionData) throws Exception {
		String endPointUrl = ApptOnlineUtils.populateBasicRequestParamsDetails(sessionData,RestClientConstants.REST_SERVICE_GET_BOOKED_APPTS.getValue());
		endPointUrl = endPointUrl.replaceAll("@customerIdParam@",String.valueOf(sessionData.getCustomerId()));
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("getBookedAppointments : endPointUrl :  "+endPointUrl);

		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		logger.debug("getBookedAppointments : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> cancelAppointment(ApptOnlineSessionData sessionData,String scheduleId) throws Exception {
		String endPointUrl = ApptOnlineUtils.populateBasicRequestParamsDetails(sessionData,RestClientConstants.REST_SERVICE_CANCEL_APPT.getValue());
		endPointUrl = endPointUrl.replaceAll("@scheduleIdParam@",scheduleId);
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("cancelAppointment : endPointUrl :  "+endPointUrl);

		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		logger.debug("cancelAppointment : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> getPageValidationMessages(ApptOnlineSessionData sessionData) throws Exception {
		String endPointUrl = ApptOnlineUtils.populateBasicRequestParamsDetails(sessionData,RestClientConstants.REST_SERVICE_GET_VALIDATION_MESSAGES.getValue());
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("getPageValidationMessages : endPointUrl :  "+endPointUrl);

		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		logger.debug("getPageValidationMessages : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> getTransId(String clientCode,String uuid,String ipAddress,String callerId,String userName) throws Exception {
		String endPointUrl = FileUtils.getEndPointURL() + RestClientConstants.REST_SERVICE_GET_TRANS_ID.getValue();
		endPointUrl = endPointUrl.replaceAll("@clientCodeParam@",clientCode);
		endPointUrl = endPointUrl.replaceAll("@deviceParam@",CommonContants.ONLINE.getValue());
		endPointUrl = endPointUrl.replaceAll("@uuidParam@",uuid);
		endPointUrl = endPointUrl.replaceAll("@ipAddressParam@",ipAddress);
		endPointUrl = endPointUrl.replaceAll("@callerIdParam@",callerId);
		endPointUrl = endPointUrl.replaceAll("@userNameParam@",userName);
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("getTransId : endPointUrl :  "+endPointUrl);
		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		logger.debug("getTransId : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> updateTransState(ApptOnlineSessionData sessionData, String pageId) throws Exception {
		String endPointUrl = FileUtils.getEndPointURL() + RestClientConstants.REST_SERVICE_UPDATE_TRANS_STATE.getValue();
		endPointUrl = endPointUrl.replaceAll("@clientCodeParam@",sessionData.getClientCode());
		endPointUrl = endPointUrl.replaceAll("@deviceParam@",CommonContants.ONLINE.getValue());
		endPointUrl = endPointUrl.replaceAll("@transIdParam@",sessionData.getTransId());
		endPointUrl = endPointUrl.replaceAll("@pageIdParam@",pageId);
		String scheduleId = "1"; //As per Anantha discussion
		if(sessionData.getScheduleId()!=null && sessionData.getScheduleId()!=""){
			try{
				if(Integer.parseInt(sessionData.getScheduleId())>1){
					scheduleId = sessionData.getScheduleId();
				}
			}catch(Exception e){
				scheduleId = "1";
			}
		}
		endPointUrl = endPointUrl.replaceAll("@scheduleIdParam@",scheduleId);
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("updateTransState : endPointUrl :  "+endPointUrl);
		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		logger.debug("updateTransState : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}	
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> extendHoldApptTime(ApptOnlineSessionData sessionData) throws Exception {
		Map<String,Object> responseMap = new HashMap<String,Object>();
		if(sessionData.getScheduleId()!=null && sessionData.getScheduleId()!=""){
			try{
				if(Integer.parseInt(sessionData.getScheduleId())>1){ //As per Anantha discussion
					try{
						String endPointUrl = FileUtils.getEndPointURL() + RestClientConstants.REST_SERVICE_EXTEND_HOLDED_APPT_TIME.getValue();
						endPointUrl = endPointUrl.replaceAll("@clientCodeParam@",sessionData.getClientCode());
						endPointUrl = endPointUrl.replaceAll("@deviceParam@",CommonContants.ONLINE.getValue());
						endPointUrl = endPointUrl.replaceAll("@transIdParam@",sessionData.getTransId());
						endPointUrl = endPointUrl.replaceAll("@scheduleIdParam@",sessionData.getScheduleId());
						endPointUrl = endPointUrl.replaceAll(" ", "%20");
						logger.debug("extendHoldApptTime : endPointUrl :  "+endPointUrl);
						ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
						logger.debug("extendHoldApptTime : response :  "+responseModel.getData());
						return (LinkedHashMap<String,Object>)responseModel.getData();
					}catch(Exception e){
						logger.error("Error :::::  "+e.getMessage(),e);
						responseMap.put("status",false);
					}
				}				
			}catch(Exception e){
				//nothing to do.
				responseMap.put("status",true);
			}
		} else {
			responseMap.put("status",true);
		}
		return responseMap;
	}	
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> checkDuplicateAppts(ApptOnlineSessionData sessionData, String serviceId) throws Exception {
		String endPointUrl = FileUtils.getEndPointURL() + RestClientConstants.REST_SERVICE_CHECK_DUPLICATE_APPTS.getValue();
		endPointUrl = endPointUrl.replaceAll("@clientCodeParam@",sessionData.getClientCode());
		endPointUrl = endPointUrl.replaceAll("@deviceParam@",CommonContants.ONLINE.getValue());
		endPointUrl = endPointUrl.replaceAll("@transIdParam@",sessionData.getTransId());
		endPointUrl = endPointUrl.replaceAll("@customerIdParam@",String.valueOf(sessionData.getCustomerId()));
		endPointUrl = endPointUrl.replaceAll("@serviceIdParam@",serviceId);
		endPointUrl = endPointUrl.replaceAll("@langCodeParam@",sessionData.getLangCode());
		
		logger.debug("checkDuplicateAppts : endPointUrl :  "+endPointUrl);
		
		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		logger.debug(" checkDuplicateAppts :::: response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> getAvailableDatesCallcenter(ApptOnlineSessionData sessionData) throws Exception {
		String endPointUrl = FileUtils.getEndPointURL() + RestClientConstants.REST_SERVICE_GET_AVAIL_DATES_CALL_CENTER.getValue();
		endPointUrl = endPointUrl.replaceAll("@clientCodeParam@",sessionData.getClientCode());
		endPointUrl = endPointUrl.replaceAll("@deviceParam@",CommonContants.ONLINE.getValue());
		endPointUrl = endPointUrl.replaceAll("@transIdParam@",sessionData.getTransId());
		endPointUrl = endPointUrl.replaceAll("@locationIdParam@",sessionData.getLocationId());
		endPointUrl = endPointUrl.replaceAll("@departmentIdParam@", (!"".equals(sessionData.getDepartmentId())) ? sessionData.getDepartmentId() : "1" );
		endPointUrl = endPointUrl.replaceAll("@serviceIdParam@",sessionData.getServiceId());
		
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("getAvailableDatesCallcenter : endPointUrl :  "+endPointUrl);
		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		logger.debug("getAvailableDatesCallcenter : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> getAvailableTimesCallcenter(ApptOnlineSessionData sessionData,String apptDate) throws Exception {
		String endPointUrl = FileUtils.getEndPointURL() + RestClientConstants.REST_SERVICE_GET_AVAIL_TIMES_CALL_CENTER.getValue();
		endPointUrl = endPointUrl.replaceAll("@clientCodeParam@",sessionData.getClientCode());
		endPointUrl = endPointUrl.replaceAll("@deviceParam@",CommonContants.ONLINE.getValue());
		endPointUrl = endPointUrl.replaceAll("@transIdParam@",sessionData.getTransId());
		endPointUrl = endPointUrl.replaceAll("@locationIdParam@",sessionData.getLocationId());
		endPointUrl = endPointUrl.replaceAll("@departmentIdParam@",(!"".equals(sessionData.getDepartmentId())) ? sessionData.getDepartmentId() : "1");
		endPointUrl = endPointUrl.replaceAll("@serviceIdParam@",sessionData.getServiceId());
		endPointUrl = endPointUrl.replaceAll("@availDateParam@",apptDate);		
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("getAvailableTimesCallcenter : endPointUrl :  "+endPointUrl);
		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		logger.debug("getAvailableTimesCallcenter : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> holdAppointmentCallCenter(ApptOnlineSessionData sessionData,String apptDateTime) throws Exception {
		String endPointUrl = FileUtils.getEndPointURL() + RestClientConstants.REST_SERVICE_HOLD_APPT_CALL_CENTER.getValue();
		endPointUrl = endPointUrl.replaceAll("@clientCodeParam@",sessionData.getClientCode());
		endPointUrl = endPointUrl.replaceAll("@deviceParam@",CommonContants.ONLINE.getValue());
		endPointUrl = endPointUrl.replaceAll("@transIdParam@",sessionData.getTransId());
		
		endPointUrl = endPointUrl.replaceAll("@locationIdParam@",sessionData.getLocationId());
		endPointUrl = endPointUrl.replaceAll("@departmentIdParam@",(!"".equals(sessionData.getDepartmentId())) ? sessionData.getDepartmentId() : "1");
		endPointUrl = endPointUrl.replaceAll("@serviceIdParam@",sessionData.getServiceId());
		endPointUrl = endPointUrl.replaceAll("@customerIdParam@",String.valueOf(sessionData.getCustomerId()));
		endPointUrl = endPointUrl.replaceAll("@apptDateTimeParam@",apptDateTime);
		endPointUrl = endPointUrl.replaceAll("@procedureIdParam@",(!"".equals(sessionData.getProcedureId())) ? sessionData.getProcedureId() : "1");
		
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("holdAppointmentCallCenter : endPointUrl :  "+endPointUrl);
		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		logger.debug("holdAppointmentCallCenter : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> getPledgeAmountLoginAuthenticate(ApptOnlineSessionData sessionData,HttpServletRequest request,OnlinePageData onlinePageData) throws Exception {
		String endPointUrl = ApptOnlineUtils.populateBasicRequestParamsDetails(sessionData,RestClientConstants.REST_SERVICE_GET_PLEDGE_AMT_LOGIN_AUTHENTICATE.getValue());
		endPointUrl = endPointUrl+"&inputParamValues="+ApptOnlineUtils.getFormData(request,ApptOnlineUtils.getCurrentPageFieldsList(sessionData,onlinePageData));
		logger.debug("getPledgeAmountLoginAuthenticate : endPointUrl :  "+endPointUrl);

		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		logger.debug("getPledgeAmountLoginAuthenticate : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> getPledgeHistory(ApptOnlineSessionData sessionData) throws Exception {
		String endPointUrl = ApptOnlineUtils.populateBasicRequestParamsDetails(sessionData,RestClientConstants.REST_SERVICE_GET_PLEDGE_AMT_DETAILS.getValue());
		endPointUrl = endPointUrl.replaceAll("@customerIdParam@",String.valueOf(sessionData.getCustomerId()));
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("getPledgeHistory : endPointUrl :  "+endPointUrl);

		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		logger.debug("getPledgeHistory : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> getPledgeAwardLetter(ApptOnlineSessionData sessionData,String customerPledgeId) throws Exception {
		String endPointUrl = ApptOnlineUtils.populateBasicRequestParamsDetails(sessionData,RestClientConstants.REST_SERVICE_GET_PLEDGE_AWARD_LETER.getValue());
		endPointUrl = endPointUrl.replaceAll("@customerPledgeIdParam@",customerPledgeId);
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("getPledgeAwardLetter : endPointUrl :  "+endPointUrl);

		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		logger.debug("getPledgeAwardLetter : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> getLocations(ApptOnlineSessionData sessionData) throws Exception {
		String endPointUrl = ApptOnlineUtils.populateBasicRequestParamsDetails(sessionData,RestClientConstants.REST_SERVICE_GET_LOCATIONS.getValue());
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("getLocations : endPointUrl :  "+endPointUrl);

		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		logger.debug("getLocations : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> getServicesCallcenter(ApptOnlineSessionData sessionData) throws Exception {
		String endPointUrl = ApptOnlineUtils.populateBasicRequestParamsDetails(sessionData,RestClientConstants.REST_SERVICE_GET_SERVICES_CALL_CENTER.getValue());
		endPointUrl = endPointUrl.replaceAll("@locationIdParam@",sessionData.getLocationId());
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("getServicesCallcenter : endPointUrl :  "+endPointUrl);

		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		logger.debug("getServicesCallcenter : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> authenticateAndUpdateCustomer(ApptOnlineSessionData sessionData,HttpServletRequest request,OnlinePageData onlinePageData) throws Exception {
		List<OnlinePageFields> pageFieldsList = ApptOnlineUtils.getCurrentPageFieldsList(sessionData,onlinePageData);
		String loginParams = ApptOnlineUtils.getFormData(request,pageFieldsList);

		String endPointUrl = FileUtils.getEndPointURL() + RestClientConstants.REST_SERVICE_AUTHENTICATE_AND_UPDATE_CUSTOMER.getValue();
		logger.debug("authenticateAndUpdateCustomer : endPointUrl :  "+endPointUrl);
		Map<String,Object> jsonMap = new HashMap<String,Object>();
		ApptOnlineUtils.populateBasicRequestData(sessionData, jsonMap);
		jsonMap.put("inputParamValues",loginParams);
		String  jsonString = ApptOnlineUtils.getJSONString(jsonMap);
		logger.debug("jsonString ::::::::: "+jsonString);

		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performPOSTRequest(endPointUrl,jsonString);
		logger.debug("authenticateAndUpdateCustomer : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> loginAuthenticateAndUpdateForExternal(ApptOnlineSessionData sessionData,HttpServletRequest request,OnlinePageData onlinePageData) throws Exception {
		List<OnlinePageFields> pageFieldsList = ApptOnlineUtils.getCurrentPageFieldsList(sessionData,onlinePageData);
		String loginParams = ApptOnlineUtils.getFormData(request,pageFieldsList);

		String endPointUrl = FileUtils.getEndPointURL() + RestClientConstants.REST_SERVICE_LOGIN_AUTHENTICATE_AND_UPDATE_FOR_EXTERNAL_LOGIN.getValue();
		logger.debug("loginAuthenticateAndUpdateForExternal : endPointUrl :  "+endPointUrl);
		Map<String,Object> jsonMap = new HashMap<String,Object>();
		ApptOnlineUtils.populateBasicRequestData(sessionData, jsonMap);
		jsonMap.put("locationId", sessionData.getLocationId());
		jsonMap.put("serviceId", sessionData.getServiceId());
		jsonMap.put("inputParamValues",loginParams);
		String  jsonString = ApptOnlineUtils.getJSONString(jsonMap);
		logger.debug("jsonString ::::::::: "+jsonString);

		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performPOSTRequest(endPointUrl,jsonString);
		logger.debug("loginAuthenticateAndUpdateForExternal : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> loginAuthenticateForExternal(ApptOnlineSessionData sessionData,HttpServletRequest request,OnlinePageData onlinePageData) throws Exception {
		List<OnlinePageFields> pageFieldsList = ApptOnlineUtils.getCurrentPageFieldsList(sessionData,onlinePageData);
		String loginParams = ApptOnlineUtils.getFormData(request,pageFieldsList);

		String endPointUrl = FileUtils.getEndPointURL() + RestClientConstants.REST_SERVICE_LOGIN_AUTHENTICATE_FOR_EXTERNAL_LOGIN.getValue();
		logger.debug("loginAuthenticateForExternal : endPointUrl :  "+endPointUrl);
		Map<String,Object> jsonMap = new HashMap<String,Object>();
		ApptOnlineUtils.populateBasicRequestData(sessionData, jsonMap);
		jsonMap.put("inputParamValues",loginParams);
		String  jsonString = ApptOnlineUtils.getJSONString(jsonMap);
		logger.debug("jsonString ::::::::: "+jsonString);

		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performPOSTRequest(endPointUrl,jsonString);
		logger.debug("loginAuthenticateForExternal : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> confirmAppointmentExternalLogic(ApptOnlineSessionData sessionData) throws Exception {
		String endPointUrl = FileUtils.getEndPointURL() + RestClientConstants.REST_SERVICE_CONFIRM_APPT_FOR_EXTERNAL_LOGIN.getValue();
		logger.debug("confirmAppointmentExternalLogic : endPointUrl :  "+endPointUrl);
		Map<String,Object> jsonMap = new HashMap<String,Object>();
		ApptOnlineUtils.populateBasicRequestData(sessionData, jsonMap);		
		jsonMap.put("scheduleId",sessionData.getScheduleId());

		String  jsonString = ApptOnlineUtils.getJSONString(jsonMap);
		logger.debug("jsonString ::::::::: "+jsonString);

		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performPOSTRequest(endPointUrl,jsonString);
		logger.debug("confirmAppointmentExternalLogic : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> cancelAppointmentExternalLogic(ApptOnlineSessionData sessionData,String scheduleId) throws Exception {
		String endPointUrl = ApptOnlineUtils.populateBasicRequestParamsDetails(sessionData,RestClientConstants.REST_SERVICE_CANCEL_APPT_FOR_EXTERNAL_LOGIN.getValue());
		endPointUrl = endPointUrl.replaceAll("@scheduleIdParam@",scheduleId);
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("cancelAppointmentExternalLogic : endPointUrl :  "+endPointUrl);

		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		logger.debug("cancelAppointmentExternalLogic : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> saveTransScriptMsg(ApptOnlineSessionData sessionData,String filePath,String fileNames,String scheduleId) throws Exception {
		String endPointUrl = FileUtils.getEndPointURL() + RestClientConstants.REST_SERVICE_SAVE_TRANS_SCRIPT_MSG.getValue();
		logger.debug("saveTransScriptMsg : endPointUrl :  "+endPointUrl);
		
		Map<String,Object> jsonMap = new HashMap<String,Object>();
		ApptOnlineUtils.populateBasicRequestData(sessionData, jsonMap);		
		jsonMap.put("scheduleId",scheduleId);
		jsonMap.put("customerId",sessionData.getCustomerId());
		jsonMap.put("displayFlag","Y");	
		jsonMap.put("filePath",filePath);
		jsonMap.put("fileName",fileNames.split(",")); 

		ObjectMapper mapper = new ObjectMapper();
		String  jsonString = mapper.writeValueAsString(jsonMap);
		logger.debug("jsonString ::::::::: "+jsonString);
		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performPOSTRequest(endPointUrl,jsonString);
		logger.debug("saveTransScriptMsg : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> getTransScriptMsgs(ApptOnlineSessionData sessionData,String scheduleId,String customerId) throws Exception {
		String endPointUrl = ApptOnlineUtils.populateBasicRequestParamsDetails(sessionData,RestClientConstants.REST_SERVICE_GET_TRANS_SCRIPT_MSGS.getValue());
		endPointUrl = endPointUrl.replaceAll("@scheduleIdParam@",scheduleId);
		endPointUrl = endPointUrl.replaceAll("@customerIdParam@",customerId);
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("getTransScriptMsgs : endPointUrl :  "+endPointUrl);

		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		logger.debug("getTransScriptMsgs : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> deleteTransScriptMsg(ApptOnlineSessionData sessionData,String transScriptMSGId) throws Exception {
		String endPointUrl = ApptOnlineUtils.populateBasicRequestParamsDetails(sessionData,RestClientConstants.REST_SERVICE_DELETE_TRANS_SCRIPT_MSG.getValue());
		endPointUrl = endPointUrl.replaceAll("@transScriptMSGIdParam@",transScriptMSGId);
		endPointUrl = endPointUrl.replaceAll(" ", "%20");
		logger.debug("deleteTransScriptMsg : endPointUrl :  "+endPointUrl);

		ResponseModel responseModel = ApptOnlineRestClient.getInstance().performGETRequest(endPointUrl);
		logger.debug("deleteTransScriptMsg : response :  "+responseModel.getData());
		return (LinkedHashMap<String,Object>)responseModel.getData();
	}
	
	@Override
	public void sendErrorEmail(String clientCode,String requestURI,String inputData, Exception ex){
		emailComponent.sendEmail(clientCode,requestURI,inputData,ex);
	}
	
}
