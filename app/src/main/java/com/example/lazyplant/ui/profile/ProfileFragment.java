package com.example.lazyplant.ui.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.example.lazyplant.ui.plantDetails.PlantDetailsFragment;

import org.w3c.dom.Text;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_profile, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        final Fragment f_fragment = this;

        Button button_edit_user = (Button)root.findViewById(R.id.profile_button_user_details);
        SharedPreferences pref = this.getContext().getApplicationContext()
                .getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE);
        final SettingsPopupBuilder sbp = new SettingsPopupBuilder(pref);
        final LayoutInflater li = inflater;
        button_edit_user.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sbp.buildSettingsPopup(root, li);
            }
        });

        String name = pref.getString(Constants.SHARED_PREFERENCE_NAME, null);
        if (name != null){
            ((TextView)root.findViewById(R.id.profile_title)).setText("Welcome " + name);
        }

        RelativeLayout button_history = (RelativeLayout)root.findViewById(R.id.profile_button_history);
        RelativeLayout button_reminder = (RelativeLayout)root.findViewById(R.id.profile_button_reminder);
        RelativeLayout button_notes = (RelativeLayout)root.findViewById(R.id.profile_button_notes);
        RelativeLayout button_plants = (RelativeLayout)root.findViewById(R.id.profile_button_plants);

        button_history.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                HistoryFragment f = new HistoryFragment();
                NavHostFragment.findNavController(f_fragment).navigate(
                        R.id.action_navigation_profile_to_navigation_history);
            }
        });

        button_reminder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ReminderFragment f = new ReminderFragment();
                NavHostFragment.findNavController(f_fragment).navigate(
                        R.id.action_navigation_profile_to_navigation_reminder);
            }
        });

        button_notes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NotesFragment f = new NotesFragment();
                NavHostFragment.findNavController(f_fragment).navigate(
                        R.id.action_navigation_profile_to_navigation_notes);
            }
        });

        button_plants.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AllPlantsFragment f = new AllPlantsFragment();
                NavHostFragment.findNavController(f_fragment).navigate(
                        R.id.action_navigation_profile_to_navigation_all_plants);
            }
        });

        return this.root;
    }


}
