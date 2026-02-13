import java.sql.*;
import java.util.Scanner;

public class Project {

    private static final String URL = "jdbc:mysql://localhost:3306/company_db?createDatabaseIfNotExist=true";
    private static final String USER = "root";
    private static final String PASSWORD = "Ukulsum@2402";

    public static void main(String[] args) {

        createTableIfNotExists();   // 🔥 Auto create table

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n==== Employee Management System ====");
            System.out.println("1. Add Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Update Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. Search Employee");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    addEmployee(sc);
                    break;

                case 2:
                    viewEmployees();
                    break;

                case 3:
                    updateEmployee(sc);
                    break;

                case 4:
                    deleteEmployee(sc);
                    break;

                case 5:
                    searchEmployee(sc);
                    break;

                case 6:
                    System.out.println("Exiting...");
                    sc.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // 🔥 Create table if not exists
    public static void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS employees ("
                + "id INT PRIMARY KEY AUTO_INCREMENT,"
                + "name VARCHAR(100) NOT NULL,"
                + "email VARCHAR(100) UNIQUE,"
                + "department VARCHAR(50),"
                + "salary DOUBLE"
                + ")";

        try (Connection con = getConnection();
             Statement stmt = con.createStatement()) {

            stmt.executeUpdate(sql);
            System.out.println("✅ Table Ready!");

        } catch (SQLException e) {
            System.out.println("❌ Table Creation Error: " + e.getMessage());
        }
    }

    public static void addEmployee(Scanner sc) {

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        System.out.print("Enter Department: ");
        String department = sc.nextLine();

        System.out.print("Enter Salary: ");
        double salary = sc.nextDouble();

        String sql = "INSERT INTO employees(name, email, department, salary) VALUES (?, ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, department);
            ps.setDouble(4, salary);

            ps.executeUpdate();
            System.out.println("✅ Employee Added Successfully!");

        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    public static void viewEmployees() {

        String sql = "SELECT * FROM employees";

        try (Connection con = getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println("--------------------------------");
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("Department: " + rs.getString("department"));
                System.out.println("Salary: " + rs.getDouble("salary"));
            }

        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    public static void updateEmployee(Scanner sc) {

        System.out.print("Enter Employee ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter New Department: ");
        String department = sc.nextLine();

        System.out.print("Enter New Salary: ");
        double salary = sc.nextDouble();

        String sql = "UPDATE employees SET department = ?, salary = ? WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, department);
            ps.setDouble(2, salary);
            ps.setInt(3, id);

            int rows = ps.executeUpdate();

            if (rows > 0)
                System.out.println("✅ Employee Updated Successfully!");
            else
                System.out.println("❌ Employee Not Found!");

        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    public static void deleteEmployee(Scanner sc) {

        System.out.print("Enter Employee ID: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM employees WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0)
                System.out.println("✅ Employee Deleted Successfully!");
            else
                System.out.println("❌ Employee Not Found!");

        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    public static void searchEmployee(Scanner sc) {

        System.out.print("Enter Employee ID: ");
        int id = sc.nextInt();

        String sql = "SELECT * FROM employees WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("Department: " + rs.getString("department"));
                System.out.println("Salary: " + rs.getDouble("salary"));
            } else {
                System.out.println("❌ Employee Not Found!");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
}
