package saleh.nis.finalprojectsaleh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

// BAGRUT: The main screen for the Moderator/Admin
public class AdminHomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_home_screen);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.conlay_admin), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Manage Trips Card -> Opens the dedicated Admin Management screen
        CardView manageTripsCard = findViewById(R.id.card_manage_trips);
        manageTripsCard.setOnClickListener(v -> {
            // BAGRUT: Now it opens AdminTripsActivity (the management tool)
            Intent intent = new Intent(AdminHomeScreen.this, AdminTripsActivity.class);
            startActivity(intent);
        });

        // 2. Add Trip Card -> Directly opens the screen to add a new item
        CardView addTripCard = findViewById(R.id.card_add_trip);
        addTripCard.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeScreen.this, AddTripActivity.class);
            startActivity(intent);
        });

        // 3. Profile Icon
        ImageView profileIcon = findViewById(R.id.profile_icon_admin);
        profileIcon.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeScreen.this, ProfileActivity.class);
            startActivity(intent);
        });

        // 4. Logout Logic
        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            SharedPreferences sp = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.apply();

            Toast.makeText(AdminHomeScreen.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(AdminHomeScreen.this, welcome.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
