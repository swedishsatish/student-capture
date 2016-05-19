package studentcapture.user;

/**
 * Created by c12ton on 5/12/16.
 * Will act as a container for information of a user
 */
public class User {


    private String userName;
    private String fName;
    private String lName;
    private String email;
    private String pswd;
    private String userID;



    //Needed because of json
    public User() {}

    public User(String userName, String fName, String lName,
                String email,String pswd) {

        this.userName = userName;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.pswd = pswd;

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return this.userID;
    }


    @Override
    public boolean equals(Object other) {
        User that = (User) other;

        return (that.getUserName() == this.getUserName())
                && (that.getfName() == this.fName)
                && (that.getlName() == this.lName)
                && (that.getEmail() == this.email)
                && (that.getPswd()  == this.pswd);
    }
}
