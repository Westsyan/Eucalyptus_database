package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.MySQLProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import com.github.tototoshi.slick.MySQLJodaSupport._
  import org.joda.time.DateTime
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Annotation.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Annotation
   *  @param id Database column id SqlType(INT), AutoInc
   *  @param geneid Database column geneid SqlType(VARCHAR), Length(255,true)
   *  @param chr Database column chr SqlType(TEXT)
   *  @param start Database column start SqlType(BIGINT)
   *  @param end Database column end SqlType(BIGINT)
   *  @param nrTophitName Database column NR_tophit_name SqlType(TEXT)
   *  @param nrTophitDescription Database column NR_tophit_description SqlType(TEXT)
   *  @param swissprotTophitName Database column Swissprot_tophit_name SqlType(TEXT)
   *  @param swissprotTophitDescription Database column Swissprot_tophit_description SqlType(TEXT)
   *  @param gos Database column GOs SqlType(TEXT)
   *  @param nog Database column NOG SqlType(TEXT)
   *  @param function Database column Function SqlType(TEXT)
   *  @param ko Database column KO SqlType(TEXT)
   *  @param keggGeneName Database column KEGG_GENE_NAME SqlType(TEXT)
   *  @param cds Database column cds SqlType(TEXT)
   *  @param pep Database column pep SqlType(TEXT) */
  final case class AnnotationRow(id: Int, geneid: String, chr: String, start: Long, end: Long, nrTophitName: String, nrTophitDescription: String, swissprotTophitName: String, swissprotTophitDescription: String, gos: String, nog: String, function: String, ko: String, keggGeneName: String, cds: String, pep: String)
  /** GetResult implicit for fetching AnnotationRow objects using plain SQL queries */
  implicit def GetResultAnnotationRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Long]): GR[AnnotationRow] = GR{
    prs => import prs._
    AnnotationRow.tupled((<<[Int], <<[String], <<[String], <<[Long], <<[Long], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String]))
  }
  /** Table description of table annotation. Objects of this class serve as prototypes for rows in queries. */
  class Annotation(_tableTag: Tag) extends profile.api.Table[AnnotationRow](_tableTag, Some("eucalyptus_database"), "annotation") {
    def * = (id, geneid, chr, start, end, nrTophitName, nrTophitDescription, swissprotTophitName, swissprotTophitDescription, gos, nog, function, ko, keggGeneName, cds, pep) <> (AnnotationRow.tupled, AnnotationRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(geneid), Rep.Some(chr), Rep.Some(start), Rep.Some(end), Rep.Some(nrTophitName), Rep.Some(nrTophitDescription), Rep.Some(swissprotTophitName), Rep.Some(swissprotTophitDescription), Rep.Some(gos), Rep.Some(nog), Rep.Some(function), Rep.Some(ko), Rep.Some(keggGeneName), Rep.Some(cds), Rep.Some(pep)).shaped.<>({r=>import r._; _1.map(_=> AnnotationRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get, _12.get, _13.get, _14.get, _15.get, _16.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column geneid SqlType(VARCHAR), Length(255,true) */
    val geneid: Rep[String] = column[String]("geneid", O.Length(255,varying=true))
    /** Database column chr SqlType(TEXT) */
    val chr: Rep[String] = column[String]("chr")
    /** Database column start SqlType(BIGINT) */
    val start: Rep[Long] = column[Long]("start")
    /** Database column end SqlType(BIGINT) */
    val end: Rep[Long] = column[Long]("end")
    /** Database column NR_tophit_name SqlType(TEXT) */
    val nrTophitName: Rep[String] = column[String]("NR_tophit_name")
    /** Database column NR_tophit_description SqlType(TEXT) */
    val nrTophitDescription: Rep[String] = column[String]("NR_tophit_description")
    /** Database column Swissprot_tophit_name SqlType(TEXT) */
    val swissprotTophitName: Rep[String] = column[String]("Swissprot_tophit_name")
    /** Database column Swissprot_tophit_description SqlType(TEXT) */
    val swissprotTophitDescription: Rep[String] = column[String]("Swissprot_tophit_description")
    /** Database column GOs SqlType(TEXT) */
    val gos: Rep[String] = column[String]("GOs")
    /** Database column NOG SqlType(TEXT) */
    val nog: Rep[String] = column[String]("NOG")
    /** Database column Function SqlType(TEXT) */
    val function: Rep[String] = column[String]("Function")
    /** Database column KO SqlType(TEXT) */
    val ko: Rep[String] = column[String]("KO")
    /** Database column KEGG_GENE_NAME SqlType(TEXT) */
    val keggGeneName: Rep[String] = column[String]("KEGG_GENE_NAME")
    /** Database column cds SqlType(TEXT) */
    val cds: Rep[String] = column[String]("cds")
    /** Database column pep SqlType(TEXT) */
    val pep: Rep[String] = column[String]("pep")

    /** Primary key of Annotation (database name annotation_PK) */
    val pk = primaryKey("annotation_PK", (id, geneid))
  }
  /** Collection-like TableQuery object for table Annotation */
  lazy val Annotation = new TableQuery(tag => new Annotation(tag))
}
