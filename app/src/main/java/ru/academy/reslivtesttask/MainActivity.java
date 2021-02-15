package ru.academy.reslivtesttask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ru.academy.reslivtesttask.api.ApiFactory;
import ru.academy.reslivtesttask.api.ApiService;
import ru.academy.reslivtesttask.pojo.ResponseWeather;

public class MainActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable;
    TextView tempTextView;
    TextView feelsTextView;
    TextView cityName;
    EditText editText;
    String city;

    double temperature;
    double temperatureFeels;
    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tempTextView = findViewById(R.id.weather);
        feelsTextView = findViewById(R.id.weatherFeels);
        cityName = findViewById(R.id.cityname);
        startButton = findViewById(R.id.startbutton);
        editText = findViewById(R.id.enterCity);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                city = editText.getText().toString();
                startShowWeather (city);
            }
        });
    }

    public void startShowWeather(String city) {
        ApiFactory apiFactory = ApiFactory.getInstance();
        ApiService apiService = apiFactory.getApiService();
        compositeDisposable = new CompositeDisposable();
        Disposable disposable = apiService
                .getResponseWeather(city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseWeather>() {
                    @Override
                    public void accept(ResponseWeather responseWeather) throws Exception {
                        temperature = responseWeather.getMain().getTemp();
                        temperatureFeels = responseWeather.getMain().getFeels_like();
                        tempTextView.setText(getString(R.string.temp, (int) temperature));
                        feelsTextView.setText(getString(R.string.tempFeelsLike, (int) temperatureFeels));
                        cityName.setText(responseWeather.getName());
                        Log.d("Проверка", String.valueOf(temperature));
                    }
//
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("Проверка", throwable.getMessage());
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void disposeDisposable() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }
}
