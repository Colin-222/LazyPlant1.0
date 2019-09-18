package com.example.lazyplant.ui.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.DbAccess;
import com.example.lazyplant.plantdata.PlantInfoEntity;
import com.example.lazyplant.plantdata.PlantNotes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
    final private String DATE_DESC = "Last edited: ";
    private List<PlantNotes> notes;

    public NotesAdapter(List<PlantNotes> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notes, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlantNotes note = notes.get(position);
        DbAccess databaseAccess = DbAccess.getInstance(holder.title.getContext());
        databaseAccess.open();
        String common_name = databaseAccess.getShortPlantInfo(note.getSpecies_id()).getCommon_name();
        databaseAccess.close();
        holder.title.setText(common_name);

        Date date = note.getLast_edit().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String d = dateFormat.format(date);
        holder.date.setText(this.DATE_DESC + ": " + d);
        holder.text.setText(note.getNotes());
    }

    @Override
    public int getItemCount() {
        if (notes != null) {
            return notes.size();
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView title;
        public final TextView date;
        public final TextView text;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            title = view.findViewById(R.id.notes_title);
            date = view.findViewById(R.id.notes_date);
            text = view.findViewById(R.id.notes_text);
        }
    }

}
