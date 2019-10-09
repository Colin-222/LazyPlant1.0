package com.example.lazyplant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.NoteAlertDialogStyle);
        builder.setTitle("Add notes for: " + p.getCommon_name());

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_note_dialog, null);
        builder.setView(v);
        final EditText input = v.findViewById(R.id.note_dialog_input);
        AppDatabase database = Room.databaseBuilder(context, AppDatabase.class, Constants.GARDEN_DB_NAME)
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();
        final PlantNotesDAO dao = database.getPlantNotesDAO();
        final String current = dao.getNotes(p.getId());
        final PlantNotes x = dao.getPlantNotes(p.getId());
        if(current != null) { input.setText(current); }

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
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
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Toast.makeText(f_context.getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alert = builder.create();
        alert.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        input.requestFocus();
        alert.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        |WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        final Drawable bgd = context.getResources().getDrawable(R.drawable.ic_rect);
        final int bg = context.getResources().getColor(R.color.pureWhite);
        final int tc = context.getResources().getColor(R.color.colorPrimaryDark);
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 3f);
                negativeButton.setLayoutParams(params);
                positiveButton.setLayoutParams(params);
                negativeButton.setBackgroundColor(bg);
                positiveButton.setBackgroundColor(bg);
                negativeButton.setTextColor(tc);
                positiveButton.setTextColor(tc);
                negativeButton.invalidate();
                positiveButton.invalidate();
            }
        });

        /*int height = (int)(context.getResources().getDisplayMetrics().heightPixels);
        int width = (int)(context.getResources().getDisplayMetrics().widthPixels);
        alert.getWindow().setLayout(width, height);*/

        alert.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        alert.show();
    }

}
