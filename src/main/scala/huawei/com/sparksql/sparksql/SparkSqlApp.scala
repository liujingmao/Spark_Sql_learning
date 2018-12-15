package huawei.com.sparksql.sparksql

import org.apache.spark.sql.SparkSession
import spark.sql.log.IPUtils

object SparkSqlApp {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("SparkSqlApp")

      .master("local[2]").getOrCreate()

    val access_file = spark.sparkContext.textFile("file:///Users/liujingmao/app/tmp/access.log")

    access_file.foreach(println)

    access_file.map(line => {

      val splits = line.split(" ")
      val ip = splits(0)
      val city =IPUtils.getCity(ip)
      val time = splits(3) + " " + splits(4)
      val url = splits(11).replaceAll("\""," ")
      val traffic = splits(9)

      (ip,DateUtils.parse(time),url,traffic)

     DateUtils.parse(time)+"\t"+url+"\t"+ip+"\t"+"\t"+traffic

    }).saveAsTextFile("file:///Users/liujingmao/liu_spark-1")

    //saveAsTextFile("file:///Users/liujingmao/app/output")


    //access_file.take(10).foreach(println)


   //new_file.take(100).foreach(println)

    spark.stop()


  }

}
