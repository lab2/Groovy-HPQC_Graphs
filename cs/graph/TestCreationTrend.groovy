package cs.graph

import com.tszadel.qctools4j.model.test.Test
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

class TestCreationChart {
	Object[] tests
	DefaultCategoryDataset dataset
	Calendar calendar

	public TestCreationChart(Object[] tests){
		dataset = new DefaultCategoryDataset()
		calendar = new GregorianCalendar()

		this.tests = tests
		def map = []
		def range = 30..52
		range.each{ w ->  map.add w }

		map.each{it ->
			dataset.addValue((Float)tests.findAll{ d ->
				Test test = (Test)d
				if (d.getCreationDate()!=null)
					calendar.setTime(d.getCreationDate())
				calendar.get(Calendar.WEEK_OF_YEAR).toString().equals(it.toString())
			}.size(), "Created", it)
		}

		createGraph()
	}

	void createGraph(){
		def labels = [
			"",
			"WK",
			"Number of Test Cases"
		]
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
		ChartUtilities.saveChartAsPNG(new File(this.class.name.toString()+".png"), chart, 550, 350);
		frame.pack()
		frame.show()
	}
}