package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//@Document
public class Game {
	 public static enum dicePossibleValues{
		num1,num2,num3,num4,num5,num6,num7
	}
	 //@Id
	 //private long id;
	 private dicePossibleValues dice1;
	 private dicePossibleValues dice2;
	 private boolean success;
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public dicePossibleValues getDice1() {
		return dice1;
	}
	public void setDice1(dicePossibleValues dice1) {
		this.dice1 = dice1;
	}
	public dicePossibleValues getDice2() {
		return dice2;
	}
	public void setDice2(dicePossibleValues dice2) {
		this.dice2 = dice2;
	}
	
	public void throwDice1() {
		double a =Math.random();
		double interval=1.0/7.0;
		if(a<interval) {
			this.dice1= dicePossibleValues.num1;
		}
		else if(a>= interval && a<interval*2) {
			this.dice1= dicePossibleValues.num2;
		}
		else if(a>= interval*2 && a<interval*3) {
			this.dice1= dicePossibleValues.num3;
		}
		else if(a>= interval*3 && a<interval*4) {
			this.dice1= dicePossibleValues.num4;         
		}
		else if(a>= interval*4 && a<interval*5) {
			this.dice1= dicePossibleValues.num5;         
		}
		else if(a>= 5*interval && a<interval*6) {
			this.dice1= dicePossibleValues.num6;         
		}
		else if(a>= 6*interval) {
			this.dice1= dicePossibleValues.num7;                         
		}
	}                                                                                   
	public void throwDice2() {                                                          
		double a =Math.random();                                                        
		double interval=1.0/(dicePossibleValues.values().length);                       
		if(a<interval) this.dice2= dicePossibleValues.num1;                             
		if(a>= interval && a<interval*2) this.dice2= dicePossibleValues.num2;
		if(a>= interval*2 && a<interval*3) this.dice2= dicePossibleValues.num3;
		if(a>= interval*3 && a<interval*4) this.dice2= dicePossibleValues.num4;
		if(a>= interval*4 && a<interval*5) this.dice2= dicePossibleValues.num5;
		if(a>= 5*interval && a<interval*6) this.dice2= dicePossibleValues.num6;
		if(a>= 6*interval) this.dice2= dicePossibleValues.num7;
	}
	public void checkGame() {
		if (this.dice1 == this.dice2 && this.dice1== dicePossibleValues.num7) {
			success =true;
		}
		
	}
}
