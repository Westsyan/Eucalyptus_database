package test

import java.io.File

import org.apache.commons.io.FileUtils

import scala.collection.JavaConverters._
import scala.math.log10

object test02 {


  def main(args: Array[String]): Unit = {
    val chr = FileUtils.readLines(new File("D:\\桉树数据库相关数据\\转录组数据2018.12.13\\P2cuffnorm2/genes.attr_table")).asScala
    val expression = FileUtils.readLines(new File("D:\\桉树数据库相关数据\\转录组数据2018.12.13\\P2cuffnorm2/genes.fpkm_table")).asScala


    val chrMap = chr.tail.map{x=>
      val map = x.split("\t")
      (map.head,map(6))
    }.toMap

    val ex = expression.drop(1).map{x=>
      val column = x.split("\t")
      column(0) = chrMap(column.head)
      column
    }

    (1 to 8).foreach{x=>
      val expre = ex.filter(y=>y(x)!="0").map{y=>
        val head = y.head.split("[:|-]").mkString("\t")
        head + "\t" + log2(y(x).toDouble +1)
      }

      val name = expression.head.split("\t")

    val buffer =  expre.filter(_.take(8) != "scaffold").sortBy(z=> z.split("\t").head.drop(3).toInt)

      FileUtils.writeLines(new File("D:\\桉树数据库相关数据\\转录组数据2018.12.13\\P2cuffnorm2/P2." + name(x)+".xls"),buffer.asJava)

    }

  }


  def log2(x: Double) = log10(x) / log10(2.0)
}
