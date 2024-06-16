package org.example.laptopselectorbot;

import org.json.JSONObject;

// хранит объект ноутбука-кандидата и его параметры

public class LaptopCandidate {
    private static int id;
    private static String modelName; // full name: brand + numbers
    private static String cpu;
    private static String gpu;
    private static String memoryType;
    private static String memorySize;
    private static int price;
    private static Double cpuRating = 0.0; // обязательный параметр для сравнения, если пользователь не указал cpu и/или gpu
    private static String gpuModel;


    // TODO: дописать парсинг параметров одного ноутбука (добавить параметры?)

    public void loadJSONobj(JSONObject oneLaptopCandidate) {
        if (oneLaptopCandidate.has("0")) {
            JSONObject laptopCandidateInfo = oneLaptopCandidate.getJSONObject("0");
            id = laptopCandidateInfo.getJSONArray("model_info").getJSONObject(0)
                    .getInt("id");
            modelName = laptopCandidateInfo.getJSONArray("model_info").getJSONObject(0)
                    .getString("noteb_name");
            cpu = laptopCandidateInfo.getJSONObject("cpu").getString("model");
            gpu = laptopCandidateInfo.getJSONObject("gpu").getString("prod").toLowerCase();
            memoryType = laptopCandidateInfo.getJSONObject("memory").getString("type").toLowerCase();
            memorySize = laptopCandidateInfo.getJSONObject("memory").getString("size");
            price = Integer.parseInt(laptopCandidateInfo.getString("config_price"));
            cpuRating = Double.parseDouble(laptopCandidateInfo.getJSONObject("cpu").getString("rating"));
            gpuModel = laptopCandidateInfo.getJSONObject("gpu").getString("model");
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        LaptopCandidate.id = id;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        LaptopCandidate.modelName = modelName;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        LaptopCandidate.cpu = cpu;
    }

    public String getGpu() {
        return gpu;
    }

    public void setGpu(String gpu) {
        LaptopCandidate.gpu = gpu;
    }

    public String getMemoryType() {
        return memoryType;
    }

    public void setMemoryType(String memoryType) {
        LaptopCandidate.memoryType = memoryType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        LaptopCandidate.price = price;
    }

    public double getCpuRating() {
        return cpuRating;
    }

    public void setCpuRating(double cpuRating) {
        LaptopCandidate.cpuRating = cpuRating;
    }

    public String getGpuModel() {
        return gpuModel;
    }

    public void setGpuModel(String gpuModel) {
        LaptopCandidate.gpuModel = gpuModel;
    }

    public String getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(String memorySize) {
        LaptopCandidate.memorySize = memorySize;
    }
}
