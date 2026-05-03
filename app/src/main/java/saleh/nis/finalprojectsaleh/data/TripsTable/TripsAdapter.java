package saleh.nis.finalprojectsaleh.data.TripsTable;

// 1. REMOVED the incorrect import for getContext

import android.content.Context; // Correct context import
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter; // 2. IMPORT for ArrayAdapter
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import saleh.nis.finalprojectsaleh.R;
import saleh.nis.finalprojectsaleh.data.TripsTable.Trips;

// 3. THE CLASS must extend ArrayAdapter
public class TripsAdapter extends ArrayAdapter<Trips> {

    // 4. A constructor is needed to get the context and layout
    public TripsAdapter(@NonNull Context context, int resource) {
        super(context, resource, 0); // Pass the context up to the parent class
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View vitem = convertView;
        if (vitem == null) {
            // 5. Use getContext() from the ArrayAdapter's own methods
            vitem = LayoutInflater.from(getContext()).inflate(R.layout.trip_item_layout, parent, false);
        }

        // --- Find all the views ---
        ImageView imageview = vitem.findViewById(R.id.iv_trip_attraction);
        TextView title = vitem.findViewById(R.id.tv_title);
        TextView status = vitem.findViewById(R.id.tv_status);
        TextView rating = vitem.findViewById(R.id.tv_rating);
        TextView address = vitem.findViewById(R.id.tv_address);

        Chip categoryChip = vitem.findViewById(R.id.chip_category);
        ChipGroup vibesChipGroup = vitem.findViewById(R.id.cg_vibes);

        // --- Get the current trip object ---
        Trips currentTrip = getItem(position);

        // --- Set the data ---
        if (currentTrip != null) {
            title.setText(currentTrip.getTitle());
            status.setText(currentTrip.getStatus());
            rating.setText(String.valueOf(currentTrip.getRating()));
            address.setText(currentTrip.getAddress());

            // A. Set the text for the single category chip
            categoryChip.setText(currentTrip.getCategory());

            // B. Handle the multiple vibes chips
            vibesChipGroup.removeAllViews(); // Clear old chips

            String vibesString = currentTrip.getVibes(); // e.g., "Historic, Outdoors, Romance"
            if (vibesString != null && !vibesString.isEmpty()) {
                String[] vibesArray = vibesString.split(",");

                for (String vibeText : vibesArray) {
                    // --- 6. FIX for creating and styling chips ---
                    // Create a new Chip using the context from the adapter
                    Chip chip = new Chip(getContext());
                    chip.setText(vibeText.trim());
                    // You can apply styles directly if needed, but the default often works well
                    // For example, to set colors:
                    // chip.setChipBackgroundColorResource(R.color.your_chip_color);
                    // chip.setTextColor(getResources().getColor(R.color.white));

                    vibesChipGroup.addView(chip);
                }
            }
        }

        return vitem;
    }
}
