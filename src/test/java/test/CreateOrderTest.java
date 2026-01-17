package test;

import client.IngredientClient;
import client.OrderClient;
import client.UserClient;
import com.github.javafaker.Faker;
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
    private final IngredientClient ingredientClient = new IngredientClient();
    private final Faker faker = new Faker();


    private User generateUser() {
        return new User(
                faker.internet().emailAddress(),
                faker.internet().password(6, 12),
                faker.name().firstName()
        );
    }

    private String getToken(User user) {
        UserClient userClient = new UserClient();

        userClient.createUser(user);

        Response loginResponse = userClient.login(
                new UserCredentials(user.getEmail(), user.getPassword())
        );

        loginResponse.then().statusCode(HttpStatus.SC_OK);
        return loginResponse.path("accessToken");
    }

    private List<String> getIngredientIds() {
        Response response = ingredientClient.getIngredients();
        System.out.println(response.asString()); // <- здесь увидишь, что пришло
        response.then().statusCode(HttpStatus.SC_OK);
        return response.jsonPath().getList("data._id", String.class);
    }

    @Test
    public void createOrderWithIngredients() {
        User user = generateUser();
        String token = getToken(user);

        List<String> ingredientIds = getIngredientIds();

        Response response = orderClient.createOrder(
                token,
                new Order(List.of(ingredientIds.get(0)))
        );

        response.then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void createOrderWithoutIngredients() {
        User user = generateUser();
        String token = getToken(user);

        Response response = orderClient.createOrder(token, new Order(null));

        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void createOrderWithoutAuth() {
        List<String> ingredientIds = getIngredientIds();

        Response response = orderClient.createOrderWithoutAuth(
                new Order(List.of(ingredientIds.get(0)))
        );

        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    public void createOrderWithWrongIngredients() {
        User user = generateUser();
        String token = getToken(user);

        Response response = orderClient.createOrder(
                token,
                new Order(List.of("wrong_hash"))
        );

        response.then().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}
