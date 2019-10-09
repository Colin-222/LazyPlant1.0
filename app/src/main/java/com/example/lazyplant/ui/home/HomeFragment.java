package com.example.lazyplant.ui.home;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.ClimateZoneGetter;
import com.example.lazyplant.ui.DisplayHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.MODE_PRIVATE;
import static com.example.lazyplant.ui.plantDetails.PlantDetailsActivity.TAG;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class HomeFragment extends Fragment {
    private View root;
    private CarouselView carousel;
    private int[] images = {R.drawable.home_info1, R.drawable.home_info2, R.drawable.home_info3};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_home, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        final Fragment f_fragment = this;
        final TextView numberText = (TextView) root.findViewById(R.id.home_number_text);

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

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("plantsInfo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i = 1;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                i++;
                            }

                            ValueAnimator animator = ValueAnimator.ofInt(i-1, i);
                            animator.setDuration(1000);
                            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    numberText.setText(animation.getAnimatedValue().toString());
                                }
                            });
                            animator.start();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
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
