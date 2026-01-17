package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {

    private static final String ORDERS = "/api/orders";

    @Step("Создать заказ с авторизацией")
    public Response createOrder(String token, Order order) {
        return given()
                .baseUri(Constants.BASE_URL)
                .header("Authorization", token)
                .header("Content-Type", "application/json")
                .body(order)
                .post(ORDERS);
    }

    @Step("Создать заказ без авторизации")
    public Response createOrderWithoutAuth(Order order) {
        return given()
                .baseUri(Constants.BASE_URL)
                .header("Content-Type", "application/json")
                .body(order)
                .post(ORDERS);
    }

    @Step("Получить заказы пользователя с авторизацией")
    public Response getOrders(String token) {
        return given()
                .baseUri(Constants.BASE_URL)
                .header("Authorization", token)
                .get(ORDERS);
    }

    @Step("Получить заказы без авторизации")
    public Response getOrdersWithoutAuth() {
        return given()
                .baseUri(Constants.BASE_URL)
                .get(ORDERS);
    }
}
