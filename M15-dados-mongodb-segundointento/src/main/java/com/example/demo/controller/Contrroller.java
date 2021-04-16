package com.example.demo.controller;

import java.lang.reflect.Field;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.example.demo.model.Game;
import com.example.demo.model.Player;
import com.example.demo.repository.PlayerRepository;

//import org.springframework.data.rest.core.annotation.RepositoryRestResource;
@RestController("/home")
public class Contrroller {
	@Autowired
	private PlayerRepository playerDao;

	@GetMapping("/home")
	public String getHtml(Model model) {
		return "pages/index";
	}
	@GetMapping("/css")
	public String getCss() {
		return "pages/estilos.css";
	}
	@GetMapping("/main.js")
	public String getjs() {
		return "pages/main.js";
	}
	
	@PostMapping("/home/players")
	public ResponseEntity<Player> postPlayer(@RequestBody Player player){
		Player newplayer =playerDao.insert(player);
		return ResponseEntity.ok(newplayer);
	}
	@PutMapping("/home/players")
	public ResponseEntity<Player> modifyPlayer(@RequestBody Player player){
		Optional<Player> optionalPlayer= playerDao.findById(player.getId());
		if(optionalPlayer.isPresent()) {
			//playerDao.deleteById(player.getId());
			Player newPlayer= playerDao.save(player);
			return ResponseEntity.ok(newPlayer);
			
		}else {
			return ResponseEntity.noContent().build();
		}
	}
	@PostMapping("/home/players/{id}/games")
	public ResponseEntity <Game> newGame(@PathVariable Long id, @RequestBody Game game){
		Optional<Player> optionalPlayer= playerDao.findById(id);
		if(optionalPlayer.isPresent()) {
			//Game game =new Game();
			game.throwDice1();
			game.throwDice2();
			game.checkGame();
			Player newPlayer=optionalPlayer.get();
			List <Game> listGames=newPlayer.getGames();
			if(listGames ==null) {
				listGames = new ArrayList<Game>();
			}
			listGames.add(game);
			newPlayer.setGames(listGames);
			playerDao.save(newPlayer);
			return ResponseEntity.ok(game);
			
		}else {
			return ResponseEntity.noContent().build();
		}
	}
	@DeleteMapping("/home/players/{id}/games")
	public ResponseEntity<Player> deleteGames(@PathVariable Long id){
		Optional<Player> optionalPlayer= playerDao.findById(id);
		if(optionalPlayer.isPresent()) {
			Player pl1=optionalPlayer.get();
			List<Game> gamesList= pl1.getGames();
			gamesList.removeAll(pl1.getGames());
			pl1.setGames(gamesList);
			Player pl = playerDao.save(pl1);
			return ResponseEntity.ok(pl);
		}else {
			return ResponseEntity.noContent().build();
		}
	}
	@GetMapping("/home/players/{id}/games")
	public ResponseEntity<List <Game>> returnGames(@PathVariable Long id){
		Optional<Player> optionalPlayer= playerDao.findById(id);
		if(optionalPlayer.isPresent()) {
			Player newPlayer=playerDao.save(optionalPlayer.get());
			List <Game> listGames=newPlayer.getGames();
			return ResponseEntity.ok(listGames);
		}else {
			return ResponseEntity.noContent().build();
		}
	}
	public Double calculateSuccessProbability(Player player) {
		List<Game> games= player.getGames();
		if(games ==null) {
			return null;
		}else {
		Double numberGames=(double) games.size();
		Double goodOnes=0.000;
		for (Game game: games) {
			if(game.isSuccess()) goodOnes+=1.0;
		}
		return (100.0000*goodOnes/numberGames);
		}
	}
	
	@GetMapping("/home/players")
	public ResponseEntity<Map<String, Object>> getRanking(){
		Map<String, Object> result= new HashMap<String, Object>();
		Map<String, Object> result2= new HashMap<String, Object>();
		for(Player player: this.playerDao.findAll()) {
			result2.put("id", player.getId());
			result2.put("name", player.getName());
			result2.put("average", this.calculateSuccessProbability(player));
			result2.put("games", player.getGames());
			result.put(""+player.getId(),result2);
			result2=new HashMap<String,Object>();
		}
		return  ResponseEntity.ok(result);
	}
	
	public List<Player> getOrderedPlayers(){
		List<Player> listPlayers = new ArrayList<Player>();
		listPlayers.addAll(this.playerDao.findAll());
		List<Player> list2= listPlayers.stream()
					.filter(p -> ! (p.getGames() ==null))
					.filter(p -> ! p.getGames().isEmpty())
					.filter(p -> ! (this.calculateSuccessProbability(p) == null))
					.sorted(Comparator.comparingDouble(p -> calculateSuccessProbability(p)))
					//.filter(player -> ! this.calculateSuccessProbability(player).isNaN())
					.collect(Collectors.toList());
		return list2;
	}
	
	@GetMapping("/home/players/ranking")
	public ResponseEntity<Map<String, Object>> getOrderedRanking(){
		Map<String, Object> result= new HashMap<String, Object>();
		Map<String, Object> result1= new HashMap<String, Object>();
		for(Player player: this.getOrderedPlayers()) {
			result1.put("id", player.getId());
			result1.put("name", player.getName());
			result1.put("average", this.calculateSuccessProbability(player));
			result1.put("position", result.size()+1);
			result.put("average result"+player.getId(),result1);
			result1=new HashMap<String,Object>();
		}
		
		return  ResponseEntity.ok(result);
	}
	@GetMapping("/home/players/ranking/looser")
	public ResponseEntity<Map<String,Object>> getLooser(){
		Map<String, Object> result= new HashMap<String, Object>();
		Player player = this.getOrderedPlayers().get(0);
		result.put("name",player.getName());
		result.put("result", this.calculateSuccessProbability(player));
		return ResponseEntity.ok(result);
	}
	@GetMapping("/home/players/ranking/winner")
	public ResponseEntity<Map<String,Object>> getWinner(){
		Map<String, Object> result= new HashMap<String, Object>();
		Player player = this.getOrderedPlayers().get(this.getOrderedPlayers().size()-1);
		result.put("name",player.getName());
		result.put("result", this.calculateSuccessProbability(player));
		return ResponseEntity.ok(result);
	}
	

}
