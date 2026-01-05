# ☁️ Cloud Deployment Guide - Bonus Task

This guide explains how to deploy and run the Harel Travel Insurance automation tests on various cloud platforms.

---

## 📋 Table of Contents

1. [GitHub Actions (Recommended)](#1-github-actions-recommended)
2. [Docker Deployment](#2-docker-deployment)
3. [Heroku Cloud](#3-heroku-cloud)
4. [AWS Cloud](#4-aws-cloud)
5. [Azure Cloud](#5-azure-cloud)
6. [Google Cloud Platform](#6-google-cloud-platform)
7. [Selenium Grid Cloud Services](#7-selenium-grid-cloud-services)

---

## 1. GitHub Actions (Recommended) ✅

### Why GitHub Actions?
- **Free** for public repositories
- **Integrated** with version control
- **Automatic** test execution on every push
- **Easy setup** - already configured!

### Setup Instructions

#### Step 1: Create GitHub Repository
```bash
# If not already on GitHub, create a new repository on github.com
# Then push your code:

git remote add origin https://github.com/YOUR_USERNAME/harel-travel-insurance-test.git
git branch -M main
git push -u origin main
```

#### Step 2: Enable GitHub Actions
- The workflow file is already created: `.github/workflows/test-automation.yml`
- GitHub Actions will automatically detect and run it
- No additional setup required!

#### Step 3: View Test Results
1. Go to your GitHub repository
2. Click on the "Actions" tab
3. Select the latest workflow run
4. View test results and download artifacts

### Features
✅ Automatic execution on push/PR  
✅ Test reports saved as artifacts  
✅ Email notifications on failure  
✅ Run on Ubuntu Linux with Chrome  
✅ Maven caching for faster builds

---

## 2. Docker Deployment

### Local Docker Run

```bash
# Build the Docker image
docker build -t harel-travel-insurance-test .

# Run the container
docker run --rm harel-travel-insurance-test

# Run with volume mounts to save reports
docker run --rm \
  -v $(pwd)/test-output:/app/test-output \
  -v $(pwd)/target:/app/target \
  harel-travel-insurance-test
```

### Docker Compose

```bash
# Run with docker-compose
docker-compose up --build

# Run in detached mode
docker-compose up -d

# View logs
docker-compose logs -f

# Stop and clean up
docker-compose down
```

### Deploy to Docker Hub

```bash
# Login to Docker Hub
docker login

# Tag the image
docker tag harel-travel-insurance-test YOUR_USERNAME/harel-travel-test:latest

# Push to Docker Hub
docker push YOUR_USERNAME/harel-travel-test:latest

# Pull and run from anywhere
docker pull YOUR_USERNAME/harel-travel-test:latest
docker run YOUR_USERNAME/harel-travel-test:latest
```

---

## 3. Heroku Cloud

### Prerequisites
- Heroku account (free tier available)
- Heroku CLI installed

### Deployment Steps

#### Step 1: Create Heroku App
```bash
# Login to Heroku
heroku login

# Create new app
heroku create harel-travel-insurance-test

# Set buildpacks
heroku buildpacks:add heroku/java
```

#### Step 2: Create Heroku Configuration

Create `Procfile`:
```
web: mvn clean test
```

Create `system.properties`:
```
java.runtime.version=11
```

#### Step 3: Deploy
```bash
git add .
git commit -m "Configure for Heroku deployment"
git push heroku main
```

#### Step 4: Run Tests
```bash
# Run as a one-off dyno
heroku run mvn test

# View logs
heroku logs --tail
```

### Limitations
- Heroku doesn't support GUI browsers by default
- Need to run in headless mode
- Consider using Heroku + Selenium Grid cloud service

---

## 4. AWS Cloud

### Option A: AWS CodeBuild

#### Step 1: Create `buildspec.yml`
```yaml
version: 0.2

phases:
  install:
    runtime-versions:
      java: corretto11
    commands:
      - echo Installing Chrome...
      - wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add -
      - echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list
      - apt-get update
      - apt-get install -y google-chrome-stable
  pre_build:
    commands:
      - echo Starting test execution...
  build:
    commands:
      - mvn clean test
  post_build:
    commands:
      - echo Test execution completed

artifacts:
  files:
    - target/surefire-reports/**/*
    - test-output/**/*
```

#### Step 2: Setup in AWS Console
1. Go to AWS CodeBuild
2. Create new project
3. Connect to GitHub repository
4. Select `buildspec.yml`
5. Run build

### Option B: AWS Lambda (Scheduled Tests)
```bash
# Package the application
mvn clean package

# Upload to S3
aws s3 cp target/travel-insurance-test-1.0-SNAPSHOT.jar s3://your-bucket/

# Create Lambda function
# Configure trigger (CloudWatch Events for scheduled runs)
```

---

## 5. Azure Cloud

### Azure DevOps Pipelines

#### Step 1: Create `azure-pipelines.yml`
```yaml
trigger:
  - main

pool:
  vmImage: 'ubuntu-latest'

steps:
  - task: Maven@3
    inputs:
      mavenPomFile: 'pom.xml'
      goals: 'clean test'
      publishJUnitResults: true
      testResultsFiles: '**/surefire-reports/TEST-*.xml'
      javaHomeOption: 'JDKVersion'
      jdkVersionOption: '1.11'
      mavenVersionOption: 'Default'
      
  - task: PublishTestResults@2
    inputs:
      testResultsFormat: 'JUnit'
      testResultsFiles: '**/surefire-reports/TEST-*.xml'
      
  - task: PublishBuildArtifacts@1
    inputs:
      pathToPublish: 'test-output'
      artifactName: 'test-reports'
```

#### Step 2: Setup in Azure DevOps
1. Create new project
2. Go to Pipelines → Create Pipeline
3. Connect to repository
4. Select existing YAML file
5. Run pipeline

---

## 6. Google Cloud Platform

### Cloud Build

#### Step 1: Create `cloudbuild.yaml`
```yaml
steps:
  # Install Chrome
  - name: 'gcr.io/cloud-builders/docker'
    args: ['pull', 'maven:3.8.6-openjdk-11']
    
  # Run tests
  - name: 'maven:3.8.6-openjdk-11'
    entrypoint: 'mvn'
    args: ['clean', 'test']
    
artifacts:
  objects:
    location: 'gs://your-bucket/test-results'
    paths: ['target/surefire-reports/**', 'test-output/**']
```

#### Step 2: Deploy
```bash
# Submit build to Cloud Build
gcloud builds submit --config=cloudbuild.yaml
```

### Cloud Run (Scheduled)
```bash
# Build and push Docker image
gcloud builds submit --tag gcr.io/PROJECT_ID/harel-travel-test

# Deploy to Cloud Run
gcloud run deploy harel-travel-test \
  --image gcr.io/PROJECT_ID/harel-travel-test \
  --platform managed \
  --region us-central1
  
# Schedule with Cloud Scheduler
gcloud scheduler jobs create http test-runner \
  --schedule="0 9 * * *" \
  --uri="https://your-cloud-run-url.run.app" \
  --http-method=GET
```

---

## 7. Selenium Grid Cloud Services

### BrowserStack

#### Step 1: Modify WebDriver Setup
```java
// In TravelInsuranceTest.java, update setUp() method
@BeforeMethod
public void setUp() {
    String USERNAME = "YOUR_BROWSERSTACK_USERNAME";
    String AUTOMATE_KEY = "YOUR_BROWSERSTACK_KEY";
    String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";
    
    DesiredCapabilities caps = new DesiredCapabilities();
    caps.setCapability("os", "Windows");
    caps.setCapability("os_version", "11");
    caps.setCapability("browser", "Chrome");
    caps.setCapability("browser_version", "latest");
    caps.setCapability("name", "Harel Travel Insurance Test");
    
    driver = new RemoteWebDriver(new URL(URL), caps);
}
```

#### Step 2: Run Tests
```bash
mvn clean test
```

View results on BrowserStack dashboard.

### Sauce Labs

```java
@BeforeMethod
public void setUp() {
    String USERNAME = "YOUR_SAUCE_USERNAME";
    String ACCESS_KEY = "YOUR_SAUCE_KEY";
    String URL = "https://" + USERNAME + ":" + ACCESS_KEY + "@ondemand.us-west-1.saucelabs.com:443/wd/hub";
    
    DesiredCapabilities caps = DesiredCapabilities.chrome();
    caps.setCapability("platform", "Windows 10");
    caps.setCapability("version", "latest");
    caps.setCapability("name", "Harel Travel Insurance Test");
    
    driver = new RemoteWebDriver(new URL(URL), caps);
}
```

### LambdaTest

```java
@BeforeMethod
public void setUp() {
    String USERNAME = "YOUR_LAMBDATEST_USERNAME";
    String ACCESS_KEY = "YOUR_LAMBDATEST_KEY";
    String URL = "https://" + USERNAME + ":" + ACCESS_KEY + "@hub.lambdatest.com/wd/hub";
    
    DesiredCapabilities caps = new DesiredCapabilities();
    caps.setCapability("platform", "Windows 10");
    caps.setCapability("browserName", "Chrome");
    caps.setCapability("version", "latest");
    caps.setCapability("name", "Harel Travel Insurance Test");
    caps.setCapability("build", "Harel Test Build");
    
    driver = new RemoteWebDriver(new URL(URL), caps);
}
```

---

## 🎯 Recommended Approach

For this project, I recommend the following **FREE** cloud deployment strategy:

### 1. **Primary: GitHub Actions** (Already Configured! ✅)
- **Cost**: FREE
- **Setup**: Already done!
- **Benefits**: Automatic CI/CD, integrated with Git
- **Action Required**: Just push to GitHub

### 2. **Secondary: Docker Hub**
- **Cost**: FREE (1 private repo)
- **Setup**: 5 minutes
- **Benefits**: Portable, can run anywhere
- **Action Required**: `docker build` and `docker push`

### 3. **Bonus: Cloud Service Trial**
Choose one for demo:
- **BrowserStack** - 100 free minutes trial
- **LambdaTest** - 100 free minutes trial
- **AWS** - 12 months free tier

---

## 📊 Comparison Table

| Platform | Cost | Setup Time | Complexity | Recommended |
|----------|------|------------|------------|-------------|
| GitHub Actions | FREE | 0 min (done!) | ⭐ Easy | ✅ YES |
| Docker Local | FREE | 5 min | ⭐⭐ Medium | ✅ YES |
| Docker Hub | FREE | 10 min | ⭐⭐ Medium | ✅ YES |
| Heroku | FREE tier | 15 min | ⭐⭐⭐ Hard | ⚠️ Limited |
| AWS | Free tier | 30 min | ⭐⭐⭐⭐ Hard | ⚠️ Complex |
| Azure | Free tier | 20 min | ⭐⭐⭐ Hard | ⚠️ Complex |
| GCP | Free tier | 25 min | ⭐⭐⭐ Hard | ⚠️ Complex |
| BrowserStack | Trial | 10 min | ⭐⭐ Medium | ✅ Demo |

---

## ✅ Quick Start Recommendation

### For Interview/Demo:

1. **Push to GitHub** (30 seconds)
   ```bash
   git remote add origin YOUR_GITHUB_URL
   git push -u origin main
   ```

2. **Show GitHub Actions** (Automatic)
   - Actions tab will show test execution
   - Green checkmark = Tests passed!

3. **Optional: BrowserStack Demo** (10 minutes)
   - Sign up for free trial
   - Update WebDriver configuration
   - Run tests on cloud browsers

---

## 🎓 Summary

✅ **GitHub Actions**: Already configured and ready!  
✅ **Docker**: Dockerfile and docker-compose.yml included  
✅ **Instructions**: Complete guides for all major cloud platforms  
✅ **Bonus**: Selenium Grid cloud services integration examples  

**Recommendation**: Push to GitHub now and let GitHub Actions do the work! 🚀

---

**Need Help?**
- Check GitHub Actions logs for any issues
- Verify all prerequisites are installed
- Ensure website URL is accessible from cloud

**Created**: January 5, 2026  
**For**: Harel Insurance QA Automation Position

