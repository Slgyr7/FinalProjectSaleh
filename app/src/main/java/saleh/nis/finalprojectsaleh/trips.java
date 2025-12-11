package saleh.nis.finalprojectsaleh;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import saleh.nis.finalprojectsaleh.data.AppDataBase;
import saleh.nis.finalprojectsaleh.data.TripsTable.Trips;
import saleh.nis.finalprojectsaleh.data.TripsTable.TripsAdapter;

public class trips extends AppCompatActivity {
    private ListView lstTrips;
    private TripsAdapter tripsadapterad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trips);
        lstTrips = findViewById(R.id.lv_trips);
        tripsadapterad = new TripsAdapter(this, R.layout.trip_item_layout);
        lstTrips.setAdapter(tripsadapterad);
        protected void onResume() {
            super.onResume();
            //
        //
            List<Trips> allTrips = AppDataBase.getDB(this).getTripsQuery().getAllTrips();
                tripsadapterad.clear();
                tripsadapterad.addAll(allTrips);
                tripsadapterad.notifyDataSetChanged();

        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}