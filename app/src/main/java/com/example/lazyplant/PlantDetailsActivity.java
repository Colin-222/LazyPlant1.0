package com.example.lazyplant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.lazyplant.ui.plantDetailUI.main.DetailsFragment;
import com.example.lazyplant.ui.plantDetailUI.main.DetailsSectionPagerAdapter;
import com.example.lazyplant.ui.plantDetailUI.main.InfoFragment;
import com.example.lazyplant.ui.plantDetailUI.main.PicsFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

public class PlantDetailsActivity extends AppCompatActivity {

    public  static  final String TAG = "PlantDetailsActivity";
    private DetailsSectionPagerAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_details);

        mSectionsPageAdapter = new DetailsSectionPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(mViewPager);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        Button button= (Button) findViewById(R.id.detail_back_button);
//        button.bringToFront();
//        button.invalidate();
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlantDetailsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupViewPager(ViewPager viewPage) {
        DetailsSectionPagerAdapter adapter = new DetailsSectionPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new InfoFragment(), "Information");
        adapter.addFragment(new DetailsFragment(), "Detail");
        adapter.addFragment(new PicsFragment(), "Picture");
        viewPage.setAdapter(adapter);
    }
}