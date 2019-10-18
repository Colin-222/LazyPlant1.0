package com.example.lazyplant.ui.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.AppDatabase;
import com.example.lazyplant.plantdata.GardenPlantDAO;
import com.example.lazyplant.plantdata.PlantCareRecord;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WateringHistoryAdapter extends RecyclerView.Adapter<WateringHistoryAdapter.ViewHolder> {

    private List<PlantCareRecord> records;

    public WateringHistoryAdapter(List<PlantCareRecord> records) {
        this.records = records;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_history, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlantCareRecord record = records.get(position);
        Date date = record.getDate().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String str_date = dateFormat.format(date);
        holder.date.setText(str_date);
    }

    @Override
    public int getItemCount() {
        if (records != null) {
            return records.size();
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView date;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            date = view.findViewById(R.id.history_date);
        }
    }

}
