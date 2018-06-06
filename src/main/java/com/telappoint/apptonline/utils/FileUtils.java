package com.telappoint.apptonline.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 
 * @author Murali
 * 
 */
public class FileUtils {
	private static final Logger logger = Logger.getLogger(FileUtils.class);

	private static Map<String, Properties> propsMap = new HashMap<String, Properties>();
	//public static final String REST_WS_URL_PROPFILE = "/apps/properties/apptonline.properties";
	//public static final String APPT_SERVICE_MAIL_PROP = "/apps/properties/apptRestService.properties";
	public static final String APPT_SERVICE_FILE_UPLOAD_PATH_PROP_KEY = "APPT_SERVICE_FILE_UPLOAD_PATH";
	
	public static final String REST_WS_URL_PROPFILE = "D:\\Murali\\work_space_new\\Apptonline_New\\src\\main\\resources\\apptonline.properties";
	public static final String APPT_SERVICE_MAIL_PROP = "D:\\Murali\\work_space_new\\Apptonline_New\\src\\main\\resources\\apptRestService.properties";
	
	public static InputStream getResourceAsStream(String fileName) throws Exception {
		InputStream propsIn = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
		if (propsIn == null) {
			propsIn = FileUtils.class.getResourceAsStream(fileName);
		}
		if (propsIn == null) {
			propsIn = ClassLoader.getSystemResourceAsStream(fileName);
		}

		if (propsIn == null) {
			logger.error(fileName + " not found");

			throw new Exception(fileName + " not found");
		}
		return propsIn;
	}
	
	public static Properties getProperties(String fileName,String mapkey) throws Exception {

		Properties properties = propsMap.get(mapkey);
		if (properties != null) {
			return properties;
		}
		try {
			properties = new Properties();
			properties.load(getResourceAsStream(fileName));
			propsMap.put(mapkey, properties);
		} catch (IOException e) {
			logger.error(e);
			throw new Exception();
		}

		return properties;
	}
	
	public static Properties getFileProperties(String fileName,String propKey) {
		Properties properties = new Properties();
		if (propsMap.get(propKey) == null) {
			try {
				properties.load(new FileInputStream(fileName));
			} catch (Exception e) {
				logger.error("Error:" + e, e);
			}
			propsMap.put(propKey, properties);
		} else {
			properties = propsMap.get(propKey);
		}
		return properties;
	}
	
	public static String getEndPointURL() {
		Properties properties = getFileProperties(REST_WS_URL_PROPFILE,"apptonlineProps");
		String restWSEndPointURL = properties.getProperty("REST_SERVICE_URL_ENDPOINT");
		return restWSEndPointURL;
	}
	
	public static String getMailProperty(String key) throws IOException {
		Properties properties = getFileProperties(APPT_SERVICE_MAIL_PROP,"mailProps");
		return (properties.get(key) !=null)?(String)properties.get(key):null;
	}
	
	public static String getPropertyFromApptonlineProp(String key) {
		Properties properties = getFileProperties(REST_WS_URL_PROPFILE,"apptonlineProps");
		String restWSEndPointURL = properties.getProperty(key);
		return restWSEndPointURL;
	}
}
