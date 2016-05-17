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
* description: 饼状图展示
* note: 
* modificationDate: 2014-11-26
*/ 
public class BarChart {
	ChartPanel frame;
	/**
	 * 构造函数,设计柱状图
	 * @return 
	 * @param 
	 * @throws 
	 */
	public BarChart() {
		CategoryDataset dataset = getDataSet();
		JFreeChart chart = ChartFactory.createBarChart3D("Top-10 Urls : RankScore", // 图表标题
														"Url", // 目录轴的显示标签
														"RankScore", // 数值轴的显示标签
														dataset, // 数据集
														PlotOrientation.VERTICAL, // 图表方向：水平、垂直
														false, // 是否显示图例(对于简单的柱状图必须是false)
														false, // 是否生成工具
														false // 是否生成URL链接
														);
		
		frame = new ChartPanel(chart, true);
		CategoryPlot categoryplot = (CategoryPlot) chart.getPlot();  		  
        NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();   
        CategoryAxis domainAxis = categoryplot.getDomainAxis();
        /*------设置X轴坐标上的文字-----------*/  
        domainAxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 11));
        domainAxis.setCategoryLabelPositions(
        		CategoryLabelPositions.createUpRotationLabelPositions(Math.PI/6.0));
		chart.getTitle().setFont(new Font("宋体",Font.BOLD,20));	//	设置标题字体
		
	}
	/**
	 * 得到各个URL排名的数据集
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
        	dataset.addValue(topRankUrls.get(url), "数据", url);
        }
		return dataset;
	}
	/**
	 * 获取柱状图
	 * @return frame(ChartPanel)
	 * @param 
	 * @throws 
	 */
	public ChartPanel getChartPanel(){
    	return frame;
    	
    }
}
