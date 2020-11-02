package ca.novigrad;

public class AccountList {


    private String FullName;
    private String Email;
    private String PhoneNumber;
    private String Role;




    public AccountList(){

    }

    public AccountList(String Email, String FullName, String PhoneNumber, String Role) {
        this.Email = Email;
        this.FullName = FullName;
        this.PhoneNumber = PhoneNumber;
        this.Role = Role;


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

    public String getRole() {
        return this.Role;
    }

    public void setRole(String Role) {
        this.Role = Role;
    }
}
