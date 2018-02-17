package edu.pitt.IS18.SpotifyKnockoff;

public class JPA_Tester {

	
	public static void main(String[] args) {
		
		JpaUtilities JPA = new JpaUtilities();
		
		Song song = new Song("Assignment2PartIII", 99, "2014-02-14", "2000-02-14");
		Album album = new Album("Assignment2 Album", "2014-02-12","pitt.edu");
		Artist artist = new Artist("David","Hinton","N/A");
		
		JPA.songCreate(song);
		System.out.println(song+"\n");
		JPA.songUpdate(song);
		System.out.println(song+"\n");
		JPA.songDelete(song);
		
		JPA.albumCreate(album);
		JPA.albumUpdate(album);
		JPA.albumDelete(album);
		
		JPA.artistCreate(artist);
		JPA.artistUpdate(artist);
		JPA.artistDelete(artist);

		JPA.closeJPA();
	
	}


}
