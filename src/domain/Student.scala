package domain

import scala.actors.Actor;
import scala.actors.Actor._;

//messages:


case object SayName
case object SayId

class Student(
  var id: String,
  var name: String,
  var studyRecords: List[StudyRecord]) extends BaseDomain {
  def this() = this(null, null, Nil)

  override def act() {
    loop {
      react {
        //////////
        case TakeCourse(offering, target) =>
          //validate (has not passed the course itself, has passed pre reqs ...)
          var takeCourse = new StudentTakeCourseActor(this)
          takeCourse.start();
          takeCourse ! TakeCourse(offering, target)
    
        case HasPassed(course, target) =>
          //APPROACH 3
          /* */debug(this + " received message: " + HasPassed(course, target))
          val coursePassActor  = new StudentCoursePassActor(this)
          coursePassActor.start
          coursePassActor ! HasPassed(course, target)
          
          
        case HasTaken(course, target) =>
          //APPROACH 3
          /* */debug(this + " received message: " + HasTaken(course, target))
          val courseTakenCheckActor  = new StudentCourseTakenCheckActor(this)
          courseTakenCheckActor.start
          courseTakenCheckActor ! HasTaken(course, target)
          
        case GPARequest(_, term: Term, target: Actor,_) =>
          /* */debug(this + " received message: " + GPARequest(null, term: Term, target: Actor,null))
          val gpaCoordinator:StudentComputeTermGPAActor = new StudentComputeTermGPAActor()
          gpaCoordinator.start
          gpaCoordinator ! GPARequest(this, term, target, null)
      }
    }
  }

  override def toString = "[Student:" + id + "]"

}