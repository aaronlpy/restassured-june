package trello;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import listeners.ExtentReportListener;
import org.json.JSONObject;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.JSONDataLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Listeners(listeners.ExtentReportListener.class)
public class TrelloTest {

    public static Response response;
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
    public void addNewOrganizationTest(ITestContext context, ITestResult result, Map<String, String> data) {

        JSONObject json = new JSONObject();
        json.put("displayName", data.get("organizationName"));
        json.put("desc", data.get("organizationDescription"));

        response = given()
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

            response = given()
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


    @Test(description = "Get lists on board", dependsOnMethods = "addNewBoardTest")
    public void getListsOnBoardTest(ITestContext context){

        response = given()
                .spec(spec)
                .pathParam("boardId", context.getAttribute("boardId"))
                .when()
                .get("/1/boards/{boardId}/lists");

        List<Map<String, Object>> items = response.jsonPath().getList("$");
        items.forEach(item -> {
            context.setAttribute(item.get("name").toString().replace(" ", ""), item.get("id"));
        });

        response.then()
                .log().all()
                .statusCode(200)
                .body("", hasSize(3));

    }

    @Test(description = "Create a new Card", dependsOnMethods = "getListsOnBoardTest")
    public void createNewCard(ITestContext context){

        String idList = context.getAttribute("Doing").toString();

        Map<String, String> params = new HashMap<>();
        params.put("idList", idList);
        params.put("name", "RestAssured Fundamentals");
        params.put("desc", "This is a random card description");

        response = given()
                .spec(spec)
                .queryParams(params)
                .contentType("application/json")
                .when()
                .post("/1/cards");


        String cardId = response.jsonPath().getString("id");
        if (cardId != null)
            context.setAttribute("cardId", cardId);


        response.then()
                .log().all()
                .statusCode(200)
                .body("id", notNullValue())
                .body("idList", equalTo(idList))
                .time(lessThan(3000L));

    }

    @Test(description = "Add attachment to card", dependsOnMethods = "createNewCard", dataProvider = "data", dataProviderClass = JSONDataLoader.class)
    public void addAttachmentToCard(ITestContext context, Map<String, String> data){

        JSONObject payload = new JSONObject();
        payload.put("url", data.get("imgUrl"));
        payload.put("setCover", true);
        payload.put("name", "puppy.png");

        response = given()
                .spec(spec)
                .body(payload.toString())
                .contentType(ContentType.JSON)
                .pathParam("cardId", context.getAttribute("cardId"))
                .when()
                .post("/1/cards/{cardId}/attachments");


        response.then()
                .log().all()
                .statusCode(200)
                .time(lessThanOrEqualTo(3000L))
                .body("id", notNullValue())
                .body("isUpload", equalTo(true))
                .body("isMalicious", equalTo(false));
    }


    @Test(description = "Update an existing card", dependsOnMethods = "createNewCard")
    public void updateCard(ITestContext context){

        response = given()
                .spec(spec)
                .queryParam("idList", context.getAttribute("Done"))
                .pathParam("cardId", context.getAttribute("cardId"))
                .contentType(ContentType.JSON)
                .when()
                .put("/1/cards/{cardId}");


        response.then()
                .statusCode(200)
                .body("idList", equalTo(context.getAttribute("Done").toString()));

    }




    @AfterMethod
    public void addLogsToReport() {
        ExtentReportListener.getTest().log(Status.PASS, MarkupHelper.createCodeBlock(response.body().asString(), CodeLanguage.JSON));
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
