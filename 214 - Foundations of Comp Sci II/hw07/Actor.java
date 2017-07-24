package com.example.kbcalculator.source;

/* ----------------------------- IMPORTS ----------------------------- */

// Java Imports
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/* ----------------------------- CLASS DEF ----------------------------- */


public class Actor implements Serializable {

    /* ----------------------- NESTED CLASS ----------------------- */

    /**
     * Private nested NameComparator.
     */
    private static class NameComparator implements Comparator<Actor> {

        /* ----------------------- METHODS ----------------------- */
        public int compare(Actor a1, Actor a2){
            String leftName = a1.getName().toLowerCase();
            String rightName = a2.getName().toLowerCase();
            // alphabetical order
            return leftName.compareTo(rightName);
        }

    }

    /* ----------------------- INSTANCE VAR ----------------------- */

    // DATA
    private String name;
    private List<Movie> movies;
    private List<Actor> friends;

    // FOR TRAVERSALS
    private boolean visited;
    private LinkedList<String> path;

    // NAME COMPARATOR
    private static final NameComparator nameComparator = new NameComparator();

    /* ----------------------- CONSTRUCTORS ----------------------- */

    /**
     * Default Actor constructor that initializes variables.
     * @param name the name of the Actor
     */
    public Actor(String name){
        this.name = name;
        this.movies = new ArrayList<Movie>();
        this.friends = new ArrayList<Actor>();
        this.visited = false;
        this.path = new LinkedList<String>();
    }


    /* ----------------------- METHODS ----------------------- */

    /**
     * Retrieves the name of the Actor.
     * @return the Actor's name as a String
     */
    public String getName(){ return this.name; }

    /**
     * Retrieves the static NameComparator.
     * @return a NameComparator
     */
    public static NameComparator getNameComparator(){ return nameComparator; }

    /**
     * Retrieves the Movies the Actor has starred in.
     * @return a list of Movies the Actor was in
     */
    public List<Movie> getMovies(){ return this.movies; }

    /**
     * Retrieves the friends of the Actor.
     * @return a list of the Actor's friends
     */
    public List<Actor> getFriends(){ return this.friends; }

    /**
     * Retrieves whether or not this Actor has been visited.
     * @return a boolean of whether this Actor was already traversed
     */
    public boolean getVisited(){ return this.visited; }

    /**
     * Retrieves the path up to this Actor in the traversal.
     * @return a LinkedList detailing the path to this Actor
     */
    public LinkedList<String> getPath(){ return this.path; }

    /**
     * Sets the name of this Actor.
     * @param name the Actor's name
     */
    public void setName(String name){ this.name = name; }

    /**
     * Adds a Movie the Actor has starred in.
     * @param movie a Movie the Actor starred in
     */
    public void addMovie(Movie movie){ this.movies.add(movie); }

    /**
     * Removes a Movie from the list of Movies the Actor has starred in.
     * @param movie a Movie
     */
    public void removeMovie(Movie movie){ this.movies.remove(movie); }

    /**
     * Adds a friend to the Actor's list of friends.
     * @param friend an Actor that has co-starred in a Movie with this Actor
     */
    public void addFriend(Actor friend){ this.friends.add(friend); }

    /**
     * Removes a friend from the Actor's list of friends.
     * @param friend an Actor
     */
    public void removeFriend(Actor friend){ this.friends.remove(friend); }

    /**
     * Modifies the visited boolean of this Actor.
     * @param visited a boolean for whether the Actor has been visited or not
     */
    public void setVisited(boolean visited){ this.visited = visited; }

    /**
     * Adds a node to the path in this Actor.
     * @param node another Actor's name in the path to this Actor.
     */
    public void addPathNode(String node){ this.path.add(node); }

    /**
     * Removes a node from the path to this Actor.
     * @param node an Actor's name
     */
    public void removePathNode(String node){ this.path.remove(node); }

    /**
     * Sets the path of this Actor.
     * @param path the path of this Actor
     */
    public void setPath(LinkedList<String> path){ this.path = path; }

    /**
     * Clears the path of this Actor.
     */
    public void clearPath(){ this.path.clear(); }

    /**
     * Returns whether or not all the Actor's friends have been visited.
     * @return true if all of the Actor's friends have been visited
     */
    public boolean allFriendsVisited(){
        for (Actor friend : friends){
            if (!friend.getVisited())
                return false;
        }
        return true;
    }

    /**
     * Returns the next unvisited friend of this Actor.
     * @return the next unvisited Actor friend
     */
    public Actor getNextUnvisitedFriend(){
        for (Actor friend : friends){
            if (!friend.getVisited())
                return friend;
        }
        return null;
    }

    /**
     * Stringifies the Actor object.
     * @return details about the Actor object as a String
     */
    public String toString(){
        String output = "Name: " + name + "<br>Movies: ";
        Movie[] moviesArray = new Movie[movies.size()];
        moviesArray = movies.toArray(moviesArray);
        for (Movie movie : moviesArray)
            output+= movie.getTitle() + ", ";
        output = output.substring(0,output.length()-2);
        output+= "<br>Friends: ";
        Actor[] friendsArray = new Actor[friends.size()];
        friendsArray = friends.toArray(friendsArray);
        for (Actor friend : friendsArray)
            output+= friend.getName() + ", ";
        output = output.substring(0,output.length()-2);
        return output;
    }

    /**
     * Stringifies the Actor object to be put into a Spinner item.
     * @return Actor name as a String
     */
    public String toSpinnerString(){
        String output = name;
        return output;
    }



}
