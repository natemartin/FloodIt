import java.util.Random;
public class Board {
	
	public byte[][]board;
	public byte[][]owned;
	public byte[][]current;
	public byte[][]heuristicMap;
	public int tilesRemaining;
	public int steps;
	public boolean showHeuristicOnMap = false;
	
	public Random r;
	
	public static final int BOARD_SIZE = 50;
	
	public Board()
	{
		tilesRemaining = BOARD_SIZE*BOARD_SIZE - 1;
		steps = 0;
		r = new Random(6);
		board = new byte[BOARD_SIZE][BOARD_SIZE];
		owned = new byte[BOARD_SIZE][BOARD_SIZE];
		heuristicMap = new byte[BOARD_SIZE][BOARD_SIZE];
		for(int i = 0; i < BOARD_SIZE; i++)
		{
			for(int j = 0; j < BOARD_SIZE; j++)
			{
				board[i][j] = (byte) r.nextInt(6);
				owned[i][j] = 0;
			}
		}
		while(board[0][0] == board[1][0])
		{
			board[1][0] = (byte) r.nextInt(6);
		}
		while(board[0][0] == board[0][1])
		{
			board[0][1] = (byte) r.nextInt(6);
		}
		owned[0][0] = 1;
	}
	public Board(Board old)
	{
		this.board = new byte[BOARD_SIZE][BOARD_SIZE];
		this.owned = new byte[BOARD_SIZE][BOARD_SIZE];
		this.heuristicMap = new byte[BOARD_SIZE][BOARD_SIZE];
		this.r = old.r;
		for(int i = 0; i < BOARD_SIZE; i++)
		{
			for(int j = 0; j < BOARD_SIZE; j++)
			{
				board[i][j] = old.board[i][j];
				owned[i][j] = old.owned[i][j];
			}
		}
		
		this.current = old.current;
		this.tilesRemaining = old.tilesRemaining;
		this.steps = old.steps;
	}
	public int getBoardHeuristicValue()
	{
		int[] colorCount = {0,0,0,0,0,0};
		int colorsum = 0;
		for(int i = 0; i < BOARD_SIZE; i++)
		{
			for(int j = 0; j < BOARD_SIZE; j++)
			{
				heuristicMap[i][j] = owned[i][j];
			}
		}
		
		int[] colorcounts = new int[BOARD_SIZE*BOARD_SIZE];
		int numflips = 1;
		int currentNum = 1;
		while(numflips > 0)
		{
			numflips = 0;
			colorcounts[currentNum] = 0;
			for(int i = 0; i < BOARD_SIZE; i++)
			{
				for(int j = 0; j < BOARD_SIZE; j++)
				{
					if(heuristicMap[i][j] == currentNum)
					{
						if(i > 0)
						{
							if(heuristicMap[i-1][j] == 0)
							{
								colorCount[board[i-1][j]] = 1;
								heuristicMap[i-1][j] = (byte) (currentNum + 1);
								hFlipAdjacent(i-1,j,currentNum + 1);
								numflips++;
							}
						}
						if(i < BOARD_SIZE-1)
						{
							if(heuristicMap[i+1][j] == 0)
							{
								colorCount[board[i+1][j]] = 1;
								heuristicMap[i+1][j] = (byte) (currentNum + 1);
								hFlipAdjacent(i+1,j,currentNum + 1);
								numflips++;
							}
						}
						if(j > 0)
						{
							if(heuristicMap[i][j-1] == 0)
							{
								colorCount[board[i][j-1]] = 1;
								heuristicMap[i][j-1] = (byte) (currentNum + 1);
								hFlipAdjacent(i,j-1,currentNum + 1);
								numflips++;
							}
						}
						if(j < BOARD_SIZE-1)
						{
							if(heuristicMap[i][j+1] == 0)
							{
								colorCount[board[i][j+1]] = 1;
								heuristicMap[i][j+1] = (byte) (currentNum + 1);
								hFlipAdjacent(i,j+1,currentNum + 1);
								numflips++;
							}
						}	
					}
				}
			}
			int sum = 0;
			for(int i=0;i<6;i++)
			{
				sum+=colorCount[i];
				colorCount[i] = 0;
			}
			if(sum > 0)
			{
				colorcounts[currentNum] = sum - 1;
			}
			else
			{
				colorcounts[currentNum] = sum;
			}
				colorsum = sum;	
			currentNum++;
		}
		
		int maxH = 0;
		int pos = 1;
		while(true)
		{
			if(pos == currentNum) break;
			if(colorcounts[pos] + pos > maxH) maxH = colorcounts[pos] + pos;
			pos++;
			
		}
		
		return maxH - 1;
	}
	public void hFlipAdjacent(int x, int y, int cost)
	{
		if(x > 0)
		{
			if(board[x-1][y] == board[x][y])
			{
				if(heuristicMap[x-1][y] == 0)
				{
					heuristicMap[x-1][y] = (byte)cost;
					hFlipAdjacent(x-1,y, cost);
				}
			}
		}
		if(x < BOARD_SIZE-1)
		{
			if(board[x+1][y] == board[x][y])
			{
				if(heuristicMap[x+1][y] == 0)
				{
					heuristicMap[x+1][y] = (byte)cost;
					hFlipAdjacent(x+1,y, cost);
				}
			}
		}
		if(y > 0)
		{
			if(board[x][y-1] == board[x][y])
			{
				if(heuristicMap[x][y-1] == 0)
				{
					heuristicMap[x][y-1] = (byte)cost;
					hFlipAdjacent(x,y-1,cost);
				}
			}
		}
		if(y < BOARD_SIZE-1)
		{
			if(board[x][y+1] == board[x][y])
			{
				if(heuristicMap[x][y+1] == 0)
				{
					heuristicMap[x][y+1] = (byte)cost;
					hFlipAdjacent(x,y+1,cost);
				}
			}
		}
	}
	public void resetBoard()
	{
		tilesRemaining = BOARD_SIZE*BOARD_SIZE - 1;
		steps = 0;
		board = new byte[BOARD_SIZE][BOARD_SIZE];
		owned = new byte[BOARD_SIZE][BOARD_SIZE];
		for(int i = 0; i < BOARD_SIZE; i++)
		{
			for(int j = 0; j < BOARD_SIZE; j++)
			{
				board[i][j] = (byte) r.nextInt(6);
				owned[i][j] = 0;
			}
		}
		while(board[0][0] == board[1][0])
		{
			board[1][0] = (byte) r.nextInt(6);
		}
		while(board[0][0] == board[0][1])
		{
			board[0][1] = (byte) r.nextInt(6);
		}
		owned[0][0] = 1;
	}
	public void step(int a)
	{
		steps++;
		current = owned;
		for(int i = 0; i < BOARD_SIZE; i++)
		{
			for(int j = 0; j < BOARD_SIZE; j++)
			{
				if(owned[i][j] == 1)
				{
					flipAdjacent(i,j,a);
					board[i][j] = (byte) a;
				}
			}
		}
		owned = current;
		tilesRemaining = 0;
		for(int i = 0; i < BOARD_SIZE; i++)
		{
			for(int j = 0; j < BOARD_SIZE; j++)
			{
				if(owned[i][j] == 0)
				{
					tilesRemaining++;
				}
			}
		}	
	}
	private void flipAdjacent(int x, int y, int c)
	{
		if(x > 0)
		{
			if(board[x-1][y] == c)
			{
				if(current[x-1][y] == 0)
				{
					current[x-1][y] = 1;
					flipAdjacent(x-1,y,c);
				}
			}
		}
		if(x < BOARD_SIZE-1)
		{
			if(board[x+1][y] == c)
			{
				if(current[x+1][y] == 0)
				{
					current[x+1][y] = 1;
					flipAdjacent(x+1,y,c);
				}
			}
		}
		if(y > 0)
		{
			if(board[x][y-1] == c)
			{
				if(current[x][y-1] == 0)
				{
					current[x][y-1] = 1;
					flipAdjacent(x,y-1,c);
				}
			}
		}
		if(y < BOARD_SIZE-1)
		{
			if(board[x][y+1] == c)
			{
				if(current[x][y+1] == 0)
				{
					current[x][y+1] = 1;
					flipAdjacent(x,y+1,c);
				}
			}
		}
		
	}
}
