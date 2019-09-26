package com.example.lazyplant.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.lazyplant.R;
import com.example.lazyplant.ui.plantDetails.PlantDetailsFragment;

public class ProfileFragment extends Fragment {
    private View root;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_profile, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        final Fragment f_fragment = this;

        RelativeLayout button_history = (RelativeLayout)root.findViewById(R.id.profile_button_history);
        RelativeLayout button_reminder = (RelativeLayout)root.findViewById(R.id.profile_button_reminder);
        RelativeLayout button_notes = (RelativeLayout)root.findViewById(R.id.profile_button_notes);
        //RelativeLayout button_backup = (RelativeLayout)root.findViewById(R.id.profile_button_backup);

        button_history.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                HistoryFragment f = new HistoryFragment();
                NavHostFragment.findNavController(f_fragment).navigate(
                        R.id.action_navigation_profile_to_navigation_history);
                //f_fragment.getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, f).commit();
            }
        });

        button_reminder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ReminderFragment f = new ReminderFragment();
                NavHostFragment.findNavController(f_fragment).navigate(
                        R.id.action_navigation_profile_to_navigation_reminder);
                //f_fragment.getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, f).commit();
            }
        });

        button_notes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NotesFragment f = new NotesFragment();
                NavHostFragment.findNavController(f_fragment).navigate(
                        R.id.action_navigation_profile_to_navigation_notes);
                //f_fragment.getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, f).commit();
            }
        });

        /*button_backup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BackupFragment f = new BackupFragment();
                f_fragment.getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, f).commit();
            }
        });*/

        return this.root;
    }


}
