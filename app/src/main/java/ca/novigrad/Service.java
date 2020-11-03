package ca.novigrad;

import java.util.HashMap;
import java.util.List;

public class Service {
    private String serviceName;
    private String serviceID;
    private HashMap<String,String> Form;
    private HashMap<String,String> Documents;

    public Service(){

    }

    public String getServiceName(){
        return serviceName;
    }
    public  HashMap getForm(){
        return Form;
    }
    public HashMap getDocuments(){
        return Documents;
    }
    public String getServiceID(){
        return serviceID;
    }

}
