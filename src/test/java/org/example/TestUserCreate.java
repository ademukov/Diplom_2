package org.example;

import com.github.javafaker.Faker;
import dto.User;
import dto.UserLogin;
import dto.response.CreateUserResponse;
import org.apache.http.entity.ContentType;
import org.example.generaleforuser.BaseApi;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TestUserCreate extends BaseApi {
    protected User user;

    @Test
    @DisplayName("Создание уникального пользователя и его авторизация")
    public void createAndAuthUser() {
        Faker faker = new Faker();
        user = new User(faker.internet().emailAddress(), faker.internet().password(), faker.name().firstName());
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

        UserLogin loginUserForLoginCheck = new UserLogin(user.getEmail(), user.getPassword(), null);//создаем объект для логина пользователя
        given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType()) // заполнили header
                .body(loginUserForLoginCheck)
                .post("/api/auth/login")
                .then()
                .extract().body().as(CreateUserResponse.class);
        MatcherAssert.assertThat(response.getSuccess(), equalTo(true));
        MatcherAssert.assertThat(response.getUser(), notNullValue());
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void createCreatedUser() {
        Faker faker = new Faker();
        user = new User(faker.internet().emailAddress(), faker.internet().password(), faker.name().firstName());
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

        given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType()) // заполнили header
                .body(createUserForLoginCheck)
                .post("/api/auth/register")
                .then()
                .assertThat()
                .statusCode(403)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя (не заполнено поле password)")
    public void createUserWithoutPassword() {
        Faker faker = new Faker();
        user = new User(faker.internet().emailAddress(), null, faker.name().firstName());
        UserLogin createUserForLoginCheck = new UserLogin(user.getEmail(), user.getPassword(), user.getName());//создаем объект для создания пользователя
        //создаем пользователя
        given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType()) // заполнили header
                .body(createUserForLoginCheck)
                .post("/api/auth/register")
                .then()
                .assertThat()
                .statusCode(403)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя (не заполнено поле email)")
    public void createUserWithoutEmail() {
        Faker faker = new Faker();
        user = new User(null, faker.internet().password(), faker.name().firstName());
        UserLogin createUserForLoginCheck = new UserLogin(user.getEmail(), user.getPassword(), user.getName());//создаем объект для создания пользователя
        //создаем пользователя
        given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType()) // заполнили header
                .body(createUserForLoginCheck)
                .post("/api/auth/register")
                .then()
                .assertThat()
                .statusCode(403)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя (не заполнено поле name)")
    public void createUserWithoutName() {
        Faker faker = new Faker();
        user = new User(faker.internet().emailAddress(), faker.internet().password(), null);
        UserLogin createUserForLoginCheck = new UserLogin(user.getEmail(), user.getPassword(), user.getName());//создаем объект для создания пользователя
        //создаем пользователя
        given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType()) // заполнили header
                .body(createUserForLoginCheck)
                .post("/api/auth/register")
                .then()
                .assertThat()
                .statusCode(403)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
