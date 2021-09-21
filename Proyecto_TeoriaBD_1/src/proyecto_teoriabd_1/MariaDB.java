package proyecto_teoriabd_1;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.HeadlessException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Tyler C
 */
public class MariaDB {

    public Connection dbcon;
    private DefaultComboBoxModel combo_model_company;
    private DefaultComboBoxModel combo_model_employee;
    private DefaultComboBoxModel combo_model_country;
    private DefaultComboBoxModel combo_model_shipper;
    private String user = "root";
    private String password = "root";
    private String DB_URL = "jdbc:mysql://localhost:3306/northwind";

    //addproducts
    private JTextField productName = new JTextField();
    private JTextField productstock = new JTextField();
    private JTextField productID = new JTextField();
    private JTextField productPrice = new JTextField();

    public void connectNorthwind() {
        Statement smt = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Connecting to a selected database...");
            dbcon = DriverManager.getConnection(
                    DB_URL, user, password);
            System.out.println("Connected database successfully...");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Connection 'Northwind' interrupted");
        }
    }

    public void test() {
        Statement stm = null;
        try {
            stm = dbcon.createStatement();
            ResultSet rs = stm.executeQuery("Select * from customers");
            while (rs.next()) {
                System.out.println("registry: " + rs.getString(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public DefaultComboBoxModel showCompanyName(DefaultComboBoxModel CB_model) {
        Statement stm = null;
        combo_model_company = CB_model;
        try {
            stm = dbcon.createStatement();
            ResultSet rs = stm.executeQuery("Select CompanyName from customers");
            while (rs.next()) {
                combo_model_company.addElement(rs.getString(1));
                //System.out.println("registry: " + rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

        return combo_model_company;
    }

    public DefaultComboBoxModel showEmployeeName(DefaultComboBoxModel CB_model) {
        Statement stm = null;
        combo_model_employee = CB_model;
        try {
            stm = dbcon.createStatement();
            ResultSet rs = stm.executeQuery("Select FirstName, LastName from employees");
            while (rs.next()) {
                combo_model_employee.addElement(rs.getString(2) + " " + rs.getString(1));
                //System.out.print("registry: " + rs.getString(1));
                //System.out.println(" " + rs.getString(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

        return combo_model_employee;
    }

    public DefaultComboBoxModel showCountry(DefaultComboBoxModel CB_model) {
        Statement stm = null;
        combo_model_country = CB_model;
        try {
            stm = dbcon.createStatement();
            ResultSet rs = stm.executeQuery("Select distinct shipCountry from orders order by shipCountry ASC");
            while (rs.next()) {
                combo_model_country.addElement(rs.getString(1));
                //System.out.println("registry: " + rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

        return combo_model_country;
    }

    public DefaultComboBoxModel showShipper(DefaultComboBoxModel CB_model) {
        Statement stm = null;
        combo_model_shipper = CB_model;
        try {
            stm = dbcon.createStatement();
            ResultSet rs = stm.executeQuery("Select CompanyName from shippers");
            while (rs.next()) {
                combo_model_shipper.addElement(rs.getString(1));
                //System.out.print("registry: " + rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

        return combo_model_shipper;
    }

    public void searchProduct(String searchProduct) {
        Statement stm = null;
        int id = 0, stock = 0;
        double unitP = 0;
        String name = "";
        try {
            stm = dbcon.createStatement();
            ResultSet rs = stm.executeQuery("Select * from Products Where '" + searchProduct + "' = ProductName");
            if (!rs.isBeforeFirst()) {
                JOptionPane.showMessageDialog(null, "The product was not found!!");
                System.out.println("No data");
                return;
            }
            while (rs.next()) {
                id = rs.getInt("ProductID");
                stock = rs.getInt("UnitsInStock");
                unitP = rs.getDouble("UnitPrice");
                name = rs.getString("ProductName");
                System.out.println("name" + name);
                System.out.println("Id: " + id);
            }
            productName.setText(name);
            productID.setText(Integer.toString(id));
            productPrice.setText(Double.toString(unitP));
            productstock.setText(Integer.toString(stock));

        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void insertIntoOrder(String customerName, String employeeName, String currentDate, String datePlus2, String shipperName, String shipWeight, String shipName,
            String shipAddress, String shipCity, String shipRegion, String shipPostalCode, String country) {
        Statement stm = null;
        try {
            stm = dbcon.createStatement();
            String customerid = companyID(customerName);
            int employeeid = employeeID(employeeName);
            int shipvia = shipperID(shipperName);
            stm.executeUpdate("INSERT INTO orders (CustomerID, EmployeeID, OrderDate, RequiredDate, ShipVia, Freight,"
                    + "ShipName, ShipAddress, ShipCity, ShipRegion, ShipPostalCode, ShipCountry) VALUES ('" + customerid + "','" + employeeid + "',"
                    + "'" + currentDate + "','" + datePlus2 + "','" + shipvia + "','" + shipWeight + "','" + shipName + "','" + shipAddress + "',"
                    + "'" + shipCity + "','" + shipRegion + "','" + shipPostalCode + "','" + country + "')");
            //"INSERT INTO `time_entry`(pid,tid,rid,tspend,description) VALUES (?, ?, ?, ?, ?)"
            JOptionPane.showMessageDialog(null, "Order was generated!");

        } catch (SQLException ex) {
            Logger.getLogger(MariaDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void insertProductIntoOrderDetails(String productName, String productID, String productStock,
            String productPrice, String productAmount, String productDiscount) {
        Statement stm = null;
        try {
            stm = dbcon.createStatement();
            String orderID = getOrderID();

            if (validatingDuplicateOrder(productID)) {
                int pStock = Integer.parseInt(getQuantityOrderDetail(orderID)) + Integer.parseInt(productAmount);
                stm.executeUpdate("UPDATE orderdetails SET Quantity = '" + pStock + "' WHERE '" + productID + "' = ProductID AND '" + orderID + "' = OrderID");
                //product subtraction
                int subtractionOfProduct = Integer.parseInt(productStock) - Integer.parseInt(productAmount);
                stm.executeUpdate("UPDATE products SET UnitsInStock = '" + subtractionOfProduct + "' WHERE '" + productID + "' = ProductID");
            } else {
                stm.executeUpdate("INSERT INTO orderdetails (OrderID, ProductID, UnitPrice, Quantity, Discount) VALUES ('" + orderID + "', '" + productID + "', '"
                        + productPrice + "', '" + productAmount + "'" + ", '" + productDiscount + "')");
                //product subtraction
                int subtractionOfProduct = Integer.parseInt(productStock) - Integer.parseInt(productAmount);
                stm.executeUpdate("UPDATE products SET UnitsInStock = '" + subtractionOfProduct + "' WHERE '" + productID + "' = ProductID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean validatingDuplicateOrder(String productID) {
        Statement stm = null;
        boolean foundExistingProduct = false;
        try {
            stm = dbcon.createStatement();
            String orderID = getOrderID();
            ResultSet rsO = stm.executeQuery("SELECT * FROM orderdetails where '" + orderID + "' = OrderID");
            while (rsO.next()) {
                if (productID.equals(rsO.getString(2))) {
//                        int pStock = Integer.parseInt(productStock) + Integer.parseInt(productQuantity);
//                        stm.executeUpdate("UPDATE products SET UnitsInStock = '" + pStock + "' WHERE '" + productID + "' = ProductID");
                    foundExistingProduct = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return foundExistingProduct;
    }

    public String getOrderID() {
        Statement stm = null;
        String orderID = "";
        try {
            stm = dbcon.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM orders ORDER BY OrderID DESC LIMIT 1");
            while (rs.next()) {
                orderID = rs.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderID;
    }

    public String getQuantityOrderDetail(String orderID) {
        Statement stm = null;
        String quantity = "";
        try {
            stm = dbcon.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM orderdetails where '" + orderID + "' = OrderID");
            while (rs.next()) {
                quantity = rs.getString("Quantity");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quantity;
    }
    
    public String[] retriveInfoForPDF(){
        Statement stm = null;
        String[] info = new String[15];
        try {
            stm = dbcon.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM orders ORDER BY OrderID DESC LIMIT 1");
            while (rs.next()) {
                //orderID, country, currentDate, datePlus2, employeeName, shipRegion, shipName, shipCity,
        //shipPostalCode, shipWeight, shipAddress, customerName, shipperName
                info[0] = rs.getString("OrderID");
                info[1] = rs.getString("ShipCountry");
                info[2] = rs.getString("OrderDate");
                info[3] = rs.getString("RequiredDate");
                info[4] = employeeName(rs.getString("EmployeeID"));
                info[5] = rs.getString("ShipRegion");
                info[6] = rs.getString("ShipName");
                info[7] = rs.getString("ShipCity");
                info[8] = rs.getString("ShipPostalCode");
                info[9] = rs.getString("Freight");
                info[10] = rs.getString("ShipAddress");
                info[11] = companyName(rs.getString("CustomerID"));
                info[12] = shipperName(rs.getString("ShipVia"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public void pdfCreator(String orderID, String country, String currentDate, String datePlus2, String employeeName, String shipRegion, String shipName,
            String shipCity, String shipPostalCode, String shipWeight, String shipAddress, String customerName, String shipperName) {
        Document documento = new Document();
        try {
            String ruta = System.getProperty("user.home");
            PdfWriter.getInstance(documento, new FileOutputStream(ruta + "/Desktop/Report.pdf"));
            documento.open();

            try {
                //img insert
                Image img = Image.getInstance("C:\\Users\\Tyler C\\Documents\\NetBeansProjects\\Proyecto_TeoriaBD_1\\src\\images\\northwindlogo.png");
                documento.add(img);
            } catch (BadElementException ex) {
                Logger.getLogger(MariaDB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MariaDB.class.getName()).log(Level.SEVERE, null, ex);
            };
            documento.add(new Paragraph("NORTHWIND \nHonduras CA Francisco Morazan (0801) \noffice@northwind.com \n\n\n"));
            String customerId = companyID(customerName);
            documento.add(new Paragraph("Customer Name: " + customerName + "    EmployeeID: " + customerId + "\n"));
            int employeeid = employeeID(employeeName);
            documento.add(new Paragraph("Employee Name: " + employeeName + "    EmployeeID: " + employeeid + "\n"));
            int shipid = shipperID(shipperName);
            documento.add(new Paragraph("Shipper Name: " + shipperName + "     ShipperId: " + shipid + "\n"));
            documento.add(new Paragraph("Date of order: " + currentDate + " \nRequiredDate: " + datePlus2 + "\n"));
            documento.add(new Paragraph("Shipping Region: " + shipRegion + "    Ship Country: " + country + "   Ship Postal Code: " + shipPostalCode + "\nShip City: " + shipCity + "\n"));
            documento.add(new Paragraph("Shipping Weight: " + shipWeight + "    Shipping Address: " + shipAddress + "\n"));
            

            PdfPTable tabla = new PdfPTable(5);
            tabla.addCell("Name of Product");
            tabla.addCell("ProductID");
            tabla.addCell("UnitPrice");
            tabla.addCell("Quantity");
            tabla.addCell("Discount");

            Statement stm = null, stm1 = null;
            try {
                stm = dbcon.createStatement();
//                stm1 = dbcon.createStatement();
                ResultSet rs = stm.executeQuery("Select * from orderdetails where '" + orderID + "' = OrderID");
                //companyID(customerName);
//                ResultSet rsC = stm1.executeQuery("Select * from customers where '" + customerName + "' = CustomerName");
//                while (rsC.next()) {
//                    System.out.println("Registry customerName: " + rsC.getString(1));
//                }
                if (rs.next()) {
                    do {
                        tabla.addCell(productName(rs.getString("ProductID")));
                        tabla.addCell(rs.getString("ProductID"));
                        tabla.addCell(rs.getString("UnitPrice"));
                        tabla.addCell(rs.getString("Quantity"));
                        tabla.addCell(rs.getString("Discount"));

                    } while (rs.next());
                    documento.add(tabla);
                }
            } catch (SQLException ex) {
                //Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }
            documento.close();
            JOptionPane.showMessageDialog(null, "Reporte creado");
        } catch (DocumentException | HeadlessException | FileNotFoundException e) {
        }
    }
    
    public int calculateSubTotal(String orderID){
        Statement stm = null;
        int quantity = 0, Uprice = 0;
        int subtotal = 0;
        try {
            stm = dbcon.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM orderdetails where '" + orderID + "' = OrderID");
            while (rs.next()) {
                quantity = Integer.parseInt(rs.getString("Quantity"));
                Uprice = Integer.parseInt(rs.getString("UnitPrice"));
                subtotal += quantity * Uprice;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subtotal;
    }

    public String companyID(String n) {
        Statement stm = null;
        ResultSet rsC;
        String companyid = "";
        try {
            stm = dbcon.createStatement();
            rsC = stm.executeQuery("Select * from customers where '" + n + "' = CompanyName;");
            while (rsC.next()) {
                companyid = rsC.getString(1);
                System.out.println("Registry customerName: " + rsC.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MariaDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return companyid;
    }
    
    public String companyName(String companyID) {
        Statement stm = null;
        ResultSet rsC;
        String companyid = "";
        try {
            stm = dbcon.createStatement();
            rsC = stm.executeQuery("Select * from customers where '" + companyID + "' = CustomerID;");
            while (rsC.next()) {
                companyid = rsC.getString("CompanyName");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MariaDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return companyid;
    }

    public int shipperID(String n) {
        Statement stm = null;
        ResultSet rsC;
        int shipperid = 0;
        try {
            stm = dbcon.createStatement();
            rsC = stm.executeQuery("Select * from shippers where '" + n + "' = CompanyName;");
            while (rsC.next()) {
                shipperid = Integer.parseInt(rsC.getString(1));
                System.out.println("Registry shipperID: " + rsC.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MariaDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return shipperid;
    }
    
    public String shipperName(String shipperID) {
        Statement stm = null;
        ResultSet rsC;
        String shipperName = "";
        try {
            stm = dbcon.createStatement();
            rsC = stm.executeQuery("Select * from shippers where '" + shipperID + "' = ShipperID;");
            while (rsC.next()) {
                shipperName = rsC.getString("Companyname");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MariaDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return shipperName;
    }

    public int productID(String n) {
        Statement stm = null;
        ResultSet rsC;
        int productID = 0;
        try {
            stm = dbcon.createStatement();
            rsC = stm.executeQuery("Select * from products where '" + n + "' = ProductName;");
            while (rsC.next()) {
                productID = Integer.parseInt(rsC.getString(1));
                System.out.println("Registry productID: " + rsC.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MariaDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return productID;
    }
    
    public String productName(String productID){
        Statement stm = null;
        ResultSet rsC;
        String productname = "";
        try {
            stm = dbcon.createStatement();
            rsC = stm.executeQuery("Select * from products where '" + productID + "' = ProductID");
            while (rsC.next()) {
                productname = rsC.getString("ProductName");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MariaDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return productname;
    }

    public int employeeID(String n) {
        Statement stm = null;
        int employeeid = 0;
        ResultSet rsC;
        String firstName = "", lastName = "";
        String fullname[] = n.split(" ");
        firstName = fullname[1];
        lastName = fullname[0];
//        System.out.println("firstName: " + firstName);
//        System.out.println("lastName: " + lastName);
        try {
            stm = dbcon.createStatement();
            rsC = stm.executeQuery("Select * from employees where '" + firstName + "' = FirstName");
            while (rsC.next()) {
                employeeid = Integer.parseInt(rsC.getString(1));
                System.out.println("Registry firstName: " + rsC.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MariaDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return employeeid;
    }
    
    public String employeeName(String employeeID) {
        Statement stm = null;
        ResultSet rsC;
        String firstName = "", lastName = "";
        String fullName = "";
//        System.out.println("firstName: " + firstName);
//        System.out.println("lastName: " + lastName);
        try {
            stm = dbcon.createStatement();
            rsC = stm.executeQuery("Select * from employees where '" + employeeID + "' = EmployeeID");
            while (rsC.next()) {
                firstName = rsC.getString("FirstName");
                lastName = rsC.getString("LastName");
            }
            fullName = lastName +" "+ firstName;
        } catch (SQLException ex) {
            Logger.getLogger(MariaDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fullName;
    }

    public void disconnectNorthwind() {
        try {
            dbcon.close();
            System.out.println("Disconnected database successfully...");
        } catch (Exception e) {
        }
    }

    public Connection getDbcon() {
        return dbcon;
    }

    public void setDbcon(Connection dbcon) {
        this.dbcon = dbcon;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDB_URL() {
        return DB_URL;
    }

    public void setDB_URL(String DB_URL) {
        this.DB_URL = DB_URL;
    }

    public DefaultComboBoxModel getCombo_model_company() {
        return combo_model_company;
    }

    public void setCombo_model_company(DefaultComboBoxModel combo_model_company) {
        this.combo_model_company = combo_model_company;
    }

    public String getProductName() {
        return productName.getText();
    }

    public void setProductName(JTextField productName) {
        this.productName = productName;
    }

    public String getProductstock() {
        return productstock.getText();
    }

    public void setProductstock(JTextField productstock) {
        this.productstock = productstock;
    }

    public String getProductID() {
        return productID.getText();
    }

    public void setProductID(JTextField productID) {
        this.productID = productID;
    }

    public String getProductPrice() {
        return productPrice.getText();
    }

    public void setProductPrice(JTextField productPrice) {
        this.productPrice = productPrice;
    }

}
