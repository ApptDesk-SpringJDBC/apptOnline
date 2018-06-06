package com.telappoint.apptonline.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.telappoint.apptonline.common.CommonContants;
import com.telappoint.apptonline.model.ApptOnlineSessionData;
import com.telappoint.apptonline.model.FieldMsg;
import com.telappoint.apptonline.model.OnlinePageData;
import com.telappoint.apptonline.model.OnlinePageFields;


/**
 * 
 * @author Murali
 *
 */
public class ApptOnlineUtils {

	private static final Logger logger = Logger.getLogger(ApptOnlineUtils.class);

	public static Object getPropertyValue(Object object,String fieldName) throws NoSuchFieldException {
		try {
			BeanInfo info = Introspector.getBeanInfo(object.getClass());
			for (PropertyDescriptor pd : info.getPropertyDescriptors())
				if (pd.getName().equals(fieldName)) {
					Method getter = pd.getReadMethod();
					if(getter != null) {
						getter.setAccessible(true);
						return getter.invoke(object, null);
					}

				}
		} catch (Exception e) {
			throw new NoSuchFieldException(object.getClass() + " has no field " + fieldName);
		}
		return "";
	}

	public static void setPropertyValue(Object object, String propertyName, Object propertyValue) throws Exception {
		try {
			BeanInfo bi = Introspector.getBeanInfo(object.getClass());
			PropertyDescriptor pds[] = bi.getPropertyDescriptors();
			for (PropertyDescriptor pd : pds) {
				if (pd.getName().equals(propertyName)) {
					Method setter = pd.getWriteMethod();
					if (setter != null) {
						setter.invoke(object, new Object[] { propertyValue });
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public static boolean isEmpty(String string) {
		return string != null ? string.length() > 0 ? true : false : false;
	}

	public static Object getInitCaseValue(Object value) {
		if(value!=null) {
			String name = (String) value;
			StringBuilder nameBuilder = new StringBuilder();
			String[] nameStrs = name.split("\\s+");
			if(nameStrs!=null && nameStrs.length>0){            		 
				for(String nameStr : nameStrs){
					if(nameStr!=null && !" ".equals(nameStr) && nameStr.length()>1){
						nameBuilder.append(nameStr.substring(0,1)!=null ? nameStr.substring(0, 1).toUpperCase() : "");
						nameBuilder.append(nameStr.substring(1));
						nameBuilder.append(" ");
					}
				}            		  
			}	  
			if(nameBuilder.toString()!=null && !"".equals(nameBuilder.toString().trim())){
				value = nameBuilder.toString().trim(); 
			}
		}
		return value;
	}


	public static String getJSONString(Object object) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		String response = gson.toJson(object);
		return "<br/>InputParameters: ["+response+"]";
	}

	public static String getJSONStr(Object object) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.toJson(object);
	}



	public static String getNumbersOnlyFromMixedString(String mixedString){
		return mixedString.replaceAll("[^0-9]", "");
	}

	public static String getFormFields(List<OnlinePageFields> pageFieldsList,boolean includeTextBox){

		StringBuilder formFields = new StringBuilder();
		if(pageFieldsList!=null && pageFieldsList.size()>0){
			boolean initial = true;			
			if(includeTextBox){
				for(OnlinePageFields pageField : pageFieldsList){	
					if(!initial){
						formFields.append("|");
					}else{
						initial = false;
					}					
					formFields.append(pageField.getFieldName());
				}
			}else{
				String displayType = "";
				for(OnlinePageFields pageField : pageFieldsList){	
					displayType = pageField.getDisplayType();					
					if (displayType != null && !displayType.contains("button") && !displayType.contains("textbox") && !displayType.contains("select")) {
						if(!initial){
							formFields.append("|");
						}else{
							initial = false;
						}					
						formFields.append(pageField.getFieldName());
					}
				}
			}			
		}
		return formFields.toString();		
	}

	public static String getFormData(HttpServletRequest request,List<OnlinePageFields> pageFieldsList){
		StringBuilder formData = new StringBuilder();
		String fieldName = "";
		String displayType = "";
		Object fieldValue = null;
		StringBuilder splitFieldsData = null;
		String[] displayTypes = null;
		boolean initial = true;

		for (OnlinePageFields pageField : pageFieldsList) {			
			displayType = pageField.getDisplayType();

			if (displayType != null && !displayType.contains("button") && (displayType.contains("textbox") || displayType.contains("radio") || displayType.contains("select"))) {
				fieldName = pageField.getFieldName();
				if(displayType.contains("textbox-")){
					String seperator = "";
					if(pageField.getValidationRules().contains("dob")){
						seperator = "/";
					}
					splitFieldsData = new StringBuilder();
					displayTypes = displayType.split("-");
					if(displayTypes!=null){
						boolean init = true;
						for(int i=1;i<=displayTypes.length;i++){
							if(request.getParameter(fieldName+i)!=null && ((String)request.getParameter(fieldName+i)).length()>0){
								if(!init){
									splitFieldsData.append(seperator);
								}else{
									init = false;
								}
								splitFieldsData.append((String)request.getParameter(fieldName+i));																
							}
						}
						if(splitFieldsData.toString()!=null && splitFieldsData.toString().length()>0){
							fieldValue = splitFieldsData.toString();
						}
					}
				} else{
					fieldValue = request.getParameter(fieldName);
				}

				if(!initial){
					formData.append("|");
				}else{
					initial = false;
				}				
				// loginParam data format: fieldId1~fieldValue| etc
				formData.append(pageField.getFieldId()+"~"+fieldValue);
			}
		}
		return formData.toString();		
	}

	public static List<OnlinePageFields> getCurrentPageFieldsList(ApptOnlineSessionData sessionData,OnlinePageData onlinePageData){
		List<String> onlinePageIdsList = onlinePageData.getOnlinePageContentMapIds().get(sessionData.getPage_method_type());
		String currentPageId = onlinePageIdsList.get(sessionData.getCurrentPageIndex());
		return  onlinePageData.getOnlinePageFieldsMap().get(currentPageId);
	}

	public static String getFieldDisplayType(ApptOnlineSessionData sessionData,String field,OnlinePageData onlinePageData){
		if(field!=null && field.length()>0){
			List<OnlinePageFields> pageFields = getCurrentPageFieldsList(sessionData,onlinePageData);
			if(pageFields!=null && pageFields.size()>0){
				for(OnlinePageFields pageField : pageFields){
					if(field.equals(pageField.getFieldName())){
						return pageField.getDisplayType();
					}
				}
			}
		}
		return "";		
	}

	public static void setFieldMsg(ApptOnlineSessionData sessionData,Set<FieldMsg> pageMessages,String field,String msg,OnlinePageData onlinePageData){
		if(field!=null && field.length()>0){
			FieldMsg fieldMsg = new FieldMsg(field,msg);
			List<OnlinePageFields> pageFields = getCurrentPageFieldsList(sessionData,onlinePageData);
			if(pageFields!=null && pageFields.size()>0){
				for(OnlinePageFields pageField : pageFields){
					if(field.equals(pageField.getFieldName())){
						fieldMsg.setDisplay(pageField.getDisplay());
						fieldMsg.setDisplayType(pageField.getDisplayType());
					}
				}
			}
			pageMessages.add(fieldMsg);
		}	
	}

	public static boolean isNullOrEmpty(String string) {
		return string == null || string.length() == 0;
	}

	public static boolean isNotEmpty(String string) {
		return !isNullOrEmpty(string);
	}

	public static String populateBasicRequestParamsDetails(ApptOnlineSessionData sessionData,String endPointUrl) {
		String url = FileUtils.getEndPointURL() + endPointUrl;
		url = url.replaceAll("@clientCodeParam@",sessionData.getClientCode());
		url = url.replaceAll("@deviceParam@",CommonContants.ONLINE.getValue());
		url = url.replaceAll("@langCodeParam@",sessionData.getLangCode());
		url = url.replaceAll("@tokenParam@", sessionData.getToken());	
		url = url.replaceAll("@transIdParam@", sessionData.getTransId());
		return url;
	}

	public static void populateBasicRequestData(ApptOnlineSessionData sessionData,Map<String, Object> jsonMap) {
		jsonMap.put("clientCode",sessionData.getClientCode());
		jsonMap.put("device",CommonContants.ONLINE.getValue());
		jsonMap.put("langCode",sessionData.getLangCode());
		jsonMap.put("transId",sessionData.getTransId());
	}

	public static <T> T getValue(Map<String,Object> map, String key ,Class<T> type) {
		return type.cast(map.get(key));
	}

	@SuppressWarnings("unchecked")
	public static <T> T getValueFromJSONString(Object json, String key ,Class<T> type) {
		return type.cast(((LinkedHashMap<String,Object>)json).get(key));
	}

	public static String getJSONString(Map<String,Object> jsonMsp) {
		StringBuilder jsonString = new StringBuilder();
		if(jsonMsp!=null && jsonMsp.size()>0){
			boolean initial =  true;			
			jsonString.append("{");
			for(String key : jsonMsp.keySet()){
				if(!initial){
					jsonString.append(", ");
				}else{
					initial =  false;
				}
				jsonString.append("\"").append(key).append("\":\"").append(jsonMsp.get(key)).append("\"");

			}
			jsonString.append("}");
		}
		return jsonString.toString();
	}

	@SuppressWarnings("rawtypes")
	public static String getStringData(List list,String separator){
		StringBuilder sb = new StringBuilder();
		boolean initial = true;
		for (Object obj : list) {
			if(!initial){
				sb.append(separator).append(obj);
			}else{
				sb.append(obj);
				initial = false;
			}
		}
		return sb.toString();
	}

	public static String getPageValidationMessage(Map<String,String> pageValidationMessages,String key,String lang_code){
		if(pageValidationMessages.containsKey(lang_code+"|"+key)){
			return pageValidationMessages.get(lang_code+"|"+key);
		}else{
			return key; //+" - "+lang_code; //+" - Key Not found";
		}		
	}

	@SuppressWarnings("rawtypes")
	public static String getValueIfNotNullOtherwisegetDefaultValue(Map<String,Object> map, String key ,Class dataType, String defaultVal) {
		return String.valueOf((map.get(key)!=null) ? dataType.cast(map.get(key)) : defaultVal);
	}

	public static int getCurrentPageId(ApptOnlineSessionData sessionData,OnlinePageData onlinePageData) {
		List<String>  method_type_page_ids_list = onlinePageData.getOnlinePageContentMapIds().get(sessionData.getPage_method_type());
		if(method_type_page_ids_list!=null && method_type_page_ids_list.size()>0){
			return Integer.parseInt(method_type_page_ids_list.get(sessionData.getCurrentPageIndex()));
		}
		return 0;
	}

	@SuppressWarnings("rawtypes")
	public static String getRequestParameters(HttpServletRequest request) {
		boolean initail = true;
		StringBuilder requestData = new StringBuilder();
		try{
			Enumeration paramNames = request.getParameterNames();
			while(paramNames.hasMoreElements()) {
				String paramName = (String)paramNames.nextElement();
				String[] paramValues = request.getParameterValues(paramName);
				if(!initail){
					requestData.append(" , ");
				}
				requestData.append(paramName).append(" : ").append(paramValues[0]);
				initail = false;
			}
		}catch(Exception e){
			logger.error("Error ::::::: "+e.getMessage(),e);
		}
		return requestData.toString();
	}
}
