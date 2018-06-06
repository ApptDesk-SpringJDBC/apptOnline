package com.telappoint.apptonline.form.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.telappoint.apptonline.common.FormValidationConstants;

/**
 * 
 * @author Murali
 * 
 */

@Service
public class FormValidationUtils  {
	
	private static Logger logger = Logger.getLogger(ApptOnlineValidatorImpl.class);
	
	private static Pattern pattern;
	  private static Matcher matcher;
	 
	  private static final String REGEX_PATTERN_START = "^(";
	  private static final String REGEX_PATTERN_END = ")$";
	  private static final String PATTERN_START = "[";
	  private static final String PATTERN_END = "]*";
	  
	  private static final String VALIDATION_RULE_ALPHA = "alpha";
	  private static final String VALIDATION_RULE_DIGITS = "digits";
	  private static final String VALIDATION_RULE_NUMERIC = "numeric";
	  private static final String VALIDATION_RULE_COMMA = "comma";
	  private static final String VALIDATION_RULE_SPACE = "space";
	  private static final String VALIDATION_RULE_SINGLE_QUOTE = "single-quote";
	  private static final String VALIDATION_RULE_SINGLEQUOTE = "singlequote";
	  private static final String VALIDATION_RULE_QUOTE = "quote";
	  private static final String VALIDATION_RULE_HYPEN = "hypen";
	  private static final String VALIDATION_RULE_PERIOD = "period";
	  private static final String VALIDATION_RULE_AMPERSAND = "ampersand";
	  private static final String VALIDATION_RULE_PERCENTAGE = "percentage";
	  private static final String VALIDATION_RULE_POUND = "pound";
	  private static final String VALIDATION_RULE_AT = "at";
	  private static final String VALIDATION_RULE_APOSTROPHE = "apostrophe";
	  private static final String VALIDATION_RULE_DOLLER = "dollar";
	  private static final String VALIDATION_RULE_STAR = "star";
	  
	  private static final String VALIDATION_RULE_DOT = "dot";
	  private static final String VALIDATION_RULE_DOUBLE_QUOTE = "double-quote";
	  private static final String VALIDATION_RULE_DOUBLEQUOTE = "doublequote";	  
	  private static final String VALIDATION_RULE_AMP = "amp";
	  private static final String VALIDATION_RULE_LEFT_PARENTHESIS = "left_parenthesis";
	  private static final String VALIDATION_RULE_LEFTPARENTHESIS = "leftparenthesis";
	  private static final String VALIDATION_RULE_RIGHT_PARENTHESIS = "right_parenthesis";
	  private static final String VALIDATION_RULE_RIGHTPARENTHESIS = "rightparenthesis";
	  
	  private static final String VALIDATION_RULE_PHONE = "phone";
	  private static final String VALIDATION_RULE_US_PHONE = "us-phone";
	  private static final String VALIDATION_RULE_EMAIL = "email";
	  
	  /* Dynamic field value validation method based on validation rules comming from DB using JAVA regex*/
	  public static boolean validateFieldValue(String rules,String fieldValue){
		  //System.out.println("\n fieldValue :::: "+fieldValue);
		  String REGEX_PATTERN = REGEX_PATTERN_START;
		  boolean valid = false;
		  if(rules!=null && rules!=""){
			  if(rules.contains(",")){
				  REGEX_PATTERN = REGEX_PATTERN +PATTERN_START;
				  String[] rulesArray = rules.split(",");
				  for(String rule : rulesArray){
					  if(rule!=null && !"".equals(rule)){
						  rule = rule.trim();
						  if(VALIDATION_RULE_ALPHA.equalsIgnoreCase(rule)) {
							REGEX_PATTERN = REGEX_PATTERN+FormValidationConstants.ALPHA.getValidateExp();
						  }else	if(VALIDATION_RULE_DIGITS.equalsIgnoreCase(rule)) {
							REGEX_PATTERN = REGEX_PATTERN+FormValidationConstants.DIGITS.getValidateExp();
						  }else	if(VALIDATION_RULE_NUMERIC.equalsIgnoreCase(rule)) {
							REGEX_PATTERN = REGEX_PATTERN+FormValidationConstants.DIGITS.getValidateExp();
						  }else if(VALIDATION_RULE_COMMA.equalsIgnoreCase(rule)) {
							REGEX_PATTERN = REGEX_PATTERN+FormValidationConstants.COMMA.getValidateExp();
						  }else if(VALIDATION_RULE_SPACE.equalsIgnoreCase(rule)) {
							REGEX_PATTERN = REGEX_PATTERN+FormValidationConstants.SPACE.getValidateExp();
						  }else if(VALIDATION_RULE_SINGLE_QUOTE.equalsIgnoreCase(rule) || VALIDATION_RULE_SINGLEQUOTE.equalsIgnoreCase(rule)) {
							REGEX_PATTERN = REGEX_PATTERN+FormValidationConstants.SINGLE_QUOTE.getValidateExp();
						  }else if(VALIDATION_RULE_QUOTE.equalsIgnoreCase(rule)) {
							REGEX_PATTERN = REGEX_PATTERN+FormValidationConstants.QUOTE.getValidateExp();
						  }else if(VALIDATION_RULE_HYPEN.equalsIgnoreCase(rule)) {
							REGEX_PATTERN = REGEX_PATTERN+FormValidationConstants.HYPHEN.getValidateExp();
						  }else	if(VALIDATION_RULE_PERIOD.equalsIgnoreCase(rule)) {
							REGEX_PATTERN = REGEX_PATTERN+FormValidationConstants.PERIOD.getValidateExp();
						  }else	if(VALIDATION_RULE_AMPERSAND.equalsIgnoreCase(rule)) {
							REGEX_PATTERN = REGEX_PATTERN+FormValidationConstants.AMPERSAND.getValidateExp();
						  }else	if(VALIDATION_RULE_PERCENTAGE.equalsIgnoreCase(rule)) {
							REGEX_PATTERN = REGEX_PATTERN+FormValidationConstants.PERCENTAGE.getValidateExp();
						  }else	if(VALIDATION_RULE_POUND.equalsIgnoreCase(rule)) {
							REGEX_PATTERN = REGEX_PATTERN+FormValidationConstants.POUND.getValidateExp();
						  }else	if(VALIDATION_RULE_AT.equalsIgnoreCase(rule)) {
							REGEX_PATTERN = REGEX_PATTERN+FormValidationConstants.AT.getValidateExp();
						  }else	if(VALIDATION_RULE_APOSTROPHE.equalsIgnoreCase(rule)) {
							REGEX_PATTERN = REGEX_PATTERN+FormValidationConstants.APOSTROPHE.getValidateExp();
						  }else	if(VALIDATION_RULE_DOLLER.equalsIgnoreCase(rule)) {
							REGEX_PATTERN = REGEX_PATTERN+FormValidationConstants.DOLLER.getValidateExp();
						  }else	if(VALIDATION_RULE_STAR.equalsIgnoreCase(rule)) {
							REGEX_PATTERN = REGEX_PATTERN+FormValidationConstants.STAR.getValidateExp();
						  }else if(VALIDATION_RULE_DOT.equalsIgnoreCase(rule)) {
							REGEX_PATTERN = REGEX_PATTERN+FormValidationConstants.PERIOD.getValidateExp();
						  }else	if(VALIDATION_RULE_DOUBLE_QUOTE.equalsIgnoreCase(rule)) {
							REGEX_PATTERN = REGEX_PATTERN+FormValidationConstants.DOUBLE_QUOTE.getValidateExp();
						  }else	if(VALIDATION_RULE_DOUBLEQUOTE.equalsIgnoreCase(rule)) {
							REGEX_PATTERN = REGEX_PATTERN+FormValidationConstants.DOUBLE_QUOTE.getValidateExp();
						  }else	if(VALIDATION_RULE_AMP.equalsIgnoreCase(rule)) {
							REGEX_PATTERN = REGEX_PATTERN+FormValidationConstants.AMP.getValidateExp();
						  }else	if(VALIDATION_RULE_LEFT_PARENTHESIS.equalsIgnoreCase(rule)) {
							//REGEX_PATTERN = REGEX_PATTERN+FormValidationConstants.LEFT_PARENTHESIS.getValidateExp();
							  fieldValue = fieldValue.replaceAll(FormValidationConstants.LEFT_PARENTHESIS.getValidateExp(),"");
						  }else	if(VALIDATION_RULE_LEFTPARENTHESIS.equalsIgnoreCase(rule)) {
							//REGEX_PATTERN = REGEX_PATTERN+FormValidationConstants.LEFT_PARENTHESIS.getValidateExp();
							  fieldValue = fieldValue.replaceAll(FormValidationConstants.LEFT_PARENTHESIS.getValidateExp(),"");
						  }else	if(VALIDATION_RULE_RIGHT_PARENTHESIS.equalsIgnoreCase(rule)) {
							//REGEX_PATTERN = REGEX_PATTERN+FormValidationConstants.RIGHT_PARENTHESIS.getValidateExp();
							  fieldValue = fieldValue.replaceAll(FormValidationConstants.RIGHT_PARENTHESIS.getValidateExp(),"");
						  }else	if(VALIDATION_RULE_RIGHTPARENTHESIS.equalsIgnoreCase(rule)) {
							//REGEX_PATTERN = REGEX_PATTERN+FormValidationConstants.RIGHT_PARENTHESIS.getValidateExp();
							  fieldValue = fieldValue.replaceAll(FormValidationConstants.RIGHT_PARENTHESIS.getValidateExp(),"");
						  }
					  }
				  }
				  REGEX_PATTERN = REGEX_PATTERN+PATTERN_END;
			  }else{
				  rules = rules.trim();
			  	  if(VALIDATION_RULE_PHONE.equalsIgnoreCase(rules)) {
					REGEX_PATTERN = REGEX_PATTERN+FormValidationConstants.PHONE.getValidateExp();
				  }else if(VALIDATION_RULE_US_PHONE.equalsIgnoreCase(rules)) {
					REGEX_PATTERN = REGEX_PATTERN+FormValidationConstants.PHONE.getValidateExp();
				  }else	if(VALIDATION_RULE_NUMERIC.equalsIgnoreCase(rules)) {
					REGEX_PATTERN = REGEX_PATTERN+FormValidationConstants.NUMERIC.getValidateExp();
				  }else if(VALIDATION_RULE_EMAIL.equalsIgnoreCase(rules)) {
					REGEX_PATTERN = REGEX_PATTERN+FormValidationConstants.EMAIL.getValidateExp();
				  }	
			  }
		  }
		  REGEX_PATTERN = REGEX_PATTERN+REGEX_PATTERN_END;
		  valid = regExValidation(fieldValue, REGEX_PATTERN);
		  
		  /*System.out.println(" REGEX_PATTERN ================> "+REGEX_PATTERN);		  
		  System.out.println(" fieldValue ================> "+fieldValue);
		  System.out.println(" valid ================> "+valid);*/
		  
		  logger.info(" REGEX_PATTERN ::: "+REGEX_PATTERN+" \t ,fieldValue ::: "+fieldValue+"  \t , valid ::: "+valid);
		  //System.out.println(" REGEX_PATTERN ::: "+REGEX_PATTERN+" \t ,fieldValue ::: "+fieldValue+"  \t , valid ::: "+valid); 
		  return valid ;

	  }

	 private static boolean regExValidation(final String value, String regExStr) {
		pattern = Pattern.compile(regExStr);		  
		matcher = pattern.matcher(value);
		return matcher.matches();
	 }
	  
	public static void main(String[] args) {
		/*
		 * Possible validation rules are:
			alpha - letters
			digits - numbers
			comma - ,
			space - space
			single-quote - '
			hypen - -
			period - .
			ampersand - &
			percentage - %
			pound - #
			at - @
			apostrophe - !
			dollar - $
			star - *
		 */
		System.out.println("Response  ------> "+validateFieldValue("alpha, numeric, space, hypen, single-quote, dot, comma, pound, double-quote, amp, left_parenthesis, right_parenthesis",
																	"asdf(123#das")+"\n");
		//alpha,digits,comma,space,single-quote,hypen,period,ampersand,percentage,pound,at,apostrophe,dollar,star
	    //String rulues = "alpha,digits,comma,space,single-quote,hypen,period,ampersand,percentage,pound,at,apostrophe,dollar,star";		    
		/*System.out.println("Response  ------> "+validateFieldValue("alpha,digits","asdf3342!")+"\n");
		System.out.println("Response  ------> "+validateFieldValue("alpha,comma","asdf,")+"\n");
		System.out.println("Response  ------> "+validateFieldValue("alpha,space","as    df asdf")+"\n");
		System.out.println("Response  ------> "+validateFieldValue("alpha,single-quote","asdf'")+"\n");
		System.out.println("Response  ------> "+validateFieldValue("alpha,hypen","asdf-asdasdf")+"\n");
		System.out.println("Response  ------> "+validateFieldValue("alpha,period","asdfasd.asdf")+"\n");
		System.out.println("Response  ------> "+validateFieldValue("alpha,ampersand","asdf&asd&asdf")+"\n");
		System.out.println("Response  ------> "+validateFieldValue("alpha,percentage","asdf%asdasdf")+"\n");
		System.out.println("Response  ------> "+validateFieldValue("alpha,pound","asdf#asd#asdf")+"\n");
		System.out.println("Response  ------> "+validateFieldValue("alpha,at","asdf@asd@asdf")+"\n");
		System.out.println("Response  ------> "+validateFieldValue("alpha,apostrophe","asdf!asd!asdf")+"\n");
		System.out.println("Response  ------> "+validateFieldValue("alpha,dollar","asdf$asd$asdf")+"\n");
		System.out.println("Response  ------> "+validateFieldValue("alpha,star","asdf*asd*asdf")+"\n");
		System.out.println("Response  ------> "+validateFieldValue("alpha,","asdfdsasddsasdf")+"\n");
		System.out.println("Response  ------> "+validateFieldValue("digits,","5563")+"\n");
		System.out.println("Response  ------> "+validateFieldValue("comma,",",,,,,,,")+"\n");
		System.out.println("Response  ------> "+validateFieldValue("space,","    ")+"\n");
		System.out.println("Response  ------> "+validateFieldValue("single-quote,","'''''''")+"\n");
		System.out.println("Response  ------> "+validateFieldValue("hypen,","------")+"\n");
		System.out.println("Response  ------> "+validateFieldValue("period,","......'")+"\n");
		System.out.println("Response  ------> "+validateFieldValue("ampersand,","&&&&&&&")+"\n");
		System.out.println("Response  ------> "+validateFieldValue("percentage,","%%%%%%%%")+"\n");
		System.out.println("Response  ------> "+validateFieldValue("pound,","########")+"\n");
		System.out.println("Response  ------> "+validateFieldValue("at,","@@@@@@@@@@@")+"\n");
		System.out.println("Response  ------> "+validateFieldValue("apostrophe,","!!!!!")+"\n");
		System.out.println("Response  ------> "+validateFieldValue("dollar,","$$$$$")+"\n");
		System.out.println("Response  ------> "+validateFieldValue("star,","****")+"\n");
		System.out.println("Response  ------> "+validateFieldValue("comma,space,single-quote,hypen,period,ampersand,percentage,pound,at,apostrophe,dollar,star","%$#@!&*-',")+"\n");
		System.out.println("Response  ------> "+validateFieldValue("alpha,digits,comma,space,single-quote,hypen,period,ampersand,percentage,pound,at,apostrophe,dollar,star","asdf4567@!#$%&*-,'@asd.asdf")+"\n");*/
		
		//String name = "";
		//System.out.println("After Replace  ---->  "+(name.replaceAll(regex, replacement)));
	}


}
