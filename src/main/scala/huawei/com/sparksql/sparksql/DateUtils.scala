package huawei.com.sparksql.sparksql


import java.util.Locale
import java.util.Date

import org.apache.commons.lang3.time.FastDateFormat
import org.junit.Test

object DateUtils {

  //DateFormate 线程不安全，所以这里用FastDateFormat

  val YYYYMMDDHHMM_TIME_FORMAT = FastDateFormat.getInstance("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH)

  val TARGET_TIME_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:ss:ss")


  def parse(time: String) = {

    TARGET_TIME_FORMAT.format(new Date(getTime(time)))

  }

  def getTime(time: String) = {
    try {

      YYYYMMDDHHMM_TIME_FORMAT.parse(time.substring(time.indexOf("[") + 1,
        time.lastIndexOf("]"))).getTime
    }
    catch {

      case e: Exception => {

        0l
      }
    }
  }
@Test
  def main(args: Array[String]): Unit = {

    var aim = parse("[10/Nov/2016:00:01:07 +0800]")

    println(aim)

  }


}
