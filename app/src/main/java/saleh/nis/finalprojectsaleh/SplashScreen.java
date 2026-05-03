package saleh.nis.finalprojectsaleh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import saleh.nis.finalprojectsaleh.data.UserProfileTable.MyUser;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Use your splash layout

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            SharedPreferences sp = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            boolean isLoggedIn = sp.getBoolean("isLoggedIn", false);

            if (currentUser != null && isLoggedIn) {
                // User is logged in, check role
                String role = sp.getString("userRole", "");

                Intent intent;
                if (MyUser.ROLE_ADMIN.equals(role)) {
                    intent = new Intent(SplashScreen.this, AdminHomeScreen.class);
                } else {
                    intent = new Intent(SplashScreen.this, HomeScreen.class);
                }
                startActivity(intent);
            } else {
                // No user logged in, go to welcome/login screen
                startActivity(new Intent(SplashScreen.this, welcome.class));
            }
            finish();
        }, SPLASH_DURATION);
    }
}
