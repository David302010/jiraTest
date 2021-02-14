package com.codecool;

import com.codecool.pages.*;
import com.codecool.util.WebDriverSingleton;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {
//    private static WebDriver driver = WebDriverSingleton.getInstance();
    private static LoginPage loginPage = new LoginPage();
    private static DashBoardPage dashBoardPage = new DashBoardPage();
//    AlternateLogin alternateLogin = new AlternateLogin();
//    IssuesPage issuesPage = new IssuesPage();
    private final CreateIssuePage createIssuePage = new CreateIssuePage();

    @BeforeAll
    public static void setUp() {
//        driver.get("https://jira.codecool.codecanvas.hu/secure/Dashboard.jspa");
        loginPage.loginSuccessful();
    }

//    @Test
//    public void testLogout() {
//        WebElement logoutConfirmation = dashBoardPage.logout();
//        Assertions.assertNotNull(logoutConfirmation);
//    }

    @ParameterizedTest
    @CsvSource({"TOUCAN projekt, TOUCAN",
                "COALA Project, COALA",
                "JETI Project, JETI"})
    public void testBrowseProject(String expected, String project){
        String projectName = dashBoardPage.browseProject(project);
        assertEquals(expected, projectName);
    }

    @Test
    public void searchProject() {
        String actualProject = dashBoardPage.searchProject("Main Testing", "MTP");
        assertEquals("Main Testing Project", actualProject);
    }

    /*@ParameterizedTest
    @CsvSource({"TOUCAN, Task",
            "COALA, Sub-task"})
    public void testCreateIssue(String project, String issueType) throws InterruptedException {
        loginPage.loginSuccessful();
        dashBoardPage.getCreateIssueButton().click();
        String result = createIssuePage.createNewIssue(project, issueType, "randomString");
        boolean resultActual = createIssuePage.compare(result, project);
        dashBoardPage.deleteIssue(result);
        assertTrue(resultActual);
    }*/

    /*@ParameterizedTest
    @MethodSource("createStreamOfIssueType")
    public void testCreateIssueWithIssueType(String project, String issueType) throws InterruptedException {
        loginPage.loginSuccessful();
        dashBoardPage.getCreateIssueButton().click();
        String issueId = createIssuePage.createNewIssue(project, issueType, "randomString");
        dashBoardPage.searchForIssueCreatedByMe(issueId);
        //boolean resultActual = createIssuePage.compare(result, project);
        dashBoardPage.deleteIssue(issueId);
        assertTrue(resultActual);

    }*/

    @ParameterizedTest
    @MethodSource("createListOfIssueType")
    public void testIssueTypeAndProjectName(String project, String issueType) throws InterruptedException {
//        loginPage.loginSuccessful();
//        dashBoardPage.getCreateIssueButton().click();
        String issueId = createIssuePage.createNewIssue(project, issueType, "randomString");
        String actualIssueType = dashBoardPage.getIssueTypeByIssueId(issueId);
        String actualProject = issueId.split("-")[0];
        dashBoardPage.deleteIssueByIssueId(issueId);
        assertEquals(issueType, actualIssueType);
        assertEquals(project, actualProject);
    }

    private static List<Arguments> createListOfIssueType() {
        List<String> issueTypes = Arrays.asList("Bug", "Task", "Story", "Improvement");
        List<String> projects = Arrays.asList("COALA", "JETI", "TOUCAN");
//        List<String> projects = Arrays.asList("COALA", "TOUCAN");
        List<Arguments> argumentsList = new ArrayList<>();
        for (String project : projects) {
            for (String type : issueTypes) {
                argumentsList.add(Arguments.of(project, type));
            }
        }
        return argumentsList;
    }

    @AfterAll
    public static void tearDown() {
//        dashBoardPage.logout();
        dashBoardPage.close();
    }
}
