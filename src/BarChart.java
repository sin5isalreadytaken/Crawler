import java.awt.Font;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.TextAnchor;
/**
* description: ��״ͼչʾ
* note: 
* modificationDate: 2014-11-26
*/ 
public class BarChart {
	ChartPanel frame;
	/**
	 * ���캯��,�����״ͼ
	 * @return 
	 * @param 
	 * @throws 
	 */
	public BarChart() {
		CategoryDataset dataset = getDataSet();
		JFreeChart chart = ChartFactory.createBarChart3D("Top-10 Urls : RankScore", // ͼ�����
														"Url", // Ŀ¼�����ʾ��ǩ
														"RankScore", // ��ֵ�����ʾ��ǩ
														dataset, // ���ݼ�
														PlotOrientation.VERTICAL, // ͼ����ˮƽ����ֱ
														false, // �Ƿ���ʾͼ��(���ڼ򵥵���״ͼ������false)
														false, // �Ƿ����ɹ���
														false // �Ƿ�����URL����
														);
		
		frame = new ChartPanel(chart, true);
		CategoryPlot categoryplot = (CategoryPlot) chart.getPlot();  		  
        NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();   
        CategoryAxis domainAxis = categoryplot.getDomainAxis();
        /*------����X�������ϵ�����-----------*/  
        domainAxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 11));
        domainAxis.setCategoryLabelPositions(
        		CategoryLabelPositions.createUpRotationLabelPositions(Math.PI/6.0));
		chart.getTitle().setFont(new Font("����",Font.BOLD,20));	//	���ñ�������
		
	}
	/**
	 * �õ�����URL���������ݼ�
	 * @return dataset(CategoryDataset)
	 * @param 
	 * @throws 
	 */
	private static CategoryDataset getDataSet() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();		
		Hashtable<String, Double> topRankUrls = new Hashtable<String, Double>();
		ArrayList<String> topUrlsSort = new ArrayList<String>();
		topRankUrls = LinkQueue.getTopRankUrls();
		topUrlsSort = LinkQueue.getTopUrlsSort();
        for (String url: topUrlsSort) {
        	dataset.addValue(topRankUrls.get(url), "����", url);
        }
		return dataset;
	}
	/**
	 * ��ȡ��״ͼ
	 * @return frame(ChartPanel)
	 * @param 
	 * @throws 
	 */
	public ChartPanel getChartPanel(){
    	return frame;
    	
    }
}
