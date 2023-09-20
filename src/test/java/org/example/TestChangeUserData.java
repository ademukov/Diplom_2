package org.example;

import com.github.javafaker.Faker;
import dto.User;
import dto.UserLogin;
import dto.response.CreateUserResponse;
import org.apache.http.entity.ContentType;
import org.example.generaleforuser.BaseApi;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TestChangeUserData extends BaseApi {
    protected User user;
    protected User user2;

    @Test
    @DisplayName("Изменение имени пользователя (пользователь авторизован)")
    public void changeNameAuthorizedUser() {
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

        UserLogin loginUserForLoginCheck = new UserLogin(user.getEmail(), user.getPassword(), user2.getName());//создаем объект для логина пользователя
        given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType()) // заполнили header
                .header("Authorization", response.getAccessToken())
                .body(loginUserForLoginCheck)
                .patch("/api/auth/user")
                .then()
                .assertThat()
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true))
                .body("user.name", equalTo(user2.getName()));
    }

    @Test
    @DisplayName("Изменение имени пользователя (пользователь НЕ авторизован)")
    public void changeNameNotAuthorizedUser() {
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

        UserLogin loginUserForLoginCheck = new UserLogin(user.getEmail(), user.getPassword(), user2.getName());//создаем объект для логина пользователя
        given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType()) // заполнили header
                .body(loginUserForLoginCheck)
                .patch("/api/auth/user")
                .then()
                .assertThat()
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение логина пользователя (пользователь авторизован)")
    public void changeLoginAuthorizedUser() {
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

        UserLogin loginUserForLoginCheck = new UserLogin(user2.getEmail(), user.getPassword(), user.getName());//создаем объект для логина пользователя
        given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType()) // заполнили header
                .header("Authorization", response.getAccessToken())
                .body(loginUserForLoginCheck)
                .patch("/api/auth/user")
                .then()
                .assertThat()
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true))
                .body("user.email", equalTo(user2.getEmail()));
    }

    @Test
    @DisplayName("Изменение логина пользователя (пользователь НЕ авторизован)")
    public void changeLoginNotAuthorizedUser() {
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

        UserLogin loginUserForLoginCheck = new UserLogin(user2.getEmail(), user.getPassword(), user.getName());//создаем объект для логина пользователя
        given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType()) // заполнили header
                .body(loginUserForLoginCheck)
                .patch("/api/auth/user")
                .then()
                .assertThat()
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}
