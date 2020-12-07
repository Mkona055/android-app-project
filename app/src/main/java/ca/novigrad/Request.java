package ca.novigrad;

public class Request {
    private String sender;
    private String status;
    private String serviceRequested;
    private String serviceSelectedKey;
    private boolean missingDocuments;
    public Request(String sender,String status,String serviceRequested){
        this.sender = sender;
        this.status = status;
        this.serviceRequested = serviceRequested;
    }
    public Request(){

    }

    public String getSender() {
        return sender;
    }

    public String getServiceRequested() {
        return serviceRequested;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isMissingDocuments() {
        return missingDocuments;
    }

    public String getServiceSelectedKey() {
        return serviceSelectedKey;
    }
}
