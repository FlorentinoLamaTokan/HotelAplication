package tr.hotel.controller;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;

public class Koneksi {
    public static Connection con;
    public static Statement stm;

    public void config() {
        try {
            //localhost:3306
            String url = "jdbc:mysql://localhost:3306/dbhotel"; // Tambahkan :3306 untuk port
            String user = "root";
            String pass = ""; // Kosong jika tanpa password

            Class.forName("com.mysql.cj.jdbc.Driver"); // Driver untuk MySQL 8+
            con = DriverManager.getConnection(url, user, pass);
            stm = con.createStatement();

            System.out.println("connection successful");
        } catch (Exception e) {
            System.out.println("connection failed");
        }
    }
}
