package com.example.lazyplant.ui.plantDetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.lazyplant.MainActivity;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.DbAccess;
import com.example.lazyplant.plantdata.Favourite;
import com.example.lazyplant.plantdata.PlantInfoEntity;
import com.example.lazyplant.ui.calendar.CalendarFragment;
import com.example.lazyplant.ui.favourites.FavouritesFragment;
import com.example.lazyplant.ui.plantDetailsDisplayHelper;
import com.example.lazyplant.ui.search.SearchResultFragment;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class PlantDetailsActivity extends AppCompatActivity {

    public  static  final String TAG = "PlantDetailsActivity";
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_details);

        Button button= (Button) findViewById(R.id.detail_back_button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlantDetailsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        String message;
        message = intent.getStringExtra(CalendarFragment.EXTRA_MESSAGE);
        if (message == null){
            message = intent.getStringExtra(SearchResultFragment.EXTRA_MESSAGE);
        }
        DbAccess databaseAccess = DbAccess.getInstance(this.getApplicationContext());
        databaseAccess.open();
        PlantInfoEntity p = databaseAccess.getPlantInfo(message);
        databaseAccess.close();
        final String pid = p.getId();

        if(p != null){
            ImageView image = findViewById(R.id.details_image_main);
            plantDetailsDisplayHelper.displayDetailsPageImage(convertToImageName(p.getId()),
                    image, this, 0.4);
            plantDetailsDisplayHelper.displayPlantTitle(p,
                    (ConstraintLayout) findViewById(R.id.details_constraint_layout), image, this);
            plantDetailsDisplayHelper.displayPlantDetails(p,
                    (ConstraintLayout) findViewById(R.id.details_constraint_layout), image, this);
        }

    }

    String convertToImageName(String name){
        String x = name;
        x = x.replaceAll("[^a-zA-Z0-9]", "");
        x = x.toLowerCase();
        return x;
    }

    boolean checkIfFavourite(String id, List<Favourite> favs){
        for (Favourite i : favs){
            if (i.getSpecies_id().equals(id)){
                return true;
            }
        }
        return false;
    }

}