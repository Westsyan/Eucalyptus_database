package test

import java.io.File

import org.apache.commons.io.FileUtils

import scala.collection.JavaConverters._
import scala.collection.mutable

object test06 {

  def main(args: Array[String]): Unit = {

    getSection("D:\\桉树数据库相关数据\\cir","P1")

  }


  def gettype(input:String,sample:String) = {


    val head =
      s"""
         |library(circlize)
         |
        |pdf("$sample.type.circle.pdf",w=12,h=12)
         |
        |da=read.table("$sample.len.txt",sep="\t",header=TRUE)
         |id<-merge(da[,1],1:2)
         |chr<-data.frame(y=id[,1],x=c(da[,2],da[,3]))
         |factor<-factor(chr$$y,levels=c(as.character(da[,1])))
         |circos.genomicInitialize(da,plotType = c("axis"),factors = factor,major.by=10000000,axis.labels.cex=0.45)
         |color_regions <- structure(c("blue","green","red","grey","orange","yellow"),names = c("Insertion","Deletion","Tandem_expansion","Tandem_contraction","Repeat_expansion","Repeat_contraction"))
         |legend("bottomright",pch=20,col=color_regions,legend=names(color_regions),cex=1)
         |
      """.stripMargin

    val chr  = FileUtils.readLines(new File(input + "/" + sample + ".len.txt")).asScala

    val title = chr.drop(1).map{x=>
      val column = x.split("\t")
      val color = getColor(column.head)
      val name = column.head
      s"""circos.rect(0,-0.3,${column.last},-1,sector.index="$name",col="$color",border=FALSE)
         |circos.text(${column.last.toInt/2} ,2 , "$name", sector.index = "$name", niceFacing = TRUE,cex=0.8)
         """.stripMargin
    }.mkString("\n")



    val file = FileUtils.readLines(new File(input+"/" + sample + ".datas.txt")).asScala

    val data = file.map(_.split("\t")).filter{x=>
      (x(3).toInt-x(2).toInt) >= 10000 || (x(6).toInt-x(5).toInt) >= 10000
    }.groupBy(_.head)

  val body = data.map{x=>
      val color = getColor(x._1)
      x._2.map(_.drop(1)).map{y=>
        s"""circos.link("${y.head}",c(${y(1)},${y(2)}),"${y(3)}",c(${y(4)},${y(5)}),rou = 0.93,col="$color",border=FALSE)"""
      }.mkString("\n")
    }.mkString("\n")

    FileUtils.writeStringToFile(new File(input + "/" + sample + ".type.cmd.r"),head+title+"\n"+body + "\ndev.off()")


  }

  def getLength(input:String,output:String) = {
    val file = FileUtils.readLines(new File(input)).asScala
    val filter = file.map(_.split("\t")).filter(x=> x(1).toInt >= 10000 || x(3).toInt >= 10000)
    val x1 = filter.map{x=>
     x.head +"\t" + x(2) + "\t" + x(1)
    }
    val x2 = filter.map{x=>
      x(2) +"\t" + x.head + "\t" + x(3)
    }
    FileUtils.writeLines(new File(output),(x1 ++ x2).asJava)

  }

  def getFilter(file:mutable.Buffer[String]) = {
    file.map(_.split("\t")).filter{x=>
      (x(2).toInt-x(1).toInt) >= 10000 || (x(5).toInt-x(4).toInt) >= 10000
    }.map{x=>
      if((x(2).toInt-x(1).toInt) >= (x(5).toInt-x(4).toInt)){
        x
      }else{
        Array(x(3),x(4),x(5),x(0),x(1),x(2))
      }
    }
  }

  def getSection(input:String,sample:String) = {

    val head =
      s"""
        |library(circlize)
        |
        |pdf("$sample.circle.pdf",w=12,h=12)
        |
        |da=read.table("$sample.len.txt",sep="\t",header=TRUE)
        |id<-merge(da[,1],1:2)
        |chr<-data.frame(y=id[,1],x=c(da[,2],da[,3]))
        |factor<-factor(chr$$y,levels=c(as.character(da[,1])))
        |circos.genomicInitialize(da,plotType = c("axis"),factors = factor,major.by=10000000,axis.labels.cex=0.45)
        |
      """.stripMargin

      val chr  = FileUtils.readLines(new File(input + "/" + sample + ".len.txt")).asScala

     val title = chr.drop(1).map{x=>
        val column = x.split("\t")
        val color = getColor(column.head)
        val name = column.head
        s"""circos.rect(0,-0.3,${column.last},-1,sector.index="$name",col="$color",border=FALSE)
           |circos.text(${column.last.toInt/2} ,2 , "$name", sector.index = "$name", niceFacing = TRUE,cex=0.8)
         """.stripMargin
      }.mkString("\n")



    val value = FileUtils.readLines(new File(input + "/" + sample + ".data.txt")).asScala

    val body = getFilter(value).map{x=>
      val color = getColor(x.head)
      s"""circos.link("${x.head}",c(${x(1)},${x(2)}),"${x(3)}",c(${x(4)},${x(5)}),rou = 0.93,col="$color",border=FALSE)"""
    }.mkString("\n")


  FileUtils.writeStringToFile(new File(input + "/" + sample + ".cmd.r"),head+title+"\n"+body + "\ndev.off()")

  }

  def getColor(chr:String) = {
    chr match{
      case "EUCur.Chr01" | "EUCte.Chr01" => "#808786"
      case "EUCur.Chr02" | "EUCte.Chr02" => "#976691"
      case "EUCur.Chr03" | "EUCte.Chr03" => "#22050E"
      case "EUCur.Chr04" | "EUCte.Chr04" => "#8A848A"
      case "EUCur.Chr05" | "EUCte.Chr05" => "#53F97E"
      case "EUCur.Chr06" | "EUCte.Chr06" => "#171916"
      case "EUCur.Chr07" | "EUCte.Chr07" => "#080708"
      case "EUCur.Chr08" | "EUCte.Chr08" => "#1C1606"
      case "EUCur.Chr09" | "EUCte.Chr09" => "#B48938"
      case "EUCur.Chr10" | "EUCte.Chr10" => "#6D6A52"
      case "EUCur.Chr11" | "EUCte.Chr11" => "#0B1300"
      case "EUCgr.Chr01" => "#826F41"
      case "EUCgr.Chr02" => "#A42486"
      case "EUCgr.Chr03" => "#8CC847"
      case "EUCgr.Chr04" => "#2E3637"
      case "EUCgr.Chr05" => "#201D13"
      case "EUCgr.Chr06" => "#9F8E92"
      case "EUCgr.Chr07" => "#D28FB4"
      case "EUCgr.Chr08" => "#A2CABD"
      case "EUCgr.Chr09" => "#275313"
      case "EUCgr.Chr10" => "#4B484A"
      case "EUCgr.Chr11" => "#694D53"
      case "Insertion" => "blue"
      case "Deletion" =>  "green"
      case "Tandem_expansion" =>  "red"
      case "Tandem_contraction" =>  "grey"
      case "Repeat_expansion" =>  "orange"
      case "Repeat_contraction" =>  "yellow"
    }



  }




  //转换gene ID
  def getInfo(input:String,output:String,prefix:String) = {
    val buffer = FileUtils.readLines(new File(input)).asScala
    val flt = buffer.filter(_.split("\t").head.takeRight(2) == "t1").map{x=>
      val column = x.split("\t")
      val id = column.head.tail.dropRight(3)
      val geneid = (0 until  (6 - id.length)).map(_=>"0").mkString + id
      (id.toInt,prefix + geneid + "\t" + column.tail.mkString("\t"))
    }.sortBy(_._1).map(_._2)

    FileUtils.writeLines(new File(output),flt.asJava)

  }

  //筛选出有效的序列
  def getSeq(input:String,output:String) = {
    val buffer = FileUtils.readFileToString(new File(input))

    val map = buffer.split(">").tail.map { x =>
      val column = x.split("\n")
      (column.head, column.tail.mkString("\n"))
    }.toMap
    val b = map.map{x=>
      val seq =  if(x._1.last.toString == "1"){
        val seq2 = map.getOrElse(x._1.dropRight(1) + "2", "")
       if(x._2.length > seq2.length) x._2 else seq2
      }else{
        val seq1 = map(x._1.dropRight(1) + "1")
        if(seq1.length > x._2.length ) seq1 else x._2
      }
      ">" + x._1.dropRight(2) + "\n" + seq
    }.toBuffer.distinct

    FileUtils.writeLines(new File(output),b.sortBy{x=>
      x.take(12).takeRight(5).toInt
    }.asJava)
  }

  //筛选出gene的信息
  def getGeneGff = {
    val buffer = FileUtils.readLines(new File("D:\\桉树数据库相关数据\\GeneSet_Rename_20181217/P2.gff")).asScala
    FileUtils.writeLines(new File("D:\\桉树数据库相关数据\\GeneSet_Rename_20181217/P2.gene.gff"), buffer.filter(_.contains("\tgene\t")).asJava)
  }

}
