package org.example.laptopselectorbot;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// консольный класс, более не используется

public class InputReader {
    public static Map loadUserInput() throws IOException {
        // считываем инпут пользователя
        Map<String, String> inputMap = new HashMap<>();
        Scanner userInput = new Scanner(System.in);

        try {
            System.out.println("Введите бренд и желательно название линейки (уточнение линейки ускорит поиск):");

            String modelName = userInput.nextLine();
            if (!modelName.equalsIgnoreCase("exit")) {
                inputMap.put("model_name", modelName);
            } else {
                userInput.close();
                throw new RuntimeException("exiting...");

            }
//
            System.out.println("Нужен определенный производитель процессоров (в случае с ноутбками Apple указывать процессор не нужно!) (y/n)?");
            String cpuInput = userInput.nextLine();
            if (cpuInput.equalsIgnoreCase("y")) {
                while (!inputMap.containsKey("cpu_name")) {
                    System.out.println("Введите название производителя процессоров (amd или intel):");
                    String cpuName = userInput.nextLine();
                    if (!cpuName.equalsIgnoreCase("exit")) {
                        if (cpuName.equalsIgnoreCase("amd") || cpuName.equalsIgnoreCase("intel")) {
                            inputMap.put("cpu_name", cpuName);
                        } else {
                            System.out.println("Неверное название производителя!");
                        }
                    } else {
                        userInput.close();
                        throw new RuntimeException("exiting...");
                    }
                }
            }
//
            System.out.println("Нужна игровая видеокарта (y/n)?");
            String gpuInput = userInput.nextLine();
            if (gpuInput.equalsIgnoreCase("y")) {
                while (!inputMap.containsKey("gpu_name")) {
                    System.out.println("Введите производителя игровой видеокарты (amd или nvidia):");
                    String gpuName = userInput.nextLine();
                    if (!gpuName.equalsIgnoreCase("exit")) {
                        if (gpuName.equalsIgnoreCase("amd") || gpuName.equalsIgnoreCase("nvidia")) {
                            inputMap.put("gpu_name", gpuName);
                        } else {
                            System.out.println("Неверное название производителя!");
                        }
                    } else {
                        userInput.close();
                        throw new RuntimeException("exiting...");
                    }
                }
            }

            System.out.println("Нужен определенный вид оперативной памяти (y/n)?");
            String memInput = userInput.nextLine();
            if (memInput.equalsIgnoreCase("y")) {
                while (!inputMap.containsKey("memory_type")) {
                    System.out.println("Введите тип оперативной памяти (DDR4 или DDR5): ");
                    String memType = userInput.nextLine();
                    if (!memType.equalsIgnoreCase("exit")) {
                        if (memType.equalsIgnoreCase("ddr4") || memType.equalsIgnoreCase("ddr5")) {
                            inputMap.put("memory_type", memType);
                        } else {
                            System.out.println("Неверный вид оперативной памяти!");
                        }
                    } else {
                        userInput.close();
                        throw new RuntimeException("exiting...");
                    }
                }
            }

            System.out.println("Введите бюджет для покупки ($) (если не заданы доп.параметры поиска, то будет произведена попытка найти самый производительный по процессору):");
            String inputBudget = userInput.nextLine();
            try {
                int budget = Integer.parseInt(inputBudget);
                inputMap.put("budget", inputBudget);
            } catch (NumberFormatException e) {
                System.out.println("Invalid budget! exiting...");
                System.exit(0);
            }

        } catch (RuntimeException runtimeException) {
            System.out.println(runtimeException.getMessage());
            System.exit(0);
        }

        return inputMap;
    }
}

