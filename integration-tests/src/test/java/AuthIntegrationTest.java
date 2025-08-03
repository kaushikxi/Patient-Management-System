import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.notNullValue;

public class AuthIntegrationTest {
    @BeforeAll
    public static void setUp(){
        RestAssured.baseURI = "http://localhost:4004"; // this is going to happen before all of our test runs as this is the address of the api gateway when all containers run inside docker
    }

    @Test
    public void shouldReturnOKWithValidToken(){
        // 1. Arrange - you do any setup that this test needs to work 100% of the time
        // 2. Act - trigger the thing that we're testing
        // 3. Assert - to check whether the response has an OK token

        String loginPayload = """
                    {
                        "email" : "testuser@test.com",
                        "password" : "password123"
                    }
                """;
        Response response = RestAssured.given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract().response();

        System.out.println("Generated token " + response.jsonPath().getString("token"));

    }


    @Test
    public void shouldReturnUnauthorizedOnInvalidLogin(){
        // 1. Arrange - you do any setup that this test needs to work 100% of the time
        // 2. Act - trigger the thing that we're testing
        // 3. Assert - to check whether the response has an OK token

        String loginPayload = """
                    {
                        "email" : "invalid_user@test.com",
                        "password" : "password213"
                    }
                """;
        RestAssured.given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401);

    }
}
