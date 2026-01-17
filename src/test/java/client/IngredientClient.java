package client;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class IngredientClient {

    private static final String INGREDIENTS_PATH = "/api/ingredients";

    public Response getIngredients() {
        return given()
                .baseUri(Constants.BASE_URL)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .get(INGREDIENTS_PATH);
    }

}