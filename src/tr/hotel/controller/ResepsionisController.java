
package tr.hotel.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
import static tr.hotel.controller.Koneksi.con;
import tr.hotel.model.Resepsionis;
import tr.hotel.model.Transaksi;

public class ResepsionisController {
    public Statement stm;
    
    public ResepsionisController(){
        //objek koneksi
        Koneksi db = new Koneksi();
        db.config();
        stm = db.stm;
    }
    
    public boolean cekLogin(String user, String pass){
    
        Resepsionis rs = new Resepsionis(0,user, pass,"");
        
        try {
            String sqlCek = "SELECT * FROM tbresepsionis WHERE username = ? AND password = ?";
        PreparedStatement pstmt = con.prepareStatement(sqlCek);

            pstmt.setString(1, rs.getUsername()); 
            pstmt.setString(2, rs.getPassword()); 

            ResultSet res = pstmt.executeQuery();

            if (res.next()) {
                return true;
            }

        } catch (Exception e) {

            System.out.println("Query Gagal..."+e);
        }

        return false;
    }
    
    public void checkOut(int idTransaksi) {
        Transaksi tr = new Transaksi();
        tr.setId_transaksi(idTransaksi);
        try {
            
            String sqlGetKamar = "SELECT no_kamar FROM tbpesan WHERE id_transaksi = ?";
            PreparedStatement pstmtGetKamar = con.prepareStatement(sqlGetKamar);
            pstmtGetKamar.setInt(1, tr.getId_transaksi());

            ResultSet res = pstmtGetKamar.executeQuery();

           
            if (res.next()) {
                String noKamar = res.getString("no_kamar");

                
                String sqlUpdateKamar = "UPDATE tbkamar SET status = 'Tersedia' WHERE no_kamar = ?";
                PreparedStatement pstmtUpdateKamar = con.prepareStatement(sqlUpdateKamar);
                pstmtUpdateKamar.setString(1, noKamar);

                int rowsUpdated = pstmtUpdateKamar.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Kamar dengan no_kamar " + noKamar + " berhasil di-update menjadi 'Tersedia'.");
                } else {
                    System.out.println("Tidak ada kamar yang diperbarui. Periksa no_kamar.");
                }
            } else {
                System.out.println("ID Transaksi tidak ditemukan di tbpesan.");
            }
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    DefaultTableModel dtm = new DefaultTableModel();
    
    public DefaultTableModel createTableCheckIn(){
        dtm.addColumn("Id Transaksi");
        dtm.addColumn("Nama Pelanggan");
        dtm.addColumn("Nomor Kamar");
        dtm.addColumn("Tipe Kamar");
        dtm.addColumn("Tanggal Check In");
        dtm.addColumn("Status");
  
        return dtm;
    }
    
    public void lihatCheckIn(){
        dtm.getDataVector().removeAllElements(); 
        dtm.fireTableDataChanged(); 
        
        try {
            String sqlLihatPesanan = "SELECT * FROM tbpesan WHERE sts_checkin = 'No'";
            PreparedStatement pstmtGetData = con.prepareStatement(sqlLihatPesanan);
            ResultSet res = pstmtGetData.executeQuery();
            
            while(res.next()){
                Object[] obj = new Object[6];
                obj[0] = res.getString("id_transaksi");
                obj[1] = res.getString("nama_lengkap");
                obj[2] = res.getString("no_kamar");
                obj[3] = res.getString("tipe_kamar");
                obj[4] = res.getString("check_in");
                obj[5] = res.getString("sts_checkin");
                dtm.addRow(obj);
            }
        } catch (Exception e) {
            System.out.println("GAGAL QUERY..."+e);
        }
    }
    
    public void cariCheckIn(int idTransaksi, String tanggalMasuk){
        Transaksi tr = new Transaksi();
        tr.setId_transaksi(idTransaksi);
        tr.setCheck_in(tanggalMasuk);
        
        try {
            dtm.getDataVector().removeAllElements(); 
            dtm.fireTableDataChanged(); 
            
            String sqlCariTransaksi = "SELECT id_transaksi, nama_lengkap, no_kamar, tipe_kamar, check_in, sts_checkin " +
                                      "FROM tbpesan WHERE id_transaksi = ? OR check_in = ?";
            PreparedStatement pstmtGetCariTransaksi = con.prepareStatement(sqlCariTransaksi);
            pstmtGetCariTransaksi.setInt(1, tr.getId_transaksi());
            pstmtGetCariTransaksi.setString(2, tr.getCheck_in());

            ResultSet resIdTransaksi = pstmtGetCariTransaksi.executeQuery();
            System.out.println("Query: " + sqlCariTransaksi);
            System.out.println("ID Transaksi: " + idTransaksi);
            
            boolean dataDitemukan = false;
            while (resIdTransaksi.next()) {
                Object[] obj = new Object[6];
                obj[0] = resIdTransaksi.getString("id_transaksi");
                obj[1] = resIdTransaksi.getString("nama_lengkap");
                obj[2] = resIdTransaksi.getString("no_kamar");
                obj[3] = resIdTransaksi.getString("tipe_kamar");
                obj[4] = resIdTransaksi.getString("check_in");
                obj[5] = resIdTransaksi.getString("sts_checkin");
                dtm.addRow(obj); 
                dataDitemukan = true;
            }

            if (!dataDitemukan) {
                System.out.println("ID Transaksi " + idTransaksi + " tidak ditemukan.");
            }

        } catch (Exception e) {
            System.out.println("Error query: " + e.getMessage());
        }
    }
    
    public void updateStsCheckIn(int idTransaksi, String tanggalMasuk){
        Transaksi tr = new Transaksi();
        tr.setId_transaksi(idTransaksi);
        tr.setCheck_in(tanggalMasuk);
        
        try {
            System.out.println("id: "+tr.getId_transaksi());
            
            String sqlUpdate = "UPDATE tbpesan SET sts_checkin = 'Yes' WHERE id_transaksi = ? AND check_in = ?";
            PreparedStatement pstmtUpdateStatus = con.prepareStatement(sqlUpdate);
            pstmtUpdateStatus.setInt(1, tr.getId_transaksi());
            pstmtUpdateStatus.setString(2, tr.getCheck_in());
            
            pstmtUpdateStatus.executeUpdate();
            
            
        } catch (Exception e) {
            System.out.println("Query Gagal "+e);
        }
        
    }
    
    DefaultTableModel dtm1 = new DefaultTableModel();
    
    public DefaultTableModel createTableCheckOut(){
        dtm1.addColumn("Id Transaksi");
        dtm1.addColumn("Nama Pelanggan");
        dtm1.addColumn("Nomor Kamar");
        dtm1.addColumn("Tipe Kamar");
        dtm1.addColumn("Tanggal Check Out");
        dtm1.addColumn("Status");
  
        return dtm1;
    }
    
    public void lihatCheckOut(){
        dtm1.getDataVector().removeAllElements(); 
        dtm1.fireTableDataChanged(); 
        
        try {
            String sqlLihatPesanan = "SELECT * FROM tbpesan WHERE sts_checkin = 'Yes' AND sts_checkout = 'No'";
            PreparedStatement pstmtGetData = con.prepareStatement(sqlLihatPesanan);
            ResultSet resDataPesanan = pstmtGetData.executeQuery();
            
            while(resDataPesanan.next()){
                Object[] obj = new Object[6];
                obj[0] = resDataPesanan.getString("id_transaksi");
                obj[1] = resDataPesanan.getString("nama_lengkap");
                obj[2] = resDataPesanan.getString("no_kamar");
                obj[3] = resDataPesanan.getString("tipe_kamar");
                obj[4] = resDataPesanan.getString("check_out");
                obj[5] = resDataPesanan.getString("sts_checkout");
                dtm1.addRow(obj);
            }
        } catch (Exception e) {
            System.out.println("GAGAL QUERY..."+e);
        }
    }
    
    public void cariCheckOut(String noKamar, String tanggalKeluar){
        Transaksi tr = new Transaksi();
        tr.setNo_kamar(noKamar);
        tr.setCheck_out(tanggalKeluar);
        
        try {
            dtm1.getDataVector().removeAllElements(); 
            dtm1.fireTableDataChanged(); 
            
            String sqlCariTransaksi = "SELECT id_transaksi, nama_lengkap, no_kamar, tipe_kamar, check_out, sts_checkout " +
                                      "FROM tbpesan WHERE nomor_kamar = ? OR check_out = ?";
            PreparedStatement pstmtGetCariTransaksi = con.prepareStatement(sqlCariTransaksi);
            pstmtGetCariTransaksi.setString(1, tr.getNo_kamar());
            pstmtGetCariTransaksi.setString(2, tr.getCheck_out());

            ResultSet resIdTransaksi = pstmtGetCariTransaksi.executeQuery();
            System.out.println("Kamar : " + tr.getNo_kamar());
            
            boolean dataDitemukan = false;
            while (resIdTransaksi.next()) {
                Object[] obj = new Object[6];
                obj[0] = resIdTransaksi.getString("id_transaksi");
                obj[1] = resIdTransaksi.getString("nama_lengkap");
                obj[2] = resIdTransaksi.getString("no_kamar");
                obj[3] = resIdTransaksi.getString("tipe_kamar");
                obj[4] = resIdTransaksi.getString("check_out");
                obj[5] = resIdTransaksi.getString("sts_checkout");
                dtm1.addRow(obj); 
                dataDitemukan = true;
            }

        } catch (Exception e) {
            System.out.println("Error query: " + e.getMessage());
        }
    }
    
    public void updateStsCheckOut(String noKamar, String tanggalKeluar){
        Transaksi tr = new Transaksi();
        tr.setNo_kamar(noKamar);
        tr.setCheck_out(tanggalKeluar);
        
        try {
            
            
            String sqlUpdate = "UPDATE tbpesan SET sts_checkout = 'Yes' WHERE no_kamar = ? AND check_out = ? AND sts_checkin = 'Yes'";
            PreparedStatement pstmtUpdateStatus = con.prepareStatement(sqlUpdate);
            pstmtUpdateStatus.setString(1, tr.getNo_kamar());
            pstmtUpdateStatus.setString(2, tr.getCheck_out());
            
            pstmtUpdateStatus.executeUpdate();
            
            
        } catch (Exception e) {
            System.out.println("Query Gagal "+e);
        }
        
    }
}
