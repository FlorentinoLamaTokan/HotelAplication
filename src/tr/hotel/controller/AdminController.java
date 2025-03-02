
package tr.hotel.controller;

import tr.hotel.model.Admin;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import javax.swing.table.DefaultTableModel;
import static tr.hotel.controller.Koneksi.con;
import tr.hotel.model.Kamar;
import tr.hotel.model.Pelanggan;
import tr.hotel.model.Transaksi;

public class AdminController {
    public Statement stm;
   
    
    
    public AdminController(){
        Koneksi db = new Koneksi();
        db.config();
        stm = db.stm;
    }
    
    public boolean cekLogin(String username, String password) {
       Admin ad = new Admin(0, username, password, "");
        

        try {
            String sql = "SELECT * FROM tbadmin WHERE username = ? AND password = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, ad.getUsername());
            pstmt.setString(2, ad.getPassword());
            ResultSet res = pstmt.executeQuery();
            
            
          
                if (res.next()) {
                    // Membuat objek Admin dari hasil query
                    return true;
                }
            
        } catch (Exception e) {
            System.out.println("Query Gagal: " + e);
        }

        return false;
    }
    
    DefaultTableModel dtm = new DefaultTableModel();
    
    public DefaultTableModel createTablePesan(){
        dtm.addColumn("ID Transaksi");
        dtm.addColumn("Nama Lengkap");
        dtm.addColumn("Nomor Kamar");
        dtm.addColumn("Tipe Kamar");
        dtm.addColumn("Check Out");
        
        return dtm;
    }
    

    
    public void perbaruiKamar(String tipeKamar, double harga){
        Kamar kmr = new Kamar();
        kmr.setTipe_kamar(tipeKamar);
        kmr.setHarga(harga);
        
        try {
            String sqlUpdate = "UPDATE tbkamar "+
                               "SET harga_permalam = ?"+
                               "WHERE tipe_kamar = ?";
            PreparedStatement pstmt = con.prepareStatement(sqlUpdate);
            pstmt.setString(1, String.valueOf(kmr.getHarga()));
            pstmt.setString(2, kmr.getTipe_kamar());
            int rowsUpdated = pstmt.executeUpdate();
            
            if (rowsUpdated > 0) {
                System.out.println("Data berhasil diperbarui.");
            } else {
                System.out.println("Data tidak ditemukan atau gagal diperbarui.");
            }
        } catch (Exception e) {
            System.out.println("Query gagal..."+e);
        }
    }
    
    
    DefaultTableModel dtm2 = new DefaultTableModel();
    
    public DefaultTableModel createTableDataAkun(){
        dtm2.addColumn("Id Pelanggan");
        dtm2.addColumn("Username");
        dtm2.addColumn("Password");
        dtm2.addColumn("Nama Lengkap");
        dtm2.addColumn("Nomor Hp");
        dtm2.addColumn("Email");

        return dtm2;
    }
    
    public void lihatDataAkun(){
        dtm2.getDataVector().removeAllElements(); 
        dtm2.fireTableDataChanged(); 
        
        try {
            String sqlLihatPesanan = "SELECT * FROM tbpelanggan";
            PreparedStatement pstmtGetData = con.prepareStatement(sqlLihatPesanan);
            ResultSet resDataPesanan = pstmtGetData.executeQuery();
            
            
            while(resDataPesanan.next()){
                Object[] obj = new Object[6];
                obj[0] = resDataPesanan.getString("id_pelanggan");
                obj[1] = resDataPesanan.getString("username");
                obj[2] = resDataPesanan.getString("password");
                obj[3] = resDataPesanan.getString("nama_lengkap");
                obj[4] = resDataPesanan.getString("no_hp");
                obj[5] = resDataPesanan.getString("email");
                dtm2.addRow(obj);
            }
        } catch (Exception e) {
            System.out.println("GAGAL QUERY..."+e);
        }
    }
    
    public void cekUsername(String username) {
    
        Pelanggan pl = new Pelanggan(0, username, "", "", "", ""); 
        dtm2.getDataVector().removeAllElements(); 
        dtm2.fireTableDataChanged(); 

        try {

            String sqlCariUsername = "SELECT * FROM tbpelanggan WHERE username = ?";
            PreparedStatement pstmtGetData = con.prepareStatement(sqlCariUsername);
            pstmtGetData.setString(1, pl.getUsername());
            ResultSet resDataAkun = pstmtGetData.executeQuery();

            if (resDataAkun.next()) {

                int idPelanggan = resDataAkun.getInt("id_pelanggan");
                String namaLengkap = resDataAkun.getString("nama_lengkap");
                String password = resDataAkun.getString("password");
                String noHp = resDataAkun.getString("no_hp");
                String email = resDataAkun.getString("email");


                pl = new Pelanggan(idPelanggan, username, password, namaLengkap, noHp, email);


                Object[] obj = new Object[6];
                obj[0] = idPelanggan; 
                obj[1] = username; 
                obj[2] = password; 
                obj[3] = namaLengkap; 
                obj[4] = noHp; 
                obj[5] = email; 
                dtm2.addRow(obj);
            }
        } catch (Exception e) {
            System.out.println("QUERY ERROR..." + e);
        }
    }

    
    DefaultTableModel dtm1 = new DefaultTableModel();
    
    public DefaultTableModel createTableLaporan(){
        dtm1.addColumn("Nama Lengkap");
        dtm1.addColumn("Tipe Kamar");
        dtm1.addColumn("Nomor Kamar");
        dtm1.addColumn("Check In");
        dtm1.addColumn("Check Out");
        dtm1.addColumn("Metode Pembayaran");
        dtm1.addColumn("Total Bayaran");
        dtm1.addColumn("Nomor Hp");
        dtm1.addColumn("Email");
        
        
        return dtm1;
    }
    
    public double laporanRentang(String tglAwal, String tglAkhir) {
         dtm1.getDataVector().removeAllElements(); 
         dtm1.fireTableDataChanged();
         double totalPendapatan = 0;
         
        String sql = "SELECT nama_lengkap, tipe_kamar, no_kamar, check_in, check_out, metode_pembayaran, total_harga, no_hp, email "+
                     "FROM tbpesan "+
                     "WHERE check_in BETWEEN ? AND ?";
        
        
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, tglAwal); 
            pstmt.setString(2, tglAkhir);  

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Object[] obj = new Object[9];
                obj[0] = rs.getString("nama_lengkap");
                obj[1] = rs.getString("tipe_kamar");
                obj[2] = rs.getString("no_kamar");
                obj[3] = rs.getString("check_in");
                obj[4] = rs.getString("check_out");
                obj[5] = rs.getString("metode_pembayaran");
                obj[6] = rs.getString("total_harga");
                obj[7] = rs.getString("no_hp");
                obj[8] = rs.getString("email");
                dtm1.addRow(obj); 
                
                try {
                    totalPendapatan += Double.parseDouble(rs.getString("total_harga"));
                    System.out.println("Pendapatan: "+totalPendapatan);
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing total_harga: " + e.getMessage());
                }
                
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
     
        return totalPendapatan;
    } 
    
    
    public double lihatLaporan() {
        dtm1.getDataVector().removeAllElements();
        dtm1.fireTableDataChanged();
        String sql = "SELECT * FROM tbpesan";
        double totalPendapatan = 0;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Object[] obj = new Object[9];
                obj[0] = rs.getString("nama_lengkap");
                obj[1] = rs.getString("tipe_kamar");
                obj[2] = rs.getString("no_kamar");
                obj[3] = rs.getString("check_in");
                obj[4] = rs.getString("check_out");
                obj[5] = rs.getString("metode_pembayaran");
                obj[6] = rs.getString("total_harga");
                obj[7] = rs.getString("no_hp");
                obj[8] = rs.getString("email");
                dtm1.addRow(obj); 

                
                try {
                    totalPendapatan += Double.parseDouble(rs.getString("total_harga"));
                    System.out.println("Pendapatan: "+totalPendapatan);
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing total_harga: " + e.getMessage());
                }
            }
            
            System.out.println("Total Pendapatan: " + totalPendapatan);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return totalPendapatan;
    }
    
    
}
