package Games;

import java.util.*;

public class TicTacToe {

	static String[] board;
	static String turn;


	// CheckWinner method will
	// decide the combination
	// of three box given below.
				/**
				 * Checks for a winner in the Tic Tac Toe game board.
				 *
				 * @return "X" if player X wins, "O" if player O wins, "draw" if the game is a draw, or null if the game is ongoing.
				 * @throws IllegalArgumentException if the game board is not properly initialized
 				*/
	static String checkWinner()
	{
		for (int a = 0; a < 8; a++) {
			String line = null;

			switch (a) {
				case 0:
					line = board[0] + board[1] + board[2];
					break;
				case 1:
					line = board[3] + board[4] + board[5];
					break;
				case 2:
					line = board[6] + board[7] + board[8];
					break;
				case 3:
					line = board[0] + board[3] + board[6];
					break;
				case 4:
					line = board[1] + board[4] + board[7];
					break;
				case 5:
					line = board[2] + board[5] + board[8];
					break;
				case 6:
					line = board[0] + board[4] + board[8];
					break;
				case 7:
					line = board[2] + board[4] + board[6];
					break;
			}
			//For X winner
			if (line.equals("XXX")) {
				return "X";
			}

			// For O winner
			else if (line.equals("OOO")) {
				return "O";
			}
		}

		for (int a = 0; a < 9; a++) {
			if (Arrays.asList(board).contains(
					String.valueOf(a + 1))) {
				break;
			}
			else if (a == 8) {
				return "draw";
			}
		}

		// To enter the X Or O at the exact place on board.
		System.out.println(turn + "'s turn; enter a slot number to place " + turn + " in:");
		return null;
	}

	// To print out the board.
	/* |---|---|---|
	| 1 | 2 | 3 |
	|-----------|
	| 4 | 5 | 6 |
	|-----------|
	| 7 | 8 | 9 |
	|---|---|---|*/

	/**
	 * Prints the Tic-Tac-Toe board to the console.
	 *
	 * This method prints the current state of the Tic-Tac-Toe board to the console.
	 *
	 * @throws ArrayIndexOutOfBoundsException if the board array does not have at least 9 elements
 	*/
	static void printBoard()
	{
		System.out.println("|---|---|---|");
		System.out.println("| " + board[0] + " | "+ board[1] + " | " + board[2] + " |");
		System.out.println("|-----------|");
		System.out.println("| " + board[3] + " | "+ board[4] + " | " + board[5] + " |");
		System.out.println("|-----------|");
		System.out.println("| " + board[6] + " | "+ board[7] + " | " + board[8]
				+ " |");
		System.out.println("|---|---|---|");
	}

	/**
	 * This method starts a 3x3 Tic Tac Toe game and allows two players to take turns placing their marks on the board.
	 * It handles user input, checks for valid moves, and determines the winner or a draw.
	 *
	 * @throws InputMismatchException if the user enters an invalid input that is not in the range from 1 to 9.
 	*/
	public static void startTicTacToe()
	{
		Scanner in = new Scanner(System.in);
		board = new String[9];
		turn = "X";
		String winner = null;

		for (int a = 0; a < 9; a++) {
			board[a] = String.valueOf(a + 1);
		}

		System.out.println("Welcome to 3x3 Tic Tac Toe.");
		printBoard();

		System.out.println("X will play first. Enter a slot number to place X in:");

		while (winner == null) {
			int numInput;

			// Exception handling.
			// numInput will take input from user like from 1 to 9.
			// If it is not in range from 1 to 9.
			// then it will show you an error "Invalid input."
			try {
				numInput = in.nextInt();
				if (!(numInput > 0 && numInput <= 9)) {
					System.out.println("Invalid input; re-enter slot number:");
					continue;
				}
			}
			catch (InputMismatchException e) {
				System.out.println("Invalid input; re-enter slot number:");
				continue;
			}

			// This game has two player x and O.
			// Here is the logic to decide the turn.
			if (board[numInput - 1].equals(
					String.valueOf(numInput))) {
				board[numInput - 1] = turn;

				if (turn.equals("X")) {
					turn = "O";
				}
				else {
					turn = "X";
				}

				printBoard();
				winner = checkWinner();
			}
			else {
				System.out.println("Slot already taken; re-enter slot number:");
			}
		}

		// If no one win or lose from both player x and O.
		// then here is the logic to print "draw".
		if (winner.equalsIgnoreCase("draw")) {
			System.out.println("It's a draw! Thanks for playing.");
		}

		// For winner -to display Congratulations! message.
		else {
			System.out.println(
					"Congratulations! " + winner + "'s have won! Thanks for playing.");
		}
	}
}