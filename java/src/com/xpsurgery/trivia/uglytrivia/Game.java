package com.xpsurgery.trivia.uglytrivia;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;

public class Game {
  ArrayList players = new ArrayList();
  int[] places = new int[6];
  int[] purses  = new int[6];
  boolean[] inPenaltyBox  = new boolean[6];
  LinkedList popQuestions = new LinkedList();
  LinkedList scienceQuestions = new LinkedList();
  LinkedList sportsQuestions = new LinkedList();
  LinkedList rockQuestions = new LinkedList();
  int currentPlayer = 0;
  boolean isGettingOutOfPenaltyBox;
  private PrintStream outputStream;


  public  Game(PrintStream outStream){
    this.outputStream = outStream;
  	for (int i = 0; i < 50; i++) {
      popQuestions.addLast("Pop Question " + i);
      scienceQuestions.addLast(("Science Question " + i));
      sportsQuestions.addLast(("Sports Question " + i));
      rockQuestions.addLast(createRockQuestion(i));
    }
  }

	public String createRockQuestion(int index){
		return "Rock Question " + index;
	}

	public boolean isPlayable() {
		return (howManyPlayers() >= 2);
	}

	public boolean add(String playerName) {
    players.add(playerName);
    places[howManyPlayers()] = 0;
    purses[howManyPlayers()] = 0;
    inPenaltyBox[howManyPlayers()] = false;
		return true;
	}

	public int howManyPlayers() {
		return players.size();
	}

	public void roll(int roll) {
		if (inPenaltyBox[currentPlayer]) {
			if (roll % 2 != 0) {
				isGettingOutOfPenaltyBox = true;
				places[currentPlayer] = places[currentPlayer] + roll;
				if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12;
				askQuestion();
			} else {
				isGettingOutOfPenaltyBox = false;
      }
		} else {
			places[currentPlayer] = places[currentPlayer] + roll;
			if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12;
			askQuestion();
		}
	}

	private void askQuestion() {
		if (currentCategory() == "Pop")
			outputStream.println(popQuestions.removeFirst());
		if (currentCategory() == "Science")
			outputStream.println(scienceQuestions.removeFirst());
		if (currentCategory() == "Sports")
			outputStream.println(sportsQuestions.removeFirst());
		if (currentCategory() == "Rock")
			outputStream.println(rockQuestions.removeFirst());
	}

	private String currentCategory() {
		if (places[currentPlayer] == 0) return "Pop";
		if (places[currentPlayer] == 4) return "Pop";
		if (places[currentPlayer] == 8) return "Pop";
		if (places[currentPlayer] == 1) return "Science";
		if (places[currentPlayer] == 5) return "Science";
		if (places[currentPlayer] == 9) return "Science";
		if (places[currentPlayer] == 2) return "Sports";
		if (places[currentPlayer] == 6) return "Sports";
		if (places[currentPlayer] == 10) return "Sports";
		return "Rock";
	}

	public boolean wasCorrectlyAnswered() {
		if (inPenaltyBox[currentPlayer]){
			if (isGettingOutOfPenaltyBox) {
				purses[currentPlayer]++;
				boolean winner = didPlayerWin();
				if (winner) {
					outputStream.println("Winner = " + players.get(currentPlayer) + ", purses = " + purses[currentPlayer]);
				}
				nextPlayer();
				return winner;
			} else {
				nextPlayer();
				return false;
			}
		} else {
			purses[currentPlayer]++;
			boolean winner = didPlayerWin();
			if (winner) {
				outputStream.println("Winner = " + players.get(currentPlayer) + ", purses = " + purses[currentPlayer]);
			}
			nextPlayer();
			return winner;
		}
	}

	private void nextPlayer() {
		currentPlayer++;
		if (currentPlayer == players.size()) currentPlayer = 0;
	}

	public boolean wrongAnswer(){
		inPenaltyBox[currentPlayer] = true;
		nextPlayer();
		return false;
	}

	private boolean didPlayerWin() {
		return (purses[currentPlayer] == 6);
	}
}

