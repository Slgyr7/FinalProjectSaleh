package saleh.nis.finalprojectsaleh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class welcome extends AppCompatActivity {
    // SharedPreferences keys
    private static final String PREF_NAME = "UserData";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    
    // UI elements
    private TextInputEditText emailEditText, passwordEditText;
    private TextInputLayout emailInputLayout, passwordInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Initialize UI elements
        initializeViews();
        setupClickListeners();
        
        // Handle system window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.conlayw), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    
    private void initializeViews() {
        // Find views
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        emailInputLayout = findViewById(R.id.email_input_layout);
        passwordInputLayout = findViewById(R.id.password_input_layout);
    }
    
    private void setupClickListeners() {
        // Login Button
        Button logInButton = findViewById(R.id.login_button);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailInputLayout.setError(null);
                passwordInputLayout.setError(null);
                // Get input values
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if ( attemptLogin() )
                {

                }


                signInUser(email, password);
            }
        });

        // Sign Up Button
        Button signUpButton = findViewById(R.id.regbtn);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(welcome.this, Register.class);
                startActivity(intent);
            }
        });

        // Forgot Password Button
        TextView forgotPasswordButton = findViewById(R.id.forgot_password_button);
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(welcome.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
    }

    private void signInUser(String email, String password) {
        // Sign in with Firebase Authentication
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in successful
                            Toast.makeText(welcome.this, "Log in successful!", Toast.LENGTH_SHORT).show();

                            // Navigate to HomeScreen
                            Intent intent = new Intent(welcome.this, HomeScreen.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            Toast.makeText(welcome.this, "Log in failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private boolean attemptLogin() {
        boolean isValid = true;
        // Reset errors
        emailInputLayout.setError(null);
        passwordInputLayout.setError(null);
        
        // Get values
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        
        // Validate inputs
        if (TextUtils.isEmpty(email)) {
            emailInputLayout.setError("Email is required");
            emailEditText.requestFocus();
            isValid = false;
        }
        
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInputLayout.setError("Please enter a valid email");
            emailEditText.requestFocus();
            isValid = false;
        }
        
        if (TextUtils.isEmpty(password)) {
            passwordInputLayout.setError("Password is required");
            passwordEditText.requestFocus();
            isValid = false;
        }
        
        // Check credentials only if inputs are valid
        if (isValid ) {
            // Login successful
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(welcome.this, HomeScreen.class);
            startActivity(intent);
            finish();
        } else if (isValid) {
            // Login failed (credentials invalid)
            passwordInputLayout.setError("Invalid email or password");
            passwordEditText.requestFocus();
            Toast.makeText(this, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        
        return isValid;
    }

    private boolean validateCredentials(String email, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString(KEY_EMAIL, "");
        String savedPassword = sharedPreferences.getString(KEY_PASSWORD, "");
        
        // In a real app, you would hash the input password and compare with the stored hash
        return email.equals(savedEmail) && password.equals(savedPassword);
    }
}
