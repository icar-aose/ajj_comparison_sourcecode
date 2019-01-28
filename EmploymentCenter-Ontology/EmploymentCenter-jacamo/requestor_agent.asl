// Agent requestor_agent in project employment

/* Initial beliefs and rules */
company("",address("",0,"")).
engager(engager_agent).

/* Initial goals */

!setCompany.

/* Plans */

+!setCompany 
<- 
	Address = address("Via Monte Senario",98,"Roma");
	-+company("IES",Address);
	!handleEngagement
.

+!handleEngagement
<-
	Address = address("Via Vincenzo Di Marco",3,"Palermo");
	Person = person("Luca Sabatucci",42,Address);
	?company(CName,CAddress);
	WF = work_for(Person,company(CName,CAddress));
	
	?engager(Engager);
	.send(Engager,tell,query_if(WF));
.

+inform_result(WorkFor,true)
:
	WorkFor = work_for(Person,company(CName,CAddress))
&	Person = person(PName,PAge,PAddress)
<-
	.println("Person ",PName," is already working for ",CName);
.

+inform_result(WorkFor,false)[source(Engager)]
:
	WorkFor = work_for(Person,Company)
&	Person = person(PName,PAge,PAddress)
<-
	.println("Non lavora qui");
	.send(Engager,tell,request(engage(Person,Company)));
.

+inform(EngageRequest)
<-
	.println("Engagement successfully completed")
.
+failure(EngageRequest)
<-
	.println("Engagement failed")
.



{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation  
//{ include("$jacamoJar/templates/org-obedient.asl") }
