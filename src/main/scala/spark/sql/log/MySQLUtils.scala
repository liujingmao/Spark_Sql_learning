package spark.sql.log


import java.sql.{Connection, PreparedStatement, DriverManager}

/**
  * MySQL操作工具类
  */
object MySQLUtils {

  /**
    * 获取数据库连接
    */
  def getConnection() = {
    DriverManager.getConnection("jdbc:mysql://localhost:3306/imooc_project?user=root&password=52291314")
  }

  /**
    * 释放数据库连接等资源
    * @param connection
    * @param pstmt
    */
  def release(connection: Connection, pstmt: PreparedStatement): Unit = {
    try {
      if (pstmt != null) {
        pstmt.close()
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally {
      if (connection != null) {
        connection.close()
      }
    }
  }

  def main(args: Array[String]) {
    println(getConnection())

    val url = "jdbc:mysql://localhost:3306/mysql"

    val username="root"

    val password="52291314"

    var connection:Connection = null

    try {

      classOf[com.mysql.jdbc.Driver]

      connection=DriverManager.getConnection(url,username,password)

      val statement=connection.createStatement()

      val resultSet = statement.executeQuery("select host,user from user")

      while (resultSet.next()){

        //todo
        val host=resultSet.getString("host")

        val user=resultSet.getString("user")

        println(s"$host,$user")

      }

    }

    catch {
      case e:Exception=>e.printStackTrace()
    }

    finally {

      if(connection!=null){

        connection.close()
      }


    }


  }


}




