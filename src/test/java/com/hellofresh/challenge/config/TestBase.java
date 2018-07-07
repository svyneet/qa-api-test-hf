package com.hellofresh.challenge.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class TestBase {
	public RequestSpecification httpRequest = null;
	
	public void setBaseURLAndInitiateRequestSpecification(String URL) {
		RestAssured.baseURI = URL;
		httpRequest = RestAssured.given();
	}

	public void tearDown() {
		httpRequest = null;
	}

	public Properties readPropertyValues(String path) {
		Properties properties = new Properties();
		// load properties file
		try {
			properties.load(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
}
