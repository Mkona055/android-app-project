package ca.novigrad;

import java.util.HashMap;
import java.util.List;
//all services have to content a list of document and form
public class Service {
    private String serviceName;
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

}
