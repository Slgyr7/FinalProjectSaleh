package saleh.nis.finalprojectsaleh;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

public class AddTripActivity extends AppCompatActivity {
    private ImageView ivTripImage ;
    private TextInputEditText etTitle, etAddress, etPrice, etRating;
    private ChipGroup categoryChipGroup, vibesChipGroup;
    private Button btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_trip);


        ivTripImage = findViewById(R.id.ivTripImage);
        etTitle= findViewById(R.id.etTitle);
        etAddress= findViewById(R.id.etAdress);
        etPrice= findViewById(R.id.etPrice);
        etRating= findViewById(R.id.etRating);
        categoryChipGroup= findViewById(R.id.categoryChipGroup);
        vibesChipGroup= findViewById(R.id.vibesChipGroup);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(v -> {



            });

    }
}