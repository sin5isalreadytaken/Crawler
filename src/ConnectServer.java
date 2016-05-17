import java.sql.*;
/**
* description: �������ݿ�������������Զ����ݿ��ڴ����
* note: ִ��sql��ѯ�Ͳ���ʱ������������
* modificationDate: 2014-11-27
*/ 
public class ConnectServer {
	private static Connection con=null;//���ݿ����е�Connection��
	private static Statement st=null;
	private static ResultSet rs=null;
	private static String ip =  "10.2.26.60";
	private static String DatabaseName = "Crawler";
	private static String conURL= "jdbc:sqlserver://192.168.26.94;DatabaseName=Crawler";
			//			  219.224.191.25
			//				���ݿ�����ip					���ݿ���
		//	"jdbc:sqlserver://219.224.191.25;DatabaseName=Crawler";//ָ�����ӵ���ַ

	private static String username = "crawler";// "yuanhang1617";
	static String a,b,c,d;
	private static String password = "aimashi2015";//"yuanhang1617";
	/**
	 * ��ȡ���ݿ��ַ���û���������
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
	 * ��ʼ�����ݿ�����
	 * @return
	 * @param 
	 * @throws ClassNotFoundException,SQLException
	 */
	public static void dbConn()
	{
		try{
			//���ݿ���������
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");	
			//����һ���û�Ҫʹ�õ����ݿ�(��)���������򡣣�Ҳ����ע���������򣬰��������������ص�JVM�У�
		}catch(ClassNotFoundException e1){
			e1.printStackTrace();
		}
		try{
			//								��ַ��	�û�����		����
			con=DriverManager.getConnection(conURL,username,password);
			System.out.println(conURL);
			//javaִ��sql�������sql����
			st=(Statement) con.createStatement();
			//��õ�ǰ���ӵ�״̬
			System.out.println("���ݿ����� successful_!!!");
		}catch(SQLException e)
		{
			e.printStackTrace();
		}		
	}
	
	/**
	 * �Ͽ����ݿ�����
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
	 * ִ��sql��ѯ
	 * @return ResultSet(��ѯ���)
	 * @param sql(String)
	 * @throws SQLException
	 */
	public static ResultSet dataset(String sql)//�����ݿ����ַ���SQLָ���Ĳ����������ؽ��
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
	 * �õ�ȫ����ҳ����
	 * @return count[webpage](String)
	 * @param 
	 * @throws SQLException
	 */
	public static String getSum_webpage(){
		try {
			String result=new String();
			dbConn();
			ResultSet r1=dataset("select COUNT(*) from fileinfo where pagetype='webpage'");
			//ѭ������ʱ��ȡ���һ����¼�ĵ�һ���ֶΣ�����
			while(r1.next()){
				//��ȡresultSet����ÿ����¼�ĵ�һ���ֶ�
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
	 *�õ�ȫ���ʴ�ҳ����
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
	 *�õ�ȫ��doc����
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
	 *�õ�ȫ��ppt����
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
	 *�õ�ȫ��pdf����
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
	 * ����������ݿ�
	 * @return ���½��(int)
	 * @param ����sql���(String)
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
	 * ��ȡ���ݿ����ID��
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
			//ѭ������ʱ��ȡ���һ����¼�ĵ�һ���ֶΣ�����
			while(r1.next())
			{
				//��ȡresultSet����ÿ����¼�ĵ�һ���ֶ�
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
			//ѭ������ʱ��ȡ���һ����¼�ĵ�һ���ֶΣ�����
			while(r1.next()){
				//��ȡresultSet����ÿ����¼�ĵ�һ���ֶ�
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

