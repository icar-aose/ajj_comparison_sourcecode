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
	.print(Name, " divisione ", NumS, "/", NumD);
	.print(Name, " divisione finita", Quoto);
	.
	
+!multy: true <-
	?iam(NN)
	.random(Random);
	if (Random < 0.2) {
		.kill_agent(NN);	
	}
	?result(NumS, first);
	?result(NumD, second);
	Prodotto = NumS * NumD;
	?sender(Ancestor);
	.send(Ancestor, tell, result(Prodotto, NN));
	.my_name(Name);
	.print(Name, " moltiplicazione ", NumS, "*", NumD);
	.print(Name, "moltiplicazione", Prodotto);
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
	.print(Name, " somma ", NumS, " + ", NumD);
	.print(Name, " somma finita", Tot);
	.
	