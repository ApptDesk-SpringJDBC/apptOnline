package com.telappoint.apptonline.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.telappoint.apptonline.common.CommonContants;
import com.telappoint.apptonline.common.JSPPageNameConstants;
import com.telappoint.apptonline.form.validation.IApptOnlineValidator;
import com.telappoint.apptonline.model.ApptOnlineSessionData;
import com.telappoint.apptonline.model.JsonResponse;
import com.telappoint.apptonline.model.LandingPageInfo;
import com.telappoint.apptonline.model.OnlinePageData;
import com.telappoint.apptonline.model.OnlinePageFields;
import com.telappoint.apptonline.service.IApptOnlineService;
import com.telappoint.apptonline.service.impl.FroentEndServiceUtils;
import com.telappoint.apptonline.utils.ApptOnlineCacheComponent;
import com.telappoint.apptonline.utils.ApptOnlineUtils;

/**
 * @author Murali G
 *
 */

@Controller
public class ApptOnlineController {

	private Logger logger = Logger.getLogger(ApptOnlineController.class);

	@Autowired
	private ApptOnlineCacheComponent apptOnlineCacheComponent;
	@Autowired
	private IApptOnlineValidator apptOnlineValidator;
	@Autowired
	private FroentEndServiceUtils froentEndServiceUtils;
	@Autowired
	private IApptOnlineService apptOnlineService;

	@RequestMapping(value = "/landing", method = RequestMethod.GET)
	public ModelAndView getLandingPage(@RequestParam("client_code") String clientcode, @RequestParam(value = "lang_code", required = false) String lang_code, HttpServletRequest request) throws Exception {
		ModelMap modelMap = new ModelMap();
		String viewName = JSPPageNameConstants.ONLINE_APPT_LANDING_PAGE.getViewName();
		try {
			modelMap.addAttribute("ipAddress",request.getRemoteAddr());

			if (lang_code == null || "".equals(lang_code)) { //After discussing with Anantha, Added this one..
				lang_code = "us-en";
			}

			HttpSession session = request.getSession(false);
			if (null != session) {
				session.invalidate();
				session = null;
				session = request.getSession(true);
			} else {
				session = request.getSession(true);
			}
			ApptOnlineSessionData sessionData = new ApptOnlineSessionData();
			sessionData.setClientCode(clientcode);
			sessionData.setLangCode(lang_code);
			sessionData.setToken("");
			sessionData.setTransId("");
			LandingPageInfo landingPageInfo = apptOnlineCacheComponent.getLandingPageInfo(sessionData);
			//sessionData.setLangCode(landingPageInfo.getDefaultLangCode()); //After discussing with Anantha i have commented..
			sessionData.setClientName(landingPageInfo.getClientName());
			sessionData.setCssFileName(landingPageInfo.getCssFileName());
			sessionData.setLogoFileName(landingPageInfo.getLogoFileName());
			sessionData.setFooterContent(landingPageInfo.getFooterContent());
			sessionData.setFooterLinks(landingPageInfo.getFooterLinks());
			session.setAttribute(CommonContants.APPT_ONLINE_SESSION_DATA_SESSION_VARIABLE.getValue(), sessionData);
			modelMap.addAttribute("landingPageInfo",landingPageInfo);
		}catch (Exception e) {
			logger.error("Error  :" + e.getMessage(), e);
			viewName = JSPPageNameConstants.ONLINE_APPT_ERROR_PAGE.getViewName();
			StringBuilder inputData = new StringBuilder(" { ");
			inputData.append(" lang_code : ").append(lang_code);
			inputData.append(" } ");
			apptOnlineService.sendErrorEmail(clientcode,"landing",inputData.toString(), e);
		}
		return new ModelAndView(viewName,modelMap);
	}

	@RequestMapping(value = "/changelang", method = RequestMethod.GET)
	public ModelAndView changelang(@RequestParam(value = "lang_code") String lang_code, HttpServletRequest request) throws Exception {
		ModelMap modelMap = new ModelMap();
		String viewName = JSPPageNameConstants.ONLINE_APPT_LANDING_PAGE.getViewName();
		ApptOnlineSessionData sessionData = null;
		try {			
			sessionData = getApptOnlineSessionData(request);
			sessionData.setLangCode(lang_code);
			modelMap.addAttribute("landingPageInfo",apptOnlineCacheComponent.getLandingPageInfo(sessionData));
		}catch (Exception e) {
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);	
				viewName = JSPPageNameConstants.ONLINE_APPT_ERROR_PAGE.getViewName();
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append(" lang_code : ").append(lang_code);
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A", "changelang", inputData.toString(), e);
			}else{
				viewName = "redirect:"+JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName();
			}
		}
		return new ModelAndView(viewName,modelMap);
	}

	@RequestMapping(value = "/appt", method = RequestMethod.GET)
	public ModelAndView getApptPage(HttpServletRequest request) throws Exception {
		return getModelAndViewResponse(request,CommonContants.ONLINE_PAGE_CONTENT_APPT_METHOD_APPT.getValue(),"appt");
	}

	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public ModelAndView existingOrCancelAppts(HttpServletRequest request) throws Exception {
		return getModelAndViewResponse(request,CommonContants.ONLINE_PAGE_CONTENT_APPT_METHOD_CANCEL.getValue(),"cancel");
	}
	
	@RequestMapping(value = "/pledge_amount", method = RequestMethod.GET)
	public ModelAndView getPledgeAmount(HttpServletRequest request) throws Exception {
		return getModelAndViewResponse(request,CommonContants.ONLINE_PAGE_CONTENT_APPT_METHOD_PLEDGE_AMOUNT.getValue(),"pledge_amount");
	}

	@RequestMapping(value = "/upload_transcripts", method = RequestMethod.GET)
	public ModelAndView uploadTranscripts(HttpServletRequest request) throws Exception {
		return getModelAndViewResponse(request,CommonContants.ONLINE_PAGE_CONTENT_APPT_METHOD_UPLOAD_TRANSCRIPTS.getValue(),"update_transcripts");
	}
	
	private ModelAndView getModelAndViewResponse(HttpServletRequest request,String page_method_type,String requestURI){
		ModelMap modelMap = new ModelMap();
		String viewName = JSPPageNameConstants.ONLINE_APPT_LOGIN_PAGE.getViewName();
		ApptOnlineSessionData sessionData = null;
		try {
			sessionData = getApptOnlineSessionData(request);
			OnlinePageData onlinePageData = apptOnlineCacheComponent.getOnlinePageData(sessionData);

			Map<String,Object> jsonMap = apptOnlineService.getTransId(sessionData.getClientCode(),"",request.getRemoteAddr(),"","");
			if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){	
				sessionData.setTransId(String.valueOf(ApptOnlineUtils.getValue(jsonMap,"transId",Integer.class)));

				if(CommonContants.ONLINE_PAGE_CONTENT_APPT_METHOD_APPT.getValue().equals(page_method_type)) {
					if("Y".equals(onlinePageData.getSchedulerClosedNoFunding().getSchedulerClosed())){
						page_method_type = CommonContants.ONLINE_PAGE_CONTENT_APPT_METHOD_APPT_SCHEDULER_CLOSED.getValue();
					}else if("Y".equals(onlinePageData.getSchedulerClosedNoFunding().getNoFunding())){
						page_method_type = CommonContants.ONLINE_PAGE_CONTENT_APPT_METHOD_APPT_NO_FUNDING.getValue();
					}
				}
				sessionData.setCurrentPageIndex(0);
				sessionData.setPage_method_type(page_method_type);	            
				modelMap.addAttribute("sessionData",sessionData);
				modelMap.addAttribute("onlinePageData",onlinePageData);

				jsonMap = apptOnlineService.updateTransState(sessionData,String.valueOf(ApptOnlineUtils.getCurrentPageId(sessionData, onlinePageData)));
				if(!ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){	
					viewName = JSPPageNameConstants.ONLINE_APPT_ERROR_PAGE.getViewName();
				}			 
			}else{
				viewName = JSPPageNameConstants.ONLINE_APPT_ERROR_PAGE.getViewName();
			}            
		} catch (Exception e) {					
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);	
				viewName = JSPPageNameConstants.ONLINE_APPT_ERROR_PAGE.getViewName();
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append(" page_method_type : ").append(page_method_type);
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A",requestURI, inputData.toString(), e);
			}else{
				viewName = "redirect:"+JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName();
			}
		}
		return new ModelAndView(viewName,modelMap);
	}

	@RequestMapping(value = "/changePageIndex", method = RequestMethod.GET)
	public @ResponseBody String changePageIndex(@RequestParam String btnType, HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try {		
			if(btnType!=null && btnType.length()>0) {
				sessionData = getApptOnlineSessionData(request);
				if("Next".equalsIgnoreCase(btnType)){
					sessionData.setCurrentPageIndex(sessionData.getCurrentPageIndex()+1);
				}else if("Back".equalsIgnoreCase(btnType)){
					if(sessionData.getCurrentPageIndex()>0){
						sessionData.setCurrentPageIndex(sessionData.getCurrentPageIndex()-1);
					}
				}
				Map<String, Object> jsonMap = apptOnlineService.updateTransState(sessionData,String.valueOf(ApptOnlineUtils.getCurrentPageId(sessionData,apptOnlineCacheComponent.getOnlinePageData(sessionData))));
				if(!ApptOnlineUtils.getValue(jsonMap,"status",Boolean.class)){
					jsonResponse.setErrorStatus(true);
					jsonResponse.setErrorMessage("Error occured!");
				}
			}
			jsonResponse.setResponseStatus(true);
		} catch (Exception e) {
			jsonResponse.setDisplayErrorPage(true);			
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append(" BtnType : ").append(btnType);
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","changePageIndex", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}

	@RequestMapping(value = "/validateProcedureMatch", method = RequestMethod.GET)
	public @ResponseBody String validateProcedureMatch(@RequestParam String procedure,HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try {
			sessionData = getApptOnlineSessionData(request);
			jsonResponse = froentEndServiceUtils.validateProcedureMatch(sessionData,procedure);
		}catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);			
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append(" procedure : ").append(procedure);
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","validateProcedureMatch", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("validateProcedureMatch :::::  Front End :::::: Response Data :::::::: "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}

	@RequestMapping(value = "/getLocationPrimarySecondaryAvailability", method = RequestMethod.GET)
	public @ResponseBody String getLocationPrimarySecondaryAvailability(@RequestParam String procedure,HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try {
			sessionData = getApptOnlineSessionData(request);
			jsonResponse = froentEndServiceUtils.getLocationPrimarySecondaryAvailability(sessionData);
		}catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append(" procedure : ").append(procedure);
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","getLocationPrimarySecondaryAvailability", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("getLocationPrimarySecondaryAvailability :::::  Front End :::::: Response Data :::::::: "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}


	@RequestMapping(value = "/validateFormData", method = RequestMethod.POST)
	public @ResponseBody String validateFormData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try {
			sessionData = getApptOnlineSessionData(request);			  
			List<OnlinePageFields> pageFieldsList = ApptOnlineUtils.getCurrentPageFieldsList(sessionData,apptOnlineCacheComponent.getOnlinePageData(sessionData));
			Map<String,String> pageValidationMessages = apptOnlineCacheComponent.getPageValidationMessages(sessionData);
			String formValidationResponse = apptOnlineValidator.validate(request,pageFieldsList,pageValidationMessages,sessionData.getLangCode());
			if(formValidationResponse!=null && formValidationResponse.trim().length()>0){
				jsonResponse.setValidationStatus(false);
				jsonResponse.setValidationResponse(formValidationResponse);	               
			}else{
				jsonResponse.setValidationStatus(true);
			}
			jsonResponse.setFormFields(ApptOnlineUtils.getFormFields(pageFieldsList,true));
		} catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append(ApptOnlineUtils.getRequestParameters(request));
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","validateFormData", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("validateFormData :::::  Front End :::::: Response Data :::::::: "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}

	@RequestMapping(value = "/getOnlineNoLoginAuthenticate", method = RequestMethod.POST)
	public @ResponseBody String getOnlineNoLoginAuthenticate(HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try { 
			sessionData = getApptOnlineSessionData(request);	
			jsonResponse = froentEndServiceUtils.getOnlineNoLoginAuthenticate(sessionData,request);
		} catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append(ApptOnlineUtils.getRequestParameters(request));
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","getOnlineNoLoginAuthenticate", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("getOnlineNoLoginAuthenticate :::::  Front End :::::: Response Data :::::::: "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}

	@RequestMapping(value = "/getCustomerType", method = RequestMethod.GET)
	public @ResponseBody String getCustomerType(HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try {
			sessionData = getApptOnlineSessionData(request);	
			jsonResponse = froentEndServiceUtils.getCustomerType(sessionData);
		}catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append(ApptOnlineUtils.getRequestParameters(request));
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","getCustomerType", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("getCustomerType ::::::Front End :::::: Response Data::::::::   "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}

	@RequestMapping(value = "/getUtilityType", method = RequestMethod.GET)
	public @ResponseBody String getUtilityType(@RequestParam String customerTypeId,HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try {
			sessionData = getApptOnlineSessionData(request);
			jsonResponse = froentEndServiceUtils.getUtilityType(sessionData,customerTypeId);
		}catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append("customerTypeId : ").append(customerTypeId);
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","getUtilityType", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("getUtilityType ::::::Front End :::::: Response Data::::::::   "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}

	@RequestMapping(value = "/getServiceNameCustomerUtility", method = RequestMethod.GET)
	public @ResponseBody String getServiceNameCustomerUtility(@RequestParam String utilityTypeId,HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try {
			sessionData = getApptOnlineSessionData(request);
			jsonResponse = froentEndServiceUtils.getServiceNameCustomerUtility(getApptOnlineSessionData(request),utilityTypeId,null);
		}catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append("utilityTypeId : ").append(utilityTypeId);
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","getServiceNameCustomerUtility", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("getServiceNameCustomerUtility :::::Front End :::::: Response Data:::::::::   "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}

	@RequestMapping(value = "/holdFirstAvailAppointment", method = RequestMethod.GET)
	public @ResponseBody String holdFirstAvailAppointment(@RequestParam String serviceId,HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try {
			sessionData = getApptOnlineSessionData(request);
			jsonResponse = froentEndServiceUtils.holdFirstAvailAppointment(sessionData,serviceId);
		}catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append("serviceId : ").append(serviceId);
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","holdFirstAvailAppointment", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("holdFirstAvailAppointment ::::::Front End :::::: Response Data::::::::   "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}

	@RequestMapping(value = "/releaseHoldedAppointment", method = RequestMethod.GET)
	public @ResponseBody String releaseHoldedAppointment(HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try {
			sessionData = getApptOnlineSessionData(request);
			jsonResponse = froentEndServiceUtils.releaseHoldedAppointment(sessionData);
		}catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append("ScheduleId : ").append(sessionData!=null ? sessionData.getScheduleId() : "-1");
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","releaseHoldedAppointment", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("releaseHoldedAppointment ::::::Front End :::::: Response Data::::::::   "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}

	@RequestMapping(value = "/getHouseholdMonthlyIncome", method = RequestMethod.GET)
	public @ResponseBody String getHouseholdMonthlyIncome(@RequestParam String noOfPeople,HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try {
			sessionData = getApptOnlineSessionData(request);
			jsonResponse = froentEndServiceUtils.getHouseholdMonthlyIncome(sessionData,noOfPeople);
		}catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append("noOfPeople : ").append(noOfPeople);
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","getHouseholdMonthlyIncome", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("getHouseholdMonthlyIncome :::::::Front End :::::: Response Data:::::::   "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}

	@RequestMapping(value = "/getCustomerInfo", method = RequestMethod.GET)
	public @ResponseBody String getCustomerInfo(@RequestParam String pageId,HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try { 
			sessionData = getApptOnlineSessionData(request);
			jsonResponse = froentEndServiceUtils.getCustomerInfo(sessionData,pageId);
		} catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append("CustomerId :: ").append(String.valueOf(sessionData.getCustomerId()));
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","getCustomerInfo", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("getCustomerInfo :::::Front End :::::: Response Data:::::::::   "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}

	@RequestMapping(value = "/updateCustomerInfo", method = RequestMethod.POST)
	public @ResponseBody String updateCustomerInfo(HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try { 
			sessionData = getApptOnlineSessionData(request);
			jsonResponse = froentEndServiceUtils.updateCustomerInfo(getApptOnlineSessionData(request),request);
		} catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append(ApptOnlineUtils.getRequestParameters(request));
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","updateCustomerInfo", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("updateCustomerInfo :::::Front End :::::: Response Data:::::::::   "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}

	@RequestMapping(value = "/getListOfThingsToBring", method = RequestMethod.GET)
	public @ResponseBody String getListOfThingsToBring(HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try {
			sessionData = getApptOnlineSessionData(request);
			jsonResponse = froentEndServiceUtils.getListOfThingsToBring(sessionData);
		}catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append(ApptOnlineUtils.getRequestParameters(request));
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","getListOfThingsToBring", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("getListOfThingsToBring :::::Front End :::::: Response Data:::::::::   "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}

	@RequestMapping(value = "/confirmAppointment", method = RequestMethod.GET)
	public @ResponseBody String confirmAppointment(HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try {
			sessionData = getApptOnlineSessionData(request);
			jsonResponse = froentEndServiceUtils.confirmAppointment(sessionData);
		}catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append(ApptOnlineUtils.getRequestParameters(request));
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","confirmAppointment", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("confirmAppointment :::::Front End :::::: Response Data:::::::::   "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}

	@RequestMapping(value = "/getSessionDataJsonMap", method = RequestMethod.GET)
	public @ResponseBody String getSessionDataJsonMap(HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try {
			sessionData = getApptOnlineSessionData(request);
			jsonResponse.setJsonMap(sessionData.getJsonData());
		}catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append(ApptOnlineUtils.getRequestParameters(request));
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","getSessionDataJsonMap", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("getSessionDataJsonMap ::::::Front End :::::: Response Data::::::::   "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}
	
	@RequestMapping(value = "/getCancelLoginAuthenticate", method = RequestMethod.POST)
	public @ResponseBody String getCancelLoginAuthenticate(HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try { 
			sessionData = getApptOnlineSessionData(request);
			jsonResponse = froentEndServiceUtils.getCancelLoginAuthenticate(sessionData,request);
		} catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append(ApptOnlineUtils.getRequestParameters(request));
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","getCancelLoginAuthenticate", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("getCancelLoginAuthenticate :::::  Front End :::::: Response Data :::::::: "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}

	@RequestMapping(value = "/getBookedAppointments", method = RequestMethod.GET)
	public @ResponseBody String getBookedAppointments(HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try {
			sessionData = getApptOnlineSessionData(request);
			jsonResponse = froentEndServiceUtils.getBookedAppointments(sessionData);
		}catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append(ApptOnlineUtils.getRequestParameters(request));
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","getBookedAppointments", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("getBookedAppointments :::::Front End :::::: Response Data:::::::::   "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}

	@RequestMapping(value = "/cancelAppointment", method = RequestMethod.GET)
	public @ResponseBody String cancelAppointment(@RequestParam String scheduleId,HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try {
			sessionData = getApptOnlineSessionData(request);
			jsonResponse = froentEndServiceUtils.cancelAppointment(sessionData,scheduleId);
		}catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append("scheduleId :: ").append(scheduleId);
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","cancelAppointment", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("cancelAppointment ::::::Front End :::::: Response Data::::::::   "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}
	
	@RequestMapping(value = "/checkDuplicateAppts", method = RequestMethod.GET)
	public @ResponseBody String checkDuplicateAppts(@RequestParam String serviceId,HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try {
			sessionData = getApptOnlineSessionData(request);
			jsonResponse = froentEndServiceUtils.checkDuplicateAppts(sessionData,serviceId);
		}catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append("serviceId : ").append(serviceId);
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","holdFirstAvailAppointment", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("checkDuplicateAppts ::::::Front End :::::: Response Data::::::::   "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}

	@RequestMapping(value = "/getAvailableDatesCallcenter", method = RequestMethod.GET)
	public @ResponseBody String getAvailableDatesCallcenter(@RequestParam String serviceId,HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try {
			sessionData = getApptOnlineSessionData(request);
			sessionData.setServiceId(serviceId);
			jsonResponse = froentEndServiceUtils.getAvailableDatesCallcenter(sessionData);
		}catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append(ApptOnlineUtils.getRequestParameters(request));
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","getAvailableDatesCallcenter", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("getAvailableDatesCallcenter :::::Front End :::::: Response Data:::::::::   "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}
	
	@RequestMapping(value = "/getAvailableTimesCallcenter", method = RequestMethod.GET)
	public @ResponseBody String getAvailableTimesCallcenter(@RequestParam String apptDate,HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try {
			sessionData = getApptOnlineSessionData(request);
			jsonResponse = froentEndServiceUtils.getAvailableTimesCallcenter(sessionData,apptDate);
		}catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append(ApptOnlineUtils.getRequestParameters(request));
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","getAvailableTimesCallcenter", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("getAvailableTimesCallcenter :::::Front End :::::: Response Data:::::::::   "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}
	
	@RequestMapping(value = "/holdAppointmentCallCenter", method = RequestMethod.GET)
	public @ResponseBody String holdAppointmentCallCenter(@RequestParam String date,@RequestParam String time,HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try {
			sessionData = getApptOnlineSessionData(request);
			jsonResponse = froentEndServiceUtils.holdAppointmentCallCenter(sessionData,date,time);
		}catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append(ApptOnlineUtils.getRequestParameters(request));
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","holdAppointmentCallCenter", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("holdAppointmentCallCenter :::::Front End :::::: Response Data:::::::::   "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}
	
	@RequestMapping(value = "/getPledgeAmountLoginAuthenticate", method = RequestMethod.POST)
	public @ResponseBody String getPledgeAmountLoginAuthenticate(HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try { 
			sessionData = getApptOnlineSessionData(request);
			jsonResponse = froentEndServiceUtils.getPledgeAmountLoginAuthenticate(sessionData,request);
		} catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append(ApptOnlineUtils.getRequestParameters(request));
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","getCancelLoginAuthenticate", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("getPledgeAmountLoginAuthenticate :::::  Front End :::::: Response Data :::::::: "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}
	
	@RequestMapping(value = "/getPledgeHistory", method = RequestMethod.GET)
	public @ResponseBody String getPledgeHistory(HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try {
			sessionData = getApptOnlineSessionData(request);
			jsonResponse = froentEndServiceUtils.getPledgeHistory(sessionData);
		}catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append(ApptOnlineUtils.getRequestParameters(request));
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","getPledgeHistory", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("getPledgeHistory :::::Front End :::::: Response Data:::::::::   "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}
	
	//OPTION -1
	/*@RequestMapping(value = "/getPledgeAwardLetter", method = RequestMethod.GET)
	public ModelAndView getPledgeAwardLetter(@RequestParam String customerPledgeId,HttpServletRequest request) throws Exception {
		ModelMap modelMap = new ModelMap();
		String viewName = JSPPageNameConstants.ONLINE_APPT_PLEDGE_AWARD_LETER_PAGE.getViewName();
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try {
			sessionData = getApptOnlineSessionData(request);
			jsonResponse = froentEndServiceUtils.getPledgeAwardLetter(sessionData,customerPledgeId);
			modelMap.addAttribute("sessionData",sessionData);
			modelMap.addAttribute("jsonResponse",jsonResponse);
			//String htmlFilePath = "/var/www/anantha/data/www/development.itfrontdesk.com/awardletters/";
			Writer writer = null;
			System.out.println("Writing to File path ::: /var/www/anantha/data/www/development.itfrontdesk.com/awardletters/pledgeawardletter.html ");
			try {
			    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/var/www/anantha/data/www/development.itfrontdesk.com/awardletters/pledgeawardletter.html"), "utf-8"));
			    writer.write((String)jsonResponse.getJsonMap().get("pledgeLetterBody"));
			} catch (IOException ex) {
			  ex.printStackTrace();
			} finally {
			   try {
				   writer.close();
				} catch (Exception ex) {
				   ex.printStackTrace();
			   }
			}
			logger.debug("getPledgeAwardLetter :::::Front End :::::: Response Data:::::::::   "+ApptOnlineUtils.getJSONStr(jsonResponse));
		} catch (Exception e) {			
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				viewName = JSPPageNameConstants.ONLINE_APPT_ERROR_PAGE.getViewName();
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append(ApptOnlineUtils.getRequestParameters(request));
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","getPledgeAwardLetter", inputData.toString(), e);
			}else{
				viewName = "redirect:"+JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName();
			}
		}
		System.out.println("Accessing URL ::: redirect:../awardletters/pledgeawardletter.html");
		viewName= "redirect:../awardletters/pledgeawardletter.html";
		return new ModelAndView(viewName,modelMap);
	}*/
	
	//OPTION - 2
	@RequestMapping(value = "/getPledgeAwardLetter", method = RequestMethod.GET)
	public void  getPledgeAwardLetter(@RequestParam String customerPledgeId,HttpServletRequest request,HttpServletResponse response) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try {
			sessionData = getApptOnlineSessionData(request);
			jsonResponse = froentEndServiceUtils.getPledgeAwardLetter(sessionData,customerPledgeId);
			String pledgeLetterBody = (String)jsonResponse.getJsonMap().get("pledgeLetterBody");
			
			InputStream htmlInputStream = new ByteArrayInputStream(pledgeLetterBody.getBytes());
			ByteArrayOutputStream output = new ByteArrayOutputStream();
		    // step 1
	        Document document = new Document();
	        // step 2	       
	        PdfWriter writer = PdfWriter.getInstance(document,output);
	        // step 3
	        document.open();
	        // step 4
	        XMLWorkerHelper.getInstance().parseXHtml(writer, document,htmlInputStream); 
	        //step 5
	        writer.flush();
	        writer.close();
	        
	        document.close();
	       
			byte[] data = output.toByteArray();
			response.setContentType("application/pdf");
	        //response.setHeader("Content-disposition", "attachment; filename=AwardLetter.pdf");
			response.setHeader("Content-Disposition", "inline; filename=AwardLetter.pdf");
	        response.setContentLength(data.length);
	        
	        response.getOutputStream().write(data);
	        response.getOutputStream().flush();
		} catch (Exception e) {			
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append(ApptOnlineUtils.getRequestParameters(request));
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","getPledgeAwardLetter", inputData.toString(), e);
			}
		}
	}
	
	@RequestMapping(value = "/getLocations", method = RequestMethod.GET)
	public @ResponseBody String getLocations(HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try {
			sessionData = getApptOnlineSessionData(request);
			jsonResponse = froentEndServiceUtils.getLocations(sessionData);
		}catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append(ApptOnlineUtils.getRequestParameters(request));
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","getLocations", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("getLocations :::::Front End :::::: Response Data:::::::::   "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}
	
	@RequestMapping(value = "/getServicesCallcenter", method = RequestMethod.GET)
	public @ResponseBody String getServicesCallcenter(@RequestParam String locationId,HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try {
			sessionData = getApptOnlineSessionData(request);
			sessionData.setLocationId(locationId);
			jsonResponse = froentEndServiceUtils.getServicesCallcenter(sessionData);
		}catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append(ApptOnlineUtils.getRequestParameters(request));
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","getServicesCallcenter", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("getServicesCallcenter :::::Front End :::::: Response Data:::::::::   "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}
	
	@RequestMapping(value = "/updateSessionData", method = RequestMethod.GET)
	public @ResponseBody String updateSessionData(@RequestParam String sessionVariableName,@RequestParam String sessionVariableValue,HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try {
			sessionData = getApptOnlineSessionData(request);
			if("locationId".equals(sessionVariableName)){
				sessionData.setLocationId(sessionVariableValue);
			}else if("serviceId".equals(sessionVariableName)){
				sessionData.setServiceId(sessionVariableValue);
			}
		}catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append(ApptOnlineUtils.getRequestParameters(request));
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","getSessionDataJsonMap", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("getSessionDataJsonMap ::::::Front End :::::: Response Data::::::::   "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}
	
	@RequestMapping(value = "/authenticateAndUpdateCustomer", method = RequestMethod.POST)
	public @ResponseBody String authenticateAndUpdateCustomer(HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try { 
			sessionData = getApptOnlineSessionData(request);	
			jsonResponse = froentEndServiceUtils.authenticateAndUpdateCustomer(sessionData,request);
		} catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append(ApptOnlineUtils.getRequestParameters(request));
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","authenticateAndUpdateCustomer", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("authenticateAndUpdateCustomer :::::  Front End :::::: Response Data :::::::: "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}
	
	@RequestMapping(value = "/loginAuthenticateAndUpdateForExternal", method = RequestMethod.POST)
	public @ResponseBody String loginAuthenticateAndUpdateForExternal(HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try { 
			sessionData = getApptOnlineSessionData(request);	
			jsonResponse = froentEndServiceUtils.loginAuthenticateAndUpdateForExternal(sessionData,request);
		} catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append(ApptOnlineUtils.getRequestParameters(request));
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","loginAuthenticateAndUpdateForExternal", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("loginAuthenticateAndUpdateForExternal :::::  Front End :::::: Response Data :::::::: "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}
	
	@RequestMapping(value = "/loginAuthenticateForExternal", method = RequestMethod.POST)
	public @ResponseBody String loginAuthenticateForExternal(HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try { 
			sessionData = getApptOnlineSessionData(request);	
			jsonResponse = froentEndServiceUtils.loginAuthenticateForExternal(sessionData,request);
		} catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append(ApptOnlineUtils.getRequestParameters(request));
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","loginAuthenticateForExternal", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("loginAuthenticateForExternal :::::  Front End :::::: Response Data :::::::: "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}
	
	@RequestMapping(value = "/confirmAppointmentExternalLogic", method = RequestMethod.GET)
	public @ResponseBody String confirmAppointmentExternalLogic(HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try {
			sessionData = getApptOnlineSessionData(request);
			jsonResponse = froentEndServiceUtils.confirmAppointmentExternalLogic(sessionData);
		}catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append(ApptOnlineUtils.getRequestParameters(request));
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","confirmAppointmentExternalLogic", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("confirmAppointmentExternalLogic :::::Front End :::::: Response Data:::::::::   "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}
	
	@RequestMapping(value = "/cancelAppointmentExternalLogic", method = RequestMethod.GET)
	public @ResponseBody String cancelAppointmentExternalLogic(@RequestParam String scheduleId,HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try {
			sessionData = getApptOnlineSessionData(request);
			jsonResponse = froentEndServiceUtils.cancelAppointmentExternalLogic(sessionData,scheduleId);
		}catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append("scheduleId :: ").append(scheduleId);
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","cancelAppointmentExternalLogic", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("cancelAppointmentExternalLogic ::::::Front End :::::: Response Data::::::::   "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}
	
	@RequestMapping(value = "/saveTransScriptMsg", method = RequestMethod.POST)
	public @ResponseBody String saveTransScriptMsg(@RequestParam(required=false) String scheduleId,@RequestParam("file") MultipartFile[] files,HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try {
			sessionData = getApptOnlineSessionData(request);
			if(scheduleId!=null && scheduleId.length()>0){
				//nothing to do
			}else{
				if(sessionData!=null){
					scheduleId = sessionData.getScheduleId();
				} else{
					scheduleId = "-1";
				}
			}			
			jsonResponse = froentEndServiceUtils.saveTransScriptMsg(sessionData,scheduleId,files);
		}catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append("files :: ").append(files);
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","saveTransScriptMsg", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("saveTransScriptMsg ::::::Front End :::::: Response Data::::::::   "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}
	
	@RequestMapping(value = "/deleteTransScriptMsg", method = RequestMethod.GET)
	public @ResponseBody String deleteTransScriptMsg(@RequestParam String transScriptMSGId,@RequestParam String fileName,HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try {
			sessionData = getApptOnlineSessionData(request);
			jsonResponse = froentEndServiceUtils.deleteTransScriptMsg(sessionData,transScriptMSGId,fileName);
		}catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append("transScriptMSGId :: ").append(transScriptMSGId);
				inputData.append("fileName :: ").append(fileName);
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","deleteTransScriptMsg", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("deleteTransScriptMsg ::::::Front End :::::: Response Data::::::::   "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}
	
	@RequestMapping(value = "/getTransScriptMsgs", method = RequestMethod.GET)
	public @ResponseBody String getTransScriptMsgs(@RequestParam String scheduleId,HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try {
			sessionData = getApptOnlineSessionData(request);
			jsonResponse = froentEndServiceUtils.getTransScriptMsgs(sessionData,scheduleId);
		}catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append("scheduleId :: ").append(scheduleId);
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","getTransScriptMsgs", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("getTransScriptMsgs ::::::Front End :::::: Response Data::::::::   "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}
	
	@RequestMapping(value = "/error", method = RequestMethod.GET)
	public String error(HttpServletRequest request) throws Exception {
		return JSPPageNameConstants.ONLINE_APPT_ERROR_PAGE.getViewName();
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		String viewName = "";
		try {
			HttpSession session = request.getSession(false);
			if (session != null) {
				ApptOnlineSessionData sessionData = null;
				sessionData = getApptOnlineSessionData(request);

				if(sessionData.getScheduleId()!=null && sessionData.getScheduleId()!=""){
					try{
						if(Integer.parseInt(sessionData.getScheduleId())>1){ //As per Anantha discussion
							froentEndServiceUtils.releaseHoldedAppointment(sessionData);
						}
					}catch(Exception e){
						//nothing to do..
					}
				}
				OnlinePageData onlinePageData = apptOnlineCacheComponent.getOnlinePageData(sessionData);
				viewName = onlinePageData.getLogoutURL();				
				if(viewName!=null && !"".equals(viewName)){
					viewName = "redirect:"+viewName;
				}else{
					viewName = "redirect:"+JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName();
				}				
				session.removeAttribute(CommonContants.APPT_ONLINE_SESSION_DATA_SESSION_VARIABLE.getValue());
				session.invalidate();
			}
		} catch (Exception e) {
			viewName = "redirect:"+JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName();
			logger.error("Error : "+e.getMessage(), e);
		}
		return viewName;
	}

	@RequestMapping(value = "/session-expired", method = RequestMethod.GET)
	public ModelAndView sessionExpired(ModelMap map, HttpServletRequest request, HttpServletResponse response){
		String viewName = "/session-expired";
		ModelMap modelMap = new ModelMap();
		try {
			/*HttpSession session = request.getSession(false);
			if (session != null) {
				session.invalidate();
			}*/
		} catch (Exception e) {
			logger.error("Error :" + e, e);
		}
		return new ModelAndView(viewName, modelMap);
	}

	@RequestMapping(value = "/activateSession", method = RequestMethod.GET)
	public @ResponseBody String activateSession(HttpServletRequest request) throws Exception {
		JsonResponse jsonResponse = new JsonResponse();
		ApptOnlineSessionData sessionData = null;
		try {
			sessionData = getApptOnlineSessionData(request);
			Map<String,Object> jsonMap = apptOnlineService.extendHoldApptTime(sessionData);
			if(ApptOnlineUtils.getValue(jsonMap,"status", Boolean.class)){	
				jsonResponse.setJsonMap(jsonMap);
			}else{
				jsonResponse.setDisplayErrorPage(true);
			}
		}catch (Exception e) {			
			jsonResponse.setDisplayErrorPage(true);
			if(sessionData!=null){
				logger.error("Error  :" + e.getMessage(), e);
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_ERROR_PAGE.getViewName());
				StringBuilder inputData = new StringBuilder(" { ");
				inputData.append(" ");
				inputData.append(" } ");
				apptOnlineService.sendErrorEmail(sessionData!=null ? sessionData.getClientCode() : "N.A","activateSession", inputData.toString(), e);
			}else{
				jsonResponse.setErrorPage(JSPPageNameConstants.ONLINE_APPT_FRONTEND_INDEX_PAGE.getViewName());
			}
		}
		logger.debug("activateSession ::::::Front End :::::: Response Data::::::::   "+ApptOnlineUtils.getJSONStr(jsonResponse));
		return ApptOnlineUtils.getJSONStr(jsonResponse);
	}

	@RequestMapping(value = "/clearCache", method = RequestMethod.GET)
	public @ResponseBody String clearCache(@RequestParam(value = "clientcode", required = false) String clientCode,HttpServletRequest request) throws Exception {
		String appendMsg = "client(s)";
		try {
			boolean cleared = false;
			if(ApptOnlineUtils.isNullOrEmpty(clientCode)) {
				cleared = apptOnlineCacheComponent.clearCache();
				appendMsg = "all the clients";
			}else{
				cleared = apptOnlineCacheComponent.clearCache(clientCode);	
				appendMsg = "clientCode : "+clientCode;
			}
			logger.debug("Cleared Cache data : "+cleared);
			if(cleared){
				return "success ::::::: Successfully cleared cache data for "+appendMsg;
			}
		}catch (Exception e) {
			logger.error("Error  :" + e.getMessage(), e);
		}
		return "Error :  Error while clearing cache data for  "+appendMsg;
	}

	private ApptOnlineSessionData getApptOnlineSessionData(HttpServletRequest request) throws Exception {
		ApptOnlineSessionData sessionData = null;
		try {
			HttpSession session = request.getSession(false);
			sessionData = (ApptOnlineSessionData) session.getAttribute(CommonContants.APPT_ONLINE_SESSION_DATA_SESSION_VARIABLE.getValue());
			return sessionData;
		} catch (Exception e) {
			if(!(e instanceof NullPointerException)) {
				logger.error("Error  :" + e.getMessage(), e);
			}
			throw new Exception("Session Error!!!");
		}		
	}	
}