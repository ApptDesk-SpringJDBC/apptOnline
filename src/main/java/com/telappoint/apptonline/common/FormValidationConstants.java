package com.telappoint.apptonline.common;

/**
 * 
 * @author Murali
 * 
 */
public enum FormValidationConstants {
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
	ALPHA("a-zA-Z"), 
	DIGITS("0-9"),
	COMMA("\\,"),
	SPACE("\\s+"),
	SINGLE_QUOTE("\\'"),
	HYPHEN("\\-"),
	PERIOD("\\."),
	AMPERSAND("\\&"),
	PERCENTAGE("\\%"),
	POUND("\\#"),
	AT("\\@"),
	APOSTROPHE("\\!"),
	DOLLER("\\$"),
	STAR("\\*"),
	QUOTE("\\'"), 
	
	DOUBLE_QUOTE("\""),
	AMP("\\&"),
	LEFT_PARENTHESIS("\\("),
	RIGHT_PARENTHESIS("\\)"),
	
	NUMERIC("[0-9]+"),
	PHONE("[0-9]{10}+"), 	
	//EMAIL("[a-z0-9\\-_\\.]++@[a-z0-9\\-]++(\\.[a-z0-9\\-]++)++");
	EMAIL("[a-zA-Z0-9\\-_\\.]++@[a-zA-Z0-9\\-]++(\\.[a-zA-Z0-9\\-]++)++");

	// dot, double-quote, amp, left_parenthesis, right_parenthesis
	private String validateExp;

	FormValidationConstants(String validateExp) {
		this.validateExp = validateExp;
	}

	public String getValidateExp() {
		return validateExp;
	}
}
