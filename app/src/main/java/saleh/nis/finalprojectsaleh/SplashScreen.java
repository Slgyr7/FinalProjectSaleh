package saleh.nis.finalprojectsaleh;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    // 3000ms = 3 seconds
    private static final int SPLASH_DURATION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enables edge-to-edge display
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Handle padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Use a Handler to check login status after the delay
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // 1. Check if a user is already logged into Firebase
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                if (currentUser != null) {
                    // 2. User IS logged in -> Go straight to Home Screen
                    Intent intent = new Intent(SplashScreen.this, HomeScreen.class);
                    startActivity(intent);
                } else {
                    // 3. User is NOT logged in -> Go to Welcome Screen
                    Intent intent = new Intent(SplashScreen.this, welcome.class);
                    startActivity(intent);
                }

                // Finish this activity so the user cannot go back to the splash screen
                finish();
            }
        }, SPLASH_DURATION);
    }
}