package com.hellofresh.challenge.Tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Properties;

import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.hellofresh.challenge.config.TestBase;
import com.hellofresh.challenge.objects.Country;

public class APITest extends TestBase {

	public Properties settingsProp = null;
	public Response response = null;
	public int statusCode = 0;

	@Before
	public void setUp() {
		settingsProp = this.readPropertyValues("data\\settings.properties");
		this.setBaseURLAndInitiateRequestSpecification(settingsProp.getProperty("BASEURL"));
	}

	@After
	public void cleanUp() {
		this.tearDown();
	}

	@Test
	public void getCountriesAndValidateUSDEGB() {
		// Test Data
		ArrayList<String> alphaCodes2 = new ArrayList<String>();
		String[] countriesRequired = { "US", "DE", "GB" };
		// Actions
		response = httpRequest.request(Method.GET, "/all");
		statusCode = response.getStatusCode();
		JsonPath jsonPathEvaluator = response.jsonPath();
		Country[] countries = jsonPathEvaluator.getObject("RestResponse.result", Country[].class);
		for (Country c : countries) {
			alphaCodes2.add(c.alpha2_code);
		}

		// Validations
		assertEquals("Status code couldn't be verified.", statusCode, 200);
		for (int i = 0; i < countriesRequired.length; i++) {
			assertTrue("Response country " + countriesRequired[i] + " couldn't be verified",
					alphaCodes2.contains(countriesRequired[i]));
		}
	}

	@Test
	public void getEachCountryAndValidateUSDEGB() {
		// Test Data
		Country[] countries = new Country[3];
		countries[0] = new Country("United States of America", "US", "USA");
		countries[1] = new Country("Germany", "DE", "DEU");
		countries[2] = new Country("United Kingdom of Great Britain and Northern Ireland", "GB", "GBR");
		//
		for (int i = 0; i < countries.length; i++) {
			// Actions
			response = httpRequest.request(Method.GET, "/iso2code/" + countries[i].getAlpha2Code());
			statusCode = response.getStatusCode();
			JsonPath jsonPathEvaluator = response.jsonPath();
			Country countryResult = jsonPathEvaluator.getObject("RestResponse.result", Country.class);
			// Validations
			assertEquals("Status code for query: " + countries[i].name + "couldn't be verified.", 200, statusCode);
			assertEquals("Country name: " + countries[i].name + " couldn't be verified", countries[i].name,
					countryResult.getName());
			assertEquals("Country alpha2Code: " + countries[i].alpha2_code + " couldn't be verified",
					countries[i].alpha2_code, countryResult.getAlpha2Code());
			assertEquals("Country alpha3Code: " + countries[i].alpha3_code + " couldn't be verified",
					countries[i].alpha3_code, countryResult.getAlpha3Code());
		}
	}

	@Test
	public void getInvalidCountry() {
		// Test Data
		String alpha2_code = "ZZ";
		String invalidMessage = "No matching country found for requested code [" + alpha2_code + "]";
		// Actions
		response = httpRequest.request(Method.GET, "/iso2code/" + alpha2_code);
		statusCode = response.getStatusCode();
		String message = response.getBody().asString();
		// Validations
		assertEquals("Status code couldn't be verified.", 200, statusCode);
		assertTrue("Invalid country code could't be verified", message.contains(invalidMessage));
	}

	@Test
	public void addNewCountry() {
		// Test Data
		Country country = new Country("Republican States of Africa","RS", "RSA");
		JSONObject requestParams = new JSONObject();
		requestParams.put("name", country.getName());
		requestParams.put("alpha2_code", country.getAlpha2Code());
		requestParams.put("alpha3_code", country.getAlpha3Code());
		// Actions
		httpRequest.body(requestParams.toJSONString());
		response = httpRequest.post("/add_country");
		statusCode = response.getStatusCode();
		// Validations
		assertEquals("Status code couldn't be verified.", 201, statusCode);
		// TODO validation of response body
	}

}
