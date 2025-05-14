import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProductReportSystem extends JFrame {

    private JComboBox<String> reportTypeComboBox;
    private JTextField startDateField;
    private JTextField endDateField;
    private JTextArea descriptionArea;
    private JButton generateButton;
    private JButton clearButton;

    public ProductReportSystem() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Product Report Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        // Report type
        formPanel.add(new JLabel("Report Type:"));
        String[] reportTypes = {"Sales", "Inventory", "Customer", "Supplier", "Performance"};
        reportTypeComboBox = new JComboBox<>(reportTypes);
        formPanel.add(reportTypeComboBox);

        // Start date
        formPanel.add(new JLabel("Start Date (YYYY-MM-DD):"));
        startDateField = new JTextField();
        startDateField.setText(LocalDate.now().minusDays(7).toString());
        formPanel.add(startDateField);

        // End date
        formPanel.add(new JLabel("End Date (YYYY-MM-DD):"));
        endDateField = new JTextField();
        endDateField.setText(LocalDate.now().toString());
        formPanel.add(endDateField);

        // Description
        formPanel.add(new JLabel("Description:"));
        descriptionArea = new JTextArea(3, 20);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        formPanel.add(scrollPane);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        generateButton = new JButton("Generate Report");
        generateButton.addActionListener(this::generateReport);
        generateButton.setBackground(new Color(34, 139, 34));
        generateButton.setForeground(Color.WHITE);

        clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearForm());
        clearButton.setBackground(new Color(220, 53, 69));
        clearButton.setForeground(Color.WHITE);

        buttonPanel.add(clearButton);
        buttonPanel.add(generateButton);

        // Add panels to main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void generateReport(ActionEvent evt) {
        String reportType = (String) reportTypeComboBox.getSelectedItem();
        String startDateStr = startDateField.getText().trim();
        String endDateStr = endDateField.getText().trim();
        String description = descriptionArea.getText().trim();

        // Validate inputs
        if (startDateStr.isEmpty() || endDateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both start and end dates", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(startDateStr, formatter);
            LocalDate endDate = LocalDate.parse(endDateStr, formatter);

            if (startDate.isAfter(endDate)) {
                JOptionPane.showMessageDialog(this, "Start date cannot be after end date", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create report string
            String report = String.format(
                "Report Type: %s\nStart Date: %s\nEnd Date: %s\nDescription: %s\n\n",
                reportType, startDate, endDate, description
            );

            // Save to file
            saveReportToFile(report);

            JOptionPane.showMessageDialog(this, "Report generated and saved successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveReportToFile(String report) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("reports.txt", true))) {
            writer.write(report);
            writer.write("----------------------------------------\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving report: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        reportTypeComboBox.setSelectedIndex(0);
        startDateField.setText(LocalDate.now().minusDays(7).toString());
        endDateField.setText(LocalDate.now().toString());
        descriptionArea.setText("");
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        EventQueue.invokeLater(() -> {
            new ProductReportSystem().setVisible(true);
        });
    }
}