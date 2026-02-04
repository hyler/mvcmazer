package MVCMazer;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

class MVCMController
{
    private MVCMModel model;
    private MVCMSolver solver;
    private MVCMView view;

    private ButtonListener bl;
    private SliderListener sl;

    MVCMController(MVCMModel m, MVCMSolver s, MVCMView v)
    {
        model = m;
        solver = s;
        view = v;

        bl = new ButtonListener(model, solver, view);
        sl = new SliderListener(model, solver, view);

        view.addRunListener(bl);
        view.addStepListener(bl);
        view.addResetListener(bl);
        view.addSpeedListener(sl);
		view.addNewGridListener(bl);
    }
}

class ButtonListener
    implements ActionListener
{
    private MVCMModel model;
    private MVCMSolver solver;
    private MVCMView view;

    ButtonListener(MVCMModel m, MVCMSolver s, MVCMView v)
    {
        model = m;
        solver = s;
        view = v;
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals("run"))
        {
            if (model.getRunning())
                solver.stop();
            else
                solver.start();
        }
        else if (e.getActionCommand().equals("step"))
        {
            if (model.getRunning())
                return;

            solver.step();
        }
        else if (e.getActionCommand().equals("reset"))
        {
            solver.reset();
        }
		else if (e.getActionCommand().equals("new"))
		{
			int cellSize, cellBorderSize;
			int width = view.getComboWidth();
			int height = view.getComboHeight();

			if (width == 100 || height == 100)
			{
				cellSize = 6;
				cellBorderSize = 1;
			}
			else if (width == 80 || height == 80)
			{
				cellSize = 8;
				cellBorderSize = 1;
			}
			else if (width == 50 || height == 50)
			{
				cellSize = 14;
				cellBorderSize = 1;
			}
			else if (width == 30 || height == 30)
			{
				cellSize = 23;
				cellBorderSize = 2;
			}
			else if (width == 20 || height == 20)
			{
				cellSize = 34;
				cellBorderSize = 3;
			}
			else
			{
				cellSize = 50;
				cellBorderSize = 4;
			}

			model.newGrid(width, height, cellSize, cellBorderSize);
		}
    }
}

class SliderListener
    implements ChangeListener
{
    private MVCMModel model;
    private MVCMSolver solver;
    private MVCMView view;

    private JSlider slider;

    SliderListener(MVCMModel m, MVCMSolver s, MVCMView v)
    {
        model = m;
        solver = s;
        view = v;
    }

    public void stateChanged(ChangeEvent e)
    {
        // Something something.
        slider = (JSlider)e.getSource();
        if (!slider.getValueIsAdjusting())
        {
            switch (slider.getValue())
            {
                case 1:
                    solver.setSpeed(30);
                break;

                case 2:
                    solver.setSpeed(100);
                break;

                case 3:
                    solver.setSpeed(500);
                break;

                case 4:
                    solver.setSpeed(1000);
                break;

                default:
                    solver.setSpeed(30);
            }
        }
    }
}
