package Auction;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.proto.SSIteratedContractNetResponder;
import jade.proto.SSResponderDispatcher;

import java.util.Random;


/**
 * JADE agent representing a bidder of an auction.
 * It has single sequential behavior representing its lifecycle.
 * It will terminate after the budget runs out.
 */

public class CompanyD extends Agent {


    String[] Building = {"SitePreparation", "Floors", "Walls", "Roof", "WindowsDoors", "Plumbing", "ElectricalSystem", "Painting"};
    boolean[] BuildingBid = {false,false,false,false,false,false,false,false};


    // Random number generator
    private Random rn = new Random();

    // Put agent initializations here
    protected void setup() {


        // Register as bidder to the yellow pages
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("auction");
        sd.setName("Auction");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        MessageTemplate template = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_ITERATED_CONTRACT_NET),
                MessageTemplate.MatchPerformative(ACLMessage.CFP));

        addBehaviour(new SSResponderDispatcher(this, template) {


            protected Behaviour createResponder(ACLMessage initiationMsg) {

                return new SSIteratedContractNetResponder(myAgent, initiationMsg) {

                    @Override
                    protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException {

                        String proposal = evaluateAction(cfp.getContent());
                        ACLMessage propose;
                        if (!proposal.equals("nothing")) {
                            // We provide a proposal
                            System.out.println("Agent " + getLocalName() + ": Proposing " + proposal);
                            propose = cfp.createReply();
                            propose.setPerformative(ACLMessage.PROPOSE);
                            propose.setContent(proposal);
                            return propose;
                        } else {
                            // We refuse to provide a proposal
                           // System.out.println("Agent " + getLocalName() + ": Refuse");
                            throw new RefuseException("evaluation-failed");
                        }

                    }


                    protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept)  {

                        ACLMessage inform = accept.createReply();

                        System.out.println("Agent"+getLocalName()+": I'm the winner for "+accept.getContent());
                        inform.setPerformative(ACLMessage.INFORM);

                        return inform;

                    }

                };

            }

        });

        MessageTemplate t = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST) );

        addBehaviour(new AchieveREResponder(this, t) {
            protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
                System.out.println("Agent "+getLocalName()+": REQUEST received from "+request.getSender().getName()+". Action is "+request.getContent());

                // We agree to perform the action. Note that in the FIPA-Request
                // protocol the AGREE message is optional. Return null if you
                // don't want to send it.
                System.out.println("Agent "+getLocalName()+": Agree");
                ACLMessage agree = request.createReply();
                agree.setPerformative(ACLMessage.AGREE);
                return agree;
            }


            protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Agent "+getLocalName()+": Action successfully performed");
                ACLMessage inform = request.createReply();
                inform.setPerformative(ACLMessage.INFORM);
                return inform;

            }
        } );

    }

    private String evaluateAction(String cfp) {
        String[] parts = cfp.split(",");
        String itemName = parts[0];
        String ans="nothing";
        for(int i=0;i<Building.length;i++) {
            if (Building[i].equals(itemName) && !BuildingBid[i]) {
                BuildingBid[i] = true;
                int price = rn.nextInt(10000) + 800;
                ans=itemName + "," + price;
            }
        }
        return ans;
    }


}