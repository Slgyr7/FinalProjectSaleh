package saleh.nis.finalprojectsaleh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import saleh.nis.finalprojectsaleh.data.TripsTable.Trips;
import saleh.nis.finalprojectsaleh.data.TripsTable.TripsAdapter;

// BAGRUT: This activity shows the user's favorite trips (liked trips)
public class my_trips extends AppCompatActivity {

    private ListView lvFavoriteTrips;
    private TripsAdapter adapter;
    private TextView emptyMessage;
    private ImageView backButton;
    
    // BAGRUT: For favorites functionality
    private static final String PREF_NAME = "UserPrefs";
    private static final String FAVORITES_KEY = "favorite_trips";
    private DatabaseReference tripsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_trips);

        // BAGRUT: Initialize views
        lvFavoriteTrips = findViewById(R.id.lv_favorite_trips);
        emptyMessage = findViewById(R.id.empty_message);
        backButton = findViewById(R.id.back_btn);
        
        tripsRef = FirebaseDatabase.getInstance().getReference("trips");
        
        adapter = new TripsAdapter(this, R.layout.trip_item_layout);
        lvFavoriteTrips.setAdapter(adapter);

        // BAGRUT: Back button to return to home screen
        backButton.setOnClickListener(v -> finish());

        // BAGRUT: Click on a favorite trip to view full details
        lvFavoriteTrips.setOnItemClickListener((parent, view, position, id) -> {
            Trips selectedTrip = adapter.getItem(position);
            if (selectedTrip != null) {
                Intent intent = new Intent(my_trips.this, site.class);
                intent.putExtra("TRIP_DATA", selectedTrip);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // BAGRUT: Load favorite trips from Firebase
        loadFavoriteTrips();
    }

    // BAGRUT: Get the user's favorite trip keys from SharedPreferences
    private Set<String> getFavoriteTripKeys() {
        SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sp.getStringSet(FAVORITES_KEY, new HashSet<>());
    }

    // BAGRUT: Load all trips from Firebase and filter to show only favorites
    private void loadFavoriteTrips() {
        tripsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String> favoriteKeys = getFavoriteTripKeys();
                List<Trips> favoriteTrips = new ArrayList<>();

                // BAGRUT: Filter trips to show only those in favorites
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Trips trip = postSnapshot.getValue(Trips.class);
                    if (trip != null && favoriteKeys.contains(postSnapshot.getKey())) {
                        trip.setTripsKey(postSnapshot.getKey());
                        favoriteTrips.add(trip);
                    }
                }

                // BAGRUT: Update the adapter with favorite trips
                if (favoriteTrips.isEmpty()) {
                    // BAGRUT: Show empty message if no favorites
                    lvFavoriteTrips.setVisibility(View.GONE);
                    emptyMessage.setVisibility(View.VISIBLE);
                    emptyMessage.setText("No favorite trips yet.\nClick the heart icon on any trip to add it here!");
                } else {
                    // BAGRUT: Show the list of favorite trips
                    lvFavoriteTrips.setVisibility(View.VISIBLE);
                    emptyMessage.setVisibility(View.GONE);
                    adapter.setList(favoriteTrips);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(my_trips.this, "Error loading favorites", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // BAGRUT: Refresh the list when returning to this activity
        loadFavoriteTrips();
    }
}