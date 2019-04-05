// Agent sample_agent in project flakyExpressionCalculator

/* Initial beliefs and rules */


//task(divide(add(const(3), const(5)),multiply(const(2), add(const(1), const(1))))).
      

/* Initial goals */


!start.

/* Plans */

+!start : true <-
	makeArtifact("flakyGui", "flakyCalculator.GuiArtifact", [], Id);
	focus(Id);
	+gui(Id);
	makeArtifact("DirectoryService", "flakyCalculator.DirectoryService", [], DSId);
	focus(DSId);
	+dirService(DSId);
	/*
	S = divide(add(const(3), const(5)),multiply(const(2), add(const(1), const(1))));
	S = multiply(add(const(3), const(5)), divide(add(const(1), const(1)), const(0)));
	.create_agent(root, "src/agt/calculator.asl");
	.send(root, tell, root);
	.send(root, achieve, new(S));
	*/
	.
+calculate: true <-
	addAgent("supervisor", ID);
	?espressione(S);
	.print("expression to calculate: ", S);
	.term2string(T,S);
	.print(T);
	.create_agent(root, "src/agt/calculator.asl");
	.send(root, tell, root);
	.wait(1000);
	.send(root, achieve, calc(T));
	-calculate;
	!check_agent;
	.
	
-calculate: true <-
	setResult("Malformed Expression")[artifact_id(X)];
	+reset;
	.

+reset: result(_,_) <-
	.all_names(Agents);
	//.print("Agenti Rimasti", Agents);
	//.delete("supervisor", Agents, L)
	.length(Agents, Lun);
	for (.range(I, 0, Lun - 1)) {
		 .nth(I, Agents, Name);
		 if (.substring("super", Name)) {
		 	.print("****");
		 } else {
		 	.kill_agent(Name);
		 }
	}
	removeAgentList;
	.abolish(result(_,_));
	.abolish(numAgent(_));
	.drop_all_intentions;
	.drop_all_desires;
	-reset;
	.

	
+reset: true <-
	setResult("operazione fallita")[artifact_id(X)];
	.all_names(Agents);
	//.print("Agenti Rimasti", Agents);
	//.delete("supervisor", Agents, L)
	.length(Agents, Lun);
	for (.range(I, 0, Lun - 1)) {
		 .nth(I, Agents, Name);
		 if (.substring("super", Name)) {
		 	.print("****");
		 } else {
		 	.kill_agent(Name);
		 }
	}
	removeAgentList;
	.abolish(result(_,_));
	.abolish(numAgent(_));
	.drop_all_intentions;
	.drop_all_desires;
	-reset;
	.
	
			
+!check_agent: true <-
	.wait(5000);
	+reset;
	.



+result(Num, Name) : true <-
	.wait(1000);
	.print("il risultato é ottenuto  ", Num, " ricevuto da ", Name);
	?gui(X);
	.term2string(Num, S);
	setResult(S)[artifact_id(X)];
	+reset;
	.				
	
+failure[source(X)] : true <-
	.print("espressione malformata from ", X);
	+reset;
	.


{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation  
//{ include("$jacamoJar/templates/org-obedient.asl") }
