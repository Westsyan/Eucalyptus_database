package controllers

import java.io.File
import java.nio.file.Files

import org.apache.commons.io.FileUtils
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import utils.{ExecCommand, Utils}

import scala.collection.JavaConverters._

class ToolsController extends Controller {


  def goBefore = Action { implicit request =>
    Ok(views.html.tools.go())
  }

  def keggBefore = Action { implicit request =>
    Ok(views.html.tools.kegg())
  }

  case class keggData(id: String, species: String, m: String, n: String, c: String, pval: String)

  val keggForm = Form(
    mapping(
      "id" -> text,
      "species" -> text,
      "m" -> text,
      "n" -> text,
      "c" -> text,
      "pval" -> text
    )(keggData.apply)(keggData.unapply)
  )

  def keggResult = Action { implicit request =>
    val data = keggForm.bindFromRequest.get
    val Id = data.id
    val population = Utils.path + "/conf/P1.gene.xls"
    val association = data.species match {
      case "p1" => Utils.path + "/conf/P1.kegg.xls"
      case "p2" => Utils.path + "/conf/P2.kegg.xls"
    }
    val geneId = Id.split(",").map(_.trim).distinct.toBuffer
    val tmpDir = Files.createTempDirectory("tmpDir").toString
    val studies = new File(tmpDir, "tmp.txt")
    FileUtils.writeLines(studies, geneId.asJava)
    val study = studies.getAbsolutePath
    val output = new File(tmpDir, "KEGG_enrichment.txt")
    val o = output.getAbsolutePath
    // println(study,population,association,m,n,o,c,pval)
    //QVALUE在unix转译文本后可以使用
    val execCommand = new ExecCommand
    val command = "perl " + Utils.path + "/tools/identify.pl -study=" + study + " -population=" + population +
      " -association=" + association + " -m=" + data.m + " -n=" + data.n + " -o=" + o + " -c=" +
      data.c + " -maxp=" + data.pval

    execCommand.exect(command, tmpDir)
    if (execCommand.isSuccess) {
      val keggInfo = FileUtils.readLines(output).asScala
      val buffer = keggInfo.drop(1)
      val json = buffer.map { x =>
        val all = x.split("\t")
        val hyper = "<a target='_blank' href='" + all(8) + "'><input type='button' class='link' value='linked'></a><a style='display: none'>" + all(8) + "</a>"
        Json.obj("term" -> all.head, "database" -> all(1), "id" -> all(2), "input_num" -> all(3), "back_num" -> all(4),
          "p-value" -> all(5), "correct_pval" -> all(6), "input" -> all(7), "hyperlink" -> hyper)
      }
      Utils.deleteDirectory(tmpDir)
      Ok(Json.toJson(json))
    } else {
      Utils.deleteDirectory(tmpDir)
      Ok(Json.obj("valid" -> "false", "message" -> execCommand.getErrStr))
    }
  }


  case class goData(id: String, species: String, alpha: String, pval: String)

  val goForm = Form(
    mapping(
      "id" -> text,
      "species" -> text,
      "alpha" -> text,
      "pval" -> text
    )(goData.apply)(goData.unapply)
  )

  def goResult = Action { implicit request =>
    val data = goForm.bindFromRequest.get
    val Id = data.id
    val geneId = Id.split(",").map(_.trim).distinct.toBuffer
    val tmpDir = Files.createTempDirectory("tmpDir").toString
    val studies = new File(tmpDir, "tmp.txt")
    FileUtils.writeLines(studies, geneId.asJava)
    val study = studies.getAbsolutePath
    val population = Utils.path + "/conf/P1.gene.xls"
    val association = data.species match {
      case "p1" => Utils.path + "/conf/P1.go.xls"
      case "p2" => Utils.path + "/conf/P2.go.xls"
    }
    val o = new File(tmpDir, "GO_enrichment.txt")
    val execCommand = new ExecCommand
    val command = "python " + Utils.path + "/tools/goatools-0.5.7/scripts/find_enrichment.py --alpha=" + data.alpha +
      " --pval=" + data.pval + " --output " + o.getAbsolutePath + " " + study + " " + population + " " + association
    execCommand.exect(command, tmpDir)
    if (execCommand.isSuccess) {
      val goInfo = FileUtils.readLines(o).asScala
      val buffer = goInfo.drop(1)
      val json = buffer.map { x =>
        val all = x.split("\t")
        val goLink = "<a target='_blank' href='http://amigo.geneontology.org/amigo/term/" + all(0) + "'>" + all(0) + "</a>"
        Json.obj("id" -> goLink, "enrichment" -> all(1), "description" -> all(2), "ratio_in_study" -> all(3),
          "ratio_in_pop" -> all(4), "p_uncorrected" -> all(5), "p_bonferroni" -> all(6), "p_holm" -> all(7),
          "p_sidak" -> all(8), "p_fdr" -> all(9), "namespace" -> all(10), "genes_in_study" -> all(11))
      }
      Utils.deleteDirectory(tmpDir)
      Ok(Json.toJson(json))
    } else {
      Utils.deleteDirectory(tmpDir)
      Ok(Json.obj("valid" -> "false", "message" -> execCommand.getErrStr))
    }
  }

}
