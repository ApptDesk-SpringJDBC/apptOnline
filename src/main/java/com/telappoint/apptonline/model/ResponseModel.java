package com.telappoint.apptonline.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * 
 * @author Murali G
 *
 */
@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ResponseModel {
	private Object data;
	private Object errors;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	public Object getErrors() {
		return errors;
	}

	public void setErrors(Object errors) {
		this.errors = errors;
	}
}
