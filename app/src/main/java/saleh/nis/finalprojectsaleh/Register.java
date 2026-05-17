package saleh.nis.finalprojectsaleh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import saleh.nis.finalprojectsaleh.data.UserProfileTable.MyUser;

/**
 * BAGRUT: Fixed Problem 3 (Sign-Up Redirect).
 * Saves user role to SharedPreferences and clears the login stack.
 */
public class Register extends AppCompatActivity {

    private TextInputEditText usernameEditText, emailEditText, passwordEditText, phoneEditText;
    private TextInputLayout usernameLayout, emailLayout, passwordLayout, phoneLayout;
    private RadioButton radioOrganization;
    private FirebaseAuth mAuth;
    private static final String PREF_NAME = "UserPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        initializeViews();

        findViewById(R.id.back_btn).setOnClickListener(v -> finish());
        findViewById(R.id.btn_create).setOnClickListener(v -> signUpUser());
    }

    private void initializeViews() {
        usernameEditText = findViewById(R.id.ET_username);
        emailEditText = findViewById(R.id.ET_email);
        passwordEditText = findViewById(R.id.ET_password);
        phoneEditText = findViewById(R.id.ET_phone);
        usernameLayout = findViewById(R.id.usernameLayout);
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        phoneLayout = findViewById(R.id.phoneLayout);
        radioOrganization = findViewById(R.id.radioOrganization);
    }

    private void signUpUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();
        String role = radioOrganization.isChecked() ? MyUser.ROLE_ADMIN : MyUser.ROLE_CUSTOMER;

        if (TextUtils.isEmpty(email) || password.length() < 6) return;

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();
                        MyUser user = new MyUser();
                        user.setFullName(username);
                        user.setEmail(email);
                        user.setRole(role);

                        FirebaseDatabase.getInstance().getReference("users").child(uid).setValue(user)
                                .addOnCompleteListener(dbTask -> {
                                    if (dbTask.isSuccessful()) {
                                        // commit() ensures data is saved BEFORE activity switch
                                        saveUserDataImmediate(username, email, role);
                                        
                                        Intent intent = role.equals(MyUser.ROLE_ADMIN) ? 
                                                new Intent(Register.this, AdminHomeScreen.class) : 
                                                new Intent(Register.this, HomeScreen.class);
                                        
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                    }
                });
    }

    private void saveUserDataImmediate(String name, String email, String role) {
        SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putString("userName", name);
        editor.putString("userEmail", email);
        editor.putString("userRole", role);
        editor.putBoolean("isLoggedIn", true);
        editor.commit(); 
    }
}
