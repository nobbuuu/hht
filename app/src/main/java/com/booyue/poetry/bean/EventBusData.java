package com.booyue.poetry.bean;

public class EventBusData {

    private String name;
    private String age;

    @Override
    public String toString() {
        return "EventBusData{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }

    public EventBusData() {
    }

    public EventBusData(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
