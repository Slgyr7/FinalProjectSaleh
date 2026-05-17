package saleh.nis.finalprojectsaleh;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.FirebaseDatabase;

import saleh.nis.finalprojectsaleh.data.TripsTable.Trips;

/**
 * BAGRUT: Dedicated activity for ADMINS only.
 * This screen displays full info and provides tools to EDIT or DELETE the trip.
 */
public class AdminSiteActivity extends AppCompatActivity {

    private TextView tvName, tvAddress, tvDescription, tvPrice;
    private Trips currentTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_site);

        // BAGRUT: Get the trip object passed from the Admin list
        currentTrip = (Trips) getIntent().getSerializableExtra("TRIP_DATA");

        if (currentTrip == null) {
            Toast.makeText(this, "Error: Data missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        displayData();
    }

    private void initializeViews() {
        tvName = findViewById(R.id.adminSiteName);
        tvAddress = findViewById(R.id.adminSiteAddress);
        tvDescription = findViewById(R.id.adminSiteDescription);
        tvPrice = findViewById(R.id.adminSitePrice);

        Toolbar toolbar = findViewById(R.id.adminToolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // BAGRUT: EDIT tool - opens AddTripActivity in "Edit Mode"
        MaterialButton btnEdit = findViewById(R.id.btn_admin_edit);
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddTripActivity.class);
            intent.putExtra("EDIT_TRIP", currentTrip);
            startActivity(intent);
            finish(); // Close this so we don't have multiple detail screens
        });

        // BAGRUT: DELETE tool - removes item from Firebase
        MaterialButton btnDelete = findViewById(R.id.btn_admin_delete);
        btnDelete.setOnClickListener(v -> showDeleteDialog());
    }

    private void displayData() {
        tvName.setText(currentTrip.getTitle());
        tvAddress.setText(currentTrip.getAddress());
        tvDescription.setText(currentTrip.getDescription());
        tvPrice.setText("Price: ₪" + currentTrip.getPrice());
    }

    private void showDeleteDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Trip")
                .setMessage("Are you sure you want to delete this trip permanently?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    // BAGRUT: Delete from Firebase using the unique key
                    FirebaseDatabase.getInstance().getReference("trips")
                            .child(currentTrip.getTripsKey())
                            .removeValue()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Trip Deleted", Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
