package com.xpsurgery.trivia.runner;

import com.xpsurgery.trivia.uglytrivia.Game;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class GameRunnerTest {

    private static boolean winner;

//    public static void main(String[] args) throws FileNotFoundException {
//        runGame1000Times(new PrintStream(new FileOutputStream("fixture.txt", false)));
//    }


    @Test
    public void shouldReturnCorrectOutputFor1000Runs() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintStream printStream = new PrintStream(baos)) {
            runGame1000Times(printStream);

            File file = new File("fixture.txt");

            try(BufferedReader fis = new BufferedReader(new FileReader(file))) {
                BufferedReader gameReader = new BufferedReader(new StringReader(new String(baos.toByteArray())));

                String fixtureLine = fis.readLine();
                String gameReaderLine = gameReader.readLine();


                while (fixtureLine != null) {
                    System.out.println(fixtureLine);
                    assertEquals(fixtureLine, gameReaderLine);
                    fixtureLine = fis.readLine();
                    gameReaderLine = gameReader.readLine();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                fail("FAILLLLL");
            }

        } finally {
        }
    }

    @Test
    public void correctWinnerFirstGame() throws IOException {
        String result = "Winner = Sue, purses = 6";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        runGame(1, printStream);
        BufferedReader gameReader = new BufferedReader(new StringReader(new String(baos.toByteArray())));

        String lastLine = "";

        String gameReaderLine = gameReader.readLine();
        while (gameReaderLine != null) {
            lastLine = gameReaderLine;
            gameReaderLine = gameReader.readLine();
        }

        assertEquals(result, lastLine);

    }

    private static void runGame1000Times(PrintStream printStream) {
        runGame(1000, printStream);
    }

    private static void runGame(int numberOfRuns, PrintStream printStream) {
        try (PrintStream outputStream = printStream) {
            for (int seed = 0; seed < numberOfRuns; seed++) {
                Game aGame = new Game(outputStream);
                aGame.add("Chet");
                aGame.add("Pat");
                aGame.add("Sue");
                Random rand = new Random(seed);
                do {
                    aGame.roll(rand.nextInt(5) + 1);
                    if (rand.nextInt(9) == 7) {
                        winner = aGame.wrongAnswer();
                    } else {
                        winner = aGame.wasCorrectlyAnswered();
                    }
                } while (!winner);
            }

        } finally {
        }
    }
}
