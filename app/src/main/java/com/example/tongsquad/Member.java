package com.example.tongsquad;

public class Member {
    private String memberName,age, university, department;

    public Member() {
    }

    public Member(String memberName, String age, String university, String department) {
        this.memberName = memberName;
        this.age = age;
        this.university = university;
        this.department = department;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getAge() {
        return age;
    }

    public String getUniversity() {
        return university;
    }

    public String getDepartment() {
        return department;
    }
}
