package com.example.lazyplant.ui.profile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.FileUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.AppDatabase;
import com.example.lazyplant.plantdata.PlantNotes;
import com.example.lazyplant.plantdata.PlantNotesDAO;

import java.io.File;
import java.util.List;

public class BackupFragment extends Fragment {
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_backup, container, false);

        Button backup = (Button) root.findViewById(R.id.backup_backup);
        Button recover = (Button) root.findViewById(R.id.backup_recover);



        return this.root;
    }


}
