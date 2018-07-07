package com.hellofresh.challenge.objects;

public class Country {
	public String name;
	public String alpha2_code;
	public String alpha3_code;
	
	public Country(){}
	
	public Country(String name, String alpha2_code, String alpha3_code)
	{
		this.name = name;
		this.alpha2_code = alpha2_code;
		this.alpha3_code = alpha3_code;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlpha2Code() {
		return alpha2_code;
	}

	public void setAlpha2Code(String alpha2Code) {
		this.alpha2_code = alpha2Code;
	}

	public String getAlpha3Code() {
		return alpha3_code;
	}

	public void setAlpha3Code(String alpha3Code) {
		this.alpha3_code = alpha3Code;
	}

}
