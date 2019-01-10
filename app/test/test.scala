package test

import java.io.File

import org.apache.commons.io.FileUtils

import scala.collection.JavaConverters._
import scala.collection.mutable

object test {


  def main(args: Array[String]): Unit = {

   val out = "D:/P1.len.txt"

    val buffer = FileUtils.readLines(new File("D:\\桉树数据库相关数据\\桉树数据库相关数据12.4\\桉树数据库相关数据\\数据库提供材料\\P1_Augustus_Result/P1.Augustus.gff")).asScala

    val buffer1 = buffer.filter(_.take(3) == "chr").filter(_.split("\t")(2) == "gene")


    val buffer2 =  buffer1.map(x=>(x.split("\t").head,x.split("\t")(4).toInt)).groupBy(_._1).map{x=>
      val e = x._2.sortBy(_._2)
      e.last
    }.toArray.sortBy(_._1.split("chr").last.toInt).map{x=>
      x._1 + "\t1\t" + x._2
    }.toBuffer


    FileUtils.writeLines(new File(out),(mutable.Buffer("id\tstart\tend") ++ buffer2).asJava)


  }
}
