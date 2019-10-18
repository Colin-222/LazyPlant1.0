package com.example.lazyplant.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.DbAccess;
import com.example.lazyplant.plantdata.GardenPlant;
import com.example.lazyplant.plantdata.PlantInfoEntity;
import com.example.lazyplant.plantdata.PlantNotes;
import com.example.lazyplant.ui.plantListDisplayHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AllPlantsAdapter extends RecyclerView.Adapter<AllPlantsAdapter.ViewHolder> {

    private List<GardenPlant> gp;
    private Fragment fragment;

    public AllPlantsAdapter(List<GardenPlant> gp, Fragment fragment) {
        this.gp = gp;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_plants, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GardenPlant p = gp.get(position);
        final String upid = p.getPlant_id();
        holder.name.setText(p.getName());
        DbAccess databaseAccess = DbAccess.getInstance(holder.name.getContext().getApplicationContext());
        databaseAccess.open();
        PlantInfoEntity pie = databaseAccess.getPlantInfo(p.getSpecies_id());
        databaseAccess.close();
        holder.species.setText(pie.getCommon_name());

        final String pid = pie.getId();
        Context context = holder.image.getContext();
        String image_name = convertToImageName(pid);
        int image_id = context.getResources().getIdentifier(image_name,
                Constants.PLANT_IMAGES_FOLDER, context.getPackageName());
        Glide.with(context).load(image_id).into(holder.image);
        final Fragment f_fragment = this.fragment;
        holder.image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //open details page with id
                Bundle bundle = new Bundle();
                bundle.putString(Constants.USER_PLANT_DETAILS_BUNDLE_TAG, upid);
                NavController navi = NavHostFragment.findNavController(f_fragment);
                navi.navigate(R.id.action_navigation_all_plants_to_navigation_user_plant_detail, bundle);
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
        public final ImageView image;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.user_plant_name);
            image = view.findViewById(R.id.user_plant_image);
            species = view.findViewById(R.id.user_plant_species);
        }
    }

    private String convertToImageName(String name){
        String x = name;
        x = x.replaceAll("[^a-zA-Z0-9]", "");
        x = x.toLowerCase();
        return x;
    }

}
