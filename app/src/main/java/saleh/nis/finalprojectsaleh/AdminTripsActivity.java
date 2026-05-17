package saleh.nis.finalprojectsaleh;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import saleh.nis.finalprojectsaleh.data.TripsTable.Trips;
import saleh.nis.finalprojectsaleh.data.TripsTable.TripsAdapter;

/**
 * BAGRUT: The list of trips for the Admin.
 * Clicking a trip opens 'AdminSiteActivity' which has the management tools.
 */
public class AdminTripsActivity extends AppCompatActivity {

    private ListView lvAdminTrips;
    private TripsAdapter adapter;
    private DatabaseReference tripsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_trips);

        lvAdminTrips = findViewById(R.id.lv_admin_trips);
        tripsRef = FirebaseDatabase.getInstance().getReference("trips");

        adapter = new TripsAdapter(this, R.layout.trip_item_layout);
        lvAdminTrips.setAdapter(adapter);

        // BAGRUT: Open the dedicated Admin edition of the site details
        lvAdminTrips.setOnItemClickListener((parent, view, position, id) -> {
            Trips selectedTrip = adapter.getItem(position);
            if (selectedTrip != null) {
                Intent intent = new Intent(AdminTripsActivity.this, AdminSiteActivity.class);
                intent.putExtra("TRIP_DATA", selectedTrip);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.fab_add_new).setOnClickListener(v -> {
            startActivity(new Intent(this, AddTripActivity.class));
        });

        loadTrips();
    }

    private void loadTrips() {
        tripsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Trips> tripList = new ArrayList<>();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Trips trip = postSnapshot.getValue(Trips.class);
                    if (trip != null) {
                        trip.setTripsKey(postSnapshot.getKey());
                        tripList.add(trip);
                    }
                }
                adapter.setList(tripList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
