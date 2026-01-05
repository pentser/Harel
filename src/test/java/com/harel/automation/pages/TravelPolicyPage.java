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
    // Continents are displayed as MuiGrid items after clicking first time purchase
    private By continentSelectionGrid = By.xpath("//div[contains(@class, 'MuiGrid-item') and contains(@class, 'MuiGrid-grid-xs-6')]");
    private By continueToTravelDatesButton = By.xpath("//button[contains(@class, 'MuiButton')]");
    
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
            // Wait for continent grid items to be visible
            wait.until(ExpectedConditions.presenceOfElementLocated(continentSelectionGrid));
            Thread.sleep(1000);
            
            // Find all continent grid items and click the first visible one
            java.util.List<WebElement> continentOptions = driver.findElements(continentSelectionGrid);
            System.out.println("Found " + continentOptions.size() + " continent options");
            
            // Click the first visible continent
            for (WebElement continent : continentOptions) {
                if (continent.isDisplayed()) {
                    String continentText = continent.getText();
                    System.out.println("Clicking continent: " + continentText);
                    continent.click();
                    Thread.sleep(1000);
                    return;
                }
            }
            
            // If no continent found, throw exception
            throw new RuntimeException("No visible continent options found");
            
        } catch (Exception e) {
            System.err.println("Error selecting continent: " + e.getMessage());
            e.printStackTrace();
            tryAlternativeClick(continentSelectionGrid);
        }
    }
    
    /**
     * Click on "Continue to travel dates selection" button
     */
    public void clickContinueToTravelDates() {
        try {
            // Wait a bit for the button to appear after selecting continent
            Thread.sleep(1500);
            
            // Find the button - it's the MUI button at the bottom
            java.util.List<WebElement> buttons = driver.findElements(continueToTravelDatesButton);
            System.out.println("Found " + buttons.size() + " buttons");
            
            // Click the last visible button (continue button is usually at the bottom)
            WebElement continueButton = null;
            for (WebElement btn : buttons) {
                if (btn.isDisplayed() && btn.isEnabled()) {
                    String buttonText = btn.getText();
                    System.out.println("Button text: " + buttonText);
                    // Look for button with text containing "הלאה" (continue)
                    if (buttonText.contains("הלאה") || buttonText.length() > 10) {
                        continueButton = btn;
                        break;
                    }
                }
            }
            
            if (continueButton != null) {
                continueButton.click();
                System.out.println("Clicked continue button");
                
                // Wait for URL to change to dates page
                wait.until(ExpectedConditions.urlContains("/wizard/date"));
                Thread.sleep(1000); // Extra wait for page elements to load
                System.out.println("Successfully navigated to dates page");
            } else {
                throw new RuntimeException("Continue button not found");
            }
        } catch (Exception e) {
            System.err.println("Error clicking continue button: " + e.getMessage());
            e.printStackTrace();
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

