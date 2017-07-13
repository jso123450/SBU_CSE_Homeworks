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
import android.content.Context;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;


/* ----------------------------- CLASS DEF ----------------------------- */


public class Bitter implements Serializable {

    /* ----------------------- INSTANCE VAR ----------------------- */

    // LOGIC VARS
    public static final int EMAIL_NOT_FOUND = 1;
    public static final int INVALID_PASSWORD = 2;
    public static final int VALID_LOGIN = 3;
    public static final String BITTER_FILE = "bitter.ser";

    // DATA VARS
    private UserDatabase users;
    private AccountDatabase accounts;

    /* ----------------------- CONSTRUCTORS ----------------------- */

    /**
     * Default Bitter constructor.
     */
    public Bitter(){
        this.users = new UserDatabase();
        this.accounts = new AccountDatabase();
    }


    /* ----------------------- METHODS ----------------------- */

    /**
     * Attempts to add the respective User and Account associated with the Email.
     * @param  email                    associated Email
     * @param  user                     associated User
     * @param  account                  associated Account
     * @throws IllegalArgumentException if any invalid params
     */
    public void addUser(Email email, User user, Account account) throws IllegalArgumentException {
        users.addUser(email,user);
        accounts.addAccount(email,account);
    }

    /**
     * Attempts to remove the User from the databases.
     * @param email the associated Email
     */
    public void removeUser(Email email){
        // loop through Accounts to get rid of User objs in the following and followers list
        User toBeRemoved = users.getUser(email);
        for (Map.Entry<String,Account> entry : accounts.entrySet()){
            Account entryAcc = entry.getValue();
            entryAcc.removeFollower(toBeRemoved);
            entryAcc.removeFollowing(toBeRemoved);
        }

        // remove them from the databases
        users.removeUser(email);
        accounts.removeAccount(email);
    }

    /**
     * Attempts to remove the User from the databases.
     * @param email the associated email
     */
    public void removeUser(String email){
        // loop through Accounts to get rid of User objs in the following and followers list
        User toBeRemoved = users.getUser(email);
        for (Map.Entry<String,Account> entry : accounts.entrySet()){
            Account entryAcc = entry.getValue();
            entryAcc.removeFollower(toBeRemoved);
            entryAcc.removeFollowing(toBeRemoved);
        }

        // remove them from the databases
        users.removeUser(email);
        accounts.removeAccount(email);
    }

    /**
     * Clears the databases.
     */
    public void clear(){
        users.clear();
        accounts.clear();
    }

    /**
     * Retrieves the associated Account from the AccountDatabase.
     * @param email the associated email of the account
     * @return      the associated Account
     */
    public Account getAccount(String email){
        return accounts.getAccount(email);
    }

    /**
     * Retrieves the associated User from the UserDatabase.
     * @param email the associated email of the User
     * @return      the associated User
     */
    public User getUser(String email) { return users.getUser(email); }

    /**
     * Retrieves an array of all Users in the UserDatabase.
     * @return  all Users in the database
     */
    public User[] getAllUsers(){
        ArrayList<User> usersArrayList = new ArrayList<User>();
        for (Map.Entry<String, User> entry : users.entrySet()){
            usersArrayList.add(entry.getValue());
        }
        User[] usersArray = new User[usersArrayList.size()];
        usersArrayList.toArray(usersArray);
        return usersArray;
    }

    /**
     * Retrieve the email associated with an account.
     * @param account   the specified account
     * @return          the email associated
     */
    public String getAccountEmail(Account account){
        return accounts.getEmail(account);
    }

    /**
     * Retrieve the email associated with a user.
     * @param user      the specified user
     * @return          the associated email
     */
    public String getUserEmail(User user){
        return users.getEmail(user);
    }

    /**
     * Adds a Follower.
     * @param follower  the follower Account
     * @param followed  the followed User
     */
    public void addFollowing(Account follower, User followed){
        String followerEmail = getAccountEmail(follower);
        User followerUser = users.getUser(followerEmail);
        String followedEmail = getUserEmail(followed);
        Account followedAccount = accounts.getAccount(followedEmail);
        accounts.addFollower(followedAccount,followed,follower,followerUser);
    }

    /**
     * Removes a Follower.
     * @param follower  the follower Account
     * @param followed  the followed User
     */
    public void removeFollowing(Account follower, User followed){
        String followerEmail = getAccountEmail(follower);
        User followerUser = users.getUser(followerEmail);
        String followedEmail = getUserEmail(followed);
        Account followedAccount = accounts.getAccount(followedEmail);
        accounts.removeFollower(followedAccount,followed,follower,followerUser);
    }

    /**
     * Attempts to login with the specified email and password.
     * @param email    the email of the Account
     * @param password the password of the Account
     * @return         respective flag
     */
    public int login(String email, String password){
        Account account = accounts.getAccount(email);
        if (account == null)
            return EMAIL_NOT_FOUND;
        if (!account.getPassword().getPassword().equals(password))
            return INVALID_PASSWORD;
        return VALID_LOGIN;
    }

    /**
     * Attempts to save this instance of Bitter.
     * @return              true if successful
     */
    public boolean save(Context context){
        try {
            FileOutputStream fileOut = context.openFileOutput(BITTER_FILE, Context.MODE_PRIVATE);
            ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
            outStream.writeObject(this);
            outStream.close();
            System.out.println("saved");
            return true;
        } catch(IOException ioe){
            System.out.println(ioe.getMessage());
            return false;
        }
    }

    /**
     * Stringifies the UserDatabase and AccountDatabase.
     * @return String
     */
    public String toString(){
        String output = "";
        output+= users + "\n" + accounts;
        return output;
    }

}
