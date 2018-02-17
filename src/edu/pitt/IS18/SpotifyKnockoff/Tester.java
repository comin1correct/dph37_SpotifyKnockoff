package edu.pitt.IS18.SpotifyKnockoff;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

/**
 * <h1>Spotify Knockoff</h1>
 * The Spotify Knockoff program implements an application that
 * manages albums, songs, and artist on a database. The future
 * development will implement a playlist feature based on user account.
 * <p>
 * <b>Note:</b> INFSCI_1017 (Spring 2018) - Assignment 2.
 *
 * @author  David Hinton
 * @version 1.0
 * @since   2018-02-02
 */


public class Tester {

    public static void main(String[] args) {
//        //Adding a new Album:
//        Album albumObj = new Album("Hot Dogs Album", "2018-01-28 10:49:14",
//                "Aesop Rock(self)");
//
//        //Adding a new Song:
//        Song songObj = new Song("Hot Dogs", 3.47, "2017-11-01",
//                "2017-09-10");
//
//        //Adding a new Artist:
//        Artist artistObj = new Artist("Ian","Bavitz","Aesop Rock");
//
//        //Calling the Album object and insert into hashtable<songID, songObj>/database.
//        albumObj.addSong(songObj);
//
//        //Checking Hashtable:
//        System.out.println("Contents of albumSongs: "+albumObj.albumSongs);
//
//        //Calling the Song object that insert into hashtable<artistID, artistObj>/database.
//        songObj.addArtist(artistObj);
//
//        //Checking Hashtable:
//        System.out.println("Contents of songArtist: "+songObj.songArtists);
//
//        //Pull items from database
//        Album searchAlbum = new Album(albumObj.getAlbumID());
//        Song searchSong = new Song(songObj.getSongID());
//        Artist searchArtist = new Artist(artistObj.getArtistID());
//
//        System.out.format("\nSearching album, song, artist\n" +
//                "AlbumID: %s \t %s\n" +
//                "SongID: %s \t %s \n" +
//                "Artist: %s \t %s \n"
//                , searchAlbum.getAlbumID(),searchAlbum.getTitle()
//                , searchSong.getSongID(),searchSong.getTitle()
//                , searchArtist.getArtistID(), searchArtist.getFirstName());
//
//        //Destroy objects
//        songObj = null;
//        albumObj = null;
//        artistObj = null;
//
//
//
//        //Creating object from database
//        Album albumHD = new Album("b6286445-81bd-4836-af7e-bf9e50d79a79");
//        Song songHD = new Song("1293e516-aa8e-4ef0-9c13-4146e8c5d0a7");
//        Artist artistHD = new Artist("1a837f4c-88d5-496b-be1c-479d08bbbf26");
//
//        //Updating records
//        songHD.setFilepath("C:\\Users\\David\\Projects\\dph37_SpotifyKnockoff_Git\\src\\data\\hotdog.mp3");
//        artistHD.setBio("Ian Matthias Bavitz, better known by his stage name Aesop Rock, is an American" +
//                " hip hop recording artist and producer residing in Portland, Oregon.");
//
//        //REMOVING DATA:
//        songHD.deleteSong(songHD.getSongID());
//        artistHD.deleteArtist(artistHD.getArtistID());
//        albumHD.deleteAlbum(albumHD.getAlbumID());
//
//        //Destroy objects
//        songHD = null;
//        albumHD = null;
//        artistHD = null;


        Vector<Vector<String>> songTable = new Vector<>();

        try {
            String sql = "SELECT * FROM song;";
            DbUtilities db = new DbUtilities();
            ResultSet rs = db.getResultSet(sql);
            while(rs.next())
            {
                Song s = new Song(rs.getString("song_id"),
                rs.getString("title"),
                rs.getDouble("length"),
                rs.getString("file_path"),
                rs.getString("release_date"),
                rs.getString("record_date"));

                songTable.add(s.getSongRecord());
            }
            db.closeDbConnection();
            db = null;
        }catch (SQLException e){
            e.printStackTrace();
        }
        int count=1;
        for(int i = 0; i < songTable.size(); i++)
        {
            System.out.print("Line "+ count++ +":");

            for(int j = 0; j < songTable.get(i).size();j++)
            {
                System.out.print(songTable.get(i).get(j) +"\t\t\t");
            }
            System.out.println();
        }

    }//MAIN
}//CLASS