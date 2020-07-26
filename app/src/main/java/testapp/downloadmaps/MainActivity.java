package testapp.downloadmaps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;


import static testapp.downloadmaps.DeviceMemory.getAvailableInternalMemorySize;
import static testapp.downloadmaps.DeviceMemory.getTotalInternalMemorySize;

public class MainActivity extends AppCompatActivity {

    private TextView textViewFreeMemory;
    private ProgressBar progressBarMemory;
    private RecyclerView countriesRecyclerView;
    private CountriesAdapter countriesAdapter;

    private double freeMemory;
    private double allMemory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isStoragePermissionGranted();
        initTextViewFreeMemory();
        initProgressBarMemory();

        initRecyclerView();
        loadCountries();




    }

    @Override
    protected void onResume() {
        super.onResume();
        initProgressBarMemory();
    }

    private void initTextViewFreeMemory() {
        textViewFreeMemory = findViewById(R.id.tv_free_memory);
        freeMemory = getAvailableInternalMemorySize();
        textViewFreeMemory.setText(getResources().getString(R.string.free_memory, freeMemory));
    }

    private void initProgressBarMemory() {
        progressBarMemory = findViewById(R.id.progress_bar_memory);
        freeMemory = getAvailableInternalMemorySize();
        allMemory = getTotalInternalMemorySize();

        progressBarMemory.setMax((int) allMemory);
        progressBarMemory.setProgress((int) allMemory - (int) freeMemory);

        progressBarMemory.setScaleY(4f);
    }

    private void initRecyclerView() {
        countriesRecyclerView = findViewById(R.id.rv_countries);
        countriesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        countriesAdapter = new CountriesAdapter();
        countriesRecyclerView.setAdapter(countriesAdapter);

    }

    private void loadCountries() {
        ParserXml parserXml = new ParserXml(this);
        List<String> list = parserXml.parserXmlForCountries();
        countriesAdapter.setItems(list);
        countriesAdapter.notifyDataSetChanged();

    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            Log.v("TAG","Permission is granted");
            return true;
        }
    }

}
