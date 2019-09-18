package com.example.lazyplant.ui.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.PlantNotes;

import java.util.Calendar;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

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
        holder.title.setText(note.getSpecies_id());
        Calendar c = note.getLast_edit();
        String d = c.DAY_OF_MONTH + "/" + c.MONTH + "/" + c.YEAR;
        holder.date.setText(d);
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
