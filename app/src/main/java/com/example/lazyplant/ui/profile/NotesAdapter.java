package com.example.lazyplant.ui.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

    public void refresh(int position){
        this.notes.remove(position);
        notifyDataSetChanged();
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
        final int pos = position;
        PlantNotes note = notes.get(position);
        final String pid = note.getSpecies_id();
        DbAccess databaseAccess = DbAccess.getInstance(holder.title.getContext());
        databaseAccess.open();
        final PlantInfoEntity p = databaseAccess.getPlantInfo(pid);
        String common_name = p.getCommon_name();
        databaseAccess.close();
        holder.title.setText(common_name);
        Date date = note.getLast_edit().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String d = dateFormat.format(date);
        holder.date.setText(this.DATE_DESC + ": " + d);
        holder.text.setText(note.getNotes());
        final ViewHolder j_holder = holder;
        holder.edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogHelper.openNotesPopup(p, j_holder.edit.getContext());
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(j_holder.delete.getContext());
                alert.setTitle("Are you sure you want to delete this note?");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        AppDatabase db = Room.databaseBuilder(j_holder.delete.getContext(),
                                AppDatabase.class, Constants.GARDEN_DB_NAME)
                                .fallbackToDestructiveMigration()
                                .allowMainThreadQueries().build();
                        PlantNotesDAO dao = db.getPlantNotesDAO();
                        PlantNotes x = dao.getPlantNotes(pid);
                        dao.delete(x);
                        refresh(pos);
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                alert.show();
            }
        });


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
        public final ImageButton edit;
        public final ImageButton delete;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            title = view.findViewById(R.id.notes_title);
            date = view.findViewById(R.id.notes_date);
            text = view.findViewById(R.id.notes_text);
            edit = view.findViewById(R.id.notes_edit);
            delete = view.findViewById(R.id.notes_delete);
        }
    }

}
