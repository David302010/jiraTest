package com.codecool.pages;

import com.codecool.util.WebDriverSingleton;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AlternateLogin {
    public AlternateLogin() {
        PageFactory.initElements(driver, this);
    }

    WebDriver driver = WebDriverSingleton.getInstance();

    @FindBy(id = "login-form-username")
    private WebElement username;

    @FindBy(id = "login-form-password")
    private WebElement password;

    @FindBy(id = "login-form-submit")
    private WebElement loginButton;

    public void loginSuccessfulAlternateLoginPage() {
//        driver.navigate().to("https://jira.codecool.codecanvas.hu/login.jsp");
        driver.get("https://jira.codecool.codecanvas.hu/login.jsp");
        driver.manage().window().maximize();
        username.sendKeys(System.getenv("JIRAUSERNAME"));
        password.sendKeys(System.getenv("JIRAPASSWORD"));
        loginButton.click();
    }
}
