package spark.steaming.learning

import org.apache.spark.SparkConf
import org.apache.spark.streaming.flume.FlumeUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}


/**
  * Created by liujingmao
  */

object FlumePushWordCount {

  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf()//setMaster("local[2]").setAppName("FlumeWordCount")

    val ssc = new StreamingContext(sparkConf,Seconds(5))


    //SparkStreaming整合Flume
    val flumeStream = FlumeUtils.createStream(ssc,"0.0.0.0",41414)

    //flumeStream.map(x=>new Stream(x.event.getBody.array().trim)

      flumeStream.map(x=>new String(x.event.getBody.array()).trim).flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).print()

    ssc.start()

    ssc.awaitTermination()

  }

}
