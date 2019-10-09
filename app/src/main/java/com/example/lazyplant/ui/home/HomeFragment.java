package com.example.lazyplant.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.lazyplant.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class HomeFragment extends Fragment {
    private View root;
    private CarouselView carousel;
    private int[] images = {R.drawable.acaciamacradeniazigzagwattle,
            R.drawable.dampierastrictabluedampiera,
            R.drawable.nativeoreganoprostantherarotundifolia};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_home, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        final Fragment f_fragment = this;

        this.carousel = root.findViewById(R.id.home_carousel);
        this.carousel.setPageCount(this.images.length);
        this.carousel.setImageListener(imageListener);

        Button explore_go = (Button) root.findViewById(R.id.home_explore_go);
        explore_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                NavHostFragment.findNavController(f_fragment).navigate(
                        R.id.action_navigation_home_to_navigation_home_search);
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

}
