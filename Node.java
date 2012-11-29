
public class Node {
	
	public static Board board;
	public int gCost;
	public int hCost;
	public int fCost;
	public String steps = "";
	
	public Node(String boardstring)
	{
		gCost = boardstring.length();
		Board temp = new Board(board);
		for(int i=0;i<boardstring.length();i++)
		{
			 temp.step(Integer.parseInt(boardstring.charAt(i) + ""));
		}	  
		hCost = temp.getBoardHeuristicValue();
		fCost = gCost + hCost;
		steps = boardstring;
		temp = null;
	}
	
	public String toString()
	{
		return "F = " + fCost + " G = " + gCost + " H = " + hCost;
	}

}


