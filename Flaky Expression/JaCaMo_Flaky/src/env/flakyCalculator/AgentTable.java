package flakyCalculator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class AgentTable {
	private List<AgentRecord> agentList;
	private int agents = 0;
	
	public AgentTable() {
		agentList = new ArrayList<AgentRecord>();
	}
	public int addAgent(String agentName){
		AgentRecord item = new AgentRecord(agentName, "idle");
		agents++;
		agentList.add(item);
		return agents;

	}
	public String getAgent(int index) {
		AgentRecord item =  this.agentList.get(index);
		return item.getAgentName();
	}
	public String getStatus(int index) {
		AgentRecord item =  this.agentList.get(index);
		return item.getAgentStatus();			
	}
	public void setStatus(int index, String s) {
		AgentRecord item = this.agentList.get(index);
		item.setAgentStatus(s);
	}
	public String getAgentList() {
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
		return agentString;	
	}
	
	
}
