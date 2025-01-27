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

public class UserUpdateTests {
    private UserSteps userSteps;
    private User user;
    private String accessToken;

    private final String updName = "Mari"+ RandomStringUtils.randomAlphabetic(3);
    private final String updEmail = "Mari"+ RandomStringUtils.randomAlphabetic(3)+ "@yandex.ru";
    private final String updPassword = "Pas" + RandomStringUtils.randomAlphabetic(3);
    User updUser = new User();


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        String name = "Marina";
        String email = "Mari111@yandex.ru";
        String password = "Pas111";
        userSteps = new UserSteps();
        user = new User(name, email, password);
        UserSteps.postCreateUser(user);
        accessToken = UserSteps.authUserLogin(user).then().extract().path("accessToken");
    }

    @Test
    @DisplayName("Изменение имени пользователя с авторизацией.")
    @Description("Проверка успешного изменения имени пользователя с авторизацией.")
    public void updateUserNameWithAuthTest() {
        updUser.setName(updName);
        user.setName(updName);
        Response response = userSteps.updateUserDataWithAuth(updUser, accessToken);
        response.then().log().all().assertThat()
                .statusCode(200)
                .and().body("success", Matchers.is(true));
    }

    @Test
    @DisplayName("Изменение логина пользователя с авторизацией.")
    @Description("Проверка успешного изменения логина пользователя с авторизацией.")
    public void updateUserLoginWithAuthTest() {
        updUser.setEmail(updEmail);
        user.setEmail(updEmail);
        Response response = userSteps.updateUserDataWithAuth(updUser, accessToken);
        response.then().log().all().assertThat()
                .statusCode(200)
                .and().body("success", Matchers.is(true));
    }

    @Test
    @DisplayName("Изменение пароля пользователя с авторизацией.")
    @Description("Проверка успешного изменения пароля пользователя с авторизацией.")
    public void updateUserPassWithAuthTest() {
        updUser.setPassword(updPassword);
        user.setPassword(updPassword);
        Response response = userSteps.updateUserDataWithAuth(updUser, accessToken);
        response.then().log().all().assertThat()
                .statusCode(200)
                .and().body("success", Matchers.is(true));
    }

    @Test
    @DisplayName("Изменение логина пользователя без авторизации.")
    @Description("Проверка попытки изменения логина пользователя без авторизации.")
    public void updateUserLoginWithoutAuthTest() {
        updUser.setEmail(updEmail);
        user.setEmail(updEmail);
        Response response = userSteps.updateUserDataWithoutAuth(updUser);
        response.then().log().all().assertThat()
                .statusCode(401)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение пароля пользователя без авторизации.")
    @Description("Проверка попытки изменения пароля пользователя без авторизации.")
    public void updateUserPassWithoutAuthTest() {
        updUser.setPassword(updPassword);
        user.setPassword(updPassword);
        Response response = userSteps.updateUserDataWithoutAuth(updUser);
        response.then().log().all().assertThat()
                .statusCode(401)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("You should be authorised"));
    }

    @After
    public void tearDown() {
        // Удаление созданного пользователя
        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
        }
    }
}

