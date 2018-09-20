package com.gamelog.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.gamelog.model.Match;

public interface MatchRepository extends PagingAndSortingRepository<Match, Long>{
	
	List<Match> findByChampion(String champion);
	
	Optional<Match> findById(Long id);

	List<Match> findAll();
	
	List<Match> findBySummonerId(Long id);
	
	
}
