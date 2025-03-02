
package tr.hotel.model;

public class Resepsionis extends PenggunaHotelL{
    
    public Resepsionis(int id, String user, String pass, String nama) {
        super(id, user, pass, nama);
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
