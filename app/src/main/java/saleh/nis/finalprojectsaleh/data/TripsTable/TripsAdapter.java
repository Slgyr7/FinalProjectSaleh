package saleh.nis.finalprojectsaleh.data.TripsTable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import saleh.nis.finalprojectsaleh.R;

public class TripsAdapter extends ArrayAdapter<Trips> {

    // BAGRUT: We keep two lists. One for all data, one for filtered data (Search)
    private List<Trips> originalList;
    private List<Trips> filteredList;

    public TripsAdapter(@NonNull Context context, int resource) {
        super(context, resource, new ArrayList<>());
        this.originalList = new ArrayList<>();
        this.filteredList = new ArrayList<>();
    }

    // BAGRUT: This method updates our lists when data comes from Firebase
    public void setList(List<Trips> list) {
        this.originalList.clear();
        this.originalList.addAll(list);
        this.filteredList.clear();
        this.filteredList.addAll(list);
        clear();
        addAll(filteredList);
        notifyDataSetChanged(); // Refresh the ListView
    }

    // BAGRUT: This is the search logic. It checks if title or category contains the text
    public void filter(String text) {
        filteredList.clear();
        if (text.isEmpty()) {
            // If search is empty, show everything
            filteredList.addAll(originalList);
        } else {
            text = text.toLowerCase();
            for (Trips item : originalList) {
                // Check if title or category matches the search text
                if (item.getTitle().toLowerCase().contains(text) || 
                    item.getCategory().toLowerCase().contains(text)) {
                    filteredList.add(item);
                }
            }
        }
        clear();
        addAll(filteredList);
        notifyDataSetChanged(); // Refresh the list with only matching items
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View vitem = convertView;
        if (vitem == null) {
            vitem = LayoutInflater.from(getContext()).inflate(R.layout.trip_item_layout, parent, false);
        }

        // Connect the UI components from the layout
        ImageView imageview = vitem.findViewById(R.id.iv_trip_attraction);
        TextView title = vitem.findViewById(R.id.tv_title);
        TextView status = vitem.findViewById(R.id.tv_status);
        TextView rating = vitem.findViewById(R.id.tv_rating);
        TextView address = vitem.findViewById(R.id.tv_address);
        Chip categoryChip = vitem.findViewById(R.id.chip_category);
        ChipGroup vibesChipGroup = vitem.findViewById(R.id.cg_vibes);

        // Get the specific trip for this row
        Trips currentTrip = getItem(position);

        if (currentTrip != null) {
            // Set the values to the UI
            title.setText(currentTrip.getTitle());
            status.setText(currentTrip.getStatus());
            rating.setText(String.valueOf(currentTrip.getRating()));
            address.setText(currentTrip.getAddress());
            categoryChip.setText(currentTrip.getCategory());

            // Handle the Chips for "vibes"
            vibesChipGroup.removeAllViews();
            String vibesString = currentTrip.getVibes();
            if (vibesString != null && !vibesString.isEmpty()) {
                String[] vibesArray = vibesString.split(",");
                for (String vibeText : vibesArray) {
                    Chip chip = new Chip(getContext());
                    chip.setText(vibeText.trim());
                    vibesChipGroup.addView(chip);
                }
            }
        }

        return vitem;
    }
}
