package jsonplaceholder;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.lessThan;

public class JsonPlaceHolderTest {

    @Test
    public void getPostsTest() {
        Response response = RestAssured
                .given()
                .when()
                .get("https://jsonplaceholder.typicode.com/posts");

        response.then()
                .log().all()
                .statusCode(200)
                .time(lessThan(3000L));
    }

    @Test
    public void addNewPostTest() {

        JSONObject body = new JSONObject();
        body.put("userId", 1);
        body.put("title", "This is a random title");
        body.put("body", "This is a random body");

        Response response = RestAssured
                .given()
                .body(body.toString())
                .when()
                .post("https://jsonplaceholder.typicode.com/posts");

        response.then()
                .log().all()
                .statusCode(201)
                .time(lessThan(3000L));


    }

    @Test
    public void getPostByQueryParams(){

        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        Response r = RestAssured
                .given()
                .queryParam("id", 102)
                .log().all()
                .when()
                .get("/posts");

        r.then()
                .log().all()
                .statusCode(200)
                .time(lessThan(3000L));

    }

    @Test
    public void getPostByPathParams(){

        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        Response r = RestAssured
                .given()
                .pathParam("postId", 10)
                .log().all()
                .when()
                .get("/posts/{postId}");

        r.then()
                .log().all()
                .statusCode(200)
                .time(lessThan(3000L));

    }


}
