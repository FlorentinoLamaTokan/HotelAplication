
package tr.hotel.model;


public class Pelanggan extends PenggunaHotelL {
    private String noHp;
    private String email;

    public Pelanggan(int id, String user, String pass, String nama, String noHp, String email) {
        super(id, user, pass, nama);
        this.noHp=noHp;
        this.email=email;
    }

    public String getNoHp() {
        return noHp;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNama() {
        return nama;
    }

 
    
}
