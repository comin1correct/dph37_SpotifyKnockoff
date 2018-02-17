package edu.pitt.IS18.SpotifyKnockoff;

import java.awt.Color;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * source: https://www.youtube.com/watch?v=rujMhwoeqec&feature=youtu.be
 * @author David Hinton
 *
 */
public class SpotifyGUI {

	private JFrame frame;
	private JRadioButton rdbtnShowAlbum;
	private JTable tblData;
	private JTextField txtFieldSearch;
	private JLabel lblSearch;
	private JButton btnSearch;
	private DefaultTableModel musicData;
	private JScrollPane tblScrollPane;
	private JRadioButton rdbtnShowSong;
	private JRadioButton rdbtnShowArtist;
	/**
	 * Launch the application.
	 */
	
	/**
	 * Invoking the run and instantiating the SpotifyGUI 
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					SpotifyGUI window = new SpotifyGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					ErrorLogger.log(e.getMessage());
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Calling the initialize method to create the GUI
	 */
	public SpotifyGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Spotify");
		frame.setBounds(100, 100, 1000, 600);
		frame.getContentPane().setLayout(null);
		
		//LABEL
		JLabel lblViewSelector = new JLabel("Select View");
		lblViewSelector.setBounds(53, 51, 79, 16);
		frame.getContentPane().add(lblViewSelector);
		
		//ALBUM BUTTON
		rdbtnShowAlbum = new JRadioButton("Album");
		rdbtnShowAlbum.setSelected(true);
		rdbtnShowAlbum.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(rdbtnShowAlbum.isSelected())
				{
					musicData = searchAlbum(txtFieldSearch.getText()); // set musicData to the correct method
					tblData.setModel(musicData); //refresh table
				}
			}
		});
		rdbtnShowAlbum.setBounds(82, 79, 141, 23);
		frame.getContentPane().add(rdbtnShowAlbum);
		
		//SONG BUTTON
		rdbtnShowSong = new JRadioButton("Song");
		rdbtnShowSong.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(rdbtnShowSong.isSelected()){
					tblData.setModel((searchSong("")));
				}
			}
		});
		rdbtnShowSong.setBounds(82, 112, 141, 23);
		frame.getContentPane().add(rdbtnShowSong);
		
		//ARTIST BUTTON
		rdbtnShowArtist = new JRadioButton("Artist");
		rdbtnShowArtist.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(rdbtnShowArtist.isSelected()){
					tblData.setModel(searchArtist(""));	
					}
			}
		});
		rdbtnShowArtist.setBounds(82, 147, 141, 23);
		frame.getContentPane().add(rdbtnShowArtist);
		
		
		//BUTTON GROUP - manage on/off states
		//https://docs.oracle.com/javase/8/docs/api/javax/swing/ButtonGroup.html
		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnShowAlbum);
		group.add(rdbtnShowSong);
		group.add(rdbtnShowArtist);
		
		
		//SCROLLING IN TABLE
		tblScrollPane = new JScrollPane();
		tblScrollPane.setBounds(235, 91, 692, 416);
		frame.getContentPane().add(tblScrollPane);
		
		
		//CREATE TABLE
		musicData = searchSong(""); //default to empty string
		tblData = new JTable(musicData);
		
		//USER CLICK ON ITEM IN TABLE
		tblData.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//get the value base on row and column, but column is static.
				String elementID = (String)tblData.getValueAt(tblData.getSelectedRow(), 0);
				txtFieldSearch.setText("");//delete text once in variable element
				if(rdbtnShowAlbum.isSelected())
				{
					String sql = "SELECT title, length, file_path, release_date, record_date"+
					" FROM song s JOIN album_song sa ON s.song_id = sa.fk_song_id"+
					" WHERE sa.fk_album_id = '"+elementID+"';";
					getSongOfSource("album", sql, elementID);
				}

				if(rdbtnShowArtist.isSelected())
				{	
					String sql = "SELECT title, length, file_path, release_date, record_date"+
					" FROM song s JOIN song_artist sa ON s.song_id = sa.fk_song_id"+
					" WHERE sa.fk_artist_id = '"+elementID+"';";
					getSongOfSource("artist", sql, elementID);					
				}
			}
		});
	
		
		//SCROLL
		tblScrollPane.setViewportView(tblData);
		
		//added in video:
		tblData.setFillsViewportHeight(true);
		tblData.setShowGrid(true);
		tblData.setGridColor(Color.black);
		
		//TEXT FIELD
		txtFieldSearch = new JTextField();
		txtFieldSearch.setBounds(53, 375, 146, 26);
		frame.getContentPane().add(txtFieldSearch);
		txtFieldSearch.setColumns(10);
		
		//LABEL
		lblSearch = new JLabel("Seach");
		lblSearch.setBounds(53, 358, 61, 16);
		frame.getContentPane().add(lblSearch);
		
		//BUTTON
		btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(rdbtnShowAlbum.isSelected())
				{
					musicData = searchAlbum(txtFieldSearch.getText()); // set musicData to the correct method
					tblData.setModel(musicData); //refresh table
				}
				if(rdbtnShowSong.isSelected())
				{
					
					musicData = searchSong(txtFieldSearch.getText()); // set musicData to the correct method
					tblData.setModel(musicData); //refresh table
				}
				if(rdbtnShowArtist.isSelected())
				{
					System.out.println("BUTTON: "+txtFieldSearch.getText());
					musicData = searchArtist(txtFieldSearch.getText()); // set musicData to the correct method
					tblData.setModel(musicData); //refresh table
				}
			}
		});
		btnSearch.setBounds(82, 413, 117, 29);
		frame.getContentPane().add(btnSearch);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * This method searches for a song by song title and returns the search results as a DefaultTableModel.  
	 * If searchTerm parameter is blank (searchTerm = “”), return a complete list of songs from the database.
	 * @param songName
	 * @return
	 */
	private DefaultTableModel searchSong(String songName)
	{
		String sql = "SELECT * FROM song";	
		try
		{
			DbUtilities db = new DbUtilities();
			if(!songName.equals("")){
				sql += " WHERE title LIKE ?;";
				System.out.println("searchSong: "+sql);
				Connection conn = db.getConn();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%"+songName+"%");
				DefaultTableModel tb = db.getDataTable(pstmt.executeQuery());
	
				pstmt.close();
				db.closeDbConnection();
				db = null;
				return tb;
			}
			else{return db.getDataTable(sql);}			
			
		}catch(SQLException e){
			ErrorLogger.log(e.getMessage());
		}
		
		return null;
	}
	/**
	 * This method searches for a album by album title and returns the search results as a DefaultTableModel.  
	 * If searchTerm parameter is blank (searchTerm = “”), return a complete list of albums from the database.
	 * @param albumName
	 * @return
	 */
	private DefaultTableModel searchAlbum(String albumName)
	{
		String sql = "SELECT * FROM album";	
		try
		{
			DbUtilities db = new DbUtilities();
			if(!albumName.equals("")){
				sql += " WHERE title LIKE ?;";
				Connection conn = db.getConn();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%"+albumName+"%");
				DefaultTableModel tb = db.getDataTable(pstmt.executeQuery());
	
				pstmt.close();
				db.closeDbConnection();
				db = null;
				return tb;
			}
			else{return db.getDataTable(sql);}			
			
		}catch(SQLException e){
			ErrorLogger.log(e.getMessage());
		}
		
		return null;
	}
	
	/**
	 * This method searches for a artists by artist first OR last name OR band name and returns the search results as a DefaultTableModel.  
	 * If searchTerm parameter is blank (searchTerm = “”), return a complete list of artists from the database.
	 * @param songArtist
	 * @return
	 */
	private DefaultTableModel searchArtist(String songArtist)
	{
		String sql = "SELECT * FROM artist";	
		try
		{
			DbUtilities db = new DbUtilities();
			if(!songArtist.equals("")){
				sql += " WHERE first_name LIKE ? OR last_name LIKE ? OR band_name LIKE ?;";
				
				Connection conn = db.getConn();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%"+songArtist+"%");
				pstmt.setString(2, "%"+songArtist+"%");
				pstmt.setString(3, "%"+songArtist+"%");
				
				DefaultTableModel tb = db.getDataTable(pstmt.executeQuery());
	
				pstmt.close();
				db.closeDbConnection();
				db = null;
				return tb;
			}
			else{return db.getDataTable(sql);}			
			
		}catch(SQLException e){
			e.printStackTrace();
			ErrorLogger.log(e.getMessage());
		return null;
		}
	}
	/**
	 * handling on click events within the Jtable.
	 * @param table - base on schema, table name
	 * @param sql - select statement
	 * @param elementID - item select
	 */
	private void getSongOfSource(String table,String sql, String elementID)
	{
		try{
			DbUtilities db = new DbUtilities();
			musicData = db.getDataTable(sql); 
			if(musicData.getRowCount() == 0)
			{
				JOptionPane.showMessageDialog(frame, "No results Found");
				if(table.equalsIgnoreCase("album")){rdbtnShowAlbum.doClick();}
				else{rdbtnShowArtist.doClick();}
			}
			else{
				tblData.setModel(musicData); //refresh table
				rdbtnShowSong.setSelected(true);
				db.closeDbConnection();
				db = null;
			}
		}
		catch(SQLException e1){ErrorLogger.log(e1.getMessage());}
		

	}
} 
