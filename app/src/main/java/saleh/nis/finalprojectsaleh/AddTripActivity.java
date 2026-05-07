package saleh.nis.finalprojectsaleh;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import saleh.nis.finalprojectsaleh.data.AppDataBase;
import saleh.nis.finalprojectsaleh.data.TripsTable.Trips;

public class AddTripActivity extends AppCompatActivity {
    private final int IMAGE_PICK_CODE=100;
    private final int PERMISSION_CODE=101;
    private MaterialCardView imgBtn;
    private Uri toUploadimageUri;
    private Uri downladuri;


    private ImageView ivTripImage;
    private TextInputEditText etTitle, etLocation, etPrice, etRating;
    private TextInputEditText etPhone, etWebsite, etHours, etDescription; // Additional fields
    private ChipGroup categoryChipGroup, vibesChipGroup;
    private Button btnSubmit;
    private TextInputLayout title_lyot, location_lyot, price_lyot, rating_lyot;

    private ActivityResultLauncher<String> requestReadMediaImagesPermission;
    private ActivityResultLauncher<String> requestReadMediaVideoPermission;
    private ActivityResultLauncher<String> requestReadExternalStoragePermission;

    private Uri selectedImageUri;
    private ActivityResultLauncher<String> pickImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_trip);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.linlayaddtrip), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize input fields
        ivTripImage = findViewById(R.id.ivTripImage);
        etTitle = findViewById(R.id.etTitle);
        etLocation = findViewById(R.id.etLocation);
        etPrice = findViewById(R.id.etPrice);
        etRating = findViewById(R.id.etRating);
        
        // Additional info fields
        etPhone = findViewById(R.id.etPhone);
        etWebsite = findViewById(R.id.etWebsite);
        etHours = findViewById(R.id.etHours);
        etDescription = findViewById(R.id.etDescription);

        categoryChipGroup = findViewById(R.id.categoryChipGroup);
        vibesChipGroup = findViewById(R.id.vibesChipGroup);

        title_lyot = findViewById(R.id.title_lyot);
        location_lyot = findViewById(R.id.location_lyot);
        price_lyot = findViewById(R.id.price_lyot);
        rating_lyot = findViewById(R.id.rating_lyot);

        requestReadMediaImagesPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            }
        });

        requestReadExternalStoragePermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            }
        });

        checkAndRequestPermissions();

        pickImage = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    selectedImageUri = result;
                    ivTripImage.setImageURI(result);
                    ivTripImage.setVisibility(View.VISIBLE);
                }
            }
        });

       ivTripImage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               pickImage.launch("image/*");
           }
       });


        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             validate();
                                         }
                                     }
        );
    }

    private boolean validate() {
        boolean isValid = true;
        title_lyot.setError(null);
        location_lyot.setError(null);
        price_lyot.setError(null);
        rating_lyot.setError(null);

        String title = etTitle.getText().toString().trim();
        String address = etLocation.getText().toString().trim();
        String price = etPrice.getText().toString().trim();
        String rating = etRating.getText().toString().trim();
        
        // New fields
        String phone = etPhone.getText().toString().trim();
        String website = etWebsite.getText().toString().trim();
        String hours = etHours.getText().toString().trim();
        String description = etDescription.getText().toString().trim();


        if (title.isEmpty()) {
            title_lyot.setError("Title is required");
            etTitle.requestFocus();
            isValid = false;
        }

        if (address.isEmpty()) {
            location_lyot.setError("Address is required");
            etLocation.requestFocus();
            isValid = false;
        }

        if (price.isEmpty()) {
            price_lyot.setError("Price is required");
            etPrice.requestFocus();
            isValid = false;
        }

        if (categoryChipGroup.getCheckedChipIds().isEmpty()) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (vibesChipGroup.getCheckedChipIds().isEmpty()) {
            Toast.makeText(this, "Please select a vibe", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (rating.isEmpty()) {
            rating_lyot.setError("Rating is required");
            etRating.requestFocus();
            isValid = false;
        }

        if (isValid) {
            // Get selected category
            List<String> selectedCategories = new ArrayList<>();
            for (int i = 0; i < categoryChipGroup.getChildCount(); i++) {
                Chip chip = (Chip) categoryChipGroup.getChildAt(i);
                if (chip.isChecked()) {
                    selectedCategories.add(chip.getText().toString());
                }
            }
            String categoryStr = TextUtils.join(", ", selectedCategories);

            // Get selected vibes
            List<String> selectedVibes = new ArrayList<>();
            for (int i = 0; i < vibesChipGroup.getChildCount(); i++) {
                Chip chip = (Chip) vibesChipGroup.getChildAt(i);
                if (chip.isChecked()) {
                    selectedVibes.add(chip.getText().toString());
                }
            }
            String vibesStr = TextUtils.join(", ", selectedVibes);

            Trips trips = new Trips();
            trips.setTitle(title);
            trips.setAddress(address);
            trips.setPrice(Double.parseDouble(price));
            trips.setRating(Double.parseDouble(rating));
            trips.setCategory(categoryStr);
            trips.setVibes(vibesStr);
            
            // Set additional info for Site Activity
            trips.setPhone(phone);
            trips.setWebsite(website);
            trips.setHours(hours);
            trips.setDescription(description);
            
            saveTrips(trips);
        }
        return isValid;
    }

    public void saveTrips(Trips trips) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dbRef = database.child("trips");
        DatabaseReference newtripsRef = dbRef.push();
        trips.setTripsKey(newtripsRef.getKey());

        dbRef.child(trips.getTripsKey()).setValue(trips).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AddTripActivity.this, "Trip added successfully", Toast.LENGTH_SHORT).show();
                    new Thread(() -> {
                        AppDataBase.getDB(AddTripActivity.this).getTripsQuery().insertAll(trips);
                        runOnUiThread(() -> finish());
                    }).start();
                } else {
                    Toast.makeText(AddTripActivity.this, "Failed to add trip", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                requestReadMediaImagesPermission.launch(android.Manifest.permission.READ_MEDIA_IMAGES);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestReadExternalStoragePermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }
}
