package saleh.nis.finalprojectsaleh.data.TripsTable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.android.material.chip.Chip;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class Trips {
    @PrimaryKey(autoGenerate = true)
    public long keyid;
    @ColumnInfo
    public String title;
    public Chip Category;
    public double Rating ;
    public double Price;
    public String Address;
   public String Status;
    //public String Address;
    public ShapeableImageView attractionimage;
    @Ignore
    private List<String> vibesList = new ArrayList<>();
    
    // For Room database compatibility
    @ColumnInfo(name = "vibes")
    private String vibesString;  // Stores comma-separated vibes for database

    @Override
    public String toString() {
        return "Trips{" +
                "keyid=" + keyid +
                ", name=" + title +
                ", Category=" + Category +
                ", Rating=" + Rating +
                ", Price=" + Price +
                ", Status='" + Status + '\'' +
                ", Addres='" + Address+ '\''+
                ", Vibes='" + vibesList + '\'' +
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
    public Chip getCategory() {
        return Category;
    }
    public void setCategory(Chip category) {
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

    public ShapeableImageView getAttractionimage() {
        return attractionimage;
    }

    public void setAttractionimage(ShapeableImageView attractionimage) {
        this.attractionimage = attractionimage;
    }

    // Get vibes as List
    public List<String> getVibes() {
        if ((vibesList == null || vibesList.isEmpty()) && vibesString != null) {
            // If list is empty but string is not, parse the string
            vibesList = new ArrayList<>(Arrays.asList(vibesString.split("\\s*,\\s*")));
        }
        return vibesList;
    }

    // Set vibes from List
    public void setVibes(List<String> vibes) {
        this.vibesList = vibes;
        updateVibesString();
    }
    
    // Set vibes from comma-separated string
    public void setVibes(String vibes) {
        if (vibes != null && !vibes.isEmpty()) {
            this.vibesList = new ArrayList<>(Arrays.asList(vibes.split("\\s*,\\s*")));
            this.vibesString = vibes;
        }
    }
    
    // Get vibes as comma-separated string (for database)
    public String getVibesString() {
        if (vibesString == null && vibesList != null) {
            updateVibesString();
        }
        return vibesString;
    }
    
    // Update the string representation from the list
    private void updateVibesString() {
        if (vibesList != null && !vibesList.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String vibe : vibesList) {
                if (sb.length() > 0) sb.append(",");
                sb.append(vibe.trim());
            }
            vibesString = sb.toString();
        } else {
            vibesString = "";
        }
    }
    
    // Add a single vibe
    public void addVibe(String vibe) {
        if (vibesList == null) {
            vibesList = new ArrayList<>();
        }
        if (!vibesList.contains(vibe)) {
            vibesList.add(vibe);
            updateVibesString();
        }
    }
    
    // Remove a vibe
    public void removeVibe(String vibe) {
        if (vibesList != null) {
            vibesList.remove(vibe);
            updateVibesString();
        }
    }
}
