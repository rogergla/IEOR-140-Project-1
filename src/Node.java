

/**
* represents a junction in a rectangular grid<br>
* uses Grid  ; uses Grid
* In calculating shortest path, Node has the responsibility of deciding if it
 * should be added to the end of the list in Grid
* @author Roger Glassey
*/
 public class Node  

{
/**
 *shortest distance  from destination
 *set by reset(), newDistance()
 **/
  	private int  _distance;
    
/**
     * Returns the length of the shortest path to the destination
     * @return  shortest distance to the destination
     * 
     */

    public int getDistance(){return _distance;}
/**
set by blocked()
*/
  	private boolean blocked = false;

	public static final int BIG = 99; // infinity in the grid
/**
 * row and column index of my location in grid
*/
	private int _x,_y;
/**
     * allocate a new Node at location x,y in the Grid
     * @param x
     * @param y 
     */
	public Node(int x,int y)
	{
		_x = x;
		_y = y;
          _distance = BIG;
	}
/**
     * Returns the x coordinate  
     * @return  x coordinate  
     */
	public int getX() {return _x;}
    
    /**
     * Returns the y coordinate
     * @return the y coordinate 
     */
    
	public int getY() {return _y;}
	
/*
*prepare for shortest path calculation; called by Grid;
     * sets the mode distance to a big number
*/
	public void reset (){_distance = BIG;}	//indicates shortest distance not yet known

/**
* updates distance if parameter  d   is smaller than current value, in which case return true <br>
* indicating node should be added to list.
*/
	public boolean newDistance (int distance)
	{
		if (blocked) return false;
		if (distance < _distance)
		{
			_distance =  distance;
			return true;
		}
		else return false;
	}
/**
* node is now blocked; distance to destination is BIG
*/
	public void  blocked()
	{
		blocked = true;
		_distance = BIG;
	}
	public boolean isBlocked(){return blocked;}

/**
* resets blocked flag
*/
	public void unblocked()
	{
		blocked = false;
	}
}
