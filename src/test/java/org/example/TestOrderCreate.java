package org.example;

import dto.Ingredient;
import dto.Order;
import org.apache.http.entity.ContentType;
import org.example.generaleforuser.UserLoginByApi;
import org.junit.BeforeClass;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TestOrderCreate extends UserLoginByApi {
    public static List<Ingredient> ingredients;

    @BeforeClass
    public static void ingredients() {
        ingredients =
                given().get("/api/ingredients")
                        .then()
                        .assertThat()
                        .statusCode(200)
                        .extract()
                        .jsonPath()
                        .getList("data", Ingredient.class);

    }

    @Test
    @DisplayName("Создание заказа с ингредиентами авторизованным пользователем")
    public void createOrderWithIngredientsAuthorizedUser() {
        Ingredient firstIngredient = ingredients.stream()
                .findFirst()
                .orElseThrow();
        Ingredient randomIngredient = ingredients.stream()
                .skip(new Random().nextInt(ingredients.size() - 1))
                .findFirst()
                .orElseThrow();
        Order order = new Order();
        order.setIngredients(List.of(firstIngredient.getId(), randomIngredient.getId()));

        given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType()) // заполнили header
                .header("Authorization", accessToken)
                .body(order)
                .post("/api/orders")
                .then()
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа с неверным хэшем ингридиентов авторизованным пользователем")
    public void createOrderWithWrongHashAuthorizedUser() {
        Ingredient firstIngredient = ingredients.stream()
                .findFirst()
                .orElseThrow();
        ingredients.stream()
                .skip(new Random().nextInt(ingredients.size() - 1))
                .findFirst()
                .orElseThrow();
        Order order = new Order();
        order.setIngredients(List.of(firstIngredient.getId(), "61c0c5a71d1f821111111111"));

        given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType()) // заполнили header
                .header("Authorization", accessToken)
                .body(order)
                .post("/api/orders")
                .then()
                .statusCode(500)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов авторизованным пользователем")
    public void createOrderWithoutIngredientsAuthorizedUser() {
        given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .header("Authorization", accessToken)
                .body(new Order())
                .post("/api/orders")
                .then()
                .statusCode(400)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами НЕ авторизованным пользователем")
    public void createOrderWithIngredientsNotAuthorizedUser() {

        Ingredient firstIngredient = ingredients.stream()
                .findFirst()
                .orElseThrow();
        Ingredient randomIngredient = ingredients.stream()
                .skip(new Random().nextInt(ingredients.size() - 1))
                .findFirst()
                .orElseThrow();
        Order order = new Order();
        order.setIngredients(List.of(firstIngredient.getId(), randomIngredient.getId()));

        given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(order)
                .post("/api/orders")
                .then()
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа с неверным хэшем ингридиентов НЕ авторизованным пользователем")
    public void createOrderWithWrongHashNotAuthorizedUser() {
        Ingredient firstIngredient = ingredients.stream()
                .findFirst()
                .orElseThrow();
        ingredients.stream()
                .skip(new Random().nextInt(ingredients.size() - 1))
                .findFirst()
                .orElseThrow();
        Order order = new Order();
        order.setIngredients(List.of(firstIngredient.getId(), "61c0c5a71d1f821111111111"));

        given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(order)
                .post("/api/orders")
                .then()
                .statusCode(500)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов НЕ авторизованным пользователем")
    public void createOrderWithoutIngredientsNotAuthorizedUser() {

        given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(new Order())
                .post("/api/orders")
                .then()
                .statusCode(400)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }
}
