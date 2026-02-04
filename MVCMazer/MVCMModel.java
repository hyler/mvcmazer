package MVCMazer;

import java.util.Observable;

class MVCMModel
    extends Observable
{
    private int mazeWidth;
    private int mazeHeight;

    private boolean started;
    private boolean running;
    private boolean solved;

	private int cellSize;
	private int cellBorderSize;

    private static final int CELL_SIZE = 23;
    private static final int CELL_BORDER_SIZE = 2;

    private static final int DEFAULT_WIDTH = 30;
    private static final int DEFAULT_HEIGHT = 30;

    // This is the actual maze data structure.
    //
    private MazeCell maze[][];

    MVCMModel()
    {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }


    MVCMModel(int width, int height)
    {
        this.mazeWidth = width;
        this.mazeHeight = height;

		this.cellSize = CELL_SIZE;
		this.cellBorderSize = CELL_BORDER_SIZE;

        maze = new MazeCell[this.mazeWidth][this.mazeHeight];

        for (int x = 0; x < this.mazeWidth; x++)
            for (int y = 0; y < this.mazeHeight; y++)
                maze[x][y] = new MazeCell(x, y, true, true, true, true);

		setStarted(false);
		setRunning(false);
		setSolved(false);
    }

	// Recreate the maze structure, with new dimensions and cell and border
	// sizes. This function does not do sanity checking, it should be handled
	// elsewhere (the controller, etc.).
	void newGrid(
        int newMazeWidth,
        int newMazeHeight,
        int newCellSize,
        int newCellBorderSize
    )
	{
		setStarted(false);
		setRunning(false);
		setSolved(false);

		maze = null;

		this.mazeWidth = newMazeWidth;
		this.mazeHeight = newMazeHeight;

		this.cellSize = newCellSize;
		this.cellBorderSize = newCellBorderSize;

		maze = new MazeCell[this.mazeWidth][this.mazeHeight];

        for (int x = 0; x < this.mazeWidth; x++)
            for (int y = 0; y < this.mazeHeight; y++)
                maze[x][y] = new MazeCell(x, y, true, true, true, true);

		setChanged();
		notifyObservers();
	}

    // Return the specified maze cell as a MazeCell object.
    MazeCell getCellAsObject(int x, int y)
    {
        if (validX(x) && validY(y))
            return maze[x][y];
        else
            return null;
    }

    boolean validCell(int x, int y)
    {
        if (validX(x) && validY(y))
            return true;

        return false;
    }

    boolean validCell(MazeCell c)
    {
        if (validX(c.getX()) && validY(c.getY()))
            return true;

        return false;
    }

    boolean validX(int x)
    {
        if (x >= 0 && x < mazeWidth)
            return true;

        return false;
    }

    boolean validY(int y)
    {
        if (y >= 0 && y < mazeHeight)
            return true;

        return false;
    }

    boolean getCellWallNorth(int x, int y)
    {
        return maze[x][y].getWallNorth();
    }

    boolean getCellWallEast(int x, int y)
    {
        return maze[x][y].getWallEast();
    }

    boolean getCellWallSouth(int x, int y)
    {
        return maze[x][y].getWallSouth();
    }

    boolean getCellWallWest(int x, int y)
    {
        return maze[x][y].getWallWest();
    }

    void setCellWallNorth(int x, int y, boolean b)
    {
        if (y != 0)
            maze[x][y - 1].setWallSouth(b);

        maze[x][y].setWallNorth(b);

        setChanged();
        notifyObservers();
    }

    void setCellWallEast(int x, int y, boolean b)
    {
        if (x != mazeWidth - 1)
            maze[x + 1][y].setWallWest(b);

        maze[x][y].setWallEast(b);

        setChanged();
        notifyObservers();
    }

    void setCellWallSouth(int x, int y, boolean b)
    {
        if (y != mazeHeight - 1)
            maze[x][y + 1].setWallNorth(b);

        maze[x][y].setWallSouth(b);

        setChanged();
        notifyObservers();
    }

    void setCellWallWest(int x, int y, boolean b)
    {
        if (x != 0)
            maze[x - 1][y].setWallEast(b);

        maze[x][y].setWallWest(b);

        setChanged();
        notifyObservers();
    }

    void toggleCellWallNorth(int x, int y)
    {
        if (getCellWallNorth(x, y))
            setCellWallNorth(x, y, false);
        else
            setCellWallNorth(x, y, true);
    }

    void toggleCellWallEast(int x, int y)
    {
        if (getCellWallEast(x, y))
            setCellWallEast(x, y, false);
        else
            setCellWallEast(x, y, true);
    }

    void toggleCellWallSouth(int x, int y)
    {
        if (getCellWallSouth(x, y))
            setCellWallSouth(x, y, false);
        else
            setCellWallSouth(x, y, true);
    }

    void toggleCellWallWest(int x, int y)
    {
        if (getCellWallWest(x, y))
            setCellWallWest(x, y, false);
        else
            setCellWallWest(x, y, true);
    }

    void setCellWall(int x, int y, char w, boolean b)
    {
        w = Character.toLowerCase(w);

        switch (w)
        {
            case 'n':
                setCellWallNorth(x, y, b);
            break;

            case 'e':
                setCellWallEast(x, y, b);
            break;

            case 's':
                setCellWallSouth(x, y, b);
            break;

            case 'w':
                setCellWallWest(x, y, b);
            break;
        }
    }

    void setCellWalls(int x, int y, boolean n, boolean e, boolean s, boolean w)
    {
        setCellWallNorth(x, y, n);
        setCellWallEast(x, y, e);
        setCellWallSouth(x, y, s);
        setCellWallWest(x, y, w);

		setChanged();
		notifyObservers();
    }

    boolean getVisited(int x, int y)
    {
        return maze[x][y].getVisited();
    }

    void setVisited(int x, int y, boolean v)
    {
        maze[x][y].setVisited(v);

        setChanged();
        notifyObservers();
    }

    boolean getBacktracked(int x, int y)
    {
        return maze[x][y].getBacktracked();
    }

    void setBacktracked(int x, int y, boolean b)
    {
        maze[x][y].setBacktracked(b);

        setChanged();
        notifyObservers();
    }

    int getMazeWidth()
    {
        return mazeWidth;
    }

    int getMazeHeight()
    {
        return mazeHeight;
    }

    int getCellSize()
    {
        return cellSize;
    }

    int getCellBorderSize()
    {
        return cellBorderSize;
    }

    boolean getStarted()
    {
        return started;
    }

    void setStarted(boolean s)
    {
        started = s;

		setChanged();
		notifyObservers();
    }

    boolean getRunning()
    {
        return running;
    }

    void setRunning(boolean r)
    {
        running = r;

        setChanged();
        notifyObservers();
    }

    boolean getSolved()
    {
        return solved;
    }

    void setSolved(boolean s)
    {
        solved = s;

		setChanged();
		notifyObservers();
    }

    void reset()
    {
        setStarted(false);
		setRunning(false);
		setSolved(false);

        for (int x = 0; x < mazeWidth; x++)
            for (int y = 0; y < mazeHeight; y++)
            {
                setCellWalls(x, y, true, true, true, true);
                setVisited(x, y, false);
                setBacktracked(x, y, false);
            }
    }

    /*
    void carve_passage_from(int x, int y)
    {
        int nextX;
        int nextY;

        Map<Character, Integer> DX = new HashMap<Character, Integer>();
        Map<Character, Integer> DY = new HashMap<Character, Integer>();
        Map<Character, Character> OP = new HashMap<Character, Character>();
        List<Character> directions = new ArrayList<Character>();

        setVisited(x, y, true);

        DX.put('n', 0);
        DX.put('e', 1);
        DX.put('s', 0);
        DX.put('w', -1);

        DY.put('n', -1);
        DY.put('e', 0);
        DY.put('s', 1);
        DY.put('w', 0);

        OP.put('n', 's');
        OP.put('e', 'w');
        OP.put('s', 'n');
        OP.put('w', 'e');

        directions.add('n');
        directions.add('e');
        directions.add('s');
        directions.add('w');
        Collections.shuffle(directions);

        for (Character direction : directions)
        {
            nextX = x + DX.get(direction);
            nextY = y + DY.get(direction);

            if (inWidth(nextX) &&
                inHeight(nextY) &&
                !getVisited(nextX, nextY))
            {
                setCellWall(x, y, direction, false);

                carve_passage_from(nextX, nextY);
            }
        }
    }
    */

    void carve_passage_from_stack(int x, int y)
    {

    }
}

// This represents a single cell, and has fields for keeping track of what's
// the status of the cell.
class MazeCell
{
    private int coordinate_x;
    private int coordinate_y;

    private boolean wall_n;
    private boolean wall_e;
    private boolean wall_s;
    private boolean wall_w;

    private boolean entrance;
	private boolean exit;
    private boolean enclosed;
    private boolean occupied;
    private boolean visited;
    private boolean backtracked;

    MazeCell(int x, int y, boolean n, boolean e, boolean s, boolean w)
    {
        coordinate_x = x;
        coordinate_y = y;

        wall_n = n;
        wall_e = e;
        wall_s = s;
        wall_w = w;

        entrance = false;
		exit = false;
        enclosed = false;
        occupied = false;
        visited = false;
        backtracked = false;
    }

    int getX()
    {
        return coordinate_x;
    }

    int getY()
    {
        return coordinate_y;
    }

    void setWalls(boolean n, boolean e, boolean s, boolean w)
    {
        wall_n = n;
        wall_e = e;
        wall_s = s;
        wall_w = w;
    }

    boolean getWallNorth() { return wall_n; }
    boolean getWallEast() { return wall_e; }
    boolean getWallSouth() { return wall_s; }
    boolean getWallWest() { return wall_w; }

    void setWallNorth(boolean n) { wall_n = n; }
    void setWallEast(boolean e) { wall_e = e; }
    void setWallSouth(boolean s) { wall_s = s; }
    void setWallWest(boolean w) { wall_w = w; }

    boolean getEntrance() { return entrance; }
    void setEntrance(boolean e) { entrance = e; }

    boolean getEnclosed() { return enclosed; }
    void setEnclosed(boolean e) { enclosed = e; }

    boolean getOccupied() { return occupied; }
    void setOccupied(boolean o) { occupied = o; }

    boolean getVisited() { return visited; }
    void setVisited(boolean v) { visited = v; }

    boolean getBacktracked() { return backtracked; }
    void setBacktracked(boolean b) { backtracked = b; }

    boolean getExit() { return exit; }
    void setExit(boolean e) { exit = e; }
}
