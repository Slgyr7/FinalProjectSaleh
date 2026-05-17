package saleh.nis.finalprojectsaleh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import saleh.nis.finalprojectsaleh.data.TripsTable.Trips;
import saleh.nis.finalprojectsaleh.data.TripsTable.TripsAdapter;
import saleh.nis.finalprojectsaleh.data.UserProfileTable.MyUser;

/**
 * BAGRUT EXPLANATION: This activity displays the list of trips.
 * It uses ROLE-BASED navigation:
 * - ADMINS go to 'AdminSiteActivity' to manage trips.
 * - CUSTOMERS go to 'site' activity to view and favorite trips.
 */
public class plan_trips extends AppCompatActivity {
    private ListView lstTrips;
    private TripsAdapter tripsadapterad;
    private SearchView searchView;
    private static final String PREF_NAME = "UserPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_plan_trips);

        lstTrips = findViewById(R.id.lv_trips);
        searchView = findViewById(R.id.search_view);
        tripsadapterad = new TripsAdapter(this, R.layout.trip_item_layout);
        lstTrips.setAdapter(tripsadapterad);

        SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String role = sp.getString("userRole", "");

        // BAGRUT: SEARCH logic
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                tripsadapterad.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                tripsadapterad.filter(newText);
                return true;
            }
        });

        // BAGRUT: FIXED Problem 3/4 - Role-based navigation when clicking a trip
        lstTrips.setOnItemClickListener((parent, view, position, id) -> {
            Trips selectedTrip = tripsadapterad.getItem(position);
            if (selectedTrip != null) {
                Intent intent;
                if (MyUser.ROLE_ADMIN.equals(role)) {
                    // ADMIN EDITION: Full info + Edit/Delete tools
                    intent = new Intent(plan_trips.this, AdminSiteActivity.class);
                } else {
                    // CUSTOMER EDITION: Full info + Favorite tool
                    intent = new Intent(plan_trips.this, site.class);
                }
                intent.putExtra("TRIP_DATA", selectedTrip);
                startActivity(intent);
            }
        });

        // BAGRUT: Only Admins can see the button to ADD new trips
        FloatingActionButton fabAddTrip = findViewById(R.id.fab_add_trip);
        if (MyUser.ROLE_ADMIN.equals(role)) {
            fabAddTrip.setVisibility(View.VISIBLE);
            fabAddTrip.setOnClickListener(v -> startActivity(new Intent(this, AddTripActivity.class)));
        } else {
            fabAddTrip.setVisibility(View.GONE);
        }

        findViewById(R.id.b_btn).setOnClickListener(v -> finish());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllFromFirebase();
    }

    private void getAllFromFirebase() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("trips");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Trips> tripList = new ArrayList<>();
                for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                    Trips trips = taskSnapshot.getValue(Trips.class);
                    if (trips != null) {
                        trips.setTripsKey(taskSnapshot.getKey());
                        tripList.add(trips);
                    }
                }
                tripsadapterad.setList(tripList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(plan_trips.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
