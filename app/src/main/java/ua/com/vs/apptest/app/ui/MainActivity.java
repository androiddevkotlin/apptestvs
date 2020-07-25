package ua.com.vs.apptest.app.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import ua.com.vs.apptest.app.R;
import ua.com.vs.apptest.app.interfaces.OnRegionFragment;

public class MainActivity extends AppCompatActivity implements OnRegionFragment, FragmentManager.OnBackStackChangedListener {
    private FragmentManager mFragmentManager = getSupportFragmentManager();
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFragmentManager.addOnBackStackChangedListener(this);
        if (savedInstanceState == null) {
            addFirstFragment();
        }
    }

    @Override
    public void onBackStackChanged() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count > 0) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back button
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);//show hamburger
        }
    }

    private void addFirstFragment() {
        mFragmentManager.beginTransaction()
                .add(R.id.fragment_container, ContinentFragment.instantiate(this, ContinentFragment.class.getName()))
                .commit();
    }

    @Override
    public void applyRegionFragment(String region) {
        Bundle bundle = new Bundle();
        bundle.putString(RegionFragment.KEY_ARGUMENT, region);
        mFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RegionFragment.instantiate(this, RegionFragment.class.getName(), bundle))
                .addToBackStack("")
                .commit();
    }

}
