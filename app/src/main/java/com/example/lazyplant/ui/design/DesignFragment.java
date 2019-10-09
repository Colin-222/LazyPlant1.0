package com.example.lazyplant.ui.design;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

public class DesignFragment extends Fragment {
    private View root;
    private CarouselView carousel;
    private int[] images = {R.drawable.desc_informal,
            R.drawable.desc_formal, R.drawable.desc_japanese, R.drawable.desc_cottage};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_design, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        this.carousel = root.findViewById(R.id.design_carousel);
        this.carousel.setPageCount(this.images.length);
        this.carousel.setImageListener(imageListener);
        this.carousel.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                openDesignPage(position);
            }
        });

        return root;
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Context context = getContext();
            Glide.with(context).load(images[position]).into(imageView);
        }
    };

    private void openDesignPage(int position){
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.EDIBLE_TAG, position);
        NavHostFragment.findNavController(this).navigate(
                R.id.action_navigation_design_to_navigation_design_result, bundle);
    }

}
