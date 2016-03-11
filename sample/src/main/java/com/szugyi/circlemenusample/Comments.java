package com.szugyi.circlemenusample;

public class Comments {
	
	private String message;
	private String author;
	private int authorID;
	private int idGlif;
	
	public Comments(){
		
	}
	
	public Comments(String message, String author, int authorID, int idGlif) {
		super();
		this.message = message;
		this.author = author;
		this.authorID = authorID;
		this.idGlif = idGlif;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public int getAuthorID() {
		return authorID;
	}
	
	public void setAuthorID(int authorID) {
		this.authorID = authorID;
	}
	
	public int getIdGlif() {
		return idGlif;
	}
	
	public void setIdGlif(int idGlif) {
		this.idGlif = idGlif;
	}

}
