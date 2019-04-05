package Supervision;

import jade.core.*;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;


public class Main {


    public static void main(String[] args) throws StaleProxyException {


        Runtime rt = Runtime.instance();

       //  Launch the Main Container (with the administration GUI on top) listening on port 8888a
        System.out.println(">>>>>>>>>>>>>>> Launching the platform Main Container...");
        Profile pMain = new ProfileImpl(null, 8888, null);
        pMain.setParameter(Profile.GUI, "true");
        ContainerController mainCtrl = rt.createMainContainer(pMain);





        AgentController AgentSystem = mainCtrl.createNewAgent("AgentSystem", AgentSystem.class.getName(), null);
        AgentController CalculatorService = mainCtrl.createNewAgent("CalculatorService", ArithmeticService.class.getName(), new Object[] {mainCtrl});



        AgentSystem.start();

        CalculatorService.start();

        //bisogna passare task a calculator
    }
}