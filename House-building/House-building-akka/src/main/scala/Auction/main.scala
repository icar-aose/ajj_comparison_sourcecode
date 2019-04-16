package Auction

import akka.actor.{ActorSystem, Props}
import akka.stream.ActorMaterializer


object main extends App {

  val system: ActorSystem = ActorSystem.create("test-system")
  implicit val system2 = ActorSystem("QuickStart")
  println("Inizializing companies")

  val CompanyA = system.actorOf(Props(classOf[CompanyA]),"CompanyA")
  val CompanyB = system.actorOf(Props(classOf[CompanyB]),"CompanyB")
  for (i<-1 to 5){
    val CompanyC = system.actorOf(Props(classOf[CompanyC]),s"CompanyC-${i}")
  }
  for (i<-1 to 13){
    val CompanyD = system.actorOf(Props(classOf[CompanyD]),s"CompanyD-${i}")
  }
  val CompanyE = system.actorOf(Props(classOf[CompanyE]),"CompanyE")


  println("Companies Initialized")


  val Auctioneer = system.actorOf(Props(classOf[Auctioneer]), "Auctioneer")

  implicit val materializer = ActorMaterializer()



  Auctioneer ! "start"




}
