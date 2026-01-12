package test;

import client.UserClient;
import io.restassured.response.Response;
import model.User;
import model.UserCredentials;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UpdateUserTest {

    private final UserClient userClient = new UserClient();

    private String getToken(User user) {
        userClient.createUser(user);
        Response loginResponse = userClient.login(new UserCredentials(user.getEmail(), user.getPassword()));
        loginResponse.then().statusCode(HttpStatus.SC_OK);
        return loginResponse.path("accessToken");
    }

    @Test
    public void updateUserWithAuth() {
        User user = new User("test" + System.currentTimeMillis() + "@mail.ru", "123456", "Test");
        String token = getToken(user);

        User updated = new User("new" + System.currentTimeMillis() + "@mail.ru", "654321", "NewName");
        Response response = userClient.updateUser(token, updated);
        response.then().statusCode(HttpStatus.SC_OK).body("success", equalTo(true));
    }

    @Test
    public void updateUserWithoutAuth() {
        User updated = new User("new@mail.ru", "654321", "NewName");
        Response response = userClient.updateUserWithoutAuth(updated);
        response.then().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }
}
