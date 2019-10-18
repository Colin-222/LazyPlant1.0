package com.example.lazyplant.ui.plantDetails;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.lazyplant.Constants;
import com.example.lazyplant.DialogHelper;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.AppDatabase;
import com.example.lazyplant.plantdata.DbAccess;
import com.example.lazyplant.plantdata.PlantInfoEntity;
import com.example.lazyplant.plantdata.PlantNotes;
import com.example.lazyplant.plantdata.PlantNotesDAO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PlantDetailsAdapter extends RecyclerView.Adapter<PlantDetailsAdapter.ViewHolder> {
    private List<List<String>> details;

    public PlantDetailsAdapter(List<List<String>> details) {
        this.details = details;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_plant_detail, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<String> detail = details.get(position);
        holder.icon.setImageResource(getIcon(detail.get(0)));
        String text = detail.get(0) + ": " + detail.get(1);
        holder.desc.setText(text);
    }

    private int getIcon(String category){
        switch(category) {
            case "Height":
                return R.drawable.ic_height;
            case "Width":
                return R.drawable.ic_width;
            case "Frost tolerance":
                return R.drawable.ic_heavy_frost;
            case "Shade":
                return R.drawable.ic_sunny;
            case "Flowering period":
                return R.drawable.ic_flower;
            case "Food":
                return R.drawable.ic_food;
            case "Wildlife Attracted":
                return R.drawable.ic_pawprint;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        if (details != null) {
            return details.size();
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView desc;
        public final ImageView icon;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            desc = view.findViewById(R.id.plant_detail_text);
            icon = view.findViewById(R.id.plant_detail_icon);
        }
    }

}
