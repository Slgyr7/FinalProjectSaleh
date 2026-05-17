package saleh.nis.finalprojectsaleh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import saleh.nis.finalprojectsaleh.data.UserProfileTable.MyUser;

/**
 * BAGRUT: The Login Screen.
 * FIXED: Google login window and auto-login logic added without deleting original code.
 */
public class welcome extends AppCompatActivity {
    private static final String PREF_NAME = "UserPrefs";
    
    private TextInputEditText emailEditText, passwordEditText;
    private TextInputLayout emailInputLayout, passwordInputLayout;
    private FirebaseAuth mAuth;
    private CredentialManager credentialManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mAuth = FirebaseAuth.getInstance();
        credentialManager = CredentialManager.create(this);
        
        // Auto-login: If user data exists, go straight to Home
        checkAutoLogin();

        setContentView(R.layout.activity_welcome);

        initializeViews();
        setupClickListeners();
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.conlayw), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void checkAutoLogin() {
        SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        if (sp.getBoolean("isLoggedIn", false)) {
            redirectBasedOnRole(sp.getString("userRole", ""));
        }
    }

    private void redirectBasedOnRole(String role) {
        Intent intent;
        if (MyUser.ROLE_ADMIN.equals(role)) {
            intent = new Intent(this, AdminHomeScreen.class);
        } else {
            intent = new Intent(this, HomeScreen.class);
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
        findViewById(R.id.login_button).setOnClickListener(v -> {
            if (attemptLogin()) {
                signInUser(emailEditText.getText().toString().trim(), passwordEditText.getText().toString().trim());
            }
        });

        findViewById(R.id.regbtn).setOnClickListener(v -> startActivity(new Intent(this, Register.class)));
        findViewById(R.id.forgot_password_button).setOnClickListener(v -> startActivity(new Intent(this, ForgotPassword.class)));

        // Fixed Google Login
        MaterialButton googleBtn = findViewById(R.id.google_login_button);
        googleBtn.setOnClickListener(v -> signInWithGoogle());
    }

    private void signInWithGoogle() {
        String webClientId = "732526560877-af9r3gesq34pqdea0tle3v38i5dpfaj5.apps.googleusercontent.com";

        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(webClientId)
                .setAutoSelectEnabled(false) // ACCOUNT PICKER FIX
                .build();

        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        credentialManager.getCredentialAsync(
                this,
                request,
                null,
                ContextCompat.getMainExecutor(this),
                new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                    @Override
                    public void onResult(GetCredentialResponse result) {
                        handleGoogleSignIn(result.getCredential());
                    }

                    @Override
                    public void onError(@NonNull GetCredentialException e) {
                        Log.e("GoogleAuth", "Error: " + e.getMessage());
                        Toast.makeText(welcome.this, "Sign-In failed. Check SHA-1/Console.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void handleGoogleSignIn(Credential credential) {
        if (credential instanceof GoogleIdTokenCredential) {
            GoogleIdTokenCredential googleIdTokenCredential = (GoogleIdTokenCredential) credential;
            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.getIdToken(), null);
            
            mAuth.signInWithCredential(firebaseCredential).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    checkUserInDatabase(mAuth.getCurrentUser());
                } else {
                    Toast.makeText(this, "Firebase Auth failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void checkUserInDatabase(FirebaseUser user) {
        if (user == null) return;

        FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DataSnapshot snapshot = task.getResult();
                        if (snapshot.exists()) {
                            String name = snapshot.child("fullName").getValue(String.class);
                            String role = snapshot.child("role").getValue(String.class);
                            saveUserData(name, user.getEmail(), role);
                            redirectBasedOnRole(role);
                        } else {
                            MyUser newUser = new MyUser();
                            newUser.setFullName(user.getDisplayName());
                            newUser.setEmail(user.getEmail());
                            newUser.setRole(MyUser.ROLE_CUSTOMER);
                            
                            FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).setValue(newUser)
                                    .addOnCompleteListener(dbTask -> {
                                        saveUserData(user.getDisplayName(), user.getEmail(), MyUser.ROLE_CUSTOMER);
                                        redirectBasedOnRole(MyUser.ROLE_CUSTOMER);
                                    });
                        }
                    }
                });
    }

    private void signInUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        checkUserInDatabase(mAuth.getCurrentUser());
                    } else {
                        Toast.makeText(welcome.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserData(String name, String email, String role) {
        SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putString("userName", name);
        editor.putString("userEmail", email);
        editor.putString("userRole", role);
        editor.putBoolean("isLoggedIn", true);
        editor.commit(); // commit is sync and ensures data is written before redirect
    }

    private boolean attemptLogin() {
        emailInputLayout.setError(null);
        passwordInputLayout.setError(null);

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInputLayout.setError("Valid email required");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            passwordInputLayout.setError("Password is required");
            return false;
        }
        return true;
    }
}
