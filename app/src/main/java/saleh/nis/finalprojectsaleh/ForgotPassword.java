package saleh.nis.finalprojectsaleh;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

/**
 * BAGRUT EXPLANATION: This activity handles password recovery.
 * It uses Firebase Authentication's built-in 'sendPasswordResetEmail' method.
 */
public class ForgotPassword extends AppCompatActivity {

    private TextInputEditText etEmail;
    private MaterialButton btnReset;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.et_email);
        btnReset = findViewById(R.id.btn_send_reset_link);
        progressBar = findViewById(R.id.progress_bar);
        ImageView backButton = findViewById(R.id.back_btn);

        backButton.setOnClickListener(v -> finish());

        btnReset.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (validateEmail(email)) {
                sendResetEmail(email);
            }
        });
    }

    private boolean validateEmail(String email) {
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid registered email");
            return false;
        }
        return true;
    }

    /**
     * BAGRUT: If the email exists in Firebase Auth, Google will send a reset link automatically.
     * If no email is received, double-check that the email is actually in your 'Authentication' list.
     */
    private void sendResetEmail(String email) {
        progressBar.setVisibility(View.VISIBLE);
        btnReset.setEnabled(false);

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    btnReset.setEnabled(true);

                    if (task.isSuccessful()) {
                        // SUCCESS: Feedback to the user
                        Toast.makeText(ForgotPassword.this, "Reset link sent! Check your Spam folder too.", Toast.LENGTH_LONG).show();
                        finish(); // Go back to login
                    } else {
                        // FAILURE: Show exact error (e.g. User not found)
                        String error = task.getException() != null ? task.getException().getMessage() : "Error occurred";
                        Toast.makeText(ForgotPassword.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
