package com.perezcalle.songlibrary;

public class Song implements Comparable<Song>{
	private String title;
	private String artist;
	private String album;
	private String year;
	

	public Song(String t, String art, String alb, String y) {
		this.title = t;
		this.artist = art;
		this.album = alb;
		this.year = y;
		// TODO Auto-generated constructor stub
	}
	
	public Song(String t, String art)
	{
		this(t,art,"","");
	}


	public String getYear() {
		// TODO Auto-generated method stub
		return this.year;
	}

	public String getArtist() {
		// TODO Auto-generated method stub
		return this.artist;
	}

	public String getTitle() {
		return this.title;
		// TODO Auto-generated method stub
	}
	
	public String getalbum() {
		return this.album;
		
	}

	public String lowerTitle() {
		return this.title.toLowerCase();
	}

	public String lowerArtist() {
		return this.artist.toLowerCase();
	}
	
	public void setalbum(String a) {
		this.album = a;
	}
	
	public void setYear(String y) {
		this.year = y;
	}
	
	public String toString() {
		return this.title +" - "+this.artist;
	}


	@Override
	public int compareTo(Song s) {
		if(this.title.trim().toUpperCase().compareTo(s.getTitle().trim().toUpperCase()) == 0){
			if(this.artist.trim().toUpperCase().compareTo(s.getArtist().trim().toUpperCase()) == 0){
				return 0;
			}
			else if(this.artist.trim().toUpperCase().compareTo(s.getArtist().trim().toUpperCase()) > 0){
				return 1;
			}
			else{
				return -1;
			}
		}
		else if(this.title.trim().toUpperCase().compareTo(s.getTitle().trim().toUpperCase()) > 0){
			return 1;
		}
		else{
			return -1;
		}
	}
}
