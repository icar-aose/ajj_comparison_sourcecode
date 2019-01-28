package Auction;

import jade.core.*;
import jade.core.Runtime;
import jade.core.behaviours.*;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.proto.AchieveREInitiator;
import jade.proto.ContractNetInitiator;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;


import java.util.*;

public class Giacomo extends Agent {

    //Agent's states:

       private static final String STATE_A = "Auctioneer";
       private static final String STATE_B = "Architect";

    private String[] Building = {"SitePreparation", "Floors", "Walls", "Roof", "WindowsDoors", "Plumbing", "ElectricalSystem", "Painting"};

    private int[] BuildingPrice = {2000, 1000, 1000, 2000, 2500, 500, 500, 1200};

    //the catalogue of winners
    private Winners win = new Winners();

    private AID[] bidders;

    private boolean foundBestBidder=true;

    private int nResponders;

    private int numBid=0;


    public void setup() {

        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("auction");
        template.addServices(sd);
        try {
            DFAgentDescription[] result = DFService.search(this, template);
            if (result.length > 0) {
                System.out.println("Trovati i seguenti " + result.length + " costruttori nel DF:");
                bidders = new AID[result.length];
                for (int i = 0; i < result.length; ++i) {
                    bidders[i] = result[i].getName();
                    System.out.println(bidders[i].getName());
                }
            }
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }


        FSMBehaviour fsm = new FSMBehaviour(this) {
            public int onEnd() {
                System.out.println("FSM behaviour completed. The Auction and the Construction terminated.");
                myAgent.doDelete();
                return super.onEnd();
            }
        };



        // Register state A (first state)
        fsm.registerFirstState(new Auction(this), STATE_A);

        // Register state B
        fsm.registerLastState(new Construction(this), STATE_B);

        fsm.registerTransition(STATE_A, STATE_B, 1);

        addBehaviour(fsm);


    }



//The house construction's function


    class Auction extends TickerBehaviour {

        private Giacomo myAgent;

        private Auction(Giacomo agent) {
            super(agent,5000);
            myAgent = agent;
            // System.out.println("Executing behaviour " + getBehaviourName());
        }



        @Override
        protected void onTick() {
            if(numBid==8)
            stop();
        }

        @Override
        public int onEnd() {

            return 1;

        }

        @Override
        public void onStart() {
                for (int i = 0; i < Building.length; i++)
                    addBehaviour(new Bidder(myAgent, Building[i], BuildingPrice[i]));
            }



        }


    public class Bidder extends ContractNetInitiator {

        private String itemName;
        private int bestPrice;
        private AID bestBidder = null;

        private Bidder(Giacomo agent, String itemName, int itemPrice) {

            super(agent, null);
            this.itemName = itemName;
            this.bestPrice = itemPrice;

        }

        protected Vector prepareCfps(ACLMessage cfp) {

            cfp = new ACLMessage(ACLMessage.CFP);
            cfp.setProtocol(FIPANames.InteractionProtocol.FIPA_ITERATED_CONTRACT_NET);
            cfp.setContent(itemName + "," + bestPrice + "," + bestBidder);
            cfp.setReplyByDate(new Date(System.currentTimeMillis() + 10000));

            for (AID bidder : bidders) {
                cfp.addReceiver(bidder);

            }
            nResponders = bidders.length;
            Vector v = new Vector();
            v.add(cfp);
            return v;
        }

        protected void handleAllResponses(Vector responses, Vector acceptances) {


            if (responses.size() < nResponders) {
                // Some responder didn't reply within the specified timeout
                System.out.println("Timeout expired: missing " + (nResponders - responses.size()) + " responses");
            }
            Enumeration elements = responses.elements();
            while (elements.hasMoreElements()) {
                ACLMessage msg = (ACLMessage) elements.nextElement();
                if (msg.getPerformative() == ACLMessage.PROPOSE) {
                    String[] parts = msg.getContent().split(",");
                    int itemPrice = Integer.parseInt(parts[1]);
                    if (itemPrice < bestPrice) {

                        bestPrice = itemPrice;
                        bestBidder = msg.getSender();
                        foundBestBidder = false;
                    }
                }
            }
            if (!foundBestBidder)
            {

                Vector<ACLMessage> newCfps = new Vector<>();
                newIteration(newCfps);
                // build new CFPs for agents
                elements = responses.elements();
                while (elements.hasMoreElements())
                {
                    ACLMessage msg = (ACLMessage) elements.nextElement();
                        ACLMessage reply = msg.createReply();
                        reply.setPerformative(ACLMessage.CFP);
                        reply.setContent(itemName + "," + bestPrice + "," + bestBidder);
                        newCfps.addElement(reply);
                }
                foundBestBidder = true;

            } else
                {
                    ACLMessage prop = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                    prop.setContent(itemName+","+bestPrice);
                    prop.addReceiver(bestBidder);
                    acceptances.add(prop);
                    System.out.println(itemName + " sell to the agent: " + bestBidder.getName());

                    if (itemName.equals("Painting")) {
                        memCompanyItem("ExternalPainting", bestBidder.getName(), bestPrice);
                        memCompanyItem("InternalPainting", bestBidder.getName(), bestPrice);
                    }
                    else
                    {
                    if (itemName.equals("WindowsDoors")) {
                        memCompanyItem("Windows", bestBidder.getName(), bestPrice);
                        memCompanyItem("Doors", bestBidder.getName(), bestPrice);
                    } else memCompanyItem(this.itemName, bestBidder.getName(), bestPrice);

                    }

                numBid++;
                if (numBid == Building.length) {
                    printCompanies();


                }

            }
        }
    }


    private AID getCompanies(String title) {

        int x = win.title.indexOf(title);
        return new AID(win.company.get(x));

    }

    private void memCompanyItem(final String title, String Company, final int price) {

        win.company.add(Company);
        win.title.add(title);
        win.price.add(price);

    }
    private void printCompanies() {

        for (int i = 0; i < win.company.size(); i++) {

            System.out.println("Company: " + win.company.get(i));
            System.out.println("item: " + win.title.get(i));
            System.out.println("price: " + win.price.get(i));
        }

    }


    class Construction extends TickerBehaviour {


        private Construction(Giacomo agent) {
            super(agent, 5000);
            myAgent = agent;

        }

        private Giacomo myAgent;
        ParallelBehaviour pb = new ParallelBehaviour(myAgent, ParallelBehaviour.WHEN_ALL);
        ParallelBehaviour pb2 = new ParallelBehaviour(myAgent, ParallelBehaviour.WHEN_ALL);

        SequentialBehaviour seq = new SequentialBehaviour(myAgent);

        @Override
        public int onEnd() {
            return 0;
        }

        @Override
        public void onStart() {

          //String[] Task = {"SitePreparation", "Floors", "Walls", "Roof", "Windows", "Doors", "Plumbing", "ElectricalSystem", "ExternalPainting", "InternalPainting"};


            seq.addSubBehaviour(new ConstructionS(myAgent, "SitePreparation")); // series tasks
            seq.addSubBehaviour(new ConstructionS(myAgent, "Floors"));
            seq.addSubBehaviour(new ConstructionS(myAgent, "Walls"));


            pb.addSubBehaviour(new ConstructionS(myAgent, "Roof"));
            pb.addSubBehaviour(new ConstructionS(myAgent, "Windows")); // parallels tasks
            pb.addSubBehaviour(new ConstructionS(myAgent, "Doors"));
            seq.addSubBehaviour(pb);

            pb2.addSubBehaviour(new ConstructionS(myAgent, "Plumbing"));
            pb2.addSubBehaviour(new ConstructionS(myAgent, "ElectricalSystem")); // parallels tasks
            pb2.addSubBehaviour(new ConstructionS(myAgent, "ExternalPainting"));
            seq.addSubBehaviour(pb2);

            seq.addSubBehaviour(new ConstructionS(myAgent, "InternalPainting"));  //series final task

            myAgent.addBehaviour(seq);
        }

        public void onTick() {
            if (seq.done())
                stop();
        }

    }

    public class ConstructionS extends AchieveREInitiator {

        String order;


        public ConstructionS(Giacomo agent, String seqOrder) {
            super(agent,null);
            order=seqOrder;
        }

        protected Vector prepareInitiations (ACLMessage msg) {
            msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
            // We want to receive a reply in 10 secs
            msg.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
            AID x = getCompanies(order);
            msg.setContent(order);
            msg.addReceiver(x);
            Vector v = new Vector();
            v.add(msg);
            return v;
        }

        protected void handleInform (ACLMessage inform){


            System.out.println("Agent "+inform.getSender().getName()+" successfully performed the requested action");



        }

    }
// End of inner class BookNegotiator






    public static void main(String[] args) throws StaleProxyException {

        Runtime rt = Runtime.instance();

        // Launch the Main Container (with the administration GUI on top) listening on port 8888a
        System.out.println(">>>>>>>>>>>>>>> Launching the platform Main Container...");
        Profile pMain = new ProfileImpl(null, 8888, null);
        //pMain.setParameter(Profile.GUI, "true");
        ContainerController mainCtrl = rt.createMainContainer(pMain);

        AgentController CompanyA = mainCtrl.createNewAgent("CompanyA", CompanyA.class.getName(), null);
        AgentController CompanyB = mainCtrl.createNewAgent("CompanyB", CompanyB.class.getName(), null);


        for(int i=0;i < 5;i++) {
            AgentController CompanyC =mainCtrl.createNewAgent("CompanyC"+i, CompanyC.class.getName(), null);
            CompanyC.start();
        }
        for(int i=0;i < 13;i++) {
            AgentController CompanyD = mainCtrl.createNewAgent("CompanyD"+i, CompanyD.class.getName(), null);
            CompanyD.start();
        }
        AgentController CompanyE = mainCtrl.createNewAgent("CompanyE", CompanyE.class.getName(), null);

        AgentController Giacomo = mainCtrl.createNewAgent("Giacomo", Giacomo.class.getName(), null);

        CompanyA.start();
        CompanyB.start();
        CompanyE.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Giacomo.start();

    }
}
