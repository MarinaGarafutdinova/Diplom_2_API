package orderTests;

import objects.User;
import steps.UserSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OrderGetTests {
    protected String email;
    protected String password;
    protected String name;
    private UserSteps userSteps;
    private String accessToken;
    private User user;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        name = "Marina";
        email = "Mari111@yandex.ru";
        password = "Pas111";
        userSteps = new UserSteps();
        user = new User(name, email, password);
        UserSteps.postCreateUser(user);
        accessToken = UserSteps.authUserLogin(user).then().extract().path("accessToken");

    }

    @Test
    @DisplayName("Получение списка заказов авторизованным пользователем.")
    @Description("Проверка получения списка заказов авторизованным пользователем.")
    public void getUserOrdersWithAuthTest() {
        Response response = UserSteps.getOrderListWithAuth(user, accessToken);
        response.then().log().all()
                .assertThat().statusCode(200).and().body("success", Matchers.is(true))
                .and().body("orders", Matchers.notNullValue())
                .and().body("total", Matchers.any(Integer.class))
                .and().body("totalToday", Matchers.any(Integer.class));
    }

    @Test
    @DisplayName("Получение списка заказов пользователем без авторизации.")
    @Description("Проверка попытки получения списка заказов пользователем без авторизации.")
    public void getUserOrdersWithoutAuthTest() {
        Response response = UserSteps.getOrderListWithoutAuth(user);
        response.then().log().all()
                .assertThat().statusCode(401)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("You should be authorised"));
    }

    @After    // Удаление созданного пользователя
    public void tearDown() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
        }
    }
}
