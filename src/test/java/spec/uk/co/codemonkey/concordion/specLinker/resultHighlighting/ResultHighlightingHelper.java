package spec.uk.co.codemonkey.concordion.specLinker.resultHighlighting;

import static java.lang.Integer.parseInt;

import java.io.File;
import java.io.FileWriter;

public class ResultHighlightingHelper {

	static void writeJunitResults(File dir, String testName, String passes, String fails) throws Exception {
		writeJunitResults(dir, testName, parseInt(passes), parseInt(fails));
	}
	
	static void writeJunitResults(File dir, String testName, int passes, int fails) throws Exception {
		File file = new File(dir, "TEST-" + testName + ".xml");
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		fileWriter.write("<testsuite errors=\"0\" failures=\"" + fails + "\" hostname=\"local\" name=\"" + testName + "\" tests=\"" + (passes + fails) + "\">");
		fileWriter.write("</testsuite>");
		fileWriter.close();
	}
	
}
