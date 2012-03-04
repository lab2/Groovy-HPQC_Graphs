package cs.graph

import com.tszadel.qctools4j.model.defect.Bug
import org.jfree.chart.axis.NumberAxis
import org.jfree.chart.axis.NumberTickUnit
import org.jfree.data.category.DefaultCategoryDataset
import org.jfree.chart.plot.CategoryPlot
import org.jfree.chart.title.TextTitle
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.ChartUtilities
import org.jfree.chart.renderer.category.BarRenderer
import java.text.DecimalFormat 
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator
import org.jfree.chart.StandardChartTheme
import org.jfree.chart.plot.PlotOrientation as Orientation
import javax.swing.WindowConstants as WC
import org.jfree.chart.block.BlockBorder
import org.jfree.chart.axis.CategoryAxis
import groovy.swing.SwingBuilder  
import java.text.DecimalFormat
import java.awt.Color 
import java.awt.Font

class DefectTestLevelBarChart {
	Object[] defects
	DefaultCategoryDataset dataset
	
	public DefectTestLevelBarChart(Object[] defects){
		this.defects = defects
		dataset = new DefaultCategoryDataset()
		def maptl = []
		defects.each{ obj -> 
			Bug defect = (Bug)obj
			if (!maptl.contains(defect.getUser16()))
				maptl.add defect.getUser16()  
		}  
		  
		maptl.each{sv ->
				dataset.addValue defects.findAll{ d -> d.getUser16().equals(sv) }.size(), "Test Level", sv
			}
		 
		createGraph()
	} 
	
	void createGraph(){
		def options = [true, true, false]
		def labels = ["", "Test Level", "Anzahl Fehler"]
		ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme())
		BarRenderer.setDefaultShadowsVisible(false)
		def chart = ChartFactory.createBarChart3D(*labels, dataset, Orientation.HORIZONTAL, *options)
		CategoryPlot plot = chart.getCategoryPlot()
		BarRenderer barrenderer = (BarRenderer)plot.getRenderer()
		barrenderer.setItemLabelFont(new Font("", Font.BOLD, 11))
		barrenderer.setItemLabelPaint(Color.white)
		
		DecimalFormat format = new DecimalFormat("###")
		barrenderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", format));
		barrenderer.setItemLabelsVisible(true) 

		plot.getRangeAxis().setTickUnit(new NumberTickUnit(300))
		
		TextTitle subtitle = new TextTitle(new Date().toString())
		chart.addSubtitle(subtitle)
		chart.setBackgroundPaint(Color.white)
		chart.getLegend().setFrame(BlockBorder.NONE)
		ChartPanel chartPanel = new ChartPanel(chart)
		def swing = new SwingBuilder()
		def frame = swing.frame(title:new Date().toString(),
				defaultCloseOperation:WC.EXIT_ON_CLOSE) {
					panel(id:'canvas') {
						widget(chartPanel)
					}
				}
		ChartUtilities.saveChartAsPNG(new File(this.class.name.toString()+".png"), chart, 650, 450);
		frame.pack()
		frame.show()
	}
}