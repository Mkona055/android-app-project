package ca.novigrad;

import java.util.HashMap;
import java.util.List;
//all services have to content a list of document and form
public class Service {
    private String serviceName;
    private String serviceID;
    private HashMap<String,String> form;
    private HashMap<String,String> documents;

    public Service(){
    }
    public Service (String serviceName){
        this.serviceName = serviceName;
    }
    public String getServiceName(){
        return serviceName;
    }
    public  HashMap getForm(){
        return form;
    }
    public HashMap getDocuments(){
        return documents;
    }
    public String getServiceID(){
        return serviceID;
    }

}
