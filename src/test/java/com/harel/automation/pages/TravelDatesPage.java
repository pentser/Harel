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
    
    // Locators
    private By departureDateInput = By.xpath("//input[contains(@placeholder, 'יציאה') or contains(@name, 'departure') or contains(@id, 'departure')]");
    private By returnDateInput = By.xpath("//input[contains(@placeholder, 'חזרה') or contains(@name, 'return') or contains(@id, 'return')]");
    private By datePickerCalendar = By.xpath("//*[contains(@class, 'calendar') or contains(@class, 'datepicker') or contains(@class, 'picker')]");
    private By totalDaysDisplay = By.xpath("//*[contains(text(), 'ימים') or contains(text(), 'days') or contains(text(), 'סה\"כ')]");
    private By continueToPassengerDetailsButton = By.xpath("//*[contains(text(), 'הלאה לפרטי הנוסעים') or contains(text(), 'passenger')]");
    private By passengerDetailsSection = By.xpath("//*[contains(@class, 'passenger') or contains(text(), 'נוסעים') or contains(text(), 'Passenger')]");
    
    public TravelDatesPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }
    
    /**
     * Select departure date using date picker
     */
    public void selectDepartureDate(LocalDate date) {
        try {
            // Click on departure date input to open date picker
            WebElement departureDateField = wait.until(ExpectedConditions.elementToBeClickable(departureDateInput));
            departureDateField.click();
            Thread.sleep(1000);
            
            // Select date from calendar
            selectDateFromCalendar(date);
            
        } catch (Exception e) {
            System.err.println("Error selecting departure date: " + e.getMessage());
            // Try alternative method - direct input
            tryDirectDateInput(departureDateInput, date);
        }
    }
    
    /**
     * Select return date using date picker
     */
    public void selectReturnDate(LocalDate date) {
        try {
            // Click on return date input to open date picker
            WebElement returnDateField = wait.until(ExpectedConditions.elementToBeClickable(returnDateInput));
            returnDateField.click();
            Thread.sleep(1000);
            
            // Select date from calendar
            selectDateFromCalendar(date);
            
        } catch (Exception e) {
            System.err.println("Error selecting return date: " + e.getMessage());
            // Try alternative method - direct input
            tryDirectDateInput(returnDateInput, date);
        }
    }
    
    /**
     * Select a specific date from the calendar date picker
     */
    private void selectDateFromCalendar(LocalDate date) {
        try {
            // Wait for calendar to be visible
            wait.until(ExpectedConditions.presenceOfElementLocated(datePickerCalendar));
            
            // Format the date to find in calendar (try multiple formats)
            String dayOfMonth = String.valueOf(date.getDayOfMonth());
            
            // Find and click the date cell
            By dateCell = By.xpath(String.format(
                "//td[@data-day='%d' and @data-month='%d' and @data-year='%d'] | " +
                "//div[contains(@class, 'day') and text()='%s'] | " +
                "//button[contains(@aria-label, '%s')]",
                date.getDayOfMonth(), date.getMonthValue() - 1, date.getYear(),
                dayOfMonth, date.toString()
            ));
            
            WebElement dateCellElement = wait.until(ExpectedConditions.elementToBeClickable(dateCell));
            dateCellElement.click();
            Thread.sleep(500);
            
        } catch (Exception e) {
            System.err.println("Error selecting date from calendar: " + e.getMessage());
            throw new RuntimeException("Failed to select date from calendar: " + date, e);
        }
    }
    
    /**
     * Alternative method: Try direct input of date
     */
    private void tryDirectDateInput(By inputLocator, LocalDate date) {
        try {
            WebElement dateInput = driver.findElement(inputLocator);
            dateInput.clear();
            
            // Try different date formats
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = date.format(formatter);
            
            dateInput.sendKeys(formattedDate);
            Thread.sleep(500);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to input date: " + date, e);
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
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(continueToPassengerDetailsButton));
            button.click();
            Thread.sleep(2000); // Wait for page transition
        } catch (Exception e) {
            System.err.println("Error clicking continue button: " + e.getMessage());
            tryAlternativeClick(continueToPassengerDetailsButton);
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

