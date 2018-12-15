package spark.sql.log

import org.apache.spark.sql.SparkSession

/**
## 数据清洗
**/

object SparkStaticCleanJob
{


  def main(args: Array[String]): Unit =
  {

    //Get the SparkSession
    val spark = SparkSession.builder().appName("SparkStaticCleanJob")
      .master("local[2]").getOrCreate()


    // 读取文件，并将它转成RDD
    val accessRDD = spark.sparkContext.textFile("file:///Users/liujingmao/Desktop/BigData_Hadoop/access.log")

    // RDD转成DateFrame

    val accessDF = spark.createDataFrame(accessRDD.map(x=>AccessConvertUtil.parseLog(x)),AccessConvertUtil.struct)

    //打印accessDF

    accessDF.printSchema()

    accessDF.createOrReplaceGlobalTempView("accesslogtable")

    accessDF.show(10)

    //选择一部分数据暗

    //accessDF.select("cmsId","traffic","ip","city").show(10)

    //在方法体内导入这个

    import spark.implicits._

    //accessDF.select($"cmsId",$"traffic"+1,$"ip",$"city").show()

    //将数据写入指定的路径

    //accessDF.write.format("parquet").partitionBy("day").save("/Users/liujingmao/Desktop/BigData_Hadoop/cleanout")

    spark.stop

  }




}
