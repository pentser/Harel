package com.harel.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object Model for the Travel Policy Landing Page
 */
public class TravelPolicyPage {
    
    private WebDriver driver;
    private WebDriverWait wait;
    
    // Locators - using multiple strategies to ensure compatibility
    private By firstTimePurchaseButton = By.xpath("//*[contains(text(), 'לרכישה בפעם הראשונה') or contains(text(), 'First time purchase')]");
    private By continentSelection = By.xpath("//button[contains(@class, 'continent') or contains(@class, 'destination')]");
    private By continueToTravelDatesButton = By.xpath("//*[contains(text(), 'הלאה לבחירת תאריכי') or contains(text(), 'Continue')]");
    
    public TravelPolicyPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }
    
    /**
     * Click on "First time purchase" button
     */
    public void clickFirstTimePurchaseButton() {
        try {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(firstTimePurchaseButton));
            button.click();
            Thread.sleep(1000); // Small wait for smooth transition
        } catch (Exception e) {
            System.err.println("Error clicking first time purchase button: " + e.getMessage());
            // Try alternative approaches
            tryAlternativeClick(firstTimePurchaseButton);
        }
    }
    
    /**
     * Select a continent from available options
     */
    public void selectContinent() {
        try {
            // Wait for continent options to be visible
            wait.until(ExpectedConditions.presenceOfElementLocated(continentSelection));
            Thread.sleep(500);
            
            // Find all continent buttons and click the first visible one
            WebElement continentButton = wait.until(ExpectedConditions.elementToBeClickable(continentSelection));
            continentButton.click();
            Thread.sleep(1000);
        } catch (Exception e) {
            System.err.println("Error selecting continent: " + e.getMessage());
            tryAlternativeClick(continentSelection);
        }
    }
    
    /**
     * Click on "Continue to travel dates selection" button
     */
    public void clickContinueToTravelDates() {
        try {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(continueToTravelDatesButton));
            button.click();
            Thread.sleep(1500); // Wait for page transition
        } catch (Exception e) {
            System.err.println("Error clicking continue button: " + e.getMessage());
            tryAlternativeClick(continueToTravelDatesButton);
        }
    }
    
    /**
     * Alternative click method using JavaScript executor
     */
    private void tryAlternativeClick(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            Thread.sleep(1000);
        } catch (Exception e) {
            throw new RuntimeException("Failed to click element after multiple attempts: " + locator, e);
        }
    }
}

