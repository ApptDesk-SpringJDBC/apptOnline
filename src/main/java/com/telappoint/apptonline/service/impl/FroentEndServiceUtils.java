package com.telappoint.apptonline.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.telappoint.apptonline.common.CommonContants;
import com.telappoint.apptonline.common.ConfirmationPageConstants;
import com.telappoint.apptonline.common.LogicConstants.LogicConstantsLogic;
import com.telappoint.apptonline.model.ApptOnlineSessionData;
import com.telappoint.apptonline.model.BaseResponse;
import com.telappoint.apptonline.model.FieldMsg;
import com.telappoint.apptonline.model.JsonResponse;
import com.telappoint.apptonline.model.LocationPriSecAvailable;
import com.telappoint.apptonline.model.OnlineFlow;
import com.telappoint.apptonline.model.OnlinePageData;
import com.telappoint.apptonline.model.Options;
import com.telappoint.apptonline.model.ProcedureListResponse;
import com.telappoint.apptonline.service.IApptOnlineService;
import com.telappoint.apptonline.utils.ApptOnlineCacheComponent;
import com.telappoint.apptonline.utils.ApptOnlineUtils;
import com.telappoint.apptonline.utils.FileUtils;

/**
 *  @author Murali
 */

@Service
public class FroentEndServiceUtils {

	private Logger logger = Logger.getLogger(FroentEndServiceUtils.class);

	private static int NO_OF_LOGICS = 5;

	@Autowired
	private IApptOnlineService apptOnlineService;
	@Autowired
	private ApptOnlineCacheComponent apptOnlineCacheComponent;

	public JsonResponse validateProcedureMatch(ApptOnlineSessionData sessionData,String procedure) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();

		OnlinePageData onlinePageData = apptOnlineCacheComponent.getOnlinePageData(sessionData);
		Map<String, OnlineFlow> onlineFlows = onlinePageData.getOnlineFlows();
		OnlineFlow onlineFlow = onlineFlows.get(CommonContants.FIELD_PROCEDURE.getValue());
		String pageRedirect = onlineFlow.getPageRedirect(); //If Completes or Terminate means functionality will break and return data.
		String logic = "";
		String procedureId = "";
		Set<FieldMsg> pageMessages = new HashSet<FieldMsg>();

		for(int logicNo = 1;logicNo<=NO_OF_LOGICS;logicNo++){	

			logic = (String) ApptOnlineUtils.getPropertyValue(onlineFlow,CommonContants.FIELD_LOGIC.getValue()+logicNo);//ie,logic1,..
			if(ApptOnlineUtils.isNotEmpty(logic)){

				switch (LogicConstantsLogic.getLogic(onlineFlow.getField()+"."+logic)) {			     
				case "PROCEDURE_NO_MATCH": //procedure.NO_MATCH
					String warningMsg = "";
					String errorMsg = "";
					boolean isMatched = false;
					ProcedureListResponse pListResponse = apptOnlineService.getProcedure(sessionData);
					if(pListResponse!=null && pListResponse.isStatus()){
						List<Options> procedureList = pListResponse.getProcedureList();
						for(Options procedureOpt : procedureList){
							if(procedure.equals(procedureOpt.getOptionValue())){
								procedureId = procedureOpt.getOptionKey();
								warningMsg = procedureOpt.getWarningMsg();
								errorMsg = procedureOpt.getErrorMsg();
								isMatched = true;
								break;
							}
						}
					}
					if(!isMatched){ //procedure Not matched 
						BaseResponse baseResponse = apptOnlineService.getProcedureNoMatch(sessionData,procedure);
						if(baseResponse.isStatus()){									
							onlineFlow = onlineFlows.get((String) ApptOnlineUtils.getPropertyValue(onlineFlow,CommonContants.FIELD_PAGE.getValue()+logicNo));//ie,page1,..
							pageRedirect = onlineFlow.getPageRedirect();
							logicNo = 0;					 				
							ApptOnlineUtils.setFieldMsg(sessionData,pageMessages,onlineFlow.getField(),baseResponse.getMessage(),onlinePageData);
						}else{
							jsonResponse.setDisplayErrorPage(true);
							return jsonResponse;									
						}
					}else{ //procedure matched
						if(warningMsg!=null && warningMsg.length()>0){
							ApptOnlineUtils.setFieldMsg(sessionData,pageMessages,"procedure_warning", warningMsg,onlinePageData);
						}
						if(errorMsg!=null && errorMsg.length()>0){
							ApptOnlineUtils.setFieldMsg(sessionData,pageMessages,"procedure_no_match", errorMsg,onlinePageData);
						}
						sessionData.setProcedureId(procedureId);
					}
					break;
				default:
					logger.error("--------------------------------------------------------------------------------------------");
					logger.error((onlineFlow.getField()+"."+logic)+" - Logic is not mattched in validateProcedureMatch");
					logger.error("--------------------------------------------------------------------------------------------");
					break;
				}				     
			}
		}
		jsonResponse.setFormFields(ApptOnlineUtils.getFormFields(ApptOnlineUtils.getCurrentPageFieldsList(sessionData,onlinePageData),false));
		jsonResponse.setFieldsAndMessages(ApptOnlineUtils.getJSONStr(pageMessages));
		if(CommonContants.PAGE_REDIRECT_SUCCESS.getValue().equals(pageRedirect) || CommonContants.PAGE_REDIRECT_TERMINATE.getValue().equals(pageRedirect)){
			jsonResponse.setPageRedirect(pageRedirect);
		}
		return jsonResponse;
	}

	public JsonResponse getLocationPrimarySecondaryAvailability(ApptOnlineSessionData sessionData) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		LocationPriSecAvailable locPriSecAvailable = apptOnlineService.getLocationPrimarySecondaryAvailability(sessionData,sessionData.getProcedureId());

		if(locPriSecAvailable.isStatus()){
			if("N".equals(locPriSecAvailable.getErrorFlag())){	
				OnlinePageData onlinePageData = apptOnlineCacheComponent.getOnlinePageData(sessionData);
				Map<String, OnlineFlow> onlineFlows = onlinePageData.getOnlineFlows();
				OnlineFlow onlineFlow = onlineFlows.get(CommonContants.FIELD_LOCATION.getValue());
				String logic = "";
				Set<FieldMsg> pageMessages = new HashSet<FieldMsg>();

				for(int logicNo = 1;logicNo<=NO_OF_LOGICS;logicNo++){				 
					logic = (String) ApptOnlineUtils.getPropertyValue(onlineFlow,CommonContants.FIELD_LOGIC.getValue()+logicNo);//ie,logic1,..
					if(ApptOnlineUtils.isNotEmpty(logic)){

						switch (LogicConstantsLogic.getLogic(onlineFlow.getField()+"."+logic)) {
						case "LOCATION_CLOSED": //location.LOC_CLOSED     		
							if("Y".equalsIgnoreCase(locPriSecAvailable.getLocationClosed())){ //Location closed
								onlineFlow = onlineFlows.get((String) ApptOnlineUtils.getPropertyValue(onlineFlow,CommonContants.FIELD_PAGE.getValue()+logicNo)); //ie,page1,..
								logicNo = 0;
								ApptOnlineUtils.setFieldMsg(sessionData,pageMessages,onlineFlow.getField(),locPriSecAvailable.getLocationClosedDisplayMessage(),onlinePageData);									
							}else{ //Location not closed
								ApptOnlineUtils.setFieldMsg(sessionData,pageMessages,onlineFlow.getField(),locPriSecAvailable.getLocationName()+"<br/>"+locPriSecAvailable.getLocationAddress()+",&nbsp;"+locPriSecAvailable.getCity()+",&nbsp;"+locPriSecAvailable.getState()+"&nbsp;"+locPriSecAvailable.getZip(),onlinePageData);
								sessionData.putDataToJSONData(CommonContants.FIELD_LOCATION.getValue(),locPriSecAvailable.getLocationName()+"<br/>"+locPriSecAvailable.getLocationAddress()+",&nbsp;"+locPriSecAvailable.getCity()+",&nbsp;"+locPriSecAvailable.getState()+"&nbsp;"+locPriSecAvailable.getZip());
								sessionData.setLocationId(String.valueOf(locPriSecAvailable.getLocationId()));
							}							
							break;
						case "LOC_NO_APPTS":
							if(!locPriSecAvailable.isTimeSlotsAvailable()){ // Time slots Not Available
								onlineFlow = onlineFlows.get((String) ApptOnlineUtils.getPropertyValue(onlineFlow,CommonContants.FIELD_PAGE.getValue()+logicNo)); //ie,page1,..
								logicNo = 0;
								ApptOnlineUtils.setFieldMsg(sessionData,pageMessages,onlineFlow.getField(),locPriSecAvailable.getErrorMessage(),onlinePageData);									
							} else { //Time slots Available
								ApptOnlineUtils.setFieldMsg(sessionData,pageMessages,onlineFlow.getField(),locPriSecAvailable.getLocationName()+"<br/>"+locPriSecAvailable.getLocationAddress()+",&nbsp;"+locPriSecAvailable.getCity()+",&nbsp;"+locPriSecAvailable.getState()+"&nbsp;"+locPriSecAvailable.getZip(),onlinePageData);
							}
							break;

						default:
							logger.error("--------------------------------------------------------------------------------------------");
							logger.error((onlineFlow.getField()+"."+logic)+" - Logic is not mattched in getLocationPrimarySecondaryAvailability");
							logger.error("--------------------------------------------------------------------------------------------");
							break;
						}				     
					}				 
				}
				jsonResponse.setFormFields(ApptOnlineUtils.getFormFields(ApptOnlineUtils.getCurrentPageFieldsList(sessionData,onlinePageData),false));
				jsonResponse.setFieldsAndMessages(ApptOnlineUtils.getJSONStr(pageMessages));
				jsonResponse.setJsonMapKey("locationGoogleMap",locPriSecAvailable.getLocationGoogleMap());
				jsonResponse.setJsonMapKey("city",locPriSecAvailable.getCity());
				jsonResponse.setJsonMapKey("state",locPriSecAvailable.getState());
				jsonResponse.setJsonMapKey("zip",locPriSecAvailable.getZip());
				if(CommonContants.PAGE_REDIRECT_SUCCESS.getValue().equals(onlineFlow.getPageRedirect()) || CommonContants.PAGE_REDIRECT_TERMINATE.getValue().equals(onlineFlow.getPageRedirect())){
					jsonResponse.setPageRedirect(onlineFlow.getPageRedirect());
					return jsonResponse;
				}				 
			}else{
				jsonResponse.setErrorStatus(true);
				jsonResponse.setErrorMessage(locPriSecAvailable.getErrorMessage()!=null ? locPriSecAvailable.getErrorMessage() : "Error occured!");
			}
		}else{
			jsonResponse.setDisplayErrorPage(true);
			return jsonResponse;
		}		
		return jsonResponse;
	}

	public JsonResponse getOnlineNoLoginAuthenticate(ApptOnlineSessionData sessionData,HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		OnlinePageData onlinePageData = apptOnlineCacheComponent.getOnlinePageData(sessionData);
		Map<String,Object> jsonMap = apptOnlineService.getOnlineNoLoginAuthenticate(sessionData,request,onlinePageData);
		if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){	
			jsonResponse.setAuthenticationStatus(ApptOnlineUtils.getValue(jsonMap,"authSuccess", Boolean.class));
			jsonResponse.setAuthenticationMessage(ApptOnlineUtils.getValue(jsonMap,"authMessage", String.class));
			if(ApptOnlineUtils.getValue(jsonMap,"authSuccess", Boolean.class)){
				sessionData.setCustomerId(Long.valueOf(ApptOnlineUtils.getValue(jsonMap,"customerId", Integer.class)));
			}

			Map<String, OnlineFlow> onlineFlows = onlinePageData.getOnlineFlows();
			OnlineFlow onlineFlow = onlineFlows.get(CommonContants.FIELD_NO_LOGIN_AUTHENTICATE.getValue());
			String logic = "";
			Set<FieldMsg> pageMessages = new HashSet<FieldMsg>();

			for(int logicNo = 1;logicNo<=NO_OF_LOGICS;logicNo++){				 
				logic = (String) ApptOnlineUtils.getPropertyValue(onlineFlow,CommonContants.FIELD_LOGIC.getValue()+logicNo);//ie,logic1,..
				if(ApptOnlineUtils.isNotEmpty(logic)){

					switch (LogicConstantsLogic.getLogic(onlineFlow.getField()+"."+logic)) {
					case "CUST_BLOCKED": //nologin_authenticate.CUST_BLOCKED	  		
						if("Y".equalsIgnoreCase(ApptOnlineUtils.getValue(jsonMap,"blocked", String.class))){ //Customer Blocked
							onlineFlow = onlineFlows.get((String) ApptOnlineUtils.getPropertyValue(onlineFlow,CommonContants.FIELD_PAGE.getValue()+logicNo)); //ie,page1,..
							logicNo = 0;
							ApptOnlineUtils.setFieldMsg(sessionData,pageMessages,onlineFlow.getField(),ApptOnlineUtils.getValue(jsonMap,"blockedMessage", String.class),onlinePageData);									
						}							
						break;

					default:
						logger.error("--------------------------------------------------------------------------------------------");
						logger.error((onlineFlow.getField()+"."+logic)+" - Logic is not mattched in getOnlineNoLoginAuthenticate");
						logger.error("--------------------------------------------------------------------------------------------");
						break;
					}				     
				}				 
			}
			jsonResponse.setFormFields(ApptOnlineUtils.getFormFields(ApptOnlineUtils.getCurrentPageFieldsList(sessionData,onlinePageData),false));
			jsonResponse.setFieldsAndMessages(ApptOnlineUtils.getJSONStr(pageMessages));
			if(CommonContants.PAGE_REDIRECT_SUCCESS.getValue().equals(onlineFlow.getPageRedirect()) || CommonContants.PAGE_REDIRECT_TERMINATE.getValue().equals(onlineFlow.getPageRedirect())){
				jsonResponse.setPageRedirect(onlineFlow.getPageRedirect());
				return jsonResponse;
			}
		}else{
			sessionData.setCustomerId((long)-1);
			jsonResponse.setDisplayErrorPage(true);
			return jsonResponse;
		}
		return jsonResponse;
	}

	public JsonResponse getCustomerType(ApptOnlineSessionData sessionData) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String,Object> jsonMap = apptOnlineService.getCustomerType(sessionData);			 
		if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){	
			sessionData.putMapDataToJSONData(jsonMap);
			jsonResponse.setJsonMap(jsonMap);
			if(!CommonContants.FIELD_SERVICE.getValue().equalsIgnoreCase(ApptOnlineUtils.getValue(jsonMap,"categoryType", String.class))){
				sessionData.putMapDataToJSONData(jsonMap);
				jsonResponse.setJsonMap(jsonMap);
			}else{
				return getServiceNameCustomerUtility(sessionData,"",jsonMap);
			}
		}else{
			jsonResponse.setDisplayErrorPage(true);
			return jsonResponse;
		}		
		return jsonResponse;
	}

	public JsonResponse getUtilityType(ApptOnlineSessionData sessionData,String customerTypeId) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String,Object> jsonMap = apptOnlineService.getUtilityType(sessionData,customerTypeId);
		if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){
			if(!CommonContants.FIELD_SERVICE.getValue().equalsIgnoreCase(ApptOnlineUtils.getValue(jsonMap,"categoryType", String.class))){
				sessionData.putMapDataToJSONData(jsonMap);
				jsonResponse.setJsonMap(jsonMap);
			}else{
				return getServiceNameCustomerUtility(sessionData,"",jsonMap);
			}				 
		}else{
			jsonResponse.setDisplayErrorPage(true);
			return jsonResponse;
		}		
		return jsonResponse;
	}

	@SuppressWarnings("unchecked")
	public JsonResponse getServiceNameCustomerUtility(ApptOnlineSessionData sessionData,String utilityTypeId,Map<String,Object> callerJsonMap) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String,Object> jsonMap = null;
		if(callerJsonMap!=null && callerJsonMap.size()>0){
			jsonMap = callerJsonMap;
		}else{
			jsonMap = apptOnlineService.getServiceNameCustomerUtility(sessionData,String.valueOf(ApptOnlineUtils.getValueIfNotNullOtherwisegetDefaultValue(sessionData.getJsonData(),"customerTypeId", Integer.class,"")),utilityTypeId);
		}
		if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){	
			OnlinePageData onlinePageData = apptOnlineCacheComponent.getOnlinePageData(sessionData);
			Map<String, OnlineFlow> onlineFlows = onlinePageData.getOnlineFlows();
			OnlineFlow onlineFlow = onlineFlows.get(CommonContants.FIELD_SERVICE.getValue());
			String logic = "";
			Set<FieldMsg> pageMessages = new HashSet<FieldMsg>();
			sessionData.putMapDataToJSONData(jsonMap);
			jsonResponse.setJsonMap(jsonMap);
			Integer serviceId = 0;

			for(int logicNo = 1;logicNo<=NO_OF_LOGICS;logicNo++){				 
				logic = (String) ApptOnlineUtils.getPropertyValue(onlineFlow,CommonContants.FIELD_LOGIC.getValue()+logicNo);//ie,logic1,..
				if(ApptOnlineUtils.isNotEmpty(logic)){

					switch (LogicConstantsLogic.getLogic(onlineFlow.getField()+"."+logic)) {
					case "SERVICE_RCVD_2ND_GRANT": //service.RCVD_2ND_GRANT		        		
						if("Y".equalsIgnoreCase(ApptOnlineUtils.getValue(jsonMap,"errorFlag", String.class))){ //Received 2nd grant
							onlineFlow = onlineFlows.get((String) ApptOnlineUtils.getPropertyValue(onlineFlow,CommonContants.FIELD_PAGE.getValue()+logicNo)); //ie,page1,..
							logicNo = 0;
							//ApptOnlineUtils.setFieldMsg(sessionData,pageMessages,onlineFlow.getField(), ApptOnlineUtils.getValue(jsonMap,"errorPage", String.class),onlinePageData);
							//If error is there we are displaying as error and terminating the processes..
						}							
						break;
					case "SERVICE_SELECT_SINGLE_SERVICE": //service.SELECT_SINGLE_SERVICE	    
						List<Integer> ids = ApptOnlineUtils.getValue(jsonMap,"ids", List.class);
						if(ids!=null && ids.size()>1){
							Map<String,Object> singleServiceNotClosedJSONMap = apptOnlineService.getSingleServiceNotClosed(sessionData,ApptOnlineUtils.getStringData(ids,","));

							if(ApptOnlineUtils.getValue(singleServiceNotClosedJSONMap,"status", Boolean.class)){
								serviceId = ApptOnlineUtils.getValue(singleServiceNotClosedJSONMap,"serviceId", Integer.class);
								onlineFlow = onlineFlows.get((String) ApptOnlineUtils.getPropertyValue(onlineFlow,CommonContants.FIELD_PAGE.getValue()+logicNo)); //ie,page1,..
								logicNo = 0;
							}else{
								jsonResponse.setDisplayErrorPage(true);
								return jsonResponse;										
							}
						}else{
							if(ids!=null && ids.size()==1){
								serviceId = ids.get(0);
								onlineFlow = onlineFlows.get((String) ApptOnlineUtils.getPropertyValue(onlineFlow,CommonContants.FIELD_PAGE.getValue()+logicNo)); //ie,page1,..
								logicNo = 0;
							}else{
								jsonResponse.setDisplayErrorPage(true);
								return jsonResponse;										
							}
						}
						break;
					case "SERVICE_SER_CLOSED": //select_single_service.SER_CLOSED				        	 	
						if(serviceId<=0){
							ids = ApptOnlineUtils.getValue(jsonMap,"ids", List.class);
							if(ids!=null && ids.size()==0){
								serviceId = ids.get(0);
							}
						}
						if(serviceId>0){
							Map<String,Object> serviceClosedStatusJSONMap = apptOnlineService.getServiceClosedStatus(sessionData,String.valueOf(serviceId));
							if(ApptOnlineUtils.getValue(serviceClosedStatusJSONMap,"status",Boolean.class)){
								if(ApptOnlineUtils.getValue(serviceClosedStatusJSONMap,"closed",Boolean.class)){
									//serviceId = ApptOnlineUtils.getValue(serviceClosedStatusJSONMap,"serviceId", Integer.class);
									onlineFlow = onlineFlows.get((String) ApptOnlineUtils.getPropertyValue(onlineFlow,CommonContants.FIELD_PAGE.getValue()+logicNo)); //ie,page1,..
									logicNo = 0;
									ApptOnlineUtils.setFieldMsg(sessionData,pageMessages,onlineFlow.getField(),ApptOnlineUtils.getValue(serviceClosedStatusJSONMap,"closedMessage", String.class),onlinePageData);
								}
							}else{
								jsonResponse.setDisplayErrorPage(true);
								return jsonResponse;										
							}
						}else{
							jsonResponse.setDisplayErrorPage(true);
							return jsonResponse;										
						}
						break;
					case "SERVICE_SER_TIME_SLOTS_AVAIL": //select_single_service.SER_TIME_SLOTS_AVAIL			        	 	
						if(serviceId<=0){
							ids = ApptOnlineUtils.getValue(jsonMap,"ids", List.class);
							if(ids!=null && ids.size()==0){
								serviceId = ids.get(0);
							}
						}
						if(serviceId>0){
							Map<String,Object> serTimeSlotsAvailStatusJSONMap = apptOnlineService.getServiceTimeSlotsAvailableStatus(sessionData,String.valueOf(serviceId));
							if(ApptOnlineUtils.getValue(serTimeSlotsAvailStatusJSONMap,"status",Boolean.class)){
								if(!ApptOnlineUtils.getValue(serTimeSlotsAvailStatusJSONMap,"available",Boolean.class)){
									//serviceId = ApptOnlineUtils.getValue(serTimeSlotsAvailStatusSONMap,"serviceId", Integer.class);
									onlineFlow = onlineFlows.get((String) ApptOnlineUtils.getPropertyValue(onlineFlow,CommonContants.FIELD_PAGE.getValue()+logicNo)); //ie,page1,..
									logicNo = 0;
									ApptOnlineUtils.setFieldMsg(sessionData,pageMessages,onlineFlow.getField(),ApptOnlineUtils.getValue(serTimeSlotsAvailStatusJSONMap,"message", String.class),onlinePageData);
								}
							}else{
								jsonResponse.setDisplayErrorPage(true);
								return jsonResponse;										
							}
						}else{
							jsonResponse.setDisplayErrorPage(true);
							return jsonResponse;										
						}
						break;
					case "SERVICE_SER_WARNING_MSG": //select_single_service.SER_WARNING_MSG	
						Map<String,Object> serNameWarningJSONMap = apptOnlineService.getServiceNameCustomerWarningPage(sessionData, 
								String.valueOf(ApptOnlineUtils.getValueIfNotNullOtherwisegetDefaultValue(sessionData.getJsonData(),"serviceFundsRcvdId", Integer.class,"")),
								String.valueOf(ApptOnlineUtils.getValueIfNotNullOtherwisegetDefaultValue(sessionData.getJsonData(),"customerTypeId", Integer.class,"")), utilityTypeId,String.valueOf(serviceId));
						if(ApptOnlineUtils.getValue(serNameWarningJSONMap,"status",Boolean.class)){
							if("Y".equalsIgnoreCase(ApptOnlineUtils.getValue(serNameWarningJSONMap,"warningFlag", String.class))){
								onlineFlow = onlineFlows.get((String) ApptOnlineUtils.getPropertyValue(onlineFlow,CommonContants.FIELD_PAGE.getValue()+logicNo)); //ie,page1,..
								logicNo = 0;
								ApptOnlineUtils.setFieldMsg(sessionData,pageMessages,onlineFlow.getField(), ApptOnlineUtils.getValue(serNameWarningJSONMap,"warningPage", String.class),onlinePageData);
							}
						} else{
							jsonResponse.setDisplayErrorPage(true);
							return jsonResponse;										
						}							
						break;
					default:
						logger.error("--------------------------------------------------------------------------------------------");
						logger.error((onlineFlow.getField()+"."+logic)+" - Logic is not mattched in getServiceNameCustomerUtility");
						logger.error("--------------------------------------------------------------------------------------------");
						break;
					}				     
				}				 
			}
			sessionData.setServiceId(String.valueOf(serviceId));
			jsonResponse.setServiceId(serviceId);
			jsonResponse.setFormFields(ApptOnlineUtils.getFormFields(ApptOnlineUtils.getCurrentPageFieldsList(sessionData,onlinePageData),false));
			jsonResponse.setFieldsAndMessages(ApptOnlineUtils.getJSONStr(pageMessages));
			if(CommonContants.PAGE_REDIRECT_SUCCESS.getValue().equals(onlineFlow.getPageRedirect()) || CommonContants.PAGE_REDIRECT_TERMINATE.getValue().equals(onlineFlow.getPageRedirect())){
				jsonResponse.setPageRedirect(onlineFlow.getPageRedirect());
				return jsonResponse;
			}
		}else{
			jsonResponse.setDisplayErrorPage(true);
			return jsonResponse;
		}		
		return jsonResponse;
	}

	public JsonResponse holdFirstAvailAppointment(ApptOnlineSessionData sessionData,String serviceId) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		sessionData.setServiceId(serviceId);
		Map<String,Object> jsonMap = apptOnlineService.holdFirstAvailableAppointment(sessionData,serviceId);
		if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class) && "N".equals(ApptOnlineUtils.getValue(jsonMap,"errorFlag", String.class))){					
			if(ApptOnlineUtils.getValue(jsonMap,"scheduleId", Integer.class)>0){
				sessionData.setScheduleId(String.valueOf(ApptOnlineUtils.getValue(jsonMap,"scheduleId", Integer.class)));
				jsonResponse.setJsonMap(jsonMap);
				sessionData.putMapDataToJSONData(jsonMap);
			}else{
				jsonResponse.setErrorStatus(true);
				jsonResponse.setErrorMessage("Error while holding Appointment!");
			}
		}else{
			jsonResponse.setErrorStatus(true);
			jsonResponse.setErrorMessage(ApptOnlineUtils.getValueIfNotNullOtherwisegetDefaultValue(jsonMap,"errorMessage", String.class,"Error while holding Appointment!"));
		}		
		return jsonResponse;
	}

	public JsonResponse releaseHoldedAppointment(ApptOnlineSessionData sessionData) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		if(ApptOnlineUtils.isNotEmpty(sessionData.getScheduleId()) && !"-1".equals(sessionData.getScheduleId())) {
			Map<String,Object> jsonMap = apptOnlineService.releaseHoldedAppointment(sessionData);
			if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){					
				logger.debug("Release Holded Appointment is sucesses : "+ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)+" and removing ScheduleId :::::::: "+sessionData.getScheduleId()+" ::::::::: from session");
				sessionData.setScheduleId("-1");
			}else{
				jsonResponse.setErrorStatus(true);
				jsonResponse.setErrorMessage("Error while releasing holded Appointment!");
			}
		}else{
			logger.debug("Froent End called Release Holded Appointment for ScheduleId :::::::: "+sessionData.getScheduleId()+" ::::::::: so we are ignoring backend call");	
		}		
		return jsonResponse;
	}

	public JsonResponse getHouseholdMonthlyIncome(ApptOnlineSessionData sessionData,String noOfPeople) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String,Object> jsonMap = apptOnlineService.getHouseholdMonthlyIncome(sessionData,noOfPeople);
		if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){	
			jsonResponse.setJsonMap(jsonMap);
		}else{
			jsonResponse.setDisplayErrorPage(true);
			return jsonResponse;
		}		
		return jsonResponse;
	}

	public JsonResponse getCustomerInfo(ApptOnlineSessionData sessionData,String pageId) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String,Object> jsonMap = apptOnlineService.getCustomerInfo(sessionData);
		if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){					
			sessionData.putMapDataToJSONData(jsonMap);
			jsonResponse.setJsonMap(jsonMap);
		}else{
			jsonResponse.setDisplayErrorPage(true);
			return jsonResponse;
		}		
		return jsonResponse;
	}

	public JsonResponse updateCustomerInfo(ApptOnlineSessionData sessionData,HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		OnlinePageData onlinePageData = apptOnlineCacheComponent.getOnlinePageData(sessionData);
		Map<String,Object> jsonMap = apptOnlineService.updateCustomerInfo(sessionData,request,onlinePageData);
		if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){	

			Map<String, OnlineFlow> onlineFlows = onlinePageData.getOnlineFlows();
			OnlineFlow onlineFlow = onlineFlows.get(CommonContants.FIELD_PERSONAL_INFO.getValue());
			String logic = "";
			Set<FieldMsg> pageMessages = new HashSet<FieldMsg>();

			for(int logicNo = 1;logicNo<=NO_OF_LOGICS;logicNo++){				 
				logic = (String) ApptOnlineUtils.getPropertyValue(onlineFlow,CommonContants.FIELD_LOGIC.getValue()+logicNo);//ie,logic1,..
				if(ApptOnlineUtils.isNotEmpty(logic)){

					switch (LogicConstantsLogic.getLogic(onlineFlow.getField()+"."+logic)) {
					case "PERSONAL_INFO_CUST_UPDATE_FAIL":        		
						if(!ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){
							onlineFlow = onlineFlows.get((String) ApptOnlineUtils.getPropertyValue(onlineFlow,CommonContants.FIELD_PAGE.getValue()+logicNo)); //ie,page1,..
							logicNo = 0;
							ApptOnlineUtils.setFieldMsg(sessionData,pageMessages,onlineFlow.getField(),"Error while updating Personal Info",onlinePageData); 									
						}							
						break;

					default:
						logger.error("--------------------------------------------------------------------------------------------");
						logger.error((onlineFlow.getField()+"."+logic)+" - Logic is not mattched in updateCustomerInfo");
						logger.error("--------------------------------------------------------------------------------------------");
						break;
					}				     
				}				 
			}
			jsonResponse.setFormFields(ApptOnlineUtils.getFormFields(ApptOnlineUtils.getCurrentPageFieldsList(sessionData,onlinePageData),false));
			jsonResponse.setFieldsAndMessages(ApptOnlineUtils.getJSONStr(pageMessages));
			if(CommonContants.PAGE_REDIRECT_SUCCESS.getValue().equals(onlineFlow.getPageRedirect()) || CommonContants.PAGE_REDIRECT_TERMINATE.getValue().equals(onlineFlow.getPageRedirect())){
				jsonResponse.setPageRedirect(onlineFlow.getPageRedirect());
				return jsonResponse;
			}
		}else{
			jsonResponse.setDisplayErrorPage(true);
			return jsonResponse;
		}		
		return jsonResponse;
	}

	public JsonResponse getListOfThingsToBring(ApptOnlineSessionData sessionData) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String,Object> jsonMap = apptOnlineService.getListOfThingsToBring(sessionData);
		if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){
			jsonResponse.setListOfThingToBring(ApptOnlineUtils.getValue(jsonMap,"deplayText", String.class));
		}else{
			jsonResponse.setDisplayErrorPage(true);
			return jsonResponse;
		}		
		return jsonResponse;
	}

	public JsonResponse confirmAppointment(ApptOnlineSessionData sessionData) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String,Object> jsonMap = apptOnlineService.confirmAppointment(sessionData);
		if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){	
			jsonResponse.setJsonMap(ConfirmationPageConstants.getConfirmationPageJsonMap(ApptOnlineUtils.getValue(jsonMap,"displayKeys", String.class), ApptOnlineUtils.getValue(jsonMap,"displayValues", String.class)));
			sessionData.setScheduleId("-1");//ie, After appointment confirmed, making schedule id as -1 
		}else{
			jsonResponse.setDisplayErrorPage(true);
			return jsonResponse;
		}		
		return jsonResponse;
	}

	public JsonResponse getCancelLoginAuthenticate(ApptOnlineSessionData sessionData,HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		OnlinePageData onlinePageData = apptOnlineCacheComponent.getOnlinePageData(sessionData);
		Map<String,Object> jsonMap = apptOnlineService.getCancelLoginAuthenticate(sessionData,request,onlinePageData);
		if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){	
			jsonResponse.setAuthenticationStatus(ApptOnlineUtils.getValue(jsonMap,"authSuccess", Boolean.class));
			//jsonResponse.setAuthenticationMessage(ApptOnlineUtils.getValue(jsonMap,"authMessage", String.class));
			if(ApptOnlineUtils.getValue(jsonMap,"authSuccess", Boolean.class)){
				sessionData.setCustomerId(Long.valueOf(ApptOnlineUtils.getValue(jsonMap,"customerId", Integer.class)));
			}

			Map<String, OnlineFlow> onlineFlows = onlinePageData.getOnlineFlows();
			OnlineFlow onlineFlow = onlineFlows.get(CommonContants.FIELD_CANCEL_LOGIN_AUTHENTICATE.getValue());
			String logic = "";
			Set<FieldMsg> pageMessages = new HashSet<FieldMsg>();

			for(int logicNo = 1;logicNo<=NO_OF_LOGICS;logicNo++){				 
				logic = (String) ApptOnlineUtils.getPropertyValue(onlineFlow,CommonContants.FIELD_LOGIC.getValue()+logicNo);//ie,logic1,..
				if(ApptOnlineUtils.isNotEmpty(logic)){

					switch (LogicConstantsLogic.getLogic(onlineFlow.getField()+"."+logic)) {
					case "CUSTOMER_NOT_FOUND": //cancel_login_authenticate.CUSTOMER_NOT_FOUND		        		
						if(ApptOnlineUtils.getValue(jsonMap,"authSuccess", Boolean.class)==false
						&& Long.valueOf(ApptOnlineUtils.getValue(jsonMap,"customerId", Integer.class))<0){ //Customer Not Found
							onlineFlow = onlineFlows.get((String) ApptOnlineUtils.getPropertyValue(onlineFlow,CommonContants.FIELD_PAGE.getValue()+logicNo)); //ie,page1,..
							logicNo = 0;
							ApptOnlineUtils.setFieldMsg(sessionData,pageMessages,onlineFlow.getField(),ApptOnlineUtils.getValue(jsonMap,"authMessage", String.class),onlinePageData);									
						}							
						break;					        	 
					default:
						logger.error("--------------------------------------------------------------------------------------------");
						logger.error((onlineFlow.getField()+"."+logic)+" - Logic is not mattched in getCancelLoginAuthenticate");
						logger.error("--------------------------------------------------------------------------------------------");
						break;
					}				     
				}				 
			}
			jsonResponse.setFormFields(ApptOnlineUtils.getFormFields(ApptOnlineUtils.getCurrentPageFieldsList(sessionData,onlinePageData),false));
			jsonResponse.setFieldsAndMessages(ApptOnlineUtils.getJSONStr(pageMessages));
			if(CommonContants.PAGE_REDIRECT_SUCCESS.getValue().equals(onlineFlow.getPageRedirect()) || CommonContants.PAGE_REDIRECT_TERMINATE.getValue().equals(onlineFlow.getPageRedirect())){
				jsonResponse.setPageRedirect(onlineFlow.getPageRedirect());
				return jsonResponse;
			}
		}else{
			sessionData.setCustomerId((long)-1);
			jsonResponse.setDisplayErrorPage(true);
			return jsonResponse;
		}
		return jsonResponse;
	}

	public JsonResponse getBookedAppointments(ApptOnlineSessionData sessionData) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String,Object> jsonMap = apptOnlineService.getBookedAppointments(sessionData);
		if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){	
			jsonResponse.setJsonMap(jsonMap);
		}else{
			jsonResponse.setDisplayErrorPage(true);
			return jsonResponse;
		}		
		return jsonResponse;
	}

	public JsonResponse cancelAppointment(ApptOnlineSessionData sessionData,String scheduleId) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String,Object> jsonMap = apptOnlineService.cancelAppointment(sessionData,scheduleId);
		if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){	
			jsonResponse.setJsonMap(jsonMap);
		}else{
			jsonResponse.setDisplayErrorPage(true);
			return jsonResponse;
		}		
		return jsonResponse;
	}
	
	public JsonResponse checkDuplicateAppts(ApptOnlineSessionData sessionData,String serviceId) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		sessionData.setServiceId(serviceId);
		Map<String,Object> jsonMap = apptOnlineService.checkDuplicateAppts(sessionData,serviceId);
		if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class) && "N".equals(ApptOnlineUtils.getValue(jsonMap,"errorFlag", String.class))){					
			jsonResponse.setJsonMap(jsonMap);
		}else{
			jsonResponse.setErrorStatus(true);
			jsonResponse.setErrorMessage(ApptOnlineUtils.getValueIfNotNullOtherwisegetDefaultValue(jsonMap,"errorMessage", String.class,"Error while check Appointment hold !"));
		}		
		return jsonResponse;
	}
	
	public JsonResponse getAvailableDatesCallcenter(ApptOnlineSessionData sessionData) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String,Object> jsonMap = apptOnlineService.getAvailableDatesCallcenter(sessionData);
		if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){	
			jsonResponse.setJsonMap(jsonMap);
		}else{
			jsonResponse.setDisplayErrorPage(true);
			return jsonResponse;
		}		
		return jsonResponse;
	}
	
	public JsonResponse getAvailableTimesCallcenter(ApptOnlineSessionData sessionData,String apptDate) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String,Object> jsonMap = apptOnlineService.getAvailableTimesCallcenter(sessionData,apptDate);
		if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){	
			jsonResponse.setJsonMap(jsonMap);
		}else{
			jsonResponse.setDisplayErrorPage(true);
			return jsonResponse;
		}		
		return jsonResponse;
	}
	
	public JsonResponse holdAppointmentCallCenter(ApptOnlineSessionData sessionData,String date,String time) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		String apptDateTime = date+" "+time;
		Map<String,Object> jsonMap = apptOnlineService.holdAppointmentCallCenter(sessionData,apptDateTime);
		if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){					
			if(ApptOnlineUtils.getValue(jsonMap,"scheduleId", Integer.class)>0){
				sessionData.setScheduleId(String.valueOf(ApptOnlineUtils.getValue(jsonMap,"scheduleId", Integer.class)));
				jsonResponse.setJsonMap(jsonMap);
				sessionData.putMapDataToJSONData(jsonMap);
			}else{
				jsonResponse.setErrorStatus(true);
				jsonResponse.setErrorMessage("Error while holding Appointment!");
			}
		}else{
			jsonResponse.setErrorStatus(true);
			jsonResponse.setErrorMessage(ApptOnlineUtils.getValueIfNotNullOtherwisegetDefaultValue(jsonMap,"errorMessage", String.class,"Error while holding Appointment!"));
		}
		return jsonResponse;
	}
	
	public JsonResponse getPledgeAmountLoginAuthenticate(ApptOnlineSessionData sessionData,HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		OnlinePageData onlinePageData = apptOnlineCacheComponent.getOnlinePageData(sessionData);
		Map<String,Object> jsonMap = apptOnlineService.getPledgeAmountLoginAuthenticate(sessionData,request,onlinePageData);
		if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){	
			jsonResponse.setAuthenticationStatus(ApptOnlineUtils.getValue(jsonMap,"authSuccess", Boolean.class));
			//jsonResponse.setAuthenticationMessage(ApptOnlineUtils.getValue(jsonMap,"authMessage", String.class));
			if(ApptOnlineUtils.getValue(jsonMap,"authSuccess", Boolean.class)){
				sessionData.setCustomerId(Long.valueOf(ApptOnlineUtils.getValue(jsonMap,"customerId", Integer.class)));
			}

			Map<String, OnlineFlow> onlineFlows = onlinePageData.getOnlineFlows();
			OnlineFlow onlineFlow = onlineFlows.get(CommonContants.FIELD_PLEDGE_AMT_LOGIN_AUTHENTICATE.getValue());
			String logic = "";
			Set<FieldMsg> pageMessages = new HashSet<FieldMsg>();

			for(int logicNo = 1;logicNo<=NO_OF_LOGICS;logicNo++){				 
				logic = (String) ApptOnlineUtils.getPropertyValue(onlineFlow,CommonContants.FIELD_LOGIC.getValue()+logicNo);//ie,logic1,..
				if(ApptOnlineUtils.isNotEmpty(logic)){

					switch (LogicConstantsLogic.getLogic(onlineFlow.getField()+"."+logic)) {
					case "CUSTOMER_NOT_FOUND": //pledge_amt_login_auth.CUSTOMER_NOT_FOUND		        		
						if(ApptOnlineUtils.getValue(jsonMap,"authSuccess", Boolean.class)==false
						&& Long.valueOf(ApptOnlineUtils.getValue(jsonMap,"customerId", Integer.class))<0){ //Customer Not Found
							onlineFlow = onlineFlows.get((String) ApptOnlineUtils.getPropertyValue(onlineFlow,CommonContants.FIELD_PAGE.getValue()+logicNo)); //ie,page1,..
							logicNo = 0;
							ApptOnlineUtils.setFieldMsg(sessionData,pageMessages,onlineFlow.getField(),ApptOnlineUtils.getValue(jsonMap,"authMessage", String.class),onlinePageData);									
						}							
						break;					        	 
					default:
						logger.error("--------------------------------------------------------------------------------------------");
						logger.error((onlineFlow.getField()+"."+logic)+" - Logic is not mattched in getPledgeAmountLoginAuthenticate");
						logger.error("--------------------------------------------------------------------------------------------");
						break;
					}				     
				}				 
			}
			jsonResponse.setFormFields(ApptOnlineUtils.getFormFields(ApptOnlineUtils.getCurrentPageFieldsList(sessionData,onlinePageData),false));
			jsonResponse.setFieldsAndMessages(ApptOnlineUtils.getJSONStr(pageMessages));
			if(CommonContants.PAGE_REDIRECT_SUCCESS.getValue().equals(onlineFlow.getPageRedirect()) || CommonContants.PAGE_REDIRECT_TERMINATE.getValue().equals(onlineFlow.getPageRedirect())){
				jsonResponse.setPageRedirect(onlineFlow.getPageRedirect());
				return jsonResponse;
			}
		}else{
			sessionData.setCustomerId((long)-1);
			jsonResponse.setDisplayErrorPage(true);
			return jsonResponse;
		}
		return jsonResponse;
	}
	
	public JsonResponse getPledgeHistory(ApptOnlineSessionData sessionData) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String,Object> jsonMap = apptOnlineService.getPledgeHistory(sessionData);
		if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){	
			jsonMap.put("status", true);
			jsonResponse.setJsonMap(jsonMap);
		}else{
			jsonResponse.setDisplayErrorPage(true);
			return jsonResponse;
		}
		return jsonResponse;
	}
	
	public JsonResponse getPledgeAwardLetter(ApptOnlineSessionData sessionData,String customerPledgeId) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String,Object> jsonMap = apptOnlineService.getPledgeAwardLetter(sessionData,customerPledgeId);
		if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){	
			jsonMap.put("status", true);
			jsonResponse.setJsonMap(jsonMap);
		}else{
			jsonResponse.setDisplayErrorPage(true);
			return jsonResponse;
		}	
		return jsonResponse;
	}
	
	public JsonResponse getLocations(ApptOnlineSessionData sessionData) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String,Object> jsonMap = apptOnlineService.getLocations(sessionData);
		if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){	
			jsonResponse.setJsonMap(jsonMap);
		}else{
			jsonResponse.setDisplayErrorPage(true);
			return jsonResponse;
		}		
		return jsonResponse;
	}
	
	public JsonResponse getServicesCallcenter(ApptOnlineSessionData sessionData) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String,Object> jsonMap = apptOnlineService.getServicesCallcenter(sessionData);
		if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){	
			jsonResponse.setJsonMap(jsonMap);
		}else{
			jsonResponse.setDisplayErrorPage(true);
			return jsonResponse;
		}		
		return jsonResponse;
	}
	
	public JsonResponse authenticateAndUpdateCustomer(ApptOnlineSessionData sessionData,HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		OnlinePageData onlinePageData = apptOnlineCacheComponent.getOnlinePageData(sessionData);
		Map<String,Object> jsonMap = apptOnlineService.authenticateAndUpdateCustomer(sessionData,request,onlinePageData);
		if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){	
			jsonResponse.setAuthenticationStatus(ApptOnlineUtils.getValue(jsonMap,"authSuccess", Boolean.class));
			jsonResponse.setAuthenticationMessage(ApptOnlineUtils.getValue(jsonMap,"authMessage", String.class));
			if(ApptOnlineUtils.getValue(jsonMap,"authSuccess", Boolean.class)){
				sessionData.setCustomerId(Long.valueOf(ApptOnlineUtils.getValue(jsonMap,"customerId", Integer.class)));
			}

			Map<String, OnlineFlow> onlineFlows = onlinePageData.getOnlineFlows();
			OnlineFlow onlineFlow = onlineFlows.get(CommonContants.FIELD_AUTHENTICATE_UPDATE_CUSTOMER.getValue());
			String logic = "";
			Set<FieldMsg> pageMessages = new HashSet<FieldMsg>();

			for(int logicNo = 1;logicNo<=NO_OF_LOGICS;logicNo++){				 
				logic = (String) ApptOnlineUtils.getPropertyValue(onlineFlow,CommonContants.FIELD_LOGIC.getValue()+logicNo);//ie,logic1,..
				if(ApptOnlineUtils.isNotEmpty(logic)){

					switch (LogicConstantsLogic.getLogic(onlineFlow.getField()+"."+logic)) {
					case "CUST_BLOCKED": //authenticate_update_customer.CUST_BLOCKED	  		
						if("Y".equalsIgnoreCase(ApptOnlineUtils.getValue(jsonMap,"blocked", String.class))){ //Customer Blocked
							onlineFlow = onlineFlows.get((String) ApptOnlineUtils.getPropertyValue(onlineFlow,CommonContants.FIELD_PAGE.getValue()+logicNo)); //ie,page1,..
							logicNo = 0;
							ApptOnlineUtils.setFieldMsg(sessionData,pageMessages,onlineFlow.getField(),ApptOnlineUtils.getValue(jsonMap,"blockedMessage", String.class),onlinePageData);									
						}							
						break;

					default:
						logger.error("--------------------------------------------------------------------------------------------");
						logger.error((onlineFlow.getField()+"."+logic)+" - Logic is not mattched in authenticateAndUpdateCustomer");
						logger.error("--------------------------------------------------------------------------------------------");
						break;
					}				     
				}				 
			}
			jsonResponse.setFormFields(ApptOnlineUtils.getFormFields(ApptOnlineUtils.getCurrentPageFieldsList(sessionData,onlinePageData),false));
			jsonResponse.setFieldsAndMessages(ApptOnlineUtils.getJSONStr(pageMessages));
			if(CommonContants.PAGE_REDIRECT_SUCCESS.getValue().equals(onlineFlow.getPageRedirect()) || CommonContants.PAGE_REDIRECT_TERMINATE.getValue().equals(onlineFlow.getPageRedirect())){
				jsonResponse.setPageRedirect(onlineFlow.getPageRedirect());
				return jsonResponse;
			}
		}else{
			sessionData.setCustomerId((long)-1);
			jsonResponse.setDisplayErrorPage(true);
			return jsonResponse;
		}
		return jsonResponse;
	}
	
	public JsonResponse loginAuthenticateAndUpdateForExternal(ApptOnlineSessionData sessionData,HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		OnlinePageData onlinePageData = apptOnlineCacheComponent.getOnlinePageData(sessionData);
		Map<String,Object> jsonMap = apptOnlineService.loginAuthenticateAndUpdateForExternal(sessionData,request,onlinePageData);
		if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){	
			jsonResponse.setAuthenticationStatus(ApptOnlineUtils.getValue(jsonMap,"authSuccess", Boolean.class));
			jsonResponse.setAuthenticationMessage(ApptOnlineUtils.getValue(jsonMap,"authMessage", String.class));
			if(ApptOnlineUtils.getValue(jsonMap,"authSuccess", Boolean.class)){
				sessionData.setCustomerId(Long.valueOf(ApptOnlineUtils.getValue(jsonMap,"customerId", Integer.class)));
			}

			Map<String, OnlineFlow> onlineFlows = onlinePageData.getOnlineFlows();
			OnlineFlow onlineFlow = onlineFlows.get(CommonContants.FIELD_EXTERNAL_AUTHENTICATE.getValue());
			String logic = "";
			Set<FieldMsg> pageMessages = new HashSet<FieldMsg>();

			for(int logicNo = 1;logicNo<=NO_OF_LOGICS;logicNo++){				 
				logic = (String) ApptOnlineUtils.getPropertyValue(onlineFlow,CommonContants.FIELD_LOGIC.getValue()+logicNo);//ie,logic1,..
				if(ApptOnlineUtils.isNotEmpty(logic)){

					switch (LogicConstantsLogic.getLogic(onlineFlow.getField()+"."+logic)) {
					case "CUSTOMER_NOT_FOUND": //external_authenticate.CUSTOMER_NOT_FOUND	  		
						if("Y".equalsIgnoreCase(ApptOnlineUtils.getValue(jsonMap,"blocked", String.class))){ //Customer Blocked
							onlineFlow = onlineFlows.get((String) ApptOnlineUtils.getPropertyValue(onlineFlow,CommonContants.FIELD_PAGE.getValue()+logicNo)); //ie,page1,..
							logicNo = 0;
							ApptOnlineUtils.setFieldMsg(sessionData,pageMessages,onlineFlow.getField(),ApptOnlineUtils.getValue(jsonMap,"blockedMessage", String.class),onlinePageData);									
						}							
						break;

					default:
						logger.error("--------------------------------------------------------------------------------------------");
						logger.error((onlineFlow.getField()+"."+logic)+" - Logic is not mattched in loginAuthenticateAndUpdateForExternal");
						logger.error("--------------------------------------------------------------------------------------------");
						break;
					}				     
				}				 
			}
			jsonResponse.setFormFields(ApptOnlineUtils.getFormFields(ApptOnlineUtils.getCurrentPageFieldsList(sessionData,onlinePageData),false));
			jsonResponse.setFieldsAndMessages(ApptOnlineUtils.getJSONStr(pageMessages));
			if(CommonContants.PAGE_REDIRECT_SUCCESS.getValue().equals(onlineFlow.getPageRedirect()) || CommonContants.PAGE_REDIRECT_TERMINATE.getValue().equals(onlineFlow.getPageRedirect())){
				jsonResponse.setPageRedirect(onlineFlow.getPageRedirect());
				return jsonResponse;
			}
		}else{
			sessionData.setCustomerId((long)-1);
			jsonResponse.setDisplayErrorPage(true);
			return jsonResponse;
		}
		return jsonResponse;
	}
	
	public JsonResponse loginAuthenticateForExternal(ApptOnlineSessionData sessionData,HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		OnlinePageData onlinePageData = apptOnlineCacheComponent.getOnlinePageData(sessionData);
		Map<String,Object> jsonMap = apptOnlineService.loginAuthenticateForExternal(sessionData,request,onlinePageData);
		if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){	
			jsonResponse.setAuthenticationStatus(ApptOnlineUtils.getValue(jsonMap,"authSuccess", Boolean.class));
			jsonResponse.setAuthenticationMessage(ApptOnlineUtils.getValue(jsonMap,"authMessage", String.class));
			if(ApptOnlineUtils.getValue(jsonMap,"authSuccess", Boolean.class)){
				sessionData.setCustomerId(Long.valueOf(ApptOnlineUtils.getValue(jsonMap,"customerId", Integer.class)));
			}

			Map<String, OnlineFlow> onlineFlows = onlinePageData.getOnlineFlows();
			OnlineFlow onlineFlow = onlineFlows.get(CommonContants.FIELD_EXTERNAL_AUTHENTICATE.getValue());
			String logic = "";
			Set<FieldMsg> pageMessages = new HashSet<FieldMsg>();

			for(int logicNo = 1;logicNo<=NO_OF_LOGICS;logicNo++){				 
				logic = (String) ApptOnlineUtils.getPropertyValue(onlineFlow,CommonContants.FIELD_LOGIC.getValue()+logicNo);//ie,logic1,..
				if(ApptOnlineUtils.isNotEmpty(logic)){

					switch (LogicConstantsLogic.getLogic(onlineFlow.getField()+"."+logic)) {
					case "CUSTOMER_NOT_FOUND": //external_authenticate.CUSTOMER_NOT_FOUND	  		
						if("Y".equalsIgnoreCase(ApptOnlineUtils.getValue(jsonMap,"blocked", String.class))){ //Customer Blocked
							onlineFlow = onlineFlows.get((String) ApptOnlineUtils.getPropertyValue(onlineFlow,CommonContants.FIELD_PAGE.getValue()+logicNo)); //ie,page1,..
							logicNo = 0;
							ApptOnlineUtils.setFieldMsg(sessionData,pageMessages,onlineFlow.getField(),ApptOnlineUtils.getValue(jsonMap,"blockedMessage", String.class),onlinePageData);									
						}							
						break;

					default:
						logger.error("--------------------------------------------------------------------------------------------");
						logger.error((onlineFlow.getField()+"."+logic)+" - Logic is not mattched in loginAuthenticateForExternal");
						logger.error("--------------------------------------------------------------------------------------------");
						break;
					}				     
				}				 
			}
			jsonResponse.setFormFields(ApptOnlineUtils.getFormFields(ApptOnlineUtils.getCurrentPageFieldsList(sessionData,onlinePageData),false));
			jsonResponse.setFieldsAndMessages(ApptOnlineUtils.getJSONStr(pageMessages));
			if(CommonContants.PAGE_REDIRECT_SUCCESS.getValue().equals(onlineFlow.getPageRedirect()) || CommonContants.PAGE_REDIRECT_TERMINATE.getValue().equals(onlineFlow.getPageRedirect())){
				jsonResponse.setPageRedirect(onlineFlow.getPageRedirect());
				return jsonResponse;
			}
		}else{
			sessionData.setCustomerId((long)-1);
			jsonResponse.setDisplayErrorPage(true);
			return jsonResponse;
		}
		return jsonResponse;
	}
	
	public JsonResponse confirmAppointmentExternalLogic(ApptOnlineSessionData sessionData) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String,Object> jsonMap = apptOnlineService.confirmAppointmentExternalLogic(sessionData);
		if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){	
			jsonResponse.setJsonMap(ConfirmationPageConstants.getConfirmationPageJsonMap(ApptOnlineUtils.getValue(jsonMap,"displayKeys", String.class), ApptOnlineUtils.getValue(jsonMap,"displayValues", String.class)));
			sessionData.setScheduleId("-1");//ie, After appointment confirmed, making schedule id as -1 
		}else{
			jsonResponse.setDisplayErrorPage(true);
			return jsonResponse;
		}		
		return jsonResponse;
	}
	
	public JsonResponse cancelAppointmentExternalLogic(ApptOnlineSessionData sessionData,String scheduleId) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String,Object> jsonMap = apptOnlineService.cancelAppointmentExternalLogic(sessionData,scheduleId);
		if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){	
			jsonResponse.setJsonMap(jsonMap);
		}else{
			jsonResponse.setDisplayErrorPage(true);
			return jsonResponse;
		}		
		return jsonResponse;
	}
	
	public JsonResponse saveTransScriptMsg(ApptOnlineSessionData sessionData,String scheduleId,MultipartFile[] files) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		if(files!=null && files.length>0) {
			StringBuilder fileNames = new StringBuilder();
			boolean initial = true;
			String filePath = FileUtils.getPropertyFromApptonlineProp(FileUtils.APPT_SERVICE_FILE_UPLOAD_PATH_PROP_KEY);
			filePath = filePath + File.separator +sessionData.getClientCode();
			File dir = new File(filePath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			for (int i = 0; i < files.length; i++) {
				MultipartFile file = files[i];
				if(file!=null && file.getSize()>0) {
					try {
						File destFile = new File(dir.getAbsolutePath()+ File.separator + file.getOriginalFilename());
						BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(destFile));
						stream.write(file.getBytes());
						stream.close();	
						if(initial){
							initial = false;
						}else{
							fileNames.append(",");
						}
						fileNames.append(file.getOriginalFilename());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			Map<String,Object> jsonMap = apptOnlineService.saveTransScriptMsg(sessionData,filePath,fileNames.toString(),scheduleId);
			if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){	
				jsonResponse.setJsonMap(jsonMap);
			}else{
				jsonResponse.setDisplayErrorPage(true);
				return jsonResponse;
			}	
		}else {
			jsonResponse.setErrorStatus(true);
			jsonResponse.setErrorMessage("Please upload atleast one file!");
		}
		return jsonResponse;
	}
	
	public JsonResponse getTransScriptMsgs(ApptOnlineSessionData sessionData,String scheduleId) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String,Object> jsonMap = apptOnlineService.getTransScriptMsgs(sessionData,scheduleId,String.valueOf(sessionData.getCustomerId()));
		if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){	
			jsonResponse.setJsonMap(jsonMap);
		}else{
			jsonResponse.setDisplayErrorPage(true);
			return jsonResponse;
		}		
		return jsonResponse;
	}
	
	public JsonResponse deleteTransScriptMsg(ApptOnlineSessionData sessionData,String transScriptMSGId,String fileName) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		String filePath = FileUtils.getPropertyFromApptonlineProp(FileUtils.APPT_SERVICE_FILE_UPLOAD_PATH_PROP_KEY);
		filePath = filePath + File.separator +sessionData.getClientCode();

		File dir = new File(filePath);
		if (dir.exists()) {
			File fileToBeDelete = new File(dir.getAbsolutePath()+ File.separator + fileName);
			fileToBeDelete.delete();
		}
		Map<String,Object> jsonMap = apptOnlineService.deleteTransScriptMsg(sessionData,transScriptMSGId);
		if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){	
			jsonResponse.setJsonMap(jsonMap);
		}else{
			jsonResponse.setDisplayErrorPage(true);
			return jsonResponse;
		}
		return jsonResponse;
	}
}
