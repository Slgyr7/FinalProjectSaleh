package saleh.nis.finalprojectsaleh.data.UserProfileTable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MyUser {
    // Role constants for consistency
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_CUSTOMER = "customer";

    @PrimaryKey(autoGenerate = true)
    public long keyid;
    
    @ColumnInfo(name = "full_Name")
    public String fullName;
    
    public String email;
    public String phone;
    public String passw;
    public String role; // Role as a String for easier Firebase integration

    // Empty constructor required for Firebase Realtime Database
    public MyUser() {
    }

    @Override
    public String toString() {
        return "MyUser{" +
                "keyid=" + keyid +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    // Getters and Setters
    public long getKeyid() { return keyid; }
    public void setKeyid(long keyid) { this.keyid = keyid; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassw() { return passw; }
    public void setPassw(String passw) { this.passw = passw; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
