package Supervision;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.wrapper.AgentController;


import jade.core.*;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;


import static Supervision.Expression.fromStringToExp;

// A very simple service that accepts arithmetic expressions and tries to
// evaluate them. Since the calculation is dangerous (at least for the sake
// of this example) it is delegated to a worker actor of type
// FlakyExpressionCalculator.
public class ArithmeticService  extends Agent {


    public void setup() {
        //Profile p = new ProfileImpl(false);
        Object[] args = getArguments();
        ContainerController mainCtrl = (ContainerController) args[0];

        int recursive = 0;

        MessageTemplate t = MessageTemplate.and(
            MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
            MessageTemplate.MatchPerformative(ACLMessage.REQUEST)

        );

        addBehaviour(new AchieveREResponder(this, t) {


            protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {

                System.out.println("Agent "+getLocalName()+": REQUEST received from "+request.getSender().getName());

                return null;
            }


            protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {

                Expression exp;
                exp=fromStringToExp(request.getContent());
                //System.out.println("ciao "+exp.toString());
                // create agent
                try
                {
                        AgentController left = mainCtrl.createNewAgent("Service",FlakyExpressionCalculator.class.getName(), new Object[] {exp.toString(),this.getAgent(), mainCtrl,recursive});
                    // start the agent
                    left.start();
                } catch (jade.wrapper.StaleProxyException e) {
                    System.err.println("Error launching agent...");
                }

                System.out.println("wait result ");
                MessageTemplate mt =MessageTemplate.MatchPerformative(ACLMessage.INFORM);
                ACLMessage reply = blockingReceive(mt);
                System.out.println("Agent "+getLocalName()+" FINAL RESULT "+reply.getContent());




                System.out.println("Agent "+getLocalName()+": Action successfully performed");
                ACLMessage inform = request.createReply();
                inform.setPerformative(ACLMessage.INFORM);
                return inform;

            }
        } );

    }


}
