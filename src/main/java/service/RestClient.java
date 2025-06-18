package service;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import utils.ConfigManager;

public class RestClient {

    private static RestClient instance;
    private final RequestSpecification request;

    private RestClient() {
        this.request = RestAssured.given()
                .baseUri(ConfigManager.getBaseURL())
                .headers(ConfigManager.getHeaders())
                .queryParams(ConfigManager.getParams())
                .log().all();

        System.out.println("Running tests in environment:  " + ConfigManager.getEnvironment());

    }

    public static RestClient getInstance(){
        if (instance == null)
            instance = new RestClient();
        return instance;
    }

    public RequestSpecification getRequest(){
        return request;
    }

}
