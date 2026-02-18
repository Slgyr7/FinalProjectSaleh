package saleh.nis.finalprojectsaleh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.media.Rating;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import saleh.nis.finalprojectsaleh.data.TripsTable.Trips;

public class AddTripActivity extends AppCompatActivity {
    private ImageView ivTripImage ;
    private TextInputEditText etTitle, etAddress, etPrice, etRating;
    private ChipGroup categoryChipGroup, vibesChipGroup;
    private Button btnSubmit;
    private TextInputLayout title_lyot, location_lyot, price_lyot, rating_lyot;

//     private static final String PREF_NAME = "Trips data";
//    private static final String KEY_TITLE = "title";
//    private static final String KEY_ADDRESS = "address";
//    private static final String KEY_PRICE = "price";
//    private static final String KEY_RATING = "rating";
//    private static final String KEY_CATEGORY = "category";
//    private static final String KEY_VIBE = "vibe";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_trip);

        // This listener handles the edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.linlayaddtrip), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize input fields and layouts
        ivTripImage = findViewById(R.id.ivTripImage);
        etTitle= findViewById(R.id.etTitle);
        //adress
        etPrice= findViewById(R.id.etPrice);
        etRating= findViewById(R.id.etRating);
        categoryChipGroup= findViewById(R.id.categoryChipGroup);
        vibesChipGroup= findViewById(R.id.vibesChipGroup);

        // Initialize TextInputLayouts
        title_lyot = findViewById(R.id.title_lyot);
        location_lyot = findViewById(R.id.location_lyot);
        price_lyot = findViewById(R.id.price_lyot);
        rating_lyot = findViewById(R.id.rating_lyot);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener()
                                     {
            @Override
            public void onClick(View v) {
                // Reset errors

                }
            }
        );
    }
    private boolean validate() {
        boolean isValid = true;
        //Reset errors
        title_lyot.setError(null);
        location_lyot.setError(null);
        price_lyot.setError(null);
        rating_lyot.setError(null);

        String title = etTitle.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String price = etPrice.getText().toString().trim();
        String rating = etRating.getText().toString().trim();


        if (title.isEmpty()) {
            title_lyot.setError("Title is required");
            etTitle.requestFocus();
            isValid = false;
        }else if (title.length()< 3 ){
            title_lyot.setError("Title must be at least 3 characters");
            etTitle.requestFocus();
            isValid = false;
        }
        //address validation
        if (address.isEmpty()) {
            location_lyot.setError("Address is required");
            etAddress.requestFocus();
            isValid = false;
        }else if (address.length() < 3) {
            location_lyot.setError("Address must be at least 3 characters");
            etAddress.requestFocus();
            isValid = false;
        }
        //price validation
        if (price.isEmpty()) {
            price_lyot.setError("Price is required");
            etPrice.requestFocus();
            isValid = false;
        }
        // Inside your validat() method, add this logic:

// --- Chip Group Validation ---
        int checkedCategoryId = categoryChipGroup.getCheckedChipId();
        if (checkedCategoryId == View.NO_ID) { // NO_ID means no chip is selected
            // You don't have a TextView for this error, so we'll use a Toast
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        int checkedVibeId = vibesChipGroup.getCheckedChipId();
        if (checkedVibeId == View.NO_ID) {
            Toast.makeText(this, "Please select a vibe", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        //rating validation
        if (rating.isEmpty()) {
            rating_lyot.setError("Rating is required");
            etRating.requestFocus();
            isValid = false;
        } else {
            try {
                double ratingValue = Double.parseDouble(rating);
                if (ratingValue < 0 || ratingValue > 5) {
                    rating_lyot.setError("Rating must be between 0 and 5");
                    etRating.requestFocus();
                    isValid = false;
                } else if (String.valueOf(ratingValue).split("\\.")[1].length() > 1) {
                    rating_lyot.setError("Only one decimal place allowed");
                    etRating.requestFocus();
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                rating_lyot.setError("Please enter a valid number");
                etRating.requestFocus();
                isValid = false;
            }
        }




        // Validate inputs

            // --- Get Chip Text ---
            int categoryId = categoryChipGroup.getCheckedChipId();
            Chip selectedCategoryChip = findViewById(categoryId);
            String categoryStr = selectedCategoryChip.getText().toString();

            int vibeId = vibesChipGroup.getCheckedChipId();
            Chip selectedVibeChip = findViewById(vibeId);
            String vibeStr = selectedVibeChip.getText().toString();

            // Get all selected chips values from vibesChipGroup
            List<String> selectedVibes = new ArrayList<>();
            for (int i = 0; i < vibesChipGroup.getChildCount(); i++) {
                Chip chip = (Chip) vibesChipGroup.getChildAt(i);
                if (chip.isChecked()) {
                    selectedVibes.add(chip.getText().toString());
                }
            }
            String vibesStr = TextUtils.join(", ", selectedVibes);


            // Save trip to database
//                   if (saveTripToDatabase(titleStr, addressStr, priceStr, ratingStr, categoryStr, vibeStr)) {
//                       //Trip add successfull
//                       Toast.makeText(AddTripActivity.this, "Trip added successfully", Toast.LENGTH_SHORT).show();
//                       //Go to plan trips
//                       Intent intent = new Intent(AddTripActivity.this, plan_trips.class);
//                       startActivity(intent);
//                       finishAffinity();
//                       }
//                   else {
//                       //Trip add failed
//                       Toast.makeText(AddTripActivity.this, "Trip add failed", Toast.LENGTH_SHORT).show();
//                   }

        if (isValid)
        {
            Trips trips = new Trips();
           trips.setAddress(address);
           trips.setPrice(Double.parseDouble(price));
           trips.setRating(Double.parseDouble(rating));
           trips.setCategory(categoryStr);
           trips.setVibes(vibeStr);
           trips.setTitle(title);

        }
        return isValid;
    }


    // Update the method signature and body
//    private boolean saveTripToDatabase(String title, String address, String price, String rating, String category, String vibe) {
//        try {
//            SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//
//            // Store all data
//            editor.putString(KEY_TITLE, title);
//            editor.putString(KEY_ADDRESS, address);
//            editor.putString(KEY_PRICE, price);
//            editor.putString(KEY_RATING, rating);
//            editor.putString(KEY_CATEGORY, category); // Save category
//            editor.putString(KEY_VIBE, vibe);
//
//            //Apply changes
//            editor.apply();
//            return true;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//
//        }
//    }
}
