package com.gamelog.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;


@Entity
@Table(name = "summoner")
public class Summoner implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	//riot -- accountId
	private long id;
	
	@Column(name = "summonerid")
	private long summonerId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "icon")
	private int icon;
	
	@Column(name = "level")
	private int level;
	
	@Column(name = "revdate")
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime revdate;
	
	public Summoner() {
		
	}
	
	public Summoner(long id, long summonerId, String name, int icon, int level, DateTime revdate) {
		this.id = id;
		this.summonerId = summonerId;
		this.name = name;
		this.icon = icon;
		this.level = level;
		this.revdate = revdate;
	}
	
	@Override
	public String toString() {
		return String.format("Summoner [accountid =%d, summonerid =%d, name ='%s', icon =%d, "
				+ "level =%d, revdate=%s]", id, summonerId, name, icon, level,
				revdate);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSummonerId() {
		return summonerId;
	}

	public void setSummonerId(long summonerId) {
		this.summonerId = summonerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public long getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public DateTime getRevdate() {
		return revdate;
	}

	public void setRevdate(DateTime revdate) {
		this.revdate = revdate;
	}
	
	
	
	
	
	
}