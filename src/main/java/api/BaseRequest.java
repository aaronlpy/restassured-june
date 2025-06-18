package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.HttpMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import service.RestClient;

public class BaseRequest {

    protected RestClient client;
    protected Response response;
    protected RequestSpecification test;

    @BeforeClass
    public void setup() {
        client = RestClient.getInstance();
    }

    @BeforeMethod
    public void setUpTest(){
        test = RestAssured.given().spec(this.client.getRequest());
    }

    public Response sendRequest(HttpMethod httpMethod, String resourcePath, String payload) throws Exception {
        try{
            switch (httpMethod){
                case GET -> {
                    return client.getRequest().get(resourcePath);
                }

                default -> throw new IllegalArgumentException("Unsupported HTTP method");
            }
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }



}
