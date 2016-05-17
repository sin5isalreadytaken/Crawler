/*
package file;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Link {
	private static Connection con=null;//数据库类中的Connection类
	private static Statement st=null;
	private static ResultSet rs=null;
	private static String ip =  "10.2.26.60";
	private static String DatabaseName = "Crawler";
	private static String conURL= "jdbc:sqlserver://192.168.26.94;DatabaseName=Crawler";
			//			  219.224.191.25
			//				数据库所在ip					数据库名
		//	"jdbc:sqlserver://219.224.191.25;DatabaseName=Crawler";//指定连接的网址

	private static String username = "crawler";// "yuanhang1617";
	static String a,b,c,d;
	private static String password = "aimashi2015";//"yuanhang1617";
	public static void dbConn()
	{
		try{
			//数据库连接配置
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");	
			//加载一个用户要使用的数据库(类)的驱动程序。（也叫做注册驱动程序，把驱动程序的类加载到JVM中）
		}catch(ClassNotFoundException e1){
			e1.printStackTrace();
		}
		try{
			//								地址，	用户名，		密码
			con=DriverManager.getConnection(conURL,username,password);
			System.out.println(conURL);
			//java执行sql语句所需sql对象
			st=(Statement) con.createStatement();
			//获得当前连接的状态
			System.out.println("数据库连接 successful_!!!");
		}catch(SQLException e)
		{
			e.printStackTrace();
		}		
	}
	
	/**
	 * 断开数据库连接
	 * @return
	 * @param 
	 * @throws SQLException
	 */
	public static void dbClose()
	{
		try{
			if (st != null)st.close();
			if (con != null)con.close();
		}catch(SQLException e)
		{
			e.printStackTrace();
		}
		st=null;
		con=null;
	}
	public static ResultSet dataset(String sql)//对数据库做字符串SQL指定的操作，并返回结果
	{
		dbConn();
		try{
			rs= ((java.sql.Statement) st).executeQuery(sql);
		}catch(SQLException e)
		{
			e.printStackTrace();
		}
		return rs;
	}
	public static String fileManage()
	{
		try 
		{
			String result=new String();
			dbConn();
			ResultSet r1=dataset("select FilePath from fileinfo");
			while(r1.next())
			{
				if(!FileManage.addFile(r1.getString(1)))
				{
					String root=r1.getString(1);
					String error;
					dataset("delete from fileinfo where FilePath="+root);
					if(FileManage.error==1)
					{
						error=root+"文件不存在\n记录已删除\n";
					}
					else if(FileManage.error==2)
					{
						error=root+"文件已重复\n记录已删除\n";
					}
				}
			dbClose();
			return result;
			}
		}
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		return "false";
	}
}

*/
