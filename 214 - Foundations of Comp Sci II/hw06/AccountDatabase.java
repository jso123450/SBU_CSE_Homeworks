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

public class AccountDatabase extends HashMap<String,Account> implements Serializable {

    /* ----------------------- CONSTRUCTORS ----------------------- */

    /**
     * Default AccountDatabase constructor
     */
    public AccountDatabase(){
        super();
    }


    /* ----------------------- METHODS ----------------------- */

    /**
     * Attempts to add an Account with the specified Email into the HashMap.
     * @param email   desired Email
     * @param account desired Account
     */
    public void addAccount(Email email, Account account){
        if (email == null || email.getEmail().length() == 0)
            throw new IllegalArgumentException("Invalid Email");
        if (containsKey(email.getEmail()))
            throw new IllegalArgumentException("Taken Email");
        if (account == null)
            throw new IllegalArgumentException("Invalid Account");
        put(email.getEmail(),account);
    }

    /**
     * Retrieves the Account associated with the specified Email.
     * @param  email         the specified email
     * @return               the Account associated with the email
     */
    public Account getAccount(Email email){
        if (containsKey(email.getEmail()))
            return get(email.getEmail());
        else
            return null;
    }

    /**
     * Retrieves the Account associated with the specified email.
     * @param email     the specified email
     * @return          the Account associated with the email
     */
    public Account getAccount(String email){
        if (containsKey(email))
            return get(email);
        else
            return null;
    }

    /**
     * Retrieves the email associated with the specified Account.
     * @param account   the specified Account
     * @return          the email associated with the Account
     */
    public String getEmail(Account account){
        if (containsValue(account)){
            String email = "";
            for (Map.Entry<String,Account> entry : entrySet()){
                if (entry.getValue() == account) {
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
     * Attempts to remove the Account associated with an Email.
     * @param email the Account's Email
     */
    public void removeAccount(Email email){
        if (email.getEmail().length() == 0)
            throw new IllegalArgumentException("Invalid Email");
        if (!containsKey(email.getEmail()))
            throw new IllegalArgumentException("Sorry, nobody has this email.");
        remove(email.getEmail());
    }

    /**
     * Attempts to remove the Account associated with an email.
     * @param email the Account's email
     */
    public void removeAccount(String email){
        if (email.length() == 0)
            throw new IllegalArgumentException("Invalid Email");
        if (!containsKey(email))
            throw new IllegalArgumentException("Sorry, nobody has this email.");
        remove(email);
    }

    /**
     * Adds a Follower.
     * @param followed      the Account being followed
     * @param followedUser  the User being followed
     * @param follower      the follower Account
     * @param followerUser  the follower User
     */
    public void addFollower(Account followed, User followedUser, Account follower, User followerUser){
        followed.addFollower(followerUser);
        follower.addFollowing(followedUser);
    }

    /**
     * Removes a Follower.
     * @param followed      the Account that was followed
     * @param followedUser  the User that was followed
     * @param follower      the follower Account
     * @param followerUser  the follower User
     */
    public void removeFollower(Account followed, User followedUser, Account follower, User followerUser){
        followed.removeFollower(followerUser);
        follower.removeFollowing(followedUser);
    }

    /**
     * Stringifies this AccountDatabase.
     * @return String containing details about this AccountDatabase.
     */
    public String toString(){
        String output = "Accounts\n";
        for (String email : keySet()){
            output+= "Email: " + email + ", " + get(email) + "\n";
        }
        return output;
    }


}
