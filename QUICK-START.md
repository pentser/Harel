# 🚀 Quick Start Guide - הדרכת התחלה מהירה

## עברית (Hebrew)

### דרישות מקדימות
1. Java JDK 11 ומעלה
2. Maven 3.6 ומעלה
3. דפדפן Chrome (גרסה עדכנית)

### הרצה מהירה - Windows
```batch
run-tests.bat
```

פשוט הפעל את הקובץ והכל יעבוד אוטומטית!

### הרצה עם Maven
```bash
mvn clean test
```

### מיקום דוחות
- דוח TestNG: `test-output/index.html`
- דוחות Maven: `target/surefire-reports/`

### מה הבדיקה עושה?
1. פותחת את אתר הביטוח נסיעות של הראל
2. לוחצת על "לרכישה בפעם הראשונה"
3. בוחרת יבשת
4. ממשיכה לבחירת תאריכים
5. בוחרת תאריך יציאה (7 ימים מהיום)
6. בוחרת תאריך חזרה (30 ימים מתאריך היציאה)
7. מוודאת שסך הימים נכון
8. ממשיכה לפרטי נוסעים
9. מוודאת שהדף נפתח

---

## English

### Prerequisites
1. Java JDK 11 or higher
2. Maven 3.6 or higher
3. Chrome browser (latest version)

### Quick Run - Windows
```batch
run-tests.bat
```

Just run the file and everything works automatically!

### Run with Maven
```bash
mvn clean test
```

### Reports Location
- TestNG Report: `test-output/index.html`
- Maven Reports: `target/surefire-reports/`

### What the Test Does
1. Opens Harel travel insurance website
2. Clicks "First time purchase"
3. Selects a continent
4. Continues to date selection
5. Selects departure date (7 days from today)
6. Selects return date (30 days from departure)
7. Verifies total days is correct
8. Continues to passenger details
9. Verifies page opens

---

## 📁 Project Files

- `README.md` - Full documentation (English)
- `PROJECT-SUMMARY.md` - Complete project summary
- `CLOUD-DEPLOYMENT-GUIDE.md` - Cloud deployment instructions
- `pom.xml` - Maven configuration
- `testng.xml` - TestNG configuration
- `run-tests.bat` - Windows runner
- `run-tests.sh` - Linux/Mac runner
- `.github/workflows/test-automation.yml` - GitHub Actions CI/CD
- `Dockerfile` - Docker configuration
- `docker-compose.yml` - Docker Compose configuration

---

## ✅ All Requirements Completed

✓ Java + Selenium + TestNG  
✓ Chrome browser  
✓ All 9 test steps  
✓ Dynamic date selection  
✓ Verification of total days  
✓ Git source control  
✓ BONUS: Cloud deployment (GitHub Actions + Docker)

---

## 🎯 Success!

Everything is ready to run. Just execute:

**Windows**: Double-click `run-tests.bat`  
**Command line**: `mvn clean test`  
**Docker**: `docker-compose up --build`

Good luck! בהצלחה!

