import java.awt.*; import javax.swing.*; 

import java.util.PriorityQueue;
import java.util.Random;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.*;
class FloodIt extends JFrame implements MouseListener
{
	GCanvas canvas;
	Board board;
	boolean runningSim = false;
	
  public FloodIt()
  {
    super("FloodIt");setBounds(50,50,800,800);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Container con=this.getContentPane();
    con.setBackground(Color.white);
    board = new Board();
    canvas=new GCanvas();
    canvas.board = board;
    con.add(canvas);
    setVisible(true);
    canvas.addMouseListener(this);
  }
  public static void main(String arg[]) 
  {
	  
	  
	  
	  
	  final long startTime = System.nanoTime();
	  final long endTime;
	  try {
		  FloodIt game = new FloodIt();
		  int totalSteps = 0;
		  int numTrials = 5;
		  //game.board.showHeuristicOnMap = true;
		  //game.runAStarSearch();
		  for(int i=0;i<numTrials;i++)
		  {
			  //totalSteps+=game.runAStarSearch();
			  //totalSteps+=game.runDLDFS(4);
			  //totalSteps+=game.runGBFS();
			  //totalSteps+=game.runRandomAI();
			  game.board.resetBoard();
		  }
		  System.out.println("Average of "+(double)totalSteps/numTrials+" steps");
	  } finally {
	    endTime = System.nanoTime();
	  }
	  final long duration = endTime - startTime;
	  System.out.println("Time: " + duration/1000.0);
	  
  }
  public int runAStarSearch()
  {
	  runningSim = true;
	  Node.board = board;
	  int steps = 0;
	  String solution;
	  Node start = new Node("");
	  System.out.println(start);
	  PriorityQueue<Node> openList = new PriorityQueue<Node>(5000000,new NodeComparator());
	  
	  openList.add(new Node("0"));
	  openList.add(new Node("1"));
	  openList.add(new Node("2"));
	  openList.add(new Node("3"));
	  openList.add(new Node("4"));
	  openList.add(new Node("5"));
	  
	  Node current;
	  while(true)
	  {
		  steps++;
		  current = openList.poll();
		  //System.out.println(current);
		  if(steps % 10000 == 0)
		  {
			  System.out.println(steps + " nodes expanded");
			  System.out.println(current);
		  }
			  
		  if(current.hCost == 0)
		  {
			  System.out.println(steps + " nodes expanded");
			  System.out.println(current.steps);
			  solution = current.steps;
			  break;
		  }
		  
		  if(current.steps.charAt(current.steps.length()-1) != '0') openList.add(new Node(current.steps + "0"));
		  if(current.steps.charAt(current.steps.length()-1) != '1') openList.add(new Node(current.steps + "1"));
		  if(current.steps.charAt(current.steps.length()-1) != '2') openList.add(new Node(current.steps + "2"));
		  if(current.steps.charAt(current.steps.length()-1) != '3') openList.add(new Node(current.steps + "3"));
		  if(current.steps.charAt(current.steps.length()-1) != '4') openList.add(new Node(current.steps + "4"));
		  if(current.steps.charAt(current.steps.length()-1) != '5') openList.add(new Node(current.steps + "5"));
		  current = null;
	  }
	  System.out.println("A* is Done!!!" + solution);
	  
	 
	  return solution.length(); 
  }
  public int runDLDFS(int depthLimit)
  {
	  runningSim = true;
	  Random r = new Random();
	  int steps = 0;
	  while(board.tilesRemaining > 0)
	  {
		  steps++;
		  
		  
		  int nextMove = 0;
		  Board board0 = new Board(board);
		  Board board1 = new Board(board);
		  Board board2 = new Board(board);
		  Board board3 = new Board(board);
		  Board board4 = new Board(board);
		  Board board5 = new Board(board);
		  board0.step(0);
		  board1.step(1);
		  board2.step(2);
		  board3.step(3);
		  board4.step(4);
		  board5.step(5);
		  int board0result = DFSExpand(board0, 0, depthLimit);
		  int board1result = DFSExpand(board1, 0, depthLimit);
		  int board2result = DFSExpand(board2, 0, depthLimit);
		  int board3result = DFSExpand(board3, 0, depthLimit);
		  int board4result = DFSExpand(board4, 0, depthLimit);
		  int board5result = DFSExpand(board5, 0, depthLimit);
		  int minimumMove =  min(board0result,board1result,board2result,board3result,board4result,board5result);
		  if(minimumMove == 0)
		  {
			  int nextGreedyMove = 0;
			  int minTilesRemaining = board.tilesRemaining;
			  for(int i = 0;i<6;i++)
			  {
				  Board test = new Board(board);
				  test.step(i);
				  if(test.tilesRemaining < minTilesRemaining)
				  {
					  nextGreedyMove = i;
					  minTilesRemaining = test.tilesRemaining;
				  }
			  }
			  nextMove = nextGreedyMove;
		  }
		  else
		  {
			  if(minimumMove == board0result) nextMove = 0;
			  if(minimumMove == board1result) nextMove = 1;
			  if(minimumMove == board2result) nextMove = 2;
			  if(minimumMove == board3result) nextMove = 3;
			  if(minimumMove == board4result) nextMove = 4;
			  if(minimumMove == board5result) nextMove = 5;
		  }
		  board.step(nextMove);
		  canvas.repaint();
		 /* try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	  }
	  System.out.println("Done in "+steps+" steps");
	  return steps;
  }
  public int DFSExpand(Board currentBoard, int depth, int depthLimit)
  {
	  if(currentBoard.tilesRemaining == 0)
	  {
		  return 0;
	  }
	  if(depth >= depthLimit)
	  {
		  return currentBoard.tilesRemaining;
	  }
	  else
	  {
		  Board board0 = new Board(currentBoard);
		  Board board1 = new Board(currentBoard);
		  Board board2 = new Board(currentBoard);
		  Board board3 = new Board(currentBoard);
		  Board board4 = new Board(currentBoard);
		  Board board5 = new Board(currentBoard);
		  board0.step(0);
		  board1.step(1);
		  board2.step(2);
		  board3.step(3);
		  board4.step(4);
		  board5.step(5);
		  int board0result = DFSExpand(board0, depth+1, depthLimit);
		  int board1result = DFSExpand(board1, depth+1, depthLimit);
		  int board2result = DFSExpand(board2, depth+1, depthLimit);
		  int board3result = DFSExpand(board3, depth+1, depthLimit);
		  int board4result = DFSExpand(board4, depth+1, depthLimit);
		  int board5result = DFSExpand(board5, depth+1, depthLimit);
		  return min(board0result,board1result,board2result,board3result,board4result,board5result);
	  }
	  
  }
  public int runGBFS()
  {
	  runningSim = true;
	  Random r = new Random();
	  int steps = 0;
	  while(board.tilesRemaining > 0)
	  {
		  steps++;
		  int nextGreedyMove = 0;
		  int minTilesRemaining = board.tilesRemaining;
		  for(int i = 0;i<6;i++)
		  {
			  Board test = new Board(board);
			  test.step(i);
			  if(test.tilesRemaining < minTilesRemaining)
			  {
				  nextGreedyMove = i;
				  minTilesRemaining = test.tilesRemaining;
			  }
		  }
		  
		  
		  
		  
		  board.step(nextGreedyMove);
		  //canvas.repaint();
		 /* try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	  }
	  System.out.println("Done in "+steps+" steps");
	  return steps;
  }
  public int runRandomAI()
  {
	  runningSim = true;
	  Random r = new Random();
	  int steps = 0;
	  while(board.tilesRemaining > 0)
	  {
		  steps++;
		  
		  Board test = new Board(board);
		  int nextRandomMove = 0;
		  while(test.tilesRemaining == board.tilesRemaining)
		  {
			  nextRandomMove = r.nextInt(6);
			  test.step(nextRandomMove);
		  }
		  board.step(nextRandomMove);
		  //canvas.repaint();
		  /*try {
			//Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	  }
	  System.out.println("Done in "+steps+" steps");
	  return steps;
  }
@Override
public void mouseClicked(MouseEvent arg0) {
	if(runningSim) return;
	if(arg0.getX() > 50 && arg0.getX() < 100)
	{
		if(arg0.getY() > 715 && arg0.getY() < 765)
		{
			board.step(0);
			canvas.repaint();
		}
	}
	if(arg0.getX() > 150 && arg0.getX() < 200)
	{
		if(arg0.getY() > 715 && arg0.getY() < 765)
		{
			board.step(1);
			canvas.repaint();
		}
	}
	if(arg0.getX() > 250 && arg0.getX() < 300)
	{
		if(arg0.getY() > 715 && arg0.getY() < 765)
		{
			board.step(2);
			canvas.repaint();
		}
	}
	if(arg0.getX() > 350 && arg0.getX() < 400)
	{
		if(arg0.getY() > 715 && arg0.getY() < 765)
		{
			board.step(3);
			canvas.repaint();
		}
	}
	if(arg0.getX() > 450 && arg0.getX() < 500)
	{
		if(arg0.getY() > 715 && arg0.getY() < 765)
		{
			
			board.step(4);
			canvas.repaint();
		}
	}
	if(arg0.getX() > 550 && arg0.getX() < 600)
	{
		if(arg0.getY() > 715 && arg0.getY() < 765)
		{
			board.step(5);
			canvas.repaint();
		}
	}
	
	
}
private static int min(int a,int b,int c,int d,int e,int f)
{
	  int minimum = a;
	  if(minimum > b) minimum = b;
	  if(minimum > c) minimum = c;
	  if(minimum > d) minimum = d;
	  if(minimum > e) minimum = e;
	  if(minimum > f) minimum = f;
	  return minimum;
}
@Override
public void mouseEntered(MouseEvent arg0) {
	//System.out.println("hi");
	
}
@Override
public void mouseExited(MouseEvent arg0) {
	// TODO Auto-generated method stub
	
}
@Override
public void mousePressed(MouseEvent arg0) {
	// TODO Auto-generated method stub
	
}
@Override
public void mouseReleased(MouseEvent arg0) {
	// TODO Auto-generated method stub
	
}
}
class GCanvas extends Canvas // create a canvas for your graphics
{
	Board board;
  public void paint(Graphics g) // display shapes on canvas
  {
    Graphics2D g2D=(Graphics2D) g; // cast to 2D
    g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                         RenderingHints.VALUE_ANTIALIAS_ON);
    int squareLen = (int)(50.0 * (double) (14.0/board.BOARD_SIZE));
    g.drawString("Left: "+board.tilesRemaining, 720, 50);
    g.drawString("Steps: "+board.steps, 720, 150);
    //g.setColor(Color.BLUE);
    //g.fillRect(50,50,50,50);
    int x = board.getBoardHeuristicValue();
    for(int i = 0; i < board.BOARD_SIZE; i++)
	{
		for(int j = 0; j < board.BOARD_SIZE; j++)
		{
			g.setColor(colorFromInt(board.board[i][j]));
		    g.fillRect(squareLen*i,squareLen*j,squareLen,squareLen);
		    if(board.showHeuristicOnMap)
		    {
		    	if(colorFromInt(board.board[i][j]).equals(Color.BLACK))
			    {
			    	g.setColor(Color.white);
			    }
			    else
			    {
			    	 g.setColor(Color.black);
			    }
			    g.drawString(board.heuristicMap[i][j]-1 + "", (int) (squareLen*i + squareLen/2.0), (int) (squareLen*j + squareLen/2.0));
		    }
		}
	}
    
    g.setColor(colorFromInt(0));
    g.fillRect(50,715,50,50);
    
    g.setColor(colorFromInt(1));
    g.fillRect(150,715,50,50);
    
    g.setColor(colorFromInt(2));
    g.fillRect(250,715,50,50);
    
    g.setColor(colorFromInt(3));
    g.fillRect(350,715,50,50);
    
    g.setColor(colorFromInt(4));
    g.fillRect(450,715,50,50);
    
    g.setColor(colorFromInt(5));
    g.fillRect(550,715,50,50);
    
  }
  private Color colorFromInt(int a)
  {
	  if(a == 0)
	  {
		  return Color.blue;
	  }
	  if(a == 1)
	  {
		  return Color.black;
	  }
	  if(a == 2)
	  {
		  return Color.green;
	  }
	  if(a == 3)
	  {
		  return Color.pink;
	  }
	  if(a == 4)
	  {
		  return Color.yellow;
	  }
		  return Color.red;
  }
 
  
}