package com.example.lazyplant.ui.edible;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.lazyplant.Constants;
import com.example.lazyplant.R;

public class EdibleFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_edible, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        ImageButton b1 = (ImageButton) root.findViewById(R.id.edible_button_fruit);
        b1.setOnClickListener(edibleButtonListener);
        ImageButton b2 = (ImageButton) root.findViewById(R.id.edible_button_vegetable);
        b2.setOnClickListener(edibleButtonListener);
        ImageButton b3 = (ImageButton) root.findViewById(R.id.edible_button_herb);
        b3.setOnClickListener(edibleButtonListener);
        ImageButton b4 = (ImageButton) root.findViewById(R.id.edible_button_spice);
        b4.setOnClickListener(edibleButtonListener);

        return root;
    }

    private View.OnClickListener edibleButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.EDIBLE_TAG, ((ImageButton)view).getTag().toString());
            NavHostFragment.findNavController(getThis())
                    .navigate(R.id.action_navigation_edible_to_navigation_edible_search, bundle);
        }};

    private Fragment getThis(){
        return this;
    }

}
