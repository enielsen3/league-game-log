package com.gamelog.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "game")
public class Game implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotEmpty(message = "Champion is required.")
	@Column(name = "champion")
	private String champion;
	@NotEmpty(message = "Opponent is required.")
	@Column(name = "opponent")
	private String opponent;
	
	@Column(name = "kills")
	private int kills;
	
	@Column(name = "deaths")
	private int deaths;
	
	@Column(name = "assists")
	private int assists;
	
	@Column(name = "cs")
	private int cs;
	@NotEmpty(message = "Result is required.")
	@Column(name = "result")
	private String result;
	
	
	public Game() {
	}
	
	
	public Game(String champion, String opponent, int kills, int deaths, int assists, int cs, String result) {
		this.champion = champion;
		this.opponent = opponent;
		this.kills = kills;
		this.deaths = deaths;
		this.assists = assists;
		this.cs = cs;
		this.result = result;
	}
	
	@Override
	public String toString() {
		return String.format("Game [id=%d, champion ='%s', opponent='%s', "
				+ "kills=%d, deaths=%d, assists=%d, cs=%d,  result='%s']", id, champion, opponent, kills, deaths,
				assists, cs, result);
	}
	
	public static int compare(Game a, Game b) {
		if(a.id == b.id) {
			return 0;
		}
		else if(a.id < b.id) {
			return 1;
		}
		else {
			return -1;
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getChampion() {
		return champion;
	}

	public void setChampion(String champion) {
		this.champion = champion;
	}

	public String getOpponent() {
		return opponent;
	}

	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public int getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public int getAssists() {
		return assists;
	}

	public void setAssists(int assists) {
		this.assists = assists;
	}

	public int getCs() {
		return cs;
	}

	public void setCs(int cs) {
		this.cs = cs;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	
}