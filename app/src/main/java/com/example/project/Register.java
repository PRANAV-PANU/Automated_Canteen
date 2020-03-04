package com.example.project;

public class Register {

    public  String rName,rRollNo,rEmail,rPhone;

    public Register(){

    }

    public Register(String rName, String rRollNo, String rEmail, String rPhone) {
        this.rName = rName;
        this.rRollNo = rRollNo;
        this.rEmail = rEmail;
        this.rPhone = rPhone;
    }

    public String getrName() {
        return rName;
    }

    public String getrRollNo() {
        return rRollNo;
    }

    public String getrEmail() {
        return rEmail;
    }

    public String getrPhone() {
        return rPhone;
    }
}
