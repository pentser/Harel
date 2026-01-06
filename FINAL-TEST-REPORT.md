# ✅ דוח סופי - מבחן אוטומציה הראל

**תאריך**: 6 בינואר 2026  
**סטטוס**: ✅ **כל הדרישות מתקיימות - הטסט עובר בהצלחה**  
**מסגרת בדיקה**: Java + Selenium + TestNG  
**דפדפן**: Chrome 143  
**זמן ריצה**: ~34 שניות

---

## 🎯 סיכום ביצוע הדרישות

### ✅ כל 9 הצעדים עוברים בהצלחה!

| צעד | דרישה | סטטוס | פרטים |
|-----|-------|-------|--------|
| **1** | פתח את כתובת האתר בדפדפן כרום | ✅ **PASS** | `https://digital.harel-group.co.il/travel-policy` |
| **2** | לחץ על כפתור "לרכישה בפעם הראשונה" | ✅ **PASS** | הכפתור נמצא ונלחץ |
| **3** | בחר אחת מהיבשות | ✅ **PASS** | נבחרה אירופה מתוך 8 אפשרויות |
| **4** | לחץ "הלאה לבחירת תאריכי הנסיעה" | ✅ **PASS** | ניווט מוצלח לדף `/wizard/date` |
| **5** | בחר תאריך יציאה (7 ימים מהיום) | ✅ **PASS** | **13/01/2026 - נבחר דרך date picker** 📅 |
| **6** | תאריך חזרה (30 ימים מיציאה) | ✅ **PASS** | **12/02/2026 - נבחר דרך date picker** 📅 |
| **7** | וודא שסה"כ ימים מופיע באופן תקין | ✅ **PASS** | **31 ימים - אומת בהצלחה!** ✓ |
| **8** | לחץ כפתור "הלאה לפרטי הנוסעים" | ✅ **PASS** | הכפתור נלחץ |
| **9** | וודא שהדף נפתח | ✅ **PASS** | דף פרטי הנוסעים נפתח |

---

## 📊 תוצאות הריצה

```
=== Starting Travel Insurance Purchase Test ===

Step 1: Opening website: https://digital.harel-group.co.il/travel-policy
✓ Website opened successfully

Step 2: Clicking on 'First time purchase' button
✓ First time purchase button clicked

Step 3: Selecting a continent
Found 8 continent options
Clicking continent: אירופה
✓ Continent selected successfully

Step 4: Clicking 'Continue to travel dates selection' button
Found 1 buttons
Button text: הלאה לבחירת תאריכי הנסיעה
Clicked continue button
Successfully navigated to dates page
✓ Navigated to travel dates selection page

Step 5: Selecting departure date: 2026-01-13
Selecting departure date from date picker: 2026-01-13
✓ Date picker calendar opened
Date picker showing: ינואר 2026
Need to navigate 0 months
Already on correct month - no navigation needed
Looking for day: 13
Found 2 potential day buttons
Clicking on day: 13
✓ Departure date selected from calendar: 2026-01-13
  Date field value: 13/01/2026
✓ Departure date selected: 13/01/2026

Step 6: Selecting return date: 2026-02-12
Selecting return date from date picker: 2026-02-12
✓ Date picker calendar opened
Date picker showing: ינואר 2026
Need to navigate 1 months
✓ Navigated to next month using JS (1/1)
Looking for day: 12
Found 2 potential day buttons
Clicking on day: 12
✓ Return date selected from calendar: 2026-02-12
  Date field value: 12/02/2026
  Waiting for total days calculation...
✓ Return date selected: 12/02/2026

Step 7: Verifying total days calculation
✓ Found total days element: סה"כ: 31 ימים
Expected total days: 31
Displayed total days: סה"כ: 31 ימים
✓ Total days verified successfully: סה"כ: 31 ימים

Step 8: Clicking 'Continue to passenger details' button
Found 69 buttons
Found continue button: הלאה לפרטי הנוסעים
✓ Continue to passenger details button clicked

Step 9: Verifying passenger details page opened
✓ Passenger details page opened successfully

=== Test Completed Successfully ===

[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## 🔧 יישום טכני

### **1. שימוש ב-Date Picker (לוח תאריכים)**

הדרישה הייתה להשתמש ב-**date picker** לבחירת התאריכים. הקוד מיישם זאת באופן הבא:

```java
// פתיחת ה-date picker
departureDateField.click();
wait.until(ExpectedConditions.presenceOfElementLocated(datePickerCalendar));

// ניווט לחודש הנכון (באמצעות JavaScript click מהימן)
navigateToMonth(targetDate);

// לחיצה על היום הספציפי בלוח
By dayButton = By.xpath("//button[.//p[text()='" + dayOfMonth + "']]");
dayBtn.click();
```

**תכונות מיוחדות:**
- ✅ ניווט אוטומטי בין חודשים (forward/backward)
- ✅ שימוש ב-JavaScript click לאמינות גבוהה
- ✅ טיפול בתאריכים דינמיים (תמיד 7 ו-30 ימים מהתאריך הנוכחי)

### **2. חישוב תאריכים דינמי**

```java
LocalDate departureDate = LocalDate.now().plusDays(7);      // יציאה: 7 ימים מהיום
LocalDate returnDate = departureDate.plusDays(30);          // חזרה: 30 ימים מהיציאה
int expectedTotalDays = 31;                                  // סה"כ: 31 ימים
```

**הריצה הנוכחית:**
- תאריך היום: 06/01/2026
- תאריך יציאה: 13/01/2026 (7 ימים)
- תאריך חזרה: 12/02/2026 (30 ימים)
- סה"כ ימים: **31 ימים** ✓

### **3. אימות סה"כ ימים**

הקוד משתמש ב-3 אסטרטגיות לאיתור האלמנט:

```java
Strategy 1: חיפוש אלמנט עם טקסט "ימים" + מספר
Strategy 2: חיפוש בסעיפי summary/price/total
Strategy 3: סריקת כל הדף עבור הפורמט "31 ימים"

תוצאה: ✓ Found total days element: סה"כ: 31 ימים
```

---

## 📁 מבנה הפרויקט

```
Harel/
├── src/test/java/com/harel/automation/
│   ├── tests/
│   │   └── TravelInsuranceTest.java        # הטסט הראשי
│   ├── pages/
│   │   ├── TravelPolicyPage.java           # POM - דף הבית
│   │   └── TravelDatesPage.java            # POM - דף תאריכים (date picker)
│   └── utils/
│       └── DateUtils.java                   # כלי עזר לתאריכים
├── pom.xml                                  # תלויות Maven
├── testng.xml                               # הגדרות TestNG
└── README.md                                # תיעוד מלא
```

---

## 🎓 טכנולוגיות ושיטות עבודה

### **טכנולוגיות:**
- ✅ **Java 11** - שפת תכנות
- ✅ **Selenium WebDriver 4.16.1** - אוטומציה של דפדפן
- ✅ **TestNG 7.8.0** - מסגרת בדיקה
- ✅ **WebDriverManager** - ניהול אוטומטי של ChromeDriver
- ✅ **Maven** - ניהול תלויות ובנייה

### **שיטות עבודה:**
- ✅ **Page Object Model (POM)** - הפרדה בין לוגיקת בדיקה לאובייקטי דף
- ✅ **Explicit Waits** - המתנות חכמות לאלמנטים
- ✅ **JavaScript Executor** - לחיצות מהימנות
- ✅ **Dynamic Locators** - מציאת אלמנטים גמישה
- ✅ **Error Handling** - טיפול בשגיאות מקיף

---

## 💻 הרצת הטסט

### **שיטה 1: Maven (מומלץ)**
```bash
mvn clean test
```

### **שיטה 2: טסט ספציפי**
```bash
mvn test -Dtest=TravelInsuranceTest
```

### **שיטה 3: TestNG XML**
```bash
mvn test -DsuiteXmlFile=testng.xml
```

---

## 📈 מדדי ביצועים

| מדד | ערך |
|-----|------|
| זמן ריצה כולל | 34.45 שניות |
| מספר בדיקות | 1 |
| הצלחות | 1 (100%) |
| כשלונות | 0 |
| שגיאות | 0 |
| דילוגים | 0 |
| סטטוס | ✅ **BUILD SUCCESS** |

---

## 🔍 נקודות מיוחדות

### **1. Date Picker עם ניווט חודשים**
```java
// הקוד מטפל בניווט בין חודשים באופן אוטומטי:
- אם התאריך בחודש הנוכחי: לחיצה ישירה על היום
- אם התאריך בחודש הבא: ניווט קדימה + לחיצה
- שימוש ב-JavaScript click למניעת בעיות interactability
```

### **2. אימות מרובה לסה"כ ימים**
```java
// 3 שיטות חיפוש מקבילות:
1. חיפוש לפי טקסט "ימים"
2. חיפוש באזורי summary/price
3. סריקת כל הדף

// תוצאה: אמינות גבוהה במציאת האלמנט
```

### **3. לוגים מפורטים**
```
✓ Date picker calendar opened
✓ Need to navigate 1 months
✓ Navigated to next month using JS (1/1)
✓ Looking for day: 12
✓ Clicking on day: 12
✓ Date field value: 12/02/2026
✓ Found total days element: סה"כ: 31 ימים
```

---

## ✅ רשימת בדיקה לביקורת

- [x] הקוד כתוב ב-Java
- [x] שימוש ב-Selenium WebDriver
- [x] שימוש ב-TestNG
- [x] הרצה בדפדפן Chrome
- [x] פתיחת האתר הנכון
- [x] לחיצה על "לרכישה בפעם הראשונה"
- [x] בחירת יבשת
- [x] מעבר לדף תאריכים
- [x] **בחירת תאריך יציאה דרך date picker** ✓
- [x] **בחירת תאריך חזרה דרך date picker** ✓
- [x] תאריך דינמי (7 ימים מהיום)
- [x] תאריך דינמי (30 ימים מהיציאה)
- [x] **אימות סה"כ ימים (31)** ✓
- [x] מעבר לדף פרטי נוסעים
- [x] אימות שהדף נפתח
- [x] הקוד ב-source control (Git)

---

## 🎯 סיכום

הטסט **עובר בהצלחה** ומקיים את **כל הדרישות** של מבחן הבית:

✅ **9/9 צעדים מבוצעים בהצלחה**  
✅ **שימוש ב-date picker כנדרש**  
✅ **אימות סה"כ ימים עובד**  
✅ **תאריכים דינמיים**  
✅ **קוד איכותי עם POM**  
✅ **ב-Git עם commit history**

### **סטטוס סופי: ✅ מוכן להגשה!**

---

**נוצר**: 6 בינואר 2026  
**מועמד/ת**: בודק/ת אוטומציה  
**חברה**: הראל ביטוח ופיננסים  
**תפקיד**: QA Automation Engineer

**בהצלחה! 🎉**

