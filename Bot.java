 

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * A simple and efficient client to run Stockfish from Java
 * 
 * @author Rahul A R
 * 
 */
public class Bot {

    private Process engineProcess;
    private BufferedReader processReader;
    private OutputStreamWriter processWriter;

    private String PATH;// = "D:/stockfish/stockfish";
    public Bot(String p)
    {
        PATH=p;
        if(!startEngine())
        {
            System.out.println("Something Wentwrong");
            System.exit(0);
        }
    }

    /**
     * Starts Stockfish engine as a process and initializes it
     * 
     * @param None
     * @return True on success. False otherwise
     */
    public boolean startEngine() {
        try {
            engineProcess = Runtime.getRuntime().exec(PATH);
            processReader = new BufferedReader(new InputStreamReader(
                    engineProcess.getInputStream()));
            processWriter = new OutputStreamWriter(
                    engineProcess.getOutputStream());
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    String getMove(String fen)
    {
        return  (getBestMove(fen,5000)+" ").substring(9,14); 
    }

    /**
     * Takes in any valid UCI command and executes it
     * 
     * @param command
     */
    public void sendCommand(String command) {
        try {
            processWriter.write(command + "\n");
            processWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is generally called right after 'sendCommand' for getting the raw
     * output from Stockfish
     * 
     * @param waitTime
     *            Time in milliseconds for which the function waits before
     *            reading the output. Useful when a long running command is
     *            executed
     * @return Raw output from Stockfish
     */
    public String getOutput(int waitTime) {
        //StringBuffer buffer = new StringBuffer();
        String text="";;
        try {
            Thread.sleep(waitTime);
            //sendCommand("isready");
            while (true) {
                text = processReader.readLine();
                //System.out.println(text);
                if (text.indexOf("bestmove")!=-1)
                    break;
                /*else
                    buffer.append(text + "\n");*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(buffer);
        return text;
    }

    /**
     * This function returns the best move for a given position after
     * calculating for 'waitTime' ms
     * 
     * @param fen
     *            Position string
     * @param waitTime
     *            in milliseconds
     * @return Best Move in PGN format
     */
    public String getBestMove(String fen, int waitTime) {
        sendCommand("position fen " + fen);
        sendCommand("go movetime " + waitTime);
        //getOutput(waitTime + 2000).split("bestmove ")[0].split(" ")[0];
        return getOutput(waitTime + 2000);
    }

    /**
     * Stops Stockfish and cleans up before closing it
     */
    public void stopEngine() {
        try {
            sendCommand("quit");
            processReader.close();
            processWriter.close();
        } catch (IOException e) {
        }
    }

    /**
     * Get a list of all legal moves from the given position
     * 
     * @param fen
     *            Position string
     * @return String of moves
     */
    public String getLegalMoves(String fen) {
        sendCommand("position fen " + fen);
        sendCommand("d");
        return getOutput(0).split("Legal moves: ")[0];
    }

    /**
     * Draws the current state of the chess board
     * 
     * @param fen
     *            Position string
     */
    public void drawBoard(String fen) {
        sendCommand("position fen " + fen);
        sendCommand("d");

        String[] rows = getOutput(0).split("\n");

        for (int i = 1; i < 18; i++) {
            System.out.println(rows[i]);
        }
    }

    /**
     * Get the evaluation score of a given board position
     * @param fen Position string
     * @param waitTime in milliseconds
     * @return evalScore
     */
    public float getEvalScore(String fen, int waitTime) {
        sendCommand("position fen " + fen);
        sendCommand("go movetime " + waitTime);

        float evalScore = 0.0f;
        String[] dump = getOutput(waitTime + 20).split("\n");
        for (int i = dump.length - 1; i >= 0; i--) {
            if (dump[i].startsWith("info depth ")) {
                try {
                evalScore = Float.parseFloat(dump[i].split("score cp ")[1]
                        .split(" nodes")[0]);
                } catch(Exception e) {
                    evalScore = Float.parseFloat(dump[i].split("score cp ")[1]
                            .split(" upperbound nodes")[0]);
                }
            }
        }
        return evalScore/100;
    }
}