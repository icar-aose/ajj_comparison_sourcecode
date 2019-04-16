/* communicative acts */
abstract class Performative

case class Request(act : Action) extends Performative
case class Refuse(act : Action) extends Performative
case class Agree(act : Action) extends Performative
case class Inform(act : Term) extends Performative
case class Failure(act : Term) extends Performative

case class QueryIf(pred : Predicate) extends Performative
case class InformResult(pred : Predicate, result : Boolean) extends Performative

case class NotUnderstood(act : Action) extends Performative


