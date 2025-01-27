package orderTests;

import objects.Ingredients;
import objects.Order;
import objects.User;
import steps.OrderSteps;
import steps.UserSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;


public class OrderCreatingTests {

    private UserSteps userSteps;
    private String accessToken;
    private OrderSteps orderSteps;
    private List<String> ingredient;
    private Order order;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        String name = "Marina";
        String email = "Mari111@yandex.ru";
        String password = "Pas111";
        userSteps = new UserSteps();
        orderSteps = new OrderSteps();
        User user = new User(name, email, password);
        UserSteps.authUserLogin(user);
        accessToken = UserSteps.postCreateUser(user).then().extract().path("accessToken");
        ingredient = new ArrayList<>();
        order = new Order(ingredient);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией.")
    @Description("Проверка успешного создания заказа с авторизацией.")
    public void createOrderWithAuthTest() {
        Ingredients ingredients = orderSteps.getIngredient();
        ingredient.add(ingredients.getData().get(0).get_id());
        ingredient.add(ingredients.getData().get(1).get_id());
        ingredient.add(ingredients.getData().get(3).get_id());
        Response response = OrderSteps.createOrderWithAuth(order, accessToken);
        response.then().log().all()
                .assertThat().statusCode(200)
                .and().body("success", Matchers.is(true))
                .and().body("name", Matchers.notNullValue())
                .and().body("order.number", Matchers.notNullValue());

    }

    @Test
    @DisplayName("Создание заказа без авторизации.")
    @Description("Проверка попытки создания заказа без авторизации.")
    public void createOrderWithoutAuthTest() {
        Ingredients ingredients = orderSteps.getIngredient();
        ingredient.add(ingredients.getData().get(0).get_id());
        ingredient.add(ingredients.getData().get(2).get_id());
        ingredient.add(ingredients.getData().get(3).get_id());
        Response response = OrderSteps.createOrderWithoutAuth(order);
        response.then().log().all()
                .assertThat()
                .statusCode(200);
    }


    @Test
    @DisplayName("Создание заказа без ингредиентов.")
    @Description("Проверка создания заказа без ингредиентов.")
    public void createEmptyOrderTest() {
        Response response = OrderSteps.createOrderWithAuth(order, accessToken);
        response.then().log().all()
                .assertThat().statusCode(400)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Ingredient ids must be provided"));

    }



    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов.")
    @Description("Проверка создания заказа с неверным хешем ингредиентов.")
    public void createOrderWithFalseHashTest() {
        Ingredients ingredients = orderSteps.getIngredient();
        ingredient.add(ingredients.getData().get(0).get_id() + (1 + (int) (Math.random() * 3)));
        ingredient.add(ingredients.getData().get(2).get_id() + (1 + (int) (Math.random() * 3)));
        Response response = OrderSteps.createOrderWithAuth(order, accessToken);
        response.then().log().all()
                .statusCode(500);
    }

    @After  // Удаление созданного пользователя
    public void tearDown() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
        }
    }
}
