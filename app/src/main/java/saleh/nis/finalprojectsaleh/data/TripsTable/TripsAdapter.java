package saleh.nis.finalprojectsaleh.data.TripsTable;

import static android.util.Log.d;

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

import saleh.nis.finalprojectsaleh.R;

public class TripsAdapter extends ArrayAdapter<Trips> {

private  final int itemLayout;
    public TripsAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.itemLayout = resource;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View vitem= convertView;
        if(vitem==null)
            vitem= LayoutInflater.from(getContext()).inflate(itemLayout,parent,false);


ImageView imageview = vitem.findViewById(R.id.iv_trip_attraction);
        TextView title = vitem.findViewById(R.id.tv_title);
        TextView status = vitem.findViewById(R.id.tv_status);
        TextView rating = vitem.findViewById(R.id.tv_rating);
        TextView address = vitem.findViewById(R.id.tv_address);
        TextView price = vitem.findViewById(R.id.tv_price);
        TextView duration = vitem.findViewById(R.id.tv_duration);
        TextView date = vitem.findViewById(R.id.tv_date);
        Chip chip = vitem.findViewById(R.id.chip_category);
Trips current=getItem(position);
title.setText(current.getTitle());
status.setText(current.getStatus());
rating.setText(String.valueOf(current.getRating()));
address.setText(current.getAddress());
price.setText(String.valueOf(current.getPrice()));
duration.setText(current.getDuration());
date.setText(current.getDate());
chip.setText(current.getCategory());
        return vitem;
    }
}
