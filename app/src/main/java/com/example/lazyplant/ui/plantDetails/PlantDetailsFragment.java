package com.example.lazyplant.ui.plantDetails;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.AppDatabase;
import com.example.lazyplant.plantdata.DbAccess;
import com.example.lazyplant.plantdata.Favourite;
import com.example.lazyplant.plantdata.FavouriteDAO;
import com.example.lazyplant.plantdata.PlantInfoEntity;
import com.example.lazyplant.plantdata.PlantNotes;
import com.example.lazyplant.plantdata.PlantNotesDAO;
import com.example.lazyplant.ui.plantDetailsDisplayHelper;
import com.example.lazyplant.ui.plantListDisplayHelper;

import java.util.Calendar;
import java.util.List;

public class PlantDetailsFragment extends Fragment {

    PlantInfoEntity p;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_plant_details, container, false);

        Bundle bundle = this.getArguments();
        String message = bundle.getString(Constants.PLANT_DETAILS_BUNDLE_TAG);
        DbAccess databaseAccess = DbAccess.getInstance(getContext());
        databaseAccess.open();
        p = databaseAccess.getPlantInfo(message);
        databaseAccess.close();
        final String pid = p.getId();
        getActivity().setTitle(p.getCommon_name());

        if(p != null){
            ImageView image = root.findViewById(R.id.plant_details_image_main);
            ConstraintLayout cl = (ConstraintLayout) root.findViewById(R.id.plant_details_constraint_layout);
            plantDetailsDisplayHelper.displayDetailsPageImage(pid, image, this.getContext(), 0.4);
            View top_wo_nerae = (View) root.findViewById(R.id.plants_details_top);
            List<TextView> l = plantDetailsDisplayHelper.displayPlantTitle(p, cl, top_wo_nerae, this.getContext());
            List<TextView> l2 = plantDetailsDisplayHelper.displayPlantDetails(p, cl,
                    image, this.getContext());
            ToggleButton tb = (ToggleButton) root.findViewById(R.id.plant_details_button_favourites);
            plantListDisplayHelper.createToggleButton(tb, this.getContext(), pid);

            ImageButton add = (ImageButton)root.findViewById(R.id.plant_details_button_add);
            ImageButton shopping = (ImageButton)root.findViewById(R.id.plant_details_button_shopping);
            ImageButton notes = (ImageButton)root.findViewById(R.id.plant_details_button_notes);

            add.setOnClickListener(addListener);
            shopping.setOnClickListener(shoppingListener);
            notes.setOnClickListener(notesListener);

        } else {
            TextView tv = root.findViewById(R.id.plant_details_message);
            tv.setText("Sorry we ran into an error and did not find the plant.");

        }

        return root;
    }

    private View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    private View.OnClickListener notesListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            openNotesPopup();
        }
    };

    private View.OnClickListener shoppingListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    private void openNotesPopup(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this.getContext());
        alert.setTitle("Add notes for: " + p.getCommon_name());

        final EditText input = new EditText(this.getContext());
        AppDatabase database = Room.databaseBuilder(getContext(), AppDatabase.class, Constants.GARDEN_DB_NAME)
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();
        final PlantNotesDAO dao = database.getPlantNotesDAO();
        final String current = dao.getNotes(p.getId());
        final PlantNotes x = dao.getPlantNotes(p.getId());
        alert.setView(input);
        if(current != null) { input.setText(current); }

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if(current == null){
                    PlantNotes n = new PlantNotes();
                    n.setSpecies_id(p.getId());
                    n.setNotes(input.getText().toString());
                    n.setLast_edit(Calendar.getInstance());
                    dao.insert(n);
                }else{
                    x.setNotes(input.getText().toString());
                    x.setLast_edit(Calendar.getInstance());
                    dao.update(x);
                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Toast.makeText(getActivity(), "Note Not Saved.", Toast.LENGTH_SHORT).show();
            }
        });
        alert.show();
    }

}
