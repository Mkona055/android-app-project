package ca.novigrad;

import java.util.ArrayList;
import java.util.List;

public class RequestServices {
    private String firstName, secondName, adresse;
    private ArrayList<String> requestSend;

    public RequestServices(String firstName, String secondName, String adresse) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.adresse = adresse;
        requestSend = new ArrayList<>();
        requestSend.add("Annis Well");
        requestSend.add("Fredy Max");
    }


    public boolean fillForm(){
        if(firstName == ""){
            return false;
        }
        if(secondName == ""){
            return false;
        }
        if(adresse == ""){
            return false;
        }
        if(requestSend.contains(firstName+" "+ secondName)){
            return false;
        }
        return true;
    }
    public ArrayList<String> getRequestSend(){
        return requestSend;
    }


}
