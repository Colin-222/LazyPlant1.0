package com.example.lazyplant.ui.plantDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.DbAccess;
import com.example.lazyplant.plantdata.PlantInfoEntity;
import com.example.lazyplant.ui.plantDetailsDisplayHelper;
import com.example.lazyplant.ui.plantListDisplayHelper;

import java.util.List;

public class PlantDetailsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_plant_details, container, false);

        Bundle bundle = this.getArguments();
        String message = bundle.getString(Constants.PLANT_DETAILS_BUNDLE_TAG);
        DbAccess databaseAccess = DbAccess.getInstance(getContext());
        databaseAccess.open();
        PlantInfoEntity p = databaseAccess.getPlantInfo(message);
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
        } else {
            TextView tv = root.findViewById(R.id.plant_details_message);
            tv.setText("Sorry we ran into an error and did not find the plant.");

        }



        return root;
    }

}
