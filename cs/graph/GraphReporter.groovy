package cs.graph;

import com.tszadel.qctools4j.clients.BugClient
import com.tszadel.qctools4j.clients.QcConnectionImpl
import com.tszadel.qctools4j.filters.FieldFilter
import com.tszadel.qctools4j.model.defect.Bug

QcConnectionImpl ct = new QcConnectionImpl("http://qualitycenter.local.net:8080/qcbin/")
ct.connect("CMO", "yourpsw", "REPO", "PROJECT")
list = new ArrayList()
FieldFilter filter2 = new FieldFilter("BG_USER_06","Pros", true)
list.add(filter2)

BugClient coll = ct.getBugClient()
Collection op = coll.getBugs(list, 1)
Object[] array = op.toArray()
ct.disconnect()

DefectSeverityChart graph1 = new DefectSeverityChart(array)
DefectPriorityChart graph2 = new DefectPriorityChart(array)
DefectStatusChart graph3 = new DefectStatusChart(array)
DefectDetectionChart graph4 = new DefectDetectionChart(array)
DefectSeverityStatusBarChart graph5 = new DefectSeverityStatusBarChart(array)
DefectTestLevelBarChart graph6 = new DefectTestLevelBarChart(array)
DefectAreaChart graph7 = new DefectAreaChart(array)
DefectSeverityComponentBarChart graph8 = new DefectSeverityComponentBarChart(array)