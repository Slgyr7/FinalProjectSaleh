package saleh.nis.finalprojectsaleh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class Register extends AppCompatActivity {

    // Declare the views for the buttons and input fields
    private ImageView backButton;
    private Button createAccountButton;
    private TextInputEditText username, email, password, phone;
    
    // SharedPreferences for local storage
    private static final String PREF_NAME = "UserData";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_PHONE = "phone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // This listener handles the edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.conlayr), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize input fields
        username = findViewById(R.id.ET_username);
        email = findViewById(R.id.ET_email);
        password = findViewById(R.id.ET_password);
        phone = findViewById(R.id.ET_phone);
        
        // Back Button Functionality
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

        // Create Account Button Functionality
        createAccountButton = findViewById(R.id.btn_create);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values
                String usernameStr = username.getText().toString().trim();
                String emailStr = email.getText().toString().trim();
                String passwordStr = password.getText().toString().trim();
                String phoneStr = phone.getText().toString().trim();

                // Validate inputs
                if (validateInputs(usernameStr, emailStr, passwordStr, phoneStr)) {
                    // Save user data
                    if (saveUserData(usernameStr, emailStr, passwordStr, phoneStr)) {
                        // Registration successful
                        Toast.makeText(Register.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                        
                        // Navigate to HomeScreen
                        Intent intent = new Intent(Register.this, HomeScreen.class);
                        startActivity(intent);
                        finishAffinity();
                    } else {
                        Toast.makeText(Register.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean validateInputs(String username, String email, String password, String phone) {
        if (TextUtils.isEmpty(username)) {
            this.username.setError("Username is required");
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            this.email.setError("Email is required");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.email.setError("Please enter a valid email");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            this.password.setError("Password is required");
            return false;
        }

        if (password.length() < 6) {
            this.password.setError("Password must be at least 6 characters");
            return false;
        }

        if (TextUtils.isEmpty(phone)) {
            this.phone.setError("Phone number is required");
            return false;
        }

        return true;
    }

    private boolean saveUserData(String username, String email, String password, String phone) {
        try {
            // Get SharedPreferences editor
            SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            
            // Store user data
            editor.putString(KEY_USERNAME, username);
            editor.putString(KEY_EMAIL, email);
            editor.putString(KEY_PASSWORD, password); // In a real app, you should hash the password
            editor.putString(KEY_PHONE, phone);
            
            // Apply changes
            editor.apply();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    }

