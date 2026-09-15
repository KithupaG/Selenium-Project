package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public class BaseTest {
    static final String BASE_URL = "https://www.demoblaze.com/";
    static final String PRODUCT = "Samsung galaxy s6";
    static final String PRODUCT_1 = "Nokia lumia 1520";
    static final String CHECKOUT_NAME = "Test Student";
    static final String CHECKOUT_COUNTRY = "Sri Lanka";
    static final String CHECKOUT_CITY = "Colombo";
    static final String CHECKOUT_CARD = "4111111111111111";
    static final String CHECKOUT_MONTH = "12";
    static final String CHECKOUT_YEAR = "2027";

    static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);

        driver.get(BASE_URL);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if(driver != null) {
            driver.quit();
        }
    }
}
