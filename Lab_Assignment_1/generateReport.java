import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Admin {
    private String adminId;
    private String name;

    public Admin(String adminId, String name) {
        this.adminId = adminId;
        this.name = name;
    }

    public void requestReport(String reportType, String startDate, String endDate) {
        ReportGenerator generator = new ReportGenerator(reportType, startDate, endDate);
        Report report = generator.generateReport();
        if (report != null) {
            System.out.println(report.display());
        }
    }
}

class ReportGenerator {
    private String reportType;
    private String startDate;
    private String endDate;

    public ReportGenerator(String reportType, String startDate, String endDate) {
        this.reportType = reportType;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Report generateReport() {
        if (validateInput()) {
            Database db = new Database();
            List<String> data = db.queryData(reportType, startDate, endDate);
            return new Report(reportType, data);
        } else {
            System.out.println("Invalid input parameters.");
            return null;
        }
    }

    private boolean validateInput() {
        return (reportType != null && !reportType.isEmpty()
                && startDate != null && !startDate.isEmpty()
                && endDate != null && !endDate.isEmpty());
    }
}

class Report {
    private String reportType;
    private List<String> data;

    public Report(String reportType, List<String> data) {
        this.reportType = reportType;
        this.data = data;
    }

    public String display() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Report ===\nType: ").append(reportType).append("\n");
        for (String line : data) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }
}

class Database {
    private List<Product> products;
    private List<SalesRecord> sales;

    public Database() {
        products = new ArrayList<>();
        sales = new ArrayList<>();

        // Adding sample product data
        products.add(new Product("P001", "Apples", 100, 0.50));
        products.add(new Product("P002", "Bananas", 40, 0.30));  // Low stock
        products.add(new Product("P003", "Oranges", 120, 0.60));
        products.add(new Product("P004", "Lemons", 25, 0.40));   // Low stock

        // Adding sample sales data
        sales.add(new SalesRecord("P001", 30));
        sales.add(new SalesRecord("P003", 50));
    }

    public List<String> queryData(String reportType, String startDate, String endDate) {
        System.out.println("Fetching data for " + reportType + " from " + startDate + " to " + endDate);
        List<String> result = new ArrayList<>();

        if (reportType.equalsIgnoreCase("Low Stock Report")) {
            for (Product p : products) {
                if (p.getQuantity() < 50) {
                    result.add(formatProduct(p));
                }
            }
        } else if (reportType.equalsIgnoreCase("Sales Report")) {
            for (SalesRecord s : sales) {
                Product p = findProductById(s.getProductId());
                if (p != null) {
                    result.add("- " + p.getName() + " sold: " + s.getQuantitySold() + " units");
                }
            }
        } else {
            // Default: Stock Report
            for (Product p : products) {
                result.add(formatProduct(p));
            }
        }
        return result;
    }

    private String formatProduct(Product p) {
        return "- ID: " + p.getProductId()
            + ", Name: " + p.getName()
            + ", Quantity: " + p.getQuantity()
            + ", Price: $" + p.getPrice();
    }

    private Product findProductById(String productId) {
        for (Product p : products) {
            if (p.getProductId().equals(productId)) {
                return p;
            }
        }
        return null;
    }
}

class Product {
    private String productId;
    private String name;
    private int quantity;
    private double price;

    public Product(String productId, String name, int quantity, double price) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}

class SalesRecord {
    private String productId;
    private int quantitySold;

    public SalesRecord(String productId, int quantitySold) {
        this.productId = productId;
        this.quantitySold = quantitySold;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantitySold() {
        return quantitySold;
    }
}

// Main class to test the flow
public class InventoryManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Create an Admin
        Admin admin = new Admin("A001", "John Doe");

        // Get report type from user input
        System.out.println("Enter report type (Stock Report, Low Stock Report, Sales Report):");
        String reportType = scanner.nextLine();

        // Get date range from user input
        System.out.println("Enter start date (YYYY-MM-DD):");
        String startDate = scanner.nextLine();

        System.out.println("Enter end date (YYYY-MM-DD):");
        String endDate = scanner.nextLine();

        // Request the report
        admin.requestReport(reportType, startDate, endDate);

        scanner.close();
    }
}