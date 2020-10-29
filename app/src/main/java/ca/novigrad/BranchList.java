package ca.novigrad;

public class BranchList {


    private String FullName;
    private String Email;
    private String PhoneNumber;




    public BranchList(){

    }

    public BranchList( String Email, String FullName, String PhoneNumber) {
        this.Email = Email;
        this.FullName = FullName;
        this.PhoneNumber = PhoneNumber;


    }



    public String getFullName() {
        return this.FullName;
    }

    public void setFullName(String FullName) {
        this.FullName = FullName;
    }

    public String getEmail() {
        return this.Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }


    public String getPhoneNumber() {
        return this.PhoneNumber;
    }

    public void setPhoneNumber(String PhoneNumber) {
        this.PhoneNumber = PhoneNumber;
    }

}
