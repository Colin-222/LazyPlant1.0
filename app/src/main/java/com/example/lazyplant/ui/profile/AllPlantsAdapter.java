package com.example.lazyplant.ui.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.GardenPlant;
import com.example.lazyplant.plantdata.PlantNotes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AllPlantsAdapter extends RecyclerView.Adapter<AllPlantsAdapter.ViewHolder> {

    private List<GardenPlant> gp;

    public AllPlantsAdapter(List<GardenPlant> gp) {
        this.gp = gp;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_all_plants, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GardenPlant p = gp.get(position);
        holder.name.setText(p.getName());
        Calendar c = p.getLast_watering();
        c.add(Calendar.DATE, p.getWatering_interval());
        Date date = c.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String d = dateFormat.format(date);
        holder.next_watering.setText(d);
    }

    @Override
    public int getItemCount() {
        if (gp != null) {
            return gp.size();
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView name;
        public final TextView next_watering;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.ap_name);
            next_watering = view.findViewById(R.id.ap_next_watering);
        }
    }

}
