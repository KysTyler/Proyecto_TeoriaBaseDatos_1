package proyecto_teoriabd_1;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.HeadlessException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Tyler C
 */
public class MariaDB {

    public Connection dbcon;

    private String user = "root";
    private String password = "root";
    private String DB_URL = "jdbc:mysql://localhost:3306/northwind";

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

    public void pdfCreator() {
        Document documento = new Document();
        try {
            String ruta = System.getProperty("user.home");
            PdfWriter.getInstance(documento, new FileOutputStream(ruta + "/Desktop/Report.pdf"));
            documento.open();

            PdfPTable tabla = new PdfPTable(2);
            tabla.addCell("OrderID");
            tabla.addCell("ProductID");
//            tabla.addCell("UnitPrice");
//            tabla.addCell("Quantity");
//            tabla.addCell("Discount");

            Statement stm = null;
            try {
                stm = dbcon.createStatement();
                ResultSet rs = stm.executeQuery("Select * from orderdetails");
                if(rs.next()){
                    do {                        
                        tabla.addCell(Integer.toString(rs.getInt(1)));
                        tabla.addCell(Integer.toString(rs.getInt(2)));
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

    public void disconnectNorthwind() {
        try {
            dbcon.close();
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
}
