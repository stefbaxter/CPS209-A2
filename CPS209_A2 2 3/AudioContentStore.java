// STEFANIE BAXTER 501040808
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Simulation of audio content in an online store
// The songs, podcasts, audiobooks listed here can be "downloaded" to your library

public class AudioContentStore
{
	private ArrayList<AudioContent> contents=new ArrayList<AudioContent>();
	
	private Map<String, Integer> titleToIndex=new HashMap<String, Integer>(); // map 1
	
	private Map<String, ArrayList<Integer>> aToLocations=new HashMap<String, ArrayList<Integer>>(); // map 2
	
	private Map<Song.Genre, ArrayList<Integer>> gToLocations=new HashMap<Song.Genre, ArrayList<Integer>>(); // map 3
	
	public AudioContentStore()
	{
		try {
			contents=fileReader("store.txt");
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	
		// Map 1: titles to their index in contents array list
		
		for (int i=0;i<contents.size();i++) {
			titleToIndex.put(contents.get(i).getTitle(), i); 
		}
		
		// Map 2: artists/author to an array list of indexes where they can be found in contents array list
			
		for (int i=0; i<contents.size(); i++) {
			if (contents.get(i).getType().equals("SONG")) {
				Song currentSong = (Song) contents.get(i);
				String currentArtist=currentSong.getArtist();
				if (!aToLocations.containsKey(currentArtist)) { // if the artist is not already in the map, 
					ArrayList<Integer> ints = new ArrayList<Integer>(); // initiate new array list for artist locations
					ints.add(i); // add current location
					aToLocations.put(currentArtist, ints); // add artist to map
				}
				else {
					ArrayList<Integer> locations = aToLocations.get(currentArtist); // if artist already in map, extract locations array list
					locations.add(i); // add new location
					aToLocations.put(currentArtist, locations); // replace with updated locations array list
				}
			}
			else if (contents.get(i).getType().equals("AUDIOBOOK")) { // same idea for audiobook
				AudioBook currentBook = (AudioBook) contents.get(i);
				String currentAuthor=currentBook.getAuthor();
				if (!aToLocations.containsKey(currentAuthor)) {
					ArrayList<Integer> ints = new ArrayList<Integer>();
					ints.add(i);
					aToLocations.put(currentAuthor, ints);
				}
				else {
					ArrayList<Integer> locations = aToLocations.get(currentAuthor);
					locations.add(i);
					aToLocations.put(currentAuthor, locations);
				}
			}
		}
			
		// Map 3: Song genre to array list of indexes where it can be found in contents array list
		
		for (int i=0;i<contents.size();i++) {
			if (contents.get(i).getType().equals("SONG")) {
				Song currentSong=(Song) contents.get(i);
				if (!gToLocations.containsKey(currentSong.getGenre())) { // same logic as previous map
					ArrayList<Integer> inds=new ArrayList<Integer>();
					inds.add(i);
					gToLocations.put(currentSong.getGenre(), inds);
				}
				else {
					ArrayList<Integer> locations=gToLocations.get(currentSong.getGenre());
					locations.add(i);
					gToLocations.put(currentSong.getGenre(), locations);
				}
			}
		}
		
	}
	
	private ArrayList<AudioContent> fileReader(String filename) throws IOException {
		ArrayList<AudioContent> contents=new ArrayList<AudioContent>(); 
		Scanner in = new Scanner(new File(filename));
		while (in.hasNextLine()) {
			String reading = in.nextLine();
			if (reading.equals(Song.TYPENAME)) {
				
				ArrayList<String> songInfo=new ArrayList<String>();
				for (int i=0; i<7; i++) { // next 6 lines are song info we need
					songInfo.add(in.nextLine()); // populates song info in order: id, title, yr, len, artist, composer, genre
				}
				
				int numOfLyricLines=Integer.parseInt(in.nextLine()); // extract number of lines of lyrics
				String audioFile="";
				for (int i=0; i<numOfLyricLines; i++) { // extract lyrics
					audioFile+=in.nextLine();
					audioFile+="\n";
				}
				Song newSong=new Song(songInfo.get(1), Integer.parseInt(songInfo.get(2)), songInfo.get(0), Song.TYPENAME, audioFile, 
						Integer.parseInt(songInfo.get(3)), songInfo.get(4), songInfo.get(5), Song.Genre.valueOf(songInfo.get(6)), audioFile); // create new song object extracting correct info
				contents.add(newSong); // add it to contents
			}
			else if (reading.equals(AudioBook.TYPENAME)) {
				
				ArrayList<String> ABInfo=new ArrayList<String>();
				for (int i=0; i<6;i++) {
					ABInfo.add(in.nextLine()); // id, title, yr, len, author, narrator
				}
				
				int numChapters=Integer.parseInt(in.nextLine()); // get number of chapters
				ArrayList<String> chapterTitles=new ArrayList<String>();
				for (int i=0; i<numChapters; i++) {
					chapterTitles.add(in.nextLine()); // populate chapter titles array list
				}
				
				ArrayList<String> chapters=new ArrayList<String>();
				for (int i=0; i<numChapters;i++) { // for i amount of chapters
					int numChapterLines=Integer.parseInt(in.nextLine()); // extract num of lines in chapter
					String currentChapter="";
					for (int j=0; j<numChapterLines;j++) { // for the amount of lines in a chapter
						currentChapter+=in.nextLine(); // add line to current chapter
						currentChapter+="\n";
					}
					chapters.add(currentChapter); // populate chapters array list
				}
				String audioFile="";
				for (int i=0;i<numChapters;i++) {
					audioFile+=chapters.get(i); // create audio file containing all chapters
				}
				AudioBook newBook=new AudioBook(ABInfo.get(1), Integer.parseInt(ABInfo.get(2)), ABInfo.get(0), AudioBook.TYPENAME,
						audioFile, Integer.parseInt(ABInfo.get(3)), ABInfo.get(4), ABInfo.get(5), chapterTitles, chapters); // create audiobook
				contents.add(newBook); // add it to contents
			}
		}
		return contents; // return final contents array list
	}

		
		public AudioContent getContent(int index)
		{
			if (index < 1 || index > contents.size())
			{
				return null;
			}
			return contents.get(index-1); 
		}
		
		public void listAll()
		{
			for (int i = 0; i < contents.size(); i++)
			{
				int index = i + 1;
				System.out.print("" + index + ". ");
				contents.get(i).printInfo();
				System.out.println();
			}
		}
		
		public void findByTitle(String title) { // added method for SEARCH 
			if (titleToIndex.containsKey(title)) { // check if title is in the map
				int found=titleToIndex.get(title); // find its index
				AudioContent audioToPrint=contents.get(found); // choose correct audiocontent based on index found
				int indexToPrint=found+1;
				System.out.print(indexToPrint +". "); // print 1-indexed index and info for the selected content 
				audioToPrint.printInfo();
			}
			else {
				throw new AudioContentNotFoundException("No matches for " + title);
			}
		}
		
		public void findByName(String name) { // added method for SEARCHA
			if (aToLocations.containsKey(name)) {
				ArrayList<Integer> found=aToLocations.get(name); // same logic in this method but instead of one index we print for all indexes in locations array list
				for (int i=0; i<found.size();i++) {
					int indexToPrint=found.get(i)+1;
					AudioContent audioToPrint=contents.get(found.get(i));
					System.out.print(indexToPrint + ". ");
				    audioToPrint.printInfo();
				    System.out.println();
				}
			}
			else {
				throw new AudioContentNotFoundException("No matches for " + name);
			}
			
		}
		
		public void findByGenre(Song.Genre songGenre) { // added for method SEARCHG
			if (gToLocations.containsKey(songGenre)) {
				ArrayList<Integer> locations = gToLocations.get(songGenre); // same idea
				for (int i=0;i<locations.size();i++) {
					int indexToPrint=locations.get(i) + 1;
					AudioContent audioToPrint=contents.get(locations.get(i));
					System.out.print(indexToPrint + ". ");
					audioToPrint.printInfo();
					System.out.println();
				}
			}
			else {
				throw new AudioContentNotFoundException("No matches for: " + songGenre);
			}
		}
		
		public void partialSearch(String word) { // pass in contents to be searched
			boolean found=false;
			for (int i=0; i<contents.size();i++) {
				String currentInfo=contents.get(i).getInfo();
				if (currentInfo.contains(word)) {
					found=true;
					int indexToPrint=i+1;
					System.out.print(indexToPrint + ". ");
					contents.get(i).printInfo();
					System.out.println();
				}
			}
			if (!found) {
				throw new AudioContentNotFoundException("No matches for " + word);
			}
		}
		
		public ArrayList<Integer> getNameLocations(String name) { // added in for method DOWNLOADA
			return aToLocations.get(name);
		}
		
		public ArrayList<Integer> getGenreLocations(String genre) { // added in for method DOWNLOADG
			Song.Genre songGenre=Song.Genre.valueOf(genre);
			return gToLocations.get(songGenre);
		}
}


