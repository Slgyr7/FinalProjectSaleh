package saleh.nis.finalprojectsaleh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    // UI Components
    private ShapeableImageView profileImage;
    private TextView userName, userEmail, userPhone, userRole;
    private MaterialButton logoutButton;

    // SharedPreferences
    private static final String PREF_NAME = "UserData";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_PASSWORD = "password";

    // Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Handle edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.conlay_profile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI components
        initViews();

        // Load user data
        loadUserData();

        // Set up logout button
        setupLogoutButton();
    }

    private void initViews() {
        profileImage = findViewById(R.id.profile_image);
        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.user_email);
        userPhone = findViewById(R.id.user_phone);
        userRole = findViewById(R.id.user_role);
        logoutButton = findViewById(R.id.logout_button);
    }

    private void loadUserData() {
        // Get data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        
        String username = sharedPreferences.getString(KEY_USERNAME, "User");
        String email = sharedPreferences.getString(KEY_EMAIL, "user@example.com");
        String phone = sharedPreferences.getString(KEY_PHONE, "Not provided");
        String role = sharedPreferences.getString("role", "customer");

        // Set data to views
        userName.setText(username);
        userEmail.setText(email);
        userPhone.setText(phone);
        
        // Format role for display
        if ("admin".equals(role)) {
            userRole.setText("Organization Admin");
        } else {
            userRole.setText("Customer");
        }

        // You can set a default profile image here
        // For now, we'll use the sample avatar
        // profileImage.setImageResource(R.drawable.default_profile_image);
    }

    private void setupLogoutButton() {
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show confirmation dialog (optional)
                performLogout();
            }
        });
    }

    private void performLogout() {
        try {
            // Sign out from Firebase
            mAuth.signOut();

            // Clear SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            // Show success message
            Toast.makeText(ProfileActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

            // Navigate to welcome activity and clear back stack
            Intent intent = new Intent(ProfileActivity.this, welcome.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        } catch (Exception e) {
            Toast.makeText(ProfileActivity.this, "Error during logout: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


}
