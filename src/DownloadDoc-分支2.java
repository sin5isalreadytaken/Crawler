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
* description: 从特定网站中筛选出相应的doc页面，下载到本地，并更新数据库
* note: 异常造成的线程中断的问题
* modificationDate: 2014-12-27
*/ 
public class DownloadDoc{
	RankUrl myUrl;//一级页面网址
	LinkFilter myFilter;
	int C_ID = 0; //为每个爬到的网页分配ID
	public static String oripath = "D:\\XueBaResources";
	public Lock lock;
	
	/**
	 * 构造函数
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
	 * 开始下载doc
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
		
		/* 3.执行 HTTP GET 请求 */
		System.out.println(myUrl);
		try {
			statusCode = httpClient.executeMethod(getMethod);
			System.out.println("statusCode = " + statusCode);
			// 判断访问的状态码
			if (statusCode != HttpStatus.SC_OK) {
				/* 当HTTP状态码不为成功返回网页时 */
				MyCrawler.count--;
				MyCrawler.failed++;
				System.err.println("Method failed: "
						+ getMethod.getStatusLine());
				filePath = null;
				MyCrawler.Cr.updatePanel(MyCrawler.visited, MyCrawler.succeed, MyCrawler.failed, MyCrawler.unpassed);
				return ;
				//MyCrawler.number--;
			}

			/* 4.处理 HTTP 响应内容 */
			contType=getMethod.getResponseHeader(
					"Content-Type").getValue();

			System.out.println("Content-Type:"+contType);
			
			Set<String> links=HtmlParserTool.extracLinks(myUrl.getUrl(),myFilter);
			//得到未访问的 URL 
			
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
				System.out.println("进入对列的url :"+link);
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
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			e.printStackTrace();
		} catch (IOException e) {
			// 发生网络异常
			e.printStackTrace();
		} catch (SQLException e) {
			// 发生数据库更新异常
			e.printStackTrace();
		}finally {
			// 释放连接
			getMethod.releaseConnection();
		}
		System.out.println("finish for "+myUrl.url);
	}
	
	/**
	 * 更新数据库
	 * @return 
	 * @param 网址IntoDataBase(String),文件路径FilePath(String)
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
		String state="(\'"+(C_ID)//网页id
					 +"\',\'"+IntoDataBase//域名
					 +"\',\'"+FilePath//文件存储路径
					 +"\',\'"+encode//
					 +"\',\'"+pagetype//网页类型
					 +"\',\'"+lastcrawlertime//最后被爬到的时间
					 +"\',\'"+freshtime//更新时间
					 +"\',\'"+keywords//标题
					 +"\',\'"+tag//标签
					 +"\',\'"+host//IP
					 +"\')";
		String sql="insert into fileinfo values"+state+";";
		System.out.printf("DownloadPdf:%s\n", sql);
		ConnectServer.update(sql);
	}
	/**
	 * 保存网页字节数组到本地
	 * @return 
	 * @param 保存网页字节数组data,本地文件的相对地址filePath
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
