package saleh.nis.finalprojectsaleh.data.TripsTable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class Trips {
    @PrimaryKey(autoGenerate = true)
    public long keyid;
    @ColumnInfo
    public String name ;
    public String Category;
    public double Rating ;
    public double Price;
   public String Status;
 public double distance;
 public String Address;


    @Override
    public String toString() {
        return "Trips{" +
                "keyid=" + keyid +
                ", name=" + name +
                ", Category=" + Category +
                ", Rating=" + Rating +
                ", Price=" + Price +
                ", Status='" + Status + '\'' +
                ", distance=" + distance +
                ", Address=" + Address +
                '}';
    }
    public long getKeyid() {
        return keyid;
    }
    public void setKeyid(long keyid) {
        this.keyid = keyid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCategory() {
        return Category;
    }
    public void setCategory(String category) {
        Category = category;
    }
    public double getRating() {
        return Rating;
    }
    public void setRating(double rating) {
        Rating = rating;
    }
    public double getPrice() {
        return Price;
    }
    public void setPrice(double price) {
        Price = price;
    }
    public String getStatus() {
        return Status;
    }
    public void setStatus(String status) {
        Status = status;
    }
    public double getDistance() {
        return distance;
    }
    public void setDistance(double distance) {
        this.distance = distance;
    }
    public String getAddress() {
        return Address;
    }
    public void setAddress(String address) {
        Address = address;
    }
}
