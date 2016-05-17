

import java.io.IOException;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.HtmlPage;
/**
* description: 过滤并获取指定网址的子链接，同时更新网站子链接的修改时间。
* note: 过滤并获取指定网址的子链接的速度与准确度
* modificationDate: 2014-11-26
*/ 
public class HtmlParserTool {
	/**
	 * 过滤并获取网站子链接
	 * @return 
	 * @param 网址url,过滤器filter
	 * @throws ParserException
	 */
	public static Set<String> extracLinks(String url, LinkFilter filter,DownLoadFile d) {

		Set<String> links = new HashSet<String>();
		try {
			// 构造解析器：通过指定URLConnection对象创建Parser对象 
			Parser parser = new Parser(url);
			//设置Parser对象的字符编码,一般与网页的字符编码保持一致
			parser.setEncoding("UTF-8");
			//
			// 过滤 <frame >标签的 filter，用来提取 frame 标签里的 src 属性所表示的链接
			//
			@SuppressWarnings("serial")
			//创建NodeFilter实例 
			NodeFilter frameFilter = new NodeFilter() {
				public boolean accept(Node node) {
					if (node.getText().startsWith("frame src=")) {						
						return true;
					} else {
						return false;
					}
				}
			};
			// OrFilter筛选出所有 <a> 标签和 <frame> 标签（OrFilter:满足两者之一即可被选中）
			// NodeClassFilter： 利用标签类别过滤（LinkTag.class => <a>标签）
			OrFilter linkFilter = new OrFilter(new NodeClassFilter(
					LinkTag.class), frameFilter);
			// 得到所有经过过滤的标签
			NodeList list = parser.extractAllNodesThatMatch(linkFilter);
			for (int i = 0; i < list.size(); i++) {
				Node tag = list.elementAt(i);
				if (tag instanceof LinkTag) { // <a>标签
					LinkTag link = (LinkTag) tag;
					String linkUrl = link.getLink();// url
					//System.out.println(url+"000000000000000000000000"+link.getLink());

					//filter.accept(linkUrl):找回去发现成立的条件是linkUrl以http:开头，且非图片
					//其他 && 项排除了各种非法格式
					if(linkUrl.endsWith(".mp4")){
						d.moviesign=true;
						System.out.println(url+"000000000000000000000000"+link.getLink());
					}
					if (filter.accept(linkUrl) && linkUrl.indexOf("#")<0 && linkUrl.indexOf("mp4")<0
							&& linkUrl.indexOf("rar")<0 && linkUrl.indexOf("wmv")<0
							&& linkUrl.indexOf("zip")<0 && linkUrl.indexOf("gz")<0
							&& linkUrl.indexOf("[")<0 && linkUrl.indexOf("]")<0 && linkUrl.indexOf("aspx")<0
							/*&&(linkUrl.indexOf("stackoverflow.com/questions/1")>=0
								||linkUrl.indexOf("stackoverflow.com/questions/2")>=0
								||linkUrl.indexOf("stackoverflow.com/questions/3")>=0
								||linkUrl.indexOf("stackoverflow.com/questions/4")>=0
								||linkUrl.indexOf("stackoverflow.com/questions/5")>=0
								||linkUrl.indexOf("stackoverflow.com/questions/6")>=0
								||linkUrl.indexOf("stackoverflow.com/questions/7")>=0
								||linkUrl.indexOf("stackoverflow.com/questions/8")>=0
								||linkUrl.indexOf("stackoverflow.com/questions/9")>=0
							)*/
							)
						links.add(linkUrl);
				} 
				else {// <frame> 标签		
					// 提取 frame 里 src 属性的链接如 <frame src="test.html"/>
					String frame = tag.getText();
					int start = frame.indexOf("src=");
					frame = frame.substring(start);
					int end = frame.indexOf(" ");
					if (end == -1)
						end = frame.indexOf(">");
					String frameUrl = frame.substring(5, end - 1);
					if (filter.accept(frameUrl))
						links.add(frameUrl);
					else{
						MyCrawler.unpassed++;
						MyCrawler.Cr.updatePanel(MyCrawler.visited, MyCrawler.succeed, MyCrawler.failed, MyCrawler.unpassed);
					}
				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return links;
	}
	/**
	 * 过滤并获取网站子链接
	 * @return 
	 * @param 网址url,过滤器filter
	 * @throws ParserException
	 */
	public static Set<String> extracLinks_gb(String url, LinkFilter filter) {

		Set<String> links = new HashSet<String>();
		try {
			// 构造解析器：通过指定URLConnection对象创建Parser对象 
			Parser parser = new Parser(url);
			//设置Parser对象的字符编码,一般与网页的字符编码保持一致
			parser.setEncoding("gb2312");
			//
			// 过滤 <frame >标签的 filter，用来提取 frame 标签里的 src 属性所表示的链接
			//
			@SuppressWarnings("serial")
			//创建NodeFilter实例 
			NodeFilter frameFilter = new NodeFilter() {
				public boolean accept(Node node) {
					if (node.getText().startsWith("frame src=")) {						
						return true;
					} else {
						return false;
					}
				}
			};
			// OrFilter筛选出所有 <a> 标签和 <frame> 标签（OrFilter:满足两者之一即可被选中）
			// NodeClassFilter： 利用标签类别过滤（LinkTag.class => <a>标签）
			OrFilter linkFilter = new OrFilter(new NodeClassFilter(
					LinkTag.class), frameFilter);
			// 得到所有经过过滤的标签
			NodeList list = parser.extractAllNodesThatMatch(linkFilter);
			for (int i = 0; i < list.size(); i++) {
				Node tag = list.elementAt(i);
				if (tag instanceof LinkTag) { // <a>标签
					LinkTag link = (LinkTag) tag;
					String linkUrl = link.getLink();// url
					//filter.accept(linkUrl):找回去发现成立的条件是linkUrl以http:开头，且非图片
					//其他 && 项排除了各种非法格式
					if (filter.accept(linkUrl) && linkUrl.indexOf("#")<0 && linkUrl.indexOf("mp4")<0
							&& linkUrl.indexOf("rar")<0 && linkUrl.indexOf("wmv")<0
							&& linkUrl.indexOf("zip")<0 && linkUrl.indexOf("gz")<0
							&& linkUrl.indexOf("[")<0 && linkUrl.indexOf("]")<0 && linkUrl.indexOf("aspx")<0
							/*&&(linkUrl.indexOf("stackoverflow.com/questions/1")>=0
								||linkUrl.indexOf("stackoverflow.com/questions/2")>=0
								||linkUrl.indexOf("stackoverflow.com/questions/3")>=0
								||linkUrl.indexOf("stackoverflow.com/questions/4")>=0
								||linkUrl.indexOf("stackoverflow.com/questions/5")>=0
								||linkUrl.indexOf("stackoverflow.com/questions/6")>=0
								||linkUrl.indexOf("stackoverflow.com/questions/7")>=0
								||linkUrl.indexOf("stackoverflow.com/questions/8")>=0
								||linkUrl.indexOf("stackoverflow.com/questions/9")>=0
							)*/
							)
						links.add(linkUrl);
				} 
				else {// <frame> 标签		
					// 提取 frame 里 src 属性的链接如 <frame src="test.html"/>
					String frame = tag.getText();
					int start = frame.indexOf("src=");
					frame = frame.substring(start);
					int end = frame.indexOf(" ");
					if (end == -1)
						end = frame.indexOf(">");
					String frameUrl = frame.substring(5, end - 1);
					if (filter.accept(frameUrl))
						links.add(frameUrl);
					else{
						MyCrawler.unpassed++;
						MyCrawler.Cr.updatePanel(MyCrawler.visited, MyCrawler.succeed, MyCrawler.failed, MyCrawler.unpassed);
					}
				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return links;
	}
//	/**
//	 * 获取网站子链接的修改时间
//	 * @return 
//	 * @param 网址url
//	 * @throws ParserException
//	 */
//	public static void extracherftext(String url)
//	{
//		try
//		{
//			Parser parser2 = new Parser(url);
//			//parser2.setEncoding("gb2312");
//			parser2.setEncoding("UTF-8");
//			URLConnection urs = parser2.getConnection(); 
//			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			//System.out.println("获取最后修改时间方法1："+format.format(new Date(urs.getHeaderField("Last-modified")))); 
//			System.out.println("获取最后修改时间方法2："+format.format(new Date(urs.getLastModified())));
//			HtmlPage page = new HtmlPage(parser2);
//			try {
//				// HtmlPage extends visitor,Apply the given visitor to the current
//				// page.
//				parser2.visitAllNodesWith(page);
//			} catch (ParserException e1) {
//				e1 = null;
//			}
//			// 所有的节点
//			NodeList nodelist = page.getBody();
//			// 建立一个节点filter用于过滤节点
//			NodeFilter filter = new TagNameFilter("A");
//			// 得到所有过滤后，想要的节点
//			nodelist = nodelist.extractAllNodesThatMatch(filter, true);
//			for (int i = 0; i < nodelist.size(); i++) {
//				LinkTag link = (LinkTag) nodelist.elementAt(i);
//				// 链接地址
//				System.out.println(link.getLink());
//				String llink=link.getLink();
//				String ltext=link.getStringText();
//				if(llink!=null&&ltext!=null&&LinkQueue.getTagtext().containsKey(llink)==false)
//				{
//					LinkQueue.getTagtext().put(llink,ltext);
//				}
//				//System.out.println(link.getAttribute("href") );//去掉各种为空的，不已http开头的
//				// 链接名称
//				//if(link.getAttribute("href")!=null &&link.getAttribute("href").indexOf("Keyword")>0)
//					//System.out.println(link.getStringText() + "\n");//去掉各种<img>
//				
//			}
//			System.out.println(page.getTitle());
//			
//			//System.out.println(urs.getContentEncoding());
//			/*String encoding="";
//			NodeFilter tagFilter = new TagNameFilter("meta");
//			HasAttributeFilter haf = new HasAttributeFilter("http-equiv", "Content-Type");
//			//HasAttributeFilter haf = new HasAttributeFilter("http-equiv", "content");
//			AndFilter af = new AndFilter(tagFilter,haf);
//			NodeList nodes = parser2.extractAllNodesThatMatch(af);
//			
//			if (nodes != null) {
//			TagNode liTag = (TagNode) nodes.elementAt(0);
//			System.out.println("i can get here");
//			System.out.println(liTag.getTagName());
//			//encoding =liTag.getText();
//			System.out.println(liTag.getAttribute("content"));
//			//encoding = liTag.getAttribute("content").split(";")[1].trim();
//			//encoding = encoding.split("=")[1].trim();
//			//System.out.println(encoding);
//			}*/
//		
//		}catch(ParserException e) {
//			e.printStackTrace();
//		}
//	}
}
