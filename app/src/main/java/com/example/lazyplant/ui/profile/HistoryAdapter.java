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
import com.example.lazyplant.plantdata.PlantNotes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<PlantCareRecord> records;

    public HistoryAdapter(List<PlantCareRecord> records) {
        this.records = records;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_watering_history, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlantCareRecord record = records.get(position);

        Date date = record.getDate().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String str_date = dateFormat.format(date);

        AppDatabase database = Room.databaseBuilder(holder.desc.getContext(), AppDatabase.class, Constants.GARDEN_DB_NAME)
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();
        GardenPlantDAO dao = database.getGardenPlantDAO();
        String name = dao.getGardenPlant(record.getPlant_id()).get(0).getName();
        String text = name + ": " + str_date;
        holder.desc.setText(text);
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
        public final TextView desc;
        public final TextView details;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            desc = view.findViewById(R.id.history_desc);
            details = view.findViewById(R.id.history_details);
        }
    }

}
