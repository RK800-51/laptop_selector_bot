package org.example.laptopselectorbot.bot_logic;

import org.apache.commons.lang3.StringUtils;
import org.example.laptopselectorbot.LaptopSelectorStarter;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.example.laptopselectorbot.LaptopSelectorStarter.*;

@Component
public class LaptopSelectorBot extends TelegramLongPollingBot {
    private HashMap<Long, Integer> usersStatus = new HashMap<>();
    public static Map<String, String> inputMap = new HashMap<>();
    public static Map<String, String> data = new HashMap<>();
    private static boolean _inputReady = false;
    private static JSONObject _result;
    final BotConfig config;

    public LaptopSelectorBot(BotConfig config) {
        this.config = config;
    }

    public static Map<String, String> getInputMap() {
        return inputMap;
    }

    public static Map<String, String> clearInputMap() {
        inputMap.clear();
        return inputMap;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            Integer status = usersStatus.getOrDefault(chatId, 0);

            // набор состояний, по которым переходит пользователь, отвечая боту
            switch (status) {
                case 0: // стартовый кейс, запускает диалог и дает список команд
                    if (messageText.equals("/start")) {
                        startCommandReceived(chatId);
                        clearInputMap();
                        sendMessage(chatId, "Введите бренд и желательно название линейки (уточнение линейки ускорит поиск):");
                        usersStatus.put(chatId, 1);
                        break;
                    }
                    else if (update.getMessage().getText().equalsIgnoreCase("/exit")) {
                        usersStatus.put(chatId, 0);
                        sendMessage(chatId, "завершение работы..");
                        break;
                    }
                    else {
                        sendMessage(chatId, "Команда не распознана, известные команды: /start, /exit");
                        break;
                    }
                case 1: // уточняет, нужен ли определенный производитель процессора
                    if (!update.getMessage().getText().equalsIgnoreCase("/exit")) {
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        inputMap.put("model_name", update.getMessage().getText());
                        sendMessage(chatId, "Нужен определенный производитель процессоров (в случае с ноутбками Apple указывать процессор не нужно!) (y/n)?");
                        usersStatus.put(chatId, 2);
                        break;
                    } else {
                        usersStatus.put(chatId, 0);
                        sendMessage(chatId, "завершение работы..");
                        break;
                    }
                case 2: // позволяет выбрать производителя процессора или пойти дальше
                    if (update.getMessage().getText().equalsIgnoreCase("/exit")) {
                        usersStatus.put(chatId, 0);
                        sendMessage(chatId, "завершение работы..");
                        break;
                    }
                    else if (update.getMessage().getText().equalsIgnoreCase("y")) {
                        sendMessage(chatId, "Введите название производителя процессоров (amd или intel):");
                        usersStatus.put(chatId, 3);
                        break;
                        }
                    else if (update.getMessage().getText().equalsIgnoreCase("n")) {
                        sendMessage(chatId, "Нужна игровая видеокарта (y/n)?");
                        usersStatus.put(chatId, 4);
                        break;
                }
                case 3: // записывает производителя процессора и валидирует его название
                    if (update.getMessage().getText().equalsIgnoreCase("/exit")) {
                        usersStatus.put(chatId, 0);
                        sendMessage(chatId, "завершение работы..");
                        break;
                    }
                    while (!inputMap.containsKey("cpu_name")) {
                        if (update.getMessage().getText().equalsIgnoreCase("amd")
                                           || update.getMessage().getText().equalsIgnoreCase("intel")) {
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            inputMap.put("cpu_name", update.getMessage().getText());
                            sendMessage(chatId, "Нужна игровая видеокарта (y/n)?");
                            usersStatus.put(chatId, 4);
                        }
                        else if (!update.getMessage().getText().equalsIgnoreCase("/exit")) {
                            sendMessage(chatId, "Неверное название производителя!");
                            break;
                        }
                    } break;
                case 4: // предлагает выбрать производителя видеокарты или конкретизировать ли вид RAM
                    if (update.getMessage().getText().equalsIgnoreCase("/exit")) {
                        usersStatus.put(chatId, 0);
                        sendMessage(chatId, "завершение работы..");
                        break;
                    }
                    else if (update.getMessage().getText().equalsIgnoreCase("y")) {
                        sendMessage(chatId, "Введите производителя игровой видеокарты (amd или nvidia):");
                        usersStatus.put(chatId, 5);
                        break;
                    }
                    else if (update.getMessage().getText().equalsIgnoreCase("n")) {
                        sendMessage(chatId, "Нужен определенный вид оперативной памяти (y/n)?");
                        usersStatus.put(chatId, 6);
                        break;
                    }

                case 5: // записывает выбранный вид производителя видеокарты или конкретизирует вид RAM
                    if (update.getMessage().getText().equalsIgnoreCase("/exit")) {
                        usersStatus.put(chatId, 0);
                        sendMessage(chatId, "завершение работы..");
                        break;
                    }
                    while (!inputMap.containsKey("gpu_name")) {
                        if (update.getMessage().getText().equalsIgnoreCase("amd")
                                || update.getMessage().getText().equalsIgnoreCase("nvidia")) {
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            inputMap.put("gpu_name", update.getMessage().getText());
                            sendMessage(chatId, "Нужен определенный вид оперативной памяти (y/n)?");
                            usersStatus.put(chatId, 6);
                        }
                        else if (!update.getMessage().getText().equalsIgnoreCase("/exit")) {
                            sendMessage(chatId, "Неверное название производителя!");
                            break;
                        }
                    } break;

                case 6: // предлагает выбрать тип оперативной памяти или предлагает ввести бюджет покупки
                    if (update.getMessage().getText().equalsIgnoreCase("/exit")) {
                        usersStatus.put(chatId, 0);
                        sendMessage(chatId, "завершение работы..");
                        break;
                    }
                    else if (update.getMessage().getText().equalsIgnoreCase("y")) {
                        sendMessage(chatId, "Введите тип оперативной памяти (DDR4 или DDR5):");
                        usersStatus.put(chatId, 7);
                        break;
                    }
                    else if (update.getMessage().getText().equalsIgnoreCase("n")) {
                        sendMessage(chatId, "Введите бюджет для покупки ($) (если не заданы доп.параметры" +
                                " поиска, то будет произведена попытка найти самый производительный по процессору):");
                        usersStatus.put(chatId, 8);
                        break;
                    }

                case 7: // предлагает ввести бюджет для покупки или валидирует вид оперативной памяти
                    if (update.getMessage().getText().equalsIgnoreCase("/exit")) {
                        usersStatus.put(chatId, 0);
                        sendMessage(chatId, "завершение работы..");
                        break;
                    }
                    while (!inputMap.containsKey("memory_type")) {
                        if (update.getMessage().getText().equalsIgnoreCase("ddr4")
                                || update.getMessage().getText().equalsIgnoreCase("ddr5")) {
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            inputMap.put("memory_type", update.getMessage().getText());
                            sendMessage(chatId, "Введите бюджет для покупки ($) (если не заданы доп.параметры" +
                                    " поиска, то будет произведена попытка найти самый производительный по процессору):");
                            usersStatus.put(chatId, 8);
                        }
                        else if (!update.getMessage().getText().equalsIgnoreCase("/exit")) {
                            sendMessage(chatId, "Неверный вид оперативной памяти!");
                            break;
                        }
                    } break;

                case 8: // заносит бюджет в вводные данные или валидирует его
                    if (update.getMessage().getText().equalsIgnoreCase("/exit")) {
                        usersStatus.put(chatId, 0);
                        sendMessage(chatId, "завершение работы..");
                        break;
                    }
                    while (!inputMap.containsKey("budget")) {
                        if (StringUtils.isNumeric(update.getMessage().getText())) {
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            inputMap.put("budget", update.getMessage().getText());
                            sendMessage(chatId, "Спасибо! Поиск ноутбука запущен..");
                            _inputReady = true;
                            try {
                                LaptopSelectorStarter.start();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            usersStatus.put(chatId, 0);
                            sendMessage(chatId, createResponse(_result));
                        }
                        else if (!update.getMessage().getText().equalsIgnoreCase("/exit")) {
                            sendMessage(chatId, "Бюджет должен быть числом!");
                            break;
                        }
                    } break;
            }
        }
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
    }

    public static boolean isInputReady(){
        return _inputReady;
    }

    public static void loadJSONResult(JSONObject result){
        _result = result;
    }

    private String createResponse(JSONObject _result) {
        StringBuilder builder = new StringBuilder();
        if (_result.has("0")) {
            JSONObject info = _result.getJSONObject("0");
                String modelName = info.getJSONArray("model_info").getJSONObject(0)
                        .getString("noteb_name");
                String cpuProd = info.getJSONObject("cpu").getString("prod");
                String cpuModel = info.getJSONObject("cpu").getString("model");
                String memoryType = info.getJSONObject("memory").getString("type");
                String memorySize = info.getJSONObject("memory").getString("size") + " " + "Гб";
                String memorySpeed = info.getJSONObject("memory").getString("speed") + " " + "МГц";
                String gpuProd = info.getJSONObject("gpu").getString("prod");
                String gpuModel = info.getJSONObject("gpu").getString("model");
                String displaySize = info.getJSONObject("display").getString("size") + " " + "дюймов";
                String price = info.getString("config_price");
                String picture = info.getJSONObject("model_resources").getString("thumbnail");
                builder.append(picture).append("\n");
                builder.append("Модель ноутбука: ").append(modelName).append("\n");
                builder.append("Процессор: ").append(cpuProd).append(" ").append(cpuModel).append("\n");
                builder.append("Оперативная память: ").append(memoryType).append(" ").append(memorySize).
                        append(" ").append(memorySpeed).append("\n");
                builder.append("Видеокарта: ").append(gpuProd).append(" ").append(gpuModel).append("\n");
                builder.append("Диагональ дисплея: ").append(displaySize).append("\n");
                builder.append("Цена: ").append(price).append("$");
                return builder.toString();
        }
        else {
            String message = _result.getString("message");
            builder.append(message);
            return builder.toString();
        }
    }

    @Override
    public String getBotUsername() {
        // возвращает имя вашего бота
        return config.botName;
    }

    @Override
    public String getBotToken() {
        // возвращает токен вашего бота
        return config.token;
    }
}
