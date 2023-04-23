// STEFANIE BAXTER 501040808
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.StringTokenizer;

// Simulation of a Simple Text-based Music App (like Apple Music)

public class MyAudioUI
{
	public static void main(String[] args)
	{
		// Simulation of audio content in an online store
		// The songs, podcasts, audiobooks in the store can be downloaded to your mylibrary
		AudioContentStore store = new AudioContentStore();
		
		// Create my music mylibrary
		Library mylibrary = new Library();

		Scanner scanner = new Scanner(System.in);
		System.out.print(">");

		// Process keyboard actions
		while (scanner.hasNextLine())
		{
			String action = scanner.nextLine();

			if (action == null || action.equals("")) 
			{
				System.out.print("\n>");
				continue;
			}
			else if (action.equalsIgnoreCase("Q") || action.equalsIgnoreCase("QUIT"))
				return;
			
			else if (action.equalsIgnoreCase("STORE"))	// List all songs
			{
				store.listAll(); 
			}
			else if (action.equalsIgnoreCase("SONGS"))	// List all songs
			{
				mylibrary.listAllSongs(); 
			}
			else if (action.equalsIgnoreCase("BOOKS"))	// List all songs
			{
				mylibrary.listAllAudioBooks(); 
			}
			
			else if (action.equalsIgnoreCase("ARTISTS"))	// List all songs
			{
				mylibrary.listAllArtists(); 
			}
			else if (action.equalsIgnoreCase("PLAYLISTS"))	// List all play lists
			{
				mylibrary.listAllPlaylists(); 
			}
			// Download audiocontent (song/audiobook/podcast) from the store 
			// Specify the index of the content
			else if (action.equalsIgnoreCase("DOWNLOAD")) 
			{
				int index1 = 0;
				System.out.print("From Store Content #: "); // get the wanted range of indexes
				if (scanner.hasNextInt())
				{
					index1 = scanner.nextInt();
					scanner.nextLine(); // "consume" nl character (necessary when mixing nextLine() and nextInt())
				}
				int index2 =0;
				System.out.print("To Store Content #: ");
				if (scanner.hasNextInt()) {
					index2=scanner.nextInt();
					scanner.nextLine();
				}
				for (int i=index1; i<=index2;i++) { // for the range of given indexes, 
				try {
					AudioContent content=store.getContent(i); // getContent subtracts 1 so it adjusts to 0-indexed array lists
					mylibrary.download(content); // try to download the content at each index and print that it was added to library
					System.out.println(content.getType() + " " + content.getTitle() + " Added to Library");
				}
				catch (AudioContentAlreadyDownloadedException e) {
					System.out.println(e.getMessage()); // catch error if it is already downloaded (see library method download)
				}
				}
			}
			
			else if (action.equalsIgnoreCase("DOWNLOADA")) {
				String name="";
				System.out.println("Artist/Author Name: ");
				if (scanner.hasNext()) {
					name=scanner.nextLine(); // get name to download
				}
				ArrayList<Integer> artistLocations=store.getNameLocations(name); // see getNameLocations in AudioContentStore, returns array list of where to find artist/authors content
				for (int i=0; i<artistLocations.size();i++) {
					try {
						AudioContent content=store.getContent(artistLocations.get(i) + 1); // adjust indexing because getContent subtracts 1, but artistLocations uses 0-indexing
						mylibrary.download(content); // try to download every thing from that artist/author
					}
					catch (AudioContentAlreadyDownloadedException e){
						System.out.println(e.getMessage()); // catch error if already downloaded
					}
				}
			}
			
			else if (action.equalsIgnoreCase("DOWNLOADG")) {
				String genre="";
				System.out.println("Genre: ");
				if (scanner.hasNext()) {
					genre=scanner.nextLine(); // get genre to download
				}
				ArrayList<Integer> genreLocations=store.getGenreLocations(genre); // see getGenreLocations in AudioContentStore, returns locations (indexes) array list of wanted genre 
				for (int i=0;i<genreLocations.size();i++) {
					try {
						AudioContent content=store.getContent(genreLocations.get(i)+1); // adjust indexing for same reason as last method
						mylibrary.download(content); // try to download
					}
					catch (AudioContentAlreadyDownloadedException e) {
						System.out.println(e.getMessage()); // catch if already downloaded
					}
				}
			}
			
			else if (action.equalsIgnoreCase("SEARCH")) 
			{
				String title="";
				System.out.print("Title: ");
				if (scanner.hasNext()) { // get the title
					title=scanner.nextLine();
				}
				try {
				store.findByTitle(title); // see AudioContentStore method findByTitle (uses map to search)
				}
				catch (AudioContentNotFoundException e) {
					System.out.println(e.getMessage()); // catch if title can't be found
				}
			}
			
			else if (action.equalsIgnoreCase("SEARCHA")) 
			{
				String name="";
				System.out.print("Artist: ");
				if (scanner.hasNext()) {
					name=scanner.nextLine(); // get the name
				}
				try {
					store.findByName(name); // see AudioContentStore method findByName (uses map to search)
				} 
				catch (AudioContentNotFoundException e) {
					System.out.println(e.getMessage());
				}
			}
			
			else if (action.equalsIgnoreCase("SEARCHG")) 
			{
				String genre="";
				System.out.print("Genre [POP, ROCK, JAZZ, HIPHOP, RAP, CLASSICAL]: ");
				if (scanner.hasNext()) {
					genre=scanner.nextLine();
				}
				try { // string must be converted to Song.Genre type to be identified within the enum of genres at the top of class Song 
					store.findByGenre(Song.Genre.valueOf(genre)); // see method in AudioContentStore (uses map to search)
				}
				catch (AudioContentNotFoundException e) {
					System.out.println(e.getMessage()); // catch error if not found
				}
			}
			
			else if (action.equalsIgnoreCase("SEARCHP")) { // bonus method, see getInfo() in AudioContent, Song, and AudioBook
				String word="";
				System.out.print("Word to search: ");
				if (scanner.hasNext()) {
					word=scanner.nextLine(); // get word to search
				}
				try {
					store.partialSearch(word); // see method partialSearch in AudioContentStore
				}
				catch (AudioContentNotFoundException e) {
					System.out.println(e.getMessage()); // catch error if there are no matches
				} 
			}
			
			// Get the *library* index (index of a song based on the songs list)
			// of a song from the keyboard and play the song 
			else if (action.equalsIgnoreCase("PLAYSONG")) 
			{
				int index=0;
				System.out.print("Song Number: "); // get the index
				if (scanner.hasNextInt()) {
					index=scanner.nextInt();
					scanner.nextLine();
				}
				
				try {
					mylibrary.playSong(index);
				}
				catch (AudioContentNotFoundException e) {
					System.out.println(e.getMessage());
				}
			}
			// Print the table of contents (TOC) of an audiobook that
			// has been downloaded to the library. Get the desired book index
			// from the keyboard - the index is based on the list of books in the library
			else if (action.equalsIgnoreCase("BOOKTOC")) 
			{
				int index=0;
				System.out.print("Audio Book Number: "); // get index
				if (scanner.hasNextInt()) {
					index=scanner.nextInt();
					scanner.nextLine();
				}
				try {
					mylibrary.printAudioBookTOC(index);
				}
				catch (AudioContentNotFoundException e) {
					System.out.println(e.getMessage());
				}
			}
			// Similar to playsong above except for audio book
			// In addition to the book index, read the chapter 
			// number from the keyboard - see class Library
			else if (action.equalsIgnoreCase("PLAYBOOK")) 
			{
				int book_index=0;
				System.out.print("Audio Book Number: "); // get index
				if (scanner.hasNextInt()) {
					book_index=scanner.nextInt();
					scanner.nextLine();
				}
				int ch_index=0;
				System.out.print("Chapter: "); // get chapter
				if (scanner.hasNextInt()) {
					ch_index=scanner.nextInt();
					scanner.nextLine();
				}
				try {
					mylibrary.playAudioBook(book_index, ch_index);
				}
				catch (AudioContentNotFoundException e) {
					System.out.println(e.getMessage());
				}
			}
			
		
			// Specify a playlist title (string) 
			// Play all the audio content (songs, audiobooks, podcasts) of the playlist 
			// see class Library for the method to call
			else if (action.equalsIgnoreCase("PLAYALLPL")) 
			{
				String title="";
				System.out.print("Playlist Title: "); // get playlist
				if (scanner.hasNext()) {
					title=scanner.next();
					scanner.nextLine();
				}
				try {
				mylibrary.playPlaylist(title); // print it
				}
				catch (PlaylistEditingException e) {
					System.out.println(e.getMessage());
				}
			}
			// Specify a playlist title (string) 
			// Read the index of a song/audiobook/podcast in the playist from the keyboard 
			// Play all the audio content 
			// see class Library for the method to call
			else if (action.equalsIgnoreCase("PLAYPL")) 
			{
				String playlistTitle="";
				System.out.print("Playlist Title: "); // get playlist
				if (scanner.hasNext()) {
					playlistTitle=scanner.next();
					scanner.nextLine();
				}
				int content=0;
				System.out.print("Content Number: ");
				if (scanner.hasNextInt()) {
					content=scanner.nextInt();
					scanner.nextLine();
				}
				try {
				mylibrary.playPlaylist(playlistTitle, content); // call method from Playlist to play 
				}
				catch (PlaylistEditingException e) {
					System.out.println(e.getMessage());
				}
			}
			// Delete a song from the list of songs in mylibrary and any play lists it belongs to
			// Read a song index from the keyboard
			// see class Library for the method to call
			else if (action.equalsIgnoreCase("DELSONG")) 
			{
				int song_index=0;
				System.out.print("Library Song #: "); // get song to delete
				if (scanner.hasNextInt()) {
					song_index=scanner.nextInt();
					scanner.nextLine();
				}
				try {
				mylibrary.deleteSong(song_index); // delete it 
				}
				catch (AudioContentNotFoundException e){
					System.out.println(e.getMessage());
				}
			}
			// Read a title string from the keyboard and make a playlist
			// see class Library for the method to call
			else if (action.equalsIgnoreCase("MAKEPL")) 
			{
				String title="";
				System.out.print("Playlist Title: "); // get new title
				if (scanner.hasNext()) {
					title=scanner.next();
					scanner.nextLine();
				}
				try {
					mylibrary.makePlaylist(title); // try to make one
				}
				catch (PlaylistAlreadyExistsException e) {
					System.out.println(e.getMessage()); // catch error if it exists already
				}
			}
			// Print the content information (songs, audiobooks, podcasts) in the playlist
			// Read a playlist title string from the keyboard
		  // see class Library for the method to call
			else if (action.equalsIgnoreCase("PRINTPL"))	// print playlist content
			{ 
				String playlistTitle="";
				System.out.print("Playlist Title: "); 
				if (scanner.hasNext()) {
					playlistTitle=scanner.next();
					scanner.nextLine();
				}
				try {
				mylibrary.printPlaylist(playlistTitle);
				}
				catch (PlaylistEditingException e) {
					System.out.println(e.getMessage());
				}
			}
			// Add content (song, audiobook, podcast) from mylibrary (via index) to a playlist
			// Read the playlist title, the type of content ("song" "audiobook" "podcast")
			// and the index of the content (based on song list, audiobook list etc) from the keyboard
		  // see class Library for the method to call
			else if (action.equalsIgnoreCase("ADDTOPL")) 
			{
				String playlistTitle="";
				System.out.print("Playlist Title: "); // get title
				if (scanner.hasNext()) {
					playlistTitle=scanner.next();
					scanner.nextLine();
				}
				String type="";
				System.out.print("Content Type [SONG, PODCAST, AUDIOBOOK]: "); // get type
				if (scanner.hasNext()) {
					type=scanner.next();
					scanner.nextLine();
				}
				int content=0;
				System.out.print("Library Content #: "); // get content index
				if (scanner.hasNextInt()) {
					content=scanner.nextInt();
					scanner.nextLine();
				}
				try {
				mylibrary.addContentToPlaylist(type, content, playlistTitle); // add it
				}
				catch (PlaylistEditingException e) {
					System.out.println(e.getMessage());
				}
			}
			// Delete content from play list based on index from the playlist
			// Read the playlist title string and the playlist index
		  // see class Library for the method to call
			else if (action.equalsIgnoreCase("DELFROMPL")) 
			{
				String title="";
				System.out.print("Playlist Title: "); // get playlist
				if (scanner.hasNext()) {
					title=scanner.next();
					scanner.nextLine();
				}
				int content=0;
				System.out.print("Playlist Content #: "); // get index
				if (scanner.hasNextInt()) {
					content=scanner.nextInt();
					scanner.nextLine();
				}
				try {
				mylibrary.delContentFromPlaylist(content, title); // delete it
				}
				catch (PlaylistEditingException e){
					System.out.println(e.getMessage());
				}
			}
			
			else if (action.equalsIgnoreCase("SORTBYYEAR")) // sort songs by year
			{
				mylibrary.sortSongsByYear();
			}
			else if (action.equalsIgnoreCase("SORTBYNAME")) // sort songs by name (alphabetic)
			{
				mylibrary.sortSongsByName();
			}
			else if (action.equalsIgnoreCase("SORTBYLENGTH")) // sort songs by length
			{
				mylibrary.sortSongsByLength();
			}

			System.out.print("\n>");
		}
	}
}

