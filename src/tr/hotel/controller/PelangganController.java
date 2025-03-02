
package tr.hotel.controller;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.swing.table.DefaultTableModel;
import static tr.hotel.controller.Koneksi.con;
import tr.hotel.model.Kamar;
import tr.hotel.model.Pelanggan;
import tr.hotel.model.Transaksi;


public class PelangganController {
    public Statement stm;
    private double totalHarga;

    public double getTotalHarga() {
        return totalHarga;
    }
    
   
    
    public PelangganController(){
        Koneksi db = new Koneksi();
        db.config();
        stm = db.stm;
    }
    
     public boolean cekLogin(String username, String password) {
        
        Pelanggan pl = new Pelanggan(0,username,password,"","","");

        try {
            String sql = "SELECT * FROM tbpelanggan WHERE username = ? AND password = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setString(1, pl.getUsername()); 
            pstmt.setString(2, pl.getPassword()); 

            ResultSet res = pstmt.executeQuery();

            if (res.next()) {
                return true;
            }

        } catch (Exception e) {

            System.out.println("Query Gagal..."+e);
        }

        return false;  
    }



     public void insertDataPelanggan(String user, String pass, String nama, String no_hp, String email) {
        Pelanggan pl = new Pelanggan(0, user, pass, nama, no_hp, email); 

        try {
           
            String sql = "INSERT INTO tbpelanggan (username, password, nama_lengkap, no_hp, email) " +
                         "VALUES (?, ?, ?, ?, ?)";

            
            PreparedStatement pstmt = con.prepareStatement(sql);

            
            pstmt.setString(1, pl.getUsername());
            pstmt.setString(2, pl.getPassword());
            pstmt.setString(3, pl.getNama());
            pstmt.setString(4, pl.getNoHp());
            pstmt.setString(5, pl.getEmail());

            
            pstmt.executeUpdate();
            System.out.println("Data berhasil ditambahkan");

        } catch (Exception e) {
            
            System.out.println("Query Gagal: " + e);
        }
    }
    
     
    public void pesanKamar(String username, String tipe_kamar, String check_in, String check_out, String mtdPembayaran) {
        Pelanggan pl = new Pelanggan(0, username, "", "", "", ""); 
        Kamar kmr = new Kamar();
        Transaksi tr = new Transaksi();

        kmr.setTipe_kamar(tipe_kamar);
        tr.setCheck_in(check_in);
        tr.setCheck_out(check_out);
        tr.setMtdPembayaran(mtdPembayaran);

        try {
            String sqlGetPelanggan = "SELECT id_pelanggan, nama_lengkap, no_hp, email FROM tbpelanggan WHERE username = ?";
            PreparedStatement pstmtGetPelanggan = con.prepareStatement(sqlGetPelanggan);
            pstmtGetPelanggan.setString(1, pl.getUsername());

            ResultSet res = pstmtGetPelanggan.executeQuery();

            if (res.next()) {
                int id = res.getInt("id_pelanggan");
                String nama = res.getString("nama_lengkap");
                String noHp = res.getString("no_hp");
                String email = res.getString("email");

                pl = new Pelanggan(id, "", "", nama, noHp, email);

                // Mengambil harga kamar berdasarkan tipe kamar
                String sqlGetHargaKamar = "SELECT harga_permalam FROM tbkamar WHERE tipe_kamar = ?";
                PreparedStatement pstmtGetHargaKamar = con.prepareStatement(sqlGetHargaKamar);
                pstmtGetHargaKamar.setString(1, kmr.getTipe_kamar());
                ResultSet resHarga = pstmtGetHargaKamar.executeQuery();

                if (resHarga.next()) {
                    kmr.setHarga(resHarga.getDouble("harga_permalam"));

                    LocalDate checkinDate = LocalDate.parse(tr.getCheck_in());
                    LocalDate checkoutDate = LocalDate.parse(tr.getCheck_out());
                    long totalHari = ChronoUnit.DAYS.between(checkinDate, checkoutDate);
                    double totalHarga = totalHari * kmr.getHarga();
                    tr.setTotalHarga(totalHarga);

                    // Mengambil nomor kamar berdasarkan tipe kamar tanpa kolom status
                    String sqlGetNoKamar = "SELECT no_kamar FROM tbkamar WHERE tipe_kamar = ? LIMIT 1";
                    PreparedStatement pstmtGetNoKamar = con.prepareStatement(sqlGetNoKamar);
                    pstmtGetNoKamar.setString(1, kmr.getTipe_kamar());
                    ResultSet resNoKamar = pstmtGetNoKamar.executeQuery();

                    if (resNoKamar.next()) {
                        kmr.setNo_kamar(resNoKamar.getString("no_kamar"));

                        // Menyimpan transaksi ke database
                        String sqlInsertTransaksi = "INSERT INTO tbpesan (id_pelanggan, nama_lengkap, tipe_kamar, check_in, check_out, metode_pembayaran, total_harga, no_hp, email, no_kamar) " +
                                                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        PreparedStatement pstmtInsertTransaksi = con.prepareStatement(sqlInsertTransaksi);
                        pstmtInsertTransaksi.setInt(1, pl.getId());
                        pstmtInsertTransaksi.setString(2, pl.getNama());
                        pstmtInsertTransaksi.setString(3, kmr.getTipe_kamar());
                        pstmtInsertTransaksi.setString(4, tr.getCheck_in());
                        pstmtInsertTransaksi.setString(5, tr.getCheck_out());
                        pstmtInsertTransaksi.setString(6, tr.getMtdPembayaran());
                        pstmtInsertTransaksi.setDouble(7, tr.getTotalHarga());
                        pstmtInsertTransaksi.setString(8, pl.getNoHp());
                        pstmtInsertTransaksi.setString(9, pl.getEmail());
                        pstmtInsertTransaksi.setString(10, kmr.getNo_kamar());

                        pstmtInsertTransaksi.executeUpdate();

                        System.out.println("Data transaksi berhasil ditambahkan.");
                        System.out.println("Nomor kamar yang dipesan: " + kmr.getNo_kamar());
                        System.out.println("Total harga yang harus dibayar: " + tr.getTotalHarga());
                    } else {
                        System.out.println("Tidak ada kamar yang tersedia untuk tipe " + kmr.getTipe_kamar());
                    }
                } else {
                    System.out.println("Tipe kamar " + kmr.getTipe_kamar() + " tidak ditemukan.");
                }
            } else {
                System.out.println("Pelanggan dengan username " + pl.getUsername() + " tidak ditemukan.");
            }
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan: " + e);
        }
    }



    
    DefaultTableModel dtm = new DefaultTableModel();
    
    public DefaultTableModel createTable(){
        dtm.addColumn("ID Transaksi");
        dtm.addColumn("Nama Lengkap");
        dtm.addColumn("Tipe Kamar");
        dtm.addColumn("Check In");
        dtm.addColumn("Check Out");
        
        return dtm;
    }
    
    public void lihatPesanan(String username) {
        Pelanggan pl = new Pelanggan(0, username, "", "", "", "");
        try {
            dtm.getDataVector().removeAllElements();
            dtm.fireTableDataChanged();

            String sql = "SELECT tp.id_transaksi, tp.nama_lengkap, tp.tipe_kamar, tp.check_in, tp.check_out " +
                         "FROM tbpesan tp " +
                         "JOIN tbpelanggan tpl ON tp.id_pelanggan = tpl.id_pelanggan " +
                         "WHERE tpl.username = ? AND tp.sts_checkout = 'No'";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, pl.getUsername());  

            ResultSet res = pstmt.executeQuery();

            boolean dataDitemukan = false;


            while (res.next()) {
                Object[] obj = new Object[5];
                obj[0] = res.getString("id_transaksi");
                obj[1] = res.getString("nama_lengkap");
                obj[2] = res.getString("tipe_kamar");
                obj[3] = res.getString("check_in");
                obj[4] = res.getString("check_out");
                dtm.addRow(obj); 
                dataDitemukan = true; 
            }

            if (!dataDitemukan) {
                System.out.println("Tidak ada pesanan untuk username: " + username);
            }

        } catch (Exception e) {
            System.out.println("QUERY GAGAL....." + e);
        }
    }

    
    public Pelanggan editAkun(String username) {
        Pelanggan pl = new Pelanggan(0, username, "", "", "", "");

        try {
            String sqlGetData = "SELECT * FROM tbpelanggan WHERE username = ?";
            PreparedStatement pstmt = con.prepareStatement(sqlGetData);
            pstmt.setString(1, username);

            ResultSet res = pstmt.executeQuery();

            if (res.next()) {
                String user = res.getString("username");
                String pass = res.getString("password");
                String nama =res.getString("nama_lengkap");
                String noHp = res.getString("no_hp");
                String email = res.getString("email");

                pl = new Pelanggan(0,user,pass,nama,noHp,email);
            } else {
                System.out.println("Data tidak ditemukan untuk username: " + username);
            }
        } catch (Exception e) {
            System.out.println("Query gagal... " + e.getMessage());
        }

        return pl;
    }


   public void simpanData(String username, String password, String nama, String noHp, String email, String oldUsername) {
        Pelanggan pl = new Pelanggan(0, username, password, nama, noHp, email); 

        try {
            String sqlUpdate = "UPDATE tbpelanggan " +
                               "SET username = ?, password = ?, nama_lengkap = ?, no_hp = ?, email = ? " +
                               "WHERE username = ?";

            PreparedStatement pstmt = con.prepareStatement(sqlUpdate);

            pstmt.setString(1, pl.getUsername());
            pstmt.setString(2, pl.getPassword());
            pstmt.setString(3, pl.getNama());
            pstmt.setString(4, pl.getNoHp());
            pstmt.setString(5, pl.getEmail());
            pstmt.setString(6, oldUsername); 
            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Data berhasil diperbarui.");
            } else {
                System.out.println("Data tidak ditemukan atau gagal diperbarui.");
            }

        } catch (Exception e) {
            System.out.println("Query gagal... " + e);
        }
    }

    
    public void hapusAkun(String username) {
        try {
            String sqlDelete = "DELETE FROM tbpelanggan WHERE username = ?";
            PreparedStatement pstmt = con.prepareStatement(sqlDelete);

            pstmt.setString(1, username);

            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Akun dengan username " + username + " berhasil dihapus.");
            } else {
                System.out.println("Akun dengan username " + username + " tidak ditemukan.");
            }
        } catch (Exception e) {
            System.out.println("Error saat menghapus akun: " + e.getMessage());
        }
    }
    
    public boolean cekKamar(String tipeKamar, String checkIn, String checkOut) {
        Transaksi tr = new Transaksi();
        tr.setTipe_kamar(tipeKamar);
        tr.setCheck_in(checkIn);
        tr.setCheck_out(checkOut);
        try {
            // Query untuk menghitung total kamar dan kamar yang sudah dipesan
            String sqlCekKamar = "SELECT " +
                                 "    (SELECT COUNT(*) FROM tbkamar WHERE tipe_kamar = ?) - " +
                                 "    COALESCE(( " +
                                 "        SELECT COUNT(*) " +
                                 "        FROM tbpesan " +
                                 "        WHERE tipe_kamar = ? " +
                                 "        AND ( " +
                                 "            (check_in <= ? AND check_out >= ?) OR " +
                                 "            (check_in <= ? AND check_out >= ?) OR " +
                                 "            (check_in >= ? AND check_out <= ?) " +
                                 "        ) " +
                                 "    ), 0) AS sisa_kamar, " +
                                 "    (SELECT harga_permalam FROM tbkamar WHERE tipe_kamar = ? LIMIT 1) AS harga_permalam";

            PreparedStatement pstmt = con.prepareStatement(sqlCekKamar);
            pstmt.setString(1, tr.getTipe_kamar());
            pstmt.setString(2, tr.getTipe_kamar());
            pstmt.setString(3, tr.getCheck_out());  // Tanggal check-out baru
            pstmt.setString(4, tr.getCheck_in());  // Tanggal check-in baru
            pstmt.setString(5, tr.getCheck_out());  // Tanggal check-out baru
            pstmt.setString(6, tr.getCheck_in());  // Tanggal check-in baru
            pstmt.setString(7, tr.getCheck_in());  // Tanggal check-in baru
            pstmt.setString(8, tr.getCheck_out()); // Tanggal check-out baru
            pstmt.setString(9, tr.getTipe_kamar());

            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt("sisa_kamar") > 0) {
                // Hitung total harga
                double hargaPerMalam = rs.getDouble("harga_permalam");
                LocalDate checkinDate = LocalDate.parse(checkIn);
                LocalDate checkoutDate = LocalDate.parse(checkOut);
                long totalHari = ChronoUnit.DAYS.between(checkinDate, checkoutDate);

                this.totalHarga = totalHari * hargaPerMalam;

                return true; // Kamar tersedia
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return false; // Kamar tidak tersedia
    }



   
    
    DefaultTableModel dtm2 = new DefaultTableModel();
    
    public DefaultTableModel createTableRiwayat(){
        dtm2.addColumn("ID Transaksi");
        dtm2.addColumn("Nama Lengkap");
        dtm2.addColumn("Tipe Kamar");
        dtm2.addColumn("Check In");
        dtm2.addColumn("Check Out");
        
        return dtm2;
    }
    
    public void lihatRiwayat(String username) {
        Pelanggan pl = new Pelanggan(0, username, "", "", "", "");
        try {
            dtm2.getDataVector().removeAllElements();
            dtm2.fireTableDataChanged();

            String sql = "SELECT tp.id_transaksi, tp.nama_lengkap, tp.tipe_kamar, tp.check_in, tp.check_out " +
                         "FROM tbpesan tp " +
                         "JOIN tbpelanggan tpl ON tp.id_pelanggan = tpl.id_pelanggan " +
                         "WHERE tpl.username = ? AND tp.sts_checkout = 'Yes'";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, pl.getUsername());  

            ResultSet res = pstmt.executeQuery();

            boolean dataDitemukan = false;


            while (res.next()) {
                Object[] obj = new Object[5];
                obj[0] = res.getString("id_transaksi");
                obj[1] = res.getString("nama_lengkap");
                obj[2] = res.getString("tipe_kamar");
                obj[3] = res.getString("check_in");
                obj[4] = res.getString("check_out");
                dtm2.addRow(obj); 
                dataDitemukan = true; 
            }

            if (!dataDitemukan) {
                System.out.println("Tidak ada riwayat pesanan untuk username: " + username);
            }

        } catch (Exception e) {
            System.out.println("QUERY GAGAL....." + e);
        }
    }

}




    
    

