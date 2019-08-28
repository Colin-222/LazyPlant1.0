package com.example.lazyplant.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.lazyplant.MainActivity;
import com.example.lazyplant.PlantDetailsActivity;
import com.example.lazyplant.R;

public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_search, container, false);

        Button button= (Button) root.findViewById(R.id.testButton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PlantDetailsActivity.class);
                startActivity(intent);
            }
        });



        return root;
    }


}