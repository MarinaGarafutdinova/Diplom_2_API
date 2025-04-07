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


public class UserCreatingTest {
    private String name;
    private String email;
    private String password;
    private User user;
    private UserSteps userSteps;

    @Before
    public void setUp(){
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        name = "Mari"+ RandomStringUtils.randomAlphabetic(3);
        email = "Mari"+ RandomStringUtils.randomAlphabetic(3)+ "@yandex.ru";
        password = "Pas" + RandomStringUtils.randomAlphabetic(3);
        userSteps = new UserSteps();
        user = new User();
    }

    @Test
    @DisplayName("Регистрация нового пользователя.")
    @Description("Проверка создания нового пользователя.")
    public void checkCreateUserTest() {
        user = new User(name, email, password);
        Response response = UserSteps.postCreateUser(user);
        response.then().log().all().assertThat()
                .statusCode(200)
                .and().body("success", Matchers.is(true))
                .and().body("accessToken", Matchers.notNullValue())
                .and().body("refreshToken", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Регистрация уже существующнго пользователя.")
    @Description("Проверка попытки создания уже существующего пользователя.")
    public void CreateExistUserTest() {
        user = new User(name, email, password);
        UserSteps.postCreateUser(user);
        Response response = UserSteps.postCreateUser(user);
        response.then().log().all().assertThat()
                .statusCode(403)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("User already exists"));
    }

    @Test
    @DisplayName("Регистрация  пользователя без почты.")
    @Description("Проверка создания пользователя без обязательного поля email.")
    public void createUserWithoutEmailTest() {
        user = new User(name, null, password);
        Response response = UserSteps.postCreateUser(user);
        response.then().log().all().assertThat()
                .statusCode(403)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Регистрация  пользователя без пароля.")
    @Description("Проверка создания пользователя без обязательного поля password.")
    public void createUserWithoutPasswordTest() {
        user = new User(name, email, null);
        Response response = UserSteps.postCreateUser(user);
        response.then().log().all().assertThat()
                .statusCode(403)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Email, password and name are required fields"));
    }


    @After  // Удаление созданного пользователя
    public void tearDown(){
        String accessToken = UserSteps.authUserLogin(user).then().extract().path("accessToken");
        if (accessToken !=null) {
            userSteps.deleteUser(accessToken);
        }
    }
}

