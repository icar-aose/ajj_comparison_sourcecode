package Auction

import akka.actor.Actor
import main.system

class AuctionManager(var taskID: Int, var taskName: String, var maxValue: Int) extends Actor{

  var currentWinner=self
  var currentBid=maxValue

  val newAuction = new auctionStatus(taskID,maxValue,null, self,true)
  system.eventStream.publish(newAuction)


  def receive = astaAperta

  val astaAperta : Receive = {

    case m:bidMessage=>

      if(m.value<currentBid && currentWinner!= sender()){

        currentBid=m.value
        currentWinner=sender()

        println(s"A new better bid for the element ${taskName}: ${sender().path.name} has bid ${m.value}")

        val newStatus = new auctionStatus(taskID,currentBid,currentWinner,self,true)
        system.eventStream.publish(newStatus)
      }

    case m:showWinners=>

      context.become(astaChiusa)

      val endStatus = new auctionStatus(taskID,currentBid,currentWinner,self,false)
      sender() ! endStatus
      system.eventStream.publish(endStatus)

  }

  val astaChiusa : Receive = {

    case m:bidMessage=>
      sender() ! "I'm sorry, i'm dying!"


  }



}
