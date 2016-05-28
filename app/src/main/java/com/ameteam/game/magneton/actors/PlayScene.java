package com.ameteam.game.magneton.actors;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.ameteam.game.magneton.MagnetonGame;
import com.ameteam.game.magneton.R;
import com.ameteam.game.magneton.ai.GameAI;
import com.ameteam.game.magneton.ai.GameAIEasy;
import com.ameteam.game.magneton.ai.GameAIHard;
import com.ameteam.game.magneton.ai.GameAIMedium;
import com.ameteam.game.magneton.ai.GameRules;
import com.ameteam.game.magneton.ai.GameState;

/**
 * Created by edson on 5/03/2016.
 */
public class PlayScene extends Scene {

    private Board board;
    private GameRules gameRules;
    private GameAI gameAI;
    private boolean is2Players;

    private int state; //GameState: TURN_PLAYER, TURN_MACHINE, TURN_WAITING

    public PlayScene(MagnetonGame magnetonGame){
        super(magnetonGame);
        board = new Board(this, GameRules.DIMENSION_8x8);
        state = 0;
    }

    @Override
    public void init() {
        super.init();
    }

    public void initGame(){

        state = GameState.TURN_PLAYER;

        board.init();
        board.resize();
        gameRules = new GameRules(magnetonGame.getDimension());
        is2Players = magnetonGame.getLevel() == MagnetonGame.LEVEL_2_PLAYERS;

        switch (magnetonGame.getLevel()){
            case MagnetonGame.LEVEL_EASY:
                gameAI = new GameAIEasy(gameRules);
                break;
            case MagnetonGame.LEVEL_MEDIUM:
                gameAI = new GameAIMedium(gameRules);
                break;
            case MagnetonGame.LEVEL_HARD:
                gameAI = new GameAIHard(gameRules);
                break;
            case MagnetonGame.LEVEL_2_PLAYERS:
                gameAI = null;
                break;
            default:
                gameAI = new GameAIEasy(gameRules);
                break;
        }
        if(gameAI != null) {
            gameAI.setBoard(board);
        }
    }

    @Override
    public void doDraw(Canvas canvas) {
        if(canvas == null) return;

        //Scene
        Paint paintScene = new Paint();
        paintScene.setColor(getResources().getColor(R.color.ply_scn_background));
        canvas.drawRect(x, y, x + getWidth(), y + getHeight(), paintScene);

        //Board
        board.doDraw(canvas);

        switch (state){
            case GameState.WIN_PLAYER:
                drawWinOrDraw(canvas, getResources().getString(R.string.ply_scn_win_player));
                break;
            case GameState.WIN_MACHINE:
                drawWinOrDraw(canvas, getResources().getString(R.string.ply_scn_win_machine));
                break;
            case GameState.DRAW:
                drawWinOrDraw(canvas, getResources().getString(R.string.ply_scn_draw));
        }

        drawTurnSection(canvas, getResources().getString(R.string.ply_scn_turn_player), GameState.TURN_PLAYER);
        if(is2Players) {
            canvas.scale(-1, -1);
            canvas.translate(-(2 * x + board.getWidth()), -(2 * y + board.getHeight() / 8));
        }
        drawTurnSection(canvas,
                !is2Players ?   getResources().getString(R.string.ply_scn_turn_machine) :
                                getResources().getString(R.string.ply_scn_turn_player)
                , GameState.TURN_MACHINE);
        if(is2Players) {
            canvas.translate(2 * x + board.getWidth(), 2 * y + board.getHeight() / 8);
            canvas.scale(-1, -1);
        }

    }

    @Override
    public void update(float secondsElapsed) {
        board.update(secondsElapsed);
    }

    public void changeTurn(){
        if(state == GameState.TURN_PLAYER){
            state = GameState.TURN_MACHINE;
        }else if(state == GameState.TURN_MACHINE){
            state = GameState.TURN_PLAYER;
        }

        Log.i("PlayScene", "Turn: " + state);

        GameState gameState = new GameState(gameRules);
        gameState.setMatrix(board.getMatrix());
        gameState.setTurn(state);
        gameState.setRestPiecesPlayer(gameRules.getMaxPieces() - board.getCountPiecesPlayer());
        gameState.setRestPiecesMachine(gameRules.getMaxPieces() - board.getCountPiecesMachine());

        validateGameState(gameState);

        /**
         * Change for preActionMachine
         */
        /**if(state == GameState.TURN_MACHINE && !is2Players){
            gamaAIAction(gameState);
        }**/
    }

    private void validateGameState(GameState gameState){
        int objectiveFunction = gameRules.objetiveFunction(gameState);
        if(objectiveFunction != GameRules.GAME_WIN_NOBODY){
            switch (objectiveFunction){
                case GameRules.GAME_WIN_PLAYER:
                    state = GameState.WIN_PLAYER;
                    break;
                case GameRules.GAME_WIN_MACHINE:
                    state = GameState.WIN_MACHINE;
                    break;
                case GameRules.GAME_DRAW:
                    state = GameState.DRAW;
            }
        }
    }

    public void preActionMachine(int pi, int pj){
        if(state == GameState.TURN_PLAYER && !is2Players){

            GameState gameState = new GameState(gameRules);
            int [][] matrix = board.getMatrix();
            GameState.putPiece(gameRules.getDimension(), matrix, pi, pj, GameState.TURN_PLAYER);
            gameState.setMatrix(matrix);
            gameState.setTurn(GameState.TURN_MACHINE);
            gameState.setRestPiecesPlayer(gameRules.getMaxPieces() - (board.getCountPiecesPlayer() - 1));
            gameState.setRestPiecesMachine(gameRules.getMaxPieces() - board.getCountPiecesMachine());

            gamaAIAction(gameState);
        }
    }

    private void gamaAIAction(GameState gameState){
        gameAI.setGameState(gameState);
        gameAI.start();
    }

    @Override
    public void actionOnTouch(float x, float y) {
        if(state == GameState.TURN_PLAYER || (is2Players && state == GameState.TURN_MACHINE)) {
            if (board.validateInside(x, y)) {
                switch (board.getState()) {
                    case Board.STATE_WAITING:
                        board.initMoving(x, y);
                        board.initStatePlayerMoving();
                        break;
                }
            }
        }
    }

    @Override
    public void actionOnTouchUp(float x, float y) {
        if(state == GameState.TURN_PLAYER || (is2Players && state == GameState.TURN_MACHINE)) {
            if (board.validateInside(x, y)) {
                switch (board.getState()) {
                    case Board.STATE_PLAYER_MOVING:
                        int posX = board.getPositionXByX(x);
                        int posY = board.getPositionYByY(y);

                        if (!board.validateIfContainsPiece(posX, posY)) {
                            preActionMachine(posY, posX);
                            board.putPiece(posX, posY, state);
                        } else {
                            board.initStateWaiting();
                        }
                        break;
                }
            }
        }
    }

    @Override
    public void actionOnTouchMove(float x, float y) {
        if(state == GameState.TURN_PLAYER || (is2Players && state == GameState.TURN_MACHINE)) {
            switch (board.getState()) {
                case Board.STATE_PLAYER_MOVING:
                    board.initMoving(x, y);
                    break;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        float p = 0.96f; //proportion
        float r = 1.25f; //16.0f / 10.0f; //ratio

        setDimensions(width * p, width * p * r);
        setPosition((width - this.width) / 2, (height - this.height) / 2);

    }

    public void resetState(){
        setState(0);
    }

    @Override
    public boolean onBackPressed() {
        getMagnetonGame().setStateGame(MagnetonGame.STATE_GAME_LEVELS);
        return false;
    }

    public void drawWinOrDraw(Canvas canvas, String text) {
        com.ameteam.graphics.Rect.drawRectWithText(canvas, text, getRectF(), getResources().getColor(R.color.text_subtitle), Paint.Align.CENTER, 0.1125f);
    }

    private float countCircle = 0;
    private float countCircleInc = 0.025f;
    private float countCircleIncFast = 0.25f;

    public void drawTurnSection(Canvas canvas, String text, int turn) {
        float sectionHeight = board.getHeight() / 8;
        RectF rectF = new RectF();
        if(turn == GameState.TURN_MACHINE) {
            rectF.set(x, y, x + board.getWidth(), y + sectionHeight);
        }else{
            rectF.set(x, y + height - sectionHeight, x + board.getWidth(), y + height);
        }

        Paint paint = new Paint();
        float padding;
        float textProportion;
        if(turn == this.state) {
            paint.setColor(getResources().getColor(R.color.ply_scn_circle_on));
            padding = sectionHeight * 0.35f;
            textProportion = 0.48f;
        }else{
            paint.setColor(getResources().getColor(R.color.ply_scn_circle_off));
            padding = sectionHeight * 0.40f;
            textProportion = 0.44f;
        }
        paint.setStyle(Paint.Style.FILL);

        RectF rectFCircle = new RectF();
        rectFCircle.set(rectF.left + padding, rectF.top + padding, rectF.left + sectionHeight - padding, rectF.bottom - padding);
        canvas.drawOval(rectFCircle, paint);

        if(turn == this.state) {
            if(((int)countCircle) == 1){
                countCircle+=countCircleIncFast;
            }else{
                countCircle+=countCircleInc;
            }
            if(countCircle >= 2){
                countCircle = 0f;
            }
            if(((int)countCircle) == 1){
                paint.setColor(Color.parseColor("#CCffffff"));
                canvas.drawOval(rectFCircle, paint);
            }
        }

        RectF rectFText = new RectF();
        rectFText.set(rectF.left + sectionHeight, rectF.top, rectF.right, rectF.bottom);
        com.ameteam.graphics.Rect.drawRectWithText(canvas, text, rectFText, getResources().getColor(R.color.text_subtitle_light), Paint.Align.LEFT, textProportion);

        int numPieces = gameRules.getMaxPieces() - ( turn == GameState.TURN_MACHINE ? board.getCountPiecesMachine() : board.getCountPiecesPlayer() );
        rectFText.set(rectF.left + sectionHeight * 7, rectF.top, rectF.right - sectionHeight, rectF.bottom);
        com.ameteam.graphics.Rect.drawRectWithText(canvas, ("x" + numPieces), rectFText, getResources().getColor(R.color.text_subtitle_light), Paint.Align.LEFT, 0.40f);

        if(turn == GameState.TURN_PLAYER){
            paint.setColor(getResources().getColor(R.color.piece_fill_red));
        }else{
            paint.setColor(getResources().getColor(R.color.piece_fill_blue));
        }
        padding = sectionHeight * 0.275f;
        rectFCircle.set(rectF.left + sectionHeight * 6 + padding * 1.75f, rectF.top + padding, rectFText.right - padding * 0.25f, rectF.bottom - padding);
        canvas.drawOval(rectFCircle, paint);

    }

    public Board getBoard() {
        return board;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
