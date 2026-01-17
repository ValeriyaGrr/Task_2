package test;

import client.UserClient;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import model.User;
import model.UserCredentials;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class LoginUserTest {

    private final UserClient userClient = new UserClient();
    private final Faker faker = new Faker();


    private User generateUser() {
        return new User(
                faker.internet().emailAddress(),
                faker.internet().password(6, 12),
                faker.name().firstName()
        );
    }


    @Test
    public void loginSuccess() {
        User user = generateUser();
        userClient.createUser(user);

        Response response = userClient.login(
                new UserCredentials(user.getEmail(), user.getPassword())
        );

        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    public void loginWithWrongPassword() {
        User user = generateUser();
        userClient.createUser(user);

        Response response = userClient.login(
                new UserCredentials(user.getEmail(), "wrong_password")
        );

        response.then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }
}
