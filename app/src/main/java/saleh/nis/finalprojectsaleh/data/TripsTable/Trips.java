package saleh.nis.finalprojectsaleh.data.TripsTable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class Trips {
    @PrimaryKey(autoGenerate = true)
    public long keyid;
    @ColumnInfo
    public String title;
    public String Category;
    public double Rating ;
    public double Price;
   public String Status;
    public String Address;
    public String attractionimage;
    public String duration;  // e.g., "2 hours", "1 day", "3 hours 30 min"

    @Override
    public String toString() {
        return "Trips{" +
                "keyid=" + keyid +
                ", name=" + title +
                ", Category=" + Category +
                ", Rating=" + Rating +
                ", Price=" + Price +
                ", Status='" + Status + '\'' +
                ", Address=" + Address +
                ", Duration='" + duration + '\'' +
                ", attractionimage='" + attractionimage + '\'' +
                '}';
    }
    public long getKeyid() {
        return keyid;
    }
    public void setKeyid(long keyid) {
        this.keyid = keyid;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
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

    public String getAddress() {
        return Address;
    }
    public void setAddress(String address) {
        Address = address;
    }

    public String getAttractionimage() {
        return attractionimage;
    }

    public void setAttractionimage(String attractionimage) {
        this.attractionimage = attractionimage;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
