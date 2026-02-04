package MVCMazer;

class MVCMApplication
{
    private MVCMModel model;
    private MVCMSolver solver;
    private MVCMView view;
    private MVCMController controller;

    MVCMApplication()
    {
        model = new MVCMModel(30, 30);  // Grid size (X by Y squares).
        view = new MVCMView(model);
        solver = new MVCMSolver(model, view);
        controller = new MVCMController(model, solver, view);
    }
}
