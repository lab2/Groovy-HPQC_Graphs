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

class DefectSeverityComponentBarChart {
	Object[] defects
	Object[] cache
	DefaultCategoryDataset dataset

	public DefectSeverityComponentBarChart(Object[] defects){
		this.defects = defects
		dataset = new DefaultCategoryDataset()
		def mapsv = [], mapst = []
		defects.each{ obj ->
			Bug defect = (Bug)obj
			if (!mapsv.contains(defect.getUser06()))
				mapsv.add defect.getUser06()
			if (!mapst.contains(defect.getStatus()))
				mapst.add defect.getStatus()
		}

		mapsv.each{sv ->
			cache = defects.findAll{ d -> d.getUser06().equals(sv) }
			mapst.each{st ->
				dataset.addValue cache.findAll{ d -> d.status.equals(st) }.size(), st, sv
			}
		}
		createGraph()
	}

	void createGraph(){
		def options = [true, true, false]
		def labels = [
			"",
			"Schweregrad",
			"Anzahl Fehler"
		]
		ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme())
		BarRenderer.setDefaultShadowsVisible(false)
		def chart = ChartFactory.createBarChart3D(*labels, dataset, Orientation.VERTICAL, *options)
		CategoryPlot plot = chart.getCategoryPlot()
		BarRenderer barrenderer = (BarRenderer)plot.getRenderer()

		plot.setBackgroundPaint(Color.white)
		plot.getRenderer().setSeriesPaint(0, new Color(0, 93, 171))
		plot.getRenderer().setSeriesPaint(1, new Color(173, 100, 153))
		plot.getRenderer().setSeriesPaint(3, new Color(190, 204, 127))
		plot.getRenderer().setSeriesPaint(2, new Color(77, 154, 119))
		plot.getRenderer().setSeriesPaint(4, new Color(11, 9, 5))
		plot.getRenderer().setSeriesPaint(5, new Color(0, 112, 60))
		plot.getRenderer().setSeriesPaint(6, new Color(0, 217, 60))
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
					panel(id:'canvas') { widget(chartPanel) }
				}
		ChartUtilities.saveChartAsPNG(new File(this.class.name.toString()+".png"), chart, 650, 450);
		frame.pack()
		frame.show()
	}
}