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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import saleh.nis.finalprojectsaleh.data.UserProfileTable.MyUser;

public class welcome extends AppCompatActivity {
    // SharedPreferences name (Matching Register.java)
    private static final String PREF_NAME = "UserPrefs";
    
    // UI elements
    private TextInputEditText emailEditText, passwordEditText;
    private TextInputLayout emailInputLayout, passwordInputLayout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mAuth = FirebaseAuth.getInstance();
        
        // --- STEP 2: AUTO-LOGIN CHECK ---
        checkAutoLogin();

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

    private void checkAutoLogin() {
        SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        boolean isLoggedIn = sp.getBoolean("isLoggedIn", false);
        
        if (isLoggedIn) {
            String role = sp.getString("userRole", "");
            redirectBasedOnRole(role);
        }
    }

    private void redirectBasedOnRole(String role) {
        Intent intent;
        if (MyUser.ROLE_ADMIN.equals(role)) {
            intent = new Intent(welcome.this, AdminHomeScreen.class);
        } else {
            intent = new Intent(welcome.this, HomeScreen.class);
        }
        startActivity(intent);
        finish();
    }
    
    private void initializeViews() {
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        emailInputLayout = findViewById(R.id.email_input_layout);
        passwordInputLayout = findViewById(R.id.password_input_layout);
    }
    
    private void setupClickListeners() {
        Button logInButton = findViewById(R.id.login_button);
        logInButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (attemptLogin()) {
                signInUser(email, password);
            }
        });

        Button signUpButton = findViewById(R.id.regbtn);
        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(welcome.this, Register.class);
            startActivity(intent);
        });

        TextView forgotPasswordButton = findViewById(R.id.forgot_password_button);
        forgotPasswordButton.setOnClickListener(v -> {
            Intent intent = new Intent(welcome.this, ForgotPassword.class);
            startActivity(intent);
        });
    }

    private void signInUser(String email, String password) {
        // --- STEP 1: Firebase Authentication ---
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();

                        // --- FETCH ROLE FROM FIREBASE ---
                        FirebaseDatabase.getInstance().getReference("users").child(uid).get()
                                .addOnCompleteListener(dbTask -> {
                                    if (dbTask.isSuccessful()) {
                                        DataSnapshot snapshot = dbTask.getResult();
                                        if (snapshot.exists()) {
                                            // Extract data
                                            String name = snapshot.child("fullName").getValue(String.class);
                                            String userEmail = snapshot.child("email").getValue(String.class);
                                            String role = snapshot.child("role").getValue(String.class);

                                            // Save locally
                                            saveUserData(name, userEmail, role);

                                            // Redirect
                                            Toast.makeText(welcome.this, "Welcome back, " + name, Toast.LENGTH_SHORT).show();
                                            redirectBasedOnRole(role);
                                        } else {
                                            Toast.makeText(welcome.this, "User profile not found in database", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(welcome.this, "Error fetching user profile", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(welcome.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserData(String name, String email, String role) {
        SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("userName", name);
        editor.putString("userEmail", email);
        editor.putString("userRole", role);
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    private boolean attemptLogin() {
        boolean isValid = true;
        emailInputLayout.setError(null);
        passwordInputLayout.setError(null);

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailInputLayout.setError("Email is required");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInputLayout.setError("Valid email required");
            isValid = false;
        }
        
        if (TextUtils.isEmpty(password)) {
            passwordInputLayout.setError("Password is required");
            isValid = false;
        }
        
        return isValid;
    }
}
