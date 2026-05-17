package saleh.nis.finalprojectsaleh;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import saleh.nis.finalprojectsaleh.data.TripsTable.Trips;
import saleh.nis.finalprojectsaleh.data.UserProfileTable.MyUser;

/**
 * BAGRUT: This activity is for ADMINS only.
 * It handles both creating a new trip and UPDATING an existing one.
 */
public class AddTripActivity extends AppCompatActivity {

    private TextInputEditText etTitle, etLocation, etPrice, etDescription;
    private Button btnSubmit;
    private Trips tripToEdit; 
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // SECURITY CHECK: Ensure user is an Admin
        SharedPreferences sp = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        if (!MyUser.ROLE_ADMIN.equals(sp.getString("userRole", ""))) {
            Toast.makeText(this, "Access Denied!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setContentView(R.layout.activity_add_trip);
        dbRef = FirebaseDatabase.getInstance().getReference("trips");

        etTitle = findViewById(R.id.etTitle);
        etLocation = findViewById(R.id.etLocation);
        etPrice = findViewById(R.id.etPrice);
        etDescription = findViewById(R.id.etDescription);
        btnSubmit = findViewById(R.id.btnSubmit);

        // BAGRUT: Check if we are in "Update Mode"
        tripToEdit = (Trips) getIntent().getSerializableExtra("EDIT_TRIP");
        if (tripToEdit != null) {
            etTitle.setText(tripToEdit.getTitle());
            etLocation.setText(tripToEdit.getAddress());
            etPrice.setText(String.valueOf(tripToEdit.getPrice()));
            etDescription.setText(tripToEdit.getDescription());
            btnSubmit.setText("UPDATE TRIP INFO"); // Visual change for update
        }

        btnSubmit.setOnClickListener(v -> saveToFirebase());
    }

    private void saveToFirebase() {
        String title = etTitle.getText().toString().trim();
        String price = etPrice.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(price)) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // BAGRUT: If tripToEdit exists, we update it. Otherwise, create new.
        Trips trip = (tripToEdit != null) ? tripToEdit : new Trips();
        trip.setTitle(title);
        trip.setAddress(etLocation.getText().toString().trim());
        trip.setPrice(Double.parseDouble(price));
        trip.setDescription(etDescription.getText().toString().trim());
        
        // Ensure the creator ID is saved
        if (tripToEdit == null) trip.setOwnerUid(FirebaseAuth.getInstance().getUid());

        // Use the existing key if updating, or push for a new one
        String key = (trip.getTripsKey() != null) ? trip.getTripsKey() : dbRef.push().getKey();
        trip.setTripsKey(key);

        dbRef.child(key).setValue(trip).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Successfully Saved!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
