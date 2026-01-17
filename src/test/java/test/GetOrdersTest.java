package test;

import client.OrderClient;
import client.UserClient;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import model.User;
import model.UserCredentials;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class GetOrdersTest {

    private final OrderClient orderClient = new OrderClient();
    private final UserClient userClient = new UserClient();
    private final Faker faker = new Faker();


    private User generateUser() {
        return new User(
                faker.internet().emailAddress(),
                faker.internet().password(6, 12),
                faker.name().firstName()
        );
    }

    private String getToken(User user) {
        userClient.createUser(user);

        Response loginResponse = userClient.login(
                new UserCredentials(user.getEmail(), user.getPassword())
        );

        loginResponse.then().statusCode(HttpStatus.SC_OK);
        return loginResponse.path("accessToken");
    }

    @Test
    public void getOrdersWithAuth() {
        User user = generateUser();
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
