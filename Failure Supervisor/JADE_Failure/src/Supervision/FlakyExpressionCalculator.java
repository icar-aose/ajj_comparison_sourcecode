package Supervision;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

import java.util.concurrent.ThreadLocalRandom;

import static Supervision.Expression.*;

public class FlakyExpressionCalculator extends Agent {


    private int num_results =0; //n_ num_results of childs
    private int[] val_res = new int[2];

    public void setup (){

        Object[] args = getArguments();
        String val = (String) args[0];    //result
        Agent father = (Agent) args[1];       //Agent father
        ContainerController mainCtrl = (ContainerController) args[2];   //MainContainer
        final int recursive = (int) args[3];
        Expression expr=fromStringToExp(val);



        addBehaviour(new SimpleBehaviour(this) {
            

            @Override
            public void onStart() {
                if (expr instanceof Const) {
                    int value = ((Const) expr).getValue();
                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                    msg.setContent(String.valueOf(value));
                    System.out.println("Agent "+getLocalName()+" send result at: "+father.getAID()+" this value: "+value);
                    msg.addReceiver(father.getAID());
                    send(msg);
                    this.done();
                }
                else
                    {
                        try
                        {
                            AgentController left = mainCtrl.createNewAgent(myAgent.getLocalName()+"left"+recursive,FlakyExpressionCalculator.class.getName(), new Object[] {expr.getLeft().toString(),this.getAgent(),mainCtrl,recursive+1});
                            AgentController right = mainCtrl.createNewAgent(myAgent.getLocalName()+"right"+recursive,FlakyExpressionCalculator.class.getName(), new Object[] {expr.getRight().toString(),this.getAgent(),mainCtrl,recursive+1});

                            // start the agents
                            left.start();
                            right.start();
                        } catch (jade.wrapper.StaleProxyException e) {
                            System.err.println("Error launching agent...");
                        }
                    }
            }
            @Override
            public void action() {
                if (num_results == 2)
                {
                    try {
                        flakiness();
                        int result = evaluate(expr, val_res[0], val_res[1]);
                        System.out.println("Agent " + getLocalName() + " this is the result: " + result);
                        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                        msg.setContent(String.valueOf(result));
                        System.out.println("Agent "+getLocalName()+" send at: "+father.getAID()+" this value: "+result);
                        msg.addReceiver(father.getAID());
                        send(msg);
                        num_results++;
                    } catch (ArithmeticException exc) {
                        System.out.println(exc);

                    }catch (Exception exc){

                        System.out.println("Agent "+getLocalName()+" flakiness error, restart expr");
                        this.action();
                        //this.restart();
                    }

                }
                else{

                    MessageTemplate mt =MessageTemplate.MatchPerformative(ACLMessage.INFORM);
                    ACLMessage res = blockingReceive(mt);
                    //System.out.println("arrivato "+res.getContent());
                    //control the position of num_results
                    if(res.getSender().getLocalName().equals(myAgent.getLocalName()+"left"+recursive))
                        val_res[0]= Integer.parseInt(res.getContent());
                    else
                        val_res[1]= Integer.parseInt(res.getContent());
                    num_results++;
                }
            }
            public boolean done(){
                if (num_results==3  || expr instanceof Const)
                    {
                        System.out.println("Agent " + getLocalName() + " finished.");
                        return true;
                    }
                else return false;
            }
        });
    }





    // Encodes the original position of a sub-expression in its parent expression
    // Example: (4 / 2) has position Left in the original expression (4 / 2) * 3
    public static enum Position {
        Left, Right
    }

    public static class Result {
        private final Expression originalExpression;
        private final Integer value;
        private final Position position;

        public Result(Expression originalExpression, Integer value, Position position) {
            this.originalExpression = originalExpression;
            this.value = value;
            this.position = position;
        }

        public Expression getOriginalExpression() {
            return originalExpression;
        }

        public Integer getValue() {
            return value;
        }

        public Position getPosition() {
            return position;
        }
    }



    // This actor has the sole purpose of calculating a given expression and
    // return the result to its parent. It takes an additional argument,
    // myPosition, which is used to signal the parent which side of its
    // expression has been calculated.
   // private final Expression expr;
   // private final Position myPosition;

   // private Expression getExpr() { return expr; }


    // The value of these variables will be reinitialized after every restart.
    // The only stable data the actor has during restarts is those embedded in
    // the Props when it was created. In this case expr, and myPosition.
   // Map<Position, Integer> num_results  = new HashMap<>();
   // Set<Position> expected = Stream.of(Position.Left, Position.Right).collect(Collectors.toSet());



//    public FlakyExpressionCalculator(Expression expr, Position myPosition) {
//        this.expr = expr;
//        this.myPosition = myPosition;
//    }


    private Integer evaluate(Expression expr, Integer left, Integer right) {
        if (expr instanceof Add) {
            return left + right;
        } else if( expr instanceof Multiply) {
            return left * right;
        } else if (expr instanceof Divide) {
            return left / right;
        } else {
            throw new IllegalStateException("Unknown expression type " + expr.getClass());
        }
    }

    private void flakiness() throws Supervision.FlakinessException {
        if (ThreadLocalRandom.current().nextDouble() < 0.2)
            throw new Supervision.FlakinessException();
    }





}