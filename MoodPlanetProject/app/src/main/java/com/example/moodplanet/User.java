package com.example.moodplanet;

/**
 * Created by Chilka Castro on 4/11/2022.
 */
public class User {
    private String email;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;

    /**
     * Constructor for the User class
     * @param email the email of the user
     * @param password the password of the user
     * @param confirmPassword the confirm password of the user
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     */
    public User(String email, String firstName, String lastName, String password, String confirmPassword) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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
}
