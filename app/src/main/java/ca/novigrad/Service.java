package ca.novigrad;

import java.util.List;

public class Service {
    private String serviceName;
    private List<FieldName> form;
    private List<Document> documents;

    public Service(){

    }

    public String getServiceName(){
        return serviceName;
    }
    public  List<FieldName> getForm(){
        return form;
    }
    public List<Document> getDocuments(){
        return documents;
    }

}
