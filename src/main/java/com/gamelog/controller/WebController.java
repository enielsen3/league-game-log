package com.gamelog.controller;

import java.util.List;
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
	public String process() {
		// generates 1000 random game data for testing purposes
		Utilities.generateGames(1000, repository);
		return "Successfully created 1000 entries.";
	}
	
	@GetMapping
	public ModelAndView list(@PageableDefault(size = 20) 
								@SortDefault (sort = "id", direction = Sort.Direction.DESC) 
								Pageable pageable) {
		
		Page<Game> page = this.repository.findAll(pageable);
		return new ModelAndView("games/list", "games", page);
	}
	
	@RequestMapping("/stats")
	public ModelAndView viewStats(@PageableDefault(size = 20) 
									@SortDefault (sort = "id", direction = Sort.Direction.DESC) 
									Pageable pageable) {
		
		Page<Game> page = this.repository.findAll(pageable);
		ModelAndView mav = new ModelAndView("games/stats", "games", page);
		Iterable<Game> games = this.repository.findAll();
		List<Game> gamesList = (List<Game>) games;
		
		if(!gamesList.isEmpty()) {
			return Utilities.findStats(gamesList, mav);
		}
		
		return mav;
				
	}
	
	@RequestMapping("/findbychampion")
	public ModelAndView findByChampion(@RequestParam("champion") String champion){
		Iterable<Game> games = repository.findByChampion(champion);
		List<Game> gamesList = (List<Game>) games;
		gamesList.sort(Game::compare);
		ModelAndView mav = new ModelAndView("games/champion", "games", gamesList);
		mav.addObject("champion", champion);
		
		if(!gamesList.isEmpty()) {
			
			return Utilities.findStats(gamesList, mav);
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