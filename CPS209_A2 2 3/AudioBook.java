// STEFANIE BAXTER 501040808
import java.util.ArrayList;

/*
 * An AudioBook is a type of AudioContent.
 * It is a recording made available on the internet of a book being read aloud by a narrator
 * 
 */
public class AudioBook extends AudioContent
{
	public static final String TYPENAME =	"AUDIOBOOK";
	
	private String author; 
	private String narrator;
	private ArrayList<String> chapterTitles;
	private ArrayList<String> chapters;
	private int currentChapter = 0;

	
	public AudioBook(String title, int year, String id, String type, String audioFile, int length,
									String author, String narrator, ArrayList<String> chapterTitles, ArrayList<String> chapters)
	{
		// Make use of the constructor in the super class AudioContent. 
		// Initialize additional AudioBook instance variables. 
		super(title, year, id, type, audioFile, length);
		this.author=author;
		this.narrator=narrator;
		this.chapterTitles=chapterTitles;
		this.chapters=chapters;
		this.currentChapter=0;
	}
	
	public String getType()
	{
		return TYPENAME;
	}

  // Print information about the audiobook. First print the basic information of the AudioContent 
	// by making use of the printInfo() method in superclass AudioContent and then print author and narrator
	// see the video
	public void printInfo()
	{
		super.printInfo();
		System.out.println("Author: " + this.author + " Narrator: " + this.narrator);
	}
	
	public String getInfo() { // added this for SEARCHP: returns string of all the info we have on an AudioBook
		String chapterNames="";
		for (int i=0; i<chapterTitles.size(); i++) {
			chapterNames += chapterTitles.get(i) + " "; // get all the chapter titles into a single string
		}
		return super.getInfo() + this.author + this.narrator + chapterNames; // audioFile already contains the chapter contents, audioFile is returned in the super class
	} 
	
  // Play the audiobook by setting the audioFile to the current chapter title (from chapterTitles array list) 
	// followed by the current chapter (from chapters array list)
	// Then make use of the the play() method of the superclass
	public void play()
	{
	int current_chapter=this.currentChapter; // already takes the right index in selectChapter
	String file=(chapterTitles.get(current_chapter) + ". \n" + chapters.get(current_chapter)); // get the title and content
	super.setAudioFile(file); // set it as the audio file
	super.play(); // play it
	}
	
	// Print the table of contents of the book - i.e. the list of chapter titles
	// See the video
	public void printTOC()
	{
		for (int i=0; i<chapterTitles.size();i++) {
			int chap=i+1;
			System.out.println("Chapter " + chap + ". " + chapterTitles.get(i)); // print index and title
			System.out.println(); // new line
		}
	}

	// Select a specific chapter to play - nothing to do here
	public void selectChapter(int chapter)
	{
		if (chapter >= 1 && chapter <= chapters.size())
		{
			currentChapter = chapter - 1;
		}
	}
	
	//Two AudioBooks are equal if their AudioContent information is equal and both the author and narrators are equal
	public boolean equals(Object other)
	{
		AudioBook newbook=(AudioBook) other;
		return (super.equals(newbook) && this.author.equals(newbook.getAuthor()) && this.narrator.equals(newbook.getNarrator()));
	}
	
	public int getNumberOfChapters()
	{
		return chapters.size();
	}

	public String getAuthor()
	{
		return author;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}

	public String getNarrator()
	{
		return narrator;
	}

	public void setNarrator(String narrator)
	{
		this.narrator = narrator;
	}

	public ArrayList<String> getChapterTitles()
	{
		return chapterTitles;
	}

	public void setChapterTitles(ArrayList<String> chapterTitles)
	{
		this.chapterTitles = chapterTitles;
	}

	public ArrayList<String> getChapters()
	{
		return chapters;
	}

	public void setChapters(ArrayList<String> chapters)
	{
		this.chapters = chapters;
	}

}
