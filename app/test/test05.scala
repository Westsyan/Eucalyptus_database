package test

import java.io.File

import org.apache.commons.io.FileUtils

import scala.collection.JavaConverters._

object test05 {


  def main(args: Array[String]): Unit = {
    val b = FileUtils.readLines(new File("D:\\桉树数据库相关数据\\转录组数据2018.12.13\\P2cuffnorm2/P2.fpkm.xls")).asScala
    val buffer = b.map{x=>
      val column = x.split("\t")
       "0\t"+ column.head + "\t2\t" + column.tail.mkString("\t")
    }

    FileUtils.writeLines(new File("D:\\桉树数据库相关数据\\转录组数据2018.12.13\\P2cuffnorm2/P2.fpkm1.xls"),buffer.asJava)

  }
}
