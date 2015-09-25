
 

package edu.nyu.cs.connectFour;

/**
 * @author
 * Coordinate object can indicate current position in the connect four board.
 * ex: (x=0,y=0) is the top left corner of the board.
 */
public class Coordinate {
    private int x;
    private int y;
    
    /**
     * @param x
     * @param y
     */
    public Coordinate(int x,int y){
        this.x = x;
        this.y = y;
    }
    
    /**
     * @return
     */
    public int getX(){
        return x;
    }
    
    /**
     * @return
     */
    public int getY(){
        return y;
    }
    
    /**
     * @param x
     */
    public void setX(int x){
        this.x = x;
    }
    
    /**
     * @param y
     */
    public void setY(int y){
        this.y = y;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
      return "x:" +x + " y:"+y;
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
      Coordinate that = (Coordinate)obj;
      return that.x == this.x && that.y == this.y;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode(){
      final int prime = 31;
      int result = 1;
      result = prime * result + x + y;
      return result;
    }
}

 