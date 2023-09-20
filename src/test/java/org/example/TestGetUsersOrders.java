package org.example;

import dto.Ingredient;
import dto.Order;
import org.apache.http.entity.ContentType;
import org.example.generaleforuser.UserLoginByApi;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TestGetUsersOrders extends UserLoginByApi {
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

    @Before
    public void createOrders() {
        Order order = new Order();
        order.setIngredients(ingredients
                .stream()
                .map(Ingredient::getId)
                .collect(Collectors.toList()));
        given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType()) // заполнили header
                .header("Authorization", accessToken)
                .body(order)
                .post("/api/orders")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Получение списка заказов авторизованным пользователем")
    public void authorized() {
        given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType()) // заполнили header
                .header("Authorization", accessToken)

                .get("/api/orders/all")
                .then()
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Получение списка заказов неавторизованным пользователем")
    public void nonAuthorized() {
        given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType()) // заполнили header
                .get("/api/orders/all")
                .then()
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
    }
}
