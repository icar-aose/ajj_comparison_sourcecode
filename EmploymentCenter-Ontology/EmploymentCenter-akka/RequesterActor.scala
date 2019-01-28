import akka.actor.{Actor, ActorRef}

class RequesterActor(engager : ActorRef)  extends Actor {
  case class SetCompany()
  case class HandleEngagement()

  var company : Company = null

  self ! SetCompany

  override def receive: Receive = {
    case SetCompany =>
      setCompany

    case HandleEngagement =>
      setPersonInfo

    case InformResult(WorkFor(person,company),true) =>
      println("Person " + person.name + " is already working for " + company.name)

    case InformResult(WorkFor(person,company),false) =>
      sender ! Request(Engage(company,person))

    case Inform(Engage(company,person)) =>
      println("Engagement successfully completed")

    case Failure(EngagementError(explanation)) =>
      println("Engagement failed")

    case _ =>
      println("Not Understood")

  }

  def setCompany : Unit = {
    import java.io.BufferedReader
    import java.io.InputStreamReader
    val buff = new BufferedReader(new InputStreamReader(System.in))

    println("ENTER details of the company where people will be engaged")
    print("  Company name --> ")
    val c_name = buff.readLine

    c_name match {
      case "IES" =>
        val c_street = "Via Monte Senario"
        val c_number = 98
        val c_city = "Roma"

        val c_address = Address(c_street,c_number,c_city)
        company = Company(c_name,c_address)

      case "ICAR" =>
        val c_street = "Via Ugo La Malfa"
        val c_number = 153
        val c_city = "Palermo"

        val c_address = Address(c_street,c_number,c_city)
        company = Company(c_name,c_address)


      case _ =>
        println("  Company address")
        print("    Street ------> ")
        val c_street = buff.readLine

        print("    Number ------> ")
        val c_number = buff.readLine.toInt

        print("    City ------> ")
        val c_city = buff.readLine

        val c_address = Address(c_street,c_number,c_city)
        company = Company(c_name,c_address)
    }


    self ! HandleEngagement
  }

  def setPersonInfo : Unit = {
    import java.io.BufferedReader
    import java.io.InputStreamReader
    /*val buff = new BufferedReader(new InputStreamReader(System.in))

    println("ENTER details of person to engage")
    print("  Person name --> ")
    val p_name = buff.readLine

    print("  Person age --> ")
    val p_age = buff.readLine.toInt

    println("  Person address")
    print("    Street ------> ")
    val p_street = buff.readLine

    print("    Number ------> ")
    val p_number = buff.readLine.toInt

    print("    City ------> ")
    val p_city = buff.readLine*/


    val p_address = Address("Via Vincenzo Di Marco",3,"Palermo")
    val person = Person("Luca Sabatucci",42,p_address)

    val wf = WorkFor(person,company)
    engager ! QueryIf(wf)

  }


}
