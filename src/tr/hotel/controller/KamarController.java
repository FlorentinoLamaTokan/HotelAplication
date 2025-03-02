
package tr.hotel.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import tr.hotel.model.Kamar;
import javax.swing.table.DefaultTableModel;
import static tr.hotel.controller.Koneksi.con;

public class KamarController {
    public Statement stm;
   
    
    DefaultTableModel dtm = new DefaultTableModel();
    
    public KamarController(){
        //objek koneksi
        Koneksi db = new Koneksi();
        db.config();
        stm = db.stm;
    }
    
    public DefaultTableModel createTableKamar(){
        dtm.addColumn("Tipe Kamar");
        dtm.addColumn("Harga Permalam");
        
        return dtm;
    }
    
    public void lihatKamar() {
        try {
            String sql = "SELECT DISTINCT tipe_kamar, harga_permalam FROM tbkamar";

            ResultSet res = stm.executeQuery(sql);


            dtm.getDataVector().removeAllElements();
            dtm.fireTableDataChanged();


            while (res.next()) {
                Object[] obj = new Object[2]; 
                obj[0] = res.getString("tipe_kamar");
                obj[1] = res.getDouble("harga_permalam");
                dtm.addRow(obj);
            }
        } catch (Exception e) {
            System.out.println("QUERY GAGAL: " + e);
        }
    }

    public void tambahKamar(String noKamar, String tipeKamar, double harga){
        Kamar kmr = new Kamar();
        kmr.setNo_kamar(noKamar);
        kmr.setTipe_kamar(tipeKamar);
        kmr.setHarga(harga);
        
        try {
            String sqlTambah = "INSERT INTO tbkamar (no_kamar, tipe_kamar, harga_permalam) " +
                                "VALUES (?, ?, ?)";
            
            PreparedStatement pstmt = con.prepareStatement(sqlTambah);

            
            pstmt.setString(1, kmr.getNo_kamar());
            pstmt.setString(2, kmr.getTipe_kamar());
            pstmt.setDouble(3, kmr.getHarga());

            
            pstmt.executeUpdate();
            System.out.println("Data berhasil ditambahkan");
            
        } catch (Exception e) {
            System.out.println("Gagal Query...Tambah Kamar"+e);
        }
    }
    
    public void hapusKamar(String noKamar) {
        Kamar kmr = new Kamar();
        kmr.setNo_kamar(noKamar);
        try {
            String sqlDelete = "DELETE FROM tbkamar WHERE no_kamar = ?";
            PreparedStatement pstmt = con.prepareStatement(sqlDelete);

            pstmt.setString(1, kmr.getNo_kamar());

            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Kamar nomor " +kmr.getNo_kamar()  + " berhasil dihapus.");
            } else {
                System.out.println("Kamar nomor " + kmr.getNo_kamar()  + " tidak ditemukan.");
            }
        } catch (Exception e) {
            System.out.println("query gagal "+e);
        }
    }

   
}

