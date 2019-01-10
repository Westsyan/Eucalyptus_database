package controllers

import java.io.File
import javax.inject.Inject

import dao._
import models.Tables.AnnotationRow
import org.apache.commons.io.FileUtils
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, Controller}
import utils.Utils

import scala.collection.JavaConverters._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration


case class PageData(limit: Int, offset: Int, order: String, search: Option[String], sort: Option[String])

class RnaController @Inject()(annotationdao: annotationDao) extends Controller {


  def insert = Action {
    val file = FileUtils.readLines(new File("D:\\桉树数据库相关数据\\GeneSet_Rename_20181217/P2.anno.xls")).asScala
    val cds = FileUtils.readFileToString(new File("D:\\桉树数据库相关数据\\GeneSet_Rename_20181217/P2.cds.flt"))
    val pep = FileUtils.readFileToString(new File("D:\\桉树数据库相关数据\\GeneSet_Rename_20181217/P2.pep.flt"))

    val cdsMap = cds.split(">").tail.map { x =>
      val column = x.split("\n")
      (column.head, column.tail.mkString)
    }.toMap

    val pepMap = pep.split(">").tail.map { x =>
      val column = x.split("\n")
      (column.head, column.tail.mkString)
    }.toMap

    val row = file.map { x =>
      val c = x.split("\t")
      val head = c.head
   //   AnnotationRow(0, head, c(1), c(2), c(3), c(4), c(5), c(6), c(7), c(8), c(9), c(10), c(11), c(12), cdsMap(head).toUpperCase, pepMap(head))
    }

  //  Await.result(annotationdao.insert(row), Duration.Inf)

    Ok(Json.toJson("1"))
  }

  def getJson(x:Seq[AnnotationRow]) : Seq[JsValue]  = {
      x.map { y =>
        val geneid = "<a>" + y.geneid + "</a>"
        Json.obj("geneid" -> geneid,"chr"->y.chr,"start"->y.start,"end"->y.end,"nrTophitName"->y.nrTophitName,
          "nrTophitDescription" -> y.nrTophitDescription,"swissprotTophitName"->y.swissprotTophitName,
          "swissprotTophitDescription"->y.swissprotTophitDescription,"gos"->y.gos,"nog"->y.nog,"function"->y.function,
          "ko"->y.ko,"keggGeneName"->y.keggGeneName)
      }
  }


  def browseBefore = Action { implicit request=>
    Ok(views.html.rna.browse())
  }


  val pageForm = Form(
    mapping(
      "limit" -> number,
      "offset" -> number,
      "order" -> text,
      "search" -> optional(text),
      "sort" -> optional(text)
    )(PageData.apply)(PageData.unapply)
  )

  def getAllGene = Action {implicit request=>
    val page = pageForm.bindFromRequest.get
    if (Utils.data.isEmpty) {
      Utils.data = Await.result(annotationdao.getAllGene, Duration.Inf)
    }
    val x = Utils.data
    val orderX = Utils.dealDataByPage(x, page)
    val total = orderX.size
    val tmpX = orderX.slice(page.offset, page.offset + page.limit)
    val array = getJson(tmpX)
    Ok(Json.obj("rows" -> array, "total" -> total))
  }


  def searchBefore = Action { implicit request =>
    Ok(views.html.rna.search())
  }

  def blastBefore = Action { implicit request =>
    Ok(views.html.rna.blast())
  }

  def getAllId = Action.async {
    annotationdao.getAllId.map { x =>
      Ok(Json.toJson(x))
    }
  }

  case class geneIdData(geneId: String)

  val geneIdForm = Form(
    mapping(
      "geneId" -> text
    )(geneIdData.apply)(geneIdData.unapply)
  )

  def searchById = Action.async {implicit request=>
    val data = geneIdForm.bindFromRequest.get
    val id = data.geneId.split(",").map(_.trim).distinct
    annotationdao.getBydId(id).map { x =>
      Ok(Json.toJson(getJson(x)))
    }
  }

  def getAllChr = Action.async{
    annotationdao.getAllChr.map{x=>
      Ok(Json.toJson(x.distinct))
    }
  }

  case class regionData(chr:String,start:String,end:String)

  val regionForm = Form(
    mapping(
      "chr" -> text,
      "start" -> text,
      "end" -> text
    )(regionData.apply)(regionData.unapply)
  )

  def searchByRegion = Action.async{implicit request=>
    val data = regionForm.bindFromRequest.get
    annotationdao.getByRegion(data.chr,data.start.toLong,data.end.toLong).map{x=>
      Ok(Json.toJson(getJson(x)))
    }




  }


}
