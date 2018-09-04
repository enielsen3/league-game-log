package com.gamelog.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.gamelog.model.Game;

public interface GameRepository extends PagingAndSortingRepository<Game, Long>{
	List<Game> findByChampion(String champion);
	
	Optional<Game> findById(Long id);
	
	
}
