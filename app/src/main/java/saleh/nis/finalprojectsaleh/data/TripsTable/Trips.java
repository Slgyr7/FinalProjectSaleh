package saleh.nis.finalprojectsaleh.data.TripsTable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class Trips {
    @PrimaryKey(autoGenerate = true)
    public long keyid;
    @ColumnInfo
    public String title;
    public String category;
    public double rating;
    public double price;
    public String address;
   public String Status;
    //public String Address;
    public String attractionimage;
    @Ignore
    private List<String> vibes = new ArrayList<>();

    // For Room database compatibility
    private String vibesString;  // Stores comma-separated vibes for database
    private String tripsKey;

    @Override
    public String toString() {
        return "Trips{" +
                "keyid=" + keyid +
                ", name=" + title +
                ", Category=" + category +
                ", Rating=" + rating +
                ", Price=" + price +
                ", Status='" + Status + '\'' +
                ", Addres='" + address + '\''+
                ", Vibes='" + vibes + '\'' +
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
    public String getStatus() {
        return Status;
    }
    public void setStatus(String status) {
        Status = status;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }



    // Get vibes as List
    public List<String> getVibes() {
        if ((vibes == null || vibes.isEmpty()) && vibesString != null) {
            // If list is empty but string is not, parse the string
            vibes = new ArrayList<>(Arrays.asList(vibesString.split("\\s*,\\s*")));
        }
        return vibes;
    }

    // Set vibes from List
    public void setVibes(List<String> vibes) {
        this.vibes = vibes;
        updateVibesString();
    }

    // Set vibes from comma-separated string
    public void setVibes(String vibes) {
        if (vibes != null && !vibes.isEmpty()) {
            this.vibes = new ArrayList<>(Arrays.asList(vibes.split("\\s*,\\s*")));
            this.vibesString = vibes;
        }
    }

    // Get vibes as comma-separated string (for database)
    public String getVibesString() {
        if (vibesString == null && vibes != null) {
            updateVibesString();
        }
        return vibesString;
    }

    // Update the string representation from the list
    private void updateVibesString() {
        if (vibes != null && !vibes.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String vibe : vibes) {
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
        if (vibes == null) {
            vibes = new ArrayList<>();
        }
        if (!vibes.contains(vibe)) {
            vibes.add(vibe);
            updateVibesString();
        }
    }

    // Remove a vibe
    public void removeVibe(String vibe) {
        if (vibes != null) {
            vibes.remove(vibe);
            updateVibesString();
        }
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAttractionimage() {
        return attractionimage;
    }

    public void setAttractionimage(String attractionimage) {
        this.attractionimage = attractionimage;
    }

    public void setVibesString(String vibesString) {
        this.vibesString = vibesString;
    }

    public void setTripsKey(String tripsKey) {
        this.tripsKey = tripsKey;
    }

    public String getTripsKey() {
        return tripsKey;
    }


}
