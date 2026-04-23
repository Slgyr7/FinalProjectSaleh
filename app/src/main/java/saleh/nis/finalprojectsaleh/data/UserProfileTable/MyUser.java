package saleh.nis.finalprojectsaleh.data.UserProfileTable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MyUser {
    //Entity = Table =جدول
//عندما نريد ان نتعامل مع هذه الفئة كجدول معطيات
    /**
     * فئة تمثل المستعمل
     */
    
    // Role enum for user types
    public enum Role {
        ADMIN("admin"),
        CUSTOMER("customer");
        
        private final String value;
        
        Role(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
        
        public static Role fromValue(String value) {
            for (Role role : Role.values()) {
                if (role.value.equals(value)) {
                    return role;
                }
            }
            return CUSTOMER; // default fallback
        }
    }
    
    @PrimaryKey(autoGenerate = true)//تحديد الصفة كمفتاح رئيسي والذي يُنتجح بشكل تلقائي
    public long keyid;
    @ColumnInfo(name = "full_Name")//اعطاء اسم جديد للعامود-الصفة في الجدول
    public String fullName;
    public String email;//بحالة لم يتم اعطاء اسم للعامود يكون اسم الصفه هو اسم العامود
    public String phone;
    public String passw;
    @ColumnInfo(name = "role")
    public Role role;

    @Override
    public String toString() {
        return "MyUser{" +
                "keyid=" + keyid +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", passw='" + passw + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    public long getKeyid() {
        return keyid;
    }

    public void setKeyid(long keyid) {
        this.keyid = keyid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassw() {
        return passw;
    }

    public void setPassw(String passw) {
        this.passw = passw;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}


