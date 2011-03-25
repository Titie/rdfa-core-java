package fi.tikesos.rdfa.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFReader;
import com.hp.hpl.jena.rdf.model.impl.RDFReaderFImpl;

/**
 * @author ssakorho
 * 
 */
public class RDFaParserTest {
	@Test
	public void parserTest() {
		XMLReader reader;
		String[] tests = { "0002", "0006", "0007", "0008", "0009", "0010",
				"0012", "0013", "0014", "0015", "0017", "0018", "0019", "0020",
				"0021", "0023", "0025", "0026", "0027", "0029", "0030", "0031",
				"0032", "0033", "0034", "0035", "0036", "0037", "0038", "0039",
				"0040", "0041", "0042", "0046", "0047", "0048", "0049", "0050",
				"0051", "0052", "0053", "0054", "0055", "0056", "0057", "0058",
				"0059", "0060", "0061", "0062", "0063", "0064", "0065", "0066",
				"0067", "0068", "0069", "0070", "0071", "0072", "0073", "0074",
				"0075", "0076", "0077", "0078", "0079", "0080", "0081", "0082",
				"0083", "0084", "0085", "0087", "0088", "0089", "0090", "0091",
				"0093", "0099", "0104", "0106", "0107", "0108", "0109", "0110",
				"0111", "0112", "0113", "0114", "0115", "0117", "0118", "0119",
				"0120", "0121", "0122", "0126", "0131", "0134", "0140", "0147",
				"0172", "0173", "0174", "0175", "0176", "0177", "0178", "0179",
				"0180", "0181", "0182", "0183", "0184", "0185", "0186", "0187",
				"0188", "0189", "0190", "0193", "0195", "0196", "0197", "0198",
				"0200", "0204", "0205", "0206", "0207", "0208", "0209", "0210",
				"0211", "0212", "0213" };
		String[] inverse = { "0042", "0107", "0122", "0140", "0189" };

		// Register parser
		RDFReaderFImpl.setBaseReaderClassName("RDFA-CORE",
				"fi.tikesos.rdfa.core.jena.RDFaReader");

		try {
			reader = XMLReaderFactory.createXMLReader();
			reader.setFeature("http://xml.org/sax/features/validation",
					Boolean.FALSE);

			for (String currentTest : tests) {
//				if (currentTest.compareTo("0186") == 0) break;
//				if (currentTest.compareTo("0189") != 0) continue;
				InputStream inputXML = this.getClass().getResourceAsStream(
						"/tests/xhtml/" + currentTest + ".xhtml");
				if (inputXML == null) continue;
				InputStream inputSPARQL = this.getClass().getResourceAsStream(
						"/tests/sparql/" + currentTest + ".sparql");
				// Create empty model
				Model testModel = ModelFactory.createDefaultModel();
				Assert.assertNotNull(testModel);

				// Get reader
				RDFReader rdfReader = testModel.getReader("RDFA-CORE");
				Assert.assertNotNull(rdfReader);

				// Read RDFa
				long readStart = System.currentTimeMillis();
				rdfReader.read(testModel, inputXML,
						"http://rdfa.digitalbazaar.com/test-suite/test-cases/xhtml1/rdfa1.1/"
								+ currentTest + ".xhtml");
				long readEnd = System.currentTimeMillis();
				
				// Create Query
				Query query = QueryFactory.create(consumeStream(inputSPARQL));
				Assert.assertNotNull(query);

				// Prepare query for execution
				QueryExecution queryExecution = QueryExecutionFactory.create(
						query, testModel);
				Assert.assertNotNull(queryExecution);

				// Execute query
				boolean queryResult = queryExecution.execAsk();
				
				// Compare result
				boolean fail = Arrays.binarySearch(inverse, currentTest) >= 0 ? true : false;
				System.out.println("Test: " + currentTest + " "
						+ (queryResult != fail ? "[SUCCESS]" : "[FAIL!]") + " " + (readEnd - readStart) + "ms");
			}
		} catch (Exception e) {
			// Exception
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	public static void main(String[] args) {
		new RDFaParserTest().parserTest();
	}

	public String consumeStream(InputStream is) throws IOException {
		Writer writer = new StringWriter();

		char[] buffer = new char[1024];
		try {
			Reader reader = new BufferedReader(new InputStreamReader(is,
					"UTF-8"));
			int n;
			while ((n = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, n);
			}
		} finally {
			is.close();
		}
		return writer.toString();
	}
}
