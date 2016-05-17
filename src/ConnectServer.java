import java.sql.*;
/**
* description: 连接数据库服务器，并可以对数据库内储存的
* note: 执行sql查询和插入时避免死锁发生
* modificationDate: 2014-11-27
*/ 
public class ConnectServer {
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
	/**
	 * 获取数据库地址，用户名，密码
	 */
	public static void setip(String iip){
		//ip = iip;
	}
	public static void setdbname(String dbn){
		//DatabaseName = dbn;
	}
	public static void setconURL(){
		//a="jbdc:sqlserver://";
		//b=a.concat(ip);
		//c=";DatabaseName=";
		///d = b.concat(c);
		//conURL = d.concat(DatabaseName);
		//conURL = "jbdc:sqlserver://"+"10.2.26.60"+";DatabaseName="+"Crawler";
		//System.out.println(conURL+"1111111111111111111111111111111111111111111111"+conURL.equals("jdbc:sqlserver://10.2.26.60;DatabaseName=Crawler"));
	}
	public static void setusername(String name){
		username = name;
	}
	public static void setpassword(String pword){
		password = pword;
	}
	/**
	 * 初始化数据库连接
	 * @return
	 * @param 
	 * @throws ClassNotFoundException,SQLException
	 */
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
	/**
	 * 执行sql查询
	 * @return ResultSet(查询结果)
	 * @param sql(String)
	 * @throws SQLException
	 */
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
	
	/**
	 * 得到全部网页个数
	 * @return count[webpage](String)
	 * @param 
	 * @throws SQLException
	 */
	public static String getSum_webpage(){
		try {
			String result=new String();
			dbConn();
			ResultSet r1=dataset("select COUNT(*) from fileinfo where pagetype='webpage'");
			//循环结束时获取最后一条记录的第一个字段，总数
			while(r1.next()){
				//读取resultSet里面每条记录的第一个字段
				result=r1.getString(1);
			}
			
			dbClose();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "false";
	}
	
	/**
	 *得到全部问答页个数
	 * @return count[quiz](String)
	 * @param 
	 * @throws SQLException
	 */
	public static String getSum_quiz(){
		try {
			String result=new String();
			dbConn();
			ResultSet r1=dataset("select COUNT(*) from fileinfo where pagetype='quiz'");
			while(r1.next()){
				//	columnIndex
				result=r1.getString(1);
			}
			
			dbClose();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "false";
	}
	
	/**
	 *得到全部doc个数
	 * @return count[doc](String)
	 * @param 
	 * @throws SQLException
	 */
	public static String getSum_doc(){
		try {
			String result=new String();
			dbConn();
			ResultSet r1=dataset("select COUNT(*) from fileinfo where pagetype='doc'");
			while(r1.next()){
				result=r1.getString(1);
			}
			
			dbClose();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "false";
	}
	
	/**
	 *得到全部ppt个数
	 * @return count[ppt](String)
	 * @param 
	 * @throws SQLException
	 */
	public static String getSum_ppt(){
		try {
			String result=new String();
			dbConn();
			ResultSet r1=dataset("select COUNT(*) from fileinfo where pagetype='ppt'");
			while(r1.next()){
				result=r1.getString(1);
			}
			
			dbClose();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "false";
	}
	
	/**
	 *得到全部pdf个数
	 * @return count[pdf](String)
	 * @param 
	 * @throws SQLException
	 */
	public static String getSum_pdf(){
		try {
			String result=new String();
			dbConn();
			ResultSet r1=dataset("select COUNT(*) from fileinfo where pagetype='pdf'");
			while(r1.next()){
				result=r1.getString(1);
			}
			
			dbClose();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "false";
	}
	
	/**
	 * 互斥更新数据库
	 * @return 更新结果(int)
	 * @param 更新sql语句(String)
	 * @throws SQLException
	 */
	public static synchronized int update(String sql) throws SQLException 
	{
		dbConn();
		int i=0;
		i=((java.sql.Statement) st).executeUpdate(sql);
		dbClose();
		return i;
	}

	/**
	 * 获取数据库最大ID号
	 * @return maxid(int)
	 * @param 
	 * @throws SQLException
	 */
	public static int idNumber()
	{
		dbConn();
		int id=0;
		ResultSet r1=dataset("select max(id) from fileinfo");
		try {
			r1.next();
			id=r1.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(id<=2000000)
			return 2000000;
		else
			return id;
		
	}
	/*public static void main(String[] args){
		try{
		dbConn();
		ResultSet r1=dataset("select count(*) from fileinfo");
		try {
			r1.next();
			System.out.println(r1.getInt(1));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		dbClose();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		try{
		dbConn();
		String sql="select * from fileinfo;";
		ResultSet r1=dataset(sql);
		try{
			System.out.println(r1.getMetaData().getColumnCount());	
			while(r1.next())
			{
				 
				System.out.print(r1.getString(1) + " ");
				 
				System.out.print(r1.getString(2) + " ");
				 
				System.out.print(r1.getString(3) + " ");
				 
				System.out.print(r1.getString(4) + " ");
				 
				System.out.println(r1.getString(5) + " ");
				 
			}

		}catch(SQLException e){
			e.printStackTrace();
		}
		dbClose();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		try{
		dbConn();
		ResultSet r1=dataset("select max(id) from fileinfo");
		try {
			r1.next();
			System.out.println(r1.getInt(1));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		dbClose();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}*/
	public static String typeNum(String s){
		try 
		{
			String result=new String();
			dbConn();
			ResultSet r1=dataset("select COUNT(*) from fileinfo where keywords LIKE'%"+s+"%'");
			//循环结束时获取最后一条记录的第一个字段，总数
			while(r1.next())
			{
				//读取resultSet里面每条记录的第一个字段
				result=r1.getString(1);
				}
			dbClose();
			return result;
			} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		return "false";
		}
	public static String sum()
	{
		try {
			String result=new String();
			dbConn();
			ResultSet r1=dataset("select COUNT(*) from fileinfo");
			//循环结束时获取最后一条记录的第一个字段，总数
			while(r1.next()){
				//读取resultSet里面每条记录的第一个字段
				result=r1.getString(1);
			}
			dbClose();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "false";
	}

}

