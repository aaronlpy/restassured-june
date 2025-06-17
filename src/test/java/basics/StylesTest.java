package basics;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class StylesTest {


    @Test
    public void staticStyleTest(){
        Response response = RestAssured.get("https://jsonplaceholder.typicode.com/todos/1");
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println(response.getBody().prettyPeek());
    }

    @Test
    public void BBDStyleTest(){
        given()
                .log().all()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/1")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void FluentAPIStyleTest(){
        Response response = given().when().get("https://jsonplaceholder.typicode.com/todos/1");
        response.then().log().all();
    }

}
