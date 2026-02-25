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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    // Declare the views for the buttons and input fields
    private ImageView backButton;
    private Button createAccountButton;
    private TextInputEditText usernameEditText, emailEditText, passwordEditText, phoneEditText;
    private TextInputLayout usernameLayout, emailLayout, passwordLayout, phoneLayout;
    
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

        // Initialize input fields and layouts
        usernameEditText = findViewById(R.id.ET_username);
        emailEditText = findViewById(R.id.ET_email);
        passwordEditText = findViewById(R.id.ET_password);
        phoneEditText = findViewById(R.id.ET_phone);
        
        // Initialize TextInputLayouts
        usernameLayout = findViewById(R.id.usernameLayout);
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        phoneLayout = findViewById(R.id.phoneLayout);
        
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
                // Reset errors
                usernameLayout.setError(null);
                emailLayout.setError(null);
                passwordLayout.setError(null);
                phoneLayout.setError(null);
                
                // Get input values
                String usernameStr = usernameEditText.getText().toString().trim();
                String emailStr = emailEditText.getText().toString().trim();
                String passwordStr = passwordEditText.getText().toString().trim();
                String phoneStr = phoneEditText.getText().toString().trim();

                // Validate inputs
                if (validateInputs(usernameStr, emailStr, passwordStr, phoneStr)) {
                    signUpUser(emailStr, passwordStr);
                    // Save user data room db
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

    private void signUpUser(String email, String password) {
        // Create user with Firebase Authentication
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up successful
                            Toast.makeText(Register.this, "Registration successful!", Toast.LENGTH_SHORT).show();

                            // Navigate to HomeScreen
                            Intent intent = new Intent(Register.this, HomeScreen.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            Toast.makeText(Register.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
//njk''hg\j/k
    private boolean validateInputs(String username, String email, String password, String phone) {
        boolean isValid = true;
        
        // Reset errors
        usernameLayout.setError(null);
        emailLayout.setError(null);
        passwordLayout.setError(null);
        phoneLayout.setError(null);
        
        // Username validation
        if (TextUtils.isEmpty(username)) {
            usernameLayout.setError("Username is required");
            usernameEditText.requestFocus();
            isValid = false;
        } else if (username.length() < 3) {
            usernameLayout.setError("Username must be at least 3 characters");
            usernameEditText.requestFocus();
            isValid = false;
        }

        // Email validation
        if (TextUtils.isEmpty(email)) {
            emailLayout.setError("Email is required");
            if (isValid) emailEditText.requestFocus();
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Please enter a valid email");
            if (isValid) emailEditText.requestFocus();
            isValid = false;
        }

        // Password validation
        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError("Password is required");
            if (isValid) passwordEditText.requestFocus();
            isValid = false;
        } else if (password.length() < 6) {
            passwordLayout.setError("Password must be at least 6 characters");
            if (isValid) passwordEditText.requestFocus();
            isValid = false;
        }

        // Phone validation
        if (TextUtils.isEmpty(phone)) {
            phoneLayout.setError("Phone number is required");
            if (isValid) phoneEditText.requestFocus();
            isValid = false;
        } else if (!Pattern.matches("^[0-9]{10,15}$", phone)) {
            phoneLayout.setError("Please enter a valid phone number (10-15 digits)");
            if (isValid) phoneEditText.requestFocus();
            isValid = false;
        }

        return isValid;
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

