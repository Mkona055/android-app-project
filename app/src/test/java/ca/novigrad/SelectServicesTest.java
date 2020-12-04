package ca.novigrad;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SelectServicesTest {

    private SelectServices selectServices;
    private ArrayList<String> services;
    private ArrayList<String> servicesOffered;
    private ArrayList<Service> servicesInfo;

    @Test
    public void checkEquivalentTest(){

        servicesOffered = new ArrayList<String>();

        Service service1 = new Service("Photo ID");
        servicesOffered.add(service1.getServiceName());

        Service service2 = new Service("Driver's license");
        servicesOffered.add(service2.getServiceName());

        selectServices = new SelectServices();
        services = new ArrayList<String>();
        servicesInfo = new ArrayList<Service>();
        Service service = new Service("Health Card");

        selectServices.checkEquivalent(service, servicesOffered, services, servicesInfo );
        Service serviceTest = new Service("Health Card");

        assertEquals("Health Card", services.get(0));
        assertEquals(serviceTest.getServiceName(), servicesInfo.get(0).getServiceName());
    }

}