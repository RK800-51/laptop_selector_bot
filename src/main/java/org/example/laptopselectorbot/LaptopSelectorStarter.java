package org.example.laptopselectorbot;


import org.example.laptopselectorbot.bot_logic.LaptopSelectorBot;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.example.laptopselectorbot.bot_logic.LaptopSelectorBot.*;


public class LaptopSelectorStarter
{
    public static void start() throws IOException {

        // считываем инпут пользователя
        Map<String, String> inputMap = null;
        if (LaptopSelectorBot.isInputReady()) {
            inputMap = LaptopSelectorBot.getInputMap();
        }
        // скармливаем параметры парсеру и создаем объект желаемого ноутбука
        LaptopStoreParser parsedLaptop = new LaptopStoreParser();
        Laptop laptopObj = new Laptop();

        // возвращает список ID подходящих по процессору ноутбуков (если процессор был указан)
        ArrayList<Integer> laptopsIDList = parsedLaptop.getLaptopsIDByCPU(inputMap, laptopObj);
        // проходим по списку полученных ID, запрашиваем из БД подорбную информацию по каждой модели из списка
        LaptopCandidate laptopCandidate = new LaptopCandidate();
        // отдаем анализатору ноутбуки на сравнение, вернет 1 JSON-объект с найденным ноутбуком или 1 JSON-объект с
        // сообщением, что подходящий ноутбук не найден
        JSONObject result = Analyzer.compareLaptops(laptopCandidate, laptopObj, laptopsIDList);
        LaptopSelectorBot.loadJSONResult(result);
        //System.out.println(result.toString(4));
    }
}

