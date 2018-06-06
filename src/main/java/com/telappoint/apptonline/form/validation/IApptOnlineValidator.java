package com.telappoint.apptonline.form.validation;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.telappoint.apptonline.model.OnlinePageFields;
/**
 * 
 * @author Murali
 * 
 */
public interface IApptOnlineValidator {
	
	public String validate(HttpServletRequest request,List<OnlinePageFields> pageFields,Map<String,String> pageValidationMessages,String lanng_code);
}
