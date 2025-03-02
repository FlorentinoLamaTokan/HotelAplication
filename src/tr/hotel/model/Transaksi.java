
package tr.hotel.model;


public class Transaksi {
    private int id_transaksi;
    private int id_room;
    private int id_pelanggan;
    private String nama_lengkap;
    private String tipe_kamar;
    private String no_kamar;
    private String check_in;
    private String check_out;
    private String mtdPembayaran;
    private double totalHarga;
    private String no_hp;
    private String email;
    private String stsCheckIn;
    private String stsCheckOut;

    public String getNo_kamar() {
        return no_kamar;
    }

    public void setNo_kamar(String no_kamar) {
        this.no_kamar = no_kamar;
    }
    

    public String getStsCheckIn() {
        return stsCheckIn;
    }

    public void setStsCheckIn(String stsCheckIn) {
        this.stsCheckIn = stsCheckIn;
    }

    public String getStsCheckOut() {
        return stsCheckOut;
    }

    public void setStsCheckOut(String stsCheckOut) {
        this.stsCheckOut = stsCheckOut;
    }
    
    

    public double getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(double totalHarga) {
        this.totalHarga = totalHarga;
    }
    
    

    public String getMtdPembayaran() {
        return mtdPembayaran;
    }

    public void setMtdPembayaran(String mtdPembayaran) {
        this.mtdPembayaran = mtdPembayaran;
    }
    
    public int getId_transaksi() {
        return id_transaksi;
    }

    public void setId_transaksi(int id_transaksi) {
        this.id_transaksi = id_transaksi;
    }

    public int getId_room() {
        return id_room;
    }

    public void setId_room(int id_room) {
        this.id_room = id_room;
    }

    public int getId_pelanggan() {
        return id_pelanggan;
    }

    public void setId_pelanggan(int id_pelanggan) {
        this.id_pelanggan = id_pelanggan;
    }

    public String getNama_lengkap() {
        return nama_lengkap;
    }

    public void setNama_lengkap(String nama_lengkap) {
        this.nama_lengkap = nama_lengkap;
    }

    public String getTipe_kamar() {
        return tipe_kamar;
    }

    public void setTipe_kamar(String tipe_kamar) {
        this.tipe_kamar = tipe_kamar;
    }

    public String getCheck_in() {
        return check_in;
    }

    public void setCheck_in(String check_in) {
        this.check_in = check_in;
    }

    public String getCheck_out() {
        return check_out;
    }

    public void setCheck_out(String check_out) {
        this.check_out = check_out;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
}
