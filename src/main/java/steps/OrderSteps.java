package steps;

import objects.Ingredients;
import objects.Order;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class OrderSteps {

    private final static String GET_INGREDIENTS = "/api/ingredients";
    private final static String CREATE_ORDER = "/api/orders";

    @Step("Получение данных об ингредиентах.")
    public Ingredients getIngredient() {
        return given()
                .header("Content-Type", "application/json")
                .log().all()
                .get(GET_INGREDIENTS)
                .body()
                .as(Ingredients.class);
    }

    @Step("Создание заказа с авторизацией.")
    public static Response createOrderWithAuth(Order order, String accessToken) {
        return given().log().all()
                .filter(new AllureRestAssured())
                .header("Content-Type", "application/json")
                .header("authorization",accessToken)
                .body(order)
                .when()
                .post(CREATE_ORDER);
    }

    @Step("Создание заказа без авторизации.")
    public static Response createOrderWithoutAuth(Order order) {
        return given().log().all()
                .filter(new AllureRestAssured())
                .header("Content-Type", "application/json")
                .body(order)
                .when()
                .post(CREATE_ORDER);
    }


}

