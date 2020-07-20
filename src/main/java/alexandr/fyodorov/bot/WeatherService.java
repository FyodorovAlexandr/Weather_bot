package alexandr.fyodorov.bot;

import alexandr.fyodorov.bot.model.Weather;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class WeatherService {
    private final OkHttpClient client = new OkHttpClient();
    private static final String URL = "https://api.weather.yandex.ru/v2/forecast?lat=%f&lon=%f&lang=ru_RU";
    private final String token;

    public WeatherService(String token) {
        this.token = token;
    }

    public Weather getWeather(double lat, double lon) throws IOException {
        Request request = new Request.Builder()
                .url(String.format(URL, lat, lon))
                .header("X-Yandex-API-Key", this.token)
                .build();

        try (Response response = client.newCall(request).execute()) {
            Gson mapper = new Gson();
            return mapper.fromJson(response.body().string(), Weather.class);
        }
    }

    @Override
    public String toString() {
        return "WeatherService{" +
                "token='" + token + '\'' +
                '}';
    }
}