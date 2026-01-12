package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.User;
import model.UserCredentials;

import static io.restassured.RestAssured.given;

public class UserClient {

    private static final String REGISTER = "/api/auth/register";
    private static final String LOGIN = "/api/auth/login";
    private static final String USER = "/api/auth/user";

    @Step("Создать пользователя")
    public Response createUser(User user) {
        return given()
                .baseUri(Constants.BASE_URL)
                .header("Content-Type", "application/json")
                .body(user)
                .post(REGISTER);
    }

    @Step("Войти под пользователем")
    public Response login(UserCredentials creds) {
        return given()
                .baseUri(Constants.BASE_URL)
                .header("Content-Type", "application/json")
                .body(creds)
                .post(LOGIN);
    }

    @Step("Обновить данные пользователя с авторизацией")
    public Response updateUser(String token, User user) {
        return given()
                .baseUri(Constants.BASE_URL)
                .header("Authorization", token)
                .header("Content-Type", "application/json")
                .body(user)
                .patch(USER);
    }

    @Step("Обновить данные пользователя без авторизации")
    public Response updateUserWithoutAuth(User user) {
        return given()
                .baseUri(Constants.BASE_URL)
                .header("Content-Type", "application/json")
                .body(user)
                .patch(USER);
    }
}
