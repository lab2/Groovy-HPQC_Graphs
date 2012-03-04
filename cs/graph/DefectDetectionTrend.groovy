package cs.graph

import com.tszadel.qctools4j.model.defect.Bug
import java.awt.BasicStroke
import java.text.SimpleDateFormat
import org.jfree.data.category.DefaultCategoryDataset
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.axis.CategoryAxis
import org.jfree.chart.axis.NumberAxis
import org.jfree.chart.axis.NumberTickUnit
import org.jfree.chart.plot.CategoryPlot
import org.jfree.chart.renderer.category.LineAndShapeRenderer
import org.jfree.chart.plot.PlotOrientation as Orientation
import java.awt.Color
import org.jfree.chart.title.TextTitle
import groovy.swing.SwingBuilder
import org.jfree.chart.ChartUtilities
import javax.swing.WindowConstants as WC

class DefectDetectionChart {
	Object[] defects
	DefaultCategoryDataset dataset
	Calendar calendar

	public DefectDetectionChart(Object[] defects){
		dataset = new DefaultCategoryDataset()
		calendar = new GregorianCalendar()

		this.defects = defects
		def map = []
		def range = 19..36
		range.each{ w ->  map.add w }

		map.each{it ->
			dataset.addValue((Float)defects.findAll{ d ->
				Bug defect = (Bug)d
				if (d.getDetectionDate()!=null)
					calendar.setTime(d.getDetectionDate())
				calendar.get(Calendar.WEEK_OF_YEAR).toString().equals(it.toString())
			}.size(), "Detected", it)
		}

		map.each{it ->
			dataset.addValue((Float)defects.findAll{ d ->
				Bug defect = (Bug)d
				if (defect.getClosingDate()!=null){
					calendar.setTime(d.getClosingDate())
					calendar.get(Calendar.WEEK_OF_YEAR).toString().equals(it.toString())
				}
			}.size(), "Closed or Rejected", it)
		}

		createGraph()
	}

	void createGraph(){
		def labels = ["", "Woche", "Anzahl Fehler"]
		def chart = ChartFactory.createAreaChart(
				*labels,
				dataset,
				Orientation.VERTICAL,
				true,
				true,
				false)
		CategoryPlot plot = chart.getCategoryPlot()
		plot.setForegroundAlpha(0.4f)
		plot.setBackgroundPaint(new Color(210, 210, 210))
		NumberAxis axis = plot.getRangeAxis()
		axis.setTickUnit(new NumberTickUnit(20))

		TextTitle subtitle = new TextTitle(new Date().toString())
		chart.addSubtitle(subtitle)
		def swing = new SwingBuilder()
		def frame = swing.frame(title:new Date().toString(),
				defaultCloseOperation:WC.EXIT_ON_CLOSE) {
					panel(id:'canvas') { widget(new ChartPanel(chart)) }
				}
		ChartUtilities.saveChartAsPNG(new File(this.class.name.toString()+".png"), chart, 650, 350);
		frame.pack()
		frame.show()
	}
}