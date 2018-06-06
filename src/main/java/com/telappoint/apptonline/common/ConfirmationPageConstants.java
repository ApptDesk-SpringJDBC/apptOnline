package com.telappoint.apptonline.common;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Murali
 */
public enum ConfirmationPageConstants {
	
	CONF_NUMBER("a.conf_number","conf_number"),
	APPT_DATE_TIME_DISPLAY("s.appt_date_time_display","appt_date_time_display"),
	
	RESOURCE_PREFIX("r.prefix","resource_prefix"),
	RESOURCE_FIRST_NAME("r.first_name","resource_first_name"),
	RESOURCE_LAST_NAME("r.last_name","resource_last_name"),	
	
	LOCATION_NAME_ONLINE("l.location_name_online","location_name_online"),	
	LOCATION_ADDRESS("l.address","location_address"),
	LOCATION_CITY("l.city","location_city"),
	LOCATION_STATE("l.state","location_state"),
	LOCATION_ZIP("l.zip","location_zip"),
	LOCATION_GOOGLE_MAP("l.location_google_map","location_google_map"),	
	LOCATION_GOOGLE_MAP_LINK("l.location_google_map_link","location_google_map_link"),
	LOCATION_TIME_ZONE("l.time_zone","location_time_zone"),
	
	PROCEDURE_NAME_ONLINE("p.procedure_name_online","procedure_name_online"),	
	DEPARTMENT_NAME_ONLINE("d.department_name_online","department_name_online"),
	IA_MESSAGE_VALUE_SERVICE("ia.message_value.service","service_i18n_aliases_name"),
	
	CUSTOMER_ACCOUNT_NUMBER("c.account_number","customer_account_number"),
	CUSTOMER_FIRST_NAME("c.first_name","customer_first_name"),
	CUSTOMER_LAST_NAME("c.last_name","customer_last_name"),	
	CUSTOMER_CONTACT_PHONE("c.contact_phone","customer_contact_phone"),
	CUSTOMER_HOME_PHONE("c.home_phone","customer_home_phone"),	
	CUSTOMER_WORK_PHONE("c.work_phone","customer_work_phone"),
	CUSTOMER_CELL_PHONE("c.cell_phone","customer_cell_phone"),
	CUSTOMER_EMAIL("c.email","customer_email"),		
	CUSTOMER_ATTRIB1("c.attrib1","customer_attrib1"),	
	CUSTOMER_ATTRIB2("c.attrib2","customer_attrib2"),
	CUSTOMER_ATTRIB3("c.attrib3","customer_attrib3"),
	CUSTOMER_ATTRIB4("c.attrib4","customer_attrib4"),
	CUSTOMER_ATTRIB5("c.attrib5","customer_attrib5"),
	CUSTOMER_ATTRIB6("c.attrib6","customer_attrib6"),
	CUSTOMER_ATTRIB7("c.attrib7","customer_attrib7"),
	CUSTOMER_ATTRIB8("c.attrib8","customer_attrib8"),
	CUSTOMER_ATTRIB9("c.attrib9","customer_attrib9"),
	CUSTOMER_ATTRIB10("c.attrib10","customer_attrib10"),
	
	LIST_THINGS_TO_BRING("doc.display_text","list_things_to_bring");
	
	private String backEndKey;
	private String froentEndMapping;

	private ConfirmationPageConstants(String backEndKey,String froentEndMapping) {
		this.backEndKey = backEndKey;
		this.froentEndMapping = froentEndMapping;
	}

	public String getBackEndKey() {
		return backEndKey;
	}

	public void setBackEndKey(String backEndKey) {
		this.backEndKey = backEndKey;
	}

	public String getFroentEndMapping() {
		return froentEndMapping;
	}

	public void setFroentEndMapping(String froentEndMapping) {
		this.froentEndMapping = froentEndMapping;
	}
		
	public static class ConfirmationPageConstantsFrontEndMapping {
		public static String getFroentEndMapping(String backEndKey) {
			ConfirmationPageConstants[] keys = ConfirmationPageConstants.values();
			for (ConfirmationPageConstants key : keys) {
				if (key.getBackEndKey().equals(backEndKey)) {
					return key.getFroentEndMapping();
				}
			}
			return "";
		}
	}
	
	public static Map<String,Object> getConfirmationPageJsonMap(String backEndKeys,String backEndKeyValues){
		Map<String,Object> jsonMap = new HashMap<String,Object>();
		if(backEndKeys!=null && backEndKeys.length()>0 && backEndKeyValues!=null && backEndKeyValues.length()>0){
			if(backEndKeyValues.endsWith("|")){
				backEndKeyValues = backEndKeyValues+"N.A";
			}
			String[] backEndKeysArr = backEndKeys.split("\\|");
			String[] backEndKeyValuesArr = backEndKeyValues.split("\\|");
			String backEndKeyValue = "N.A";
			for(int i=0;i<backEndKeysArr.length;i++){
				if(backEndKeyValuesArr[i]!=null && backEndKeyValuesArr[i].length()>0){
					backEndKeyValue = backEndKeyValuesArr[i];
				}else{
					backEndKeyValue = "N.A";
				}
				jsonMap.put(ConfirmationPageConstantsFrontEndMapping.getFroentEndMapping(backEndKeysArr[i]),backEndKeyValue);
			}
		}
		return jsonMap;
	}
	
	public static void main(String[] args) {
		//Test case - 1
		System.out.println(getConfirmationPageJsonMap("a.conf_number|s.appt_date_time_display|r.prefix|r.first_name|r.last_name|l.location_name_online|l.address|l.city|l.state|l.zip|l.location_google_map|l.location_google_map_link|l.time_zone|p.procedure_name_online|d.department_name_online|ia.message_value.service|c.account_number|c.first_name|c.last_name|c.contact_phone|c.home_phone|c.work_phone|c.cell_phone|c.email|c.attrib1|c.attrib2|c.attrib3|c.attrib4|c.attrib5|c.attrib6|c.attrib7|c.attrib8|c.attrib9|c.attrib10|doc.display_textkeys",
						  "45|Tuesday, June 21st, 2016||Intake|Worker#1|MSC-Federal Way|100 S 336th St|Federal Way|WA|98003|<iframe src=\"https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d2705.663209319568!2d-122.32015854846395!3d47.30138081690277!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x549057bccfba5fa1%3A0xf437ba7e5117f18c!2sMulti-Service+Center!5e0!3m2!1sen!2sus!4v1460347076835\" width=\"400\" height=\"300\" frameborder=\"0\" style=\"border:0\" allowfullscreen></iframe>|https://www.google.com/maps/place/Multi-Service+Center/@47.3013808,-122.3201585,17z/data=!3m1!4b1!4m2!3m1!1s0x549057bccfba5fa1:0xf437ba7e5117f18c|US/Pacific|10001|||100101010|Murali|Gmk|123123123||||asdfg@asd.fads|3457||||||||||<p>Documents Required:</p>\r\n<ul>\r\n<li>Picture ID for applicant</li>\r\n<li>Social Security Cards for everyone in the family</li>\r\n<li>Proof of income for the last 30 days for anyone in household 18 years or older- gross income (SS, SSI, SSDI, Check stubs, Unemployment benefits, pension, retirement, military income, regular monthly payments - even cash)</li>\r\n<li>Proof of USA citizenship or legal status in the country (birth certificate, green card, USA passport, VA paperwork)</li>\r\n<li>Current utility bill (electric/gas and/or water)</li>\r\n</ul>"));
		System.out.println("\n------------------------------------------------------------\n");
		//Test case - 2
		System.out.println(getConfirmationPageJsonMap("a.conf_number|s.appt_date_time|s.appt_date_time_start|s.appt_date_time_end|s.appt_date_time_display|r.prefix|r.first_name|r.last_name|r.email|l.location_name_online|l.address|l.city|l.state|l.zip|l.location_google_map|l.location_google_map_link|l.time_zone|p.procedure_name_online|d.department_name_online|ia.message_value.service|c.account_number|c.first_name|c.last_name|c.contact_phone|c.home_phone|c.work_phone|c.cell_phone|c.email|c.attrib1|c.attrib2|c.attrib3|c.attrib4|c.attrib5|c.attrib6|c.attrib7|c.attrib8|c.attrib9|c.attrib10|doc.display_text|r.resource_tts|r.resource_audio|l.location_name_ivr_tts|l.location_name_ivr_audio|p.procedure_name_ivr_tts|p.procedure_name_ivr_audio|d.location_name_ivr_tts|d.department_name_ivr_audio|ser.service_name_ivr_tts|ser.service_name_ivr_audio|doc.list_of_docs_tts|doc.list_of_docs_audio",
				  "2|2016-09-20 09:30:00|2016-09-20 09:30:00|2016-09-20 09:35:00|Tuesday, September 20th, 2016 9:30 AM||Intake|Worker#1||CAP Administrative Office|||NH||http://development.itfontdesk.com/democall|http://development.itfontdesk.com/democall|US/Eastern|dummy|dummy|Rapid Rehousing|5410|Balaji|Abbatangelo|1231231234||||balajinsr@gmail.com||||||||||||Intake Worker#1|intakeworker|CAP Administrative Office|capadministrativeoffice|dummy|dummy|dummy|dummy|Rapid Rehousing|rapidrehousing||"));
	}

}
