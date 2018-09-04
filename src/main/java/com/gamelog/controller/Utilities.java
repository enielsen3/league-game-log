package com.gamelog.controller;

import java.util.List;
import java.util.Random;

import org.springframework.web.servlet.ModelAndView;

import com.gamelog.model.Game;
import com.gamelog.repo.GameRepository;

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
	
}