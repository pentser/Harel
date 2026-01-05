package com.harel.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Page Object Model for the Travel Dates Selection Page
 */
public class TravelDatesPage {
    
    private WebDriver driver;
    private WebDriverWait wait;
    
    // Locators - based on actual page structure
    private By departureDateInput = By.id("travel_start_date");  // Input with id="travel_start_date", name="start"
    private By returnDateInput = By.id("travel_end_date");       // Input with id="travel_end_date", name="end"
    private By datePickerCalendar = By.xpath("//div[contains(@class, 'MuiPickersCalendar')]");
    private By totalDaysDisplay = By.xpath("//*[contains(text(), 'ימים') or contains(text(), 'days') or contains(text(), 'סה\"כ') or contains(text(), 'משך')]");
    private By continueToPassengerDetailsButton = By.xpath("//button[contains(@class, 'MuiButton')]");
    private By passengerDetailsSection = By.xpath("//*[contains(@class, 'passenger') or contains(text(), 'נוסעים') or contains(text(), 'Passenger')]");
    
    public TravelDatesPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }
    
    /**
     * Select departure date using direct input
     */
    public void selectDepartureDate(LocalDate date) {
        try {
            // Wait for input field to be available
            WebElement departureDateField = wait.until(ExpectedConditions.presenceOfElementLocated(departureDateInput));
            
            // Format date as dd/mm/yyyy
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = date.format(formatter);
            
            // Clear and enter the date
            departureDateField.clear();
            departureDateField.sendKeys(formattedDate);
            Thread.sleep(500);
            
            System.out.println("Entered departure date: " + formattedDate);
            
        } catch (Exception e) {
            System.err.println("Error selecting departure date: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to select departure date: " + date, e);
        }
    }
    
    /**
     * Select return date using direct input
     */
    public void selectReturnDate(LocalDate date) {
        try {
            // Wait for input field to be available
            WebElement returnDateField = wait.until(ExpectedConditions.presenceOfElementLocated(returnDateInput));
            
            // Format date as dd/mm/yyyy
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = date.format(formatter);
            
            // Clear and enter the date
            returnDateField.clear();
            returnDateField.sendKeys(formattedDate);
            Thread.sleep(500);
            
            System.out.println("Entered return date: " + formattedDate);
            
        } catch (Exception e) {
            System.err.println("Error selecting return date: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to select return date: " + date, e);
        }
    }
    
    
    /**
     * Get the displayed total days text
     */
    public String getTotalDaysDisplayed() {
        try {
            WebElement totalDaysElement = wait.until(ExpectedConditions.presenceOfElementLocated(totalDaysDisplay));
            String totalDaysText = totalDaysElement.getText();
            System.out.println("Total days element text: " + totalDaysText);
            return totalDaysText;
        } catch (Exception e) {
            System.err.println("Error getting total days: " + e.getMessage());
            // Try to find any element containing numbers
            try {
                List<WebElement> elements = driver.findElements(By.xpath("//*[contains(text(), 'ימים') or contains(text(), 'days')]"));
                for (WebElement element : elements) {
                    String text = element.getText();
                    if (text != null && !text.isEmpty()) {
                        return text;
                    }
                }
            } catch (Exception ex) {
                System.err.println("Alternative method also failed: " + ex.getMessage());
            }
            return null;
        }
    }
    
    /**
     * Click on "Continue to passenger details" button
     */
    public void clickContinueToPassengerDetails() {
        try {
            Thread.sleep(1000); // Wait for button to be ready
            
            // Find all buttons and click the one with appropriate text
            java.util.List<WebElement> buttons = driver.findElements(continueToPassengerDetailsButton);
            System.out.println("Found " + buttons.size() + " buttons");
            
            WebElement continueButton = null;
            for (WebElement btn : buttons) {
                if (btn.isDisplayed() && btn.isEnabled()) {
                    String btnText = btn.getText();
                    if (btnText.contains("הלאה") || btnText.contains("המשך") || btnText.length() > 10) {
                        continueButton = btn;
                        System.out.println("Found continue button: " + btnText);
                        break;
                    }
                }
            }
            
            if (continueButton != null) {
                continueButton.click();
                Thread.sleep(2000); // Wait for page transition
            } else {
                // Try clicking the first enabled button
                for (WebElement btn : buttons) {
                    if (btn.isDisplayed() && btn.isEnabled()) {
                        btn.click();
                        Thread.sleep(2000);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error clicking continue button: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Verify if passenger details page is displayed
     */
    public boolean isPassengerDetailsPageDisplayed() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(passengerDetailsSection));
            return true;
        } catch (Exception e) {
            System.err.println("Passenger details page not found: " + e.getMessage());
            // Check if URL changed
            String currentUrl = driver.getCurrentUrl();
            System.out.println("Current URL: " + currentUrl);
            return currentUrl.contains("passenger") || currentUrl.contains("נוסעים");
        }
    }
    
}

