package test

import java.io.File

import org.apache.commons.io.FileUtils
import utils.Utils

import scala.collection.JavaConverters._

object test04 {


  def main(args: Array[String]): Unit = {
    val b = FileUtils.readLines(new File("D:\\桉树数据库相关数据\\桉树基因功能注释Genome_Function\\Genome_Function\\P2/annotation.table.xls")).asScala

   FileUtils.writeLines(new File(Utils.path + "/conf/P2.gene.xls"),b.map(_.split("\t").head).asJava)

   val kegg = b.map{x=>
      val column = x.split("\t")
      (column.head,column(11))
    }.tail.filter(_._2 != "None").filter(_._2 != "_").map(x=>x._1 + "\t" + x._2)

  //  FileUtils.writeLines(new File(Utils.path + "/conf/P2.kegg.xls"),kegg.asJava)

  }
}
