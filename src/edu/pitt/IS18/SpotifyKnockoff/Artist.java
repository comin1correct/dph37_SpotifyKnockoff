package edu.pitt.IS18.SpotifyKnockoff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class: Artist
 * @author David Hinton
 * @version 1.0
 * @since 2018-01-29
 */

@Entity
@Table(name = "artist")
public class Artist {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "artist_id")
    private String artistID;
	@Column(name = "first_name")
    private String firstName;
	@Column(name = "last_name")
    private String lastName;
	@Column(name = "band_name")
    private String bandName;
	@Column(name = "bio")
    private String bio;

    /**
     * default constructor for JPA
     */
    public Artist() {
    		super();
    }
    /**
     * Creates a Artist object and insert new record to the database
     * @param firstName - name of artist member
     * @param lastName - last name of artist member
     * @param bandName - name of band name
     */
    public Artist(String firstName, String lastName, String bandName)
    {
    		super(); //<-- EntityManager --> EntityManagerFactor ?
    	
        //toString() required, else it will return an object not string;
        this.artistID = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.bandName = bandName;


        /*//BEGIN INSERT
        String sql = "INSERT INTO `artist`(`artist_id`,`first_name`,`last_name`,`band_name`,`bio`)" +
                     "VALUES (?,?,?,?,?);";
        try{
            DbUtilities db = new DbUtilities();
            Connection conn = db.getConn();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, this.artistID);
            ps.setString(2,this.firstName);
            ps.setString(3,this.lastName);
            ps.setString(4,this.bandName);
            ps.setString(5, "Default Text");

            //execute - close - destroy
            ps.executeUpdate();
            ps.close();
            db.closeDbConnection();
            db = null;
        }
        catch (SQLException e){
            e.printStackTrace();
            ErrorLogger.log(e.getMessage());
        }*/

    }//END of Artist Constructor

    /**
     * Retrieves an existing record from the database using <code>artistID</code> as the key
     * Creates a new artist object
     * Sets corresponding class properties
     * @param artistID -UUID set on instantiation
     */
    public Artist(String artistID)
    {
    		super(); //<-- EntityManager --> EntityManagerFactor ?
    		
        String sql = "SELECT * FROM artist WHERE artist_id = ?;";
        //create connection to database
        DbUtilities db = new DbUtilities();
        try {
            Connection conn = db.getConn();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,artistID);

            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                this.artistID = rs.getString("artist_id");
                this.firstName = rs.getString("first_name");
                this.lastName = rs.getString("last_name");
                this.bandName = rs.getString("band_name");
                this.bio = rs.getString("bio");

            }
                //Close connection
                pstmt.close();
                rs.close();
                db.closeDbConnection();
                db = null;

        } catch (SQLException e) {
            e.printStackTrace();
            ErrorLogger.log(e.getMessage());
        }
    }//END Artist Constructor

    /**
     * Used in the case that we are querying all records from the database
     * with a "SELECT * FORM artist;". We have all information available to use
     * to create the song object. Otherwise we would had had to use the Artist
     * constructor above, costing an additional connection to the database for
     * each record.
     * @param artistID
     * @param firstName
     * @param lastName
     * @param bandName
     * @param bio
     */
    public Artist(String artistID,String firstName, String lastName, String bandName, String bio) {
    	
    		super(); //<-- EntityManager --> EntityManagerFactor ?
    		
        //toString() required, else it will return an object not string;
        this.artistID = artistID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bandName = bandName;
        this.bio = bio;
    }

    /**
     * Deletes a song from the database using <code>artistID</code> as the key
     * Note that when this method is called, the corresponding object is destroyed
     * @param artistID -UUID set on instantiation
     */
    public void deleteArtist(String artistID)
    {
        //Calling the stored procedure
        String sql = "CALL DeleteArtist(?)";

        try{
            DbUtilities db = new DbUtilities();
            Connection conn  = db.getConn();

            //creating the Preparedstatment object
            PreparedStatement pstmt = conn.prepareStatement(sql);
            //Inserting the String value into the appropriate index.
            pstmt.setString(1, artistID);

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

    }//END deleteArtist

    public String getArtistID() {
        return artistID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setBio(String bio) {
        this.bio = bio;

//        //we also need to update the database table
//        String sql = "UPDATE artist SET bio = ? WHERE artist_id = ?;";
//        try {
//            DbUtilities db = new DbUtilities();
//            PreparedStatement pstmt = db.getConn().prepareStatement(sql);
//            pstmt.setString(1,bio);
//            pstmt.setString(2, this.artistID);
//
//            //execute-close-destroy
//            pstmt.executeUpdate();
//            pstmt.close();
//            db.closeDbConnection();
//            db = null;
//        }catch (SQLException e){
//            e.printStackTrace();
//            ErrorLogger.log(e.getMessage());
//        }
    }//END setBio
}//END MAIN
