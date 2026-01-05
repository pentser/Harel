package com.harel.automation.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Debug test to inspect the dates page structure
 */
public class DebugDatesPageTest {
    
    @Test
    public void inspectDatesPage() throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        
        WebDriver driver = new ChromeDriver(options);
        
        try {
            // Navigate through the flow to get to dates page
            System.out.println("\n=== Navigating to Dates Page ===");
            driver.get("https://digital.harel-group.co.il/travel-policy");
            Thread.sleep(2000);
            
            // Click first time purchase
            WebElement firstTimeBtn = driver.findElement(By.xpath("//*[contains(text(), 'לרכישה')]"));
            firstTimeBtn.click();
            Thread.sleep(2000);
            
            // Click first continent - use more specific selector
            List<WebElement> continents = driver.findElements(By.xpath("//div[contains(@class, 'MuiGrid-item') and contains(@class, 'MuiGrid-grid-xs-6')]"));
            System.out.println("Found " + continents.size() + " continents");
            for (WebElement continent : continents) {
                if (continent.isDisplayed() && !continent.getText().isEmpty() && continent.getText().length() < 50) {
                    System.out.println("Clicking continent: " + continent.getText());
                    continent.click();
                    Thread.sleep(1500); // Wait after clicking continent
                    break;
                }
            }
            
            // Check buttons before clicking continue
            System.out.println("\n=== Looking for Continue Button ===");
            List<WebElement> buttons = driver.findElements(By.tagName("button"));
            System.out.println("Found " + buttons.size() + " buttons");
            
            WebElement continueBtn = null;
            for (WebElement btn : buttons) {
                try {
                    if (btn.isDisplayed()) {
                        String btnText = btn.getText();
                        System.out.println("Button: " + btnText);
                        if (btnText.contains("הלאה") || btnText.length() > 10) {
                            continueBtn = btn;
                            System.out.println("Found continue button!");
                            break;
                        }
                    }
                } catch (Exception e) {
                    // Skip
                }
            }
            
            if (continueBtn != null) {
                String urlBefore = driver.getCurrentUrl();
                System.out.println("URL before clicking: " + urlBefore);
                
                continueBtn.click();
                System.out.println("Clicked continue button");
                
                // Wait for URL to change
                System.out.println("Waiting for URL to change...");
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(1000);
                    String currentUrl = driver.getCurrentUrl();
                    System.out.println("After " + (i+1) + " second(s): " + currentUrl);
                    if (!currentUrl.equals(urlBefore)) {
                        System.out.println("URL changed!");
                        break;
                    }
                }
            } else {
                System.out.println("Continue button NOT found! Printing all page elements...");
                System.out.println("\nAll buttons:");
                for (WebElement btn : buttons) {
                    try {
                        System.out.println("  - " + btn.getText() + " | Class: " + btn.getAttribute("class"));
                    } catch (Exception e) {
                        // Skip
                    }
                }
            }
            
            System.out.println("\n=== Arrived at Dates Page ===");
            System.out.println("Current URL: " + driver.getCurrentUrl());
            Thread.sleep(2000); // Extra wait for page elements to load
            
            // Find all input elements
            System.out.println("\n=== Looking for Input Fields ===");
            List<WebElement> inputs = driver.findElements(By.tagName("input"));
            System.out.println("Total inputs found: " + inputs.size());
            
            for (int i = 0; i < inputs.size(); i++) {
                WebElement input = inputs.get(i);
                try {
                    String type = input.getAttribute("type");
                    String placeholder = input.getAttribute("placeholder");
                    String name = input.getAttribute("name");
                    String id = input.getAttribute("id");
                    String className = input.getAttribute("class");
                    String value = input.getAttribute("value");
                    
                    System.out.println("Input " + (i+1) + ":");
                    System.out.println("  Type: " + type);
                    System.out.println("  Placeholder: " + placeholder);
                    System.out.println("  Name: " + name);
                    System.out.println("  ID: " + id);
                    System.out.println("  Class: " + className);
                    System.out.println("  Value: " + value);
                    System.out.println("  Displayed: " + input.isDisplayed());
                    System.out.println();
                } catch (Exception e) {
                    // Skip
                }
            }
            
            // Look for date picker elements
            System.out.println("\n=== Looking for Date Picker Elements ===");
            List<WebElement> allElements = driver.findElements(By.xpath("//*[contains(@class, 'date') or contains(@class, 'Date') or contains(@class, 'picker') or contains(@class, 'Picker')]"));
            System.out.println("Found " + allElements.size() + " date-related elements");
            
            for (int i = 0; i < Math.min(allElements.size(), 20); i++) {
                try {
                    WebElement elem = allElements.get(i);
                    if (elem.isDisplayed()) {
                        System.out.println("Element " + (i+1) + ": Tag=" + elem.getTagName() + 
                            ", Class=" + elem.getAttribute("class") + 
                            ", Text=" + elem.getText().substring(0, Math.min(50, elem.getText().length())));
                    }
                } catch (Exception e) {
                    // Skip
                }
            }
            
            // Look for MUI components
            System.out.println("\n=== Looking for MUI TextField Components ===");
            List<WebElement> muiElements = driver.findElements(By.xpath("//*[contains(@class, 'MuiTextField') or contains(@class, 'MuiInput')]"));
            System.out.println("Found " + muiElements.size() + " MUI components");
            
            for (int i = 0; i < muiElements.size(); i++) {
                try {
                    WebElement elem = muiElements.get(i);
                    if (elem.isDisplayed()) {
                        System.out.println("MUI " + (i+1) + ": Tag=" + elem.getTagName() + 
                            ", Class=" + elem.getAttribute("class"));
                        
                        // Find input inside this MUI component
                        try {
                            WebElement innerInput = elem.findElement(By.tagName("input"));
                            System.out.println("  Inner input: Placeholder=" + innerInput.getAttribute("placeholder") + 
                                ", Name=" + innerInput.getAttribute("name"));
                        } catch (Exception e2) {
                            // No input inside
                        }
                    }
                } catch (Exception e) {
                    // Skip
                }
            }
            
            Thread.sleep(5000); // Keep browser open
            
        } finally {
            driver.quit();
        }
    }
}

