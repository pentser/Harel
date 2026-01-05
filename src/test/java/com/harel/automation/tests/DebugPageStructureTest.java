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
 * Debug test to inspect the actual website structure
 * This helps us find the correct locators
 */
public class DebugPageStructureTest {
    
    @Test
    public void inspectPageStructure() throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        
        WebDriver driver = new ChromeDriver(options);
        
        try {
            // Step 1: Open the website
            System.out.println("\n=== Opening Website ===");
            driver.get("https://digital.harel-group.co.il/travel-policy");
            Thread.sleep(3000); // Wait for page to load
            
            System.out.println("Page Title: " + driver.getTitle());
            System.out.println("Current URL: " + driver.getCurrentUrl());
            
            // Step 2: Find "First time purchase" button
            System.out.println("\n=== Looking for 'First Time Purchase' Button ===");
            List<WebElement> buttons = driver.findElements(By.tagName("button"));
            System.out.println("Total buttons found: " + buttons.size());
            
            for (int i = 0; i < buttons.size() && i < 20; i++) {
                WebElement btn = buttons.get(i);
                try {
                    String text = btn.getText();
                    String className = btn.getAttribute("class");
                    String id = btn.getAttribute("id");
                    if (!text.isEmpty() || className != null) {
                        System.out.println("Button " + (i+1) + ": Text='" + text + "', Class='" + className + "', ID='" + id + "'");
                    }
                } catch (Exception e) {
                    // Skip if stale or hidden
                }
            }
            
            // Try to find and click first time purchase button
            System.out.println("\n=== Attempting to click 'First Time Purchase' ===");
            
            // Try multiple strategies
            WebElement firstTimeButton = null;
            
            // Strategy 1: By text
            try {
                firstTimeButton = driver.findElement(By.xpath("//*[contains(text(), 'לרכישה') or contains(text(), 'First')]"));
                System.out.println("✓ Found button using text search");
            } catch (Exception e) {
                System.out.println("✗ Text search failed");
            }
            
            // Strategy 2: By button with specific text
            if (firstTimeButton == null) {
                try {
                    List<WebElement> allButtons = driver.findElements(By.tagName("button"));
                    for (WebElement btn : allButtons) {
                        String btnText = btn.getText().toLowerCase();
                        if (btnText.contains("לרכישה") || btnText.contains("first") || btnText.contains("purchase")) {
                            firstTimeButton = btn;
                            System.out.println("✓ Found button by iterating: " + btn.getText());
                            break;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("✗ Iteration search failed");
                }
            }
            
            if (firstTimeButton != null) {
                firstTimeButton.click();
                System.out.println("✓ Clicked first time purchase button");
                Thread.sleep(2000);
                
                // Now inspect page after clicking
                System.out.println("\n=== After Clicking - Looking for Continent Selection ===");
                System.out.println("Current URL: " + driver.getCurrentUrl());
                
                // Find all clickable elements
                List<WebElement> allButtons = driver.findElements(By.tagName("button"));
                System.out.println("Buttons found: " + allButtons.size());
                
                for (int i = 0; i < allButtons.size() && i < 30; i++) {
                    WebElement btn = allButtons.get(i);
                    try {
                        if (btn.isDisplayed()) {
                            String text = btn.getText();
                            String className = btn.getAttribute("class");
                            String dataValue = btn.getAttribute("data-value");
                            String ariaLabel = btn.getAttribute("aria-label");
                            
                            System.out.println("Button " + (i+1) + ":");
                            System.out.println("  Text: " + text);
                            System.out.println("  Class: " + className);
                            System.out.println("  Data-value: " + dataValue);
                            System.out.println("  Aria-label: " + ariaLabel);
                            System.out.println();
                        }
                    } catch (Exception e) {
                        // Skip
                    }
                }
                
                // Also check for divs that might be clickable
                System.out.println("\n=== Looking for ALL Divs with Classes ===");
                List<WebElement> divs = driver.findElements(By.tagName("div"));
                System.out.println("Total divs found: " + divs.size());
                
                int displayedDivs = 0;
                for (WebElement div : divs) {
                    try {
                        if (div.isDisplayed()) {
                            String className = div.getAttribute("class");
                            String text = div.getText();
                            
                            // Look for MUI components, cards, or items
                            if (className != null && !className.isEmpty() && 
                                (className.contains("Mui") || className.contains("card") || 
                                 className.contains("item") || className.contains("destination") || 
                                 className.contains("continent"))) {
                                displayedDivs++;
                                if (displayedDivs <= 30) {
                                    System.out.println("Div " + displayedDivs + ": Class='" + className + "'");
                                    if (text != null && !text.isEmpty() && text.length() < 100) {
                                        System.out.println("  Text: " + text);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        // Skip
                    }
                }
                
                // Look for list items
                System.out.println("\n=== Looking for List Items ===");
                List<WebElement> listItems = driver.findElements(By.tagName("li"));
                System.out.println("Total list items: " + listItems.size());
                for (int i = 0; i < listItems.size() && i < 20; i++) {
                    try {
                        WebElement li = listItems.get(i);
                        if (li.isDisplayed()) {
                            System.out.println("LI " + (i+1) + ": Class='" + li.getAttribute("class") + "', Text='" + 
                                li.getText().substring(0, Math.min(50, li.getText().length())) + "'");
                        }
                    } catch (Exception e) {
                        // Skip
                    }
                }
                
                // Look for anchor tags
                System.out.println("\n=== Looking for Links/Anchors ===");
                List<WebElement> links = driver.findElements(By.tagName("a"));
                System.out.println("Total links: " + links.size());
                for (int i = 0; i < links.size() && i < 20; i++) {
                    try {
                        WebElement link = links.get(i);
                        if (link.isDisplayed() && !link.getText().isEmpty()) {
                            System.out.println("Link " + (i+1) + ": Class='" + link.getAttribute("class") + 
                                "', Href='" + link.getAttribute("href") + "', Text='" + 
                                link.getText().substring(0, Math.min(50, link.getText().length())) + "'");
                        }
                    } catch (Exception e) {
                        // Skip
                    }
                }
                
                // Print page source (first 2000 characters)
                System.out.println("\n=== Page Source Sample ===");
                String pageSource = driver.getPageSource();
                System.out.println(pageSource.substring(0, Math.min(2000, pageSource.length())));
                
            } else {
                System.out.println("✗ Could not find first time purchase button");
                System.out.println("\n=== Page Source Sample ===");
                String pageSource = driver.getPageSource();
                System.out.println(pageSource.substring(0, Math.min(2000, pageSource.length())));
            }
            
            Thread.sleep(5000); // Keep browser open to inspect manually
            
        } finally {
            driver.quit();
        }
    }
}

