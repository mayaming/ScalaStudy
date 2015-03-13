/**
 * Created by yaming.ma on 2015/3/11.
 */

import java.sql.{Connection, DriverManager, ResultSet};

object JdbcExample {
  //val sql = "select * from datayesdbp.vmkt_equd where \ntrade_date = '2014-01-07' \nor trade_date = '2013-01-07' \nor trade_date = '2011-01-07' \nor trade_date = '2010-01-07' \nor trade_date = '2009-01-07' \nor trade_date = '2008-01-07' \nor trade_date = '2007-01-07' \nor trade_date = '2006-01-07' \nor trade_date = '2005-01-07' \nor trade_date = '2004-01-07' \nor trade_date = '2003-01-07' \nor trade_date = '2002-01-07' \nor trade_date = '2001-01-07' \nor trade_date = '2000-01-07' \nor trade_date = '1999-01-07' \nor trade_date = '1998-01-07' \nor trade_date = '2013-01-08' \nor trade_date = '2011-01-08' \nor trade_date = '2010-01-08' \nor trade_date = '2009-01-08' \nor trade_date = '2008-01-08' \nor trade_date = '2007-01-08' \nor trade_date = '2006-01-08' \nor trade_date = '2005-01-08' \nor trade_date = '2004-01-08' \nor trade_date = '2003-01-08' \nor trade_date = '2002-01-08' \nor trade_date = '2001-01-08' \nor trade_date = '2000-01-08' \nor trade_date = '1999-01-08' \nor trade_date = '1998-01-08'; ";
  val sql = "select * from datayesdbp.vmkt_equd";
  //val sql = "select * from datayesdbp.vmkt_equd limit 1000";
  def main(args: Array[String]): Unit = {
    // Change to Your Database Config
    val conn_str = "jdbc:mysql://db-datayesdb.wmcloud-stg.com:3307/datayesdbp?user=app_athena_ro&password=App_athena_ro_20150213"

    // Load the driver
    classOf[com.mysql.jdbc.Driver]

    // Setup the connection
    val conn = DriverManager.getConnection(conn_str)
    try {
      // Configure to be Read Only
      val statement = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)

      // Execute Query
      println(new java.util.Date())
      val rs = statement.executeQuery(sql)
      println(new java.util.Date())
      println(rs.getFetchSize())

      // Iterate Over ResultSet
      var cnt = 1
      while (rs.next) {
        cnt += 1
        if (cnt % 100 == 0)
          println(cnt)
        //println(rs.getString("sec_short_name"))
      }

      println(cnt)
    }
    finally {
      conn.close
    }
  }
}
