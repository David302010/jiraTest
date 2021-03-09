package com.codecool.pages;

import com.codecool.util.WebDriverSingleton;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class CreateIssuePage {
    WebDriver driver = WebDriverSingleton.getInstance();
    WebDriverWait wait = new WebDriverWait(driver, 5);
    LoginPage loginPage = new LoginPage();
    private final IssuesPage issuesPage = new IssuesPage();
    private DashBoardPage dashBoardPage = new DashBoardPage();

    //div[contains(@class, 'aui-message-success') and contains(@class, 'shadowed')];
    @FindBy(xpath = "//div[contains(@class, 'aui-message-success') and contains(@class, 'shadowed')]")
    private WebElement successMessage;

    @FindBy(xpath = "//input[@id='project-field']")
    private WebElement dropDown;

    public CreateIssuePage() {
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 4), this);
    }

    @FindBy(id = "project-field")
    private WebElement dropdown;

    @FindBy(id = "issuetype-field")
    private WebElement dropDownIssue;

    @FindBy(id = "summary")
    private WebElement summary;

    @FindBy(xpath = "//a[@class=\"cancel\"]")
    private WebElement cancel;


    public String createNewIssue(String project, String issueType, String issueSummary) throws InterruptedException {
//        Thread.sleep(5000);
        wait.until(ExpectedConditions.elementToBeClickable(dashBoardPage.getCreateIssueButton()));
        dashBoardPage.getCreateIssueButton().click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        dropdown.click();
        dropdown.sendKeys(project);
        dropdown.sendKeys(Keys.TAB);

//        cancel.click();

        try {
            wait.until(ExpectedConditions.invisibilityOf(dropDownIssue));
        } catch (Exception e) {
            System.out.println("dropdown issue exception caught");
        }
        wait.until(ExpectedConditions.visibilityOf(dropDownIssue));
        dropDownIssue.click();
        dropDownIssue.sendKeys(issueType + Keys.TAB);

        /*try {

            wait.until(ExpectedConditions.stalenessOf(summary));
        } catch (Exception e) {
            System.out.println("summary exception caught");
        }
        wait.until(ExpectedConditions.visibilityOf(summary));*/

        // testing usability of other way to ignore StaleElementReferenceException
        wait.ignoring(StaleElementReferenceException.class)
                .until(ExpectedConditions.elementToBeClickable(summary));
        summary.click();
        summary.sendKeys(issueSummary);
        summary.sendKeys(Keys.TAB);
        summary.sendKeys(Keys.ENTER);

        /*try {
            wait.until(ExpectedConditions.stalenessOf(successMessage));
        } catch (Exception e) {
            System.out.println("success message staleness");
        }*/
        wait.until(ExpectedConditions.visibilityOf(successMessage));
        String id = getCreatedIssueId(successMessage.getText());
        System.out.println(successMessage.getText());
        System.out.println(id);

        return id;
    }

    private String getCreatedIssueId(String text) {
        String[] words = text.split(" ");
        return words[1];
    }

    public void deleteIssue() throws InterruptedException {
        Thread.sleep(2000);
        driver.findElement(By.xpath("//a[starts-with(@data-issue-key,'MTP')]")).click();
        driver.findElement(By.xpath("//a[@id='opsbar-operations_more']")).click();
        driver.findElement(By.xpath("//span[contains(text(),'Delete')]")).click();
        driver.findElement(By.xpath("//input[@id='delete-issue-submit']")).click();
    }

    public void searchForIssue() {
        driver.findElement(By.xpath("//a[@id='find_link']")).click();
        driver.findElement(By.xpath("//a[@id='filter_lnk_reported_lnk']")).click();
        WebElement searchQuery = driver.findElement(By.xpath("//input[@id='searcher-query']"));
        searchQuery.click();
        searchQuery.sendKeys("randomString");
        searchQuery.sendKeys(Keys.ENTER);
    }

    public boolean compare(String result, String project) {
        String[] resultArray = result.split("-");
        return resultArray[0].equals(project);
    }
}
