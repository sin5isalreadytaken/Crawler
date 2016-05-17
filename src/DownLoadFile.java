

import java.io.*;
import java.net.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
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
* description: 爬取指定的链接，下载到本地，并更新数据库
* note: 采用多线程爬取，对于不同网站，采用不同爬取措施
* modificationDate: 2014-11-27
*/ 
public class DownLoadFile extends Thread{
	/**
	 * 根据 url 和网页类型生成需要保存的网页的文件名 去除掉 url 中非文件名字符
	 */
	//	原始版本中myUrl为String，所有使用其链接字符串的地方使用myUrl.getUrl()即可（已修改过）edit by hxy
	RankUrl myUrl;
	LinkFilter myFilter;
	boolean moviesign=false;
	int C_ID = 0;
	public static String oripath = "D:\\XueBaResources";
	//public static String oripath = System.getProperty("user.dir");//获取当前路径
	public boolean flag;
	public Lock lock;
	public static boolean pdf=false;
	public static boolean ppt=false;
	public static boolean doc=false;
	public static boolean video=false;
	/**
	 * 构造函数
	 * @return 
	 * @param 网址url,过滤器filter
	 * @throws 
	 */
 	public DownLoadFile (RankUrl url,LinkFilter filter,Lock lock){
		myUrl=url;
		myFilter=filter;
		MyCrawler.count++;
		flag = Keyword.accept(url.getUrl());
		this.lock=lock;
		//System.out.println("count:"+MyCrawler.count);
	}
	
	/**
	 * 开始下载
	 * @return 
	 * @param url,过滤器filter
	 * @throws 
	 */
	public void run()
	{
		C_ID = MyCrawler.id;
		MyCrawler.id ++;
		if(MyCrawler.succeed >= MyCrawler.max)
			return ;
		MyCrawler.visited++;
		//MyCrawler.state.updatePanel(MyCrawler.visited, MyCrawler.succeed, MyCrawler.failed, MyCrawler.unpassed);
		
		String filePath = null;
		/* 1.生成 HttpClinet 对象并设置参数 */
		HttpClient httpClient = new HttpClient();
		// 设置 Http 连接超时 50s
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(
				50000);
		//HttpClient委托HttpConnectionManager管理连接，委托HttpMethodDirector执行方法，其本身是无状态线程安全的。
		//http://www.ibm.com/developerworks/cn/opensource/os-cn-crawler/
		String _s = myUrl.getUrl(),__s = "";
		
		try {
			for(int i = 0;i < _s.length();++i){
				char _c = _s.charAt(i);
				if(_c > 127 || _c == '%')
					__s += URLEncoder.encode(_c+"", "utf-8");
				else
					__s += _c;
			}
			//_s = new String(myUrl.getUrl().getBytes("UTF-8"),"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//转码之前
		System.out.println(_s);
		//转码之后
		System.out.println(__s);
		/* 2.生成 GetMethod 对象并设置参数 */
		GetMethod getMethod = new GetMethod(__s);
		// 设置 get 请求超时 50s
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 50000);
		// 设置请求重试处理
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		int statusCode=0;
		String contType="";
		
		/* 3.执行 HTTP GET 请求 */
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
			System.out.println();
			System.out.println(contType+" "+myUrl.url+ " "+myUrl.getUrl()+ "            caonima!");
			/*
			Content-Type，内容类型，一般是指网页中存在的Content-Type，
			用于定义网络文件的类型和网页的编码，决定浏览器将以什么形式、
			什么编码读取这个文件，
			这就是经常看到一些Asp网页点击的结果却是下载到的一个文件或一张图片的原因。
			public Header getResponseHeader(String headerName)
			得到了与名字相关的响应头。标题名称匹配是不区分大小写。 
			HTTP的头域包括通用头，请求头，响应头和实体头四个部分。
			每个头域由一个域名，冒号（:）和域值三部分组成。
			域名是大小写无关的，域值前可以添加任何数量的空格符，
			头域可以被扩展为多行，在每行开始处，使用至少一个空格或制表符。 
			*/
			String TT=myUrl.url;
			if(contType.indexOf("html")>=0)
			{
				//System.out.printf("It's a html.");
				contType="html";
			}
			else if(contType.endsWith("pdf")||TT.endsWith(".pdf")||TT.endsWith(".PDF"))
			{
				//System.out.printf("It's a pdf!");
				contType="pdf";
			}
			else if(TT.endsWith(".ppt")||TT.endsWith(".pptx")||TT.endsWith(".PPT")||TT.endsWith(".PPTX"))
			{
				//System.out.printf("It's a pdf!");
				contType="ppt";
			}
			else if(TT.endsWith(".doc")||TT.endsWith(".docx")||TT.endsWith(".DOC")||TT.endsWith(".DOCX")||TT.endsWith(".txt")||TT.endsWith(".TXT"))
			{
				//System.out.printf("It's a pdf!");
				contType="doc";
			}
			else
				contType="else";
			System.out.println(contType);
			
			LinkQueue.addVisitedUrl(myUrl);
			
			//提取出下载网页中的 URL
			if(contType.equals("html"))
			{
				Set<String> links=HtmlParserTool.extracLinks(myUrl.getUrl(),myFilter,this);
			//新的未访问的 URL 入队, 已访问和未访问的都加入外链
				for(String link:links)	
				{
					LinkQueue.addUnvisitedUrl(new RankUrl(link), myUrl);
					
					//System.out.println("进入对列的url :	"+links);
				}
		
			}
			
			InputStream temp = getMethod.getResponseBodyAsStream();  
            ByteArrayOutputStream bAOut = new ByteArrayOutputStream();  
            int c;  
            while ((c = temp.read()) != -1) {  
                bAOut.write(c);  
            }  
            byte[] responseBody = bAOut.toByteArray();  
            // 读取为字节数组
			// 根据网页 url 生成保存时的文件名
            	if(moviesign){
            		filePath = oripath + "\\"+"mp4"+"\\"
							+ getFileNameByID(C_ID,getMethod.getResponseHeader(
									"Content-Type").getValue());
            	}
            	else if(contType.equals("html")||contType.equals("else"))
				{
					filePath = oripath + "\\"
							+ getFileNameByID(C_ID,getMethod.getResponseHeader(
									"Content-Type").getValue());
//					filePath = oripath + "\\"
//					+ getFileNameByUrl(myUrl.getUrl(), getMethod.getResponseHeader(
//							"Content-Type").getValue());
				}
				else
				{
					URL u = new URL(myUrl.getUrl());
					int len;
					InputStream i = u.openStream();
					filePath = oripath + "\\"+contType+"\\"
							+ getFileNameByID(C_ID,contType);
					//filePath = oripath + "\\"+getFileNameByUrl(myUrl.getUrl(),"pdf");
					//System.out.println(filePath);
					//读取了两次？存疑
					OutputStream bos = new FileOutputStream(new File(filePath));
					if((len = i.read(responseBody))!=-1)
						bos.write(responseBody, 0, len);
					/*
					read(byte[] b)这个方法是先规定一个数组长度，
					将这个流中的字节缓冲到数组b中，返回的这个数组中的字节个数，
					这个缓冲区没有满的话，则返回真实的字节个数，到未尾时都返回-1
					*/
				} 
				
				synchronized(lock)
				{
					if(MyCrawler.succeed<MyCrawler.max){
						if((flag&&!pdf&&!ppt&&!doc&&!video) || (contType.equals("pdf")&&pdf)||(contType.equals("ppt")&&ppt)||(contType.equals("doc")&&doc)||(moviesign&&video))
							MyCrawler.succeed++;
					}
					else
						return;
				}

						if((flag&&!pdf&&!ppt&&!doc&&!video) || (contType.equals("pdf")&&pdf)||(contType.equals("ppt")&&ppt)||(contType.equals("doc")&&doc)||(moviesign&&video)){
							MyCrawler.Cr.updatePanel(MyCrawler.visited, MyCrawler.succeed, MyCrawler.failed, MyCrawler.unpassed);
							saveToLocal(responseBody, filePath);
						}else{
							MyCrawler.Cr.updatePanel(MyCrawler.visited, MyCrawler.succeed, MyCrawler.failed, MyCrawler.unpassed);					
						}
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			System.out.println("Please check your provided http address!");
			e.printStackTrace();
		} catch (IOException e) {
			// 发生网络异常
			e.printStackTrace();
		} finally {
			// 释放连接
			getMethod.releaseConnection();
		}
		
		//System.out.println("!!!!!download:  "+myUrl);
		//该 url 放入到已访问的 URL 中
		/*LinkQueue.addVisitedUrl(myUrl);
		
		//提取出下载网页中的 URL
		if(contType.equals("html"))
		{
			Set<String> links=HtmlParserTool.extracLinks(myUrl.getUrl(),myFilter);
		//新的未访问的 URL 入队, 已访问和未访问的都加入外链
			for(String link:links)	
			{
				LinkQueue.addUnvisitedUrl(new RankUrl(link), myUrl);
				
				//System.out.println("进入对列的url :	"+links);
			}
	
		}*/
		
			String encode = "utf8";
			String keywords=null;
			String pagetype=null;
			//java.sql.Date lastcrawlertime=null;
			//java.sql.Date freshtime=null;
			String lastcrawlertime="1980-01-01";
			String freshtime="1980-01-01";
			String tag=null;
			String host=null;
			
			//问答
			//	q.cnblogs/q/12
			//	stackoverflow.com/questions/12
			//	zhihu.com/question/12
			//	zhidao.baidu.com/question/12
			//	wenwen.sogou.com/z/q12
			System.out.println(myUrl.url+"++++++++++++++++++++++++++++++++++++++++++++++++++");
			if(moviesign)
				pagetype="videoweb";
			else if(myUrl.getUrl().endsWith(".pdf")||myUrl.getUrl().endsWith(".PDF"))
				pagetype="pdf";
			else if(myUrl.getUrl().endsWith(".ppt")||myUrl.getUrl().endsWith(".pptx")||myUrl.getUrl().endsWith(".PPT")||myUrl.getUrl().endsWith(".PPTX"))
				pagetype="ppt";
			else if(myUrl.getUrl().endsWith(".doc")||myUrl.getUrl().endsWith(".docx")||myUrl.getUrl().endsWith(".txt")||myUrl.getUrl().endsWith(".DOC")||myUrl.getUrl().endsWith(".DOCX"))
				pagetype="doc";
			else if(myUrl.getUrl().indexOf("q.cnblog/q/")>=0
			||myUrl.getUrl().indexOf("stackoverflow.com/questions/")>=0
			||myUrl.getUrl().indexOf("zhihu.com/question/")>=0
			||myUrl.getUrl().indexOf("zhidao.baidu.com/question/")>=0
			||myUrl.getUrl().indexOf("wenwen.sogou.com/z/q")>=0)
				pagetype="quiz";
			else
				pagetype="webpage";
			 host=myUrl.getUrl().split("/")[2];
				host="http://"+host;
			
			try
			{
				/*if(contType.equals("pdf")||contType.equals("html"))
				{*/
					Parser parser2 = new Parser(myUrl.getUrl());/*HTML解析器*/
				//parser2.setEncoding("gb2312");
					parser2.setEncoding("UTF-8");
					//设置Parser对象的字符编码,一般与网页的字符编码保持一致 
					URLConnection urs = parser2.getConnection(); 
					SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
					lastcrawlertime=format.format(new java.util.Date());
					freshtime=lastcrawlertime;
					//获取网页文件的最后修改时间和大小
					freshtime=format.format(new java.util.Date(urs.getLastModified()));
					if(freshtime.indexOf("1970")>=0)
						freshtime=lastcrawlertime;
					//System.out.println("获取最后修改时间方法1："+format.format(new Date(urs.getHeaderField("Last-modified")))); 
					//System.out.println("获取最后修改时间方法2："+format.format(new Date(urs.getLastModified())));
						/*String a=format.format(new java.util.Date(urs.getLastModified()));
						//java.util.Date lastcrawlertime1=format.parse(a);
						lastcrawlertime= new java.sql.Date(urs.getLastModified());
						String b=format.format(new java.util.Date());
						//java.util.Date freshtime1=format.parse(b);
						//freshtime= new java.sql.Date(new java.util.Date());
					if(a.indexOf("1970")>=0)
					{
						lastcrawlertime=freshtime;
					}
					*/
				
				
				HtmlPage page = new HtmlPage(parser2);
				
				try {
					// HtmlPage extends visitor,Apply the given visitor to the current
					// page.
					parser2.visitAllNodesWith(page);
					/*HTMLParser 将解析过的信息保存为一个树的结构。 Node 是信息保存的数据类型基础 。请看 Node 的定义：*/
				} catch (ParserException e1) {
					e1 = null;
				}
				keywords=page.getTitle();
				System.out.println(keywords);
				// 所有的节点
				NodeList nodelist = page.getBody();
				// 建立一个节点filter用于过滤节点
				NodeFilter filter = new TagNameFilter("A");
				// 得到所有过滤后，想要的节点
				nodelist = nodelist.extractAllNodesThatMatch(filter, true);
				for (int i = 0; i < nodelist.size(); i++) {
					LinkTag link = (LinkTag) nodelist.elementAt(i);
					// 链接地址
					//System.out.println(link.getLink());
					String llink=link.getLink();
					//System.out.println(myUrl);
					String ltext=link.getStringText();
					
					if(llink!=null&&ltext!=null&&LinkQueue.getTagtext().containsKey(llink)==false)
					{
						LinkQueue.getTagtext().put(llink,ltext);
						
					}
					//System.out.println(link.getAttribute("href") );//去掉各种为空的，不已http开头的
					// 链接名称
					//if(link.getAttribute("href")!=null &&link.getAttribute("href").indexOf("Keyword")>0)
						//System.out.println(link.getStringText() + "\n");//去掉各种<img>
					
					
				}
				//System.out.println(page.getTitle());
				
				
				
				if(LinkQueue.getTagtext().containsKey(myUrl.getUrl())==true)
				{
					tag=(String) LinkQueue.getTagtext().get("myUrl");
				}
				
				
				//System.out.println(urs.getContentEncoding());
				/*String encoding="";
				NodeFilter tagFilter = new TagNameFilter("meta");
				HasAttributeFilter haf = new HasAttributeFilter("http-equiv", "Content-Type");
				//HasAttributeFilter haf = new HasAttributeFilter("http-equiv", "content");
				AndFilter af = new AndFilter(tagFilter,haf);
				NodeList nodes = parser2.extractAllNodesThatMatch(af);
				
				if (nodes != null) {
				TagNode liTag = (TagNode) nodes.elementAt(0);
				System.out.println("i can get here");
				System.out.println(liTag.getTagName());
				//encoding =liTag.getText();
				System.out.println(liTag.getAttribute("content"));
				//encoding = liTag.getAttribute("content").split(";")[1].trim();
				//encoding = encoding.split("=")[1].trim();
				//System.out.println(encoding);
				}*/
				//}
			}catch(ParserException e) {
				e.printStackTrace();
			} 
			
			
			
			
			
			/*catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			if((flag&&!pdf&&!ppt&&!doc) || (contType.equals("pdf")&&pdf)||(contType.equals("ppt")&&ppt)||(contType.equals("doc")&&doc))
			{
			String state="(\'"+(C_ID)//网页id
							+"\',\'"+myUrl.getUrl()//域名
							+"\',\'"+filePath//文件存储路径
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
			System.out.printf("haha:%s\n", sql);
			if(flag || contType.equals("pdf"))
				try {
					ConnectServer.update(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			/*int i=ConnectServer.update(sql);
			if(i!=0)
				MyCrawler.id++;*/
			}
		MyCrawler.count--;
		//System.out.println("count:"+MyCrawler.count);
//		MyCrawler.Cr.jl1.setText("当前线程数		：	"+MyCrawler.count+"	----"+LinkQueue.getUnVisitedUrl().size());
//		MyCrawler.Cr.jl2.setText("已访问URL个数     :    " + MyCrawler.number);
//		MyCrawler.Cr.progress.setValue( MyCrawler.number);
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
		//如application/pdf类型
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
