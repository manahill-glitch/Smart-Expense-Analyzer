import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// ================= ABSTRACTION =================

interface Searchable {

    void searchExpense(String category);
}

// ================= EXPENSE CLASS =================

class Expense {

    String category;
    double amount;
    String date;
    String month;
    String year;

    // Constructor
    Expense(String category,double amount,String date,String month,String year) {

        this.category = category;
        this.amount = amount;
        this.date = date;
        this.month = month;
        this.year = year;
    }

    // ================= POLYMORPHISM =================

    Expense(String category, double amount) {

        this.category = category;
        this.amount = amount;
    }
}

// ================= INHERITANCE =================

public class ExpenseGUI extends JFrame implements Searchable {

    private JTextField categoryField;
    private JTextField amountField;
    private JTextField dateField;
    private JTextArea outputArea;

    private ArrayList<Expense> expenses = new ArrayList<>();

    public ExpenseGUI() {

        // Window Settings
        setTitle("Smart Expense Analyzer");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1200, 800));
        setLayout(null);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      ImageIcon bgImage = new ImageIcon("background.jpeg");
ImageIcon logo = new ImageIcon("logo.jpeg");
setIconImage(logo.getImage());

Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

Image scaledImage = bgImage.getImage().getScaledInstance(
        screenSize.width,
        screenSize.height,
        Image.SCALE_SMOOTH
);

JLabel background = new JLabel(new ImageIcon(scaledImage));
background.setBounds(0, 0, screenSize.width, screenSize.height);


        add(background);

        JLabel title = new JLabel("SMART EXPENSE ANALYZER");
        
        title.setBounds(0, 20, screenSize.width, 50);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Comic Sans MS", Font.BOLD, 32));
        title.setForeground(new Color(219,112,147));
        background.add(title);
        

        // ================= CATEGORY =================

        JLabel categoryLabel = new JLabel("Category:");
        Color labelColor = new Color(219,112,147);
        categoryLabel.setFont(new Font("Comic Sans MS",Font.PLAIN, 20));
        categoryLabel.setBounds(500, 140, 150, 40);
        categoryLabel.setForeground(labelColor);
        background.add(categoryLabel);
        categoryField = new JTextField();
        categoryField.setBounds(700, 140, 300, 40);
        background.add(categoryField);

        // ================= AMOUNT =================

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setBounds(500, 210, 150, 40);
        amountLabel.setFont(new Font("Comic Sans MS",Font.BOLD, 20));
        amountLabel.setForeground(labelColor);
        background.add(amountLabel);
        amountField = new JTextField();
        amountField.setBounds(700, 210, 300, 40);
        background.add(amountField);

        // ================= DATE =================

        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setBounds(500, 280, 150, 40);
        dateLabel.setFont(new Font("Comic Sans MS",Font.BOLD, 20));
        dateLabel.setForeground(labelColor);
        background.add(dateLabel);
        dateField = new JTextField();
        dateField.setBounds(700, 280, 300, 40);
        background.add(dateField);

        // ================= BUTTONS =================

        JButton addButton = new JButton("Add");
        JButton viewButton = new JButton("Show All");
        JButton searchButton = new JButton("Search");
        JButton clearButton = new JButton("Clear");
        addButton.setBounds(420, 380, 140, 45);
        viewButton.setBounds(600, 380, 160, 45);
        searchButton.setBounds(800, 380, 140, 45);
        clearButton.setBounds(980, 380, 140, 45);

        // Button Colors

        addButton.setBackground(new Color(173,216,230));
        addButton.setForeground(new Color(219,112,147));

        viewButton.setBackground(new Color(173,216,230));
        viewButton.setForeground(new Color(219,112,147));

        searchButton.setBackground(new Color(173,216,230));
        searchButton.setForeground(new Color(219,112,147));

        clearButton.setBackground(new Color(173,216,230));
        clearButton.setForeground(new Color(219,112,147));

        background.add(addButton);
        background.add(viewButton);
        background.add(searchButton);
        background.add(clearButton);

        // ================= OUTPUT AREA =================

        outputArea = new JTextArea();
        outputArea.setFont(new Font("Comic Sans MS",Font.PLAIN, 16));
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBounds(380, 480, 760, 300);
        background.add(scrollPane);

        // ================= ADD BUTTON =================

        addButton.addActionListener(e -> {

        String category = categoryField.getText();
        String amountText = amountField.getText();
        String date = dateField.getText();

        // Date Validation
        String dateError = validateDate(date);

        if (!dateError.equals("")) {

        outputArea.setText(dateError);
        return;
        }

        double amount;

       // Amount Validation
       try {

            amount = Double.parseDouble(amountText);
        }

        catch (NumberFormatException ex) {

            outputArea.setText(
                "Please enter a valid numeric amount."
        );
            return;
        }
  
        // Negative Amount Check
        if (amount < 0) {

            outputArea.setText(
                "Amount cannot be negative."
            );
            return;
        }

        String month = getMonthName(date);
        String year = getYear(date);

        expenses.add(new Expense(category, amount, date, month, year));
        outputArea.setText("Expense Added Successfully!");

        categoryField.setText("");
        amountField.setText("");
        dateField.setText("");
        });

        // ================= SHOW ALL BUTTON =================

        viewButton.addActionListener(e -> {

            if (expenses.isEmpty()) {
                outputArea.setText("No expenses recorded.");
                return;
            }

            Collections.sort(expenses, new Comparator<Expense>() {

                        public int compare(Expense e1,Expense e2) {

                            int yearCompare = e1.year.compareTo(e2.year);

                            if (yearCompare != 0) {

                                return yearCompare;
                            }
                            return e1.month.compareTo(e2.month);
                        }
                    }
            );

            String text = "";
            String currentYear = "";
            String currentMonth = "";
            double monthTotal = 0;
            for (Expense ex : expenses) {

                // New Year

                if (!ex.year.equals(currentYear)) {

                    if (!currentMonth.equals("")) {

                        text += "Total: Rs." + String.format("%.0f", monthTotal) + "\n\n";
                    }
                    currentYear = ex.year;
                    currentMonth = "";
                    text += "\n============ " + currentYear + " ============\n\n";
                }

                // New Month

                if (!ex.month.equals(currentMonth)) {

                    if (!currentMonth.equals("")) {
                        text += "Total: Rs." + String.format("%.0f", monthTotal)+ "\n\n";
                    }
                    currentMonth = ex.month;
                    monthTotal = 0;
                    text += "------- "+ currentMonth+ " -------\n";
                }

                text += ex.category + " - Rs." + String.format("%.0f", ex.amount) + " (" + ex.date+ ")\n";
                monthTotal += ex.amount;
            }

            text += "Total: Rs." + String.format("%.0f", monthTotal);
            outputArea.setText(text);
        });

        // ================= SEARCH BUTTON =================

        searchButton.addActionListener(e -> {
            String search =
                    JOptionPane.showInputDialog(
                            "Enter category to search:"
                    );
            searchExpense(search);
        });

        // ================= CLEAR BUTTON =================

        clearButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                            null,
                            "Are you sure you want to permanently remove all expense records?",
                            "Confirm Data Deletion",
                            JOptionPane.YES_NO_OPTION
                    );
            if (choice == JOptionPane.YES_OPTION) {
                expenses.clear();
                outputArea.setText(
                        "All expense records have been cleared successfully."
                );
            }
        });
        setVisible(true);
    }

    // ================= SEARCH METHOD =================

    @Override
    public void searchExpense(String search) {
        if (search == null ||
                search.trim().isEmpty()) {outputArea.setText("Please enter a valid category." );
            return;
        }

        String text = "Search Results for: " + search + "\n\n";

        boolean found = false;

        for (Expense ex : expenses) {

            if (ex.category.equalsIgnoreCase(search)) {

                text += "Category: "
                        + ex.category
                        + "\nAmount: Rs."
                        + String.format("%.0f", ex.amount)
                        + "\nDate: "
                        + ex.date
                        + "\nMonth: "
                        + ex.month
                        + "\nYear: "
                        + ex.year
                        + "\n\n";

                found = true;
            }
        }

        if (!found) {

            outputArea.setText("No expense records found for category: " + search);
        }

        else {

            outputArea.setText(text);
        }
    }

    // ================= DATE VALIDATION =================

    public String validateDate(String date) {

    String error = "";

    // Format Check
    if (!date.matches("\\d{2}-\\d{2}-\\d{4}")) {

        error += "Invalid format! Use DD-MM-YYYY\n";
        return error;
    }

    String[] parts = date.split("-");

    int day = Integer.parseInt(parts[0]);
    int month = Integer.parseInt(parts[1]);
    int year = Integer.parseInt(parts[2]);

    // Month Validation
    if (month < 1 || month > 12) {

        error += "Invalid month! Month must be between 1 and 12\n";
    }

    // Day Validation
    if (day < 1 || day > 31) {

        error += "Invalid day! Day must be between 1 and 31\n";
    }

    // Check real calendar date
    try {

        java.time.LocalDate enteredDate =
                java.time.LocalDate.of(year, month, day);

        java.time.LocalDate today =
                java.time.LocalDate.now();

        // Past Date Check
        if (enteredDate.isBefore(today)) {

            error += "Past dates cannot be entered\n";
        }
    }

    catch (Exception ex) {

        error += "Invalid calendar date\n";
    }

    return error;
}
    // ================= GET MONTH NAME =================

    public String getMonthName(String date) {

        if (date.contains("-")) {

            String[] parts = date.split("-");

            String month = parts[1];

            switch (month) {

                case "01":
                    return "January";

                case "02":
                    return "February";

                case "03":
                    return "March";

                case "04":
                    return "April";

                case "05":
                    return "May";

                case "06":
                    return "June";

                case "07":
                    return "July";

                case "08":
                    return "August";

                case "09":
                    return "September";

                case "10":
                    return "October";

                case "11":
                    return "November";

                case "12":
                    return "December";
            }
        }

        return "Unknown";
    }

    // ================= GET YEAR =================

    public String getYear(String date) {

        String[] parts = date.split("-");

        return parts[2];
    }

    // ================= MAIN METHOD =================

    public static void main(String[] args) {

        new ExpenseGUI();
    }
}