package edu.nyu.cs.connectFour;

import java.awt.Color;

/**
 * All constant of the ConnectFour classes are defined in this interface.
 * ConnectFour caller has freedom to customize following properties:
 *
 * Size of the connectFour board, by default it's 6*7.
 * Size of the application frame, by default it's 500 * 500.
 * Color of the player.
 * etc.
 */
public interface ConnectFourConstant {
  static public final int ROW = 6;
  static public final int COL = 7;
  static public final int FRAME_HEIGHT = 500;
  static public final int FRAME_WIDTH = 500;
  static public final Color COLORR = Color.RED;
  static public final Color COLORG = Color.GREEN;
  static public final int NUMCONNECT = 4;
  static public final String WELCOMEMSG = "Welcome to the Connect Four Game";
  static public final String TITLE = "Connect Four Game";
  static public final String RESTARTBT = "Restart Game";
  static public final String AUTOCOMPLETE = "Auto Complete";
  static public final String WINMSG = "win the Game";
  static public final String TIEMSG = "Tie Game";
  static public final String ERROR_AUTO_COMPLETE = "Unable to complete the game in one move";
  static public final String ERROR_INVALID_MOVE = "Invalid Move!! please refer to https" +
                                                    ":wiki/Connect_Four";
}

 