import java.awt.Font;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
/**
* description: ��״ͼչʾ
* note: 
* modificationDate: 2014-11-26
*/ 
public class PieChart {
	ChartPanel frame1;
	/**
	 * ���캯��,��Ʊ�״ͼ
	 * @return 
	 * @param 
	 * @throws 
	 */
	public PieChart(){
		  DefaultPieDataset data = getDataSet();
		  //	��ӡ���� = true,tooltips = false, ���� = false
	      JFreeChart chart = ChartFactory.createPieChart3D("Data",data,true,false,false);
	      //	���ðٷֱ�
	      PiePlot pieplot = (PiePlot) chart.getPlot();
	      DecimalFormat df = new DecimalFormat("0.00%");//	���һ��DecimalFormat������Ҫ������С������
	      NumberFormat nf = NumberFormat.getNumberInstance();//	���һ��NumberFormat����
	      //���StandardPieSectionLabelGenerator����
	      StandardPieSectionLabelGenerator sp1 = new StandardPieSectionLabelGenerator("{0}  {2}", nf, df);
	      pieplot.setLabelGenerator(sp1);//���ñ�ͼ��ʾ�ٷֱ�
	      //û�����ݵ�ʱ����ʾ������
	      pieplot.setNoDataMessage("��������ʾ");
	      pieplot.setCircular(false);
	      pieplot.setLabelGap(0.02D);
	      pieplot.setIgnoreNullValues(true);//	���ò���ʾ��ֵ
	      pieplot.setIgnoreZeroValues(true);//	���ò���ʾ��ֵ
	      frame1=new ChartPanel (chart,true);
	      chart.getTitle().setFont(new Font("����",Font.BOLD,20));//���ñ�������
	      PiePlot piePlot= (PiePlot) chart.getPlot();//��ȡͼ���������
	      piePlot.setLabelFont(new Font("����",Font.BOLD,20));//�������
	      chart.getLegend().setItemFont(new Font("����",Font.BOLD,20));
	}
	/**
	 * �õ�pdf��quiz��webpage�����ݼ�
	 * @return dataset(DefaultPieDataset)
	 * @param 
	 * @throws 
	 */
    private static DefaultPieDataset getDataSet() {
    	//
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("pdf",Integer.parseInt(ConnectServer.getSum_pdf()));
        dataset.setValue("ppt",Integer.parseInt(ConnectServer.getSum_ppt()));
        dataset.setValue("doc",Integer.parseInt(ConnectServer.getSum_doc()));
        dataset.setValue("quiz",Integer.parseInt(ConnectServer.getSum_quiz()));
        dataset.setValue("webpage",Integer.parseInt(ConnectServer.getSum_webpage()));
        return dataset;
    }
    /**
	 * ��ȡ��״ͼ
	 * @return frame(ChartPanel)
	 * @param 
	 * @throws 
	 */
    public ChartPanel getChartPanel(){
    	return frame1;
    }
}