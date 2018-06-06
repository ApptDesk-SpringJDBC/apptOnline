package com.telappoint.apptonline.restclient;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.telappoint.apptonline.model.ResponseModel;

/**
 * @author Murali G
 *
 */
public class ApptOnlineRestClient {

	private Logger logger = Logger.getLogger(ApptOnlineRestClient.class);

	private static Client client;

	private static volatile ApptOnlineRestClient instance;

	private ApptOnlineRestClient() {
		getRestWSClient();
	}

	public static ApptOnlineRestClient getInstance() {
		if (instance == null) {
			synchronized (ApptOnlineRestClient.class) {
				if (instance == null)
					instance = new ApptOnlineRestClient();
			}
		}
		return instance;
	}

	public static Client getRestWSClient() {
		ClientConfig config = new DefaultClientConfig();
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		client = Client.create(config);
		//client.setReadTimeout(60000);
		//client.setConnectTimeout(5000);
		return client;
	}

	public ResponseModel performGETRequest(String endPointUrl) throws Exception {	
		//replacing special characters
		endPointUrl = endPointUrl.replaceAll("\\|","%7C");
		endPointUrl = endPointUrl.replaceAll("~","%7E");
		WebResource webResource = client.resource(endPointUrl);
		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		if (response.getStatus() != 200) {
			logger.error("Request EndPointUrl :::::  "+endPointUrl +" , \t Failed GET Request with HTTP Ststus code ::::::: "+response.getStatus());
			throw new Exception("Failed GET Request with HTTP Ststus code : " + response.getStatus());
		}		
		return response.getEntity(ResponseModel.class);
	}

	public ResponseModel performPOSTRequest(String endPointUrl,Object request) throws Exception {		
		WebResource webResource  = client.resource(endPointUrl);
		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(ClientResponse.class,request);
		if (response.getStatus() != 201) {
			logger.error("Request EndPointUrl :::::  "+endPointUrl +" , \t Failed POST Request with HTTP Ststus code ::::::: "+response.getStatus());
			throw new Exception("Failed POST Request with HTTP Ststus code : " + response.getStatus());
		}		
		return response.getEntity(ResponseModel.class);
	}
	
}
