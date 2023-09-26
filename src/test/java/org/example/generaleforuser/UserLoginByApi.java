package org.example.generaleforuser;

import com.github.javafaker.Faker;
import dto.User;
import dto.UserLogin;
import dto.response.CreateUserResponse;
import org.apache.http.entity.ContentType;
import org.junit.After;
import org.junit.Before;

import static io.restassured.RestAssured.given;

public class UserLoginByApi extends BaseApi {
    protected String accessToken;
    protected String refreshToken;

    @Before
    public void setUp() {
        Faker faker = new Faker();
        User user = new User(
                faker.internet().emailAddress(),
                faker.internet().password(),
                faker.name().firstName()
        );
        UserLogin createUserBody = new UserLogin(
                user.getEmail(),
                user.getPassword(),
                user.getName()
        );
        //создаем пользователя
        given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType()) // заполнили header
                .body(createUserBody)
                .post("/api/auth/register")
                .then()
                .statusCode(200);

        UserLogin loginBody = new UserLogin(
                user.getEmail(),
                user.getPassword(),
                null
        );
        var userLoginResponse = given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType()) // заполнили header
                .body(loginBody)
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .extract().body().as(CreateUserResponse.class);

        accessToken = userLoginResponse.getAccessToken();
        refreshToken = userLoginResponse.getRefreshToken();
    }

    @After
    public void deleteUser() {
        given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType()) // заполнили header
                .header("Authorization", accessToken)
                .delete("/api/auth/user")
                .then()
                .statusCode(202);
    }
}

