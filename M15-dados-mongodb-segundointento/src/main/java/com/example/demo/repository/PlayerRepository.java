package com.example.demo.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Player;

@Repository
public interface PlayerRepository extends MongoRepository<Player,Long> {
	@Override
	public default Player insert(Player pl) {
		if (this.findById(pl.getId()).isEmpty()) {
			pl.setId(this.count()+1);
			this.save(pl);
		}else {
			Player newpl =this.findById(pl.getId()).get();
			this.findById(pl.getId()).get().setName(pl.getName());
			this.findById(pl.getId()).get().setGames(pl.getGames());
			pl =newpl;
		}
		return pl;
	}
}
