package spark.sql.log

import java.sql.{Connection, DriverManager}

object MySQLUtil {

  def main(args: Array[String]): Unit = {

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
