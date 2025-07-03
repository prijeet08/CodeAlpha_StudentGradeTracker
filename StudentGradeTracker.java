import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.io.*;

public class GUIGradeTracker extends JFrame {
    private static class Student implements Serializable {
        private String name;
        private String id;
        private Map<String, Double> grades = new HashMap<>();

        public Student(String name, String id) {
            this.name = name;
            this.id = id;
        }

        public void addGrade(String subject, double grade) {
            grades.put(subject, grade);
        }

        public double getAverage() {
            return grades.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
        }
    }

    private List<Student> students = new ArrayList<>();
    private final String DATA_FILE = "grades_gui.dat";

    // GUI Components
    private JTextField nameField, idField, subjectField, gradeField, searchField;
    private JTextArea outputArea;
    private JComboBox<String> reportCombo, searchCombo;

    public GUIGradeTracker() {
        super("Student Grade Tracker (GUI)");
        loadData();
        initializeGUI();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeGUI() {
        // Create panels
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JPanel searchPanel = new JPanel(new FlowLayout());
        
        // Input fields
        inputPanel.add(new JLabel("Student Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);
        
        inputPanel.add(new JLabel("Student ID:"));
        idField = new JTextField();
        inputPanel.add(idField);
        
        inputPanel.add(new JLabel("Subject:"));
        subjectField = new JTextField();
        inputPanel.add(subjectField);
        
        inputPanel.add(new JLabel("Grade (0-100):"));
        gradeField = new JTextField();
        inputPanel.add(gradeField);
        
        // Buttons
        JButton addStudentBtn = new JButton("Add Student");
        addStudentBtn.addActionListener(e -> addStudent());
        
        JButton addGradeBtn = new JButton("Add Grade");
        addGradeBtn.addActionListener(e -> addGrade());
        
        JButton viewAllBtn = new JButton("View All");
        viewAllBtn.addActionListener(e -> viewStudents());
        
        buttonPanel.add(addStudentBtn);
        buttonPanel.add(addGradeBtn);
        buttonPanel.add(viewAllBtn);
        
        // Report options
        buttonPanel.add(new JLabel("Reports:"));
        reportCombo = new JComboBox<>(new String[]{
            "Class Statistics", "Subject Statistics", "Top/Bottom Performers"
        });
        buttonPanel.add(reportCombo);
        
        JButton generateReportBtn = new JButton("Generate");
        generateReportBtn.addActionListener(e -> generateReport());
        buttonPanel.add(generateReportBtn);
        
        // Search components
        searchPanel.add(new JLabel("Search:"));
        searchCombo = new JComboBox<>(new String[]{
            "By Name", "By ID", "By Grade Range"
        });
        searchPanel.add(searchCombo);
        
        searchField = new JTextField(15);
        searchPanel.add(searchField);
        
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> searchStudents());
        searchPanel.add(searchBtn);
        
        // Output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        
        // Add components to frame
        setLayout(new BorderLayout(5, 5));
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.EAST);
    }

    private void addStudent() {
        String name = nameField.getText().trim();
        String id = idField.getText().trim();
        
        if (name.isEmpty() || id.isEmpty()) {
            showMessage("Please enter both name and ID.");
            return;
        }
        
        if (students.stream().anyMatch(s -> s.id.equals(id))) {
            showMessage("Student ID already exists!");
            return;
        }
        
        students.add(new Student(name, id));
        saveData();
        showMessage("Student added successfully!");
        clearFields();
    }

    private void addGrade() {
        String id = idField.getText().trim();
        String subject = subjectField.getText().trim();
        String gradeText = gradeField.getText().trim();
        
        if (id.isEmpty() || subject.isEmpty() || gradeText.isEmpty()) {
            showMessage("Please enter ID, subject, and grade.");
            return;
        }
        
        try {
            double grade = Double.parseDouble(gradeText);
            if (grade < 0 || grade > 100) {
                showMessage("Grade must be between 0 and 100.");
                return;
            }
            
            for (Student s : students) {
                if (s.id.equals(id)) {
                    s.addGrade(subject, grade);
                    saveData();
                    showMessage("Grade added successfully!");
                    clearFields();
                    return;
                }
            }
            
            showMessage("Student not found!");
        } catch (NumberFormatException e) {
            showMessage("Please enter a valid number for grade.");
        }
    }

    private void viewStudents() {
        if (students.isEmpty()) {
            showMessage("No students available.");
            return;
        }
        
        StringBuilder sb = new StringBuilder("=== ALL STUDENTS ===\n\n");
        students.forEach(s -> {
            sb.append(String.format("ID: %s, Name: %s\n", s.id, s.name));
            if (s.grades.isEmpty()) {
                sb.append("  No grades recorded\n");
            } else {
                s.grades.forEach((subj, grade) -> 
                    sb.append(String.format("  %s: %.2f\n", subj, grade)));
                sb.append(String.format("  Average: %.2f\n", s.getAverage()));
            }
            sb.append("-----------------\n");
        });
        
        outputArea.setText(sb.toString());
    }

    private void generateReport() {
        if (students.isEmpty()) {
            showMessage("No students available.");
            return;
        }
        
        String reportType = (String) reportCombo.getSelectedItem();
        StringBuilder sb = new StringBuilder();
        
        switch (reportType) {
            case "Class Statistics":
                classStatistics(sb);
                break;
            case "Subject Statistics":
                subjectStatistics(sb);
                break;
            case "Top/Bottom Performers":
                topBottomPerformers(sb);
                break;
        }
        
        outputArea.setText(sb.toString());
    }

    private void classStatistics(StringBuilder sb) {
        DoubleSummaryStatistics stats = students.stream()
            .filter(s -> !s.grades.isEmpty())
            .mapToDouble(Student::getAverage)
            .summaryStatistics();
        
        sb.append("=== CLASS STATISTICS ===\n\n");
        sb.append(String.format("Students with grades: %d\n", stats.getCount()));
        sb.append(String.format("Average Grade: %.2f\n", stats.getAverage()));
        sb.append(String.format("Highest Average: %.2f\n", stats.getMax()));
        sb.append(String.format("Lowest Average: %.2f\n", stats.getMin()));
    }

    private void subjectStatistics(StringBuilder sb) {
        Map<String, DoubleSummaryStatistics> subjectStats = students.stream()
            .flatMap(s -> s.grades.entrySet().stream())
            .collect(Collectors.groupingBy(
                Map.Entry::getKey,
                Collectors.summarizingDouble(Map.Entry::getValue)));
        
        if (subjectStats.isEmpty()) {
            sb.append("No grades recorded for any subject.");
            return;
        }
        
        sb.append("=== SUBJECT STATISTICS ===\n\n");
        subjectStats.forEach((subject, stats) -> {
            sb.append(String.format("Subject: %s\n", subject));
            sb.append(String.format("  Grades Recorded: %d\n", stats.getCount()));
            sb.append(String.format("  Average: %.2f\n", stats.getAverage()));
            sb.append(String.format("  Highest: %.2f\n", stats.getMax()));
            sb.append(String.format("  Lowest: %.2f\n", stats.getMin()));
            sb.append("-----------------\n");
        });
    }

    private void topBottomPerformers(StringBuilder sb) {
        List<Student> gradedStudents = students.stream()
            .filter(s -> !s.grades.isEmpty())
            .sorted(Comparator.comparingDouble(Student::getAverage).reversed())
            .toList();
        
        if (gradedStudents.isEmpty()) {
            sb.append("No students with grades recorded.");
            return;
        }
        
        sb.append("=== TOP/BOTTOM PERFORMERS ===\n\n");
        sb.append("Top 5 Students:\n");
        gradedStudents.stream().limit(5).forEach(s -> 
            sb.append(String.format("%s (ID: %s) - Avg: %.2f\n", 
                s.name, s.id, s.getAverage())));
        
        sb.append("\nBottom 5 Students:\n");
        gradedStudents.stream()
            .skip(Math.max(0, gradedStudents.size() - 5))
            .forEach(s -> 
                sb.append(String.format("%s (ID: %s) - Avg: %.2f\n", 
                    s.name, s.id, s.getAverage())));
    }

    private void searchStudents() {
        String query = searchField.getText().trim();
        String searchType = (String) searchCombo.getSelectedItem();
        
        if (query.isEmpty()) {
            showMessage("Please enter a search term.");
            return;
        }
        
        List<Student> results = new ArrayList<>();
        
        switch (searchType) {
            case "By Name":
                results = students.stream()
                    .filter(s -> s.name.toLowerCase().contains(query.toLowerCase()))
                    .toList();
                break;
                
            case "By ID":
                results = students.stream()
                    .filter(s -> s.id.contains(query))
                    .toList();
                break;
                
            case "By Grade Range":
                try {
                    String[] range = query.split("-");
                    if (range.length != 2) {
                        showMessage("Use format: min-max (e.g., 70-90)");
                        return;
                    }
                    
                    double min = Double.parseDouble(range[0].trim());
                    double max = Double.parseDouble(range[1].trim());
                    
                    results = students.stream()
                        .filter(s -> !s.grades.isEmpty())
                        .filter(s -> s.getAverage() >= min && s.getAverage() <= max)
                        .toList();
                } catch (NumberFormatException e) {
                    showMessage("Invalid numbers in range.");
                    return;
                }
                break;
        }
        
        displaySearchResults(results, searchType + ": " + query);
    }

    private void displaySearchResults(List<Student> results, String criteria) {
        if (results.isEmpty()) {
            showMessage("No students found matching: " + criteria);
            return;
        }
        
        StringBuilder sb = new StringBuilder("=== SEARCH RESULTS ===\n\n");
        sb.append("Criteria: ").append(criteria).append("\n\n");
        results.forEach(s -> {
            sb.append(String.format("ID: %s, Name: %s\n", s.id, s.name));
            if (!s.grades.isEmpty()) {
                sb.append(String.format("  Average: %.2f\n", s.getAverage()));
            }
            sb.append("-----------------\n");
        });
        
        outputArea.setText(sb.toString());
    }

    private void showMessage(String message) {
        outputArea.setText(message);
    }

    private void clearFields() {
        nameField.setText("");
        idField.setText("");
        subjectField.setText("");
        gradeField.setText("");
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(DATA_FILE))) {
            students = (List<Student>) ois.readObject();
        } catch (FileNotFoundException e) {
            // First run - no data file exists
        } catch (IOException | ClassNotFoundException e) {
            showMessage("Error loading data: " + e.getMessage());
        }
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(DATA_FILE))) {
            oos.writeObject(students);
        } catch (IOException e) {
            showMessage("Error saving data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUIGradeTracker());
    }
}