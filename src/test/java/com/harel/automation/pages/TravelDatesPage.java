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
    private By datePickerNextMonthButton = By.xpath("//button[contains(@class, 'MuiPickersCalendarHeader-iconButton')][2]");
    private By datePickerPreviousMonthButton = By.xpath("//button[contains(@class, 'MuiPickersCalendarHeader-iconButton')][1]");
    private By datePickerMonthYear = By.xpath("//div[contains(@class, 'MuiPickersCalendarHeader-transitionContainer')]");
    private By totalDaysDisplay = By.xpath("//*[contains(text(), 'ימים') or contains(text(), 'days') or contains(text(), 'סה\"כ') or contains(text(), 'משך')]");
    private By continueToPassengerDetailsButton = By.xpath("//button[contains(@class, 'MuiButton')]");
    private By passengerDetailsSection = By.xpath("//*[contains(@class, 'passenger') or contains(text(), 'נוסעים') or contains(text(), 'Passenger')]");
    
    public TravelDatesPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }
    
    /**
     * Select departure date using date picker calendar
     */
    public void selectDepartureDate(LocalDate date) {
        try {
            System.out.println("Selecting departure date from date picker: " + date);
            
            // Click on departure date input to open date picker
            WebElement departureDateField = wait.until(ExpectedConditions.elementToBeClickable(departureDateInput));
            departureDateField.click();
            Thread.sleep(1500); // Wait for date picker to open
            
            // Wait for calendar to be visible
            wait.until(ExpectedConditions.presenceOfElementLocated(datePickerCalendar));
            System.out.println("✓ Date picker calendar opened");
            
            // Select the date from calendar
            selectDateFromCalendar(date);
            
            System.out.println("✓ Departure date selected from calendar: " + date);
            
            // Wait for the date to be populated in the field
            Thread.sleep(1000);
            
            // Verify date was entered
            String enteredValue = departureDateField.getAttribute("value");
            System.out.println("  Date field value: " + enteredValue);
            
        } catch (Exception e) {
            System.err.println("Error selecting departure date from picker: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to select departure date from picker: " + date, e);
        }
    }
    
    /**
     * Select return date using date picker calendar
     */
    public void selectReturnDate(LocalDate date) {
        try {
            System.out.println("Selecting return date from date picker: " + date);
            
            // Click on return date input to open date picker
            WebElement returnDateField = wait.until(ExpectedConditions.elementToBeClickable(returnDateInput));
            returnDateField.click();
            Thread.sleep(1500); // Wait for date picker to open
            
            // Wait for calendar to be visible
            wait.until(ExpectedConditions.presenceOfElementLocated(datePickerCalendar));
            System.out.println("✓ Date picker calendar opened");
            
            // Select the date from calendar
            selectDateFromCalendar(date);
            
            System.out.println("✓ Return date selected from calendar: " + date);
            
            // Wait for the date to be populated in the field
            Thread.sleep(1000);
            
            // Verify date was entered
            String enteredValue = returnDateField.getAttribute("value");
            System.out.println("  Date field value: " + enteredValue);
            
            // Extra wait for total days to calculate
            System.out.println("  Waiting for total days calculation...");
            Thread.sleep(1500);
            
        } catch (Exception e) {
            System.err.println("Error selecting return date from picker: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to select return date from picker: " + date, e);
        }
    }
    
    /**
     * Select a specific date from the date picker calendar
     * This method navigates to the correct month/year and clicks on the day
     */
    private void selectDateFromCalendar(LocalDate targetDate) throws InterruptedException {
        try {
            LocalDate currentDate = LocalDate.now();
            
            // Get current month/year displayed in picker
            WebElement monthYearElement = driver.findElement(datePickerMonthYear);
            String displayedMonthYear = monthYearElement.getText();
            System.out.println("Date picker showing: " + displayedMonthYear);
            
            // Navigate to the correct month if needed
            navigateToMonth(targetDate);
            
            // Click on the specific day
            int dayOfMonth = targetDate.getDayOfMonth();
            System.out.println("Looking for day: " + dayOfMonth);
            
            // Find and click the day button
            // Try multiple strategies to find the date button
            By dayButton = By.xpath(
                "//div[contains(@class, 'MuiPickersCalendar')]" +
                "//button[contains(@class, 'MuiPickersDay-day') and not(contains(@class, 'MuiPickersDay-hidden'))]" +
                "//*[text()='" + dayOfMonth + "']"
            );
            
            // Alternative: Try finding by day text in any button
            if (driver.findElements(dayButton).isEmpty()) {
                dayButton = By.xpath(
                    "//div[contains(@class, 'MuiPickersCalendar')]" +
                    "//button[.//p[text()='" + dayOfMonth + "']]"
                );
            }
            
            // Another alternative: Simple button with text
            if (driver.findElements(dayButton).isEmpty()) {
                dayButton = By.xpath(
                    "//div[contains(@class, 'MuiPickersCalendar')]" +
                    "//button[contains(., '" + dayOfMonth + "') and not(contains(@class, 'Mui-disabled'))]"
                );
            }
            
            List<WebElement> dayButtons = driver.findElements(dayButton);
            System.out.println("Found " + dayButtons.size() + " potential day buttons");
            
            if (dayButtons.isEmpty()) {
                throw new RuntimeException("Could not find day " + dayOfMonth + " in calendar");
            }
            
            // Click the first matching enabled day
            for (WebElement btn : dayButtons) {
                try {
                    if (btn.isDisplayed() && btn.isEnabled()) {
                        System.out.println("Clicking on day: " + dayOfMonth);
                        btn.click();
                        Thread.sleep(500);
                        return;
                    }
                } catch (Exception e) {
                    // Try next button
                }
            }
            
            // If no button clicked yet, try JavaScript click
            WebElement dayBtn = dayButtons.get(0);
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", dayBtn);
            System.out.println("Clicked day using JavaScript: " + dayOfMonth);
            Thread.sleep(500);
            
        } catch (Exception e) {
            System.err.println("Error in selectDateFromCalendar: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Navigate to the correct month in the date picker
     */
    private void navigateToMonth(LocalDate targetDate) throws InterruptedException {
        LocalDate now = LocalDate.now();
        int monthDiff = (targetDate.getYear() - now.getYear()) * 12 + 
                        (targetDate.getMonthValue() - now.getMonthValue());
        
        System.out.println("Need to navigate " + monthDiff + " months");
        
        if (monthDiff > 0) {
            // Navigate forward
            for (int i = 0; i < monthDiff; i++) {
                try {
                    // Try multiple methods to find and click next month button
                    List<WebElement> navigationButtons = driver.findElements(
                        By.xpath("//button[contains(@class, 'MuiPickersCalendarHeader-iconButton')]"));
                    
                    if (navigationButtons.size() >= 2) {
                        // Second button is "next month"
                        WebElement nextButton = navigationButtons.get(1);
                        
                        // Try JavaScript click first (more reliable)
                        try {
                            ((org.openqa.selenium.JavascriptExecutor) driver)
                                .executeScript("arguments[0].click();", nextButton);
                            System.out.println("✓ Navigated to next month using JS (" + (i + 1) + "/" + monthDiff + ")");
                        } catch (Exception js_ex) {
                            // Fallback to regular click
                            wait.until(ExpectedConditions.elementToBeClickable(nextButton));
                            nextButton.click();
                            System.out.println("✓ Navigated to next month (" + (i + 1) + "/" + monthDiff + ")");
                        }
                        Thread.sleep(500);
                    } else {
                        System.err.println("Navigation buttons not found");
                        break;
                    }
                } catch (Exception e) {
                    System.err.println("Could not navigate to next month: " + e.getMessage());
                    // Try to continue anyway
                }
            }
        } else if (monthDiff < 0) {
            // Navigate backward
            for (int i = 0; i < Math.abs(monthDiff); i++) {
                try {
                    List<WebElement> navigationButtons = driver.findElements(
                        By.xpath("//button[contains(@class, 'MuiPickersCalendarHeader-iconButton')]"));
                    
                    if (navigationButtons.size() >= 1) {
                        // First button is "previous month"
                        WebElement prevButton = navigationButtons.get(0);
                        
                        // Try JavaScript click first
                        try {
                            ((org.openqa.selenium.JavascriptExecutor) driver)
                                .executeScript("arguments[0].click();", prevButton);
                            System.out.println("✓ Navigated to previous month using JS (" + (i + 1) + "/" + Math.abs(monthDiff) + ")");
                        } catch (Exception js_ex) {
                            wait.until(ExpectedConditions.elementToBeClickable(prevButton));
                            prevButton.click();
                            System.out.println("✓ Navigated to previous month (" + (i + 1) + "/" + Math.abs(monthDiff) + ")");
                        }
                        Thread.sleep(500);
                    }
                } catch (Exception e) {
                    System.err.println("Could not navigate to previous month: " + e.getMessage());
                }
            }
        } else {
            System.out.println("Already on correct month - no navigation needed");
        }
        
        Thread.sleep(500); // Wait for calendar to update
    }
    
    
    /**
     * Get the displayed total days text
     * Waits for the element to appear after dates are selected
     */
    public String getTotalDaysDisplayed() {
        try {
            // Wait a bit for the calculation to happen
            Thread.sleep(1000);
            
            // Try multiple strategies to find total days element
            
            // Strategy 1: Look for element with "ימים" text
            try {
                List<WebElement> elements = driver.findElements(
                    By.xpath("//*[contains(text(), 'ימים') or contains(text(), 'יום') or contains(text(), 'days')]"));
                for (WebElement element : elements) {
                    String text = element.getText();
                    if (text != null && !text.trim().isEmpty() && text.matches(".*\\d+.*")) {
                        System.out.println("✓ Found total days element: " + text);
                        return text;
                    }
                }
            } catch (Exception e1) {
                System.err.println("Strategy 1 failed: " + e1.getMessage());
            }
            
            // Strategy 2: Look for price summary section which might contain days
            try {
                List<WebElement> summaryElements = driver.findElements(
                    By.xpath("//div[contains(@class, 'summary') or contains(@class, 'price') or contains(@class, 'total')]"));
                for (WebElement element : summaryElements) {
                    String text = element.getText();
                    if (text != null && text.contains("ימים")) {
                        System.out.println("✓ Found in summary section: " + text);
                        return text;
                    }
                }
            } catch (Exception e2) {
                System.err.println("Strategy 2 failed: " + e2.getMessage());
            }
            
            // Strategy 3: Look in entire page for pattern like "31 ימים" or "משך: 31"
            try {
                String pageSource = driver.getPageSource();
                if (pageSource.contains("ימים") || pageSource.contains("יום")) {
                    // Try to extract from visible text
                    List<WebElement> allElements = driver.findElements(By.xpath("//*[contains(., 'ימים')]"));
                    for (WebElement elem : allElements) {
                        try {
                            if (elem.isDisplayed()) {
                                String text = elem.getText();
                                if (text.matches(".*\\d+.*ימים.*") || text.matches(".*ימים.*\\d+.*")) {
                                    System.out.println("✓ Found days in page: " + text);
                                    return text;
                                }
                            }
                        } catch (Exception ignore) {}
                    }
                }
            } catch (Exception e3) {
                System.err.println("Strategy 3 failed: " + e3.getMessage());
            }
            
            System.err.println("⚠ Total days element not found after trying all strategies");
            return null;
            
        } catch (Exception e) {
            System.err.println("Error getting total days: " + e.getMessage());
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

