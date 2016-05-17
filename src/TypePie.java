import java.awt.Font;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;


public class TypePie {
	ChartPanel frame1;
	/**
	 * 构造函数,设计饼状图
	 * @return 
	 * @param 
	 * @throws 
	 */
	public 	TypePie(){
		  DefaultPieDataset data = getDataSet();
		  //	刻印文字 = true,tooltips = false, 链接 = false
	      JFreeChart chart = ChartFactory.createPieChart3D("Data",data,true,false,false);
	      //	设置百分比
	      PiePlot pieplot = (PiePlot) chart.getPlot();
	      DecimalFormat df = new DecimalFormat("0.00%");//	获得一个DecimalFormat对象，主要是设置小数问题
	      NumberFormat nf = NumberFormat.getNumberInstance();//	获得一个NumberFormat对象
	      //获得StandardPieSectionLabelGenerator对象
	      StandardPieSectionLabelGenerator sp1 = new StandardPieSectionLabelGenerator("{0}  {2}", nf, df);
	      pieplot.setLabelGenerator(sp1);//设置饼图显示百分比
	      //没有数据的时候显示的内容
	      pieplot.setNoDataMessage("无数据显示");
	      pieplot.setCircular(false);
	      pieplot.setLabelGap(0.02D);
	      pieplot.setIgnoreNullValues(true);//	设置不显示空值
	      pieplot.setIgnoreZeroValues(true);//	设置不显示负值
	      frame1=new ChartPanel (chart,true);
	      chart.getTitle().setFont(new Font("宋体",Font.BOLD,20));//设置标题字体
	      PiePlot piePlot= (PiePlot) chart.getPlot();//获取图表区域对象
	      piePlot.setLabelFont(new Font("宋体",Font.BOLD,20));//解决乱码
	      chart.getLegend().setItemFont(new Font("黑体",Font.BOLD,20));
	}
	/**
	 * 得到pdf，quiz，webpage的数据集
	 * @return dataset(DefaultPieDataset)
	 * @param 
	 * @throws 
	 */
	private static DefaultPieDataset getDataSet() {
    	//
		MyT.primaryType();
        DefaultPieDataset dataset = new DefaultPieDataset();
        int i=0;
        int j,k;
        for(String s:MyT.type)
        {
        	j=Integer.parseInt(ConnectServer.typeNum(s));
        	dataset.setValue(s,j);
        	i+=j;
        }
        k=Integer.parseInt(ConnectServer.sum());
        dataset.setValue("其他",k-i);
        return dataset;
    }
	/*private static DefaultPieDataset getDataSet() {
    	//
		MyT.primaryType();
        DefaultPieDataset dataset = new DefaultPieDataset();
        for(String s:MyT.type)
        {
        	dataset.setValue(s,Integer.parseInt(ConnectServer.typeNum(s)));
        }
        return dataset;
    }*/

    /**
	 * 获取饼状图
	 * @return frame(ChartPanel)
	 * @param 
	 * @throws 
	 */
    public ChartPanel getChartPanel(){
    	return frame1;
    }

}
