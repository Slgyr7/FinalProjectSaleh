package saleh.nis.finalprojectsaleh; // Make sure this package name is correct

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button; // Import the Button class

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);

        // This listener handles padding for system bars, which is correct.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.conlayw), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // --- CORRECTED FUNCTIONAL BUTTONS ---

        // 1. Find the buttons using their correct and current IDs from the XML
        Button logInButton = findViewById(R.id.login_button); // Correct ID for the main login button
        Button signUpButton = findViewById(R.id.regbtn);      // Correct ID for the "Sign Up" text button

        // 2. Set the OnClickListener for the "Log In" button
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This code runs when the "Log In" button is clicked.
                // TODO: You will add authentication logic here later (checking email/password).

                // Navigate to the main screen after successful login.
                Intent intent = new Intent(welcome.this, HomeScreen.class); // Assuming your home screen is HomeScreen.java
                startActivity(intent);
            }
        });

        // 3. Set the OnClickListener for the "Sign Up" button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This code runs when the "Sign Up" text button is clicked.
                Intent intent = new Intent(welcome.this, Register.class); // Navigates to the Register screen
                startActivity(intent);
            }
        });
    }
}
