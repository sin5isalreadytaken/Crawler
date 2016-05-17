import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Set;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.HtmlPage;

/**
* description: ���ض���վ��ɸѡ����Ӧ��docҳ�棬���ص����أ����������ݿ�
* note: �쳣��ɵ��߳��жϵ�����
* modificationDate: 2014-12-27
*/ 
public class DownloadDoc{
	RankUrl myUrl;//һ��ҳ����ַ
	LinkFilter myFilter;
	int C_ID = 0; //Ϊÿ����������ҳ����ID
	public static String oripath = "D:\\XueBaResources";
	public Lock lock;
	
	/**
	 * ���캯��
	 * @return 
	 * @param url
	 * @throws 
	 */
	public DownloadDoc(RankUrl url,LinkFilter filter,Lock lock){
		myUrl=url;
		myFilter=filter;
		MyCrawler.count++;
		this.lock=lock;
		//5555
	}
	
	/**
	 * ��ʼ����doc
	 * @return 
	 * @param 
	 * @throws HttpException,IOException,SQLException
	 */
	public void run()
	{
		String filePath = null;
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(
				50000);
		String s = myUrl.getUrl();
		GetMethod getMethod = new GetMethod(s);
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 50000);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		int statusCode=0;
		String contType="";
		
		/* 3.ִ�� HTTP GET ���� */
		System.out.println(myUrl);
		try {
			statusCode = httpClient.executeMethod(getMethod);
			System.out.println("statusCode = " + statusCode);
			// �жϷ��ʵ�״̬��
			if (statusCode != HttpStatus.SC_OK) {
				/* ��HTTP״̬�벻Ϊ�ɹ�������ҳʱ */
				MyCrawler.count--;
				MyCrawler.failed++;
				System.err.println("Method failed: "
						+ getMethod.getStatusLine());
				filePath = null;
				MyCrawler.Cr.updatePanel(MyCrawler.visited, MyCrawler.succeed, MyCrawler.failed, MyCrawler.unpassed);
				return ;
				//MyCrawler.number--;
			}

			/* 4.���� HTTP ��Ӧ���� */
			contType=getMethod.getResponseHeader(
					"Content-Type").getValue();

			System.out.println("Content-Type:"+contType);
			
			Set<String> links=HtmlParserTool.extracLinks(myUrl.getUrl(),myFilter);
			//�õ�δ���ʵ� URL 
			
			URL SonUrl;
			InputStream SonInput;
			String SonLine;
			FileWriter fw;
			BufferedWriter bw;
			FileWriter fw2 = new FileWriter(new File(C_ID+".txt"), true);;
			BufferedWriter bw2 = new BufferedWriter(fw2);
			int num = 0;
			
			/*for(String link:links)	
			{
				if(!link.endsWith(".doc") && !link.endsWith(".docx"))
					continue;
				System.out.println("12");
			}*/
			for(String link:links)	
			{
				if(!link.endsWith(".doc") && !link.endsWith(".docx"))
					continue;
				//LinkQueue.addUnvisitedUrl(new RankUrl(link), myUrl);
				System.out.println("������е�url :"+link);
				RankUrl rankUrl = new RankUrl(link);
				
				SonUrl = new URL(rankUrl.getUrl());
		        SonInput = SonUrl.openStream();
				BufferedReader SonReader = new BufferedReader(new InputStreamReader(SonInput));
				SonLine = SonReader.readLine();
				
				C_ID = MyCrawler.id;
				MyCrawler.id ++;
				if(link.endsWith(".doc"))
					filePath = oripath + "\\"+ C_ID+".doc";
				else
					filePath = oripath + "\\"+ C_ID+".docx";
				
				InputStream is = SonUrl.openStream();
				ByteArrayOutputStream bAOut = new ByteArrayOutputStream();  
	            int c;  
	            while ((c = is.read()) != -1) {  
	                bAOut.write(c);  
	            }  
	            byte[] responseBody = bAOut.toByteArray();
	            saveToLocal(responseBody, filePath);
	            
				MyCrawler.visited++;
				synchronized(lock)
				{
					if(MyCrawler.succeed<MyCrawler.max)
					{
						MyCrawler.succeed++;
						LinkQueue.addVisitedUrl(rankUrl);
						MyCrawler.Cr.updatePanel(MyCrawler.visited, MyCrawler.succeed, MyCrawler.failed, MyCrawler.unpassed);
						DataBase(rankUrl.url,filePath);
					}
					else
						break;
				}
			}
		} catch (HttpException e) {
			// �����������쳣��������Э�鲻�Ի��߷��ص�����������
			e.printStackTrace();
		} catch (IOException e) {
			// ���������쳣
			e.printStackTrace();
		} catch (SQLException e) {
			// �������ݿ�����쳣
			e.printStackTrace();
		}finally {
			// �ͷ�����
			getMethod.releaseConnection();
		}
		System.out.println("finish for "+myUrl.url);
	}
	
	/**
	 * �������ݿ�
	 * @return 
	 * @param ��ַIntoDataBase(String),�ļ�·��FilePath(String)
	 * @throws ParserException
	 */
	public void DataBase(String IntoDataBase,String FilePath) throws SQLException
	{
		String encode = "Default";
		String keywords=null;
		String pagetype=null;
		String lastcrawlertime="1980-01-01";
		String freshtime="1980-01-01";
		String tag=null;
		String host=null;
		pagetype="doc";
		host=IntoDataBase.split("/")[2];
		host="http://"+host;
		try	{
			Parser parser2 = new Parser(IntoDataBase);
			parser2.setEncoding("UTF-8");
			URLConnection urs = parser2.getConnection(); 
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			lastcrawlertime=format.format(new java.util.Date());
			freshtime=lastcrawlertime;
			freshtime=format.format(new java.util.Date(urs.getLastModified()));
			if(freshtime.indexOf("1970")>=0)	freshtime=lastcrawlertime;
			HtmlPage page = new HtmlPage(parser2);
			try {
				parser2.visitAllNodesWith(page);
			} catch (ParserException e1) {
				e1 = null;
			}
			keywords=page.getTitle();
		} catch(ParserException e) {
			e.printStackTrace();
		} 
		String state="(\'"+(C_ID)//��ҳid
					 +"\',\'"+IntoDataBase//����
					 +"\',\'"+FilePath//�ļ��洢·��
					 +"\',\'"+encode//
					 +"\',\'"+pagetype//��ҳ����
					 +"\',\'"+lastcrawlertime//���������ʱ��
					 +"\',\'"+freshtime//����ʱ��
					 +"\',\'"+keywords//����
					 +"\',\'"+tag//��ǩ
					 +"\',\'"+host//IP
					 +"\')";
		String sql="insert into fileinfo values"+state+";";
		System.out.printf("DownloadPdf:%s\n", sql);
		ConnectServer.update(sql);
	}
	/**
	 * ������ҳ�ֽ����鵽����
	 * @return 
	 * @param ������ҳ�ֽ�����data,�����ļ�����Ե�ַfilePath
	 * @throws IOException
	 */
	private void saveToLocal(byte[] data, String filePath) {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					new File(filePath)));
			for (int i = 0; i < data.length; i++)
				out.write(data[i]);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
