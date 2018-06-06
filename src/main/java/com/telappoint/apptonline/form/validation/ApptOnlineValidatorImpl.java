package com.telappoint.apptonline.form.validation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.telappoint.apptonline.common.FormValidationConstants;
import com.telappoint.apptonline.model.FieldMsg;
import com.telappoint.apptonline.model.OnlinePageFields;
import com.telappoint.apptonline.utils.ApptOnlineUtils;

/**
 * 
 * @author Murali
 */

@Service
public class ApptOnlineValidatorImpl implements IApptOnlineValidator {
	private Logger logger = Logger.getLogger(ApptOnlineValidatorImpl.class);
 
	public String validate(HttpServletRequest request,List<OnlinePageFields> pageFieldsList,Map<String,String> pageValidationMessages,String lanng_code) {
		List<FieldMsg> fieldErrors = new ArrayList<FieldMsg>();

		String fieldValueStr = null;
		String maxChars = null;
		String minChars = null;
		String[] displayTypes = null;
		StringBuilder splitFieldsData = null;
		try {			
			/*Iterating through LoginDetails list*/
			for (OnlinePageFields pageField : pageFieldsList) {
				String displayType = pageField.getDisplayType();
								
				if (displayType != null && !displayType.contains("button")  && !displayType.contains("radio")) {
					maxChars = pageField.getValidateMaxChars();
					minChars = pageField.getValidateMinChars();					
					String display = pageField.getDisplay();
					String emptyErrorMsg = ApptOnlineUtils.getPageValidationMessage(pageValidationMessages,pageField.getEmptyErrorMsg(),lanng_code);
					String invalidErrorMsg = ApptOnlineUtils.getPageValidationMessage(pageValidationMessages,pageField.getInvalidErrorMsg(),lanng_code);
					String fieldName = pageField.getFieldName();
					
					Object fieldValue = null;
					if(displayType.contains("textbox-")){
						splitFieldsData = new StringBuilder();
						displayTypes = displayType.split("-");
						if(displayTypes!=null){
							for(int i=1;i<=displayTypes.length;i++){
								if(request.getParameter(fieldName+i)!=null && ((String)request.getParameter(fieldName+i)).length()>0){
									splitFieldsData.append((String)request.getParameter(fieldName+i));
								}
							}
							if(splitFieldsData.toString()!=null && splitFieldsData.toString().length()>0){
								fieldValue = splitFieldsData.toString();
							}
						}
					}else{
						fieldValue = request.getParameter(fieldName);
					}
						
					if(fieldValue!=null) {					
						if (fieldValue instanceof String) {
							fieldValueStr = (String) fieldValue;
						}
						
						if(!"Y".equals(pageField.getRequired()) && (fieldValueStr==null || "".equals(fieldValueStr))){
							continue;
						}
						
						if(("Y".equals(pageField.getRequired()) || (fieldValue!=null && fieldValueStr!=null && !"".equals(fieldValueStr)))) {
							/*Checking that validation is required or not for this field */
							if (displayType != null && !pageField.getValidationRules().contains("alpha") && (displayType.contains("textbox-")|| pageField.getValidationRules().contains("phone") || pageField.getValidationRules().contains("numeric"))) {
								if(pageField.getValidationRules().contains("phone")) {
									if ("".equals(fieldValueStr.trim()) || fieldValueStr == null) {
										fieldErrors.add(new FieldMsg(fieldName, emptyErrorMsg,display,displayType));
									}
									else{
										check(fieldValue.toString(), FormValidationConstants.PHONE.getValidateExp(), fieldName, invalidErrorMsg, fieldErrors,display,displayType);
									}
								}								
								else if ( minChars != null && fieldValueStr.length() < Integer.parseInt(minChars) ) {
									fieldErrors.add(new FieldMsg(fieldName, invalidErrorMsg,display,displayType));
								}
								else if ( maxChars != null && fieldValueStr.length() > Integer.parseInt(maxChars) ) {
									fieldErrors.add(new FieldMsg(fieldName, invalidErrorMsg,display,displayType));
								}
								else if(pageField.getValidationRules().contains("numeric")) {
									check(fieldValue.toString(), FormValidationConstants.NUMERIC.getValidateExp(), fieldName, invalidErrorMsg, fieldErrors,display,displayType);
								}else if(pageField.getValidationRules().contains("dob")) {
									check(fieldValue.toString(), FormValidationConstants.NUMERIC.getValidateExp(), fieldName, invalidErrorMsg, fieldErrors,display,displayType);
									validateDOBDate(fieldValue.toString(), fieldName, invalidErrorMsg, fieldErrors,display,displayType);
								}
							} else if(!(fieldName.equals("date") || fieldName.equals("Date&Time"))) {
								if ("".equals(fieldValueStr.trim()) || fieldValueStr == null) {
									fieldErrors.add(new FieldMsg(fieldName, emptyErrorMsg,display,displayType));
								} 
								else if ( minChars != null && fieldValueStr.length() < Integer.parseInt(minChars) ) {
									fieldErrors.add(new FieldMsg(fieldName, invalidErrorMsg,display,displayType));
								}
								
								else if(maxChars != null && fieldValueStr.length() > Integer.parseInt(maxChars)) {
									fieldErrors.add(new FieldMsg(fieldName, invalidErrorMsg,display,displayType));
								} else {										
									boolean isValid = FormValidationUtils.validateFieldValue(pageField.getValidationRules(),fieldValue.toString());
									if(!isValid) {
										fieldErrors.add(new FieldMsg(fieldName, invalidErrorMsg,display,displayType));
									}
								}
							}							
						}else{
							fieldErrors.add(new FieldMsg(fieldName, emptyErrorMsg,display,displayType));
						}
					}else{
						if("Y".equals(pageField.getRequired())){
							fieldErrors.add(new FieldMsg(fieldName, emptyErrorMsg,display,displayType));
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error:" + e, e);
			throw e;
		}
		if(fieldErrors.size()>0){
			return ApptOnlineUtils.getJSONStr(fieldErrors);
		}else{
			return "";
		}
	}
	
	private void check(String fieldValue, String reg, String fieldName, String message,List<FieldMsg> fieldErrors,String display,String displayType) {
		fieldValue = fieldValue.replaceAll(FormValidationConstants.HYPHEN.getValidateExp(), "");
		if(!fieldValue.matches(reg)) {
			fieldErrors.add(new FieldMsg(fieldName, message,display,displayType));
		}
	}
	
	private void validateDOBDate(String fieldValue, String fieldName, String message,List<FieldMsg> fieldErrors,String display,String displayType) {
		if(fieldValue!=null && fieldValue.length()==8) { //considering 8 characters
			String datePattern = "[0-9]{8}+";
		    boolean isValid = fieldValue.matches(datePattern);
		    if(isValid){	
		    	isValid = isValidDate(fieldValue);
		    	if(isValid){
					String MMDDYYYY_DATE_FORMAT = "MMddyyyy";
					Date dob = null;
					try {
						DateFormat dateFormat = new SimpleDateFormat(MMDDYYYY_DATE_FORMAT);
						dob = dateFormat.parse(fieldValue);
						
						Date today = new Date();
						if(today.after(dob)){
							//nothing to do
						}else{
							fieldErrors.add(new FieldMsg(fieldName, message,display,displayType));
						}
						
					} catch (Exception e) {
						fieldErrors.add(new FieldMsg(fieldName, message,display,displayType));
					}
		    	}else{
					fieldErrors.add(new FieldMsg(fieldName, message,display,displayType));
				}
			}else{
				fieldErrors.add(new FieldMsg(fieldName, message,display,displayType));
			}
	    }else{
	    	fieldErrors.add(new FieldMsg(fieldName, message,display,displayType));
		}
	}
	
	private static boolean isValidDate(String date) {
		int month = Integer.parseInt(date.substring(0, 2));
		int day = Integer.parseInt(date.substring(2, 4));
		int year = Integer.parseInt(date.substring(4));
		
		if(month<=12){
		    switch (month) {
		        case 1: // fall through
		        case 3: // fall through
		        case 5: // fall through
		        case 7: // fall through
		        case 8: // fall through
		        case 10: // fall through
		        case 12:
		        	if(day<=31){
		            	return true;
		            }
		            break;
		        case 2:
		            if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
		            	if(day<=29){
			            	return true;
			            }
		            } else {
		            	if(day<=28){
			            	return true;
			            }
		            }
		            break;
		        default:
		            if(day<=30){
		            	return true;
		            }
		    }
		}
	    return false;
	}
}
