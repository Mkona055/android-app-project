package ca.novigrad;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class RequestServicesTest  {
    private String firstName;
    private String secondName;
    private  String adresse;
    private RequestServices request;
    private ArrayList<String> requestAlreadySend;

    @Test
    public void checkFirstName (){
        firstName = "";
        secondName = "bruno";
        adresse = "Ottawa";
        request= new RequestServices(firstName, secondName, adresse);
        assertEquals(false,request.fillForm());
    }
    @Test
    public void checkSecondName(){
        firstName = "John";
        secondName = "";
        adresse = "Montreal";
        request= new RequestServices(firstName, secondName, adresse);
        assertEquals(false,request.fillForm());
    }
    @Test
    public void checkAdresse(){
        firstName = "John";
        secondName = "bruno";
        adresse = "";
        request= new RequestServices(firstName, secondName, adresse);
        assertEquals(false,request.fillForm());
    }
    @Test
    public void checkRequestAlreadySend(){
        firstName = "Fredy";
        secondName = "Max";
        adresse = "Gatineau";
        request= new RequestServices(firstName, secondName, adresse);
        assertEquals(false,request.fillForm());


    }

}