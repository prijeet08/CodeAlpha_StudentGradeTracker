# CodeAlpha_StudentGradeTracker
 Student Grade Tracker 
 ● Build a Java program to input and manage student grades.
 ● Calculate average, highest, and lowest scores.
 ● Use arrays or ArrayLists to store and manage data. 
 ● Display a summary report of all students. 
 ● Make the interface console-based or GUI-based as desired
# **Student Grade Tracker - Detailed Explanation**  

## **1. Overview**  
The **Student Grade Tracker** is a Java program designed to help teachers or educators efficiently manage and analyze student grades. It allows users to:  
- **Input and store** student grades  
- **Calculate** average, highest, and lowest scores  
- **Generate reports** summarizing student performance  
- **Choose between a console-based or GUI-based interface**  

---

## **2. Key Features**  

### **(A) Input and Manage Student Grades**  
- The program allows users to **add students** along with their grades for different subjects.  
- Each student has:  
  - A **unique identifier (ID)**  
  - A **name**  
  - A **list of subjects and corresponding grades**  
- Users can **add, modify, or remove** student records as needed.  

### **(B) Calculate Average, Highest, and Lowest Scores**  
- **Average Score**: Computes the mean grade for each student and the entire class.  
- **Highest Score**: Identifies the top-performing student(s).  
- **Lowest Score**: Identifies the student(s) needing improvement.  

### **(C) Data Storage (Arrays or ArrayLists)**  
- **Arrays** (fixed-size) or **ArrayLists** (dynamic-size) store student data.  
- Each student’s grades can be stored in a **HashMap** (for subject-grade pairs).  
- Example structure:  
  ```java
  List<Student> students = new ArrayList<>();  
  class Student {
      String name;
      String id;
      Map<String, Double> grades; // Subject → Grade
  }
  ```

### **(D) Summary Report Generation**  
- Displays a formatted report containing:  
  - **List of all students** (name, ID, grades)  
  - **Class statistics** (average, highest, lowest grades)  
  - **Subject-wise performance** (if multiple subjects are tracked)  
  - **Top and bottom performers**  

### **(E) Console-Based vs. GUI-Based Interface**  
- **Console-Based Version**  
  - Runs in the terminal/command prompt.  
  - Uses `Scanner` for input and `System.out` for output.  
  - Best for quick, lightweight use.  

- **GUI-Based Version**  
  - Built using **Java Swing** or **JavaFX**.  
  - Provides **buttons, text fields, and visual feedback**.  
  - Better for **interactive, user-friendly** applications.  

---

## **3. Workflow**  

### **(A) Adding a Student**  
1. User enters **student name** and **ID**.  
2. Program checks for **duplicate IDs**.  
3. Student is added to the list.  

### **(B) Adding Grades**  
1. User selects a student (by ID or name).  
2. Enters **subject name** and **grade (0-100)**.  
3. Grade is stored in the student’s record.  

### **(C) Generating Reports**  
1. User selects a report type:  
   - **Class Statistics** (average, highest, lowest)  
   - **Subject-wise Analysis** (performance per subject)  
   - **Top/Bottom Performers** (ranking students)  
2. Program computes and displays results.  

### **(D) Searching Students**  
- Users can search by:  
  - **Name** (partial matches allowed)  
  - **ID** (exact match)  
  - **Grade Range** (e.g., find students with averages between 70-90)  

---

## **4. Technical Implementation**  

### **(A) Data Structures Used**  
| Structure       | Purpose |
|----------------|---------|
| **ArrayList**  | Stores all student objects |
| **HashMap**    | Maps subjects to grades for each student |
| **Arrays** (optional) | Fixed-size alternative to ArrayList |

### **(B) Key Methods**  
| Method | Description |
|--------|-------------|
| `addStudent()` | Adds a new student to the list |
| `addGrade()` | Assigns a grade to a student |
| `calculateAverage()` | Computes average score |
| `findHighestGrade()` | Identifies the top score |
| `findLowestGrade()` | Identifies the lowest score |
| `generateReport()` | Displays summary statistics |
| `searchStudent()` | Finds students by name, ID, or grade range |

### **(C) Persistence (Optional)**  
- **Saving Data**: Uses **file I/O** (e.g., `.dat` file via `ObjectOutputStream`).  
- **Loading Data**: Restores saved data when the program starts.  

---

## **5. Choosing Between Console & GUI**  

| Feature | Console-Based | GUI-Based |
|---------|--------------|-----------|
| **Ease of Use** | Requires typing commands | Buttons & menus for navigation |
| **Visual Appeal** | Text-only | Interactive windows |
| **Best For** | Quick CLI usage | User-friendly applications |
| **Implementation** | `Scanner` + `System.out` | Java Swing/JavaFX |

---

## **6. Expected Output Examples**  

### **Console Output**  
```
=== Student Grade Tracker ===
1. Add Student
2. Add Grade
3. View All Students
4. Generate Report
5. Exit
Choose an option: 3

List of Students:
-----------------
ID: S001, Name: Alice
  Math: 95.0
  Science: 88.5
  Average: 91.75
-----------------
ID: S002, Name: Bob
  Math: 72.0
  Science: 85.0
  Average: 78.50
-----------------
```

## **7. Conclusion**  
The **Student Grade Tracker** is a versatile tool that helps educators efficiently manage and analyze student performance. Whether using a **console-based** (fast, lightweight) or **GUI-based** (user-friendly) interface, it provides:  
✔ **Easy grade management**  
✔ **Automatic calculations** (average, highest, lowest)  
✔ **Detailed reporting**  
✔ **Search functionality**  
