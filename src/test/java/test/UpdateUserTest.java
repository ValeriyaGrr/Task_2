package test;

import client.UserClient;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import model.User;
import model.UserCredentials;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UpdateUserTest {

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
    public void updateUserWithAuth() {
        User user = generateUser();
        String token = getToken(user);

        User updatedUser = new User(
                faker.internet().emailAddress(),
                faker.internet().password(6, 12),
                faker.name().firstName()
        );

        Response response = userClient.updateUser(token, updatedUser);

        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    public void updateUserWithoutAuth() {
        User updatedUser = new User(
                faker.internet().emailAddress(),
                faker.internet().password(6, 12),
                faker.name().firstName()
        );

        Response response = userClient.updateUserWithoutAuth(updatedUser);

        response.then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }
}
