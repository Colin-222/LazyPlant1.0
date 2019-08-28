package com.example.lazyplant;

import android.os.Bundle;

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
    }

    private void setupViewPager(ViewPager viewPage) {
        DetailsSectionPagerAdapter adapter = new DetailsSectionPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new InfoFragment(), "Information");
        adapter.addFragment(new DetailsFragment(), "Detail");
        adapter.addFragment(new PicsFragment(), "Picture");
        viewPage.setAdapter(adapter);
    }
}