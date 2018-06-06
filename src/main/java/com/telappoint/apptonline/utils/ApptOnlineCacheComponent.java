package com.telappoint.apptonline.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telappoint.apptonline.model.ApptOnlineSessionData;
import com.telappoint.apptonline.model.LandingPageInfo;
import com.telappoint.apptonline.model.OnlinePageData;
import com.telappoint.apptonline.service.IApptOnlineService;

@Component
public class ApptOnlineCacheComponent {

	private Logger logger = Logger.getLogger(ApptOnlineCacheComponent.class);

	@Autowired
	private IApptOnlineService apptOnlineService;

	private static final Object lock = new Object();
	private static boolean enableCache = true;
	private static Map<String,OnlinePageData> onlinePageDataCacheMap = new HashMap<String,OnlinePageData>();
	private static Map<String,LandingPageInfo> landingPageInfoCacheMap = new HashMap<String,LandingPageInfo>();
	private static Map<String,Map<String,String>> pageValidationMessagesCacheMap = new HashMap<String,Map<String,String>>();

	@SuppressWarnings("unchecked")
	public Map<String, String> getPageValidationMessages(ApptOnlineSessionData sessionData) {
		Map<String,String> pageValidationMessages = null;
		try{
			if(enableCache){
				pageValidationMessages = pageValidationMessagesCacheMap.get(getCacheMapKey(sessionData));
			}
			if(pageValidationMessages==null){
				Map<String, Object> pageValidationMessagesResponse = null;
				synchronized (lock) {
					pageValidationMessagesResponse = apptOnlineService.getPageValidationMessages(sessionData);
				}
				if(ApptOnlineUtils.getValue(pageValidationMessagesResponse,"status", Boolean.class)){
					pageValidationMessages = ApptOnlineUtils.getValue(pageValidationMessagesResponse,"pageValidationMap",LinkedHashMap.class);
				}
				if(enableCache){
					pageValidationMessagesCacheMap.put(getCacheMapKey(sessionData),pageValidationMessages);
				}
				logger.debug("###################  Page Validation Messages read from  --  DB --  ###################");
			}else{
				logger.debug("###################  Page Validation Messages read from  --  CACHE --  ###################");
			}
		}catch(Exception e){
			logger.error("Error :: "+e.getMessage(),e);
		}
		return pageValidationMessages;		
	}

	public OnlinePageData getOnlinePageData(ApptOnlineSessionData sessionData) {
		OnlinePageData onlinePageData = null;
		try{
			if(enableCache){
				onlinePageData = onlinePageDataCacheMap.get(getCacheMapKey(sessionData));
			}
			if(onlinePageData==null){
				synchronized (lock) {
					onlinePageData = apptOnlineService.getOnlinePageData(sessionData);
				}	
				if(enableCache){
					onlinePageDataCacheMap.put(getCacheMapKey(sessionData),onlinePageData);
				}
				logger.debug("###################  Online Page Data read from  --  DB --  ###################");
			}else{
				logger.debug("###################  Online Page Data read from  --  CACHE --  ###################");
			}			
		}catch(Exception e){
			logger.error("Error :: "+e.getMessage(),e);
		}
		return onlinePageData;		
	}

	public LandingPageInfo getLandingPageInfo(ApptOnlineSessionData sessionData) {
		LandingPageInfo landingPageInfo = null;
		try{
			if(enableCache){
				landingPageInfo = landingPageInfoCacheMap.get(getCacheMapKey(sessionData));
			}
			if(landingPageInfo==null){
				synchronized (lock) {
					landingPageInfo = apptOnlineService.getLandingPageInfo(sessionData);
					//sessionData.setLangCode(landingPageInfo.getDefaultLangCode());  //After discussing with Anantha i have commented..
				}	
				if(enableCache){
					landingPageInfoCacheMap.put(getCacheMapKey(sessionData),landingPageInfo);
				}
				logger.debug("###################  Landing Page Info read from  --  DB --  ###################");
			}else{
				logger.debug("###################  Landing Page Info read from  --  CACHE --  ###################");
			}			
		}catch(Exception e){
			logger.error("Error :: "+e.getMessage(),e);
		}
		return landingPageInfo;		
	}

	public String getCacheMapKey(ApptOnlineSessionData sessionData){
		StringBuilder cacheMapKey = new StringBuilder();
		cacheMapKey.append(sessionData.getClientCode()).append("_").append(sessionData.getLangCode());
		return cacheMapKey.toString();
	}

	public boolean clearCache() {
		try{
			synchronized (lock) {
				onlinePageDataCacheMap.clear();
				landingPageInfoCacheMap.clear();
				pageValidationMessagesCacheMap.clear();
			}
			logger.debug("###################  Cleared Cache Data  ###################");
			return true;	
		}catch(Exception e){
			logger.error("Error :: "+e.getMessage(),e);
			return false;
		}			
	}
	
	public boolean clearCache(String clientCode) {
		try{
			synchronized (lock) {
				String mapKeyStarts = clientCode+"_";
				Set<String> cacheKeys = new HashSet<String>();
				//removing data from onlinePageData
				for(String key : onlinePageDataCacheMap.keySet()){
					if(key.startsWith(mapKeyStarts)){						
						cacheKeys.add(key);						
					}
				}
				onlinePageDataCacheMap.keySet().removeAll(cacheKeys);
				cacheKeys.clear();
				
				//removing data from landingPageInfo
				for(String key : landingPageInfoCacheMap.keySet()){
					if(key.startsWith(mapKeyStarts)){						
						cacheKeys.add(key);						
					}
				}
				landingPageInfoCacheMap.keySet().removeAll(cacheKeys);
				cacheKeys.clear();
				
				//removing data from pageValidationMessages
				for(String key : pageValidationMessagesCacheMap.keySet()){
					if(key.startsWith(mapKeyStarts)){						
						cacheKeys.add(key);						
					}
				}
				pageValidationMessagesCacheMap.keySet().removeAll(cacheKeys);
			}
			logger.debug("###################  Cache Data Cleared for clientCode ###################  "+clientCode);
			return true;	
		}catch(Exception e){
			logger.error("Error :: "+e.getMessage(),e);
			return false;
		}			
	}
}
