package test;

import client.UserClient;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import model.User;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest {

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
    public void createUniqueUser() {
        Response response = userClient.createUser(generateUser());

        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    public void createDuplicateUser() {
        User user = generateUser();
        userClient.createUser(user);

        Response response = userClient.createUser(user);

        response.then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void createUserWithoutEmail() {
        User user = new User(
                null,
                faker.internet().password(6, 12),
                faker.name().firstName()
        );

        Response response = userClient.createUser(user);

        response.then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }
}
