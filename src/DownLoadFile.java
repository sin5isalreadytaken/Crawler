

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
* description: ��ȡָ�������ӣ����ص����أ����������ݿ�
* note: ���ö��߳���ȡ�����ڲ�ͬ��վ�����ò�ͬ��ȡ��ʩ
* modificationDate: 2014-11-27
*/ 
public class DownLoadFile extends Thread{
	/**
	 * ���� url ����ҳ����������Ҫ�������ҳ���ļ��� ȥ���� url �з��ļ����ַ�
	 */
	//	ԭʼ�汾��myUrlΪString������ʹ���������ַ����ĵط�ʹ��myUrl.getUrl()���ɣ����޸Ĺ���edit by hxy
	RankUrl myUrl;
	LinkFilter myFilter;
	boolean moviesign=false;
	int C_ID = 0;
	public static String oripath = "D:\\XueBaResources";
	//public static String oripath = System.getProperty("user.dir");//��ȡ��ǰ·��
	public boolean flag;
	public Lock lock;
	public static boolean pdf=false;
	public static boolean ppt=false;
	public static boolean doc=false;
	public static boolean video=false;
	/**
	 * ���캯��
	 * @return 
	 * @param ��ַurl,������filter
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
	 * ��ʼ����
	 * @return 
	 * @param url,������filter
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
		/* 1.���� HttpClinet �������ò��� */
		HttpClient httpClient = new HttpClient();
		// ���� Http ���ӳ�ʱ 50s
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(
				50000);
		//HttpClientί��HttpConnectionManager�������ӣ�ί��HttpMethodDirectorִ�з������䱾������״̬�̰߳�ȫ�ġ�
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
		//ת��֮ǰ
		System.out.println(_s);
		//ת��֮��
		System.out.println(__s);
		/* 2.���� GetMethod �������ò��� */
		GetMethod getMethod = new GetMethod(__s);
		// ���� get ����ʱ 50s
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 50000);
		// �����������Դ���
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		int statusCode=0;
		String contType="";
		
		/* 3.ִ�� HTTP GET ���� */
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
			System.out.println();
			System.out.println(contType+" "+myUrl.url+ " "+myUrl.getUrl()+ "            caonima!");
			/*
			Content-Type���������ͣ�һ����ָ��ҳ�д��ڵ�Content-Type��
			���ڶ��������ļ������ͺ���ҳ�ı��룬�������������ʲô��ʽ��
			ʲô�����ȡ����ļ���
			����Ǿ�������һЩAsp��ҳ����Ľ��ȴ�����ص���һ���ļ���һ��ͼƬ��ԭ��
			public Header getResponseHeader(String headerName)
			�õ�����������ص���Ӧͷ����������ƥ���ǲ����ִ�Сд�� 
			HTTP��ͷ�����ͨ��ͷ������ͷ����Ӧͷ��ʵ��ͷ�ĸ����֡�
			ÿ��ͷ����һ��������ð�ţ�:������ֵ��������ɡ�
			�����Ǵ�Сд�޹صģ���ֵǰ��������κ������Ŀո����
			ͷ����Ա���չΪ���У���ÿ�п�ʼ����ʹ������һ���ո���Ʊ���� 
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
			
			//��ȡ��������ҳ�е� URL
			if(contType.equals("html"))
			{
				Set<String> links=HtmlParserTool.extracLinks(myUrl.getUrl(),myFilter,this);
			//�µ�δ���ʵ� URL ���, �ѷ��ʺ�δ���ʵĶ���������
				for(String link:links)	
				{
					LinkQueue.addUnvisitedUrl(new RankUrl(link), myUrl);
					
					//System.out.println("������е�url :	"+links);
				}
		
			}
			
			InputStream temp = getMethod.getResponseBodyAsStream();  
            ByteArrayOutputStream bAOut = new ByteArrayOutputStream();  
            int c;  
            while ((c = temp.read()) != -1) {  
                bAOut.write(c);  
            }  
            byte[] responseBody = bAOut.toByteArray();  
            // ��ȡΪ�ֽ�����
			// ������ҳ url ���ɱ���ʱ���ļ���
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
					//��ȡ�����Σ�����
					OutputStream bos = new FileOutputStream(new File(filePath));
					if((len = i.read(responseBody))!=-1)
						bos.write(responseBody, 0, len);
					/*
					read(byte[] b)����������ȹ涨һ�����鳤�ȣ�
					��������е��ֽڻ��嵽����b�У����ص���������е��ֽڸ�����
					���������û�����Ļ����򷵻���ʵ���ֽڸ�������δβʱ������-1
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
			// �����������쳣��������Э�鲻�Ի��߷��ص�����������
			System.out.println("Please check your provided http address!");
			e.printStackTrace();
		} catch (IOException e) {
			// ���������쳣
			e.printStackTrace();
		} finally {
			// �ͷ�����
			getMethod.releaseConnection();
		}
		
		//System.out.println("!!!!!download:  "+myUrl);
		//�� url ���뵽�ѷ��ʵ� URL ��
		/*LinkQueue.addVisitedUrl(myUrl);
		
		//��ȡ��������ҳ�е� URL
		if(contType.equals("html"))
		{
			Set<String> links=HtmlParserTool.extracLinks(myUrl.getUrl(),myFilter);
		//�µ�δ���ʵ� URL ���, �ѷ��ʺ�δ���ʵĶ���������
			for(String link:links)	
			{
				LinkQueue.addUnvisitedUrl(new RankUrl(link), myUrl);
				
				//System.out.println("������е�url :	"+links);
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
			
			//�ʴ�
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
					Parser parser2 = new Parser(myUrl.getUrl());/*HTML������*/
				//parser2.setEncoding("gb2312");
					parser2.setEncoding("UTF-8");
					//����Parser������ַ�����,һ������ҳ���ַ����뱣��һ�� 
					URLConnection urs = parser2.getConnection(); 
					SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
					lastcrawlertime=format.format(new java.util.Date());
					freshtime=lastcrawlertime;
					//��ȡ��ҳ�ļ�������޸�ʱ��ʹ�С
					freshtime=format.format(new java.util.Date(urs.getLastModified()));
					if(freshtime.indexOf("1970")>=0)
						freshtime=lastcrawlertime;
					//System.out.println("��ȡ����޸�ʱ�䷽��1��"+format.format(new Date(urs.getHeaderField("Last-modified")))); 
					//System.out.println("��ȡ����޸�ʱ�䷽��2��"+format.format(new Date(urs.getLastModified())));
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
					/*HTMLParser ������������Ϣ����Ϊһ�����Ľṹ�� Node ����Ϣ������������ͻ��� ���뿴 Node �Ķ��壺*/
				} catch (ParserException e1) {
					e1 = null;
				}
				keywords=page.getTitle();
				System.out.println(keywords);
				// ���еĽڵ�
				NodeList nodelist = page.getBody();
				// ����һ���ڵ�filter���ڹ��˽ڵ�
				NodeFilter filter = new TagNameFilter("A");
				// �õ����й��˺���Ҫ�Ľڵ�
				nodelist = nodelist.extractAllNodesThatMatch(filter, true);
				for (int i = 0; i < nodelist.size(); i++) {
					LinkTag link = (LinkTag) nodelist.elementAt(i);
					// ���ӵ�ַ
					//System.out.println(link.getLink());
					String llink=link.getLink();
					//System.out.println(myUrl);
					String ltext=link.getStringText();
					
					if(llink!=null&&ltext!=null&&LinkQueue.getTagtext().containsKey(llink)==false)
					{
						LinkQueue.getTagtext().put(llink,ltext);
						
					}
					//System.out.println(link.getAttribute("href") );//ȥ������Ϊ�յģ�����http��ͷ��
					// ��������
					//if(link.getAttribute("href")!=null &&link.getAttribute("href").indexOf("Keyword")>0)
						//System.out.println(link.getStringText() + "\n");//ȥ������<img>
					
					
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
			String state="(\'"+(C_ID)//��ҳid
							+"\',\'"+myUrl.getUrl()//����
							+"\',\'"+filePath//�ļ��洢·��
							+"\',\'"+encode//
							+"\',\'"+pagetype//��ҳ����
							+"\',\'"+lastcrawlertime//���������ʱ��
							+"\',\'"+freshtime//����ʱ��
							+"\',\'"+keywords//����
							+"\',\'"+tag//��ǩ
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
//		MyCrawler.Cr.jl1.setText("��ǰ�߳���		��	"+MyCrawler.count+"	----"+LinkQueue.getUnVisitedUrl().size());
//		MyCrawler.Cr.jl2.setText("�ѷ���URL����     :    " + MyCrawler.number);
//		MyCrawler.Cr.progress.setValue( MyCrawler.number);
	}
	/**
	 * ͨ��ID����ļ���
	 * @return �ļ���(String)
	 * @param id(int),��վ����contentType
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
		//��application/pdf����
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
	 * ͨ����ַ�õ��ļ���
	 * @return �ļ���(String)
	 * @param ��ַurl,��վ����contentType
	 * @throws 
	 */
	public  String getFileNameByUrl(String url,String contentType)
	{
		//remove http://
		url=url.substring(7);
		//text/html����
		if(contentType.indexOf("html")!=-1)
		{
			url= url.replaceAll("[\\?/:*|<>\"]", "_")+".html";
			return url;
		}
		//��application/pdf����
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
