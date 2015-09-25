package edu.nyu.cs.connectFour;

import java.util.LinkedList;
import java.util.List;

import edu.nyu.cs.connectFour.ConnectFourConstant;

/**
 * @author shao
 *
 *ConnectFourModel implements the logical part of the Connect Four game.
 *It checks whether the current move by player can end up a winning stage.
 *  -check if there are four connected vertical/horizontal/right/left diagonal  
 *It checks if the end is ended by keeping track of number of moves on the game board.
 *It implements the auto complete to finish a game
 */
public class ConnectFourModel {
  private List<ConnectFourListener> listeners = new LinkedList<ConnectFourListener>();
  private Player[][] playBoard = new Player[ConnectFourConstant.ROW][ConnectFourConstant.COL];
  private int numsOfMove = 0;
  private String ModelId;
 
  /**
   * Constructor for Builder class.
   * Building the ConnectFourModel by providing an id.
   */
  public static class Builder{
    private String ModelId;
    public Builder(String ModelId){
      this.ModelId = ModelId;
    }
    public ConnectFourModel build(){
      return new ConnectFourModel(this);
    }
  }
  
  /**
   * @param builder
   */
  private ConnectFourModel(Builder builder){
    this.ModelId = builder.ModelId;
  }
  
  /**
   * The setPlayBoard method is designed for unit testing and potential change in 
   * the future.
   * @param playBoard
   */
  public void setPlayBoard(Player[][] playBoard){
    this.playBoard = new Player[playBoard.length][playBoard[0].length];
    for(int i=0;i<playBoard.length;i++){
      for(int j=0;j<playBoard[0].length;j++){
        this.playBoard[i][j] = playBoard[i][j];
      }
    }
  }
  
  /**
   * Entry point to decide stage of the game by making a move at pos by a player.
   * fireInvalidMoveEvent - position is not valid.
   * fireGameEndEvent - the move is a winning move. 
   * fireGameEndEvent(xPos,yPos,Player.TIE); - TIE game if numsOfMove is equal to total number
   * of cells on game board.
   * fireGameContinueEvent - the move is valid and the game is not ended, pass the turn to opponent.
   * @param pos current position of the move
   * @param turn player
   * return 
   * @throws Exception 
   */
  public boolean checkCurrentStatusOfGame(Coordinate pos, Player turn) throws IllegalArgumentException{
    try{
      int xPos = pos.getX();
      int yPos = pos.getY();
      if(checkIfInValidMove(xPos,yPos,turn)){    
          fireInvalidMoveEvent(turn);
          return false;
      }else{
          playBoard[xPos][yPos] = turn;
          if(checkIfConnectFourComplete(turn)){
              fireGameEndEvent(xPos,yPos,turn);
              return true;
          }else{
              numsOfMove ++ ;
              System.out.println(numsOfMove);
              if(numsOfMove == ConnectFourConstant.ROW * ConnectFourConstant.COL){
                  fireGameEndEvent(xPos,yPos,Player.TIE);
                  return true;
              }
              fireGameContinueEvent(xPos, yPos,turn);
              return true;
          }
      }
    }catch(Exception e){
      throw new IllegalArgumentException("Inlegal inputs.");
    }
}
 
  /**
  * First statement check if position is not out of the board, second statement check
  * if below position is empty. either case will fire InvalidEvent.
  * Third statement check if the cell is occupied.
  * @param xPos x coordinate
  * @param yPos y coordinate
  * @param turn player
  * @return
  */
  private boolean checkIfInValidMove(int xPos, int yPos, Player turn){
      return (xPos<0 || xPos>= ConnectFourConstant.ROW || yPos <0 || yPos>=ConnectFourConstant.COL)
              || (xPos+1 < ConnectFourConstant.ROW && playBoard[xPos+1][yPos] == null)
              || playBoard[xPos][yPos]!=null;
  }

  /**
  *  Check if there is 4 connected cells on the board for the current player by looking at
  *  four directions.
  * @param xPos x coordinate
  * @param yPos y coordinate
  * @param turn player
  * @return
  */
  private boolean checkIfConnectFourComplete(Player turn){
    for(int i=0; i<ConnectFourConstant.COL; i++){
      for(int j=0; j< ConnectFourConstant.ROW;j++){
        if(playBoard[j][i] == turn){
          for(Direction dir : Direction.values()){
            if(validateIfConnectFour(j,i,0,turn,dir)){
                return true;
            }
          }
          if(playBoard[j][i]!=null)
            //only need to check the top layer.
            break;
      }
    }
  }
   return false;
  }
 
  /**
  * Check if there is 4 connected cells on the board for the current player.
  * @param curX current coordinate x
  * @param curY current coordinate y
  * @param numConnected
  * @param turn
  * @param direction
  * @return
  */
  private boolean validateIfConnectFour(int curX,
                                          int curY,
                                          int numConnected,
                                          Player turn,
                                          Direction direction){
      if(numConnected == ConnectFourConstant.NUMCONNECT)
          return true;
      if(curX<0 || curX>= ConnectFourConstant.ROW || curY <0 || curY>=ConnectFourConstant.COL)
          return false;// potential exception.
      
      if(playBoard[curX][curY] == turn){
          switch (direction){
            case LEFT:
                  return validateIfConnectFour(curX,curY-1,numConnected+1,turn,direction);
            case RIGHT:
                  return validateIfConnectFour(curX,curY+1,numConnected+1,turn,direction);
            case DOWN:
                  return validateIfConnectFour(curX+1,curY,numConnected+1,turn,direction);
            case R_DIAGONAL:
                  return validateIfConnectFour(curX+1,curY+1,numConnected+1,turn,direction);
            case L_DIAGONAL:
                  return validateIfConnectFour(curX-1,curY+1,numConnected+1,turn,direction);
          }
      }
      return false;
  }
 
  /**
  * Restart the game by resetting new playBoard array and numsOfMove to 0.
  */
  public void restart(){
      playBoard = new Player[ConnectFourConstant.ROW][ConnectFourConstant.COL];
      numsOfMove = 0;
  }
 
  /**
   * fireInvalidMoveEvent is to indicate the move is not valid.
   * @param turn
   */
  private void fireInvalidMoveEvent(Player turn){
      for (ConnectFourListener listener : listeners) {
          listener.invalidMoveNotifier(turn);
      }
  }
   
  /**
   * fireGameEndEvent is to indicate the game is over, the ended status depends on
   * turn.
   * @param turn
   */
  private void fireGameEndEvent(int xPos,int yPos,Player turn) {
      for (ConnectFourListener listener : listeners) {
          listener.gameEndNotifier(xPos,yPos,turn);
      }
  }
 
  /**
   * fireGameContinueEvent is to indicate the game is not ended, pass the turn to opponent.
 * @param xPos  current coordinate x
 * @param yPos  current coordinate y
 * @param turn
 */
  private void fireGameContinueEvent(int xPos, int yPos,Player turn) {
      for (ConnectFourListener listener : listeners) {
          listener.gameContinueNotifier(xPos,yPos,turn);
        }
  }
 
  /**
   * Autocomplet is not valid, it's possible that the move is not the last.
   * @param turn
   */
  private void fireInvalidAutoCompleteEvent(Player turn) {
      for (ConnectFourListener listener : listeners) {
          listener.invalidAutoCompleteNotifier(turn);
        }
  }

  /**
  * Check if it's possible to auto - complete the game in one single move.
  * @param turn
  */
  public boolean autoComplete(Player turn) {
    for(int i = 0; i< ConnectFourConstant.COL; i++){
        for(int j=0; j< ConnectFourConstant.ROW; j++){
            if(!checkIfInValidMove(j,i,turn)){
              playBoard[j][i] = turn;
              if(checkIfConnectFourComplete(turn)){
                fireGameEndEvent(j,i,turn);
                return true;
              }
              playBoard[j][i] = null;
            }         
       }
      }
      fireInvalidAutoCompleteEvent(turn);
      return false;
    }

  /**
  * Add game listener.
  * @param listener
  */
  public void addGameListener(ConnectFourListener listener){
    listeners.add(listener);
   }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString(){
    StringBuffer rt = new StringBuffer();
    for(int i = 0; i < playBoard.length; i++){
      for(int j=0; j< playBoard[i].length; j++){
        if(playBoard[i][j] == null)
          continue;
        rt.append(i+","+j+":"+ playBoard[i][j] + "~");
      }
    }
    rt.append("numsOfMove:"+numsOfMove);
    return rt.toString();
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj){
    if(this == obj)
      return true;
    if(obj == null)
      return false;
    ConnectFourModel that = (ConnectFourModel)obj;
    for(int i = 0; i < playBoard.length; i++){
      for(int j=0; j< playBoard[i].length; j++){
        if(that.playBoard[i][j] != this.playBoard[i][j])
          return false;
      }
     }
    return true;
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode(){
    final int prime = 31;
    int result = 1;
    for(int i = 0; i < playBoard.length; i++){
      for(int j=0; j< playBoard[i].length; j++){
        if(playBoard[i][j] == null)
          continue;
        result = prime * result + playBoard[i][j].hashCode();
      }
    }
    result += this.numsOfMove;
    return result;
  }
}

 