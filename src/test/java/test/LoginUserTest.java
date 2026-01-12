package test;

import client.UserClient;
import io.restassured.response.Response;
import model.User;
import model.UserCredentials;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class LoginUserTest {

    private final UserClient userClient = new UserClient();

    private User randomUser() {
        return new User("test" + System.currentTimeMillis() + "@mail.ru", "123456", "TestUser");
    }

    @Test
    public void loginSuccess() {
        User user = randomUser();
        userClient.createUser(user);

        Response response = userClient.login(new UserCredentials(user.getEmail(), user.getPassword()));
        response.then().statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    public void loginWithWrongPassword() {
        User user = randomUser();
        userClient.createUser(user);

        Response response = userClient.login(new UserCredentials(user.getEmail(), "wrong"));
        response.then().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }
}
