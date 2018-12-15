package spark.sql.log

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._

import scala.collection.mutable.ListBuffer

object TopNStatJob {

  def main(args: Array[String]): Unit = {

  val spark = SparkSession.builder()
    .appName("TopNStatJob")
    .config("spark.sql.sources.partitionColumnTypeInference.enabled", "false")
    .master("local[2]").getOrCreate()

  val accessDF = spark.read.format("parquet").load("/Users/liujingmao/Desktop/BigData_Hadoop/cleanout")

   //accessDF.printSchema()

   accessDF.show(false)

    //videoAccessTopNStat(spark,accessDF)
    //videoAccessStatTopNWithSQL(spark,accessDF)

    //the most popular cmsid
    //videoAccessTopNStat(spark,accessDF)

    cityAccessTopNStat(spark,accessDF)

    //videoAccessTopNStat(spark,accessDF)


  spark.stop()

}


  /**
    * 按照地市统计TopN课程！！
    * @param spark
    * @param accessDF
    * @return
    */
  def cityAccessTopNStat(spark: SparkSession, accessDF:DataFrame):Unit ={

    import spark.implicits._

    val videoAccessTopNF = accessDF.filter($"day"==="20170511" && $"cmsType"==="video")
      .groupBy("day","city", "cmsId").agg(count("cmsId").as("times"))

    //videoAccessTopNF.printSchema()

    videoAccessTopNF.show(false)






  }


  /**
    * 最爱欢迎的课程
    * @param spark
    * @param accessDF
    */

  def videoAccessTopNStat(spark: SparkSession, accessDF:DataFrame): Unit ={

    import spark.implicits._

    val videoAccessTopNF = accessDF.filter($"day"==="20170511" && $"cmsType"==="video")
      .groupBy("day","cmsId").agg(count("cmsId").as("times")).orderBy($"times".desc)

      videoAccessTopNF.printSchema()

      videoAccessTopNF.show(false)

  }

  /**
    * 使用SQL的方式进行统计！！
    * @param spark
    * @param accessDF
    */

  def videoAccessStatTopNWithSQL (spark:SparkSession,accessDF:DataFrame): Unit = {

    accessDF.createOrReplaceTempView("access_log")

    val videoAccessTopNDF = spark.sql("select day,cmsId, count(1) as times from access_log where day = '20170511' " +
      "and  cmsType='video' group by day,cmsId order by times desc")

    //videoAccessTopNDF.show(false)

    //

    /*try {

      videoAccessTopNDF.foreachPartition(partionOfRecords => {

        val list = new ListBuffer[DayVideoAccessSta]

        partionOfRecords.foreach(info => {
          val day = info.getAs[String]("day")
          val cmsId = info.getAs[Long]("cmsId")
          val times = info.getAs[Long]("times")

          list.append(DayVideoAccessSta(day, cmsId, times))
        })

        StatDAO.insertDayVideoAccessTopN(list)

      })*/
    /*} catch {

      case e:Exception=>{

        e.printStackTrace()
      }

    }*/



  }




}
