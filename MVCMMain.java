package MVCMazer;

// This file only contains the entry point. Basically something to instantiate
// and run the MVCMApplication class.

class MVCMMain
{
    public static void main(String args[])
    {
        // Create the object on the event dispatching thread.
        javax.swing.SwingUtilities.invokeLater(
            new Runnable()
            {
                public void run()
                {
                    new MVCMApplication();
                }
            }
        );
    }
}
