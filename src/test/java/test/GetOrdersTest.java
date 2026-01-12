package test;

import client.OrderClient;
import client.UserClient;
import io.restassured.response.Response;
import model.User;
import model.UserCredentials;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class GetOrdersTest {

    private final OrderClient orderClient = new OrderClient();

    private String getToken(User user) {
        UserClient userClient = new UserClient();
        userClient.createUser(user);
        Response loginResponse = userClient.login(new UserCredentials(user.getEmail(), user.getPassword()));
        loginResponse.then().statusCode(HttpStatus.SC_OK);
        return loginResponse.path("accessToken");
    }

    @Test
    public void getOrdersWithAuth() {
        User user = new User("test" + System.currentTimeMillis() + "@mail.ru", "123456", "Test");
        String token = getToken(user);

        Response response = orderClient.getOrders(token);
        response.then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void getOrdersWithoutAuth() {
        Response response = orderClient.getOrdersWithoutAuth();
        response.then().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }
}
