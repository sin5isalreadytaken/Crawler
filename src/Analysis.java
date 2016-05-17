import java.awt.GridLayout;
import javax.swing.*;
import org.htmlparser.util.ParserException;
/**
* description: ͼ��չʾ���
* note: 
* modificationDate: 2014-12-1
*/ 
public class Analysis extends JFrame{
	/**
	 * ���캯��,��Ʊ�״ͼ
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
		
		// ����ѡ���
        JTabbedPane pane = new JTabbedPane();
        // ������岼��Ϊ���񲼾�
        this.setLayout(new GridLayout(1,1));
        // �趨ѡ������ϲ�
        pane.setTabPlacement(JTabbedPane.TOP);
        // ��ѡ������������� 
        this.add(pane);
        // ����һ��PieChart��塢һ��BarChart��岢��ӵ�ѡ���
        pane.addTab("PieChart",  new PieChart().getChartPanel());
        pane.addTab("BarChart",  new BarChart().getChartPanel());
        pane.addTab("TypePie",   new TypePie().getChartPanel());
                
        setSize(800, 600);
        setLocationRelativeTo(null); 
        setVisible(true);
	}
	
}
