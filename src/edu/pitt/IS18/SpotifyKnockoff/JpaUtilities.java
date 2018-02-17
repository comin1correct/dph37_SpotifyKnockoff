package edu.pitt.IS18.SpotifyKnockoff;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaUtilities {
	
	private EntityManagerFactory emFactory;
	private EntityManager eManager;
	
	
	public JpaUtilities() {
		emFactory = Persistence.createEntityManagerFactory("dph37_SpotifyKnockoff");
		eManager = emFactory.createEntityManager();
		}
	
	public void closeJPA(){
		eManager.close();
		emFactory.close();
	}
	
	//~~~~~~~~~~~~~~~SONG~~~~~~~~~~~~~~~~~
	public void songCreate(Song song) {
		eManager.getTransaction().begin();	
		eManager.persist(song);
		eManager.getTransaction().commit();
	}

	public void songUpdate(Song song) {
		eManager.getTransaction().begin();
		eManager.find(Song.class, song.getSongID());
		song.setFilepath("C://project/spotify/songs/song.mp3");
		eManager.persist(song);
		eManager.getTransaction().commit();
	}

	public void songDelete(Song song) {
		eManager.getTransaction().begin();
		eManager.remove(eManager.find(Song.class, song.getSongID()));
		eManager.getTransaction().commit();
	}

	//~~~~~~~~~~~~~~~ALBUM~~~~~~~~~~~~~~~~~

	public void albumCreate(Album album) {
		eManager.getTransaction().begin();	
		eManager.persist(album);
		eManager.getTransaction().commit();

	}

	public void albumUpdate(Album album) {
		eManager.getTransaction().begin();
		eManager.find(Album.class, album.getAlbumID());
		album.SetcoverImagePath("C://project/spotify/album_images/test.png");
		eManager.persist(album);
		eManager.getTransaction().commit();
	}

	public void albumDelete(Album album) {
		eManager.getTransaction().begin();
		eManager.remove(eManager.find(Album.class, album.getAlbumID()));
		eManager.getTransaction().commit();

	}
	//~~~~~~~~~~~~~~~ARTIST~~~~~~~~~~~~~~~~~
	public void artistCreate(Artist artist) {
		eManager.getTransaction().begin();	
		eManager.persist(artist);
		eManager.getTransaction().commit();
	}

	public void artistUpdate(Artist artist) {
		eManager.getTransaction().begin();
		eManager.find(Artist.class, artist.getArtistID());
		artist.setBio("Lorem ipsum dolor sit amet, consectetur adipisicing elit.");
		eManager.persist(artist);
		eManager.getTransaction().commit();
	}

	public void artistDelete(Artist artist) {
		eManager.getTransaction().begin();
		eManager.remove(eManager.find(Artist.class, artist.getArtistID()));
		eManager.getTransaction().commit();
	}
}
