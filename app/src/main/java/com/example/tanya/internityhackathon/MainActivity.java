package com.example.tanya.internityhackathon;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tanya.internityhackathon.weather.Weather;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    TextView edtlon, edtlat, edtdesc, edttemp, edtpressure,
            edtmintemp, edtmaxtemp, edthumid, edtspeed, edtdeg, edtcity;
    LocationManager mLocationManager;
    LocationListener mLocationListener;
    ProgressBar progressBar;
    public static final int locationPermission= 1;



    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtlon = (TextView) findViewById(R.id.edtlong);
        edtlat = (TextView) findViewById(R.id.edtlat);
        edtdesc = (TextView) findViewById(R.id.edtweather);
        edttemp = (TextView) findViewById(R.id.edttemp);
        edtpressure = (TextView) findViewById(R.id.edtpressure);
        edthumid = (TextView) findViewById(R.id.edthumid);
        edtmintemp = (TextView) findViewById(R.id.edttempmin);
        edtmaxtemp = (TextView) findViewById(R.id.edttempmax);
        edtspeed = (TextView) findViewById(R.id.edtspeed);
        edtdeg = (TextView) findViewById(R.id.edtwinddeg);
        edtcity = (TextView) findViewById(R.id.edtcity);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

    }

    private void getData(String city) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        Api api = retrofit.create(Api.class);

        Call<Data> call = api.getData("51bd186d81e9633cd022b45a26cdf1f2", city);


        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response)
            {
                progressBar.setVisibility(View.GONE);

                if(response.body() == null)
                {
                    Toast.makeText(MainActivity.this, "No result found", Toast.LENGTH_LONG).show();
                    return;
                }

                ArrayList<Weather> dlist = response.body().getWeather();
//                Log.i("Checkkkkkkk", "onResponse: "+ response.body());

                edtcity.setText(response.body().getName());
                edtlon.setText(response.body().getCoord().getLon());
                edtlon.setText(response.body().getCoord().getLat());
                edttemp.setText(response.body().getMain().getTemp());
                edtpressure.setText(response.body().getMain().getPressure());
                edthumid.setText(response.body().getMain().getHumidity());
                edtmintemp.setText(response.body().getMain().getTemp_min());
                edtmaxtemp.setText(response.body().getMain().getTemp_max());

                edtspeed.setText(response.body().getWind().getSpeed());
                edtdeg.setText(response.body().getWind().getDeg());

                for (int i = 0; i < dlist.size(); i++) {
                    edtdesc.setText(response.body().getWeather().get(i).getMain());
                }


            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.actionsearch);

        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s)
            {
                progressBar.setVisibility(View.VISIBLE);

                city = s;
                getData(city);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.location)
        {
            progressBar.setVisibility(View.VISIBLE);
            getCurrentLocation();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getCurrentLocation() {
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                getData2(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    locationPermission);
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10,
                10, mLocationListener);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == locationPermission)
        {
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                getCurrentLocation();
            }
        }

    }

    private void getData2(String lat, String lon) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        Api api = retrofit.create(Api.class);

        Call<Data> call = api.getData("51bd186d81e9633cd022b45a26cdf1f2", lat, lon);


        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {

                progressBar.setVisibility(View.GONE);
//                Log.i("Checkkkkkkk", "onResponse: "+ response.body());
                if(response.body() == null)
                {
                    Toast.makeText(MainActivity.this, "No result found", Toast.LENGTH_LONG).show();
                    return;
                }
                ArrayList<Weather> dlist = response.body().getWeather();

                edtcity.setText(response.body().getName());
                edtlon.setText(response.body().getCoord().getLon());
                edtlon.setText(response.body().getCoord().getLat());
                edttemp.setText(response.body().getMain().getTemp());
                edtpressure.setText(response.body().getMain().getPressure());
                edthumid.setText(response.body().getMain().getHumidity());
                edtmintemp.setText(response.body().getMain().getTemp_min());
                edtmaxtemp.setText(response.body().getMain().getTemp_max());

                edtspeed.setText(response.body().getWind().getSpeed());
                edtdeg.setText(response.body().getWind().getDeg());

                for (int i = 0; i < dlist.size(); i++) {
                    edtdesc.setText(response.body().getWeather().get(i).getMain());
                }


            }

            @Override
            public void onFailure(Call<Data> call, Throwable t)
            {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
