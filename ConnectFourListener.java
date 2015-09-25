package edu.nyu.cs.connectFour;
/**
 * @author shao
 * The Listener interface is bridging the gap between ConnectFourModel
 * and ConnectFourView by providing a sequence of notifier in each stage.
 */
public interface ConnectFourListener {
  /**
   * After evaluation of current move for player, if the game is not ended.
   * The view will need to move on to the next stage.
   * @param xPos x coordinate of the current position on game board.
   * @param yPos y coordinate of the current position on game board.
   * @param turn player
   */
  void gameContinueNotifier(int xPos, int yPos,Player turn);
  
  /**
   * Trigger the gameEndNotifier to indicate the end of the game.
   * There are two situation, either Player can win the game or the game is 
   * Tie, for a Tie game , turn would be set to Player.Tie
   * @param xPos x coordinate of the current position on game board.
   * @param yPos y coordinate of the current position on game board.
   * @param turn player
   */
  void gameEndNotifier(int xPos, int yPos,Player turn);
  
  /**
   * To indicate that user is placing a wrong move.
   * @param turn player
   */
  void invalidMoveNotifier(Player turn);
  
  /**
   * According to the assignment requirement, opponent can make one single 
   * automatic move if that move can end up a winning stage.
   * @param turn player
   */
  void invalidAutoCompleteNotifier(Player turn);
}

 
