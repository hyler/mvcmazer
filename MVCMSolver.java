package MVCMazer;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Stack;
import javax.swing.Timer;
import java.awt.event.*;

class MVCMSolver
    implements ActionListener
{
    private MVCMModel model;
    private MVCMView view;

    private boolean started;
    private boolean running;
    private boolean solved;

    private Timer t;
    private int speed;

    private HashMap<Character, Integer> DX;
    private HashMap<Character, Integer> DY;
    private HashMap<Character, Character> OP;

    private ArrayList<Character> directions;
    private Stack<MazeCell> cellsStack;

    private int currentX;
    private int currentY;

    private int nextX;
    private int nextY;

    private MazeCell currentCell;

    private boolean moved;

    MVCMSolver(MVCMModel m, MVCMView v)
    {
        model = m;
        view = v;

        speed = 30;

        // Register ourselves ("this") as the listener for the timer events.
        // Actions are handled in the actionPerformed() method.
        t = new Timer(speed, this);

        DX = new HashMap<Character, Integer>();
        DY = new HashMap<Character, Integer>();
        OP = new HashMap<Character, Character>();

        directions = new ArrayList<Character>();
        cellsStack = new Stack<MazeCell>();

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

        currentX = 0;
        currentY = 0;

        nextX = 0;
        nextY = 0;
    }

    int getSpeed()
    {
        return speed;
    }

    void setSpeed(int s)
    {
        speed = s;
        t.setDelay(speed);
    }

    void start()
    {
        if (model.getSolved())
            model.reset();

        t.start();
        model.setRunning(true);
    }

    void stop()
    {
        t.stop();
        model.setRunning(false);
    }

    void step()
    {
        if (model.getSolved())
            return;

        if (!model.getStarted())
        {
            model.setStarted(true);

            // There was a bug here... reusing the stack can result in a larger
            // stack being used in a smaller maze and always having elements at
            // the end, causing a java.lang.ArrayIndexOutOfBoundsException when
            // attempting to set the backtracked property of a void cell.
            //
            cellsStack = new Stack<MazeCell>();

            view.printString("*** RUN STARTED ***\n");

            // Should provide for a way to specify a starting cell, or tell
            // the solver to pick one for us randomly (probably from the
            // periphery only).
			//
            currentX = 0;
            currentY = 0;

            model.setVisited(currentX, currentY, true);

            view.printString(
                "Starting in cell (" + currentX + ", " + currentY + ")\n"
            );

            currentCell = model.getCellAsObject(currentX, currentY);
            cellsStack.push(currentCell);
        }
        else
        {
            moved = false;

            Collections.shuffle(directions);

            view.printString("In cell (" + currentX + ", " + currentY + ")\n");

            for (Character direction : directions)
            {
                nextX = currentX + DX.get(direction);
                nextY = currentY + DY.get(direction);

                view.printString(
                    "  Trying to move to (" + nextX + ", " + nextY + ")"
                );

                if (model.validX(nextX) &&
                    model.validY(nextY) &&
                    !model.getVisited(nextX, nextY))
                {
                    view.printString("  |  OK.\n");

                    model.setCellWall(currentX, currentY, direction, false);
                    currentX = nextX;
                    currentY = nextY;

                    model.setVisited(currentX, currentY, true);

                    currentCell = model.getCellAsObject(currentX, currentY);
                    cellsStack.push(currentCell);
                    moved = true;

                    break;
                }
                else
                {
                    view.printString("  |  Invalid!\n");
                }
            }

            if (!moved && !cellsStack.empty())
            {
                currentCell = cellsStack.pop();
                currentX = currentCell.getX();
                currentY = currentCell.getY();

                model.setBacktracked(currentX, currentY, true);

                view.printString(
                    "Backtracking to (" + currentX + ", " + currentY + ")\n"
                );

                if (cellsStack.empty())
                {
                    stop();
                    model.setSolved(true);

                    // Give the garbage collector a chance!
                    //
                    cellsStack = null;
                }
            }
        }
    }

    void reset()
    {
        t.stop();
        model.reset();
    }

    // Called on timer ticks.
    public void actionPerformed(ActionEvent e)
    {
        step();
    }
}
