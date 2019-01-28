import akka.actor.{ActorSystem, Props}

object Main extends App {
  val system : ActorSystem = ActorSystem("employment")
  val props1 = Props.create(classOf[EngagerActor],Company("IES",Address("Via Monte Senario",98,"Roma")))
  val ies = system.actorOf(props1, "company1")
  val props2 = Props.create(classOf[RequesterActor],ies)
  val eng = system.actorOf(props2, "engager")

}
