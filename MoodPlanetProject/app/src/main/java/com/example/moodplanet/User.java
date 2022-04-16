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
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     */
    public User(String email, String firstName, String lastName, String password) {
        this.email = email;
        this.password = password;
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
