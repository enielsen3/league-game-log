package com.gamelog.controller;

import java.util.List;
import java.util.Random;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gamelog.model.Game;
import com.gamelog.repo.GameRepository;

@RestController
@RequestMapping("/")
public class WebController {
	
	@Autowired
	GameRepository repository;
	
	@RequestMapping("/save")
	public String process(){
		// generates random game data for testing purposes
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
		
		for(int i = 0; i < 1000; i++) {
			repository.save(new Game(champs[generator.nextInt(champs.length)], 
									champs[generator.nextInt(champs.length)], 
									generator.nextInt(20), 
									generator.nextInt(20), 
									generator.nextInt(25), 
									generator.nextInt(400), 
									results[generator.nextInt(results.length)]));
		}
		
		
				
		return "Successfully created 1000 entries.";
	}
	
	/*
	 * Just for testing Paging/sorting --- delete
	 
	@GetMapping("/findall")
	public String findAll(@PageableDefault(size = 20) 
							@SortDefault (sort = "id", direction = Sort.Direction.DESC) 
							Pageable pageable){
		
		Page<Game> page = this.repository.findAll(pageable);
		List<Game> games = page.getContent();
		String response = games.stream().map(Game::toString).collect(Collectors.joining("<br/>"));
		return response;
	}
	*/
	
	@GetMapping
	public ModelAndView list(@PageableDefault(size = 20) 
								@SortDefault (sort = "id", direction = Sort.Direction.DESC) 
								Pageable pageable) {
		
		Page<Game> page = this.repository.findAll(pageable);
		return new ModelAndView("games/list", "games", page);
	}
	
	/* 
	 * unused -- to be removed
	 * 
	@RequestMapping("/findbyid")
	public String findById(@RequestParam("id") long id){
		String result = "";
		result = repository.findById(id).toString();
		return result;
	}
	*/
	
	@RequestMapping("/stats")
	public ModelAndView viewStats(@PageableDefault(size = 20) 
									@SortDefault (sort = "id", direction = Sort.Direction.DESC) 
									Pageable pageable) {
		
		Page<Game> page = this.repository.findAll(pageable);
		ModelAndView mav = new ModelAndView("games/stats", "games", page);
		
		Iterable<Game> games = this.repository.findAll();
		List<Game> gamesList = (List<Game>) games;
		
		if(!gamesList.isEmpty()) {
					
			long totalGames = gamesList.stream().count();
			long wins = gamesList.stream().filter(game -> game.getResult().equals("Win")).count();
			double winPercent = 100*wins*1.0/totalGames;
			double avgKills = gamesList.stream().mapToInt(Game::getKills).average().getAsDouble();
			double avgDeaths = gamesList.stream().mapToInt(Game::getDeaths).average().getAsDouble();
			double avgAssists = gamesList.stream().mapToInt(Game::getAssists).average().getAsDouble();
			double avgCS = gamesList.stream().mapToInt(Game::getCs).average().getAsDouble();
			
			mav.addObject("total", totalGames);
			mav.addObject("winPercent", winPercent);
			mav.addObject("avgKills", avgKills);
			mav.addObject("avgDeaths", avgDeaths);
			mav.addObject("avgAssists", avgAssists);
			mav.addObject("avgCS", avgCS);
		}
		
		return mav;
	}
	
	
	@RequestMapping("/findbychampion")
	public ModelAndView findByChampion(@RequestParam("champion") String champion){
		Iterable<Game> games = repository.findByChampion(champion);
		List<Game> gamesList = (List<Game>) games;
		gamesList.sort(Game::compare);
		ModelAndView mav = new ModelAndView("games/champion", "games", gamesList);
		
		if(!gamesList.isEmpty()) {
			
			long totalGames = gamesList.stream().count();
			long wins = gamesList.stream().filter(game -> game.getResult().equals("Win")).count();
			double winPercent = 100*wins*1.0/totalGames;
			double avgKills = gamesList.stream().mapToInt(Game::getKills).average().getAsDouble();
			double avgDeaths = gamesList.stream().mapToInt(Game::getDeaths).average().getAsDouble();
			double avgAssists = gamesList.stream().mapToInt(Game::getAssists).average().getAsDouble();
			double avgCS = gamesList.stream().mapToInt(Game::getCs).average().getAsDouble();
			
			mav.addObject("champion", champion);
			mav.addObject("total", totalGames);
			mav.addObject("winPercent", winPercent);
			mav.addObject("avgKills", avgKills);
			mav.addObject("avgDeaths", avgDeaths);
			mav.addObject("avgAssists", avgAssists);
			mav.addObject("avgCS", avgCS);
		}
		
		return mav;
	}
	
		
	@GetMapping(params = "form")
	public ModelAndView createForm(@ModelAttribute Game game) {
		return new ModelAndView("games/form", "game", game);
	}
	
	@PostMapping
	public ModelAndView create(@Valid Game game, BindingResult result, RedirectAttributes redirect) {
		if (result.hasErrors()) {
			return new ModelAndView("games/form", "formErrors", result.getAllErrors());
		}
		game = this.repository.save(game);
		redirect.addFlashAttribute("globalMessage", "Successfully created a new game");
		return new ModelAndView("redirect:/{game.id}", "game.id", game.getId());
	}
	
	@GetMapping("{id}")
	public ModelAndView view(@PathVariable("id") Game game) {
		return new ModelAndView("games/view", "game", game);
	}
	
	@GetMapping("delete/{id}")
	public ModelAndView delete(@PathVariable("id") Long id) {
		this.repository.deleteById(id);
		Iterable<Game> games = this.repository.findAll();
		((List<Game>) games).sort(Game::compare);
		return new ModelAndView("games/list", "games", games);
	}
	
	@GetMapping("modify/{id}")
	public ModelAndView modifyForm(@PathVariable("id") Game game) {
		return new ModelAndView("games/form", "game", game);
	}
	
	
}