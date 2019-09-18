package com.example.lazyplant.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.google.android.gms.location.FusedLocationProviderClient;

public class NameSearchFragment extends Fragment {

    private FusedLocationProviderClient client;
    private Integer current_display_option;
    private SelectedFiltersEntity selected_filters = new SelectedFiltersEntity();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_name_search, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        //Set search button behaviour
        final EditText et = (EditText) root. findViewById(R.id.name_search_edit);
        ImageButton button = (ImageButton) root.findViewById(R.id.name_search_go);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search_term = et.getText().toString();
                if (!search_term.equals("")){
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.NAME_SEARCH_TAG, search_term);
                    NameSearchResultFragment nsrf = new NameSearchResultFragment();
                    nsrf.setArguments(bundle);
                    getFragmentManager() .beginTransaction()
                            .replace(R.id.nav_host_fragment, nsrf).commit();
                } else {
                    et.setError("Please Enter a Valid Plant Name");
                }
            }
        });

        return root;
    }

}


