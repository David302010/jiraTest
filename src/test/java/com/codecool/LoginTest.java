package com.codecool;

import com.codecool.pages.AlternateLogin;
import com.codecool.pages.CreateIssuePage;
import com.codecool.pages.DashBoardPage;
import com.codecool.pages.LoginPage;
import com.codecool.util.WebDriverSingleton;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest {
    private static WebDriver driver = WebDriverSingleton.getInstance();
    private LoginPage loginPage = new LoginPage();
    private static final DashBoardPage dashBoardPage =  new DashBoardPage();
    private AlternateLogin alternateLogin = new AlternateLogin();
    private CreateIssuePage createIssuePage = new CreateIssuePage();

    @BeforeAll
    public static void setUp() {
        driver.get("https://jira.codecool.codecanvas.hu/secure/Dashboard.jspa");
    }

    @ParameterizedTest
    @CsvSource({"User 10"})
    public void testLoginSuccessful(String userId) {
        loginPage.loginSuccessful();
        boolean isLogOutPresent = dashBoardPage.checkLogout();
        String userName = dashBoardPage.checkUserName();
        assertTrue(isLogOutPresent && userId.equals(userName));
    }

    @Test
    public void testLoginFailedWithIncorrectPassword() throws InterruptedException {
        WebElement loginError = loginPage.loginFailed("incorrectPassword");
        assertNotNull(loginError);
    }

    @Test
    public void testLoginFailedWithIncorrectUserName() throws InterruptedException {
        WebElement loginError = loginPage.loginFailed("incorrectUsername");
        assertNotNull(loginError);
    }

    /*@Test
    public void loginWrongPassword3Times() throws InterruptedException {
        WebElement captcha = loginPage.loginWrongPassword3Times();
        assertNotNull(captcha);
    }*/


    @ParameterizedTest
    @CsvSource({"User 10"})
    public void testAlternateLoginSuccessful(String userId) {
        alternateLogin.loginSuccessfulAlternateLoginPage();
        boolean isLogOutPresent = dashBoardPage.checkLogout();
        String userName = dashBoardPage.checkUserName();
        assertTrue(isLogOutPresent && userId.equals(userName));
    }

    @AfterAll
    public static void tearDown() {
        dashBoardPage.logout();
        dashBoardPage.close();
    }
}

