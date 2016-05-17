import java.awt.GridLayout;
import javax.swing.*;
import org.htmlparser.util.ParserException;
/**
* description: 图标展示面板
* note: 
* modificationDate: 2014-12-1
*/ 
public class Analysis extends JFrame{
	/**
	 * 构造函数,设计饼状图
	 * @return 
	 * @param 
	 * @throws 
	 */
	Analysis(){
		super("Database Analysis");
		/*this.add(new BarChart().getChartPanel());
		//this.add(new PieChart().getChartPanel());
		this.setBounds(50, 50, 800, 600);
		this.setVisible(true);
		*/
		
		// 创建选项窗格
        JTabbedPane pane = new JTabbedPane();
        // 设置面板布局为网格布局
        this.setLayout(new GridLayout(1,1));
        // 设定选项卡放在上部
        pane.setTabPlacement(JTabbedPane.TOP);
        // 将选项窗格放置在面板中 
        this.add(pane);
        // 创建一个PieChart面板、一个BarChart面板并添加到选项窗格
        pane.addTab("PieChart",  new PieChart().getChartPanel());
        pane.addTab("BarChart",  new BarChart().getChartPanel());
        pane.addTab("TypePie",   new TypePie().getChartPanel());
                
        setSize(800, 600);
        setLocationRelativeTo(null); 
        setVisible(true);
	}
	
}
