package saleh.nis.finalprojectsaleh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button; // 1. IMPORT the Button class
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class Register extends AppCompatActivity {

    // Declare the views for the buttons and input fields
    private ImageView backButton;
    private Button createAccountButton; // 2. DECLARE the create account button
    private com.google.android.material.textfield.TextInputEditText username, email, password, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // This listener handles the edge-to-edge display and is correct.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.conlayr), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
username= findViewById(R.id.ET_username);

email= findViewById(R.id.ET_email);
password= findViewById(R.id.ET_password);
phone= findViewById(R.id.ET_phone);
        // Initialize input fields
        username = findViewById(R.id.ET_username);
        email = findViewById(R.id.ET_email);
        password = findViewById(R.id.ET_password);
        phone = findViewById(R.id.ET_phone);
        
        // --- Back Button Functionality (already working) ---
        backButton = findViewById(R.id.back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the 'welcome' activity
                Intent intent = new Intent(Register.this, welcome.class);
                startActivity(intent);
                finish();
            }
        });

        // --- NEW: "CREATE ACCOUNT" BUTTON FUNCTIONALITY ---

        // 3. FIND the "Create Account" button by its ID from the XML
        createAccountButton = findViewById(R.id.btn_create);

        // 4. SET an OnClickListener on the button
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This code runs when the "Create Account" button is clicked.

                // TODO: Before navigating, you should get text from EditText fields
                // and perform user registration logic (e.g., save to a database).

                // For now, we will navigate directly to the HomeScreen.
                Intent intent = new Intent(Register.this, HomeScreen.class);
                startActivity(intent);

                // Finish both Register and Welcome activities so the user cannot go back to them
                // after creating an account. This provides a clean navigation flow.
                finishAffinity();
            }
        });
    }
}
