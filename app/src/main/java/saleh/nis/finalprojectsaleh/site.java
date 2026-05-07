package saleh.nis.finalprojectsaleh;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import saleh.nis.finalprojectsaleh.data.TripsTable.Trips;

public class site extends AppCompatActivity {

    private ImageView siteImage;
    private TextView siteName, siteRating, siteAddress, sitePhone, siteWebsite, siteHours, siteCost, siteDescription;
    private Chip siteCategory;
    private ChipGroup cgVibes;
    private Toolbar toolbar;
    private FloatingActionButton fabFavorite;
    private MaterialButton btnCall, btnWebsite, btnDirections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_site);

        // Initialize Views
        siteImage = findViewById(R.id.siteImage);
        siteName = findViewById(R.id.siteName);
        siteCategory = findViewById(R.id.siteCategory);
        siteRating = findViewById(R.id.siteRating);
        siteAddress = findViewById(R.id.siteAddress);
        sitePhone = findViewById(R.id.sitePhone);
        siteWebsite = findViewById(R.id.siteWebsite);
        siteHours = findViewById(R.id.siteHours);
        siteCost = findViewById(R.id.siteCost);
        siteDescription = findViewById(R.id.siteDescription);
        cgVibes = findViewById(R.id.cgVibes);
        toolbar = findViewById(R.id.toolbar);
        fabFavorite = findViewById(R.id.fabFavorite);
        
        btnCall = findViewById(R.id.btnCall);
        btnWebsite = findViewById(R.id.btnWebsite);
        btnDirections = findViewById(R.id.btnDirections);

        // Setup Toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // Get Trip Data from Intent
        Trips trip = (Trips) getIntent().getSerializableExtra("TRIP_DATA");

        if (trip != null) {
            displayTripDetails(trip);
        } else {
            Toast.makeText(this, "Error: Trip data not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void displayTripDetails(Trips trip) {
        // Set basic info
        siteName.setText(trip.getTitle());
        siteCategory.setText(trip.getCategory());
        siteRating.setText(String.valueOf(trip.getRating()));
        siteAddress.setText(trip.getAddress());
        siteCost.setText("₪" + trip.getPrice());

        // Set additional info
        if (trip.getPhone() != null && !trip.getPhone().isEmpty()) {
            sitePhone.setText(trip.getPhone());
            btnCall.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + trip.getPhone()));
                startActivity(intent);
            });
        } else {
            sitePhone.setText("Not provided");
            btnCall.setEnabled(false);
        }

        if (trip.getWebsite() != null && !trip.getWebsite().isEmpty()) {
            siteWebsite.setText(trip.getWebsite());
            btnWebsite.setOnClickListener(v -> {
                String url = trip.getWebsite();
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://" + url;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            });
        } else {
            siteWebsite.setText("Not provided");
            btnWebsite.setEnabled(false);
        }

        if (trip.getHours() != null && !trip.getHours().isEmpty()) {
            siteHours.setText(trip.getHours());
        } else {
            siteHours.setText("Not provided");
        }

        if (trip.getDescription() != null && !trip.getDescription().isEmpty()) {
            siteDescription.setText(trip.getDescription());
        } else {
            siteDescription.setText("No description available for this trip.");
        }
        
        // Directions logic
        btnDirections.setOnClickListener(v -> {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + Uri.encode(trip.getAddress()));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                // Fallback to browser maps
                Intent webMapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Uri.encode(trip.getAddress())));
                startActivity(webMapIntent);
            }
        });

        // Set Vibes Tags
        cgVibes.removeAllViews();
        if (trip.getVibes() != null && !trip.getVibes().isEmpty()) {
            String[] vibesArray = trip.getVibes().split(",");
            for (String vibe : vibesArray) {
                Chip chip = new Chip(this);
                chip.setText(vibe.trim());
                cgVibes.addView(chip);
            }
        }

        // Favorite FAB
        fabFavorite.setOnClickListener(v -> {
            Toast.makeText(this, "Saved to favorites!", Toast.LENGTH_SHORT).show();
        });
    }
}
