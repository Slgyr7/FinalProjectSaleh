package saleh.nis.finalprojectsaleh;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import saleh.nis.finalprojectsaleh.data.TripsTable.Trips;

/**
 * BAGRUT: The detailed view for CUSTOMERS.
 * It features the 'Heart' (Favorite) button.
 */
public class site extends AppCompatActivity {

    private TextView tvName, tvAddress, tvDescription, tvPrice;
    private FloatingActionButton fabHeart;
    private Trips currentTrip;
    private String userId;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site);

        userId = FirebaseAuth.getInstance().getUid();
        currentTrip = (Trips) getIntent().getSerializableExtra("TRIP_DATA");

        if (currentTrip == null) {
            finish();
            return;
        }

        initializeViews();
        displayData();
        checkIfFavorite();
    }

    private void initializeViews() {
        tvName = findViewById(R.id.siteName);
        tvAddress = findViewById(R.id.siteAddress);
        tvDescription = findViewById(R.id.siteDescription);
        tvPrice = findViewById(R.id.siteCost);
        fabHeart = findViewById(R.id.fabFavorite);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // BAGRUT: Clicking the Heart saves the trip ID to the user's favorites node
        fabHeart.setOnClickListener(v -> toggleFavorite());
    }

    private void toggleFavorite() {
        DatabaseReference favRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userId).child("favorites").child(currentTrip.getTripsKey());

        if (isFavorite) {
            favRef.removeValue();
            isFavorite = false;
            Toast.makeText(this, "Removed from Favorites", Toast.LENGTH_SHORT).show();
        } else {
            favRef.setValue(true);
            isFavorite = true;
            Toast.makeText(this, "Added to Favorites!", Toast.LENGTH_SHORT).show();
        }
        updateHeartIcon();
    }

    private void checkIfFavorite() {
        FirebaseDatabase.getInstance().getReference("users")
                .child(userId).child("favorites").child(currentTrip.getTripsKey()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        isFavorite = true;
                        updateHeartIcon();
                    }
                });
    }

    private void updateHeartIcon() {
        fabHeart.setImageResource(isFavorite ? android.R.drawable.star_big_on : android.R.drawable.star_big_off);
    }

    private void displayData() {
        tvName.setText(currentTrip.getTitle());
        tvAddress.setText(currentTrip.getAddress());
        tvDescription.setText(currentTrip.getDescription());
        tvPrice.setText("Price: ₪" + currentTrip.getPrice());
    }
}
