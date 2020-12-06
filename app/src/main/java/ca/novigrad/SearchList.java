package ca.novigrad;

public class SearchList {


    private String BranchID;
    private String BranchAddress;





    public SearchList(){

    }

    public SearchList(String BranchAddress, String BranchID) {
        this.BranchAddress = BranchAddress;
        this.BranchID = BranchID;


    }



    public String getBranchID() {
        return this.BranchID;
    }

    public void setBranchID(String BranchID) {
        this.BranchID = BranchID;
    }

    public String getBranchAddress() {
        return this.BranchAddress;
    }

    public void setBranchAddress(String BranchAddress) {
        this.BranchAddress = BranchAddress;
    }



}
