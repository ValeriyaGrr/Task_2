package test;

import client.OrderClient;
import client.UserClient;
import io.restassured.response.Response;
import model.Order;
import model.User;
import model.UserCredentials;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderTest {

    private final OrderClient orderClient = new OrderClient();

    private String getToken(User user) {
        UserClient userClient = new UserClient();
        userClient.createUser(user);
        Response loginResponse = userClient.login(new UserCredentials(user.getEmail(), user.getPassword()));
        loginResponse.then().statusCode(HttpStatus.SC_OK);
        return loginResponse.path("accessToken");
    }

    @Test
    public void createOrderWithIngredients() {
        User user = new User("test" + System.currentTimeMillis() + "@mail.ru", "123456", "Test");
        String token = getToken(user);

        Response response = orderClient.createOrder(token, new Order(List.of("61c0c5a71d1f82001bdaaa6d")));
        response.then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void createOrderWithoutIngredients() {
        User user = new User("test" + System.currentTimeMillis() + "@mail.ru", "123456", "Test");
        String token = getToken(user);

        Response response = orderClient.createOrder(token, new Order(null));
        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void createOrderWithoutAuth() {
        Response response = orderClient.createOrderWithoutAuth(
                new Order(List.of("61c0c5a71d1f82001bdaaa6d"))
        );
        response.then().statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    public void createOrderWithWrongIngredients() {
        User user = new User("test" + System.currentTimeMillis() + "@mail.ru", "123456", "Test");
        String token = getToken(user);

        Response response = orderClient.createOrder(token, new Order(List.of("wrong_hash")));
        response.then().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}
