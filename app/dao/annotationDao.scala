package dao

import javax.inject.Inject

import models.Tables._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

class annotationDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends
  HasDatabaseConfigProvider[JdbcProfile]{


  import profile.api._

  def insert(row: Seq[AnnotationRow]) : Future[Unit] = {
    db.run(Annotation ++= row).map(_=>())
  }

  def getAllGene : Future[Seq[AnnotationRow]] = {
    db.run(Annotation.result)
  }

  def getAllId : Future[Seq[String]] = {
    db.run(Annotation.map(_.geneid).result)
  }

  def getBydId(id:Seq[String]) : Future[Seq[AnnotationRow]] ={
    db.run(Annotation.filter(_.geneid.inSetBind(id)).result)
  }

  def getAllChr : Future[Seq[String]] = {
    db.run(Annotation.map(_.chr).result)
  }

  def getByRegion(chr:String,start:Long,end:Long) : Future[Seq[AnnotationRow]] ={
    db.run(Annotation.filter(_.chr === chr).filter(_.start >= start).filter(_.end <= end).result)
  }


}
