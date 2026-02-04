package MVCMazer;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.util.Observable;
import java.util.Observer;
import java.util.Hashtable;

class MVCMView
    implements Observer
{
    private MVCMModel model;

    private PControlPanel controlPanel;
    private PConsolePanel consolePanel;
    private PCanvasPanel canvasPanel;

    MVCMView(MVCMModel m)
    {
        model = m;

        model.addObserver(this);

        // Create a new JFrame container.
        JFrame jfrm = new JFrame("MVCMazer");

        // Specify BorderLayout manager.
        jfrm.getContentPane().setLayout(new BorderLayout());

        // Set not resizable.
        jfrm.setResizable(false);

        // Terminate the program when the user closes the application.
        jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the control panel.
        controlPanel = new PControlPanel(model);

        // Create the console panel.
        consolePanel = new PConsolePanel(model);

        // Create the canvas panel.
        canvasPanel = new PCanvasPanel(model);

        // Add the panels to the frame.
        jfrm.getContentPane().add(controlPanel, BorderLayout.LINE_START);
        jfrm.getContentPane().add(canvasPanel, BorderLayout.CENTER);
        jfrm.getContentPane().add(consolePanel, BorderLayout.PAGE_END);

        // Pack the frame.
        jfrm.pack();

        // Display the frame.
        jfrm.setVisible(true);
    }

    void addRunListener(ActionListener al)
    {
        controlPanel.addRunListener(al);
    }

    void addStepListener(ActionListener al)
    {
        controlPanel.addStepListener(al);
    }

    void addResetListener(ActionListener al)
    {
        controlPanel.addResetListener(al);
    }

    void addSpeedListener(ChangeListener cl)
    {
        controlPanel.addSpeedListener(cl);
    }

	void addNewGridListener(ActionListener al)
	{
		controlPanel.addNewGridListener(al);
	}

	int getComboWidth()
	{
		return controlPanel.getComboWidth();
	}

	int getComboHeight()
	{
		return controlPanel.getComboHeight();
	}

    void printString(String s)
    {
        consolePanel.printString(s);
    }

    public void update(Observable o, Object arg)
    {
        controlPanel.updateContents(o, arg);
        canvasPanel.updateContents(o, arg);
        consolePanel.updateContents(o, arg);
    }
}

class PControlPanel
    extends JPanel
{
    private MVCMModel model;

    private JButton btnRun;
    private JButton btnStep;
    private JButton btnReset;
    private JButton btnNew;

    private JSlider sldSpeed;

	private JComboBox cmbWidth;
	private JComboBox cmbHeight;
	private JComboBox cmbSolver;

    private JCheckBox chkLoop;
    private JCheckBox chkLog;

    PControlPanel(MVCMModel m)
    {
        model = m;

        setOpaque(true);
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));

        BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
        setLayout(layout);

        btnRun   = new JButton("Run");
        btnStep  = new JButton("Step");
        btnReset = new JButton("Reset");
        btnNew   = new JButton("Create New Grid");

        // Add action listeners for the buttons.
        btnRun.setActionCommand("run");
        btnStep.setActionCommand("step");
        btnReset.setActionCommand("reset");
        btnNew.setActionCommand("new");

        JLabel sliderLabel = new JLabel("Step delay:");
        JLabel widthLabel  = new JLabel("Width");
        JLabel heightLabel = new JLabel("Height");
        JLabel solverLabel = new JLabel("Generator:");

        String widthValues[]  = {"10", "20", "30", "50", "80", "100"};
        String heightValues[] = {"10", "20", "30", "50", "80", "100"};

        String generators[] =
            {
                "Depth-first search",
                "Kruskal",
                "Prim"
            };

        cmbWidth  = new JComboBox<String>(widthValues);
        cmbHeight = new JComboBox<String>(heightValues);
        cmbSolver = new JComboBox<String>(generators);

        cmbWidth.setMaximumSize(cmbWidth.getPreferredSize());
        cmbHeight.setMaximumSize(cmbHeight.getPreferredSize());
        cmbSolver.setMaximumSize(cmbSolver.getPreferredSize());

        cmbSolver.setEnabled(false);

        Hashtable<Integer, JLabel> labels;
        labels = new Hashtable<Integer, JLabel>();
        labels.put(1, new JLabel("30ms"));
        labels.put(2, new JLabel("0.1s"));
        labels.put(3, new JLabel("0.5s"));
        labels.put(4, new JLabel("1s"));

        sldSpeed = new JSlider(JSlider.HORIZONTAL, 1, 4, 1);
        sldSpeed.setLabelTable(labels);
        sldSpeed.setSnapToTicks(true);
        sldSpeed.setPaintLabels(true);

        chkLoop = new JCheckBox("Loop forever");
        chkLog  = new JCheckBox("Show activity log");

        chkLoop.setEnabled(false);
        chkLog.setEnabled(true);

        JPanel startStepRow = new JPanel();
        startStepRow.setLayout(
            new BoxLayout(startStepRow, BoxLayout.LINE_AXIS)
        );
        startStepRow.add(btnRun);
        startStepRow.add(btnStep);

        startStepRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnReset.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnNew.setAlignmentX(Component.CENTER_ALIGNMENT);
        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sldSpeed.setAlignmentX(Component.CENTER_ALIGNMENT);
        solverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        chkLoop.setAlignmentX(Component.CENTER_ALIGNMENT);
        chkLog.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel widthCol = new JPanel();
        widthCol.setLayout(
            new BoxLayout(widthCol, BoxLayout.PAGE_AXIS)
        );
        widthCol.add(widthLabel);
        widthCol.add(cmbWidth);
        widthLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cmbWidth.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel heightCol = new JPanel();
        heightCol.setLayout(
            new BoxLayout(heightCol, BoxLayout.PAGE_AXIS)
        );
        heightCol.add(heightLabel);
        heightCol.add(cmbHeight);
        heightLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cmbHeight.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel widthHeightRow = new JPanel();
        widthHeightRow.setLayout(
            new BoxLayout(widthHeightRow, BoxLayout.LINE_AXIS)
        );
        widthHeightRow.add(widthCol);
        widthHeightRow.add(heightCol);

        add(Box.createRigidArea(new Dimension(0, 20)));
        add(startStepRow);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(btnReset);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(sliderLabel);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(sldSpeed);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(chkLoop);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(btnNew);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(widthHeightRow);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(solverLabel);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(cmbSolver);
        add(Box.createVerticalGlue());
        add(chkLog);
        add(Box.createRigidArea(new Dimension(0, 20)));
    }

	int getComboWidth()
	{
		return Integer.parseInt(cmbWidth.getSelectedItem().toString());
	}

	int getComboHeight()
	{
		return Integer.parseInt(cmbHeight.getSelectedItem().toString());
	}

    void updateContents(Observable o, Object arg)
    {
        if (((MVCMModel)o).getRunning())
        {
            btnRun.setText("Pause");
            btnStep.setEnabled(false);

			btnNew.setEnabled(false);
			cmbWidth.setEnabled(false);
			cmbHeight.setEnabled(false);
        }
        else
        {
            btnRun.setText("Run");
            btnStep.setEnabled(true);

			btnNew.setEnabled(true);
			cmbWidth.setEnabled(true);
			cmbHeight.setEnabled(true);
        }
    }

    void addRunListener(ActionListener al)
    {
        btnRun.addActionListener(al);
    }

    void addStepListener(ActionListener al)
    {
        btnStep.addActionListener(al);
    }

    void addResetListener(ActionListener al)
    {
        btnReset.addActionListener(al);
    }

    void addSpeedListener(ChangeListener cl)
    {
        sldSpeed.addChangeListener(cl);
    }

	void addNewGridListener(ActionListener al)
	{
		btnNew.addActionListener(al);
	}

    public Dimension getMinimumSize()
    {
        return new Dimension(200, 800);
    }

    public Dimension getMaximumSize()
    {
        return new Dimension(200, 800);
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(200, 800);
    }
}

class PCanvasPanel
    extends JPanel
{
    private MVCMModel model;

    //private final Color MVCM_COLOR_CANVAS;
    //private final Color MVCM_COLOR_UNVISITED;
    //private final Color MVCM_COLOR_ENTRANCE;
    //private final Color MVCM_COLOR_EXIT;

    private final Color MVCM_COLOR_BORDER = new Color( 80,  80,  80);
    private final Color MVCM_COLOR_VISITED = new Color(200, 200, 200);
    private final Color MVCM_COLOR_BACKTRACKED = new Color(180, 180, 255);

    PCanvasPanel(MVCMModel m)
    {
        model = m;

        setOpaque(true);
    }

    void updateContents(Observable o, Object arg)
    {
        repaint();
    }

    public Dimension getMinimumSize()
    {
        return new Dimension(800, 800);
    }

    public Dimension getMaximumSize()
    {
        return new Dimension(800, 800);
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(800, 800);
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_OFF
        );

        g2.setRenderingHint(
            RenderingHints.KEY_DITHERING,
            RenderingHints.VALUE_DITHER_DISABLE
        );

        g2.setRenderingHint(
            RenderingHints.KEY_STROKE_CONTROL,
            RenderingHints.VALUE_STROKE_PURE)
        ;

        int mazeWidthInPixels = model.getMazeWidth() * model.getCellSize() +
            model.getMazeWidth() * model.getCellBorderSize() +
            model.getCellBorderSize();

        int mazeHeightInPixels = model.getMazeHeight() * model.getCellSize() +
            model.getMazeHeight() * model.getCellBorderSize() +
            model.getCellBorderSize();

        int mazeX = ((int)this.getSize().getWidth() - mazeWidthInPixels) / 2;
        int mazeY = ((int)this.getSize().getHeight() - mazeHeightInPixels) / 2;

        int northX1, northY1, northX2, northY2;
        int eastX1, eastY1, eastX2, eastY2;
        int southX1, southY1, southX2, southY2;
        int westX1, westY1, westX2, westY2;
		int shadingX1, shadingY1, shadingX2, shadingY2;
        int neCornerX, neCornerY;
        int seCornerX, seCornerY;
        int swCornerX, swCornerY;
        int nwCornerX, nwCornerY;

        for (int x = 0; x < model.getMazeWidth(); x++)
        {
            for (int y = 0; y < model.getMazeHeight(); y++)
            {
                // Draw the cell borders without the corners.
                northX1 = mazeX + x * model.getCellSize() +
                    (x + 1) * model.getCellBorderSize();
                northY1 = mazeY + y *
                    (model.getCellSize() + model.getCellBorderSize());

				northX2 = northX1 + model.getCellSize() -
                    model.getCellBorderSize();
                northY2 = northY1;

                eastX1 = mazeX + (x + 1) *
                    (model.getCellSize() + model.getCellBorderSize());
                eastY1 = mazeY + y * model.getCellSize() +
                    (y + 1) * model.getCellBorderSize();

                eastX2 = eastX1;
				eastY2 = eastY1 + model.getCellSize() -
                    model.getCellBorderSize();

                southX1 = mazeX + x * model.getCellSize() +
                    (x + 1) * model.getCellBorderSize();
                southY1 = mazeY + (y + 1) *
                    (model.getCellSize() + model.getCellBorderSize());

				southX2 = southX1 + model.getCellSize() -
                    model.getCellBorderSize();
                southY2 = southY1;

                westX1 = mazeX + x *
                    (model.getCellSize() + model.getCellBorderSize());
                westY1 = mazeY + y * model.getCellSize() +
                    (y + 1) * model.getCellBorderSize();

                westX2 = westX1;
				westY2 = westY1 + model.getCellSize() -
                    model.getCellBorderSize();

				g2.setStroke(new BasicStroke(model.getCellBorderSize()));

                if (model.getCellWallNorth(x, y))
                    g.setColor(MVCM_COLOR_BORDER);
                else
                {
                    if (model.getBacktracked(x, y) &&
                        model.getBacktracked(x, y - 1))
                        g.setColor(MVCM_COLOR_BACKTRACKED);
                    else
                        g.setColor(MVCM_COLOR_VISITED);
                }

                g2.fillRect(
                    northX1,
                    northY1,
                    model.getCellSize(),
                    model.getCellBorderSize()
                );

                if (model.getCellWallEast(x, y))
                    g.setColor(MVCM_COLOR_BORDER);
                else
                {
                    if (model.getBacktracked(x, y) &&
                        model.getBacktracked(x + 1, y))
                        g.setColor(MVCM_COLOR_BACKTRACKED);
                    else
                        g.setColor(MVCM_COLOR_VISITED);
                }

                g2.fillRect(
                    eastX1,
                    eastY1,
                    model.getCellBorderSize(),
                    model.getCellSize()
                );

                if (model.getCellWallSouth(x, y))
                    g.setColor(MVCM_COLOR_BORDER);
                else
                {
                    if (model.getBacktracked(x, y) &&
                        model.getBacktracked(x, y + 1))
                        g.setColor(MVCM_COLOR_BACKTRACKED);
                    else
                        g.setColor(MVCM_COLOR_VISITED);
                }

                g2.fillRect(
                    southX1,
                    southY1,
                    model.getCellSize(),
                    model.getCellBorderSize()
                );

                if (model.getCellWallWest(x, y))
                    g.setColor(MVCM_COLOR_BORDER);
                else
                {
                    if (model.getBacktracked(x, y) &&
                        model.getBacktracked(x - 1, y))
                        g.setColor(MVCM_COLOR_BACKTRACKED);
                    else
                        g.setColor(MVCM_COLOR_VISITED);
                }

                g2.fillRect(
                    westX1,
                    westY1,
                    model.getCellBorderSize(),
                    model.getCellSize()
                );

                // Draw the corners.
                neCornerX = mazeX + (x + 1) *
                    (model.getCellSize() + model.getCellBorderSize());
                neCornerY = mazeY + y *
                    (model.getCellSize() + model.getCellBorderSize());

                seCornerX = mazeX + (x + 1) *
                    (model.getCellSize() + model.getCellBorderSize());
                seCornerY = mazeY + (y + 1) *
                    (model.getCellSize() + model.getCellBorderSize());

                swCornerX = mazeX + x *
                    (model.getCellSize() + model.getCellBorderSize());
                swCornerY = mazeY + (y + 1) *
                    (model.getCellSize() + model.getCellBorderSize());

                nwCornerX = mazeX + x *
                    (model.getCellSize() + model.getCellBorderSize());
                nwCornerY = mazeY + y *
                    (model.getCellSize() + model.getCellBorderSize());

                g.setColor(MVCM_COLOR_BORDER);

                g2.fillRect(
                    neCornerX,
                    neCornerY,
                    model.getCellBorderSize(),
                    model.getCellBorderSize()
                );
                g2.fillRect(
                    seCornerX,
                    seCornerY,
                    model.getCellBorderSize(),
                    model.getCellBorderSize()
                );
                g2.fillRect(
                    swCornerX,
                    swCornerY,
                    model.getCellBorderSize(),
                    model.getCellBorderSize()
                );
                g2.fillRect(
                    nwCornerX,
                    nwCornerY,
                    model.getCellBorderSize(),
                    model.getCellBorderSize()
                );

                // Draw the cell shading.
                shadingX1 = mazeX + x * model.getCellSize() +
                    (x + 1) * model.getCellBorderSize();
                shadingY1 = mazeY + y * model.getCellSize() +
                    (y + 1) * model.getCellBorderSize();

                shadingX2 = shadingX1 + model.getCellSize();
                shadingY2 = shadingY1 + model.getCellSize();

                // Do not try to "optimize" these two ifs by taking the draw
                // call outside after both of them.
                if (model.getVisited(x, y))
                {
                    g.setColor(MVCM_COLOR_VISITED);
                    g.fillRect(
                        shadingX1,
                        shadingY1,
                        model.getCellSize(),
                        model.getCellSize()
                    );
                }

                if (model.getBacktracked(x, y))
                {
                    g.setColor(MVCM_COLOR_BACKTRACKED);
                    g.fillRect(
                        shadingX1,
                        shadingY1,
                        model.getCellSize(),
                        model.getCellSize()
                    );
                }
            }
        }
    }
}

class PConsolePanel
    extends JPanel
{
    private MVCMModel model;

    private SpringLayout layout;
    private JScrollPane  consoleScroll;
    private JTextArea    consoleText;

    PConsolePanel(MVCMModel m)
    {
        model = m;

        setOpaque(true);
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));

        layout = new SpringLayout();
        setLayout(layout);

        consoleText = new JTextArea();
        consoleText.setBorder(null);
        consoleText.setBorder(BorderFactory.createCompoundBorder(
            consoleText.getBorder(),
            BorderFactory.createEmptyBorder(4, 4, 4, 4))
        );
        consoleText.setEditable(false);
        consoleText.setLineWrap(true);
        consoleText.setFont(new Font("Monospaced", Font.PLAIN, 11));
        consoleText.setBackground(Color.LIGHT_GRAY);

        DefaultCaret caret = (DefaultCaret)consoleText.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        consoleScroll = new JScrollPane(consoleText);
        consoleScroll.setOpaque(false);
        consoleScroll.setBorder(null);

        add(consoleScroll);

        layout.putConstraint(
            SpringLayout.WEST,
            consoleScroll,
            0,
            SpringLayout.WEST,
            this
        );
        layout.putConstraint(
            SpringLayout.NORTH,
            consoleScroll,
            0,
            SpringLayout.NORTH,
            this
        );
        layout.putConstraint(
            SpringLayout.EAST,
            consoleScroll,
            0,
            SpringLayout.EAST,
            this
        );
        layout.putConstraint(
            SpringLayout.SOUTH,
            consoleScroll,
            0,
            SpringLayout.SOUTH,
            this
        );
    }

    void updateContents(Observable o, Object arg)
    {
		// Placeholder.
        if (((MVCMModel)o).getRunning())
            Math.abs(0);
        else
            Math.abs(0);
    }

    public Dimension getMinimumSize()
    {
        return new Dimension(1000, 200);
    }

    public Dimension getMaximumSize()
    {
        return new Dimension(1000, 200);
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(1000, 200);
    }

    void printString(String s)
    {
        consoleText.append(s.replace("\n", "") + "\n");
		//consoleText.append(s);
        //consoleText.setText(s);
    }
}
