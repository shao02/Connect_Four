package edu.nyu.cs.connectFour;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import edu.nyu.cs.connectFour.ConnectFourConstant;
import edu.nyu.cs.connectFour.ConnectFourListener;
import edu.nyu.cs.connectFour.ConnectFourModel;
import edu.nyu.cs.connectFour.Coordinate;
import edu.nyu.cs.connectFour.Player;

/**
 * ConnectFourView is the JFrame view that represent all UI components of
 * the ConnectFour game.  ConnectFourListener is bridging the gap between
 * ConnectFourView and ConnectFourModel.
 *
 */
public class ConnectFourView implements ConnectFourListener{
  private JFrame frame = new JFrame(ConnectFourConstant.TITLE);
  private JPanel mainPanel = new JPanel();
  private JTextArea textArea = new JTextArea(ConnectFourConstant.WELCOMEMSG);
  //This is button for auto - complete while it's one step away from winning the game.
  //User can click this button for auto - complete.
  private JButton autoCompleteButton = new JButton(ConnectFourConstant.AUTOCOMPLETE);
  private JButton restartButton = new JButton(ConnectFourConstant.RESTARTBT);
  private JPanel northPanel = new JPanel();
  //allButtons represents the position of each player move during the game.
  private JButton[][] allButtons = new JButton[ConnectFourConstant.ROW][ConnectFourConstant.COL];
  //buttonMapper is for looking up JButton Coordinate efficiently instead of iterating
  //through the array.
  private Map<JButton,Coordinate> buttonMapper = new HashMap<JButton,Coordinate>();
  //two players take turn to play the game.
  private Player turn = Player.PlayerA;
  private ConnectFourModel model;
  private JPanel connectFourBoardPanel= new JPanel();
 
  /**
   * @param model object that control logical part of the program.
   */
  public ConnectFourView(ConnectFourModel model){
      model.addGameListener(this);
      this.model = model;      
      ConnectFourLayout();
  }
 
  /**
  * ConnectFourLayout initializes the grid,buttons,game board of the ConnectFour program.
  * links event to all buttons defined in the program.
  */
  private void ConnectFourLayout(){
      mainPanel.setLayout(new BorderLayout());
      connectFourBoardPanel.setLayout(new GridLayout(ConnectFourConstant.ROW,
                                                          ConnectFourConstant.COL));
      //Add Event handler to all game board buttons.
      for(int i=0; i< ConnectFourConstant.ROW;i++){
            for(int j=0; j < ConnectFourConstant.COL; j++){
                JButton curButton = addJButtonName(i,j);
                buttonMapper.put(curButton, new Coordinate(i,j));
                connectFourBoardPanel.add(curButton);
            }
      }
      setRestartButton();
      setAutoCompleteButton();
      northPanel.add(restartButton);
      northPanel.add(autoCompleteButton);
      mainPanel.add(connectFourBoardPanel,BorderLayout.CENTER);
      mainPanel.add(new JScrollPane(textArea), BorderLayout.SOUTH);
      mainPanel.add(northPanel, BorderLayout.NORTH);
      frame.getContentPane().add(mainPanel);
      frame.setSize(ConnectFourConstant.FRAME_WIDTH, ConnectFourConstant.FRAME_HEIGHT);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
  }
 
  /**
  * Add listener to restartbutton to restart the game.
  */
  private void setRestartButton(){
      restartButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            gameReStart();
        }
      });
  }
 
  /**
  * Add listener to autoComplete to try complete the game if one
  * step away from winning for current player.
  */
  private void setAutoCompleteButton(){
      autoCompleteButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            model.autoComplete(turn);
        }
      });
  }
 
  /**
   * Build one JButton on the game board.
 * @param x x coordinate
 * @param y y coordinate
 * @return
 */
  private JButton addJButtonName(int x, int y){
    JButton curJButton = new JButton();
    allButtons[x][y] = curJButton;
    //curJButton.setActionCommand(pos/ConnectFourConstant.COL+","+pos%ConnectFourConstant.COL);
        
    curJButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        JButton button = (JButton) event.getSource();
        Coordinate pos = buttonMapper.get(button);
        try {
            model.checkCurrentStatusOfGame(pos,turn);
        } catch (Exception e) {
            e.printStackTrace();
        }
      }
    });
    return curJButton;
  }
 
  public static void main(String[] args){
    //Player[][] playBoard = new Player[ConnectFourConstant.ROW][ConnectFourConstant.COL];
    //System.out.print(playBoard[0][0]);
    ConnectFourModel model = ConnectFourModel.Builder("id_1").build;
    ConnectFourView view = new ConnectFourView(model);
  }
 
/**
 * Reset the game, basally for the view, it just need to reset the
 * buttonMapper and allButtons array.
 */
  public void gameReStart() {
     for(int i=0; i< ConnectFourConstant.ROW;i++){
            for(int j=0; j < ConnectFourConstant.COL; j++){
              allButtons[i][j].setEnabled(true);
              allButtons[i][j].setBackground(null);  
            }
       }
     model.restart();
   }

 /* (non-Javadoc)
 * @see edu.nyu.cs.connectFour.ConnectFourListener#gameContinueNotifier(int, int, edu.nyu.cs.connectFour.Player)
 */
  @Override
  public void gameContinueNotifier(int xPos, int yPos,Player turn) {
    Color cellColor = turn == Player.PlayerA ? ConnectFourConstant.COLORG : ConnectFourConstant.COLORR;
    this.turn = turn == Player.PlayerA ? Player.PlayerB : Player.PlayerA;
    allButtons[xPos][yPos].setBackground(cellColor);
    allButtons[xPos][yPos].setOpaque(true);
    textArea.setText(turn.toString());  
  }

 /* (non-Javadoc)
 * @see edu.nyu.cs.connectFour.ConnectFourListener#gameEndNotifier(int, int, edu.nyu.cs.connectFour.Player)
 */
  @Override
  public void gameEndNotifier(int xPos, int yPos,Player turn) {
    Color cellColor = turn == Player.PlayerA ? ConnectFourConstant.COLORG : ConnectFourConstant.COLORR;
    allButtons[xPos][yPos].setBackground(cellColor);
    allButtons[xPos][yPos].setOpaque(true);
    
    if(turn == Player.TIE)
        JOptionPane.showMessageDialog(null, ConnectFourConstant.TIEMSG);
        //textArea.setText(ConnectFourConstant.TIEMSG);
    else
        JOptionPane.showMessageDialog(null, turn+" "+ConnectFourConstant.WINMSG);
        //textArea.setText(turn+ ConnectFourConstant.WINMSG);
    
    for(int i=0; i< ConnectFourConstant.ROW;i++){
            for(int j=0; j < ConnectFourConstant.COL; j++){
                JButton curButton = allButtons[i][j];
                curButton.setEnabled(false);
             }
    }
}

 /* (non-Javadoc)
 * @see edu.nyu.cs.connectFour.ConnectFourListener#invalidAutoCompleteNotifier(edu.nyu.cs.connectFour.Player)
 */
  @Override
  public void invalidAutoCompleteNotifier(Player turn){
    textArea.setText(ConnectFourConstant.ERROR_AUTO_COMPLETE);
   }

/* (non-Javadoc)
 * @see edu.nyu.cs.connectFour.ConnectFourListener#invalidMoveNotifier(edu.nyu.cs.connectFour.Player)
 */
  @Override
  public void invalidMoveNotifier(Player turn) {
    textArea.setText(ConnectFourConstant.ERROR_INVALID_MOVE);    
   }
}

 