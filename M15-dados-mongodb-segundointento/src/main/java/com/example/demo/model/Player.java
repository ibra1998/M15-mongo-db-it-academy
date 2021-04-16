package com.example.demo.model;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;


 @Document
public class Player {
	 

	 public Player(String name){
		 this.name = name;
	 }
	public Player(long id, String name, List<Game> games, long rankingPosition, double averageSuccess) {
		super();
		this.id = id;
		this.name = name;
		this.games = games;
		this.rankingPosition = rankingPosition;
		this.averageSuccess = averageSuccess;
	}
	@Id
		private long id; 
	public Player() {
		super();
	}
	private String name = "anonymous";
	//private java.sql.Timestamp creationDate = new java.sql.Timestamp(System.currentTimeMillis());
	 
	 List <Game> games;
	
	public long getRankingPosition() {
		return rankingPosition;
	}
	public void setRankingPosition(long rankingPosition) {
		this.rankingPosition = rankingPosition;
	}
	public double getAverageSuccess() {
		return averageSuccess;
	}
	public void setAverageSuccess(double averageSuccess) {
		this.averageSuccess = averageSuccess;
	}
	private long rankingPosition;
	private double averageSuccess;
	public List<Game> getGames() {
		return games;
	}
	public void setGames(List<Game> games) {
		this.games = games;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}/*
	public Timestamp getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}*/
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	

}
