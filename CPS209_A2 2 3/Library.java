// STEFANIE BAXTER 501040808
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*
 * This class manages, stores, and plays audio content such as songs, podcasts and audiobooks. 
 */
public class Library
{
	private ArrayList<Song> 			songs; 
	private ArrayList<AudioBook> 	audiobooks;
	private ArrayList<Playlist> 	playlists; 
	
	
	// Public methods in this class set errorMesg string 
	// Error Messages can be retrieved from main in class MyAudioUI by calling  getErrorMessage()
	// In assignment 2 we will replace this with Java Exceptions
	String errorMsg = "";
	
	public String getErrorMessage()
	{
		return errorMsg;
	}

	public Library()
	{
		songs 			= new ArrayList<Song>(); 
		audiobooks 	= new ArrayList<AudioBook>(); ;
		playlists   = new ArrayList<Playlist>();
	}
	/*
	 * Download audio content from the store. Since we have decided (design decision) to keep 3 separate lists in our library
	 * to store our songs, podcasts and audiobooks (we could have used one list) then we need to look at the type of
	 * audio content (hint: use the getType() method and compare to Song.TYPENAME or AudioBook.TYPENAME etc)
	 * to determine which list it belongs to above
	 * 
	 * Make sure you do not add song/podcast/audiobook to a list if it is already there. Hint: use the equals() method
	 * If it is already in a list, set the errorMsg string and return false. Otherwise add it to the list and return true
	 * See the video
	 */
	public void download(AudioContent content)  
	{
		
		if (content.getType().equals("AUDIOBOOK")) { 
			AudioBook mybook=(AudioBook) content; // case
			 int index=audiobooks.indexOf(mybook); // get the java index of the book if it exists
			 if (index==-1) { // if it doesn't exist
				 audiobooks.add(mybook); // add it
			}
			 else { 
				 throw new AudioContentAlreadyDownloadedException(mybook.getType() +" "+ mybook.getTitle() + " already downloaded");
			 }
			
		}
		else if (content.getType().equals("SONG")) { // same process but with song
			Song mysong=(Song) content; //
			int index=songs.indexOf(mysong);
			if (index==-1) {
				songs.add(mysong);
			}
			else {
				
				throw new AudioContentAlreadyDownloadedException(mysong.getType() +" "+ mysong.getTitle() + " already downloaded");
			}
		}
	
	}
	
	// Print Information (printInfo()) about all songs in the array list
	public void listAllSongs()
	{
		for (int i = 0; i < songs.size(); i++)
		{
			int index = i + 1; // proper index to print
			System.out.print("" + index + ". ");
			songs.get(i).printInfo(); // print index then song info for each song 
			System.out.println();	// new line
		}
	}
	
	// Print Information (printInfo()) about all audiobooks in the array list
	public void listAllAudioBooks()
	{
		for (int i=0; i<audiobooks.size();i++) {
			int ind=i+1;
			System.out.print("" + ind + ". ");
			audiobooks.get(i).printInfo(); // print correct index and info for each book
			System.out.println(); // new line
		}
	}
	
	
  // Print the name of all playlists in the playlists array list
	// First print the index number as in listAllSongs() above
	public void listAllPlaylists()
	{
		for (int i=0;i<playlists.size();i++) { // same method but with playlists
			int ind=i+1;
			System.out.print(""+ind+". ");
			System.out.println(playlists.get(i).getTitle());
		}
	}
	
  // Print the name of all artists. 
	public void listAllArtists()
	{
		// First create a new (empty) array list of string 
		// Go through the songs array list and add the artist name to the new arraylist only if it is
		// not already there. Once the artist arrayl ist is complete, print the artists names
		ArrayList<String> artist_names = new ArrayList<String>();
		for (int i=0;i<songs.size();i++) {
			String current_artist=songs.get(i).getArtist(); // get current song object's artist
			if (!artist_names.contains(current_artist)) {
				artist_names.add(current_artist); // if not already there add it to list
			}
		}
		for (int n=0;n<artist_names.size();n++) {
			int ind=n+1;
			System.out.println(""+ind+". "+artist_names.get(n)); // print each index + artist
		}
	}

	// Delete a song from the library (i.e. the songs list) - 
	// also go through all playlists and remove it from any playlist as well if it is part of the playlist
	public void deleteSong(int index) 
	{
		if (index>0 && index<=songs.size()) { // check valid index
			Song removed_song=songs.get(index-1); // store song in variable for later use
			songs.remove(index-1); // remove song
			
			for (int i=0;i<playlists.size();i++) {
				Playlist current=playlists.get(i);
				int index_of_song=current.getContent().indexOf(removed_song); // check if removed song is in a playlist
				if (index_of_song!=-1) { // if it is in the playlist, remove it
					current.deleteContent(index_of_song + 1); // adjust index in this case
				}
			}
		}
		else {
			throw new AudioContentNotFoundException("Song could not be deleted");
		}
	}
	
  //Sort songs in library by year
	public void sortSongsByYear()
	{
		Collections.sort(songs, new SongYearComparator()); // call from Song.java
	
	}
  // Write a class SongYearComparator that implements
	// the Comparator interface and compare two songs based on year
	private class SongYearComparator implements Comparator<Song> // make sure order is good
	{
		public int compare(Song a, Song b) {
			if (a.getYear()<b.getYear()) {
				return -1; // a's year is before b's year
			}
			else if (a.getYear()>b.getYear()) {
				return 1; // a's year is after b's
			}
			return 0;
		}
	}

	// Sort songs by length
	public void sortSongsByLength()
	{
		Collections.sort(songs, new SongLengthComparator());
	}
  // Write a class SongLengthComparator that implements
	// the Comparator interface and compare two songs based on length
	private class SongLengthComparator implements Comparator<Song>
	{
		public int compare(Song a, Song b) {
			if (a.getLength()<b.getLength()) {
				return -1; // a is shorter than b
			}
			else if (a.getLength()>b.getLength()) {
				return 1; // a is longer than b
			}
			return 0;
		}
	}

	// Sort songs by title 
	public void sortSongsByName()
	{
		class SongTitleComparator implements Comparator<Song> {
			public int compare(Song a, Song b) {
				return a.getTitle().compareTo(b.getTitle()); // check alphabetical order
			}
		}
		Collections.sort(songs, new SongTitleComparator());
	}

	
	
	/*
	 * Play Content
	 */
	
	// Play song from songs list
	public void playSong(int index)
	{
		if (index < 1 || index > songs.size())
		{
			throw new AudioContentNotFoundException("Song not found");
		}
		songs.get(index-1).play();
	}
	
	
	// Play a chapter of an audio book from list of audiobooks
	public void playAudioBook(int index, int chapter)
	{
		if (index>0 && index<=audiobooks.size()) { // validate index
			AudioBook my_book=audiobooks.get(index-1);
			if (chapter>0 && chapter<=my_book.getNumberOfChapters()) {
				my_book.selectChapter(chapter); // indexing adjusted in AudioBook selectChapter method
				my_book.play(); // play selected chapter of the book
			}
			else {
				throw new AudioContentNotFoundException("AudioBook not found");
			}
		}
		else {
			throw new AudioContentNotFoundException("AudioBook not found");
		}
	}
	
	// Print the chapter titles (Table Of Contents) of an audiobook
	// see class AudioBook
	public void printAudioBookTOC(int index)
	{
		if (index>0 && index<=audiobooks.size()) {
			audiobooks.get(index-1).printTOC();
		}
		else {
			throw new AudioContentNotFoundException("AudioBook not found");
		}
	}
	
  /*
   * Playlist Related Methods
   */
	
	// Make a new playlist and add to playlists array list
	// Make sure a playlist with the same title doesn't already exist
	
	public void makePlaylist(String title)  
	{
		ArrayList<String> playlist_titles=new ArrayList<String>();
		for (int i=0; i<playlists.size();i++) {
			String current_name=playlists.get(i).getTitle();
			playlist_titles.add(current_name); // create array list of playlist titles
		}
		if (!playlist_titles.contains(title)) { // check if new playlist title already exists
			Playlist newpl=new Playlist(title);
			playlists.add(newpl); // if it doesn't, add it to playlists
		}
		else {
			throw new PlaylistAlreadyExistsException("Playlist " + title + " Already Exists");
		}
	}
	
	// Print list of content information (songs, audiobooks etc) in playlist named title from list of playlists
	public void printPlaylist(String title)
	{
		Playlist newpl= new Playlist(title);
		if (playlists.contains(newpl)) {
		int index=playlists.indexOf(newpl); // assumes title is a valid entry that already exists
		playlists.get(index).printContents(); // find it within playlists arraylist and call printContents
		}
		else {
			throw new PlaylistEditingException("Playlist " + title + " does not exist");
		}
		
	}
	
	// Play all content in a playlist
	public void playPlaylist(String playlistTitle)
	{
		boolean found=false;
		for (int i=0;i<playlists.size();i++) {
			Playlist current_playlist=playlists.get(i);
			if (current_playlist.getTitle().equals(playlistTitle)) { // find playlist which matches entered title
				current_playlist.playAll(); // play it all
				found=true;
			}
		}
		if (!found) {
			throw new PlaylistEditingException("Playlist " + playlistTitle + " not found");
		}
	}
	
	// Play a specific song/audiobook in a playlist
	public void playPlaylist(String playlistTitle, int indexInPL)
	{ // same process as above method but also find indexInPL
		boolean found=false; 
		for (int i=0;i<playlists.size();i++) {
			Playlist current_playlist=playlists.get(i);
			if (current_playlist.getTitle().equals(playlistTitle) && current_playlist.contains(indexInPL)) {
				System.out.println(playlistTitle);
				current_playlist.play(indexInPL); // indexing adjusted in Playlist play method
				found=true;
			}
		}
		if (!found) {
			throw new PlaylistEditingException("Element " + indexInPL + " from playlist " + playlistTitle + " not found");
		}
	}
	
	// Add a song/audiobook/podcast from library lists at top to a playlist
	// Use the type parameter and compare to Song.TYPENAME etc
	// to determine which array list it comes from then use the given index
	// for that list
	public void addContentToPlaylist(String type, int index, String playlistTitle)
	{
		boolean added=false; 
		Playlist pl=new Playlist(playlistTitle); // new playlist object
		int i=playlists.indexOf(pl); // find it in playlists arraylist
		if (type.equalsIgnoreCase(Song.TYPENAME) && index>0 && index<=songs.size()) { // case for song
			Song song_to_add=songs.get(index-1); // create song
			playlists.get(i).addContent(song_to_add); // add it to correct playlist
			added=true;
		} // same thing below for audiobook and podcast
		else if (type.equalsIgnoreCase(AudioBook.TYPENAME) && index>0 && index<=audiobooks.size()) {
			AudioBook book_to_add=audiobooks.get(index-1);
			playlists.get(i).addContent(book_to_add);
			added=true;
		}
		if (!added) {
			throw new PlaylistEditingException(type + " " + index +" could not be added to " + playlistTitle);
		}
	}

  // Delete a song/audiobook/podcast from a playlist with the given title
	// Make sure the given index of the song/audiobook/podcast in the playlist is valid 
	public void delContentFromPlaylist(int index, String title) 
	{
		boolean deleted=false;
		for (int i=0; i<playlists.size(); i++) {
			Playlist current_pl=playlists.get(i);
			if (current_pl.getTitle().equals(title)) { // find playlist to delete from 
				Playlist pl_to_delete_from=current_pl;
				
				if (pl_to_delete_from.contains(index)) { // call deleteContent on correct playlist if index is valid
					pl_to_delete_from.deleteContent(index); // indexing fixed in Playlist
					deleted=true;
				}
			}
		
		}
	 if (!deleted) {
		 throw new PlaylistEditingException("Deletion from playlist " + title + " failed");
	 }
	}
	
}

class AudioContentAlreadyDownloadedException extends RuntimeException {
	public AudioContentAlreadyDownloadedException() {}
	public AudioContentAlreadyDownloadedException(String message) {
		super(message);
	}
}

class AudioContentNotFoundException extends RuntimeException {
	public AudioContentNotFoundException() {}
	public AudioContentNotFoundException (String message) {
		super(message);
	}
}

class PlaylistAlreadyExistsException extends RuntimeException {
	public PlaylistAlreadyExistsException() {}
	public PlaylistAlreadyExistsException(String message) {
		super(message);
	}
}

class PlaylistEditingException extends RuntimeException {
	public PlaylistEditingException() {}
	public PlaylistEditingException(String message) {
		super(message);
	}
}