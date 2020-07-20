package alexandr.fyodorov.bot;

import alexandr.fyodorov.bot.model.Weather;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Location;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import java.io.IOException;
import java.util.List;

/**
 * Telegram weather bot, который показывает погоду при отправке геопозиции.
 */

public class Main {
    private static final String TELEGRAM_TOKEN = "1177440511:AAHhiRZYK9lC9qGy_XSfwTnAWnTckxWsJCs";
    private static final String YANDEX_TOKEN = "b4725322-2429-4eb4-becd-c9da72f90a7d";
    private static int offset = 0;

    public static void main(String[] args) throws IOException {

        TelegramBot bot = new TelegramBot(TELEGRAM_TOKEN);
        WeatherService weatherService = new WeatherService(YANDEX_TOKEN);

        while(true){
            GetUpdates getUpdates = new GetUpdates().limit(1).offset(offset).timeout(0);
            GetUpdatesResponse updatesResponse = bot.execute(getUpdates);

            List<Update> updates = updatesResponse.updates();

            if(!updates.isEmpty()) {
                Update update = updates.get(0);
                System.out.println(update);

                Message message = update.message();
                System.out.println(message);

                String answer = "";

                if("/start".equals(message.text())){
                    answer = "Привет! Я могу показать прогноз погоды.";
                }else if (message.location() != null) {
                    Location location = message.location();
                    Weather weather = weatherService.getWeather(location.latitude(), location.longitude());
                    answer = "Погода на сегодня: \n" +
                            "Температура: " + weather.getFact().getTemp() + "\n" +
                            "Ощущается как: " + weather.getFact().getFeels_like() + "\n" +
                            "Скорость ветра: " + weather.getFact().getWind_speed() + "\n" +
                            "Давление: " + weather.getFact().getPressure_mm() + "\n" +
                            "Подробнее: " + weather.getInfo().getUrl() + "\n";
                }else {
                    answer = "Я пока не знаю такой команды :(";
                }
                SendMessage sendMessage = new SendMessage(message.chat().id(), answer);

                bot.execute(sendMessage);

                offset = update.updateId() + 1;
            }
        }
    }
}