package org.example.laptopselectorbot;

import org.json.JSONObject;
import org.example.laptopselectorbot.RequestSenderByID;

import java.io.IOException;
import java.util.ArrayList;

public class Analyzer {
    private static Laptop requiredLaptop;
    private static LaptopCandidate candidateLaptop;
    private static Double topCpuRating = 0.0;
    private static int idTopCpuRating;

    public static JSONObject compareLaptops(LaptopCandidate laptopCandidate, Laptop requestedLaptop, ArrayList<Integer> laptopsIDList) throws IOException {
        candidateLaptop = laptopCandidate;
        requiredLaptop = requestedLaptop;
        if (!laptopsIDList.isEmpty() && ((requiredLaptop.getGpu() != null) && (requiredLaptop.getMemoryType() != null))) {
            for (int i = 0; i < laptopsIDList.size(); i++) {
                JSONObject oneLaptop = RequestSenderByID.getCandidate(laptopsIDList.get(i));
                if (!oneLaptop.isEmpty()) {
                    laptopCandidate.loadJSONobj(oneLaptop);
                    if (compareBudgets() && compareGPU() && compareRAM()) {
                        // возвращаем подошедший по параметрам ноутбук
                        return oneLaptop;
                    }

                }
            }
            return new JSONObject().put("message", "Подходящего ноутбука не найдено, попробуйте выбрать другого производителя процессоров, если вам интересная данная линейка ноутбуков"); // возвращаем JSON-объект с сообщением

        } else if (!laptopsIDList.isEmpty()) {
            for (int i = 0; i < laptopsIDList.size(); i++) {
                JSONObject oneLaptop = RequestSenderByID.getCandidate(laptopsIDList.get(i));
                if (!oneLaptop.isEmpty()) {
                    candidateLaptop.loadJSONobj(oneLaptop);
                    if (compareBudgets()) {
                        decideTopCpuRating();
                    }
                }
            }
            return idTopCpuRating > 0 ? RequestSenderByID.getCandidate(idTopCpuRating): new JSONObject().put("message", "Подходящего ноутбука в указанном ценовом пределе не найдено");
        }
        return new JSONObject().put("message", "Подходящего ноутбука не найдено, проверьте правильность введенного названия, либо данного ноутбука нет в продаже/базе данных"); // возвращаем JSON-объект с сообщением
    }

    public static boolean compareBudgets() {
        return requiredLaptop.getBudget() >= candidateLaptop.getPrice();
    }

    public static void decideTopCpuRating() {
        if (candidateLaptop.getCpuRating() > topCpuRating) {
            topCpuRating = candidateLaptop.getCpuRating();
            idTopCpuRating = candidateLaptop.getId();
        }

    }
    public static boolean compareGPU() {
        if (candidateLaptop.getGpu().equals(requiredLaptop.getGpu())) {
            return true;
            // return candidateLaptop.getGpu() + " " + candidateLaptop.getGpuModel();
        }
        return false;

    }
    public static boolean compareRAM() {
        if (candidateLaptop.getMemoryType().equals(requiredLaptop.getMemoryType())) {
            return true;
            // return candidateLaptop.getMemorySize() + " Gb" + " " + candidateLaptop.getMemoryType() ;
        } else {
            return false;
        }
    }
}
