package test;

import client.UserClient;
import io.restassured.response.Response;
import model.User;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest {

    private final UserClient userClient = new UserClient();

    private User randomUser() {
        return new User(
                "test" + System.currentTimeMillis() + "@mail.ru",
                "123456",
                "TestUser"
        );
    }

    @Test
    public void createUniqueUser() {
        Response response = userClient.createUser(randomUser());
        response.then().statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    public void createDuplicateUser() {
        User user = randomUser();
        userClient.createUser(user);

        Response response = userClient.createUser(user);
        response.then().statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void createUserWithoutEmail() {
        User user = new User(null, "123456", "Test");

        Response response = userClient.createUser(user);
        response.then().statusCode(HttpStatus.SC_FORBIDDEN);
    }
}
