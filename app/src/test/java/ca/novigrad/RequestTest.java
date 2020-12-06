package ca.novigrad;

import android.widget.ListView;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RequestTest {

    private Request request;
    private String sender;
    private String status;
    private String serviceRequested;
    private  String selectedKey;

    private SelectServicesForCustomer serviceSelected;
    private ListView servicesForCustomer;


    private  FillForm form;
    private FillDocument doc;

    @Test
    public void checkRequestText(){
        sender = "paul";
        serviceRequested = "driver licence";

        request = new Request(sender,status,serviceRequested);
        request.setStatus("myStatus");

        assertEquals("paul", request.getSender());
        assertEquals("myStatus", request.getStatus());
        assertEquals("driver licence", request.getServiceRequested());
        assertEquals(null, request.getServiceSelectedKey());

    }
    @Test
    public void checkSelectServicesForCustomer(){

    }
    @Test
    public void checkFillForm(){

    }
    @Test
    public void checkFillDocument(){

    }
}
