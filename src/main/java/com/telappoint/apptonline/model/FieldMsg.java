package com.telappoint.apptonline.model;

public class FieldMsg {

	private String field;
	private String msg;
	private String display;
	private String displayType;
	
	public FieldMsg(String field,String msg){
		this.field = field;
		this.msg = msg;
	}
	
	public FieldMsg(String field,String msg,String display,String displayType){
		this.field = field;
		this.msg = msg;
		this.display = display;
		this.displayType = displayType;
	}
	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}
	
	public int hashCode(){
	  return (int) field.hashCode();
	}
	
	public boolean equals(Object o){
	    if(o == null) {
	    	return false;
	    }
	    if(!(o instanceof FieldMsg)){
	    	return false;
	    }
	    FieldMsg other = (FieldMsg) o;
	    if(! this.field.equals(other.field)){
	    	return false;
	    }

	    return true;
	 }
	
}
