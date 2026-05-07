package saleh.nis.finalprojectsaleh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import saleh.nis.finalprojectsaleh.data.AppDataBase;
import saleh.nis.finalprojectsaleh.data.TripsTable.Trips;
import saleh.nis.finalprojectsaleh.data.TripsTable.TripsAdapter;

public class plan_trips extends AppCompatActivity {
    private ListView lstTrips;
    private TripsAdapter tripsadapterad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_plan_trips);

        lstTrips = findViewById(R.id.lv_trips);
        tripsadapterad = new TripsAdapter(this, R.layout.trip_item_layout);
        lstTrips.setAdapter(tripsadapterad);

        // --- OPEN SITE ACTIVITY ON CLICK ---
        lstTrips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trips selectedTrip = tripsadapterad.getItem(position);
                if (selectedTrip != null) {
                    Intent intent = new Intent(plan_trips.this, site.class);
                    // Pass the whole object (it works because we made Trips Serializable)
                    intent.putExtra("TRIP_DATA", selectedTrip);
                    startActivity(intent);
                }
            }
        });

        FloatingActionButton fabAddTrip = findViewById(R.id.fab_add_trip);
        fabAddTrip.setOnClickListener(v -> {
            Intent intent = new Intent(plan_trips.this, AddTripActivity.class);
            startActivity(intent);
        });

        ImageView backButton = findViewById(R.id.b_btn);
        backButton.setOnClickListener(v -> finish());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllFromFirebase(tripsadapterad);
    }

    private void getAllFromFirebase(TripsAdapter adapter) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("trips");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapter.clear();
                for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                    Trips trips = taskSnapshot.getValue(Trips.class);
                    if (trips != null) adapter.add(trips);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(plan_trips.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
