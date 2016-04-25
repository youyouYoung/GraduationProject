/**
 * 
 * 该接口定义数据库链接类的接口.使子类具有:
 * 	1.返回Statement对象的方法.
 * 	2.关闭数据库链接的方法.
 * 
 * */
package collection.database;

import java.sql.SQLException;
//import java.util.List;
import java.sql.Statement;

interface DBConnection
{	
	/**
	 * 该方法用于返回一个Statement对象.
	 * @return 返回一个Statement对象.
	 * @throws SQLException 获取Statement对象失败时抛出此异常.
	 * */
	Statement getStatement() throws SQLException;
	
	//返回一个包含Statement对象的集合
	//List<Statement> getStatements()throws SQLException;
	
	/**
	 * 该方法用于关闭Connection链接
	 * */
	void close();
}
