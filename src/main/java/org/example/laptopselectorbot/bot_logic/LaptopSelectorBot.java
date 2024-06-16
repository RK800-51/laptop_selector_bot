package org.example.laptopselectorbot.bot_logic;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

@Component
public class LaptopSelectorBot extends TelegramLongPollingBot {
    private HashMap<Long, Integer> usersStatus = new HashMap<>();
    final BotConfig config;

    public LaptopSelectorBot(BotConfig config) {
        this.config = config;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            // TODO: создать логику общения - этот метод вызывается каждый раз, когда пользователь что-то написал боту
            // расписать кучу ифов: связать последовательность вопросов с userStatus-мапой и аутпутом, который дает пользователь
            // формировать из аутпута мапу inputMap
            if (messageText.equals("/start")) {
                startCommandReceived(chatId);
            }
            else {
                sendMessage(chatId, "Команда не распознана, известные команды: /start");
            }

//            switch (messageText) {
//                case "/start":
//                    startCommandReceived(chatId);
//                    break;
//                default: sendMessage(chatId, "Команда не распознана, известные команды: /start");
//            }

        }

        // Check if the update has a message
//        if (update.hasMessage()){
//            // Fetch the chatId and the status of the user.
//            Long chatId = update.getMessage().getChatId();
//            Integer status = usersStatus.getOrDefault(chatId, 0);
//
//            // Create a SendMessage object with chatId and status string
//            SendMessage sm = new SendMessage();
//            sm.setChatId(chatId.toString());
//
//            switch (status) {
//                case 0:
//                    sm.setText("First Question");
//                    usersStatus.put(chatId, 1);
//                    break;
//                case 1:
//                    // Fetch the user's response and process.
//                    String firstAnswer = update.getMessage().getText();
//                    sm.setText("Second Question");
//                    usersStatus.put(chatId, 2);
//                    break;
//                // Add additional cases for more questions
//                case 2:
//                    String secondAnswer = update.getMessage().getText();
//                    sm.setText("Third Question");
//                    usersStatus.put(chatId, 3);
//                    break;
//                // After the last question, process all the answers and return the output.
//                case 3:
//                    String thirdAnswer = update.getMessage().getText();
//                    JsonObject response = LaptopSelectorStarter.main(firstAnswer, secondAnswer, thirdAnswer));
//                    sm.setText(response.toString());
//                    usersStatus.remove(chatId); // Removes the user status after processing
//                    break;
//            }
//
//            try {
//                execute(sm); // Sending the response message
//            } catch (TelegramApiException e){
//                e.printStackTrace();
//            }
//        }
    }

    private void sendMessage(long chatId, String textToSend) {
        // отправляет сообщение пользователю бота
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void startCommandReceived(long chatId) {
        String answer = "Здравствуй, человек! Я бот, который умеет искать ноутбуки";
        sendMessage(chatId, answer);

        // инициализируем контейнеры для хранения истории диалога бота с пользователем
        Integer status = usersStatus.put(chatId, 0);
        Map<String, String> inputMap = new HashMap<>();

//        switch (status) {
//            case 0:
//                String questionModelName = "Введите бренд и желательно название линейки (уточнение линейки ускорит поиск):";
//                sendMessage(chatId, questionModelName);
//
//                usersStatus.put(chatId, 1);
//        }

    }



    @Override
    public String getBotUsername() {
        // Return your bot's username
        return config.botName;
    }

    @Override
    public String getBotToken() {
        // Return your bot's token
        return config.token;
    }
}
