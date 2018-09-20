package com.gamelog.controller;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import org.joda.time.DateTime;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.gamelog.model.Game;
import com.gamelog.model.Match;
import com.gamelog.model.Summoner;
import com.gamelog.repo.GameRepository;
import com.gamelog.repo.MatchRepository;
import com.gamelog.repo.SummonerRepository;


public class Utilities {
	
		
	public static void generateGames(int number, GameRepository repository) {
		
	
		String[] champs = {"Aatrox","Ekko","Jinx","Miss Fortune", "Shen", "Varus",
							"Ahri", "Elise", "Kalista", "Mordekaiser", "Shyvana", "Vayne",
							"Akali", "Evelynn", "Karma", "Morgana", "Singed", "Veigar",
							"Alistar", "Ezreal", "Karthus", "Nami", "Sion", "Vel’Koz",
							"Amumu", "Fiddlesticks", "Kassadin", "Nasus", "Sivir", "Vi",
							"Anivia", "Fiora", "Katarina", "Nautilus", "Skarner", "Viktor",
							"Annie", "Fizz", "Kayle", "Nidalee", "Sona", "Vladimir",
							"Ashe", "Galio", "Kennen", "Nocturne", "Soraka", "Volibear",
							"Aurelion Sol", "Gangplank", "Kha’Zix", "Nunu", "Swain", "Warwick",
							"Azir", "Garen", "Kindred", "Olaf", "Syndra", "Wukong",
							"Bard", "Gnar", "Kled", "Orianna", "Tahm Kench", "Xerath",
							"Blitzcrank", "Gragas", "Kog’Maw", "Pantheon", "Taliyah", "Zin Zhao",
							"Brand", "Graves", "LeBlanc", "Poppy", "Talon", "Yasuo",
							"Braum", "Hecarim", "Lee Sin", "Quinn", "Taric", "Yorick",
							"Caitlyn", "Heimerdinger", "Leona", "Rammus", "Teemo", "Zac",
							"Camille", "Illaoi", "Lissandra", "Rek’Sai", "Thresh", "Zed",
							"Cassiopeia", "Irelia", "Lucian", "Renekton", "Tristana", "Ziggs",
							"Cho’Gath", "Ivern", "Lulu", "Rengar", "Trundle", "Zilean",
							"Corki", "Janna", "Lux", "Riven", "Tryndamere", "Zyra",
							"Darius", "Jarvan IV", "Malphite", "Rumble", "Twisted Fate", 
							"Diana", "Jax", "Malzahar", "Ryze", "Twitch",
							"Dr. Mundo", "Jayce", "Maokai", "Sejuani", "Udyr",
							"Draven", "Jhin", "Master Yi", "Shaco", "Urgot"};
		
		String[] results = {"Win", "Loss"};
		
		Random generator = new Random();
		
		for(int i = 0; i < number; i++) {
			repository.save(new Game(champs[generator.nextInt(champs.length)], 
									champs[generator.nextInt(champs.length)], 
									generator.nextInt(20), 
									generator.nextInt(10), 
									generator.nextInt(25), 
									generator.nextInt(400), 
									results[generator.nextInt(results.length)]));
		}
	}
	
	public static ModelAndView findStats(List<Game> games, ModelAndView mav) {
		
		long totalGames = games
				.stream()
				.count();
		
		long wins = games
				.stream()
				.filter(game -> game.getResult()
				.equals("Win"))
				.count();
		
		double winPercent = 100 * wins*1.0/totalGames;
		
		double avgKills = games
				.stream()
				.mapToInt(Game::getKills)
				.average()
				.getAsDouble();
		
		double avgDeaths = games
				.stream()
				.mapToInt(Game::getDeaths)
				.average()
				.getAsDouble();
		
		double avgAssists = games
				.stream()
				.mapToInt(Game::getAssists)
				.average().getAsDouble();
		
		double avgCS = games
				.stream()
				.mapToInt(Game::getCs)
				.average()
				.getAsDouble();
		
		mav.addObject("total", totalGames);
		mav.addObject("winPercent", winPercent);
		mav.addObject("avgKills", avgKills);
		mav.addObject("avgDeaths", avgDeaths);
		mav.addObject("avgAssists", avgAssists);
		mav.addObject("avgCS", avgCS);
		
		return mav;
	}
	
	public static Boolean checkSummonerName(String name) {
		
		return name.matches("^[0-9\\p{L} _\\.]+$");
	}
	
	public static Summoner getSummoner(String name, SummonerRepository repo) {
		
		if(repo.findByName(name) == null) {
			
			final String uri = "https://na1.api.riotgames.com/lol/summoner/v3/summoners/by-name/" + name.toLowerCase();
		     
			RestTemplate restTemplate = new RestTemplate();
			     
			HttpHeaders headers = new HttpHeaders();
			
			String riotAPIkey = System.getenv("RIOT_API_KEY");
			
			headers.add("Accept-Charset", "application/x-www-form-urlencoded; charset=UTF-8");
			headers.add("X-Riot-Token", riotAPIkey);
			headers.add("Accept-Language", "en-US,en;q=0.9");
			
			HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
			
			System.out.println("Making GET API call to: " + uri);
			ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
			
			//System.out.println(result.getStatusCodeValue());
			JsonReader jsonReader = Json.createReader(new StringReader(result.getBody().toString()));
			JsonObject resultJSON = jsonReader.readObject();
			jsonReader.close();
				
			long accountId = resultJSON.getInt("accountId");
			long summonerId = resultJSON.getInt("id");
			String summonerName = resultJSON.getString("name").toLowerCase();
			int profileIconId = resultJSON.getInt("profileIconId");
			DateTime revisionDate = new DateTime(resultJSON.getJsonNumber("revisionDate").longValueExact());
			int summonerLevel = resultJSON.getInt("summonerLevel");
				
			Summoner summoner = new Summoner(accountId, summonerId, summonerName, 
													profileIconId, summonerLevel, revisionDate);
			repo.save(summoner);
			return summoner;		
			
		}
		
		else {
			Summoner summoner = repo.findByName(name.toLowerCase());
			return summoner;
		}
 
	}
	
	
	public static void getMatchHistory(Summoner summoner, MatchRepository repo) {
		
		final String uri = "https://na1.api.riotgames.com/lol/match/v3/matchlists/by-account/" + summoner.getId();
		
		final Map<Integer, String> championsToId = getChampions();
		
		RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders headers = new HttpHeaders();
		String riotAPIkey = System.getenv("RIOT_API_KEY");
		
		headers.add("Accept-Charset", "application/x-www-form-urlencoded; charset=UTF-8");
		headers.add("X-Riot-Token", riotAPIkey);
		headers.add("Accept-Language", "en-US,en;q=0.9");
		
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		
		System.out.println("Making GET API call to: " + uri);
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		
		JsonReader jsonReader = Json.createReader(new StringReader(result.getBody().toString()));
		JsonObject resultJSON = jsonReader.readObject();
		jsonReader.close();
		
		//int totalGames = resultJSON.getInt("totalGames");
		int startIndex = resultJSON.getInt("startIndex");
		int endIndex = resultJSON.getInt("endIndex");
		JsonArray array = resultJSON.getJsonArray("matches");
		
		for(int i = 0; i < (endIndex - startIndex); i++) {
			
			JsonObject matchJSON = array.get(i).asJsonObject();
			
			String lane = matchJSON.getString("lane");
			long gameId = matchJSON.getJsonNumber("gameId").longValueExact();
			int championId = matchJSON.getInt("champion");
			String platformId = matchJSON.getString("platformId");
			int season = matchJSON.getInt("season");
			int queue = matchJSON.getInt("queue");
			String role = matchJSON.getString("role");
			DateTime timeStamp = new DateTime(matchJSON.getJsonNumber("timestamp").longValueExact());
			
			Match match = new Match(gameId, summoner.getId(), championsToId.get(championId), role, lane, 
										Integer.toString(queue), season, platformId, timeStamp);
			repo.save(match);
		}
		
	}
	
	public static Map<Integer, String> getChampions() {
		
		final String uri = "http://ddragon.leagueoflegends.com/cdn/8.18.2/data/en_US/champion.json";
		RestTemplate restTemplate = new RestTemplate();	    
	    HttpEntity<String> entity = new HttpEntity<String>(null, null);
	    
	    System.out.println("Making GET API call to: " + uri);
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		JsonReader jsonReader = Json.createReader(new StringReader(result.getBody().toString()));
		JsonObject resultJSON = jsonReader.readObject();
		jsonReader.close();
		
		JsonValue data = resultJSON.get("data");
		Set<String> ids = data.asJsonObject().keySet();
		
		Map<Integer, String> championMap = new HashMap<>();
		
		for(String id : ids) {
			JsonValue champion = data.asJsonObject().get(id);
			Integer key = Integer.parseInt(champion.asJsonObject().getString("key"));
			String name = champion.asJsonObject().getString("name");
			championMap.put(key, name);
		}
		
		return championMap;
	}
	
	public static Map<Integer, String> getSeasons() {
		
		Map<Integer, String> seasons = new HashMap<>();
			seasons.put(0, "Preseason 3");
			seasons.put(1, "Season 3");
			seasons.put(2, "Preseason 2014");
			seasons.put(3, "Season 2014");
			seasons.put(4, "Preseason 2015");
			seasons.put(5, "Season 2015");
			seasons.put(6, "Preseason 2016");
			seasons.put(7, "Season 2016");
			seasons.put(8, "Preseason 2017");
			seasons.put(9, "Season 2017");
			seasons.put(10, "Preseason 2018");
			seasons.put(11, "Season 2018");
		
		return seasons;
					
	}
	
	public static Map<Integer, String> getQueues() {
		
		Map<Integer, String> queues = new HashMap<>();
			queues.put(400, "5v5 Draft Pick games");
			queues.put(420, "5v5 Ranked Solo games");
			
		return queues;
	}
		
	  
	        
}
	
	
	
	
