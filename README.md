# Harel Travel Insurance - Automation Test

This project contains automated tests for the Harel Travel Insurance purchase system using Java, Selenium WebDriver, and TestNG.

## Test Description

The automation test validates the complete flow of travel insurance purchase:

1. ✅ Open the Harel travel insurance website
2. ✅ Click on "First time purchase" button
3. ✅ Select a continent/destination
4. ✅ Navigate to travel dates selection
5. ✅ Select departure date (7 days from current date)
6. ✅ Select return date (30 days from departure date)
7. ✅ Verify total days calculation is correct
8. ✅ Continue to passenger details page
9. ✅ Verify passenger details page opens successfully

## Prerequisites

Before running the tests, ensure you have the following installed:

- **Java JDK 11 or higher** - [Download Java](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.6 or higher** - [Download Maven](https://maven.apache.org/download.cgi)
- **Google Chrome browser** (latest version)
- **Git** (for version control)

## Project Structure

```
travel-insurance-test/
├── src/
│   └── test/
│       └── java/
│           └── com/
│               └── harel/
│                   └── automation/
│                       ├── tests/
│                       │   └── TravelInsuranceTest.java
│                       ├── pages/
│                       │   ├── TravelPolicyPage.java
│                       │   └── TravelDatesPage.java
│                       └── utils/
│                           └── DateUtils.java
├── pom.xml
├── testng.xml
└── README.md
```

## Technologies Used

- **Java 11** - Programming language
- **Selenium WebDriver 4.16.1** - Browser automation
- **TestNG 7.8.0** - Testing framework
- **WebDriverManager 5.6.3** - Automatic driver management
- **Maven** - Build and dependency management

## Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd Harel
```

### 2. Verify Java Installation

```bash
java -version
```

You should see Java version 11 or higher.

### 3. Verify Maven Installation

```bash
mvn -version
```

### 4. Install Dependencies

```bash
mvn clean install
```

This command will:
- Download all required dependencies
- Compile the project
- Run the tests

## Running the Tests

### Method 1: Run with Maven

```bash
mvn clean test
```

### Method 2: Run with TestNG XML

```bash
mvn test -DsuiteXmlFile=testng.xml
```

### Method 3: Run from IDE (IntelliJ IDEA / Eclipse)

1. Right-click on `testng.xml`
2. Select "Run" or "Debug"

OR

1. Open `TravelInsuranceTest.java`
2. Right-click on the test class or test method
3. Select "Run" or "Debug"

## Test Reports

After running the tests, reports will be generated in:

- **TestNG HTML Report**: `test-output/index.html`
- **Surefire Reports**: `target/surefire-reports/`
- **Console Output**: Detailed step-by-step execution logs

To view the TestNG report:

```bash
# On Windows
start test-output/index.html

# On Mac
open test-output/index.html

# On Linux
xdg-open test-output/index.html
```

## Test Configuration

The test is configured with the following parameters:

- **Website URL**: `https://digital.harel-group.co.il/travel-policy`
- **Departure Date**: 7 days from current date (dynamic)
- **Return Date**: 30 days from departure date (dynamic)
- **Expected Total Days**: 31 days
- **Browser**: Chrome (maximized window)
- **Wait Timeout**: 20 seconds

## Key Features

### 🎯 Dynamic Date Selection
- Automatically calculates dates based on current date
- Uses Java LocalDate for precise date handling
- Validates correct date range selection

### 🔄 Page Object Model (POM)
- Clean separation of test logic and page interactions
- Reusable page components
- Easy maintenance and scalability

### ⏱️ Smart Waits
- Explicit waits for element availability
- Handles dynamic content loading
- Fallback mechanisms for robust execution

### 📊 Detailed Logging
- Step-by-step execution logs
- Clear pass/fail messages
- Easy debugging with detailed error messages

### 🛡️ Error Handling
- Alternative click methods (JavaScript executor)
- Multiple locator strategies
- Graceful fallback mechanisms

## Troubleshooting

### Issue: ChromeDriver not found
**Solution**: The project uses WebDriverManager which automatically downloads the correct driver. If issues persist, manually download ChromeDriver from [here](https://chromedriver.chromium.org/).

### Issue: Element not found
**Solution**: The website may have changed. Update the locators in the Page Object classes (`TravelPolicyPage.java` and `TravelDatesPage.java`).

### Issue: Test timeout
**Solution**: Increase the wait timeout in the test class:
```java
wait = new WebDriverWait(driver, Duration.ofSeconds(30));
```

### Issue: Maven build fails
**Solution**: Clean the project and rebuild:
```bash
mvn clean install -U
```

## Continuous Integration (CI/CD)

This project is ready for CI/CD integration with:
- Jenkins
- GitHub Actions
- GitLab CI
- Azure DevOps

Sample CI configuration files can be added upon request.

## Cloud Deployment (Bonus Task)

### Running on Cloud Platforms

The test can be executed on various cloud platforms:

#### Option 1: GitHub Actions (Recommended)
1. Push code to GitHub repository
2. Create `.github/workflows/test.yml` for automated testing
3. Tests run automatically on every push/PR

#### Option 2: Selenium Grid on Cloud
- **Sauce Labs**: [https://saucelabs.com/](https://saucelabs.com/)
- **BrowserStack**: [https://www.browserstack.com/](https://www.browserstack.com/)
- **LambdaTest**: [https://www.lambdatest.com/](https://www.lambdatest.com/)

#### Option 3: Docker Container
```bash
docker build -t harel-travel-test .
docker run harel-travel-test
```

## Contributing

1. Create a feature branch: `git checkout -b feature/new-test`
2. Make your changes
3. Commit: `git commit -am 'Add new test case'`
4. Push: `git push origin feature/new-test`
5. Create a Pull Request

## Author

**Position**: QA Automation Engineer Candidate
**Date**: January 2026
**Company**: Harel Insurance

## License

This project is created for testing purposes as part of the Harel QA Automation Engineer interview process.

## Contact

For questions or issues, please contact the development team.

---

**Note**: This test is designed to run against the live Harel website. Ensure you have proper authorization before running automated tests against production systems.

