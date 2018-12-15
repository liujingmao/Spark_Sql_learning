package spark.sql.log

import java.sql.{Connection, PreparedStatement}

import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.collection.mutable.ListBuffer

object AccessDAO {

  /*create table access_log (
    url varchar(64) not null,
    cmsType varchar(16) not null,
    cmsId varchar(16) not null,
    traffic bigint(10) not null,
    ip varchar(32) not null,
    city varchar(16) not null,
    time varchar(64) not null,
    day varchar(64) not null
  )*/

  def main(args: Array[String]): Unit = {
    //insertDayVideoAccessTopN()

    //Get the SparkSession
    val spark = SparkSession.builder().appName("SparkStaticCleanJob")
      .master("local[2]").getOrCreate()


    // 读取文件，并将它转成RDD
    val accessRDD = spark.sparkContext.textFile("file:///Users/liujingmao/Desktop/BigData_Hadoop/access.log")

    // RDD转成DateFrame

    val accessDF = spark.createDataFrame(accessRDD.map(x=>AccessConvertUtil.parseLog(x)),AccessConvertUtil.struct)

    //insertIntoSQLTable(spark,accessDF)


  }

  var connection:Connection=null
  var pstmt:PreparedStatement=null

  def insertIntoSQLTable(list:ListBuffer[access_log]): Unit ={

    try{

      connection=MySQLUtils.getConnection()

      connection.setAutoCommit(false)

      val sql="insert into access_log(url,cmsType,cmsId,traffic,ip,city,time,day) values(?,?,?,?,?,?,?,?)"

      pstmt=connection.prepareStatement(sql)

      for(ele<-list){

        pstmt.setString(1,ele.url)

        pstmt.setString(2,ele.cmsType)

        pstmt.setString(3,ele.cmsId)

        pstmt.setInt(4,ele.traffic)

        pstmt.setString(5,ele.ip)

        pstmt.setString(6,ele.city)

        pstmt.setString(7,ele.time)

        pstmt.setString(8,ele.day)

        pstmt.addBatch()

      }

      pstmt.executeBatch()

      connection.commit()
    }

    catch {

      case e:Exception=>e.printStackTrace()

    } finally {

      MySQLUtils.release(connection,pstmt)

    }


  }




  def insertIntoTableWithMySQL(spark:SparkSession,accessDF:DataFrame)={

    //Get the SparkSession
    val spark = SparkSession.builder().appName("SparkStaticCleanJob")
      .master("local[2]").getOrCreate()


    // 读取文件，并将它转成RDD
    val accessRDD = spark.sparkContext.textFile("file:///Users/liujingmao/Desktop/BigData_Hadoop/access.log")

    // RDD转成DateFrame

    val accessDF = spark.createDataFrame(accessRDD.map(x=>AccessConvertUtil.parseLog(x)),AccessConvertUtil.struct)



    try {

      accessDF.foreachPartition(partionOfRecords => {

        val list = new ListBuffer[access_log]

        partionOfRecords.foreach(info => {
          val url=info.getAs[String]("url")
          val cmsType=info.getAs[String]("cmsType")
          val traffic=info.getAs[Int]("traffic")
          val ip = info.getAs[String]("ip")
          val city = info.getAs[String]("city")
          val cmsId = info.getAs[Long]("cmsId")
          val time = info.getAs[Long]("time")
          val day = info.getAs[String]("day")

          list.append(AccessDAO(url,cmsType,cmsId,traffic,ip,city,time,day))
        })

        AccessDAO.insertIntoSQLTable(list)

      })
    } catch {

      case e:Exception=>{

        e.printStackTrace()
      }

    }
  }


}
