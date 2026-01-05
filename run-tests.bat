@echo off
REM Batch script to run Harel Travel Insurance tests on Windows

echo ========================================
echo Harel Travel Insurance - Test Automation
echo ========================================
echo.

REM Check if Java is installed
echo Checking Java installation...
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java JDK 11 or higher
    pause
    exit /b 1
)
echo Java is installed successfully
echo.

REM Check if Maven is installed
echo Checking Maven installation...
call mvn -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven 3.6 or higher
    pause
    exit /b 1
)
echo Maven is installed successfully
echo.

REM Clean and compile
echo Step 1: Cleaning and compiling project...
call mvn clean compile
if errorlevel 1 (
    echo ERROR: Build failed
    pause
    exit /b 1
)
echo.

REM Run tests
echo Step 2: Running automated tests...
echo This may take a few minutes...
echo.
call mvn test
echo.

REM Check results
if errorlevel 1 (
    echo.
    echo ========================================
    echo TESTS FAILED - Please check the logs
    echo ========================================
) else (
    echo.
    echo ========================================
    echo TESTS COMPLETED SUCCESSFULLY!
    echo ========================================
)
echo.

REM Open test report
echo Opening test report...
if exist "test-output\index.html" (
    start test-output\index.html
) else (
    echo Test report not found at test-output\index.html
)

echo.
echo Press any key to exit...
pause >nul

