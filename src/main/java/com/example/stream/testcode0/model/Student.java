package com.example.stream.testcode0.model;

import lombok.Data;

@Data
public class Student {

    private int kor;
    private int eng;
    private int math;

    public Student() {
    }

    public Student(int kor, int eng, int math) {
        this.kor = kor;
        this.eng = eng;
        this.math = math;
    }
}
