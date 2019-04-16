package flakyCalculator;
// CArtAgO artifact code for project flakyExpressionCalculator



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import cartago.*;

public class DirectoryService extends Artifact {
	private List<AgentRecord> agentList;
	private int agents = 0;
	
	
	void init(){
		this.defineObsProperty("numAgent", 0);
		agentList = new ArrayList<AgentRecord>();
		//AgentRecord item = new AgentRecord("Supervisor", "idle");
		//agentList.add(item);
		System.out.println("artifact Directory Service created ");
	}

	@OPERATION void addAgent(String agentName, OpFeedbackParam<Integer> id){
		AgentRecord item = new AgentRecord(agentName, "idle");
		agents++;
		agentList.add(item);
		id.set(agents);
		getObsProperty("numAgent").updateValue(agents);
		
	}
	@OPERATION void getAgent(int index, OpFeedbackParam<String> agentName) {
		AgentRecord item =  this.agentList.get(index);
		agentName.set(item.getAgentName());
	}
	@OPERATION void getStatus(int index, OpFeedbackParam<String> agentStatus) {
		AgentRecord item =  this.agentList.get(index);
		agentStatus.set(item.getAgentStatus());			
	}
	@OPERATION void setStatus(int index, String s) {
		AgentRecord item = this.agentList.get(index);
		item.setAgentStatus(s);
	}
	@OPERATION void getAgentList(OpFeedbackParam<String> lista) {
		String agentString = "";
		Iterator iterator = this.agentList.iterator();
		String name;
		String status;
		AgentRecord item;
		while (iterator.hasNext()) {
			item = (AgentRecord)iterator.next();
			name = new String(item.getAgentName());
			status = new String(item.getAgentStatus());
			agentString = agentString + name + "," + status + ",";
		}
		lista.set(agentString);	
	}
	@OPERATION void getOperatingAgent(OpFeedbackParam<Integer> num) {
		int numOperatingAgent = 0;
		Iterator<AgentRecord> iterator = this.agentList.iterator();
		String status;
		AgentRecord item;
		while (iterator.hasNext()) {
			item = (AgentRecord)iterator.next();
			status = new String(item.getAgentStatus());
			if (status.startsWith("operating")) {
			 numOperatingAgent = numOperatingAgent + 1;
			}
		}
		num.set(numOperatingAgent);
	}
	@OPERATION void removeAgentList() {
		agentList.removeAll(agentList);
		agents = 0;
	}
}