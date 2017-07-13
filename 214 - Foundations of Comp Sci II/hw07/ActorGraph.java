package com.example.kbcalculator.source;

/**
 * Johnny So
 * 111158276
 * Homework #6
 * CSE 214 Recitation 12
 * Recitation TA: Charles Chen
 * Grading TA: Tim Zhang
 * @author Johnny
 */


/* ----------------------------- IMPORTS ----------------------------- */

// Java Imports
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;



/* ----------------------------- CLASS DEF ----------------------------- */


public class ActorGraph implements Serializable {

    /* ----------------------- STATIC VAR ----------------------- */

    // DATA
    private static HashMap<String,Actor> actorsByName;
    private static HashMap<String,Movie> moviesByTitle;


    /* ----------------------- CONSTRUCTORS ----------------------- */

    /**
     * Default ActorGraph constructor that initializes variables.
     */
    public ActorGraph(){
        if (actorsByName == null)
            actorsByName = new HashMap<String,Actor>();
        if (moviesByTitle == null)
            moviesByTitle = new HashMap<String,Movie>();
    }

    /* ----------------------- METHODS ----------------------- */

    /**
     * Retrieves the Actor with the specified name.
     * @param actorName the Actor's name
     * @return          the Actor
     */
    public Actor getActor(String actorName){
        if (actorsByName.containsKey(actorName))
            return actorsByName.get(actorName);
        return null;
    }

    /**
     * Retrieves the Movie with the specified title.
     * @param movieTitle    the Movie's title
     * @return              the Movie
     */
    public Movie getMovie(String movieTitle){
        if (moviesByTitle.containsKey(movieTitle))
            return moviesByTitle.get(movieTitle);
        return null;
    }

    /**
     * Retrieves all Actors imported.
     * @return all Actors
     */
    public List<Actor> getAllActors(){
        List<Actor> list = new ArrayList<Actor>(actorsByName.values());
        Collections.sort(list, Actor.getNameComparator());
        return list;
    }

    /**
     * Retrieves all Movies imported.
     * @return all Movies
     */
    public List<Movie> getAllMovies(){
        List<Movie> list = new ArrayList<Movie>(moviesByTitle.values());
        Collections.sort(list, Movie.getTitleComparator());
        return list;
    }

    /**
     * Attempts to add an Actor to the ActorGraph.
     * @param actor the desired Actor to add
     * @return true upon success
     * @throws IllegalArgumentException if the Actor is already in the ActorGraph
     */
    public boolean addActor(Actor actor) throws IllegalArgumentException {
        if (actorsByName.containsKey(actor.getName()))
            throw new IllegalArgumentException("Sorry, but the specified Actor is already added.");
        actorsByName.put(actor.getName(),actor);
        return true;
    }

    /**
     * Attempts to add a Movie to the ActorGraph.
     * @param movie the desired Movie to add
     * @return true upon success
     * @throws IllegalArgumentException if the Movie is already in the ActorGraph
     */
    public boolean addMovie(Movie movie) throws IllegalArgumentException {
        if (moviesByTitle.containsKey(movie.getTitle()))
            throw new IllegalArgumentException("Sorry, but the specified Movie is already added.");
        moviesByTitle.put(movie.getTitle(),movie);
        List<Actor> actors = new ArrayList<Actor>();
        for (String actorName : movie.getActorsNames()){
            Actor actor;
            if (!actorsByName.containsKey(actorName)) {
                actor = new Actor(actorName);
                addActor(actor);
            }
            else {
                actor = actorsByName.get(actorName);
            }
            actors.add(actor);
        }
        movie.setActors(actors);
        movie.setRelationships();
        return true;
    }

    /**
     * Refreshes the Actors in preparation for a new BFS.
     */
    public static void refreshForBFS(){
        for (Object value : actorsByName.values()){
            Actor actor = ((Actor) value);
            actor.setVisited(false);
            actor.clearPath();
        }
    }

    /**
     * Returns the breadth-first traversal starting with the Actor whose name is passed. This method
     * also sets the path variable of the Actors passed in the breadth-first traversal with the
     * current path up to that Actor.
     * @param actorName the name of the Actor the bfs is starting from
     * @return the breadth-first traversal of the Actors
     */
    public static LinkedList<String> bfs(String actorName){
        // prepare for traversal
        refreshForBFS();
        Queue<Actor> queue = new LinkedList<Actor>();
        LinkedList<String> bfs = new LinkedList<String>();

        // add the root
        Actor rootActor = ((Actor)actorsByName.get(actorName));
        rootActor.setVisited(true);
        rootActor.addPathNode(actorName);
        queue.add(rootActor);
        bfs.add(actorName);

        // perform the traversal
        while (!queue.isEmpty()){
            Actor actor = queue.remove();
            Actor friend;
            while (!actor.allFriendsVisited()) {
                friend = actor.getNextUnvisitedFriend();
                LinkedList<String> friendPath = (LinkedList<String>)actor.getPath().clone();
                friendPath.add(friend.getName());
                friend.setPath(friendPath);
                bfs.add(friend.getName());
                queue.add(friend);
                friend.setVisited(true);
            }
        }

        return bfs;
    }

}
