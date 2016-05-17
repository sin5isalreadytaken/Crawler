import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;

/**
* description: 通用型爬取、聚集型爬取、综合型爬取、问答页爬取功能的入口
* note: 海量数据爬取速度
* modificationDate: 2014-12-19
*/ 

public class MyCrawler {
	public static int count=0;//爬取线程数
	public static int ST = 0;//stackoverflow
	public static int CN = 0;//q.cnblogs
	public static int DW = 0;//dwen
	public static int BZ = 0;//zhidao.baidu
	public static int WW = 0;//wenwen
	public static CraUi Cr;//UI界面
	public static int number=0;//已经爬到的页面数
	public static int id;//当前网页id
	public static String keyword = "";//关键字
	static ArrayList<String> filterStrings = new ArrayList<String>();
	private static String showfilterStrings = "";
	public static String getshowfilter()
	{
			showfilterStrings = "";
			for(String tmp :filterStrings){
				showfilterStrings = showfilterStrings +tmp +"\n";
			}
			return showfilterStrings;
	}
	public static void primaryfilter(){
		filterStrings.add("http:");
		filterStrings.add("https:");
	}
	public static void addfilter(String s){
		for(String tmp:filterStrings){
			if(tmp.equals(s)){
				JOptionPane.showMessageDialog(null, s+"过滤器规则已存在");
				return;
			}
		}
		filterStrings.add(s);
		JOptionPane.showMessageDialog(null,"成功添加过滤器规则！");
		getshowfilter();
		
	}
	public static void deletefilter(String s){
		for(String tmp:filterStrings){
			if(tmp.equals(s)){
				filterStrings.remove(tmp);
				JOptionPane.showMessageDialog(null,"成功删除过滤器规则！");
				getshowfilter();
				return ;
			}
		}
		JOptionPane.showMessageDialog(null,s+"不是已存在的过滤器规则！");
	}

	
	public static int visited = 0;//已经访问
	public static int succeed = 0;//爬取成功
	public static int failed = 0;//爬取失败
	public static int unpassed = 0;//过滤个数
	public static long time;//已经进行时间
	public static long startTime;//爬取开始时间
	//public static HistogramJPanel state = new HistogramJPanel();
	public static int max = 0;//最大爬取数
	public Lock lock=new Lock();
	public static ExecutorService pool = Executors.newFixedThreadPool(50);
	
	/**
	 * 使用种子初始化 URL队列
	 * @return
	 * @param seeds(String[])
	 * @throws 
	 */
	private void initCrawlerWithSeeds(String[] seeds)// edit by hxy
	{
		startTime = System.currentTimeMillis();
		for(int i=0;i<seeds.length;i++) {
			RankUrl url = new RankUrl(seeds[i],1);
			LinkQueue.addUnvisitedUrl(url, null);
			LinkQueue.addSeedUrl(url);
		}
			
		Keyword.keyword = keyword;
	}	
	/**
	 * 开始抓取stackoverflow页面过程
	 * @return
	 * @param seeds
	 * @throws InterruptedException 
	 */
	public void STCrawling(String[] STseeds)
	{
		//initCrawlerWithSeeds(STseeds);
		int i1 = 0,i2 = 0,i3 = 0,i4 = 0,i5 = 0;
		try {
			File file = new File(System.getProperty("user.dir")+"\\Point.txt");
			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);
			String line = br.readLine();
			int num = 0;
			while (line != null)
			{
				System.out.println(line);
				num++;
				if (num == 1)	i1 = Integer.valueOf(line);
				else if (num == 2)	i2 = Integer.valueOf(line);
				else if (num == 3)	i3 = Integer.valueOf(line);
				else if (num == 4)	i4 = Integer.valueOf(line);
				else if (num == 5)	i5 = Integer.valueOf(line);
				line = br.readLine();
			}
			br.close();
			FileWriter writer = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(writer);
			number=0;
			max=Cr.total;
			startTime = System.currentTimeMillis();
			//ConnectServer.dbConn();
			int first = 0;
			while(succeed<max)
			{
				//time = System.currentTimeMillis()-start;
				if (Cr.startBut.getText().equals("Continue"))	continue;
				if (ST == 0)
				{
					try {
						if (first != 0)	Thread.sleep(30000);
						first = 1;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (i1 < STseeds.length)
					{
						ST = 1;
						System.out.println(STseeds[i1++]);
						RankUrl visitUrl= new RankUrl(STseeds[i1++], 1);
						DownloadQuiz downLoader=new DownloadQuiz(visitUrl,visitUrl.UrlKind);
						//downLoader.start();
						pool.execute(downLoader);
					}	
				}
			}
			//System.out.println(String.valueOf(i1));
			bw.write(String.valueOf(i1)+"\r\n");
			bw.append(String.valueOf(i2)+"\r\n");
			bw.append(String.valueOf(i3)+"\r\n");
			bw.append(String.valueOf(i4)+"\r\n");
			bw.append(String.valueOf(i5)+"\r\n");
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 开始抓取q.cnblogs页面过程
	 * @return
	 * @param seeds
	 * @throws InterruptedException 
	 */
	public void CNCrawling(String[] CNseeds)
	{
		//initCrawlerWithSeeds(STseeds);
		File file = new File("Point.txt");
		int i1 = 0,i2 = 0,i3 = 0,i4 = 0,i5 = 0;
		try {
			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);
			String line = br.readLine();
			int num = 0;
			while (line != null)
			{
				num++;
				if (num == 1)	i1 = Integer.valueOf(line);
				else if (num == 2)	i2 = Integer.valueOf(line);
				else if (num == 3)	i3 = Integer.valueOf(line);
				else if (num == 4)	i4 = Integer.valueOf(line);
				else if (num == 5)	i5 = Integer.valueOf(line);
				line = br.readLine();
			}
			br.close();
			FileWriter writer = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(writer);
			number=0;
			max=Cr.total;
			startTime = System.currentTimeMillis();
			//ConnectServer.dbConn();
			while(succeed<max)
			{
				//time = System.currentTimeMillis()-start;
				if (Cr.startBut.getText().equals("Continue"))	continue;
				if (CN == 0)
				{
					if (i2 < CNseeds.length)
					{
						CN = 1;
						RankUrl visitUrl= new RankUrl(CNseeds[i2++], 2);
						DownloadQuiz downLoader=new DownloadQuiz(visitUrl,visitUrl.UrlKind);
						//downLoader.start();
						pool.execute(downLoader);
					}	
				}
			}
			bw.write(String.valueOf(i1)+"\r\n");
			bw.append(String.valueOf(i2)+"\r\n");
			bw.append(String.valueOf(i3)+"\r\n");
			bw.append(String.valueOf(i4)+"\r\n");
			bw.append(String.valueOf(i5)+"\r\n");
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 开始抓取dwen页面过程
	 * @return
	 * @param seeds
	 * @throws InterruptedException 
	 */
	public void DWCrawling(String[] DWseeds)
	{
		//initCrawlerWithSeeds(STseeds);
		File file = new File("Point.txt");
		int i1 = 0,i2 = 0,i3 = 0,i4 = 0,i5 = 0;
		try {
			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);
			String line = br.readLine();
			int num = 0;
			while (line != null)
			{
				num++;
				if (num == 1)	i1 = Integer.valueOf(line);
				else if (num == 2)	i2 = Integer.valueOf(line);
				else if (num == 3)	i3 = Integer.valueOf(line);
				else if (num == 4)	i4 = Integer.valueOf(line);
				else if (num == 5)	i5 = Integer.valueOf(line);
				line = br.readLine();
			}
			br.close();
			FileWriter writer = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(writer);
			number=0;
			max=Cr.total;
			startTime = System.currentTimeMillis();
			//ConnectServer.dbConn();
			while(succeed<max)
			{
				//time = System.currentTimeMillis()-start;
				if (Cr.startBut.getText().equals("Continue"))	continue;
				if (DW == 0)
				{
					if (i3 < DWseeds.length)
					{
						DW = 1;
						RankUrl visitUrl= new RankUrl(DWseeds[i3++], 3);
						DownloadQuiz downLoader=new DownloadQuiz(visitUrl,visitUrl.UrlKind);
						//downLoader.start();
						pool.execute(downLoader);
					}	
				}
			}
			bw.write(String.valueOf(i1)+"\r\n");
			bw.append(String.valueOf(i2)+"\r\n");
			bw.append(String.valueOf(i3)+"\r\n");
			bw.append(String.valueOf(i4)+"\r\n");
			bw.append(String.valueOf(i5)+"\r\n");
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 开始抓取zhidao.baidu页面过程
	 * @return
	 * @param seeds
	 * @throws InterruptedException 
	 */
	public void BZCrawling()
	{
		//initCrawlerWithSeeds(STseeds);
		File file = new File("Point.txt");
		int i1 = 0,i2 = 0,i3 = 0,i4 = 0,i5 = 0;
		try {
			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);
			String line = br.readLine();
			int num = 0;
			while (line != null)
			{
				num++;
				if (num == 1)	i1 = Integer.valueOf(line);
				else if (num == 2)	i2 = Integer.valueOf(line);
				else if (num == 3)	i3 = Integer.valueOf(line);
				else if (num == 4)	i4 = Integer.valueOf(line);
				else if (num == 5)	i5 = Integer.valueOf(line);
				line = br.readLine();
			}
			br.close();
			FileWriter writer = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(writer);
			number=0;
			max=Cr.total;
			startTime = System.currentTimeMillis();
			//ConnectServer.dbConn();
			while(succeed<max)
			{
				//time = System.currentTimeMillis()-start;
				if (Cr.startBut.getText().equals("Continue"))	continue;
				if (BZ == 0)
				{
					BZ = 1;
					String BZseed = "http://zhidao.baidu.com/question/"+String.valueOf(i4++)+"/";
					RankUrl visitUrl= new RankUrl(BZseed, 4);
					DownloadQuiz downLoader=new DownloadQuiz(visitUrl,visitUrl.UrlKind);
					//downLoader.start();
					pool.execute(downLoader);
				}
			}
			bw.write(String.valueOf(i1)+"\r\n");
			bw.append(String.valueOf(i2)+"\r\n");
			bw.append(String.valueOf(i3)+"\r\n");
			bw.append(String.valueOf(i4)+"\r\n");
			bw.append(String.valueOf(i5)+"\r\n");
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 开始抓取wenwen页面过程
	 * @return
	 * @param seeds
	 * @throws InterruptedException 
	 */
	public void WWCrawling(String[] WWseeds)
	{
		//initCrawlerWithSeeds(STseeds);
		File file = new File("Point.txt");
		int i1 = 0,i2 = 0,i3 = 0,i4 = 0,i5 = 0;
		try {
			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);
			String line = br.readLine();
			int num = 0;
			while (line != null)
			{
				num++;
				if (num == 1)	i1 = Integer.valueOf(line);
				else if (num == 2)	i2 = Integer.valueOf(line);
				else if (num == 3)	i3 = Integer.valueOf(line);
				else if (num == 4)	i4 = Integer.valueOf(line);
				else if (num == 5)	i5 = Integer.valueOf(line);
				line = br.readLine();
			}
			br.close();
			FileWriter writer = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(writer);
			number=0;
			max=Cr.total;
			startTime = System.currentTimeMillis();
			//ConnectServer.dbConn();
			while(succeed<max)
			{
				//time = System.currentTimeMillis()-start;
				if (Cr.startBut.getText().equals("Continue"))	continue;
				if (WW == 0)
				{
					if (i5 < WWseeds.length)
					{
						WW = 1;
						RankUrl visitUrl= new RankUrl(WWseeds[i5++], 5);
						DownloadQuiz downLoader=new DownloadQuiz(visitUrl,visitUrl.UrlKind);
						//downLoader.start();
						pool.execute(downLoader);
					}
				}
			}
			bw.write(String.valueOf(i1)+"\r\n");
			bw.append(String.valueOf(i2)+"\r\n");
			bw.append(String.valueOf(i3)+"\r\n");
			bw.append(String.valueOf(i4)+"\r\n");
			bw.append(String.valueOf(i5)+"\r\n");
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//ConnectServer.dbClose();
	}
	/**
	 * 开始抓取问答页面过程
	 * @return
	 * @param seeds
	 * @throws InterruptedException 
	 */
	public void QuizCrawling(String[] STseeds, String[] CNseeds, String[] DWseeds, String[] WWseeds)
	{
		//initCrawlerWithSeeds(STseeds);
		File file = new File("Point.txt");
		int i1 = 0,i2 = 0,i3 = 0,i4 = 0,i5 = 0;
		try {
			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);
			String line = br.readLine();
			int num = 0;
			while (line != null)
			{
				num++;
				if (num == 1)	i1 = Integer.valueOf(line);
				else if (num == 2)	i2 = Integer.valueOf(line);
				else if (num == 3)	i3 = Integer.valueOf(line);
				else if (num == 4)	i4 = Integer.valueOf(line);
				else if (num == 5)	i5 = Integer.valueOf(line);
				line = br.readLine();
			}
			br.close();
			FileWriter writer = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(writer);
			number=0;
			max=Cr.total;
			startTime = System.currentTimeMillis();
			//ConnectServer.dbConn();\
			int first = 0;
			while(succeed<max)
			{
				//time = System.currentTimeMillis()-start;
				if (Cr.startBut.getText().equals("Continue"))	continue;
				if (ST == 0)
				{
					try {
						if (first != 0)	Thread.sleep(30000);
						first = 1;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (i1 < STseeds.length)
					{
						ST = 1;
						RankUrl visitUrl= new RankUrl(STseeds[i1++], 1);
						DownloadQuiz downLoader=new DownloadQuiz(visitUrl,visitUrl.UrlKind);
						//downLoader.start();
						pool.execute(downLoader);
					}	
				}
				if (CN == 0)
				{
					if (i2 < CNseeds.length)
					{
						CN = 1;
						RankUrl visitUrl= new RankUrl(CNseeds[i2++], 2);
						DownloadQuiz downLoader=new DownloadQuiz(visitUrl,visitUrl.UrlKind);
						//downLoader.start();
						pool.execute(downLoader);
					}	
				}
				if (DW == 0)
				{
					if (i3 < DWseeds.length)
					{
						DW = 1;
						RankUrl visitUrl= new RankUrl(DWseeds[i3++], 3);
						DownloadQuiz downLoader=new DownloadQuiz(visitUrl,visitUrl.UrlKind);
						//downLoader.start();
						pool.execute(downLoader);
					}	
				}
				if (BZ == 0)
				{
					BZ = 1;
					String BZseed = "http://zhidao.baidu.com/question/"+String.valueOf(i4++)+"/";
					RankUrl visitUrl= new RankUrl(BZseed, 4);
					DownloadQuiz downLoader=new DownloadQuiz(visitUrl,visitUrl.UrlKind);
					//downLoader.start();
					pool.execute(downLoader);
				}
				if (WW == 0)
				{
					if (i5 < WWseeds.length)
					{
						WW = 1;
						RankUrl visitUrl= new RankUrl(WWseeds[i5++], 5);
						DownloadQuiz downLoader=new DownloadQuiz(visitUrl,visitUrl.UrlKind);
						//downLoader.start();
						pool.execute(downLoader);
					}	
				}
			}
			bw.write(String.valueOf(i1)+"\r\n");
			bw.append(String.valueOf(i2)+"\r\n");
			bw.append(String.valueOf(i3)+"\r\n");
			bw.append(String.valueOf(i4)+"\r\n");
			bw.append(String.valueOf(i5)+"\r\n");
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//ConnectServer.dbClose();
	}
	/**
	 * 开始抓取过程
	 * @return
	 * @param seeds
	 * @throws  
	 */
	public void crawling(String[] seeds)
	{   //定义过滤器，提取以http://www.lietu.com开头的链接
		//state.pack();//以合适的大小显示
        //state.setVisible(true);
		LinkFilter filter = new LinkFilter(){
		
			public boolean accept(String url) {
				int flag=0;
				for(int i = 0; i < filterStrings.size(); i++){
					if(url.startsWith(filterStrings.get(i))){
						flag=1;
						break;
					}					
				}				
				//	判断是否有误？为啥不是判断尾端？ 存疑
				if(url.indexOf("jpg")!=-1||url.indexOf("gif")!=-1||url.indexOf("png")!=-1)
					flag=0;
				if(flag==0)
					return false;
				else
					return true;			
			}
		};
		//初始化 URL 队列
		initCrawlerWithSeeds(seeds);
		//循环条件：待抓取的链接不空且抓取的网页不多于1000
		number=0;
		//总共需要爬取的网页数
		max=Cr.total;
		while(succeed<max)
		{
			//	break而不是continue，否则搜不够的话无法停止
			//	count>3原因？存疑
			//	队列中没有未访问的URL则停止循环
			time = System.currentTimeMillis();
			if (Cr.startBut.getText().equals("Continue"))	continue;
			if(LinkQueue.unVisitedUrlsEmpty()||count>10)
			{
				//System.out.println("d");
				continue;
			}
			System.out.println(LinkQueue.getVisitedUrlNum());
			RankUrl visitUrl=(RankUrl)LinkQueue.unVisitedUrlDeQueue();
			DownLoadFile downLoader=new DownLoadFile(visitUrl,filter,lock);
			//	下载网页
			//downLoader.start();
			pool.execute(downLoader);	
			//	爬到的网页数++
			number++;
			System.out.println("number:"+number);
		}
	}
	/**
	 * main函数入口
	 * @return
	 * @param args
	 * @throws InterruptedException,ClassNotFoundException,InstantiationException,IllegalAccessException,UnsupportedLookAndFeelException,
	 */
	public static void main(String[]args)
	{
		//final Logo lg =new Logo(System.getProperty("user.dir").replace('\\', '/') + "\\image\\icon1.jpg");
		final ConUi conui = new ConUi();
		//lg.run();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//System.out.println(System.getProperty("user.dir"));
		try {  
	           javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");  
	       } catch (ClassNotFoundException e) {  
	           e.printStackTrace();  
	       } catch (InstantiationException e) {  
	           e.printStackTrace();  
	       } catch (IllegalAccessException e) {  
	           e.printStackTrace();  
	       } catch (javax.swing.UnsupportedLookAndFeelException e) {
	           e.printStackTrace();  
	       }
	       javax.swing.SwingUtilities.invokeLater(new Runnable() {  
	           public void run() {  
	        	   	//	打开UI界面的线程
	                Cr = new CraUi();     
	                Cr.setVisible(false);
	                conui.settojump(Cr);
	                Thread t1=new Thread(Cr);
	                t1.start(); 
	           }  
	       }); 
		id=ConnectServer.idNumber()+1;		
		//pool.shutdown();
	}
	/**
	 * 设置关键字方法
	 * @return
	 * @param keyword
	 * @throws 
	 */
	public void setKeyword(String _keyword) {
		// TODO Auto-generated method stub
		keyword = new String(_keyword);
	}
}


