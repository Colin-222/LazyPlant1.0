package com.example.lazyplant.ui.search;

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

import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.DbAccess;
import com.example.lazyplant.plantdata.GardenPlant;
import com.example.lazyplant.plantdata.PlantInfoEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<SearchCategory> categories;
    private Fragment fragment;

    public SearchAdapter(List<SearchCategory> categories, Fragment fragment) {
        this.categories = categories;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category,
                parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SearchCategory p = categories.get(position);
        holder.name.setText(p.getDesc());
        holder.image.setImageResource(p.getImage_resource());
        final Fragment f = this.fragment;
        holder.image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.CATEGORY_TYPE,p.getType());
                bundle.putString(Constants.CATEGORY_TERM, p.getTerm());
                NavController navi = NavHostFragment.findNavController(f);
                navi.navigate(R.id.action_navigation_search_to_navigation_browse, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (categories != null) {
            return categories.size();
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView name;
        public final ImageView image;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.category_name);
            image = view.findViewById(R.id.category_image);
        }
    }

}
