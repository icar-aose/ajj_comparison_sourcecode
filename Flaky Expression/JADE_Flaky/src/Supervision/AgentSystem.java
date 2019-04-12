package Supervision;


import jade.core.*;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import java.util.Date;
import java.util.Vector;

import com.google.gson.Gson;

import static Supervision.Expression.*;



public class AgentSystem extends Agent {


    // (3 + 5) / (2 * (1 + 1))
    Expression task = new Divide( new Add( new Const(3), new Const(5)),
           new Multiply(new Const(2), new Add( new Const(1), new Const(1))));

   // Expression task = new Multiply(new Const(3), new Add( new Const(1), new Const(1)));

    AID x = new AID ("CalculatorService@169.254.32.83:8888/JADE");

    public void setup()
    {
        //request to agent arithmeticService for execute the expr

        addBehaviour(new MathRequest(this,task));


    }

    public class MathRequest extends AchieveREInitiator {

        Expression task;


        public MathRequest(AgentSystem agent, Expression task) {
            super(agent,null);
            this.task=task;
        }

        protected Vector prepareInitiations (ACLMessage msg) {
            msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
            // We want to receive a reply in 10 secs
            msg.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
            msg.setContent(task.toString());
            msg.addReceiver(x);
            Vector v = new Vector();
            v.add(msg);
            return v;
        }

        protected void handleInform (ACLMessage inform){


            System.out.println("Agent "+inform.getSender().getName());


        }

    }

}