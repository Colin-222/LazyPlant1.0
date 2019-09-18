package com.example.lazyplant.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.GardenPlant;
import com.example.lazyplant.plantdata.PlantInfoEntity;
import com.example.lazyplant.ui.plantDetails.PlantDetailsFragment;
import com.example.lazyplant.ui.plantListDisplayHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BrowseAdapter extends RecyclerView.Adapter<BrowseAdapter.ViewHolder> {

    private List<PlantInfoEntity> plant_list;

    public BrowseAdapter(List<PlantInfoEntity> plant_list) {
        this.plant_list = plant_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_single_plant, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlantInfoEntity plant = plant_list.get(position);
        final String pid = plant.getId();
        holder.name.setText(plant.getCommon_name());
        Context context = holder.image.getContext();
        String image_name = convertToImageName(plant.getId());
        int image_id = context.getResources().getIdentifier(image_name,
                Constants.PLANT_IMAGES_FOLDER, context.getPackageName());
        Glide.with(context).load(image_id).into(holder.image);

        final FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();

        holder.image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //open details page with id
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PLANT_DETAILS_BUNDLE_TAG, pid);
                PlantDetailsFragment pdf = new PlantDetailsFragment();
                pdf.setArguments(bundle);
                manager.beginTransaction().replace(R.id.nav_host_fragment, pdf).commit();
            }
        });
        plantListDisplayHelper.createToggleButton(holder.favourite_button, context, pid);
    }

    @Override
    public int getItemCount() {
        if (plant_list != null) {
            return plant_list.size();
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final ImageView image;
        public final TextView name;
        public final ToggleButton favourite_button;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.image = view.findViewById(R.id.single_plant_image);
            this.name = view.findViewById(R.id.single_plant_name);
            this.favourite_button = view.findViewById(R.id.single_button_favorite);
        }
    }

    private String convertToImageName(String name){
        String x = name;
        x = x.replaceAll("[^a-zA-Z0-9]", "");
        x = x.toLowerCase();
        return x;
    }

}
