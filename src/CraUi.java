import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Menu;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.xml.soap.Text;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/**
* description: UI界面设计
* note: 合理、人性化
* modificationDate: 2014-12-19
*/ 
@SuppressWarnings("serial")
public class CraUi extends JFrame implements ActionListener, Runnable, ItemListener
{
//	private int counter=0;
	public static long startTime;
	public static long estimatedTime;
	public boolean isFileInput=false;
	private String txt = new String("");  
	
	FileInputStream fis = null;
	InputStreamReader isr = null;
	BufferedReader br = null; //用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
	
	boolean isStart;
	public int total;
	public final String howToUse = "本爬虫软件是由newbe软件工程队在远航1617软件工程队的爬虫软件基础上进行改写开发而成的。\n" +
									"使用方法：\n" + 
									"1.输入网址：在URL Seed中输入爬取的网址，可以输入多个网址（缺省为百度搜索）；或者选择yes按钮后用Select File按钮选择包含多个URL地址的txt文件\n" +
									"2.输入关键词：在Keyword中输入即可（缺省为通常爬取）\n" + 
									"3.输入爬取数：在How Many Pages中输入即可（不可缺省）\n" + 
									"4.选择页面保存地址：通过Save Path选项后的select即可，还可以通过open按钮打开目录\n" + 
									"5.爬取：点击Start按钮；爬取开始后可以暂停Pause和继续Continue\n" + 
									"6.分析：点击Analyze按钮\n" + 
									"7.爬取问答页：点击Quiz按钮\n" +
									"8.爬取pdf：选择txt，点击Pdf按钮\n" +
									"9.爬取doc：选择txt，点击Doc按钮\n" +
									"10.爬取ppt：选择txt，点击PPt按钮\n" +
									"PS：\n" +
									"1.运行本爬虫软件需要连接指定服务器，服务器不是全天开启的，不连接服务器本软件无法正确运行。\n" +
									"2.本爬虫软件具有关键字搜索功能，并且可以展示爬取过程。\n" +
									"3.Keyword关键字搜索，可以通过输入关键字对爬取的网页进行过滤处理，只爬取带有关键字的网页（并进行排序，得到根据热度排列的相对最优结果）;\n" +
									"4.由于爬取速度有限，建议爬取网页的数量不要输入太多。\n" +
									"5.当爬取成功网页数达到How Many Pages中输入的网页数量时，爬取过程将会停止。\n" +
									"6.analyze中的饼状图是对服务器中存有的所有网页进行分类分析；条形图是本次爬取中根据热度排列的结果。";//这里写readme
	JButton video = new JButton("Video");
	JLabel typetoadd = new JLabel("关键词:");
	JButton addtype = new JButton("添加");
	JButton subtype = new JButton("删除");
	JTextField gettype = new JTextField();
	JTextArea show = new JTextArea();
	JTextArea show2 = new JTextArea();
	JLabel typetoshow = new JLabel("关键词");
	JLabel filtertoshow = new JLabel("规则");
	JScrollPane typejsp ;
	JScrollPane filterjsp;
	JLabel filtertoadd = new JLabel("过滤器规则:");
	JButton addfilter = new JButton("添加");
	JButton subfilter = new JButton("删除");
	JTextField getfilter = new JTextField();
	
	JLabel state=new JLabel("");
	JLabel timeCost = new JLabel("Time Cost:"+ 0 +"s");
	JLabel urlVis=new JLabel("URL Visited :" + 0);
	JLabel pageNum=new JLabel("How Many Pages :");
	JLabel keyword=new JLabel("Keyword :");
	JLabel urlSeed=new JLabel("URL Seed :");
	JLabel visitedURLs=new JLabel("Visited URLs:");
	JLabel urlSeedFile=new JLabel("Or You Can Select The URL File:");
	JLabel dir = new JLabel("Save Path:");
	JTextField  getkeyword=new JTextField ();						//单行文本区域
	JTextArea  getUrlSeed=new JTextArea ();							//多行文本区域
	JScrollPane ssUrlSeed=new JScrollPane(getUrlSeed);				//滚动窗格
	JButton txtBut = new JButton("Select Txt");					//浏览按钮
	JTextField num=new JTextField(10);								//单行文本区域
	JTextField selecteddir=new JTextField(DownLoadFile.oripath);	//add by UDvoid
	Choice inputWay;												//构建下拉菜单列表
	JFileChooser pathFileIO=new JFileChooser();						//文件选择器
	JTextArea URLsVisited=new JTextArea();							//多行文本区域
	JScrollPane URLs=new JScrollPane(URLsVisited);					//滚动窗格
	JButton startBut=new JButton("Start");							//开始按钮
	//JButton cancelBut=new JButton("Close");							//取消按钮
	JButton pdfBut=new JButton("Pdf");
	JButton docBut=new JButton("Doc");
	JButton pptBut=new JButton("PPt");
	JButton analyzeBut=new JButton("Analyze");						//分析按钮
	JButton selectBut=new JButton("Select");						//浏览按钮
	JButton openBut=new JButton("Open");							//打开按钮
	JButton howBut=new JButton("How To Use");						//功能说明
	JButton qpz = new JButton("you can't see");
	JButton quiz=new JButton("Quiz Page");
	JPopupMenu menu=new JPopupMenu(); 
	JToggleButton selectquiz=new JToggleButton("▼");
	JMenuItem menuitem[] = new JMenuItem[6];
	JPanel backGround=new JPanel();									//布局背景
	JProgressBar progress=new JProgressBar();						//进度条
	Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
	static DefaultCategoryDataset data = new DefaultCategoryDataset(); 
	static JFreeChart chart;
	JPanel panel;
	ResUi resui;
	/**
	 * 根据给定的数据创建图表
	 * @return
	 * @param 数据集dataset(CategoryDataset)
	 * @throws 
	 */
    public JFreeChart createChart(CategoryDataset dataset) //用数据集创建一个图表
    {
        chart=ChartFactory.createBarChart("", 
        		"网页状态",
        		"网页数量", 
        		data, 
        		PlotOrientation.VERTICAL, 
        		true, 
        		true, 
        		false); //创建一个JFreeChart
        chart.setTitle(new TextTitle("网页爬取状态",new Font("宋体",Font.BOLD+Font.ITALIC,20)));//可以重新设置标题，替换“hi”标题,BOLD加粗ITALIC斜体
        //CategoryPlot plot=(CategoryPlot)chart.getPlot();//获得图标中间部分，即plot
        CategoryPlot plot = chart.getCategoryPlot();//设置图的高级属性 
        BarRenderer3D renderer = new BarRenderer3D();//3D属性修改 
        plot.setRenderer(renderer);//将修改后的属性值保存到图中 
        CategoryAxis categoryAxis=plot.getDomainAxis();//获得横坐标,DomainAxis横坐标，RangeAxis纵坐标
        
        categoryAxis.setLabelFont(new Font("微软雅黑",Font.BOLD,15));//设置横坐标字体//网页状态
        categoryAxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 12));//已经访问数等等
        
        NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
        numberAxis.setLabelFont(new Font("微软雅黑",Font.BOLD,15));//网页数量
        numberAxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 12));//0.0000
        numberAxis.setLowerBound(0);
        chart.getLegend().setItemFont(new Font("宋体",Font.PLAIN,12)); //数据
        
        BarRenderer barRenderer = (BarRenderer) plot.getRenderer();
        barRenderer.setIncludeBaseInRange(true);
        barRenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        barRenderer.setBaseItemLabelsVisible(true);
        barRenderer.setMaximumBarWidth(0.05);
        //System.out.printf("__________________________%d %d\n",this.getSize().height,this.getSize().width);
        return chart;
    }
    /**
	 * 创建供图表显示的面板
	 * @return 面板chart(JPanel)
	 * @param 
	 * @throws 
	 */
    public JPanel createPanel()
    {
        chart =createChart(data);
        return new ChartPanel(chart); //将chart对象放入Panel面板中去，ChartPanel类已继承Jpanel
    }
    /**
	 * 刷新面板
	 * @return 
	 * @param 已经访问页面数visited,爬取成功页面数succeed,爬取失败页面数failed,过滤页面数passed(int)
	 * @throws 
	 */
    public static void updatePanel(int visited,int succeed,int failed,int passed)
    {
    	//data = new DefaultCategoryDataset(); 
    	data.setValue(visited,"数据","已经访问数");
    	data.setValue(succeed,"数据","爬取成功数");
        data.setValue(failed,"数据","爬取失败数");
        data.setValue(passed,"数据","过滤数");
        CategoryPlot plot = chart.getCategoryPlot();//设置图的高级属性 
        NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
        numberAxis.setUpperBound(visited);
        //chart =createChart(data);
        //this.setSize(500,650);
        //this.setVisible(true);
        //this.setContentPane(new ChartPanel(chart));
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.printf("更新成功！");
    }
    /**
   	 * 构造函数,设计UI布局
   	 */
	public CraUi()
	{
		super("正在抓取爬虫");
		// Icon
		String test = System.getProperty("user.dir").replace('\\', '/');
		BufferedImage src = null;
		try{
			src = ImageIO.read(new File(test + "\\image\\icon1.jpg"));
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 读入源图像
		this.setIconImage(src);	
		ImageIcon bg = new ImageIcon(test + "\\image\\logo4.png");
		JLabel back = new JLabel();
		back.setBounds(0,0,516,278);
		this.getLayeredPane().add(back,new Integer(Integer.MIN_VALUE));
		JPanel jp = (JPanel)this.getContentPane();
		jp.setOpaque(false);
		getUrlSeed.setOpaque(true);
		ssUrlSeed.setOpaque(true);
		ssUrlSeed.getViewport().setOpaque(true);
		URLsVisited.setOpaque(false);
		URLs.setOpaque(true);
		URLs.getViewport().setOpaque(false);
		getkeyword.setOpaque(true);
		num.setOpaque(true);
		//inputWay?
		
		typetoadd.setBounds(20, 500, 80, 30);
		this.add(typetoadd);
		gettype.setBounds(90, 500, 220, 30);
		this.add(gettype);
		addtype.setBounds(320, 500, 70, 30);
		this.add(addtype);
		addtype.addActionListener(this);
		subtype.setBounds(400, 500, 70, 30);
		this.add(subtype);
		subtype.addActionListener(this);
		MyT.primaryType();
		show.setText(MyT.getshowtype());
		show.setEditable(false);
		typejsp = new JScrollPane(show);
		typejsp.setBounds(520,540,100,150);
		this.add(typejsp);
		MyCrawler.primaryfilter();
		show2.setText(MyCrawler.getshowfilter());
		show2.setEditable(false);
		filterjsp = new JScrollPane(show2);
		filterjsp.setBounds(630,540,100,150);	
		this.add(filterjsp);
		//p = new JScrollPane(show);
		//p.setBounds(720,500,20,100);
		//this.add(p);
		typetoshow.setBounds(550,500,75,30);
		this.add(typetoshow);
		filtertoshow.setBounds(670,500,75,30);
		this.add(filtertoshow);
		
		filtertoadd.setBounds(20,550,80,30);
		this.add(filtertoadd);
		getfilter.setBounds(90,550,220,30);
		this.add(getfilter);
		addfilter.setBounds(320,550,70,30);
		this.add(addfilter);
		addfilter.addActionListener(this);
		subfilter.setBounds(400,550,70,30);
		this.add(subfilter);
		subfilter.addActionListener(this);
		
		dir.setVisible(false);
		selecteddir.setVisible(false);
		selectBut.setVisible(false);
		openBut.setVisible(false);
		
		data.setValue(0,"数据","已经访问数");
    	data.setValue(0,"数据","爬取成功数");
        data.setValue(0,"数据","爬取失败数");
        data.setValue(0,"数据","过滤数");
        panel = createPanel();
        panel.setOpaque(false);
        panel.setLayout(null);
		backGround.setLayout(null);					//null布局		
		backGround.setOpaque(false);
		resui = new ResUi();
		
		String item[] = new String[6];
		item[0]="all";
		item[1]="stackoverflow";
		item[2]="zhidao.baidu";
		item[3]="wenwen";
		item[4]="q.cnblogs";
		item[5]="dwen";
				
		pathFileIO.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		/* 表格的位置
		 * 
		 */
		panel.setVisible(true);
		panel.setBounds(0, 0, 400, 500);//表格的位置
		//backGround.add(panel);
		resui.add(panel);
		//resui.setVisible(true);
		inputWay=new Choice();
		inputWay.addItemListener(this);
		//inputWay.setLocation(210,255);
		inputWay.setLocation(210,415);
		inputWay.add("No");							//增加No选项
		inputWay.add("Yes");
		backGround.add(inputWay);
		
		///URLSEED 标签位置
		//urlSeed.setBounds(20, 20, 90, 30);
		//urlseed的位置
		urlSeed.setBounds(155, 20, 90, 30);
		backGround.add(urlSeed);					//把urlseed添加到背景
		///urlseed输入框
		//ssUrlSeed.setBounds(100, 20, 360, 100);
		ssUrlSeed.setBounds(20, 70, 360, 100);
		backGround.add(ssUrlSeed);
		//how many page Label location
		//pageNum.setBounds(20, 160, 120, 30);
		pageNum.setBounds(155, 290, 120, 30);
		backGround.add(pageNum);
		//how many page input loaction
		//num.setBounds(130, 160, 220, 30);
		num.setBounds(20, 340, 360, 30);
		backGround.add(num);
		//num.setBackground(Color.RED);
		
		//how to use Button Location
		//howBut.setBounds(360, 160, 100, 30);
		qpz.setBounds(0, 10, 0, 0);
		backGround.add(qpz);
		
		howBut.setBounds(520, 20, 100, 30);
		howBut.addActionListener(this);
		backGround.add(howBut);
		
		//keyword Label Location
		//keyword.setBounds(20, 125, 90, 30);
		keyword.setBounds(155, 190, 90, 30);
		backGround.add(keyword);
		
		//key word input site
		//getkeyword.setBounds(100, 125, 360, 30);		
		getkeyword.setBounds(20, 240, 360, 30);
		backGround.add(getkeyword);	
		
		//or you can select the URL file Label site
		//urlSeedFile.setBounds(20, 250, 310, 30);
		urlSeedFile.setBounds(20, 410, 310, 30);
		backGround.add(urlSeedFile);
	
		/////那个select txt
		txtBut.addActionListener(this);
		txtBut.setBounds(270, 410, 110, 30);
		backGround.add(txtBut);
		txtBut.setVisible(false);
		//unknown
		selectBut.addActionListener(this);
		selectBut.setBounds(330, 245, 70, 30);
		backGround.add(selectBut);
		openBut.addActionListener(this);
		openBut.setBounds(400, 245, 60, 30);
		backGround.add(openBut);
		dir.setBounds(20, 245, 90, 30);
		backGround.add(dir);
		selecteddir.setBounds(95, 245, 230, 30);
		backGround.add(selecteddir);
		//从327到这里都是看不见的
		//visited URL site
		visitedURLs.setBounds(585, 190, 120, 30);
		backGround.add(visitedURLs);
		//visitedURL input site
		URLs.setBounds(400, 230, 440, 140);
		backGround.add(URLs);
		//url visited label site
		urlVis.setBounds(400, 460, 280, 30);
		backGround.add(urlVis);
		//time cost label site
		timeCost.setBounds(680, 460, 200, 30);
		backGround.add(timeCost);
		//进度条位置
		progress.setBounds(400, 410, 440, 30);			//设置进度条
		progress.setStringPainted(true);
		progress.setMinimum(0);
		progress.setValue(0);
		
		startBut.setBounds(405, 70, 90, 30);
		backGround.add(startBut);
		startBut.addActionListener(this);
		
		analyzeBut.addActionListener(this);
		analyzeBut.setBounds(525, 70, 90, 30);
		backGround.add(analyzeBut);
		
		quiz.addActionListener(this);
		quiz.setBounds(645, 70, 90, 30);
		backGround.add(quiz);
		
		pdfBut.addActionListener(this);
		pdfBut.setBounds(405, 120, 90, 30);
		backGround.add(pdfBut);
		
		video.setBounds(750,120,90,30);
		video.addActionListener(this);
		backGround.add(video);
		docBut.addActionListener(this);
		docBut.setBounds(525, 120, 90, 30);
		backGround.add(docBut);
		
		pptBut.addActionListener(this);
		pptBut.setBounds(645, 120, 90, 30);
		backGround.add(pptBut);
		
		for(int i = 0;i < 6;++i){
			menuitem[i]=new JMenuItem(item[i]);
			switch(i){
			case 0:
				menuitem[i].addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent e) {
		            	quiz.setText("all");
		            }	           	
		        });
				break;
			case 1:
				menuitem[i].addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent e) {
		            	quiz.setText("stackoverflow");
		            }	           	
		        });
				break;
			case 2:
				menuitem[i].addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent e) {
		            	quiz.setText("zhidao.baidu");
		            }	           	
		        });
				break;
			case 3:
				menuitem[i].addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent e) {
		            	quiz.setText("wenwen");
		            }	           	
		        });
				break;
			case 4:
				menuitem[i].addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent e) {
		            	quiz.setText("q.cnblogs");
		            }	           	
		        });
				break;
			case 5:
				menuitem[i].addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent e) {
		            	quiz.setText("dwen");
		            }	           	
		        });
				break;
			}
			menu.add(menuitem[i]);  
		}
		
		
	    selectquiz.addActionListener(new ActionListener(){  
            @Override  
            public void actionPerformed(ActionEvent arg0) {  
                if(selectquiz.isSelected()){  
                    menu.show(selectquiz, 0, selectquiz.getHeight());  
                }else{
                    menu.setVisible(false);  
                }  
                  
            }  
        });
	    //quiz page 旁边的下拉按钮
	    selectquiz.setFont(new Font("SansSerif",Font.PLAIN,7));
	    selectquiz.setMargin(getInsets());
	    selectquiz.setBounds(730, 70, 20, 30);
		backGround.add(selectquiz);
		
		state.setBounds(0, 650, 280, 30);
		backGround.add(state);
		
		
		//progress.setIndeterminate(true);
		backGround.add(progress);
		this.add(backGround);						//把backGround添加到錔frame
		this.setSize(860, 800);
		this.setLocation(0,0);
		this.setVisible(true);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
					System.exit(0);	
			}
		});
	}
	/**
       * 配置事件监听器动作
	 * @return 
	 * @param 动作e(ActionEvent)
	 * @throws IOException,UnsupportedEncodingException,FileNotFoundException,
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==startBut||e.getSource()== pdfBut || e.getSource()== docBut || e.getSource()== pptBut||e.getSource()==video)
		{
			if(e.getSource()== pdfBut)
				DownLoadFile.pdf=true;
			else if(e.getSource()== pptBut)
				DownLoadFile.ppt=true;
			else if(e.getSource()== docBut)
				DownLoadFile.doc=true;
			else if(e.getSource()==video)
				DownLoadFile.video=true;
			if (startBut.getText().equals("Start")){			
				if(num.getText().equals("")){
					JOptionPane.showMessageDialog(this, "Please enter a number！");
					return;
				}
				if(num.getText().equals("0")){
					JOptionPane.showMessageDialog(this, "number 0 is invalid ！");
					return;
				}
				total=Integer.parseInt(num.getText());
				resui.setVisible(true);
				String text=getUrlSeed.getText();
				String line = "";
				final String _keyword = getkeyword.getText();
				// 1000是否够用？存疑
				final String [] a=new String[1000];
				int i = 0;
				if(!isFileInput){
					BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(text.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
					try {
						while((line = br.readLine()) != null)
						{
							if(!line.equals(""))
								a[i++] = line;
							if(i >= 990)
								break;
						}
						br.close();
					}
					catch (IOException e1) {
						JOptionPane.showMessageDialog(this, "Error when reading！");
						return;
					}
				}else if(getTxt().equals("")){
					JOptionPane.showMessageDialog(this, "Please select a file first！");
					return;
				}else{
					try {
						fis = new FileInputStream(getTxt());// FileInputStream
						// 从文件系统中的某个文件中获取字节
						isr = new InputStreamReader(fis);// InputStreamReader 是字节流通向字符流的桥梁,
						br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
						while ((line = br.readLine()) != null) {
							if(!line.equals(""))
								a[i++]=line;//now
							if(i >= 990)
								break;
						}
					}
					catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(this, "Can't find the file！");
						return;
					}// FileInputStream
					catch (IOException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(this, "Error when reading！");
						return;
					}
					finally{
						try {
							br.close();
							isr.close();
							fis.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				
				if(i == 0){
					if((isFileInput && !getTxt().equals("")) || !isFileInput){
						if(_keyword.equals("")){
							JOptionPane.showMessageDialog(this, "No Url To crawl! ");
							return ;
						}else{
							try {
								a[0] = "http://www.baidu.com/s?wd="+URLEncoder.encode(_keyword, "utf-8")+"&ie=utf-8";
							} catch (UnsupportedEncodingException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}
				else if(i >= 990){
					JOptionPane.showMessageDialog(this, "Too Many Urls To crawl! ");
					return ;
				}
				progress.setMaximum(total);
				isStart=true;
				startBut.setText("Pause");
				new Thread(){
					public void run(){
						
						startTime = System.nanoTime();
						System.out.println("Look here!It's the time to start!");

						MyCrawler crawler = new MyCrawler();
						crawler.setKeyword(_keyword);
						crawler.crawling(a);
						System.out.println("finish");
						
						estimatedTime = System.nanoTime() - startTime;
						System.out.println("Total crawling time: " + Long.toString(estimatedTime/1000000) + " ms=================================");
					}
				}.start();
			}
			else if (startBut.getText().equals("Pause"))	
				startBut.setText("Continue");
			else if (startBut.getText().equals("Continue")) 
				startBut.setText("Pause");
			else if (startBut.getText().equals("Restart")){
				
				if(num.getText().equals("")){
					JOptionPane.showMessageDialog(this, "Please enter a number！");
					return;
				}
				resui.setVisible(true);				
				total=Integer.parseInt(num.getText());
				String text=getUrlSeed.getText();
				String line = "";
				final String _keyword = getkeyword.getText();
				// 1000是否够用？存疑
				final String [] a=new String[1000];
				int i = 0;
				if(!isFileInput){
					BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(text.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
					try {
						while((line = br.readLine()) != null)
						{
							if(!line.equals(""))
								a[i++] = line;
							if(i >= 990)
								break;
						}
						br.close();
					}
					catch (IOException e1) {
						JOptionPane.showMessageDialog(this, "Error when reading！");
						return;
					}
				}else if(getTxt().equals("")){
					JOptionPane.showMessageDialog(this, "Please select a file first！");
					return;
				}else{
					try {
						fis = new FileInputStream(getTxt());// FileInputStream
						// 从文件系统中的某个文件中获取字节
						isr = new InputStreamReader(fis);// InputStreamReader 是字节流通向字符流的桥梁,
						br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
						while ((line = br.readLine()) != null) {
							if(!line.equals(""))
								a[i++]=line;//now
							if(i >= 990)
								break;
						}
					}
					catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(this, "Can't find the file！");						
						return;
					}// FileInputStream
					catch (IOException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(this, "Error when reading！");
						return;
					}
					finally{
						try {
							br.close();
							isr.close();
							fis.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				
				if(i == 0){
					if((isFileInput && !getTxt().equals("")) || !isFileInput){
						if(_keyword.equals("")){
							JOptionPane.showMessageDialog(this, "No Url To crawl! ");
							return ;
						}else{
							try {
								a[0] = "http://www.baidu.com/s?wd="+URLEncoder.encode(_keyword, "utf-8")+"&ie=utf-8";
								
							} catch (UnsupportedEncodingException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}
				else if(i >= 990){
					JOptionPane.showMessageDialog(this, "Too Many Urls To crawl! ");
					return ;
				}
				progress.setMaximum(total);
				isStart=true;
				startBut.setText("Pause");
				new Thread(){
					public void run(){
						//System.out.println("Restart");
						startTime = System.nanoTime();
						System.out.println("Look here!It's the time to start!");
						MyCrawler crawler = new MyCrawler();
						MyCrawler.succeed = 0;
						MyCrawler.failed = 0;
						MyCrawler.visited = 0;
						MyCrawler.unpassed = 0;
						URLsVisited.setText("");
						urlVis.setText("URL Visited :  " + MyCrawler.succeed);
						progress.setValue( MyCrawler.succeed);
						System.out.println("MyCrawler.succeed = "+MyCrawler.succeed);
						crawler.setKeyword(_keyword);
						crawler.crawling(a);
						System.out.println("finish");
						
						estimatedTime = System.nanoTime() - startTime;
						System.out.println("Total crawling time: " + Long.toString(estimatedTime/1000000) + " ms=================================");
					}
				}.start();
			}
		}
		else if (e.getSource()== addtype){
			String types ,typein;
			types = gettype.getText();
			typein = types.replace(" ", "");
			if(typein.length()<1){
				JOptionPane.showMessageDialog(this, "请输入关键词");
			}
			else{
				MyT.addType(typein);
				show.setText("");
				show.setText(MyT.getshowtype());
				gettype.setText("");
			}
			
		}
		else if (e.getSource()== subtype){
			String types ,typein;
			types = gettype.getText();
			typein = types.replace(" ", "");
			if(typein.length()<1){
				JOptionPane.showMessageDialog(this, "请输入关键词");
			}
			else{
				MyT.deleteType(typein);
				show.setText("");
				show.setText(MyT.getshowtype());
				gettype.setText("");
			}
			//System.out.println("subbb");
			
		}
		else if(e.getSource()==addfilter){
			String f,fin;
			f = getfilter.getText();
			fin = f.replace(" ", "");
			if(fin.length()<1)
				JOptionPane.showMessageDialog(this,"请输入过滤器规则");
			else{
				MyCrawler.addfilter(fin);
				show2.setText("");
				show2.setText(MyCrawler.getshowfilter());
				getfilter.setText("");
			}
		}
		else if(e.getSource()==subfilter){
			String f ,fin;
			f = getfilter.getText();
			fin = f.replace(" ", "");
			if(fin.length()<1){
				JOptionPane.showMessageDialog(this, "请输入过滤器规则");
			}
			else{
				MyCrawler.deletefilter(fin);
				show2.setText("");
				show2.setText(MyCrawler.getshowfilter());
				getfilter.setText("");
			}
		}
		else if(e.getSource()==quiz)
		{
			if(quiz.getText().equals("stackoverflow")){
				total=Integer.parseInt(num.getText());
				final String _keyword = getkeyword.getText();
				//11000 -> 18000 changed by lyp
				final String [] a = new String[170000];
				for (int i = 0 ; i < 170000 ; i++)
					a[i] = "http://stackoverflow.com/questions?page="+String.valueOf(i+1)+"&sort=votes";
				// 1000是否够用？存疑
				progress.setMaximum(total);
				isStart=true;
				startBut.setText("Quiz Pause");
				new Thread(){
					public void run(){
						
						startTime = System.nanoTime();
						System.out.println("Look here!It's the time to start!");

						MyCrawler crawler = new MyCrawler();
						crawler.setKeyword(_keyword);
						crawler.STCrawling(a);
						System.out.println("finish");
						
						estimatedTime = System.nanoTime() - startTime;
						System.out.println("Total crawling time: " + Long.toString(estimatedTime/1000000) + " ms=================================");
					}
				}.start();
			}
			else if(quiz.getText().equals("zhidao.baidu")){
				total=Integer.parseInt(num.getText());
				final String _keyword = getkeyword.getText();
				// 1000是否够用？存疑
				progress.setMaximum(total);
				isStart=true;
				startBut.setText("Quiz Pause");
				new Thread(){
					public void run(){
						
						startTime = System.nanoTime();
						System.out.println("Look here!It's the time to start!");

						MyCrawler crawler = new MyCrawler();
						crawler.setKeyword(_keyword);
						crawler.BZCrawling();
						System.out.println("finish");
						
						estimatedTime = System.nanoTime() - startTime;
						System.out.println("Total crawling time: " + Long.toString(estimatedTime/1000000) + " ms=================================");
					}
				}.start();
			}
			else if(quiz.getText().equals("wenwen")){
				total=Integer.parseInt(num.getText());
				final String _keyword = getkeyword.getText();
				final String [] wenwen = new String[1000];
				for (int i = 0 ; i < 1000 ; i++)
					wenwen[i] = "http://wenwen.sogou.com/cate/?cid=0&tp=6&pg="+String.valueOf(i);
				
				// 1000是否够用？存疑
				progress.setMaximum(total);
				isStart=true;
				startBut.setText("Quiz Pause");
				new Thread(){
					public void run(){
						
						startTime = System.nanoTime();
						System.out.println("Look here!It's the time to start!");

						MyCrawler crawler = new MyCrawler();
						crawler.setKeyword(_keyword);
						crawler.WWCrawling(wenwen);
						System.out.println("finish");
						
						estimatedTime = System.nanoTime() - startTime;
						System.out.println("Total crawling time: " + Long.toString(estimatedTime/1000000) + " ms=================================");
					}
				}.start();
			}
			else if(quiz.getText().equals("q.cnblogs")){
				total=Integer.parseInt(num.getText());
				final String _keyword = getkeyword.getText();
				final String [] cnblog = new String[200];
				for (int i = 0 ; i < 200 ; i++)
					cnblog[i] = "http://q.cnblogs.com/list/solved?page=" + String.valueOf(i+1);
				// 1000是否够用？存疑
				progress.setMaximum(total);
				isStart=true;
				startBut.setText("Quiz Pause");
				new Thread(){
					public void run(){
						
						startTime = System.nanoTime();
						System.out.println("Look here!It's the time to start!");

						MyCrawler crawler = new MyCrawler();
						crawler.setKeyword(_keyword);
						crawler.CNCrawling(cnblog);
						System.out.println("finish");
						
						estimatedTime = System.nanoTime() - startTime;
						System.out.println("Total crawling time: " + Long.toString(estimatedTime/1000000) + " ms=================================");
					}
				}.start();
			}
			else if(quiz.getText().equals("dwen")){
				total=Integer.parseInt(num.getText());
				final String _keyword = getkeyword.getText();
				final String [] dewen = new String[1000];
				for (int i = 0 ; i < 1000 ; i++)
					dewen[i] = "http://www.dewen.io/questions?page=" + String.valueOf(i+1);
				progress.setMaximum(total);
				isStart=true;
				startBut.setText("Quiz Pause");
				new Thread(){
					public void run(){
						
						startTime = System.nanoTime();
						System.out.println("Look here!It's the time to start!");

						MyCrawler crawler = new MyCrawler();
						crawler.setKeyword(_keyword);
						crawler.WWCrawling(dewen);
						System.out.println("finish");
						
						estimatedTime = System.nanoTime() - startTime;
						System.out.println("Total crawling time: " + Long.toString(estimatedTime/1000000) + " ms=================================");
					}
				}.start();
			}
			//全部一起爬取
			else if(quiz.getText().equals("all") || quiz.getText().equals("Quiz Pages")){
				total=Integer.parseInt(num.getText());
				final String _keyword = getkeyword.getText();
				//11000 -> 18000 changed by lyp
				final String [] a = new String[170000];
				final String [] cnblog = new String[200];
				final String [] dewen = new String[1000];
				final String [] wenwen = new String[1000];
				for (int i = 0 ; i < 18000 ; i++)
					a[i] = "http://stackoverflow.com/questions?page="+String.valueOf(i+1)+"&sort=votes";
				for (int i = 0 ; i < 200 ; i++)
					cnblog[i] = "http://q.cnblogs.com/list/solved?page=" + String.valueOf(i+1);
				for (int i = 0 ; i < 1000 ; i++)
					dewen[i] = "http://www.dewen.io/questions?page=" + String.valueOf(i+1);
				for (int i = 0 ; i < 1000 ; i++)
					wenwen[i] = "http://wenwen.sogou.com/cate/?cid=0&tp=6&pg="+String.valueOf(i);
				
				// 1000是否够用？存疑
				progress.setMaximum(total);
				isStart=true;
				startBut.setText("Quiz Pause");
				new Thread(){
					public void run(){
						
						startTime = System.nanoTime();
						System.out.println("Look here!It's the time to start!");

						MyCrawler crawler = new MyCrawler();
						crawler.setKeyword(_keyword);
						crawler.QuizCrawling(a, cnblog, dewen,wenwen);
						System.out.println("finish");
						
						estimatedTime = System.nanoTime() - startTime;
						System.out.println("Total crawling time: " + Long.toString(estimatedTime/1000000) + " ms=================================");
					}
				}.start();
			}
			else if(quiz.getText().equals("Quiz Pause")){
				JOptionPane.showMessageDialog(this, "Pause");
			}
		}
		/*
		else if(e.getSource()==quiz)
		{
			System.out.println("Quiz");
			if(num.getText().equals("")){
				JOptionPane.showMessageDialog(this, "Please enter a number！");
				return;
			}
			if(num.getText().equals("0")){
				JOptionPane.showMessageDialog(this, "number 0 is invalid ！");
				return;
			}
			total=Integer.parseInt(num.getText());
			final String _keyword = getkeyword.getText();
			//11000 -> 18000 changed by lyp
			final String [] a = new String[18000];
			final String [] cnblog = new String[200];
			final String [] dewen = new String[1000];
			final String [] wenwen = new String[1000];
			for (int i = 0 ; i < 18000 ; i++)
				a[i] = "http://stackoverflow.com/questions?page="+String.valueOf(i+1)+"&sort=frequent";
			for (int i = 0 ; i < 200 ; i++)
				cnblog[i] = "http://q.cnblogs.com/list/solved?page=" + String.valueOf(i+1);
			for (int i = 0 ; i < 1000 ; i++)
				dewen[i] = "http://www.dewen.io/questions?page=" + String.valueOf(i+1);
			for (int i = 0 ; i < 1000 ; i++)
				wenwen[i] = "http://wenwen.sogou.com/cate/?cid=0&tp=6&pg="+String.valueOf(i);
			
			// 1000是否够用？存疑
			progress.setMaximum(total);
			isStart=true;
			startBut.setText("Quiz Pause");
			new Thread(){
				public void run(){
					
					startTime = System.nanoTime();
					System.out.println("Look here!It's the time to start!");

					MyCrawler crawler = new MyCrawler();
					crawler.setKeyword(_keyword);
					crawler.QuizCrawling(a, cnblog, dewen,wenwen);
					System.out.println("finish");
					
					estimatedTime = System.nanoTime() - startTime;
					System.out.println("Total crawling time: " + Long.toString(estimatedTime/1000000) + " ms=================================");
				}
			}.start();
		}
		*/
		else if(e.getSource()==analyzeBut){
			//计算分数 add by hxy
			LinkQueue.getUrlsScores();
			Analysis a=new Analysis();
			a.setVisible(true);
		}
		/*else if(e.getSource()==cancelBut)
		{
			isStart=false;
			System.exit(0);
		}*/
		/*else if(e.getSource()== pdfBut || e.getSource()== docBut || e.getSource()== pptBut){
			String text=getUrlSeed.getText();
			String line = "";
			final String _keyword = getkeyword.getText();
			// 1000是否够用？存疑
			final String [] a=new String[1000];
			int i = 0;
			if(!isFileInput){
				BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(text.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
				try {
					while((line = br.readLine()) != null)
					{
						if(!line.equals(""))
							a[i++] = line;
						if(i >= 990)
							break;
					}
					br.close();
				}
				catch (IOException e1) {
					JOptionPane.showMessageDialog(this, "Error when reading！");
					return;
				}
			}else if(getTxt().equals("")){
				JOptionPane.showMessageDialog(this, "Please select a file first！");
				return;
			}else{
				try {
					fis = new FileInputStream(getTxt());// FileInputStream
					// 从文件系统中的某个文件中获取字节
					isr = new InputStreamReader(fis);// InputStreamReader 是字节流通向字符流的桥梁,
					br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
					while ((line = br.readLine()) != null) {
						if(!line.equals(""))
							a[i++]=line;//now
						if(i >= 990)
							break;
					}
				}
				catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(this, "Can't find the file！");
					return;
				}// FileInputStream
				catch (IOException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(this, "Error when reading！");
					return;
				}
				finally{
					try {
						br.close();
						isr.close();
						fis.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
			if(i == 0){
				JOptionPane.showMessageDialog(this, "No Url To crawl! ");
				return ;
			}
						
			if(e.getSource()== pdfBut){
				isStart=true;
				startBut.setText("Pdf Pause");
				new Thread(){
					public void run(){
						
						startTime = System.nanoTime();
						System.out.println("Look here!It's the time to start!");
	
						MyCrawler crawler = new MyCrawler();
						crawler.setKeyword(_keyword);
						crawler.pdfCrawling(a);
						System.out.println("finish");
						
						estimatedTime = System.nanoTime() - startTime;
						System.out.println("Total crawling time: " + Long.toString(estimatedTime/1000000) + " ms=================================");
					}
				}.start();
			}
			else if(e.getSource()== docBut){
				isStart=true;
				startBut.setText("Doc Pause");
				new Thread(){
					public void run(){
						startTime = System.nanoTime();
						System.out.println("Look here!It's the time to start!");
	
						MyCrawler crawler = new MyCrawler();
						crawler.setKeyword(_keyword);
						crawler.docCrawling(a);
						System.out.println("finish");
						
						estimatedTime = System.nanoTime() - startTime;
						System.out.println("Total crawling time: " + Long.toString(estimatedTime/1000000) + " ms=================================");
					}
				}.start();
			}
			else if(e.getSource()== pptBut){
				isStart=true;
				startBut.setText("PPt Pause");
				new Thread(){
					public void run(){
						startTime = System.nanoTime();
						System.out.println("Look here!It's the time to start!");
	
						MyCrawler crawler = new MyCrawler();
						crawler.setKeyword(_keyword);
						crawler.pptCrawling(a);
						System.out.println("finish");
						
						estimatedTime = System.nanoTime() - startTime;
						System.out.println("Total crawling time: " + Long.toString(estimatedTime/1000000) + " ms=================================");
					}
				}.start();
			}
		}
	*/
		else if(e.getSource()==selectBut)
		{
			pathFileIO.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if(pathFileIO.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
				DownLoadFile.oripath = pathFileIO.getSelectedFile().getAbsolutePath();
				selecteddir.setText(DownLoadFile.oripath);
			}
		}
		else if(e.getSource()==txtBut)
		{
			pathFileIO.setFileSelectionMode(JFileChooser.FILES_ONLY);
			if(pathFileIO.showOpenDialog(null) == JFileChooser.FILES_ONLY){
				setTxt(pathFileIO.getSelectedFile().getAbsolutePath());
			}
		}
		else if(e.getSource()==openBut)
		{
			try {
				Runtime.getRuntime().exec("cmd /c start "+DownLoadFile.oripath);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		else if(e.getSource()==howBut)
		{
			JOptionPane.showMessageDialog(this, howToUse);
		}
	}
	/**
	 * 在指定位置插入已访问URL
	 * @return
	 * @param 目标URL(String)
	 * @throws 
	 */
	public void UIinsertURLs(String newURL)	
	{
		this.URLsVisited.insert(newURL+"\n", 0);		
	}
	/**
	 * UI开始运行
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			urlVis.setText("URL Visited :  " + MyCrawler.succeed);
			if (!startBut.getText().equals("Start") && 
				!startBut.getText().equals("Restart") && 
				MyCrawler.succeed != MyCrawler.max)	
				timeCost.setText("Time cost: "+(System.currentTimeMillis()-MyCrawler.startTime)+"ms");
			progress.setValue( MyCrawler.succeed);
			if (MyCrawler.succeed == MyCrawler.max && MyCrawler.succeed != 0)	
				startBut.setText("Restart");
			//下一句if用于测试
//			if(counter++<10000)
//				this.UIinsertURLs("test"+counter+"\n");;
		}
	}
	/**
	 * 配置下拉框状态改变响应触发
	 * @return
	 * @param 事件e(ItemEvent)
	 * @throws 
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		if(inputWay.getSelectedItem().equalsIgnoreCase("No")){
			isFileInput=false;//不选择文件输入
			getUrlSeed.setEditable(true);
			txtBut.setVisible(false);
		}
		else{
			isFileInput=true;//选择文件输入
			getUrlSeed.setEditable(false);
			txtBut.setVisible(true);
		}
	}
	/**
	 * 得到关键字
	 * @return 关键字txt(String)
	 * @param 
	 * @throws 
	 */
	public String getTxt() {
		return txt;
	}
	/**
	 * 设置关键字
	 * @return 关键字txt(String)
	 * @param
	 * @throws 
	 */
	public void setTxt(String txt) {
		this.txt = txt;
	}
}