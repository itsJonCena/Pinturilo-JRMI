package Server;

import java.net.Socket;

/**
 * Created by Alex on 06/03/17.
 */
public class Player {
    private String name;


    private int score;

    /**
     * talvez lo utilice
     */
    private Socket socketChat;
    private Socket socketCanvas;
    private Socket socketInstrucciones;

    public Player(){

    }

    public Player(String name, int score){
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score += score;
    }

}
