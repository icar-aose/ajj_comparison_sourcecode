package flakyCalculator;

public class AgentRecord {
	private String agentName;
	private String agentStatus;

	public AgentRecord() {

	}
	public AgentRecord(String name) {
		setAgentName(name);
	}
	public AgentRecord(String name, String status) {
		setAgentName(name);
		setAgentStatus(status);
	}
	public void setAgentName(String s) {
		this.agentName = new String(s);
	}
	public void setAgentStatus(String s) {
		this.agentStatus = new String(s);
	}
	public String getAgentName() {
		return this.agentName;
	}
	public String getAgentStatus() {
		return this.agentStatus;
	}
}
