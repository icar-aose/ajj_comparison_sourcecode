package flakyCalculator;

import java.util.List;

public class Test {

	public static void main(String[] args) {
		AgentTable myList;
		myList = new AgentTable();
		int id = myList.addAgent("pippo");
		id = myList.addAgent("pluto");
		id = myList.addAgent("topolino");
		id = myList.addAgent("paperino");
		id = myList.addAgent("clarabella");
		
		String list = myList.getAgentList();
		System.out.println(list);
		
		
		

	}

}
