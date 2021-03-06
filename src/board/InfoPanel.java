/*
 * <pre> 
 * Class: <b>InfoPanel</b> 
 * File: InfoPanel.java 
 * Course: TCSS 305 � Autumn 2015
 * Assignment 6 � Tetris
 * Copyright 2015 Benjamin Abdipour
 * </pre>
 */

package board;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import model.AbstractPiece;
import model.Board;
import model.IPiece;
import model.OPiece;
import model.Piece;
import view.TetrisGUI;

/**
 * <pre>
 * This class is the a panel that displays the information about the current active
 * game. The information are the next coming block, the stats, and the control keys.
 * </pre>
 * 
 * @author Benjamin Abdipour
 * @version 12/11/2015
 * @since November 22, 2015
 */

public class InfoPanel extends JPanel implements Observer {

    /**
     * The auto-generated serial ID.
     */
    private static final long serialVersionUID = 4830685152151502285L;

    /**
     * The initial scale to which panel should be drawn.
     */
    private static final int INITIAL_SCALE = 20;

    /**
     * The panel's background color.
     */
    private static final Color PANEL_COLOR = Color.gray;

    /**
     * The panel's background color.
     */
    private static final Color DEAD_BLOCK_COLOR = Color.white;

    /**
     * The name of the font used in the panel.
     */
    private static final String FONT_NAME = "Tahoma";

    /**
     * Points scored per piece played.
     */
    private static final int SCORE_PER_PIECE = 10;

    /**
     * Points scored per line cleared.
     */
    private static final int SCORE_PER_LINE = 50;

    /**
     * The ratio of scale to width.
     */
    private static final int WIDTH_RATIO = 26;

    /**
     * The ratio of scale to height.
     */
    private static final int HEIGHT_RATIO = 26;

    /**
     * Double representing 33 percent.
     */
    private static final double PERCENT_33 = .33;

    /**
     * Double representing 75 percent.
     */
    private static final double PERCENT_75 = .75;

    /**
     * Represents three pixel shift.
     */
    private static final int TWO_PIXELS = 2;

    /**
     * Represents three pixel shift.
     */
    private static final int THREE_PIXELS = 3;

    /**
     * Represents five pixel shift.
     */
    private static final int FIVE_PIXELS = 5;

    /**
     * Represents ten pixel shift.
     */
    private static final int TEN_PIXELS = 10;

    /**
     * Represents twenty pixel shift.
     */
    private static final int TWENTY_PIXELS = 20;

    /**
     * Represents twelve pixel shift.
     */
    private static final int ELEVEN_PIXELS = 11;

    /**
     * Represents twelve pixel shift.
     */
    private static final int TWELVE_PIXELS = 12;

    /**
     * Represents thirteen pixel shift.
     */
    private static final int THIRTEEN_PIXELS = 13;

    /**
     * Represents thirteen pixel shift.
     */
    private static final int FOURTEEN_PIXELS = 14;

    /**
     * Represents sixteen pixel shift.
     */
    private static final int SIXTEEN_PIXELS = 16;

    /**
     * The number of pieces needed to go to the next level.
     */
    private static final int PIECE_PER_LEVEL = 10;

    /**
     * The frozen lines number.
     */
    private int myFrozenBlocksSize;

    /**
     * The next piece.
     */
    private Piece myNextPiece;

    /**
     * Pixel x-coordinate shift adjustment for next piece display.
     */
    private int myXAdjust;

    /**
     * Pixel y-coordinate shift adjustment for next piece display.
     */
    private int myYAdjust;

    /**
     * The number of lines cleared in current game.
     */
    private int myLineScore;

    /**
     * The total points scored in current game.
     */
    private int myTotalScore;

    /**
     * The total number of pieces played.
     */
    private int myPiecePlayed;

    /**
     * The current scale to which panel items are drawn.
     */
    private int myLevel;

    /**
     * The current scale to which panel items are drawn.
     */
    private int myScale;

    /**
     * An object of BoardPanel.
     */
    private BoardPanel myBoardPanel;

    /**
     * Constructor for game info board.
     */
    public InfoPanel() {
        super();
        setBackground(PANEL_COLOR);
        init();
    }

    /**
     * Constructor for the game info board.
     * 
     * @param theBoardPanel an object of BoardPanel
     */
    public InfoPanel(final BoardPanel theBoardPanel) {
        super();
        myBoardPanel = theBoardPanel;
        myScale = INITIAL_SCALE;
        myLevel = 0;
    }

    /**
     * Initializes the variables.
     */
    private void init() {
        myXAdjust = 0;
        myYAdjust = 0;
        myLineScore = 0;
        myTotalScore = 0;
        myFrozenBlocksSize = 0;
        myPiecePlayed = 0;
    }

    /**
     * Sets up panel for a new game.
     */
    public void newGame() {
        init();
        myLevel = 1;
    }

    /**
     * Method to stop the game.
     */
    public void stop() {
        JOptionPane.showMessageDialog(this.getParent(), "Game Ended\nYour Score: " 
                        + myTotalScore, "Game Ended", 
                        JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Updates the info panel.
     * 
     * @param theObserver the in-action observable (Board)
     * @param theObject the object passed in by observable object
     */
    @Override
    public void update(final Observable theObservable, final Object theObject) {
        if (theObservable instanceof Board) {
            final Board localBoard = (Board) theObservable;
            final Piece localNextPiece = localBoard.getNextPiece();
            calculatePieceScore(localNextPiece);
            calculateLevel();
            calculatePieceAlignment(localNextPiece);
            calculateLinesCleared(myBoardPanel.getFrozenBlockSize());
            if (localBoard.isGameOver()) {
                JOptionPane.showMessageDialog(this.getParent(), "Game Over\nYour Score: " 
                                + myTotalScore, "Game Over", 
                                JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (theObservable instanceof TetrisGUI) {
            final Dimension currentSize = ((ComponentEvent) theObject).
                            getComponent().getSize();
            final double width = currentSize.getWidth();
            final double height = currentSize.getHeight();
            if (width < height) {
                myScale = (int) width / WIDTH_RATIO;
            } else {
                myScale = (int) height / HEIGHT_RATIO;
            }
        }
        repaint();
    }



    /**
     * Calculates the total score for each piece that is placed.
     * 
     * @param thePiece the current piece in play on board
     */
    private void calculatePieceScore(final Piece thePiece) {
        if (myNextPiece != null && !thePiece.equals(myNextPiece)) {
            myPiecePlayed++;
            myTotalScore += SCORE_PER_PIECE;
        }   
    }

    /**
     * Calculates the level of the game.
     */
    private void calculateLevel() {
        if (myPiecePlayed == PIECE_PER_LEVEL * myLevel) {
            myBoardPanel.setDifficulty(myLevel);
            myLevel++;
        }   
    }

    /**
     * Adjusts the position for the next piece display.
     * 
     * @param thePiece the current piece in play on board
     */
    private void calculatePieceAlignment(final Piece thePiece) {
        if (!thePiece.equals(myNextPiece)) {
            myNextPiece = thePiece;
            if (myNextPiece instanceof IPiece) {
                myXAdjust = -TEN_PIXELS;
                myYAdjust = 0;
            } else if (myNextPiece instanceof OPiece) {
                myYAdjust = -FIVE_PIXELS; 
                myXAdjust = 0;
            } else {
                myXAdjust = 0;
                myYAdjust = FIVE_PIXELS;
            }
        }
    }

    /**
     * Updates lines cleared score.
     * 
     * @param theFrozenBlocksSize the current frozen blocks on the board
     */
    private void calculateLinesCleared(final int theFrozenBlocksSize) { 
        if (theFrozenBlocksSize < myFrozenBlocksSize) {
            myLineScore++;
            myTotalScore += SCORE_PER_LINE;
        }
        myFrozenBlocksSize = theFrozenBlocksSize;
    }

    /**
     * Draws the infoPanel.
     * 
     * @param theGraphic graphics object for drawing
     */
    @Override
    public void paintComponent(final Graphics theGraphic) {
        super.paintComponent(theGraphic);
        final Graphics2D g2d = (Graphics2D) theGraphic;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                             RenderingHints.VALUE_ANTIALIAS_ON);

        final int panelWidth = getWidth();
        drawPanel(g2d, panelWidth);
        drawNextPiece(g2d, panelWidth);
        drawControlKey(g2d, panelWidth);
    }

    /**
     * Draws the stats on the infoPanel.
     * 
     * @param theGraphic the graphics object to draw on
     * @param theWidth the current width of panel
     */
    private void drawPanel(final Graphics2D theGraphic, final int theWidth) {
        theGraphic.setColor(PANEL_COLOR);
        theGraphic.fillRect(0, 0, myBoardPanel.getWidth(), 
                            myBoardPanel.getHeight());
        theGraphic.setColor(DEAD_BLOCK_COLOR);
        theGraphic.setFont(new Font(FONT_NAME, Font.BOLD, (int) (PERCENT_75 * myScale)));
        theGraphic.drawString("Next Piece", (int) (theWidth * PERCENT_33) - 1, 
                              (myScale - 1) * TWO_PIXELS);
        theGraphic.drawString("Lines: " + myLineScore, 
                              (int) (theWidth * PERCENT_33) - TWENTY_PIXELS, 
                              myScale * ELEVEN_PIXELS);
        theGraphic.drawString("Score: " + myTotalScore, 
                              (int) (theWidth * PERCENT_33) - TWENTY_PIXELS, 
                              myScale * TWELVE_PIXELS);
        theGraphic.drawString("Level: " + myLevel, 
                              (int) (theWidth * PERCENT_33) - TWENTY_PIXELS, 
                              myScale * THIRTEEN_PIXELS);
        if (myBoardPanel.isPlaying()) {
            theGraphic.drawString("Next Level in " 
                            + (PIECE_PER_LEVEL * myLevel - myPiecePlayed) 
                            + " pieces", (int) (theWidth * PERCENT_33) - TWENTY_PIXELS, 
                            myScale * FOURTEEN_PIXELS);
        }
    }

    /**
     * Draws the next piece on the infoPanel.
     * 
     * @param theGraphic the graphics object to draw on
     * @param theWidth the current width of panel
     */
    private void drawNextPiece(final Graphics2D theGraphic, final int theWidth) {
        if (myNextPiece != null) {
            final int[][] piece = ((AbstractPiece) myNextPiece).getRotation();

            for (final int[] block : piece) {
                theGraphic.setColor(DEAD_BLOCK_COLOR);
                theGraphic.fillRect((int) (theWidth * PERCENT_33) 
                                    + block[1] * myScale + myXAdjust, 
                                    THREE_PIXELS * myScale + block[0] 
                                                    * myScale + myScale / 2 + myYAdjust, 
                                                    myScale, myScale);  
                theGraphic.setColor(PANEL_COLOR);
                theGraphic.drawRect((int) (theWidth * PERCENT_33) 
                                    + block[1] * myScale + myXAdjust, 
                                    THREE_PIXELS * myScale + block[0] 
                                                    * myScale + myScale / 2 + myYAdjust, 
                                                    myScale, myScale);
            }
        }
    }

    /**
     * Draws the control keys.
     * 
     * @param theGraphic the graphics object to draw on
     * @param theWidth the current width of panel
     */
    private void drawControlKey(final Graphics2D theGraphic, final int theWidth) {
        int localOffset = SIXTEEN_PIXELS;
        theGraphic.setColor(DEAD_BLOCK_COLOR);
        theGraphic.drawRect((int) (theWidth * PERCENT_33) - TEN_PIXELS, 
                            THREE_PIXELS * myScale, myScale * FIVE_PIXELS, 
                            myScale * FIVE_PIXELS);
        theGraphic.setFont(new Font(FONT_NAME, Font.BOLD, (int) (PERCENT_75 * myScale)));
        theGraphic.drawString("Move Left: " + KeyEvent.getKeyText(myBoardPanel.
                                                                  getControlKeys().
                                                                  get("left")), 
                              (int) (theWidth * PERCENT_33) - TWENTY_PIXELS, myScale 
                              * localOffset++);
        theGraphic.drawString("Move Right: " + KeyEvent.getKeyText(myBoardPanel.
                                                                   getControlKeys().
                                                                   get("right")), 
                              (int) (theWidth * PERCENT_33) - TWENTY_PIXELS, myScale 
                              * localOffset++);
        theGraphic.drawString("Move Down: " + KeyEvent.getKeyText(myBoardPanel.
                                                                  getControlKeys().
                                                                  get("down")), 
                              (int) (theWidth * PERCENT_33) - TWENTY_PIXELS, myScale 
                              * localOffset++);
        theGraphic.drawString("Rotate: " + KeyEvent.getKeyText(myBoardPanel.
                                                                  getControlKeys().
                                                                  get("rotate")), 
                              (int) (theWidth * PERCENT_33) - TWENTY_PIXELS, myScale 
                              * localOffset++);
        theGraphic.drawString("Drop: " + KeyEvent.getKeyText(myBoardPanel.getControlKeys().
                                                             get("drop")), 
                              (int) (theWidth * PERCENT_33) - TWENTY_PIXELS, myScale 
                              * localOffset++);
        theGraphic.drawString("Pause: " + KeyEvent.getKeyText(myBoardPanel.getControlKeys().
                                                             get("pause")), 
                              (int) (theWidth * PERCENT_33) - TWENTY_PIXELS, myScale 
                              * localOffset++);
    }

    /**
     * Returns the current level of the game.
     * @return myLevel The current level of the game
     */
    public int getLevel() {
        return myLevel;
    }
}
