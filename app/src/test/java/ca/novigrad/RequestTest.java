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

    private Pair pair;
    private String fieldName;
    private String filling;

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
    public void checkPair(){
        fieldName = "myFieldName";
        pair = new Pair(fieldName, filling);
        pair.setFilling("myFilling");
        assertEquals("myFieldName", pair.getFieldName());
        assertEquals("myFilling", pair.getFilling());

    }

}
