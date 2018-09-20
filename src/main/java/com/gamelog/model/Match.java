package com.gamelog.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "match")
public class Match implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "gameid")
	private long gameId;
	
	@Column(name = "summonerid")
	private long summonerId;
	
	@Column(name = "champion")
	private String champion;

	@Column(name = "role")
	private String role;
	
	@Column(name = "lane")
	private String lane;
	
	@Column(name = "queue")
	private String queue;
	
	@Column(name = "season")
	private int season;
	
	@Column(name = "platformid")
	private String platformId;
	
	@Column(name = "timestamp")
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime timestamp;

	
	
	public Match() {
	}
	
	
	public Match(Long gameId, Long summonerId, String champion, String role, String lane, String queue, 
									int season, String platformId, DateTime timestamp) {
		this.gameId = gameId;
		this.summonerId = summonerId;
		this.champion = champion;
		this.role = role;
		this.lane = lane;
		this.queue = queue;
		this.season = season;
		this.platformId = platformId;
		this.timestamp = timestamp;
		
	}
	
	@Override
	public String toString() {
		return String.format("Match [gameId=%d, summonerId=%d, champion ='%s', role='%s', lane='%s', "
								+ "queue='%s', season=%d, platformId='%s', timeStamp='%s']", 
								gameId, summonerId, champion, role, lane, queue, season, platformId, timestamp);
	}
	

	public long getGameId() {
		return gameId;
	}

	
	public void setGameId(long gameId) {
		this.gameId = gameId;
	}

	
	public String getChampion() {
		return champion;
	}

	
	public void setChampion(String champion) {
		this.champion = champion;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public String getQueue() {
		return queue;
	}


	public void setQueue(String queue) {
		this.queue = queue;
	}


	public int getSeason() {
		return season;
	}


	public void setSeason(int season) {
		this.season = season;
	}


	public DateTime getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}

		
	
}