package spark.sql.log

import java.sql.PreparedStatement
import java.sql.Connection

/*day varchar(8) not null,
cms_id bigint(10) not null,
city varchar(20) not null,
times bigint(10) not null,
times_rank int not null,
primary key (day, cms_id, city)
);*/


import scala.collection.mutable.ListBuffer

object StatDAO {

  /**
    *
    * @param list
    */
  var connection:Connection=null
  var pstmt:PreparedStatement=null

  def insertDayVideoAccessTopN(list:ListBuffer[DayVideoAccessSta]): Unit ={

    try{

      connection=MySQLUtils.getConnection()

      connection.setAutoCommit(false)

      val sql="insert into day_video_access_topn_stat(day,cms_id,times) values(?,?,?)"

      pstmt=connection.prepareStatement(sql)

      for(ele<-list){

        pstmt.setString(1,ele.day)

        pstmt.setLong(2,ele.cmsId)

        pstmt.setLong(3,ele.times)

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



}
