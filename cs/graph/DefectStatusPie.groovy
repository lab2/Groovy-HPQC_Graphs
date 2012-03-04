package cs.graph

import com.tszadel.qctools4j.model.defect.Bug
import org.jfree.data.general.DefaultPieDataset
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.plot.PiePlot
import org.jfree.chart.labels.StandardPieSectionLabelGenerator
import java.awt.Color
import org.jfree.chart.axis.NumberAxis
import org.jfree.chart.axis.NumberTickUnit
import java.text.NumberFormat
import org.jfree.chart.title.TextTitle
import groovy.swing.SwingBuilder
import org.jfree.chart.ChartUtilities
import javax.swing.WindowConstants as WC

class DefectStatusChart {
	Object[] defects
	DefaultPieDataset dataset

	public DefectStatusChart(Object[] defects){
		this.defects = defects
		dataset = new DefaultPieDataset()
		def map = []
		defects.each{ obj ->
			Bug defect = (Bug)obj
			if (!map.contains(defect.getStatus()))
				map.add defect.getStatus()
		}

		map.each{it ->
			dataset.setValue(it.toString(), defects.findAll{ d -> d.getStatus().equals(it) }.size())
		}
		createGraph()
	}

	void createGraph(){
		def options = [true, true, false]
		def chart = ChartFactory.createPieChart3D("", dataset, *options)
		PiePlot plot = (PiePlot) chart.getPlot()
		plot.setBackgroundPaint(Color.white)
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}, ({1}), {2}", NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance()));

		TextTitle subtitle = new TextTitle(new Date().toString())
		chart.addSubtitle(subtitle)
		def swing = new SwingBuilder()
		def frame = swing.frame(title:new Date().toString(),
				defaultCloseOperation:WC.EXIT_ON_CLOSE) {
					panel(id:'canvas') { widget(new ChartPanel(chart)) }
				}
		ChartUtilities.saveChartAsPNG(new File(this.class.name.toString()+".png"), chart, 450, 310);
		frame.pack()
		frame.show()
	}
}