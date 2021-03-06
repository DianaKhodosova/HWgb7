package lesson7;

import com.fasterxml.jackson.databind.ObjectMapper;
import lesson7.entity.Weather;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

public class AccuweatherModel implements WeatherModel {
    //http://dataservice.accuweather.com/forecasts/v1/daily/1day/349727
    private static final String PROTOKOL = "https";
    private static final String BASE_HOST = "dataservice.accuweather.com";
    private static final String FORECASTS = "forecasts";
    private static final String VERSION = "v1";
    private static final String DAILY = "daily";
    private static final String ONE_DAY = "1day";

    private static final String FIVE_DAYS = "5day";
    private static final String API_KEY = "U0CW4YAxcvlKhC2EoufRmrtQTAGGRk5C";
    private static final String API_KEY_QUERY_PARAM = "apikey";
    private static final String LOCATIONS = "locations";
    private static final String CITIES = "cities";
    private static final String AUTOCOMPLETE = "autocomplete";

    private static final OkHttpClient okHttpClient = new OkHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private DataBaseRepository dataBaseRepository = new DataBaseRepository();

    public void getWeather(String selectedCity, Period period) throws IOException {
        switch (period) {
            case NOW:
                HttpUrl httpUrl = new HttpUrl.Builder()
                        .scheme(PROTOKOL)
                        .host(BASE_HOST)
                        .addPathSegment(FORECASTS)
                        .addPathSegment(VERSION)
                        .addPathSegment(DAILY)
                        .addPathSegment(ONE_DAY)
                        .addPathSegment(detectCityKey(selectedCity))
                        .addQueryParameter(API_KEY_QUERY_PARAM, API_KEY)
                        .build();

                Request request = new Request.Builder()
                        .url(httpUrl)
                        .build();

                Response oneDayForecastResponse = okHttpClient.newCall(request).execute();
                String weatherResponse = oneDayForecastResponse.body().string();
                System.out.println(weatherResponse);

                //TODO: сделать человекочитаемый вывод погоды. Выбрать параметры для вывода на свое усмотрение
                //Например: Погода в городе Москва - 5 градусов по цельсию Expect showers late Monday night
                //dataBaseRepository.saveWeatherToDataBase(new Weather())
                /* Тут тоже я не уверена но попробую что-то написать. Надесь вы поправите меня и укажите на ошибки.
                Вы на уроке сказали распарсить с помощью ObjectMapper, Jackson. Так вот попробую предположить как это будет.
                 ObjectMapper mapper = new ObjectMapper();
                 Response response = mapper.readValue(jsonStr, Response.class);
                 @JsonIgnoreProperties(ignoreUnknown = true) class Response {List<Weather> list; general getters and setters}
                 @JsonIgnoreProperties(ignoreUnknown = true) class Weather.
                 Применить @JsonIgnoreProperties (предоставленный Джексоном),
                 чтобы игнорировать те поля, которые вам не нужны. У меня его почемуто не было и
                 дабавляла его в ручную с сайта.
                 */



                break;

                //TODO. Я попыталась сделать для 5 дней. Честно не до конца всё понмиаю. Как, что и за чем идет. Очень путаюсь.
            case FIVE_DAYS:
                public void getWeather(String selectedCity, Period period) throws IOException {
                switch (period) {
                    case FIVE_DAYS:
                        HttpUrl httpUrl1 = new HttpUrl.Builder()
                                .scheme(PROTOKOL)
                                .host(BASE_HOST)
                                .addPathSegment(FORECASTS)
                                .addPathSegment(VERSION)
                                .addPathSegment(DAILY)
                                .addPathSegment(FIVE_DAYS)
                                .addPathSegment(detectCityKey(selectedCity))
                                .addQueryParameter(API_KEY_QUERY_PARAM, API_KEY)
                                .build();

                        Request request = new Request.Builder()
                                .url(httpUrl)
                                .build();

                        Response fiveDayForecastResponse = okHttpClient.newCall(request).execute();
                        String weatherResponse = fiveDayForecastResponse.body().string();
                        System.out.println(weatherResponse);
                break;
        }
    }

    @Override
    public List<Weather> getSavedToDBWeather() {
        return dataBaseRepository.getSavedToDBWeather();
    }

    private String detectCityKey(String selectCity) throws IOException {
        //http://dataservice.accuweather.com/locations/v1/cities/autocomplete
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme(PROTOKOL)
                .host(BASE_HOST)
                .addPathSegment(LOCATIONS)
                .addPathSegment(VERSION)
                .addPathSegment(CITIES)
                .addPathSegment(AUTOCOMPLETE)
                .addQueryParameter(API_KEY_QUERY_PARAM, API_KEY)
                .addQueryParameter("q", selectCity)
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .get()
                .addHeader("accept", "application/json")
                .build();

        Response response = okHttpClient.newCall(request).execute();
        String responseString = response.body().string();

        String cityKey = objectMapper.readTree(responseString).get(0).at("/Key").asText();
        return cityKey;
    }
}

    private void detectCityKey() {
    }