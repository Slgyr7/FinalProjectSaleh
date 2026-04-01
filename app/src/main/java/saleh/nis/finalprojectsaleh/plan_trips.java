package saleh.nis.finalprojectsaleh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        // Set up FAB click listener
        FloatingActionButton fabAddTrip = findViewById(R.id.fab_add_trip);
        fabAddTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to AddTripActivity
                Intent intent = new Intent(plan_trips.this, AddTripActivity.class);
                startActivity(intent);
            }
        });

        // Set up back button click listener
        ImageView backButton = findViewById(R.id.b_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to HomeScreen
                Intent intent = new Intent(plan_trips.this, HomeScreen.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    protected void onResume() {
        super.onResume();
        tripsadapterad.clear();
        List<Trips> allTrips = AppDataBase.getDB(this).getTripsQuery().getAllTrips();
        tripsadapterad.addAll(allTrips);
        tripsadapterad.notifyDataSetChanged();

        //from firebase
        getAllFromFirebase(tripsadapterad);

    }

    private void getAllFromFirebase(TripsAdapter adapter) {
        //عنوان قاعدة البيانات
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // عنوان مجموعة المعطيات داخل قاعدة البيانات
        DatabaseReference myRef = database.getReference("trips");
//إضافة listener مما يسبب الإصغاء لكل تغيير حتلنة عرض المعطيات//
        myRef.addValueEventListener(new ValueEventListener() {
            @Override//دالة معالج حدث تقوم بتلقى نسخة عن كل المعطيات عند أي تغيير
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapter.clear();//حذف كل المعطيات بالوسيط
                for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                    //  استخراج كل المعطيات على وتحويلها لكائن ملائم//
                    Trips trips = taskSnapshot.getValue(Trips.class);
                    adapter.add(trips);//اضافة كل معطى (كائن) للمنسق
                }
                adapter.notifyDataSetChanged();//اعلام المنسق بالتغيير
                Toast.makeText(plan_trips.this, "Data fetched successfully", Toast.LENGTH_SHORT).show();


            }
            @Override//بحالة فشل استخراج المعطيات
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(plan_trips.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }


}