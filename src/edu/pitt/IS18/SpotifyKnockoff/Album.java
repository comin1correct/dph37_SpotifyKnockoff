package edu.pitt.IS18.SpotifyKnockoff;

import java.sql.*;
import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Class: Album
 * @author David Hinton
 * @version 1.0
 * @since 2018-01-29
 */


@Entity
@Table (name = "album")
public class Album {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) 

	@Column(name = "album_id")
    private String albumID;
	@Column(name = "title")
    private String title;
	@Column(name = "release_date")
    private String releaseDate;
	@Column(name = "cover_image_path")
    private String coverImagePath;
	@Column(name = "recording_company_name")
    private String recordingCompany;
	@Column(name = "number_of_tracks")
    private int numberOfTracks;
	@Column(name = "PMRC_rating")
    private String pmrcRating;
	@Column(name = "length")
    private double length;
	@Transient
    Map <String, Song> albumSongs;
    
    
    /**
     * default/zero args constructor of JPA
     */
    public Album()
    {
    		super();
    }

    /**
     * Creates a Album object and insert new record to the database
     * @param title - title of album
     * @param releaseDate - sql timedate format
     * @param recordingCompany - name of recording company
     */
    public Album(String title, String releaseDate, String recordingCompany)
    {
    	    super(); //<-- EntityManager --> EntityManagerFactor ?
        this.albumID = UUID.randomUUID().toString();

        this.title = title;
        this.releaseDate = releaseDate;
        this.recordingCompany = recordingCompany;
        this.numberOfTracks = numberOfTracks;
        this.pmrcRating = pmrcRating;
        this.length = length;
        this.albumSongs  = new Hashtable<String, Song>();

        /*        //BEGIN INSERT
       

        //https://docs.microsoft.com/en-us/sql/connect/jdbc/using-basic-data-types
        //https://docs.oracle.com/javase/8/docs/api/java/sql/Timestamp.html#valueOf-java.lang.String-
        //to resolve data types between SQL and JDBC e.g. TIMEDATE.

        try{
            DbUtilities db = new DbUtilities();
            Connection conn = db.getConn();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, this.albumID);
            ps.setString(2,this.title);
            //            ps.setString(3,this.releaseDate);
            ps.setString(3,"C://Project/...");
            ps.setString(4, this.recordingCompany);
            ps.setInt(5,10);
            ps.setString(6,"A");
            ps.setDouble(7,6000);

            //execute - close - destroy
            ps.executeUpdate();
            ps.close();
            db.closeDbConnection();
            db = null;
        }
        catch (SQLException e){
            e.printStackTrace();
            ErrorLogger.log(e.getMessage());
        }
        
        */

    }//END album - adding

    /**
     * Retrieves an existing record from the database using <code>albumID</code> as the key
     * Creates a new album object
     * Sets corresponding class properties
     * @param albumID -UUID set on instantiation
     */
    public Album(String albumID)
    {
	    super(); //<-- EntityManager --> EntityManagerFactor ?
	    
        this.albumSongs = new Hashtable<String, Song>();
        //reference: https://www.ibm.com/support/knowledgecenter/en/SSEPGG_9.5.0/com.ibm.db2.luw.apdv
        // .java.doc/src/tpc/imjcc_tjvpsxqu.html

        String sql = "SELECT * FROM album WHERE album_id = ?;";

        //Connection to database
        DbUtilities db = new DbUtilities();
        try {
            Connection conn = db.getConn();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, albumID);

            ResultSet rs = pstmt.executeQuery();
            while(rs.next())
            {
                this.albumID = rs.getString("album_id");
                this.title = rs.getString("title");
                this.releaseDate = rs.getString("release_date");
                this.recordingCompany = rs.getString("recording_company_name");
                this.numberOfTracks = rs.getInt("number_of_tracks");
                this.length = rs.getDouble("length");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorLogger.log(e.getMessage());
        }

    }//END Album - pull

    /**
     * Used in the case that we are querying all records from the database
     * with a "SELECT * FORM album;". We have all information available to use
     * to create the song object. Otherwise we would had had to use the Album
     * constructor above, costing an additional connection to the database for
     * each record.
     * @param albumID
     * @param title
     * @param releaseDate
     * @param recordingCompany
     * @param numberOfTracks
     * @param pmrcRating
     * @param length
     */
    public Album(String albumID, String title , String releaseDate, String recordingCompany,
                 int numberOfTracks, String pmrcRating, double length) {
    	
	    super(); //<-- EntityManager --> EntityManagerFactor ?
        this.albumID = albumID;
        this.title = title;
        this.releaseDate = releaseDate;
        this.recordingCompany = recordingCompany;
        this.numberOfTracks = numberOfTracks;
        this.pmrcRating = pmrcRating;
        this.length = length;
        this.albumSongs = new Hashtable<String, Song>();

    }


        /**
         * Deletes a album from the database using <code>albumID</code> as the key
         * Note that when this method is called, the corresponding object is destroyed
         * @param albumID - UUID set on instantiation
         */
    public void deleteAlbum(String albumID)
    {
        //Creating the DELETE statement
        String sql = "DELETE FROM album " +
                "WHERE album_id = ?;";

        //Creating a connection to the database
        try {
            DbUtilities db = new DbUtilities();
            Connection conn = db.getConn();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,albumID);

            //DELETE - Close Connection
            pstmt.executeUpdate();
            pstmt.close();
            db.closeDbConnection();
            db = null;

        }catch (SQLException e){
            e.printStackTrace();
            ErrorLogger.log(e.getMessage());
        }

    }// END deleteSong

    /**
     * 1. Adds new Song object to the list of the album’s songs
     * 2. Insert albumID and songID into album_song junction table
     * @param song - object of the <code>Song</code> class
     */
    public void addSong(Song song)
    {
        albumSongs.put(song.getSongID(),song);
        String sql = "INSERT INTO album_song (fk_album_id, fk_song_id) VALUES(?,?);";

        try{
            DbUtilities db = new DbUtilities();
            PreparedStatement pstmt = db.getConn().prepareStatement(sql);
            pstmt.setString(1, this.albumID);
            pstmt.setString(2, song.getSongID());

            //execute-close-destroy
            pstmt.executeUpdate();
            pstmt.close();
            db.closeDbConnection();
            db = null;

        }catch(SQLException e){
            e.printStackTrace();
            ErrorLogger.log(e.getMessage());
        }


    }//END addArtist

    /**
     * Deletes a song from the list of the <code>albumSongs</code> songs by songID
     * Note: does not delete song from database
     * @param songID - UUID set on instantiation
     */
    public void deleteSong(String songID)
    {
        //Source: https://www.tutorialspoint.com/java/java_mapentry_interface.htm

        System.out.println("HashTable of Albums before Deleted: "+ albumSongs);
        //Get a set of the entries
        Set set = albumSongs.entrySet();

        //Get iterator
        Iterator iterator = set.iterator();

        //Loop through elements
        while(iterator.hasNext()){

            Map.Entry element = (Map.Entry)iterator.next();

            //casting to a Artist object
            Song tempVar = (Song) element.getValue();
            String tempKey = (String)element.getKey();


            if(tempVar.getSongID() == songID){
                System.out.format("Removed Song: %s (Song Key: %s) from Album HashTable\n" +
                                "The albumID associated this song is %s.",tempVar.getTitle()
                        ,tempVar.getSongID(),tempKey);
                albumSongs.remove(element.getKey());
            }
        }

    }//END deleteArtist

    /**
     * Deletes a song from the list of the <code>albumSongs</code> songs by
     * songID property of the corresponding Song object
     * @param song - object of the <code>Song</code> class
     */
    public void deleteSong(Song song)
    {
        albumSongs.remove(song.getSongID());
    }

    public String getAlbumID() {
        return albumID;
    }

    public String getTitle() {
        return title;
    }
    
    public void SetcoverImagePath(String path) 
    {
    		coverImagePath = path;
    }
    
    

}//END CLASS
