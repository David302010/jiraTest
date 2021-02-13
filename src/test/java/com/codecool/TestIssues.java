package com.codecool;

import com.codecool.pages.CreateIssuePage;
import com.codecool.pages.DashBoardPage;
import com.codecool.pages.IssuesPage;
import com.codecool.pages.LoginPage;
import com.codecool.util.WebDriverSingleton;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestIssues {
    private static WebDriver driver = WebDriverSingleton.getInstance();
    private static final LoginPage loginPage = new LoginPage();
    private static final DashBoardPage dashBoardPage = new DashBoardPage();
    private final CreateIssuePage createIssuePage = new CreateIssuePage();
    private final IssuesPage issuesPage = new IssuesPage();

    @BeforeAll
    public static void setUp() {
        driver.get("https://jira.codecool.codecanvas.hu/secure/Dashboard.jspa");
        loginPage.loginSuccessful();
    }

    @ParameterizedTest
    @CsvSource({"TOUCAN, Task"})
    public void testCreateIssue(String project, String issueType) throws InterruptedException {
//        clickCreateButton();
        String result = createIssuePage.createNewIssue(project, issueType, "randomString");
        boolean resultActual = createIssuePage.compare(result, project);
        dashBoardPage.deleteIssueByIssueId(result);
        assertTrue(resultActual);
    }

    @ParameterizedTest
    @MethodSource("createListOfIssueType")
    public void testIssueTypeOfProject(String project, String issueType) throws InterruptedException {
//        clickCreateButton();
        String issueId = createIssuePage.createNewIssue(project, issueType, "randomString");
        String actualIssueType = dashBoardPage.getIssueTypeByIssueId(issueId);
        dashBoardPage.deleteIssueByIssueId(issueId);
        assertEquals(issueType, actualIssueType);
    }

    @AfterAll
    private static void tearDown() {
        dashBoardPage.logout();
        dashBoardPage.close();
        dashBoardPage.quit();
    }

    private static List<Arguments> createListOfIssueType() {
        List<String> issueTypes = Arrays.asList("Story", "Task", "Improvement");
        List<String> projects = Arrays.asList("TOUCAN");
        List<Arguments> argumentsList = new ArrayList<>();
        for (String project : projects) {
            for (String type : issueTypes) {
                argumentsList.add(Arguments.of(project, type));
            }
        }
        return argumentsList;
    }

    private void clickCreateButton() {
        try {
            dashBoardPage.getCreateIssueButton().click();
        } catch (NoSuchElementException nse) {
            loginPage.loginSuccessful();
            dashBoardPage.getCreateIssueButton().click();
        }
    }

}
