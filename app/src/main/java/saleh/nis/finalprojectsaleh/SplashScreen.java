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

public class SplashScreen extends AppCompatActivity {

    // The duration the splash screen will be visible (in milliseconds)
    // 3000ms = 3 seconds
    private static final int SPLASH_DURATION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This enables the edge-to-edge display, which is good.
        EdgeToEdge.enable(this);

        // Set the layout for this activity
        setContentView(R.layout.activity_main);

        // This listener handles padding for system bars (like status bar), which is part of EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Use a Handler to post a delayed action to navigate to the next screen
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // This code will execute after the SPLASH_DURATION

                // Create an Intent to start the welcome screen
                // We assume your next screen is called 'welcome.java'
                Intent intent = new Intent(SplashScreen.this, welcome.class);
                startActivity(intent);

                // Finish this activity so the user cannot navigate back to it
                finish();
            }
        }, SPLASH_DURATION);
    }
}
