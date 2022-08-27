package com.example.stream.testcode0.model;

import lombok.Data;

@Data
public class Person {
    private String name;
    private int age;
    private String phoneNumber;

    public Person(String name, int age, String phoneNumber) {
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
    }
    @Override
    public String toString() {
        return "name:"+name+" age:"+age+" phone:"+phoneNumber;
    }

}
