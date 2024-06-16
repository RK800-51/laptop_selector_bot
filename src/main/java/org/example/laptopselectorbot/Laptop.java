package org.example.laptopselectorbot;

public class Laptop {
    private static String modelName; // full name: brand + numbers
    private static String cpu;
    private static String gpu;
    private static String memoryType;
    private static String storage;
    private static String display_type;
    private static String display_res;
    private static String imgUrl; // img of laptop in the online store
    private static String batteryCapacity;
    private static int budget;

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public String getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(String batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }
    public String getModel(){
        return modelName;
    }

    public void setModel(String modelName){
        this.modelName = modelName;
    }

    public String getUrl() {
        return imgUrl;
    }

    public void setUrl(String imgUrl) {
        this.imgUrl = this.imgUrl;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getGpu() {
        return gpu;
    }

    public void setGpu(String gpu) {
        this.gpu = gpu;
    }

    public String getMemoryType() {
        return memoryType;
    }

    public void setMemoryType(String memory_type) {
        this.memoryType = memory_type;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getDisplay_type() {
        return display_type;
    }

    public void setDisplay_type(String display_type) {
        this.display_type = display_type;
    }

    public String getDisplay_res() {
        return display_res;
    }

    public void setDisplay_res(String display_res) {
        this.display_res = display_res;
    }
}