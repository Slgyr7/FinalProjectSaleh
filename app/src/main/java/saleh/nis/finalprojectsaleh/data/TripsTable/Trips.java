package saleh.nis.finalprojectsaleh.data.TripsTable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Trips implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public long keyid;

    @ColumnInfo
    public String title;
    public String category;
    public double rating;
    public double price;
    public String address;
    public String status;
    public String attractionimage;
    private String vibes;

    // Additional fields for Detailed Info
    private String phone;
    private String website;
    private String hours;
    private String description;
    
    // BAGRUT: ownerUid stores the ID of the Admin who created this trip
    private String ownerUid;

    @Ignore
    private String vibesString;
    private String tripsKey;

    // --- GETTERS AND SETTERS (Standard Format) ---

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
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAttractionimage() {
        return attractionimage;
    }

    public void setAttractionimage(String attractionimage) {
        this.attractionimage = attractionimage;
    }

    public String getVibes() {
        return vibes;
    }

    public void setVibes(String vibes) {
        this.vibes = vibes;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(String ownerUid) {
        this.ownerUid = ownerUid;
    }

    public String getTripsKey() {
        return tripsKey;
    }

    public void setTripsKey(String tripsKey) {
        this.tripsKey = tripsKey;
    }

    public String getVibesString() {
        return vibesString;
    }

    public void setVibesString(String vibesString) {
        this.vibesString = vibesString;
    }
}
