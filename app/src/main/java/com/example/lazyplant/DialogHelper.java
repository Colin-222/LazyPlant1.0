package com.example.lazyplant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.room.Room;

import com.example.lazyplant.plantdata.AppDatabase;
import com.example.lazyplant.plantdata.PlantInfoEntity;
import com.example.lazyplant.plantdata.PlantNotes;
import com.example.lazyplant.plantdata.PlantNotesDAO;

import java.util.Calendar;

public class DialogHelper {

    public static void openNotesPopup(PlantInfoEntity p, Context context){
        final Context f_context = context;
        final String pid = p.getId();
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Add notes for: " + p.getCommon_name());

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_note_dialog, null);
        alert.setView(v);
        final EditText input = v.findViewById(R.id.note_dialog_input);
        AppDatabase database = Room.databaseBuilder(context, AppDatabase.class, Constants.GARDEN_DB_NAME)
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();
        final PlantNotesDAO dao = database.getPlantNotesDAO();
        final String current = dao.getNotes(p.getId());
        final PlantNotes x = dao.getPlantNotes(p.getId());
        if(current != null) { input.setText(current); }

        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(current == null){
                    PlantNotes n = new PlantNotes();
                    n.setSpecies_id(pid);
                    n.setNotes(input.getText().toString());
                    n.setLast_edit(Calendar.getInstance());
                    dao.insert(n);
                }else{
                    x.setNotes(input.getText().toString());
                    x.setLast_edit(Calendar.getInstance());
                    dao.update(x);
                }
                Toast.makeText(f_context.getApplicationContext(), "Note Saved.", Toast.LENGTH_SHORT).show();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Toast.makeText(f_context.getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        });
        alert.show();
    }

}
