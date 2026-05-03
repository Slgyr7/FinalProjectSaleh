package saleh.nis.finalprojectsaleh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    private TextView userName, userEmail, userRole;
    private MaterialButton logoutButton;

    // MATCHING YOUR NEW SYSTEM
    private static final String PREF_NAME = "UserPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.user_email);
        userRole = findViewById(R.id.user_role);
        logoutButton = findViewById(R.id.logout_button);

        loadUserData();

        logoutButton.setOnClickListener(v -> performLogout());
    }

    private void loadUserData() {
        SharedPreferences sp = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        
        // Using the exact keys from Register.java
        String name = sp.getString("userName", "User");
        String email = sp.getString("userEmail", "Not provided");
        String role = sp.getString("userRole", "customer");

        userName.setText(name);
        userEmail.setText(email);
        
        if ("admin".equals(role)) {
            userRole.setText("Role: Organization Admin");
        } else {
            userRole.setText("Role: Customer");
        }
    }

    private void performLogout() {
        FirebaseAuth.getInstance().signOut();

        // Clear the new UserPrefs
        SharedPreferences sp = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().clear().apply();

        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ProfileActivity.this, welcome.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
