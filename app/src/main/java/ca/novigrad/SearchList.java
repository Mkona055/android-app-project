package ca.novigrad;

public class SearchList {


    private String BranchID;
    private String BranchAddress;
    private String Rating;





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

    public String getRating() {
        return this.Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public void setBranchAddress(String BranchAddress) {
        this.BranchAddress = BranchAddress;
    }



}
