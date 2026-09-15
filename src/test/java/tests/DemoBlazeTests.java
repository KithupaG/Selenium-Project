package tests;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

public class DemoBlazeTests extends BaseTest {
    private static final By CART_ROWS = By.xpath("//tbody[@id='tbodyid']/tr");

    // Opens phones category
    private void openPhonesCategory() {
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Phones"))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[@id='tbodyid']//a[@class='hrefch']")
        ));
    }

    // Opens a product's detail page by its visible link text on the product grid.
    private void openProduct(String productName) {
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText(productName))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("name")));
    }

    // Clicks "Add to cart" button
    private String addOpenProductToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Add to cart"))).click();
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();

        alert.accept();
        return alertText;
    }

    // Navigate Home -> Phone -> the name product
    private void addPhoneToCart(String productName) {
        openPhonesCategory();
        openProduct(productName);
        addOpenProductToCart();
    }

    // Open cart page via the navbar
    private void openCart() {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("cartur"))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbodyid")));
    }


    // TEST CASES

    @Test
    public void homePageSmokeTest() {
        String title = driver.getTitle();
        Assert.assertFalse(title == null || title.trim().isEmpty(),
                "Home page title should not be empty");

        WebElement storeHeading = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("nava")));
        Assert.assertTrue(storeHeading.isDisplayed(), "PRODUCT STORE heading should be displayed");
        Assert.assertEquals(storeHeading.getText().trim(), "PRODUCT STORE", "Heading text should read PRODUCT STORE");
    }

    @Test
    public void productSelection() {
        openPhonesCategory();
        openProduct(PRODUCT);

        WebElement heading = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("name"))
        );
        Assert.assertEquals(heading.getText().trim(), PRODUCT,
                "Product heading should match the selected product");

        WebElement priceEl = driver.findElement(By.className("price-container"));
        String price = priceEl.getText();
        System.out.println("Product price: " + price);
        Assert.assertFalse(price.isEmpty(), "Product price should not be emptu");
    }

    @Test
    public void addToCart() {
        openPhonesCategory();
        openProduct(PRODUCT);

        String alertText = addOpenProductToCart();
        System.out.println("TC03 - Add-to-cart alert text: " + alertText);
        Assert.assertTrue(alertText.toLowerCase().contains("product added"),
                "Alert text should confirm the product was added");
    }

    @Test
    public void checkoutValidation() {
        addPhoneToCart(PRODUCT);
        openCart();
        wait.until(ExpectedConditions.numberOfElementsToBe(CART_ROWS, 1));

        wait.until(ExpectedConditions.elementToBeClickable(By.id("orderModalBtn"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("orderModal")));

        driver.findElement(By.xpath("/button[text()='Purchase']")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        Alert invalidAlert = driver.switchTo().alert();
        String invalidMessage = invalidAlert.getText();

        System.out.println("TC05 - Invalid checkout alert: " + invalidMessage);
        Assert.assertTrue(invalidMessage.toLowerCase().contains("please fill out"),
                "Alert text should indicate that required fields are missing");
        invalidAlert.accept();

        driver.findElement(By.id("name")).sendKeys(CHECKOUT_NAME);
        driver.findElement(By.id("country")).sendKeys(CHECKOUT_COUNTRY);
        driver.findElement(By.id("city")).sendKeys(CHECKOUT_CITY);
        driver.findElement(By.id("card")).sendKeys(CHECKOUT_CARD);
        driver.findElement(By.id("month")).sendKeys(CHECKOUT_MONTH);
        driver.findElement(By.id("year")).sendKeys(CHECKOUT_YEAR);

        driver.findElement(By.xpath("//button[text()='Purchase")).click();

        WebElement confirmation = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("sweet-alert")));
                String confirmationText = confirmation.getText();
                System.out.println("TC05 - Purchase conrimation: " + confirmationText);
                Assert.assertTrue(confirmationText.contains("Thank you for your purchase"), "Successful purchase should show a thank-you confirmatuon");

                driver.findElement(By.xpath("button[text()='OK']")).click();
    }
}
