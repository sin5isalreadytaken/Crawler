

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
* description: ���˲���ȡָ����ַ�������ӣ�ͬʱ������վ�����ӵ��޸�ʱ�䡣
* note: ���˲���ȡָ����ַ�������ӵ��ٶ���׼ȷ��
* modificationDate: 2014-11-26
*/ 
public class HtmlParserTool {
	/**
	 * ���˲���ȡ��վ������
	 * @return 
	 * @param ��ַurl,������filter
	 * @throws ParserException
	 */
	public static Set<String> extracLinks(String url, LinkFilter filter,DownLoadFile d) {

		Set<String> links = new HashSet<String>();
		try {
			// �����������ͨ��ָ��URLConnection���󴴽�Parser���� 
			Parser parser = new Parser(url);
			//����Parser������ַ�����,һ������ҳ���ַ����뱣��һ��
			parser.setEncoding("UTF-8");
			//
			// ���� <frame >��ǩ�� filter��������ȡ frame ��ǩ��� src ��������ʾ������
			//
			@SuppressWarnings("serial")
			//����NodeFilterʵ�� 
			NodeFilter frameFilter = new NodeFilter() {
				public boolean accept(Node node) {
					if (node.getText().startsWith("frame src=")) {						
						return true;
					} else {
						return false;
					}
				}
			};
			// OrFilterɸѡ������ <a> ��ǩ�� <frame> ��ǩ��OrFilter:��������֮һ���ɱ�ѡ�У�
			// NodeClassFilter�� ���ñ�ǩ�����ˣ�LinkTag.class => <a>��ǩ��
			OrFilter linkFilter = new OrFilter(new NodeClassFilter(
					LinkTag.class), frameFilter);
			// �õ����о������˵ı�ǩ
			NodeList list = parser.extractAllNodesThatMatch(linkFilter);
			for (int i = 0; i < list.size(); i++) {
				Node tag = list.elementAt(i);
				if (tag instanceof LinkTag) { // <a>��ǩ
					LinkTag link = (LinkTag) tag;
					String linkUrl = link.getLink();// url
					//System.out.println(url+"000000000000000000000000"+link.getLink());

					//filter.accept(linkUrl):�һ�ȥ���ֳ�����������linkUrl��http:��ͷ���ҷ�ͼƬ
					//���� && ���ų��˸��ַǷ���ʽ
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
				else {// <frame> ��ǩ		
					// ��ȡ frame �� src ���Ե������� <frame src="test.html"/>
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
	 * ���˲���ȡ��վ������
	 * @return 
	 * @param ��ַurl,������filter
	 * @throws ParserException
	 */
	public static Set<String> extracLinks_gb(String url, LinkFilter filter) {

		Set<String> links = new HashSet<String>();
		try {
			// �����������ͨ��ָ��URLConnection���󴴽�Parser���� 
			Parser parser = new Parser(url);
			//����Parser������ַ�����,һ������ҳ���ַ����뱣��һ��
			parser.setEncoding("gb2312");
			//
			// ���� <frame >��ǩ�� filter��������ȡ frame ��ǩ��� src ��������ʾ������
			//
			@SuppressWarnings("serial")
			//����NodeFilterʵ�� 
			NodeFilter frameFilter = new NodeFilter() {
				public boolean accept(Node node) {
					if (node.getText().startsWith("frame src=")) {						
						return true;
					} else {
						return false;
					}
				}
			};
			// OrFilterɸѡ������ <a> ��ǩ�� <frame> ��ǩ��OrFilter:��������֮һ���ɱ�ѡ�У�
			// NodeClassFilter�� ���ñ�ǩ�����ˣ�LinkTag.class => <a>��ǩ��
			OrFilter linkFilter = new OrFilter(new NodeClassFilter(
					LinkTag.class), frameFilter);
			// �õ����о������˵ı�ǩ
			NodeList list = parser.extractAllNodesThatMatch(linkFilter);
			for (int i = 0; i < list.size(); i++) {
				Node tag = list.elementAt(i);
				if (tag instanceof LinkTag) { // <a>��ǩ
					LinkTag link = (LinkTag) tag;
					String linkUrl = link.getLink();// url
					//filter.accept(linkUrl):�һ�ȥ���ֳ�����������linkUrl��http:��ͷ���ҷ�ͼƬ
					//���� && ���ų��˸��ַǷ���ʽ
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
				else {// <frame> ��ǩ		
					// ��ȡ frame �� src ���Ե������� <frame src="test.html"/>
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
//	 * ��ȡ��վ�����ӵ��޸�ʱ��
//	 * @return 
//	 * @param ��ַurl
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
//			//System.out.println("��ȡ����޸�ʱ�䷽��1��"+format.format(new Date(urs.getHeaderField("Last-modified")))); 
//			System.out.println("��ȡ����޸�ʱ�䷽��2��"+format.format(new Date(urs.getLastModified())));
//			HtmlPage page = new HtmlPage(parser2);
//			try {
//				// HtmlPage extends visitor,Apply the given visitor to the current
//				// page.
//				parser2.visitAllNodesWith(page);
//			} catch (ParserException e1) {
//				e1 = null;
//			}
//			// ���еĽڵ�
//			NodeList nodelist = page.getBody();
//			// ����һ���ڵ�filter���ڹ��˽ڵ�
//			NodeFilter filter = new TagNameFilter("A");
//			// �õ����й��˺���Ҫ�Ľڵ�
//			nodelist = nodelist.extractAllNodesThatMatch(filter, true);
//			for (int i = 0; i < nodelist.size(); i++) {
//				LinkTag link = (LinkTag) nodelist.elementAt(i);
//				// ���ӵ�ַ
//				System.out.println(link.getLink());
//				String llink=link.getLink();
//				String ltext=link.getStringText();
//				if(llink!=null&&ltext!=null&&LinkQueue.getTagtext().containsKey(llink)==false)
//				{
//					LinkQueue.getTagtext().put(llink,ltext);
//				}
//				//System.out.println(link.getAttribute("href") );//ȥ������Ϊ�յģ�����http��ͷ��
//				// ��������
//				//if(link.getAttribute("href")!=null &&link.getAttribute("href").indexOf("Keyword")>0)
//					//System.out.println(link.getStringText() + "\n");//ȥ������<img>
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
