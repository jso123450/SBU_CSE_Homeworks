package com.example.kbcalculator.source;


/* ----------------------------- IMPORTS ----------------------------- */

// Java Import
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// External Libraries
import big.data.DataInstantiationException;
import big.data.DataSource;


/* ----------------------------- CLASS DEF ----------------------------- */


public class Movie implements Serializable {

    /* ----------------------- NESTED CLASS ----------------------- */

    /**
     * Private TitleComparator.
     */
    private static class TitleComparator implements Comparator<Movie> {

        /* ----------------------- METHODS ----------------------- */
        public int compare(Movie m1, Movie m2){
            String leftName = m1.getTitle().toLowerCase();
            String rightName = m2.getTitle().toLowerCase();
            // alphabetical order
            return leftName.compareTo(rightName);
        }

    }

    /* ----------------------- INSTANCE VAR ----------------------- */

    // DATA
    private String title;
    private List<Actor> actors;
    private String[] actorsNames;
    private int year;

    // COMPARATOR
    private static final TitleComparator titleComparator = new TitleComparator();


    /* ----------------------- CONSTRUCTORS ----------------------- */

    /**
     * Default Movie constructor that queries the OMDB API for the Movie details.
     * @param title the title of the Movie
     */
    public Movie(String title) throws DataInstantiationException{
        // generate the URL to get the Movie details from OMDB API
        String prefix = "http://www.omdbapi.com/?t=";
        String postfix ="&y=&plot=short&r=xml";

        // get the data
        DataSource ds = DataSource.connectXML(prefix+title.replace(' ','+')+postfix);
        ds.load();

        // set member variables accordingly
        this.title = ds.fetchString("movie/title");
        this.year = ds.fetchInt("movie/year");
        String actorString = ds.fetchString("movie/actors");
        actorsNames = actorString.split(", ");
        actors = new ArrayList<Actor>();

    }




    /* ----------------------- METHODS ----------------------- */

    /**
     * Retrieves the title of this Movie.
     * @return the Movie title
     */
    public String getTitle(){ return this.title; }

    /**
     * Retrieves the TitleComparator.
     * @return the TitleComparator
     */
    public static TitleComparator getTitleComparator(){ return titleComparator; }

    /**
     * Retrieves the List of Actors in this Movie.
     * @return the Actors in this Movie
     */
    public List<Actor> getActors(){ return this.actors; }

    /**
     * Retrieves the names of the Actors in this Movie.
     * @return the names of the Actors in this Movie
     */
    public String[] getActorsNames(){ return this.actorsNames; }

    /**
     * Retrieves the year this Movie was released.
     * @return the release year of this Movie
     */
    public int getYear(){ return this.year; }

    /**
     * Sets the Actors in this Movie.
     * @param actors the Actors in this Movie
     */
    public void setActors(List<Actor> actors){ this.actors = actors; }

    /**
     * Sets the relationships between the Actors and this Movie.
     */
    public void setRelationships(){
        // handle actor and movie relationships
        for (Actor actor : actors){
            actor.addMovie(this);
            for (Actor actor2 : actors){
                if (actor2 != actor && (!actor.getFriends().contains(actor2)))
                    actor.addFriend(actor2);
            }
        }
    }

    /**
     * Stringifies the Movie object.
     * @return details about the Movie as a String
     */
    public String toString(){
        String output = "Movie: " + title + "<br>Actors: ";
        Actor[] actorsArray = new Actor[actors.size()];
        actorsArray = actors.toArray(actorsArray);
        for (Actor actor : actorsArray){
            output+= actor.getName() + ", ";
        }
        output = output.substring(0,output.length()-2);
        output+= "<br>";
        output+= "Year: " + year;
        return output;
    }

}
