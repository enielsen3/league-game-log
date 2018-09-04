package com.gamelog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GamelogDbApplication /*implements CommandLineRunner*/ {

	/*
	@Autowired
	GameRepository repository;
	*/
	
	public static void main(String[] args) {
		SpringApplication.run(GamelogDbApplication.class, args);
	}
	
	
	/*
	@Override
	public void run(String... arg0) throws Exception {
		// clear all record if existed before do the tutorial with new data 
		repository.deleteAll();
	}
	*/
}
