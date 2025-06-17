package basics;

import io.restassured.RestAssured;
import org.testng.annotations.*;

import static io.restassured.RestAssured.given;

public class StaticNonStaticTest {

    @Test
    public void nonStaticTest(){
        // Non-static method
        RestAssured.given()
                .when()
                .then();
    }

    @Test
    public void staticTest(){
        // static method
        given()
                .when()
                .then();
    }
}
