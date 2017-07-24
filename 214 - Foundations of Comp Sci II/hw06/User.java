package com.example.bitterplatform.source;



/* ----------------------------- IMPORTS ----------------------------- */

// Java Imports
import java.io.Serializable;


/* ----------------------------- CLASS DEF ----------------------------- */


public class User implements Serializable {

    /* ----------------------- INSTANCE VAR ----------------------- */

    private String name;
    private Email email;    // needed to see list of followers/following accounts


    /* ----------------------- CONSTRUCTORS ----------------------- */
    /**
     * Default User constructor.
     * @param  name          the real name of the user
     * @return               User object as specified
     */
    public User(String name){
        this.name = name;
    }

    public User(String name, Email email){
        this.name = name;
        this.email = email;
    }


    /* ----------------------- METHODS ----------------------- */

    /**
     * Retrieves the User's name.
     * @return String name
     */
    public String getName() { return this.name; }

    /**
     * Sets the User's name.
     * @param name the User's name
     */
    public void setName(String name) { this.name = name; }

    /**
     * Retrieves the User's Email.
     * @return Email email
     */
    public Email getEmail() { return this.email; }

    /**
     * Sets the User's Email.
     * @param email the User's Email
     */
    public void setEmail(Email email) { this.email = email; }

    /**
     * Stringifies the User.
     * @return String details about the User
     */
    public String toString(){
        String output = "";
        output+= "User Name: " + name + "<br> Email: " + email.getEmail();
        return output;
    }

    public String toSpinnerString(){
        String output = "";
        output+= name + ", " + email.getEmail();
        return output;
    }

}
