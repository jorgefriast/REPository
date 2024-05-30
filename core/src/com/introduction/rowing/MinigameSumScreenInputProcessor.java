package com.introduction.rowing;

public class MinigameSumScreenInputProcessor extends InputProcessor {
    private String next;
    public MinigameSumScreenInputProcessor(MyRowing myRowing, String nextGame) {
        super(myRowing);
        this.next = nextGame;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (next == "game")
            setGameState(GameState.PLAY_GAME);
        else if (next == "final-game")
            setGameState(GameState.FINAL_GAME);
        return false;
    }
}
