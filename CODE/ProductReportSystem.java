import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ProductReportSystem extends JFrame {

    // UI Components
    private JComboBox<String> reportTypeComboBox;
    private JTextField startDateField;
    private JTextField endDateField;
    private JTextArea descriptionArea;
    private JButton generateButton;
    private JButton clearButton;

    // Data Storage
    private List<SaleRecord> salesData;
    private List<InventoryRecord> inventoryData;
    private List<CustomerRecord> customerData;
    private List<SupplierRecord> supplierData;

    // Data Model Classes
    private static class SaleRecord {
        LocalDate date;
        String product;
        int quantity;
        double price;

        public SaleRecord(LocalDate date, String product, int quantity, double price) {
            this.date = date;
            this.product = product;
            this.quantity = quantity;
            this.price = price;
        }
    }

    private static class InventoryRecord {
        LocalDate date;
        String product;
        int stock;
        String location;

        public InventoryRecord(LocalDate date, String product, int stock, String location) {
            this.date = date;
            this.product = product;
            this.stock = stock;
            this.location = location;
        }
    }

    private static class CustomerRecord {
        LocalDate date;
        String customerName;
        String purchasedProduct;
        double amount;

        public CustomerRecord(LocalDate date, String customerName, String purchasedProduct, double amount) {
            this.date = date;
            this.customerName = customerName;
            this.purchasedProduct = purchasedProduct;
            this.amount = amount;
        }
    }

    private static class SupplierRecord {
        LocalDate date;
        String supplierName;
        String suppliedProduct;
        int quantity;

        public SupplierRecord(LocalDate date, String supplierName, String suppliedProduct, int quantity) {
            this.date = date;
            this.supplierName = supplierName;
            this.suppliedProduct = suppliedProduct;
            this.quantity = quantity;
        }
    }

    public ProductReportSystem() {
        initComponents();
        initializeSampleData();
    }

    private void initializeSampleData() {
        // Initialize sales data (2025 dates)
        salesData = new ArrayList<>();
        salesData.add(new SaleRecord(LocalDate.parse("2025-05-01"), "Laptop", 5, 999.99));
        salesData.add(new SaleRecord(LocalDate.parse("2025-05-10"), "Phone", 10, 699.99));
        salesData.add(new SaleRecord(LocalDate.parse("2025-05-15"), "Tablet", 8, 399.99));

        // Initialize inventory data
        inventoryData = new ArrayList<>();
        inventoryData.add(new InventoryRecord(LocalDate.parse("2025-05-01"), "Laptop", 25, "Warehouse A"));
        inventoryData.add(new InventoryRecord(LocalDate.parse("2025-05-07"), "Phone", 50, "Warehouse B"));
        inventoryData.add(new InventoryRecord(LocalDate.parse("2025-05-15"), "Tablet", 30, "Warehouse A"));

        // Initialize customer data
        customerData = new ArrayList<>();
        customerData.add(new CustomerRecord(LocalDate.parse("2025-05-02"), "naseem", "Laptop", 999.99));
        customerData.add(new CustomerRecord(LocalDate.parse("2025-05-08"), "shahid", "Phone", 699.99));
        customerData.add(new CustomerRecord(LocalDate.parse("2025-05-12"), "wazir", "Tablet", 399.99));

        // Initialize supplier data
        supplierData = new ArrayList<>();
        supplierData.add(new SupplierRecord(LocalDate.parse("2025-05-03"), "naseem", "Laptop", 20));
        supplierData.add(new SupplierRecord(LocalDate.parse("2025-05-09"), "shahid", "Phone", 30));
        supplierData.add(new SupplierRecord(LocalDate.parse("2025-05-14"), "wazir", "Tablet", 25));
    }

    private void initComponents() {
        setTitle("Product Report Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);

        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        // Report type
        formPanel.add(new JLabel("Report Type:"));
        String[] reportTypes = {"Sales", "Inventory", "Customer", "Supplier"};
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

            // Generate report
            StringBuilder report = new StringBuilder();
            report.append(String.format(
                "Report Type: %s\nStart Date: %s\nEnd Date: %s\nDescription: %s\n\n",
                reportType, startDate, endDate, description
            ));

            switch (reportType) {
                case "Sales":
                    generateSalesReport(report, startDate, endDate);
                    break;
                case "Inventory":
                    generateInventoryReport(report, startDate, endDate);
                    break;
                case "Customer":
                    generateCustomerReport(report, startDate, endDate);
                    break;
                case "Supplier":
                    generateSupplierReport(report, startDate, endDate);
                    break;
            }

            // Save to file
            saveReportToFile(report.toString());

            JOptionPane.showMessageDialog(this, "Report generated and saved successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generateSalesReport(StringBuilder report, LocalDate startDate, LocalDate endDate) {
        report.append("=== SALES DETAILS ===\n");
        report.append(String.format("%-12s %-15s %-8s %-10s %-12s\n", 
            "Date", "Product", "Qty", "Price", "Total"));
        report.append("------------------------------------------------\n");

        double grandTotal = 0;
        for (SaleRecord record : salesData) {
            if (!record.date.isBefore(startDate) && !record.date.isAfter(endDate)) {
                double total = record.quantity * record.price;
                grandTotal += total;
                report.append(String.format("%-12s %-15s %-8d $%-9.2f $%-10.2f\n",
                    record.date, record.product, record.quantity, record.price, total));
            }
        }
        report.append("\nGrand Total: $").append(String.format("%.2f", grandTotal)).append("\n");
    }

    private void generateInventoryReport(StringBuilder report, LocalDate startDate, LocalDate endDate) {
        report.append("=== INVENTORY DETAILS ===\n");
        report.append(String.format("%-12s %-15s %-8s %-15s\n", 
            "Date", "Product", "Stock", "Location"));
        report.append("----------------------------------------\n");

        for (InventoryRecord record : inventoryData) {
            if (!record.date.isBefore(startDate) && !record.date.isAfter(endDate)) {
                report.append(String.format("%-12s %-15s %-8d %-15s\n",
                    record.date, record.product, record.stock, record.location));
            }
        }
    }

    private void generateCustomerReport(StringBuilder report, LocalDate startDate, LocalDate endDate) {
        report.append("=== CUSTOMER DETAILS ===\n");
        report.append(String.format("%-12s %-20s %-15s %-10s\n", 
            "Date", "Customer Name", "Product", "Amount"));
        report.append("------------------------------------------------\n");

        double totalRevenue = 0;
        for (CustomerRecord record : customerData) {
            if (!record.date.isBefore(startDate) && !record.date.isAfter(endDate)) {
                totalRevenue += record.amount;
                report.append(String.format("%-12s %-20s %-15s $%-9.2f\n",
                    record.date, record.customerName, record.purchasedProduct, record.amount));
            }
        }
        report.append("\nTotal Revenue: $").append(String.format("%.2f", totalRevenue)).append("\n");
    }

    private void generateSupplierReport(StringBuilder report, LocalDate startDate, LocalDate endDate) {
        report.append("=== SUPPLIER DETAILS ===\n");
        report.append(String.format("%-12s %-20s %-15s %-8s\n", 
            "Date", "Supplier Name", "Product", "Qty"));
        report.append("----------------------------------------\n");

        int totalQuantity = 0;
        for (SupplierRecord record : supplierData) {
            if (!record.date.isBefore(startDate) && !record.date.isAfter(endDate)) {
                totalQuantity += record.quantity;
                report.append(String.format("%-12s %-20s %-15s %-8d\n",
                    record.date, record.supplierName, record.suppliedProduct, record.quantity));
            }
        }
        report.append("\nTotal Quantity Received: ").append(totalQuantity).append("\n");
    }

    private void saveReportToFile(String report) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("reports.txt", true))) {
            writer.write(report);
            writer.write("\n========================================\n\n");
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