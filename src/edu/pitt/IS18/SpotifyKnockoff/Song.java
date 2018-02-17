package edu.pitt.IS18.SpotifyKnockoff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Class: Song
 * @author David Hinton
 * @version 1.0
 * @since 2018-01-29
 */

@Entity 
@Table (name = "song")
public class Song {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column (name = "song_id")
    private String songID;
	@Column (name = "title")
    private String title;
	@Column (name = "length")
    private double length;
	@Column (name = "file_path")
    private String filepath;
	@Column (name = "release_date")
    private String releaseDate;
	@Column (name = "record_date")
    private String recordDate;
	@Transient
    Map <String , Artist> songArtists; //junction tables

	
	
	/**
	 * Assignment2 Part III 
	 * JPA implementation and adding super() to all constructors
	 */
	public Song() 
	{
		super();
	}
	
    /**
     * Creates a Song object and insert new record to the database
     * @param title - title of song
     * @param length - length of song
     * @param releaseDate - sql timedate format
     * @param recordDate - sql timedate format
     */
    public Song(String title, double length, String releaseDate, String recordDate) {
    		super();
        this.title = title;
        this.length = length;
        this.releaseDate = releaseDate;
        this.recordDate = recordDate;
        this.songID = UUID.randomUUID().toString();
        this.songArtists = new Hashtable<String, Artist>();

        /**
         * Question:  When running the JPA_Tester takes care of prepared statement and insert.
         * 			I'm wondering if the hashtable should be populated by the JPA rather than
         * 			using our DbUtilities class?
         *
         */

        try {
            DbUtilities db = new DbUtilities();
 
            /*  ~~~~~~~~~~~~~~~~~~~~~ JPA Utilities replaces this code ~~~~~~~~~~~~~~~~~~~~~
             * 
             *String sql = "INSERT INTO song (song_id, title, length, file_path, release_date, record_date) ";
             *sql += "VALUES (?,?,?,?,?,?);";
             * 
             *Connection conn = db.getConn();
             *PreparedStatement ps = conn.prepareStatement(sql);
             *ps.setString(1,this.songID);
             *ps.setString(2,this.title);
             *ps.setDouble(3,this.length);
             *ps.setString(4,"");
             *ps.setString(5,this.releaseDate);
             *ps.setString(6,this.recordDate);
             *
             *db.executeQuery(sql);  ---> this needs to be replaced with the ps object
             *ps.executeUpdate(); 
             *  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
             */

            songArtists = new Hashtable<String, Artist>();
            String sqlUpdate = "SELECT * FROM song;";

            ResultSet rs = db.getResultSet(sqlUpdate);
            while (rs.next()) {
                Artist artist = new Artist(rs.getString("song_id"));

              
                songArtists.put(getSongID(),artist);
            }
            rs.close();
            db.closeDbConnection();
            db = null;

        }catch (SQLException e) {
            e.printStackTrace();
            ErrorLogger.log(e.getMessage());
        }

    }//END SONG(new)

    /**
     * Retrieves an existing record from the database using songID as the key
     * Creates a new song object
     * Sets corresponding class properties
     * @param songID - UUID set on instantiation
     */
    public Song(String songID)
    {
    		super();
        this.songArtists = new Hashtable<String, Artist>();
        //toString(), because it creates an object rather than a string
        String sql = "SELECT * FROM song WHERE song_id = ?;";
        DbUtilities db = new DbUtilities();
        try {
            Connection conn = db.getConn();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, songID);

            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                this.songID = rs.getString("song_id");
                this.title = rs.getString("title");
                this.length = rs.getDouble("length");
                this.filepath = rs.getString("file_path");
                this.releaseDate = rs.getDate("release_date").toString();
                this.recordDate = rs.getDate("record_date").toString();

            }

            //Closing database connection
            pstmt.close();
            rs.close();
            db.closeDbConnection();
            db = null;

        } catch (SQLException e) {
            e.printStackTrace();
            ErrorLogger.log(e.getMessage());
        }
    }//END SONG CONSTRUCTOR

    /**
     * Used in the case that we are querying all records from the database
     * with a "SELECT * FORM song;". We have all information available to use
     * to create the song object. Otherwise we would had had to use the Song
     * constructor above, costing an additional connection to the database for
     * each record.
     * @param songID
     * @param title
     * @param length
     * @param filepath
     * @param releaseDate
     * @param recordDate
     */
    public Song(String songID,String title, double length, String filepath, String releaseDate, String recordDate) {
    		super();
        this.songID = songID;
        this.title = title;
        this.length = length;
        this.filepath = filepath;
        this.releaseDate = releaseDate;
        this.recordDate = recordDate;
        this.songArtists = new Hashtable<String, Artist>();
    }


    /**
     * Deletes a song from the database using songID as the key
     * Note that when this method is called, the corresponding object is destroyed
     * @param songID - UUID set on instantiation
     */
    public void deleteSong(String songID)
    {
        //Hiding from users view with a flag
        //Calling the stored procedure
        String sql = "CALL DeleteSong(?)";

        //Creating a connection to the database
        try {
            DbUtilities db = new DbUtilities();
            Connection conn = db.getConn();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, songID);

            //DELETE
            pstmt.executeUpdate();

            //Close Connection
            pstmt.close();
            db.closeDbConnection();
            db = null;

        }catch (SQLException e){
            e.printStackTrace();
            ErrorLogger.log(e.getMessage());
        }
    }// END deleteSong

    /**
     * 1. Adds new artist object to the list of the song’s artists
     * 2. Insert songID and albumID into song_artist junction table
     * @param artist - object of the <code>Artist</code> class
     */
    public void addArtist(Artist artist)
    {
        songArtists.put(artist.getArtistID(),artist);
        String sql = "INSERT INTO song_artist (fk_song_id, fk_artist_id) VALUES (?,?);";
        try {
            DbUtilities db = new DbUtilities();
            PreparedStatement pstmt = db.getConn().prepareStatement(sql);
            pstmt.setString(1, this.songID);
            pstmt.setString(2, artist.getArtistID());

            //execute-close-destroy.
            pstmt.executeUpdate();
            pstmt.close();
            db.closeDbConnection();
            db = null;
        }catch (SQLException e){
            e.printStackTrace();
            ErrorLogger.log(e.getMessage());
        }
    }//END addArtist

    /**
     * Deletes an artist from the list of the <code> songArtists </code> by artistID
     * Note: will not delete from database.
     * @param artistID - UUID set on instantiation
     */
    public void deleteArtist(String artistID)
    {
        System.out.println("HashTable of Song before Deleted: "+ songArtists);
        /*
         *   Deletes an artist from the list of the songArtists by artistID
         *   accessing hashtable:
         *
         *  pseudo code:
         *
         *  the hashtable contains Key: songID Value: Artist Object
         *
         *  Looping through each Value:
         *      if(artistObject.getArtistID() == artistID){
         *          give me the corresponding key:
         *              songArtists.remove(Key);
         *            }
         */

        //Source: https://www.tutorialspoint.com/java/java_mapentry_interface.htm

        //Get a set of the entries
        Set set = songArtists.entrySet();

        //Get iterator
        Iterator iterator = set.iterator();

        //Loop through elements
        while(iterator.hasNext()){

            Map.Entry element = (Map.Entry)iterator.next();

            //casting to a Artist object
            Artist tempVar = (Artist)element.getValue();
            String tempKey = (String)element.getKey();


            if(tempVar.getArtistID() == artistID){
                System.out.format("Removed Artist: %s (Artist Key: %s) From Song HashTable\n" +
                        "The songID associated this artist is %s.",tempVar.getFirstName(),
                        tempVar.getArtistID(),tempKey);
                songArtists.remove(element.getKey());
            }
        }

    }//END deleteArtist

    /**
     * Deletes an artist from the list of the <code> songArtists </code> by
     * artistID property of the Artist object
     * Note: will not delete from database.
     * @param artist - object of the <code>Artist</code> class
     */
    public void deleteArtist(Artist artist)
    {
        songArtists.remove(artist.getArtistID());
    }

    //GETTERS & SETTERS
    public String getSongID() {
        return songID;
    }

    public void setFilepath(String filepath){
        this.filepath = filepath;
        //we also need to update the database table
        String sql = "UPDATE song SET file_path = ? WHERE song_id = ?;";
        try {
            DbUtilities db = new DbUtilities();
            PreparedStatement pstmt = db.getConn().prepareStatement(sql);
            pstmt.setString(1,filepath);
            pstmt.setString(2, this.songID);

            //execute-close-destroy
            pstmt.executeUpdate();
            pstmt.close();
            db.closeDbConnection();
            db = null;
        }catch (SQLException e){
            e.printStackTrace();
            ErrorLogger.log(e.getMessage());
        }

    }

    public String getTitle() {
        return title;
    }

    /**
     *
     * @return
     */
    Vector<String> getSongRecord(){
        Vector<String> songRecord = new Vector<>();

        songRecord.add(this.songID);
        songRecord.add(this.title);
        songRecord.add(String.valueOf(this.length));
        songRecord.add(this.filepath);
        songRecord.add(this.releaseDate);
        songRecord.add(this.recordDate);

        return songRecord;
    }
    
    @Override
    public String toString(){
    		String message = String.format("\n\tSong_id: %s \n\tTitle: %s \n\tLength: %f \n\tFile Path: %s \n\t"
    				+ "Release Date: %s \n\tRecord Date: %s.",songID,title,length, filepath,releaseDate, recordDate);
    		return (message);
    }

}//END CLASS



