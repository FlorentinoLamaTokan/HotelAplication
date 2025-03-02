
package tr.hotel.model;


public class Admin extends PenggunaHotelL{
    
    public Admin(int id, String user, String pass, String nama) {
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
