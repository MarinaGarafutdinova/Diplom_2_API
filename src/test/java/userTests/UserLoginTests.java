package userTests;

import objects.User;
import steps.UserSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserLoginTests {
    private String email;
    private String password;
    private String name;
    private UserSteps userSteps;
    private User user;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        name = "Marina";
        email = "Mari111@yandex.ru";
        password = "Pas111";
        userSteps = new UserSteps();
        user = new User(name, email, password);
    }


    @Test
    @DisplayName("Авторизация пользователя.")
    @Description("Проверка авторизации под существующим пользователем.")
    public void authorizationTest() {
        user = new User(name, email, password);
        UserSteps.postCreateUser(user);
        Response response = UserSteps.authUserLogin(user);
        response.then().log().all().assertThat()
                .statusCode(200)
                .and().body("success", Matchers.is(true))
                .and().body("accessToken", Matchers.notNullValue())
                .and().body("refreshToken", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Авторизация с неверным логином.")
    @Description("Проверка авторизации пользователя c неверным логином.")
    public void authorizationWithFalseLoginTest() {
        user = new User(name, email, password);
        user.setEmail(RandomStringUtils.randomAlphabetic(3) + email);
        Response response = UserSteps.authUserLogin(user);
        response.then().log().all().assertThat()
                .statusCode(401)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация с неверным паролем.")
    @Description("Проверка авторизации пользователя c неверным паролем.")
    public void authorizationIncorrectPasswordTest() {
        user = new User(name, email, password);
        user.setPassword(RandomStringUtils.randomAlphabetic(3) + password);
        Response response = UserSteps.authUserLogin(user);
        response.then().log().all().assertThat()
                .statusCode(401)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("email or password are incorrect"));
    }

    @After
    public void tearDown(){
        // Удаление созданного пользователя
        String accessToken = UserSteps.authUserLogin(user).then().extract().path("accessToken");
        if (accessToken !=null) {
            userSteps.deleteUser(accessToken);
        }
    }
}

