package com.gamelog.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
import com.gamelog.model.Match;
import com.gamelog.model.Summoner;
import com.gamelog.repo.GameRepository;
import com.gamelog.repo.MatchRepository;
import com.gamelog.repo.SummonerRepository;


@RestController
@RequestMapping("/")
public class WebController {
	
	@Autowired
	GameRepository repository;
	
	@Autowired
	MatchRepository matchRepo;
	
	@Autowired
	SummonerRepository summonerRepo;
	
	@RequestMapping("/save")
	public String process() {
		// generates 1000 random game data for testing purposes
		Utilities.generateGames(1000, repository);
		return "Successfully created 1000 entries.";
	}
	
	@RequestMapping("/champs")
	public String champTest() {
		
		Map<Integer, String> champs = Utilities.getChampions();
		return champs.toString();
	}
	
	
	@GetMapping
	public ModelAndView list(@PageableDefault(size = 20) 
								@SortDefault (sort = "id", direction = Sort.Direction.DESC) 
								Pageable pageable) {
		
		Page<Game> page = this.repository.findAll(pageable);
		return new ModelAndView("games/list", "games", page);
	}
	
	/*
	@GetMapping
	public ModelAndView findSummoner(String name) {
		return new ModelAndView("summoner/form", "name", name);
	}
	
	@PostMapping
	public ModelAndView search(String name, BindingResult result, RedirectAttributes redirect) {
		if (result.hasErrors()) {
			return new ModelAndView("summoner/form", "formErrors", result.getAllErrors());
		}
		if(summonerRepo.findByName(name) == null) {
			com.gamelog.model.Summoner summoner = Utilities.getSummoner(name);
			summonerRepo.save(summoner);
		}
		else {
			Optional<com.gamelog.model.Summoner> summoner = summonerRepo.findByName(name);
		}
				
		redirect.addFlashAttribute("globalMessage", "Successfully created a new game");
		return new ModelAndView("redirect:/{game.id}", "game.id", game.getId());
	}
	*/
	
	@RequestMapping("/stats")
	public ModelAndView viewStats(@PageableDefault(size = 20) 
									@SortDefault (sort = "id", direction = Sort.Direction.DESC) 
									Pageable pageable) {
		
		Page<Game> page = this.repository.findAll(pageable);
		ModelAndView mav = new ModelAndView("games/stats", "games", page);
		
		List<Game> games = repository.findAll();
		
		if(!games.isEmpty()) {
			return Utilities.findStats(games, mav);
		}
		
		return mav;
				
	}
	
	
	@RequestMapping("/findbysummoner")
	public ModelAndView findBySummoner(@RequestParam("name") String name) {
		
		if(Utilities.checkSummonerName(name)) {
			Summoner summoner = Utilities.getSummoner(name, summonerRepo);	
			Utilities.getMatchHistory(summoner, matchRepo);
			List<Match> matchList = matchRepo.findBySummonerId(summoner.getId());
			matchList.sort(Comparator.comparing(Match::getGameId));
			Collections.reverse(matchList);
			ModelAndView mav = new ModelAndView("matches/list", "matches", matchList);
			return mav;
		}
		else {
			List<Match> matchList = null;
			return new ModelAndView("matches/list", "matches", matchList);
		}
		
	}
	
	
	@RequestMapping("/findbychampion")
	public ModelAndView findByChampion(@RequestParam("champion") String champion){
		List<Game> games = repository.findByChampion(champion);
		games.sort(Game::compare);
		ModelAndView mav = new ModelAndView("games/champion", "games", games);
		mav.addObject("champion", champion);
		
		if(!games.isEmpty()) {
			
			return Utilities.findStats(games, mav);
		}
		
		return mav;
	}
	
	
	
	@RequestMapping("/findbyopponent")
	public ModelAndView findByOpponent(@RequestParam("opponent") String opponent){
		List<Game> games = repository.findByOpponent(opponent);
		games.sort(Game::compare);
		ModelAndView mav = new ModelAndView("games/opponent", "games", games);
		mav.addObject("opponent", opponent);
		
		if(!games.isEmpty()) {
			
			return Utilities.findStats(games, mav);
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
	public ModelAndView delete(@PathVariable("id") Long id, 
								@PageableDefault(size = 20) 
								@SortDefault (sort = "id", direction = Sort.Direction.DESC) 
								Pageable pageable) {
		
		this.repository.deleteById(id);
		Iterable<Game> page = this.repository.findAll(pageable);
		
		return new ModelAndView("games/list", "games", page);
	}
	
	@GetMapping("modify/{id}")
	public ModelAndView modifyForm(@PathVariable("id") Game game) {
		return new ModelAndView("games/form", "game", game);
	}
	
	
}