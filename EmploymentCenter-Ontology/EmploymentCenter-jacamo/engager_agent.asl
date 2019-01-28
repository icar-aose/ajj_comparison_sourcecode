// Agent engager_agent in project employment

/* Initial beliefs and rules */
represented_company("IES",address("Via Monte Senario",98,"Roma")).
employees([]).

/* Initial goals */


/* Plans */
+query_if(WorkFor)[source(Requestor)]
: 
	WorkFor = work_for(Person,company(Name,Address))
&	represented_company(Name,Address)
<-
	!isWorking(Person,Result);
	.send(Requestor,tell,inform_result(WorkFor,Result));
.

+request(EngageRequest)[source(Requestor)]
:
	EngageRequest= engage(Person,company(Name,Address))
&	represented_company(Name,Address)
<-
	!doIngage(Person,Result);
	if (Result==true) {
		.send(Requestor,tell,inform(EngageRequest));
	} else {
		.send(Requestor,tell,failure(EngageRequest));
	}
.

+!isWorking(Person,Result)
:
	employees(EmployeeList)
<-
	!search(Person,EmployeeList,Result);
.
+!search(Person,[],false).
+!search(Person,[Person|T],true).
+!search(Person,[H|T],Result) <- !search(T,Result).

+!doIngage(Person,true)
:
	employees(EmployeeList)
<-
	NewEmployeeList = [Person | EmployeeList ];
	-+employees(NewEmployeeList);
.


{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation  
//{ include("$jacamoJar/templates/org-obedient.asl") }
