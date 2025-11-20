package saleh.nis.finalprojectsaleh.data.MySiteTable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class site {
    @PrimaryKey(autoGenerate = true)
    public long keyid;
@ColumnInfo
    public int name ;
    public int Category;
    public double Rating ;
    public int Opentime;
    public int CloseTime;
    public int Cost;
    public int Image;

    public int Address;
    public int Phone;
    public int Website;
    @Override
    public String toString() {
        return "site{" +
                "keyid=" + keyid +
                ", name=" + name +
                ", Category=" + Category +
                ", Rating=" + Rating +
                ", Opentime=" + Opentime +
                ", CloseTime=" + CloseTime +
                ", Cost=" + Cost +
                ", Image=" + Image +
                ", Address=" + Address +
                ", Phone=" + Phone +
                ", Website=" + Website +
                '}';
    }

    public long getKeyid() {
        return keyid;
    }
    public void setKeyid(long keyid) {
        this.keyid = keyid;
    }

    public int getName() {
        return name;
    }
    public void setName(int name) {
        this.name = name;
    }

    public int getCategory() {
        return Category;
    }
    public void setCategory(int category) {
        Category = category;
    }

    public double getRating() {
        return Rating;
    }

    public void setRating(double rating) {
        Rating = rating;
    }

    public int getOpentime() {
        return Opentime;
    }

    public void setOpentime(int opentime) {
        Opentime = opentime;
    }

    public int getCloseTime() {
        return CloseTime;
    }

    public void setCloseTime(int closeTime) {
        CloseTime = closeTime;
    }

    public int getCost() {
        return Cost;
    }

    public void setCost(int cost) {
        Cost = cost;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }


    public int getAddress() {
        return Address;
    }

    public void setAddress(int address) {
        Address = address;
    }

    public int getPhone() {
        return Phone;
    }

    public void setPhone(int phone) {
        Phone = phone;
    }

    public int getWebsite() {
        return Website;
    }

    public void setWebsite(int website) {
        Website = website;
    }

}
