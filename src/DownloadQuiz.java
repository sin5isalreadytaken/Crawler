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
* description: 从特定网站中筛选出相应的问答页面，下载到本地，并更新数据库
* note: 异常造成的线程中断的问题
* modificationDate: 2014-12-16
*/ 
public class DownloadQuiz extends Thread{
	RankUrl myUrl;//一级页面网址
	int PageKind;//网页类别
	int C_ID = 0;//为每个爬到的网页分配ID
	public static String oripath1 = "D:\\XueBaResources\\stackoverflow";
	public static String oripath2 = "D:\\XueBaResources\\cnblogs";
	public static String oripath3 = "D:\\XueBaResources\\dewen";
	public static String oripath4 = "D:\\XueBaResources\\zhidao";
	public static String oripath5 = "D:\\XueBaResources\\wenwen";
	//public boolean flag;//是否符合过滤条件
	/**
	 * 构造函数
	 * @return 
	 * @param url,网站类别kind(int)
	 * @throws 
	 */
	public DownloadQuiz (RankUrl url,int kind){
		myUrl=url;
		PageKind = kind;
		MyCrawler.count++;
		//flag = Keyword.accept(url.getUrl());
	}
	
	/**
	 * 开始下载问答页
	 * @return 
	 * @param 
	 * @throws UnsupportedEncodingException
	 */
	public void run()
	{
		//C_ID = MyCrawler.id;
		//MyCrawler.id ++;
		if(MyCrawler.succeed >= MyCrawler.max)
			return ;
		MyCrawler.visited++;
		MyCrawler.Cr.updatePanel(MyCrawler.visited, MyCrawler.succeed, MyCrawler.failed, MyCrawler.unpassed);
		String filePath = null;
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(
				50000);
		String _s = myUrl.getUrl(),__s = "";
		
		try {
			for(int i = 0;i < _s.length();++i){
				char _c = _s.charAt(i);
				if(_c > 127 || _c == '%')
					__s += URLEncoder.encode(_c+"", "utf-8");
				else
					__s += _c;
			}
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		GetMethod getMethod = new GetMethod(__s);
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 50000);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		int statusCode=0;
		System.out.println(myUrl.url+"\t"+HttpStatus.SC_OK);
		try {
			statusCode = httpClient.executeMethod(getMethod);
			System.out.println("statusCode = " + statusCode);
			if (statusCode != HttpStatus.SC_OK) {
				MyCrawler.count--;
				MyCrawler.failed++;
				System.err.println("Method failed:"+myUrl.url);
				filePath = null;
				MyCrawler.Cr.updatePanel(MyCrawler.visited, MyCrawler.succeed, MyCrawler.failed, MyCrawler.unpassed);
				return ;
			}
			URL url = new URL(myUrl.getUrl());
	        InputStream input = url.openStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String line = reader.readLine();
			URL SonUrl;
			InputStream SonInput;
			String SonLine;
			FileWriter fw;
			BufferedWriter bw;
			FileWriter fw2 = new FileWriter(new File(C_ID+".txt"), true);;
			BufferedWriter bw2 = new BufferedWriter(fw2);
			int num = 0;
			while (line != null)
			{
				//bw2.write(line+"\n");
				if(PageKind == 1){
					if (line.indexOf("<div class=\"summary\">") >= 0)	
					{
						line = reader.readLine();
						RankUrl rankUrl = new RankUrl("http://stackoverflow.com"+line.substring(line.indexOf("/questions/"),line.indexOf("\"",line.indexOf("/questions/"))),1);
						
						SonUrl = new URL(rankUrl.getUrl());
				        SonInput = SonUrl.openStream();
						BufferedReader SonReader = new BufferedReader(new InputStreamReader(SonInput));
						SonLine = SonReader.readLine();
						
						
						C_ID = MyCrawler.id;
						MyCrawler.id ++;
						filePath = oripath1 + "\\"+ getFileNameByID(C_ID,getMethod.getResponseHeader("Content-Type").getValue());
						
						fw = new FileWriter(new File(filePath), true);
						bw = new BufferedWriter(fw);
						while (SonLine != null)
						{
							bw.write(SonLine+"\n");
							SonLine = SonReader.readLine();
						}						
						LinkQueue.addVisitedUrl(rankUrl);
						DataBase(rankUrl.url,filePath);
						MyCrawler.succeed++;
						MyCrawler.visited++;
						MyCrawler.Cr.updatePanel(MyCrawler.visited, MyCrawler.succeed, MyCrawler.failed, MyCrawler.unpassed);
						MyCrawler.ST = 0;
						//Thread.sleep(30000);
						if (MyCrawler.succeed > MyCrawler.max)	break;
					}
				}else if(PageKind == 2){
					if (line.indexOf("<a target=\"_blank\" href=\"") >= 0)	
					{
						RankUrl rankUrl = new RankUrl("http://q.cnblogs.com"+line.substring(line.indexOf("/q/"),line.indexOf("\"",line.indexOf("/q/"))),2);
						SonUrl = new URL(rankUrl.getUrl());
						SonInput = SonUrl.openStream();
				        BufferedReader SonReader = new BufferedReader(new InputStreamReader(SonInput));
						SonLine = SonReader.readLine();
						C_ID = MyCrawler.id;
						MyCrawler.id ++;
						filePath = oripath2 + "\\"+ getFileNameByID(C_ID,getMethod.getResponseHeader("Content-Type").getValue());
						InputStream is = SonUrl.openStream();
						ByteArrayOutputStream bAOut = new ByteArrayOutputStream();  
			            int c;  
			            while ((c = is.read()) != -1) {  
			                bAOut.write(c);  
			            }  
			            byte[] responseBody = bAOut.toByteArray();
			            saveToLocal(responseBody, filePath);
						LinkQueue.addVisitedUrl(rankUrl);
						DataBase(rankUrl.url,filePath);
						MyCrawler.succeed++;
						MyCrawler.visited++;
						MyCrawler.Cr.updatePanel(MyCrawler.visited, MyCrawler.succeed, MyCrawler.failed, MyCrawler.unpassed);
						MyCrawler.CN = 0;
						if (MyCrawler.succeed > MyCrawler.max)	break;
					}
				}else if(PageKind == 3){
					if (line.indexOf("<span class=\"question_listitle\">") >= 0)	
					{
						line = reader.readLine();
						RankUrl rankUrl = new RankUrl("http://www.dewen.io"+line.substring(line.indexOf("/q/"),line.indexOf("\"",line.indexOf("/q/"))),3);
						SonUrl = new URL(rankUrl.getUrl());
				        SonInput = SonUrl.openStream();
						BufferedReader SonReader = new BufferedReader(new InputStreamReader(SonInput));
						SonLine = SonReader.readLine();
						C_ID = MyCrawler.id;
						MyCrawler.id ++;
						filePath = oripath3 + "\\"+ getFileNameByID(C_ID,getMethod.getResponseHeader("Content-Type").getValue());
						fw = new FileWriter(new File(filePath), true);
						bw = new BufferedWriter(fw);
						while (SonLine != null)
						{
							bw.write(SonLine+"\n");
							SonLine = SonReader.readLine();
						}
						
						LinkQueue.addVisitedUrl(rankUrl);
						DataBase(rankUrl.url,filePath);
						MyCrawler.succeed++;
						MyCrawler.visited++;
						MyCrawler.Cr.updatePanel(MyCrawler.visited, MyCrawler.succeed, MyCrawler.failed, MyCrawler.unpassed);
						MyCrawler.DW = 0;
						if (MyCrawler.succeed > MyCrawler.max)	break;
					}
				}else if (PageKind == 4){
					if (line.indexOf("<title>") >= 0)
					{
						if (line.indexOf("百度知道 - 信息提示") < 0 && line.indexOf("百度--您的访问出错了") < 0)
						{
							System.out.println(line);
							SonUrl = new URL(myUrl.getUrl());
					        SonInput = SonUrl.openStream();
							BufferedReader SonReader = new BufferedReader(new InputStreamReader(SonInput));
							SonLine = SonReader.readLine();
							C_ID = MyCrawler.id;
							MyCrawler.id ++;
							filePath = oripath4 + "\\"+ getFileNameByID(C_ID,getMethod.getResponseHeader("Content-Type").getValue());
							fw = new FileWriter(new File(filePath), true);
							bw = new BufferedWriter(fw);
							while (SonLine != null)
							{
								bw.write(SonLine+"\n");
								SonLine = SonReader.readLine();
							}							
							LinkQueue.addVisitedUrl(myUrl);
							DataBase(myUrl.url,filePath);
							MyCrawler.succeed++;
							MyCrawler.visited++;
							MyCrawler.Cr.updatePanel(MyCrawler.visited, MyCrawler.succeed, MyCrawler.failed, MyCrawler.unpassed);
							if (MyCrawler.succeed > MyCrawler.max)	break;
						}
						MyCrawler.BZ = 0;
						break;
					}
				}
				else if (PageKind == 5){
					if (line.indexOf("/z/q") >= 0)
					{
						int i = 0,j = 0;
						while (line.indexOf("/z/q", i) >= 0){
							i = line.indexOf("/z/q", i)+4;
							j++;
							RankUrl rankUrl = new RankUrl("http://wenwen.sogou.com"+line.substring(i-4,line.indexOf("wtk.title",i)+9));
							if (j %2 == 1)	continue;
							System.out.println(rankUrl.url);
							LinkQueue.addVisitedUrl(rankUrl);				//LinkQueue.addUnvisitedUrl(rankUrl, myUrl);
							SonUrl = new URL(rankUrl.getUrl());
					        SonInput = SonUrl.openStream();
							BufferedReader SonReader = new BufferedReader(new InputStreamReader(SonInput));
							SonLine = SonReader.readLine();
							C_ID = MyCrawler.id;
							MyCrawler.id ++;
							filePath = oripath5 + "\\"+ getFileNameByID(C_ID,getMethod.getResponseHeader("Content-Type").getValue());
							fw = new FileWriter(new File(filePath), true);
							bw = new BufferedWriter(fw);
							while (SonLine != null)
							{
								bw.write(SonLine+"\n");
								SonLine = SonReader.readLine();
							}
							
							LinkQueue.addVisitedUrl(rankUrl);
							DataBase(rankUrl.url,filePath);
							MyCrawler.succeed++;
							MyCrawler.visited++;
							MyCrawler.Cr.updatePanel(MyCrawler.visited, MyCrawler.succeed, MyCrawler.failed, MyCrawler.unpassed);
							MyCrawler.WW = 0;
							if (MyCrawler.succeed > MyCrawler.max)	break;
						}
					}
				}
				if (MyCrawler.succeed > MyCrawler.max)	break;
				line = reader.readLine();
			}
		} catch (HttpException e) {
			System.out.println("Please check your provided http address!");
			if (PageKind == 1)	MyCrawler.ST = 0;
			else if (PageKind == 2)	MyCrawler.CN = 0;
			else if (PageKind == 3)	MyCrawler.DW = 0;
			else if (PageKind == 4)	MyCrawler.BZ = 0;
			else if (PageKind == 5)	MyCrawler.WW = 0;
			e.printStackTrace();
		} catch (IOException e) {
			if (PageKind == 1)	MyCrawler.ST = 0;
			else if (PageKind == 2)	MyCrawler.CN = 0;
			else if (PageKind == 3)	MyCrawler.DW = 0;
			else if (PageKind == 4)	MyCrawler.BZ = 0;
			else if (PageKind == 5)	MyCrawler.WW = 0;
			e.printStackTrace();
		} 
		catch (SQLException e) {
			if (PageKind == 1)	MyCrawler.ST = 0;
			else if (PageKind == 2)	MyCrawler.CN = 0;
			else if (PageKind == 3)	MyCrawler.DW = 0;
			else if (PageKind == 4)	MyCrawler.BZ = 0;
			else if (PageKind == 5)	MyCrawler.WW = 0;
			e.printStackTrace();
		} 
		finally {
			getMethod.releaseConnection();
		}
		MyCrawler.count--;
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
		pagetype="quiz";
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
					 +"\',\'"+0
					 +"\')";
		String sql="insert into fileinfo values"+state+";";
		System.out.printf("DownloadQuiz:%s\n", sql);
		ConnectServer.update(sql);
	}
	/**
	 * 通过ID获得文件名
	 * @return 文件名(String)
	 * @param id(int),网站类型contentType
	 * @throws 
	 */
	public String getFileNameByID(int _id,String contentType)
	{
		String res;
		res = String.valueOf(_id);
		if(contentType.indexOf("html")!=-1)
		{
			res= res + ".html";
		}
		else if(contentType.indexOf("pdf")!=-1)
		{
			res= res + ".pdf";
		}
		else
		{
			res= res + "." + contentType.substring(contentType.lastIndexOf("/")+1);
		}	
		return res;
	}
	/**
	 * 通过网址得到文件名
	 * @return 文件名(String)
	 * @param 网址url,网站类型contentType
	 * @throws 
	 */
	public  String getFileNameByUrl(String url,String contentType)
	{
		//remove http://
		url=url.substring(7);
		//text/html类型
		if(contentType.indexOf("html")!=-1)
		{
			url= url.replaceAll("[\\?/:*|<>\"]", "_")+".html";
			return url;
		}
		//如application/pdf类型
		else if(contentType.indexOf("pdf")!=-1)
		{
			url= url.replaceAll("[\\?/:*|<>\"]", "_");
			if(!url.endsWith(".pdf"))
				url += ".pdf";

			//url=url;
			return url;
		}
		else
		{
          return url.replaceAll("[\\?/:*|<>\"]", "_")+"."+
          contentType.substring(contentType.lastIndexOf("/")+1);
		}	
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
