package drata.web.test;

import java.time.Duration;
import java.util.*;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/****
 * TestNG and Selenium based web page test.
 *
 * Given the main web page, it does following:
 *  - First collect all the sub-page links
 *  - For each page, checks if Sign In link is available
 *  - For each page, check if logo is visible and no console errors are present.
 *  - Checks sign in action using invalid email and valid email
 */
public class DrataWebPageTest {

    private WebDriver driver;
    private static List<String> drataPageLinks;
    private static final String MAIN_WEB_PAGE = "https://www.drata.com/";
    private static final String DRATA_LOGO_CLASS_NAME = "mui-omha0-Media-root-Header-logo";
    private static final String SIGN_IN_LINK  = ".mui-lckw3x-MuiListItem-root-Header-headerMenuCtaItem";
    private static final String LOGIN_FIELD = "login-email";
    private static final String LOGIN_SUBMIT_BUTTON = "login-submit";

    @BeforeClass
    public void setUp() {
        driver = new FirefoxDriver();
        drataPageLinks = getPageLinks();
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    @DataProvider(name = "subPageLinks")
    public static Object[] getSubPageLinks() {
        Object[] objects = new Object[drataPageLinks.size()];

        for(int i=0; i<drataPageLinks.size(); i++) {
            objects[i] = drataPageLinks.get(i);
        }

        return objects;
    }

    // This test will run for each page link and verify if Sign In link exists on the page
    @Test(dataProvider = "subPageLinks")
    public void testSignInLink(String pageLink) {
        //System.out.println("Input page link - " + pageLink);

        driver.get(pageLink);
        String pageTitle = driver.getTitle();
        boolean signInVisible = false;
        try {
            driver.findElement(By.linkText("Sign In"));
            signInVisible = true;
        } catch(NoSuchElementException e) {
        }

        Assert.assertTrue(signInVisible, "Sign in link not visible on the page "+ pageLink + " ("+ pageTitle + ")");
    }

    // This test will run for each page link and verify if Drata logo is visible on the page
    @Test(dataProvider = "subPageLinks")
    public void testDrataLogoAndConsoleErrors(String pageLink) {
        //System.out.println("Input page link - " + pageLink);
        driver.get(pageLink);
        String pageTitle = driver.getTitle();

        boolean logoVisible = false;
        try {
            driver.findElement(By.className(DRATA_LOGO_CLASS_NAME)).isDisplayed();
            logoVisible = true;
        } catch(NoSuchElementException e) {
        }

        Assert.assertTrue(logoVisible, "Drata logo is not visible on the page "+ pageLink + " ("+ pageTitle + ")");

        List<Object> consoleErrors = getConsoleErrors();
        boolean consoleErrorsPresent = (consoleErrors != null && consoleErrors.size() > 0);
        Assert.assertFalse(consoleErrorsPresent, "Console errors present on the page "+ pageLink + " ("+ pageTitle + ")");
    }

    /*
     *  - Test sign page for error message when proper email is not entered.
     *  - Check if the page is redirected to IDP login when valid email is entered.
     */
    @Test
    public void testSignInProcess(){
        driver.get(MAIN_WEB_PAGE);
        driver.findElement(By.cssSelector(SIGN_IN_LINK)).click();

        List<String> tabs = new ArrayList<> (driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        WebElement loginElement = driver.findElement(By.id(LOGIN_FIELD));
        loginElement.isDisplayed();

        // Try login with invalid email entry
        loginElement.sendKeys("DummyEmail");
        driver.findElement(By.id(LOGIN_SUBMIT_BUTTON)).click();
        Assert.assertEquals(driver.findElement(By.cssSelector(".text-danger")).getText(), "Email must be a valid email");

        loginElement.clear();
        loginElement.sendKeys("testMail@gmail.com");
        driver.findElement(By.id(LOGIN_SUBMIT_BUTTON)).click();
        Assert.assertTrue(driver.findElement(By.className("applogin-app-title")).isDisplayed(), "Paged directed to login page");
    }

    /***
     * Get all sub pages links
     */
    private List<String> getPageLinks() {
        driver.get(MAIN_WEB_PAGE);
        Set<String> pageSubLinks = new TreeSet<>();
        List<WebElement> links = driver.findElements(By.tagName("a"));

        // Iterating through all the Links
        for (WebElement link : links) {
            //System.out.println("Link --> "+ link.getText());
            String linkPath = link.getAttribute("href");
            if(linkPath.contains("drata.com")) {
                pageSubLinks.add(linkPath);

                // limit to 50 pages
                if(pageSubLinks.size() >= 50) {
                    break;
                }
            }
        }

        return new ArrayList<>(pageSubLinks);
    }

    private List<Object> getConsoleErrors() {
        String script = "return window.JSErrorCollector_errors ? window.JSErrorCollector_errors.pump() : []";
        List<Object> consoleErrors = (List<Object>) ((JavascriptExecutor) driver).executeScript(script);

        return consoleErrors;
    }

}
