package ru.academy.reslivtesttask.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.academy.reslivtesttask.pojo.ResponseWeather;

public interface ApiService {

    @GET("weather?units=metric&appid=60ac937a95b7355dbf856446fad8af84")
    Observable<ResponseWeather> getResponseWeather(@Query("q") String name);
}
