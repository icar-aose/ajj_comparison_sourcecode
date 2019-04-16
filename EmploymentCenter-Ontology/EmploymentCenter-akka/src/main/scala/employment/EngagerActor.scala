import akka.actor.{Actor, ActorRef}

class EngagerActor(representedCompany : Company) extends Actor {
  var employees : List[Person] = List()

  override def receive: Receive = {
    case QueryIf(WorkFor(person,company)) =>
      val r : Boolean = isWorking(person)
      sender ! InformResult(WorkFor(person,company),r)

    case Request(Engage(company,person)) =>
      val r : Boolean = doIngage(company,person)
      if (r==true)
        sender ! Inform(Engage(company,person))
      else
        sender ! Failure(EngagementError(NotNecessary()))

    case _ =>
      println("Not Understood")
  }

  def isWorking(person: Person) : Boolean = employees.contains(person)

  def doIngage(company: Company, person: Person): Boolean = {
    var result = true
    if (company==representedCompany)
      employees = person :: employees
    else
      result = false

    result
  }


}
