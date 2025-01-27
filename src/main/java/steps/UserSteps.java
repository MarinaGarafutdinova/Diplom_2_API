package steps;

import objects.User;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;


public class UserSteps {

    private final static String REGISTER_USER = "api/auth/register";
    private final static String LOGIN_USER = "api/auth/login";
    private final static String UPD_USER = "api/auth/user";
    private final static String GET_ORDER_LIST = "/api/orders";

    @Step("Создание нового пользователя.")
    public static Response postCreateUser(User user) {
        return given()
                .log()
                .all()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(REGISTER_USER);
    }



    @Step("Логин под существующим пользователем.")
    public static Response authUserLogin(User user) {
        return given()
                .log()
                .all()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(LOGIN_USER);
    }

    @Step("Изменение данных пользователя с авторизацией.")
    public Response updateUserDataWithAuth(User user, String accessToken) {
        return given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .header("authorization", accessToken)
                .body(user)
                .when()
                .patch(UPD_USER);
    }

    @Step("Изменение данных пользователя без авторизации.")
    public Response updateUserDataWithoutAuth(User user) {
        return given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .patch(UPD_USER);
    }


    @Step("Удаление пользователя")
    public void deleteUser(String accessToken){
        given()
                .header("Authorization", accessToken)
                .when()
                .delete(UPD_USER);
    }

    @Step("Получение списка заказов авторизованного пользователя.")
    public static Response getOrderListWithAuth(User user, String accessToken) {
        return given().log().all()
                .filter(new AllureRestAssured())
                .header("Content-Type", "application/json")
                .header("authorization",accessToken)
                .body(user)
                .when()
                .get(GET_ORDER_LIST);

    }

    @Step("Получение списка заказов  пользователя без авторизации.")
    public static Response getOrderListWithoutAuth(User user) {
        return given().log().all()
                .filter(new AllureRestAssured())
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .get(GET_ORDER_LIST);

    }
}

