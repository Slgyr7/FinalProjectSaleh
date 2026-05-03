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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

import saleh.nis.finalprojectsaleh.data.AppDataBase;
import saleh.nis.finalprojectsaleh.data.UserProfileTable.MyUser;

public class Register extends AppCompatActivity {

    private ImageView backButton;
    private Button createAccountButton;
    private TextInputEditText usernameEditText, emailEditText, passwordEditText, phoneEditText;
    private TextInputLayout usernameLayout, emailLayout, passwordLayout, phoneLayout;
    private RadioGroup roleRadioGroup;
    private RadioButton radioCustomer, radioOrganization;
    private FirebaseAuth mAuth;

    // Use "UserPrefs" as requested by the user
    private static final String PREF_NAME = "UserPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.conlayr), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI components
        usernameEditText = findViewById(R.id.ET_username);
        emailEditText = findViewById(R.id.ET_email);
        passwordEditText = findViewById(R.id.ET_password);
        phoneEditText = findViewById(R.id.ET_phone);
        usernameLayout = findViewById(R.id.usernameLayout);
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        phoneLayout = findViewById(R.id.phoneLayout);
        roleRadioGroup = findViewById(R.id.roleRadioGroup);
        radioCustomer = findViewById(R.id.radioCustomer);
        radioOrganization = findViewById(R.id.radioOrganization);
        backButton = findViewById(R.id.back_btn);
        createAccountButton = findViewById(R.id.btn_create);

        backButton.setOnClickListener(v -> {
            startActivity(new Intent(Register.this, welcome.class));
            finish();
        });

        createAccountButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();

            if (validateInputs(username, email, password, phone)) {
                signUpUser(username, email, password, phone);
            }
        });
    }

    private void signUpUser(String username, String email, String password, String phone) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();
                        
                        String role = radioOrganization.isChecked() ? MyUser.ROLE_ADMIN : MyUser.ROLE_CUSTOMER;

                        MyUser user = new MyUser();
                        user.setFullName(username);
                        user.setEmail(email);
                        user.setPhone(phone);
                        user.setPassw(password);
                        user.setRole(role);

                        FirebaseDatabase.getInstance().getReference("users")
                                .child(uid)
                                .setValue(user)
                                .addOnCompleteListener(dbTask -> {
                                    if (dbTask.isSuccessful()) {
                                        // Save to local Room database
                                        AppDataBase.getDB(getApplicationContext()).getMyUserQuery().insert(user);
                                        
                                        // Save to SharedPreferences for auto-login
                                        saveUserData(username, email, role);
                                        
                                        Toast.makeText(Register.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                        
                                        // ROLE-BASED ROUTING
                                        Intent intent;
                                        if (MyUser.ROLE_ADMIN.equals(role)) {
                                            intent = new Intent(Register.this, AdminHomeScreen.class);
                                        } else {
                                            intent = new Intent(Register.this, HomeScreen.class);
                                        }
                                        startActivity(intent);
                                        finishAffinity();
                                    } else {
                                        Toast.makeText(Register.this, "Database Error: " + dbTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(Register.this, "Auth Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean validateInputs(String username, String email, String password, String phone) {
        boolean isValid = true;
        usernameLayout.setError(null);
        emailLayout.setError(null);
        passwordLayout.setError(null);
        phoneLayout.setError(null);

        if (TextUtils.isEmpty(username)) {
            usernameLayout.setError("Username required");
            isValid = false;
        }
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Valid email required");
            isValid = false;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            passwordLayout.setError("Password (min 6 chars) required");
            isValid = false;
        }
        if (TextUtils.isEmpty(phone) || !Pattern.matches("^[0-9]{10,15}$", phone)) {
            phoneLayout.setError("Valid phone required");
            isValid = false;
        }
        return isValid;
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
}
