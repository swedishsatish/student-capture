package studentcapture.user;

/**
 * Created by c12ton on 5/12/16.
 * Will act as a container for information of a user
 */
public class User {


    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String pswd;
    private String userID;
    private String token;  //Will be used for recovery of password


    //Needed because of json
    public User() {}

    public User(String userName, String fName, String lName,
                String email,String pswd) {

        this.userName = userName;
        this.firstName = fName;
        this.lastName = lName;
        this.email = email;
        this.pswd = pswd;

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object other) {
        User that = (User) other;

        return (that.getUserName() == this.getUserName())
                && (that.getFirstName() == this.firstName)
                && (that.getLastName() == this.lastName)
                && (that.getEmail() == this.email)
                && (that.getPswd()  == this.pswd)
                && (that.getToken() == this.token);
    }
}
