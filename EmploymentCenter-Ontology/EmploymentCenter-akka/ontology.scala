import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorRef}

/* ontology */
abstract class Term
abstract class Predicate extends Term
abstract class Concept extends Term
abstract class Action extends Term



/* concepts */
case class Address(street:String,number:Int,city:String) extends Concept
case class Company(name: String, address: Address) extends Concept

/* predicates */
case class Person(name: String, age: Int, address: Address) extends Predicate
case class WorkFor(person: Person,company: Company) extends Predicate
case class EngagementError(expl: Predicate) extends Predicate
case class NotNecessary() extends Predicate
case class PersonTooOld() extends Predicate

/* actions */
case class Engage(company: Company,person: Person) extends Action



