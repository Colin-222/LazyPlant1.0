package com.example.lazyplant.ui.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.AppDatabase;
import com.example.lazyplant.plantdata.DbAccess;
import com.example.lazyplant.plantdata.GardenPlant;
import com.example.lazyplant.plantdata.GardenPlantDAO;
import com.example.lazyplant.plantdata.PlantInfoEntity;
import com.example.lazyplant.plantdata.PlantNotes;
import com.example.lazyplant.plantdata.PlantNotesDAO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {

    private List<GardenPlant> gp;

    public ReminderAdapter(List<GardenPlant> gp) {
        this.gp = gp;
    }

    public void refresh(int position){
        this.gp.remove(position);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_plant_reminder, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GardenPlant p = gp.get(position);
        final int pos = position;
        final ViewHolder j_holder = holder;
        final String pid = p.getPlant_id();
        final Context context = j_holder.name.getContext();
        holder.name.setText(p.getName());

        DbAccess databaseAccess = DbAccess.getInstance(holder.name.getContext());
        databaseAccess.open();
        final PlantInfoEntity pie = databaseAccess.getPlantInfo(p.getSpecies_id());
        databaseAccess.close();
        if(pie == null){
            holder.species.setText(" ");
        } else {
            String common_name = pie.getCommon_name();
            holder.species.setText(common_name);
        }
        
        Calendar c = p.getLast_watering();
        c.add(Calendar.DATE, p.getWatering_interval());
        Date date = c.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String d = dateFormat.format(date);
        holder.next_watering.setText(d);

        holder.water.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ReminderControl rc = new ReminderControl(context, Constants.GARDEN_DB_NAME);
                rc.recordPlantWatering(pid, Constants.WATERING);
                refresh(pos);
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
                        GardenPlantDAO dao = db.getGardenPlantDAO();
                        GardenPlant x = dao.getGardenPlant(pid).get(0);
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
        if (gp != null) {
            return gp.size();
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView name;
        public final TextView species;
        public final TextView next_watering;
        public final ImageButton water;
        //public final ImageButton edit;
        public final ImageButton delete;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.reminder_name);
            species = view.findViewById(R.id.reminder_species);
            next_watering = view.findViewById(R.id.reminder_next);
            water = view.findViewById(R.id.reminder_water);
            //edit = view.findViewById(R.id.reminder_edit);
            delete = view.findViewById(R.id.reminder_delete);
        }
    }

}
