/*
package file;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Link {
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
						error=root+"�ļ�������\n��¼��ɾ��\n";
					}
					else if(FileManage.error==2)
					{
						error=root+"�ļ����ظ�\n��¼��ɾ��\n";
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
