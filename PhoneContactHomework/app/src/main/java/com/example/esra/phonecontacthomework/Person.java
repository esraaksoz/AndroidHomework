package com.example.esra.phonecontacthomework;

/**
 * Created by Esra on 26.03.2016.
 */
public class Person {
    private String PersonName;
    private String PersonNumber;
    private int ImageID;

    public Person(String name, String number,int img_Id){
        PersonName = name;
        PersonNumber = number;
        ImageID=img_Id;

    }

    public String getPersonName()

    {
        return PersonName;
    }

    public String getPersonNumber()
    {
        return PersonNumber;
    }

    public  void setPersonName(String personName)

    {
        PersonName=personName;
    }

    public  void setPersonNumber(String personNumber)
    {
        PersonNumber=personNumber;
    }

}



