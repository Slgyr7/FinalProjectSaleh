package saleh.nis.finalprojectsaleh; // Make sure this package name is correct

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button; // Make sure to import Button
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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // --- CORRECTED CODE TO ADD ---

        // 1. Find the "Sign Up" button using its CORRECT ID from the XML
        Button signUpButton = findViewById(R.id.regbtn); // Use R.id.regbtn, NOT a different ID

        // 2. Set an OnClickListener on the button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This code will run when the "Sign Up" button is clicked

                // 3. Create an Intent to open the Register activity
                Intent intent = new Intent(welcome.this, Register.class);

                // 4. Start the new activity
                startActivity(intent);
            }
        });

        // --- End of the corrected code ---
    }
}
