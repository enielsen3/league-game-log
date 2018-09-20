package com.gamelog.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.gamelog.model.Summoner;

public interface SummonerRepository extends CrudRepository<Summoner, Long>{
	
	Optional<Summoner> findById(Long id);
	
	Summoner findByName(String name);

	List<Summoner> findAll();
	
	
}
