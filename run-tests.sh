#!/bin/bash

# Bash script to run Harel Travel Insurance tests on Linux/Mac

echo "========================================"
echo "Harel Travel Insurance - Test Automation"
echo "========================================"
echo ""

# Check if Java is installed
echo "Checking Java installation..."
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH"
    echo "Please install Java JDK 11 or higher"
    exit 1
fi
echo "Java is installed successfully"
echo ""

# Check if Maven is installed
echo "Checking Maven installation..."
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven is not installed or not in PATH"
    echo "Please install Maven 3.6 or higher"
    exit 1
fi
echo "Maven is installed successfully"
echo ""

# Clean and compile
echo "Step 1: Cleaning and compiling project..."
mvn clean compile
if [ $? -ne 0 ]; then
    echo "ERROR: Build failed"
    exit 1
fi
echo ""

# Run tests
echo "Step 2: Running automated tests..."
echo "This may take a few minutes..."
echo ""
mvn test
TEST_RESULT=$?
echo ""

# Check results
if [ $TEST_RESULT -ne 0 ]; then
    echo ""
    echo "========================================"
    echo "TESTS FAILED - Please check the logs"
    echo "========================================"
else
    echo ""
    echo "========================================"
    echo "TESTS COMPLETED SUCCESSFULLY!"
    echo "========================================"
fi
echo ""

# Open test report
echo "Opening test report..."
if [ -f "test-output/index.html" ]; then
    if [[ "$OSTYPE" == "darwin"* ]]; then
        open test-output/index.html
    elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
        xdg-open test-output/index.html
    fi
else
    echo "Test report not found at test-output/index.html"
fi

echo ""
echo "Script completed!"

