package basics;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class RestAssuredBasicsTest {

    @Test
    public void GetTest(){
        Response r = RestAssured.given()
                .when()
                .get();

        r.then().log().all();
    }

    @Test
    public void PostTest(){
        Response r = RestAssured.given()
                .when()
                .post();

        r.then().log().all();
    }

    @Test
    public void PutTest(){
        Response r = RestAssured.given()
                .when()
                .put();

        r.then().log().all();
    }

    @Test
    public void PatchTest(){
        Response r = RestAssured.given()
                .when()
                .patch();

        r.then().log().all();
    }

    @Test
    public void DeleteTest(){
        Response r = RestAssured.given()
                .when()
                .delete();

        r.then().log().all();
    }

    @Test
    public void OptionsTest(){
        Response r = RestAssured.given()
                .when()
                .options();

        r.then().log().all();
    }

    @Test
    public void HeadTest(){
        Response r = RestAssured.given()
                .when()
                .head();

        r.then().log().all();
    }
}
