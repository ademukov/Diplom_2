package org.example;

import com.github.javafaker.Faker;
import dto.User;
import dto.UserLogin;
import dto.response.CreateUserResponse;
import org.apache.http.entity.ContentType;
import org.example.generaleforuser.BaseApi;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TestUserLogin extends BaseApi {
    protected User user;
    protected User user2;
    //логин пользователя проверяется в классе TestUserCreate "Создание уникального пользователя и его авторизация"

    @Test
    @DisplayName("Логин с неверным логином")
    public void loginWithWrongLogin() {
        Faker faker = new Faker();
        user = new User(faker.internet().emailAddress(), faker.internet().password(), faker.name().firstName());
        user2 = new User(faker.internet().emailAddress(), faker.internet().password(), faker.name().firstName());
        UserLogin createUserForLoginCheck = new UserLogin(user.getEmail(), user.getPassword(), user.getName());//создаем объект для создания пользователя
        //создаем пользователя
        var response = given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType()) // заполнили header
                .body(createUserForLoginCheck)
                .post("/api/auth/register")
                .then()
                .extract().body().as(CreateUserResponse.class);
        MatcherAssert.assertThat(response.getSuccess(), equalTo(true));
        MatcherAssert.assertThat(response.getUser(), notNullValue());

        UserLogin loginUserForLoginCheck = new UserLogin(user2.getEmail(), user.getPassword(), null);//создаем объект для логина пользователя
        var response2 = given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType()) // заполнили header
                .body(loginUserForLoginCheck)
                .post("/api/auth/login")
                .then()
                .assertThat()
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
    @Test
    @DisplayName("Логин с неверным паролем")
    public void loginWithWrongPassword() {
        Faker faker = new Faker();
        user = new User(faker.internet().emailAddress(), faker.internet().password(), faker.name().firstName());
        user2 = new User(faker.internet().emailAddress(), faker.internet().password(), faker.name().firstName());
        UserLogin createUserForLoginCheck = new UserLogin(user.getEmail(), user.getPassword(), user.getName());//создаем объект для создания пользователя
        //создаем пользователя
        var response = given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType()) // заполнили header
                .body(createUserForLoginCheck)
                .post("/api/auth/register")
                .then()
                .extract().body().as(CreateUserResponse.class);
        MatcherAssert.assertThat(response.getSuccess(), equalTo(true));
        MatcherAssert.assertThat(response.getUser(), notNullValue());

        UserLogin loginUserForLoginCheck = new UserLogin(user.getEmail(), user2.getPassword(), null);//создаем объект для логина пользователя
        var response2 = given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType()) // заполнили header
                .body(loginUserForLoginCheck)
                .post("/api/auth/login")
                .then()
                .assertThat()
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}
