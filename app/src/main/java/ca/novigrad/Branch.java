package ca.novigrad;

import java.util.HashMap;

public class Branch {
    private String branchID;
    private String branchAddress;
    private HashMap<String,HashMap<String,String>> schedule;

    public Branch(){

    }
    public String getBranchAddress() {
        return branchAddress;
    }

    public String getBranchID() {
        return branchID;
    }
}
