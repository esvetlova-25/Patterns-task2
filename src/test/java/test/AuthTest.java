package test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static data.DataGenerator.Registration.getRegisteredUser;
import static data.DataGenerator.Registration.getUser;
import static data.DataGenerator.getRandomLogin;
import static data.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999/");
    }


    @Test
    @DisplayName("Should successfully log in to an active account")
    void shouldOpenPersonalAccount() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("button").click();
        $("h2").shouldBe(Condition.visible);
        $("h2").shouldHave(Condition.exactText("Личный кабинет"));
    }

    @Test
    @DisplayName("Should give an error stating that the user is not registered")
    void shouldSendErrorUserNotRegistered()
    {
        var notRegisteredUser = getUser("active");
        $("[data-test-id=login] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id=password] input").setValue(notRegisteredUser.getPassword());
        $("button").click();
        $("[data-test-id=error-notification] .notification__content").shouldBe(Condition.visible);
        $("[data-test-id=error-notification] .notification__content").shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Should to send an error message the user is blocked")
    void shouldSendErrorUserBlocked()
    {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("button").click();
        $("[data-test-id=error-notification] .notification__content").shouldBe(Condition.visible);
        $("[data-test-id=error-notification] .notification__content").shouldHave(Condition.exactText("Ошибка! Пользователь заблокирован"), Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Should send an error message if the username is entered incorrectly")
    void shouldSendErrorInvalidLogin()
    {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id=login] input").setValue(wrongLogin);
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("button").click();
        $("[data-test-id=error-notification] .notification__content").shouldBe(Condition.visible);
        $("[data-test-id=error-notification] .notification__content").shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Should send an error message if the password is entered incorrectly")
    void shouldSendErrorInvalidPassword()
    {var wrongPassword = getRandomPassword();
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(wrongPassword);
        $("button").click();
        $("[data-test-id=error-notification] .notification__content").shouldBe(Condition.visible);
        $("[data-test-id=error-notification] .notification__content").shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10));
    }
}