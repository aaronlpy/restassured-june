package trello;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

public class TrelloTest {


    final static String API_KEY = "";
    final static String API_TOKEN="";
    String organizationId = "";
    protected RequestSpecification spec;

    @BeforeClass
    public void setup(){

        Map<String, String> params = new HashMap<>();
        params.put("token", API_TOKEN);
        params.put("key", API_KEY);

        spec = new RequestSpecBuilder()
                .setBaseUri("https://api.trello.com")
                .addQueryParams(params)
                .build();
    }


    @Test(description = "Create a new organization")
    public void addNewOrganizationTest(ITestContext context){
        JSONObject json = new JSONObject();
        json.put("displayName", "RestAssured Course");
        json.put("desc", "RestAssured Organization");

        Response response = given()
                .spec(spec)
                .body(json.toString())
                .contentType("application/json")
                .when()
                .post("/1/organizations");

        organizationId = response.then().extract().path("id");
        if (!organizationId.isEmpty())
            context.setAttribute("organizationId", organizationId);

        response
                .then()
                .log().all()
                .statusCode(200)
                .time(lessThan(3000L));
    }

    @AfterTest
    public void tearDown(ITestContext context){

        Response response = given()
                .spec(spec)
                .log().all()
                .pathParam("organizationId", context.getAttribute("organizationId"))
                .when()
                .delete("/1/organizations/{organizationId}");

        response.then().log().all().statusCode(200);
    }




}
