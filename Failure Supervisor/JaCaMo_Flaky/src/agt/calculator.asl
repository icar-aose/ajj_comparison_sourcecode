// Agent calculator in project flakyExpressionCalculator

/* Initial beliefs and rules */

/* Initial goals */




!start.

/* Plans */

+!start : true <-  
	.my_name(Name);
	focusWhenAvailable("DirectoryService");
	addAgent(Name, ID);
	.print("hello world.", Name, " ", ID);
	Index = ID - 1;
	getStatus(Index, Status);
	.print("Agent: ", Name, " Index: ", ID, " status: ", Status);
	+myIndex(Index);
	.

+first : true <- 
	+iam(first);
	.my_name(Name);
	.
+second : true <-
	+iam(second);
	.
+root: true <-
	+iam(root);
	.

+!calc(Task)[source(Ancestor)] : Task = divide(First, Second)  <-
	+sender(Ancestor);
	+whatToDo(divide);
	!create_son(First, Second);
	.
+!calc(Task)[source(Ancestor)] : Task = multiply(First, Second) <-
	+sender(Ancestor);
	+whatToDo(multiply);
	!create_son(First, Second);
	.
+!calc(Task)[source(Ancestor)] : Task = add(First, Second) <-
    +sender(Ancestor);
    +whatToDo(add);
	!create_son(First, Second);
	.	
+!calc(Task)[source(Ancestor)] : Task = const(Num) <-
	?iam(NN);
	.send(Ancestor, tell, result(Num, NN));
	.
	
	
/*
+!calc(Task)[source(X)] : Task = const(Num) <-
	+sender(X);
	?iam(NN);
	.send(X, tell, result(Num, NN));
	.print(Num);
	.print("io sono ", NN, " e il mio risultato è ", Num);
	.print("ho finito");
	?myIndex(ID);
	setStatus(ID, "finished");
	.
*/

	
+!create_son(LL, RR) : true <-
	 ?myIndex(ID);
	 setStatus(ID, "operating");
	.my_name(X);
	.concat(X,"F", SubFirst);
	.concat(X,"S", SubSecond);
	.create_agent(SubFirst, "src/agt/calculator.asl");
	.create_agent(SubSecond, "src/agt/calculator.asl");
	.send(SubFirst, tell, first);
	.send(SubSecond, tell, second);
	.wait(1000);
	.send(SubFirst, achieve, calc(LL));
	.send(SubSecond, achieve, calc(RR));
	.

+!change_status: true <-
	?myIndex(ID);
	setStatus(ID, "finished");
	.
	
+result(Num, Order) : .count(result(_,_), X) & X == 2 & whatToDo(Opname) & Opname == add <-
	.print(Opname);
	!summy;
	.abolish(result(_,_));
	!change_status;
	.

+result(Num, Order) : .count(result(_,_), X) & X == 2 & whatToDo(Opname) & Opname == divide <-
	.print(Opname);
	!divvy;
	.abolish(result(_,_));
	!change_status;
	.
	
+result(Num, Order) : .count(result(_,_), X) & X == 2 & whatToDo(Opname) & Opname == multiply <-
	.print(Opname);
	!multy;
	.abolish(result(_,_));
	!change_status;
	.

{ include("src/agt/common.asl")}
{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation  
//{ include("$jacamoJar/templates/org-obedient.asl") }
