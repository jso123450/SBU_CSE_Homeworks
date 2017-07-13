package com.example.bitterplatform.source;

/**
 * Johnny So
 * 111158276
 * Homework #6
 * CSE 214 Recitation 12
 * Recitation TA: Charles Chen
 * Grading TA: Timothy Zhang
 * @author Johnny
 */


/* ----------------------------- IMPORTS ----------------------------- */

// Java Imports
import java.util.HashMap;
import java.io.Serializable;
import java.util.Map;



/* ----------------------------- CLASS DEF ----------------------------- */

public class UserDatabase extends HashMap<String,User> implements Serializable {

    /* ----------------------- CONSTRUCTORS ----------------------- */

    /**
     * Default UserDatabase constructor
     */
    public UserDatabase(){
        super();
    }


    /* ----------------------- METHODS ----------------------- */

    /**
     * Attempts to add a User with the specified Email into the HashMap.
     * @param email desired email
     * @param user  desired user
     */
    public void addUser(Email email, User user){
        if (email == null || email.getEmail().length() == 0)
            throw new IllegalArgumentException("Invalid Email");
        if (containsKey(email.getEmail()))
            throw new IllegalArgumentException("Taken Email");
        if (user == null)
            throw new IllegalArgumentException("Invalid User");
        put(email.getEmail(),user);
    }

    /**
     * Retrieves the User associated with the specified Email.
     * @param  email         the specified Email
     * @return               the User associated with the email
     */
    public User getUser(Email email){
        if (containsKey(email.getEmail()))
            return get(email.getEmail());
        else
            return null;
    }

    /**
     * Retrieves the User associated with the specified email.
     * @param  email         the specified email
     * @return               the User associated with the email
     */
    public User getUser(String email){
        if (containsKey(email))
            return get(email);
        else
            return null;
    }

    /**
     * Attempts to remove the User associated with an Email.
     * @param email the User's Email
     */
    public void removeUser(Email email){
        if (email.getEmail().length() == 0)
            throw new IllegalArgumentException("Invalid Email");
        if (!containsKey(email.getEmail()))
            throw new IllegalArgumentException("Sorry, nobody has this email.");
        remove(email.getEmail());
    }

    /**
     * Attempts to remove the User associated with an email.
     * @param email the User's email
     */
    public void removeUser(String email){
        if (email.length() == 0)
            throw new IllegalArgumentException("Invalid Email");
        if (!containsKey(email))
            throw new IllegalArgumentException("Sorry, nobody has this email.");
        remove(email);
    }

    /**
     * Retrieves the email associated with the specified User.
     * @param user      the specified User
     * @return          the email associated with the User
     */
    public String getEmail(User user){
        if (containsValue(user)){
            String email = "";
            for (Map.Entry<String,User> entry : entrySet()){
                if (entry.getValue() == user) {
                    email = entry.getKey();
                    break;
                }
            }
            return email;
        }
        else
            return null;
    }

    /**
     * Stringifies this UserDatabase.
     * @return String containing details about this UserDatabase
     */
    public String toString(){
        String output = "Users\n";
        for (Entry<String,User> entry : entrySet())
            output+= "Name: " + entry.getValue().getName() + ", Email: " + entry.getKey() + "\n";
        return output;
    }


}
