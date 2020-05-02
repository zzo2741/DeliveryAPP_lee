package model;

import java.io.Serializable;

public class UserSignupModel implements Serializable {

    public String user_email = "";
    public String user_password = "";
    public String user_name = "";
    public String user_mileage = "";

    public UserSignupModel() {
    }

    public UserSignupModel(String user_email, String user_password, String user_name, String user_mileage) {
        this.user_email = user_email;
        this.user_password = user_password;
        this.user_name = user_name;
        this.user_mileage = user_mileage;
    }
}
