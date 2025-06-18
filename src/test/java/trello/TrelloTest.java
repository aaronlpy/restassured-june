package trello;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.JSONDataLoader;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

public class TrelloTest {


    final static String API_KEY = "";
    final static String API_TOKEN = "";
    String organizationId = "";
    protected RequestSpecification spec;

    @BeforeClass
    public void setup() {
        Map<String, String> params = new HashMap<>();
        params.put("token", API_TOKEN);
        params.put("key", API_KEY);

        spec = new RequestSpecBuilder()
                .setBaseUri("https://api.trello.com")
                .addQueryParams(params)
                .build();
    }

    @Test(description = "Create a new organization", dataProvider = "data", dataProviderClass = JSONDataLoader.class)
    public void addNewOrganizationTest(ITestContext context, Map<String, String> data) {

        JSONObject json = new JSONObject();
        json.put("displayName", data.get("organizationName"));
        json.put("desc", data.get("organizationDescription"));

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

    @Test(description = "Create a new board",
            dependsOnMethods = "addNewOrganizationTest",
            dataProvider = "data",
            dataProviderClass = JSONDataLoader.class)
    public void addNewBoardTest(ITestContext context, Map<String, String> data) throws Exception {
        try {

            Map<String, String> params = new HashMap<>();
            params.put("name", data.get("boardName"));
            params.put("desc", data.get("boardDescription"));

            Response response = given()
                    .spec(spec)
                    .queryParams(params)
                    .queryParam("idOrganization", context.getAttribute("organizationId"))
                    .contentType("application/json")
                    .log().all()
                    .when()
                    .post("/1/boards");

            response.then().log().all();

            String boardId = response.then().extract().path("id");
            if (!boardId.isEmpty())
                context.setAttribute("boardId", boardId);


            response.then().log().all().statusCode(200);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @AfterClass(description = "Clean all requests")
    public void tearDown(ITestContext context) {

        if (context.getAttribute("boardId") != null) {
            given()
                    .spec(spec)
                    .pathParam("boardId", context.getAttribute("boardId"))
                    .when()
                    .delete("/1/boards/{boardId}");
        }

        if (context.getAttribute("organizationId") != null) {
            given()
                    .spec(spec)
                    .log().all()
                    .pathParam("organizationId", context.getAttribute("organizationId"))
                    .when()
                    .delete("/1/organizations/{organizationId}");
        }
    }
}
