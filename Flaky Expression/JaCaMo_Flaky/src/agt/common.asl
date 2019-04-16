+!divvy: true <-
	?iam(NN)
	.random(Random);
	if (Random < 0.2) {
		.kill_agent(NN);	
	}
	?result(NumS, first);
	?result(NumD, second);
	Quoto = NumS/NumD;
	?sender(Ancestor);
	?iam(NN)
	.send(Ancestor, tell, result(Quoto, NN));
	.my_name(Name);
	.print(Name, " division ", NumS, "/", NumD);
	.print(Name, " division finished", Quoto);
	.
	
+!multy: true <-
	?iam(NN)
	.random(Random);
	if (Random < 0.2) {
		.kill_agent(NN);	
	}
	?result(NumS, first);
	?result(NumD, second);
	Product = NumS * NumD;
	?sender(Ancestor);
	.send(Ancestor, tell, result(Prodotto, NN));
	.my_name(Name);
	.print(Name, " multiplication ", NumS, "*", NumD);
	.print(Name, "multiplication", Product);
	.
	
+!summy: true <-
	?iam(NN);
	.random(Random);
	if (Random < 0.2) {
		.kill_agent(NN);	
	}
	?result(NumS, first);
	?result(NumD, second);
	Tot = NumS + NumD;
	?sender(Ancestor);
	?iam(NN)
	.send(Ancestor, tell, result(Tot, NN));
	.my_name(Name);
	.print(Name, " sum ", NumS, " + ", NumD);
	.print(Name, " sum :", Tot);
	.
	