package com.example.tanya.internityhackathon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.tanya.internityhackathon.weather.Weather;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
{
    EditText edtlon, edtlat, edtdesc, edttemp, edtpressure,
            edtmintemp, edtmaxtemp,edthumid, edtspeed, edtdeg, edtcity;


    private String city;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtlon = (EditText)findViewById(R.id.edtlong);
        edtlat = (EditText)findViewById(R.id.edtlat);
        edtdesc = (EditText)findViewById(R.id.edtweather);
        edttemp = (EditText)findViewById(R.id.edttemp);
        edtpressure = (EditText)findViewById(R.id.edtpressure);
        edthumid = (EditText)findViewById(R.id.edthumid);
        edtmintemp = (EditText)findViewById(R.id.edttempmin);
        edtmaxtemp = (EditText)findViewById(R.id.edttempmax);
        edtspeed = (EditText)findViewById(R.id.edtspeed);
        edtdeg = (EditText)findViewById(R.id.edtwinddeg);
        edtcity = (EditText)findViewById(R.id.edtcity);

    }

    private void getData(String city)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        Api api = retrofit.create(Api.class);

        Call<Data> call = api.getData("51bd186d81e9633cd022b45a26cdf1f2",city);


        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response)
            {
                ArrayList<Weather> dlist = response.body().getWeather();
                Log.i("Checkkkkkkk", "onResponse: "+ response.body());

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

                for (int i=0; i<dlist.size(); i++)
                {
                    edtdesc.setText(response.body().getWeather().get(i).getMain());
                }


            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

}

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        MenuItem item = menu.findItem(R.id.actionsearch);

        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {

            @Override
            public boolean onQueryTextSubmit(String s)
            {
                city = s;
                getData(city);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s)
            {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
