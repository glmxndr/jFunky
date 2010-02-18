package fr.jfunctest.render;

import java.util.List;

import fr.jfunctest.BaseTestCase;
import fr.jfunctest.assertion.Assertion;

public class JunitXmlRenderer extends BaseRenderer {

	public void render(BaseTestCase testCase) {
		String testName = testCase.getClass().getCanonicalName();
		
		int totalTests = testCase.getTestTotalCount();
		int failures = testCase.getTestFailuresCount();
		
		out.print("<testsuite errors=\"0\" failures=\"");
		out.print(failures);
		out.print("\" tests=\"");
		out.print(totalTests);
		out.print("\" name=\"");
		out.print(sanitize(testName));
		out.print("\" hostname=\"UNUSED\" time=\"0\" timestamp=\"UNUSED\">\n");
		
		printProperties();
		
		for (String requirement : testCase.getRequirements()){
			List<Assertion> assertions = testCase.getAssertions(requirement);
			
			for (Assertion a: assertions){

				out.print("  <testcase time=\"0\" classname=\"");
				out.print(sanitize(testName));
				out.print("\" name=\"");
				out.print(sanitize(requirement));
				out.print(sanitize(a.hasMessage() ? " - " + a.getMessage() : ""));
				out.print("\">\n");
				if (!a.isOk()){
					out.print("    <failure message=\"failed\" type=\"Assertion failure\">\n");
					if (a.hasFailureDescription()){
						out.print(sanitize(a.getFailureDescription()));
					}
					out.print("\n  </failure>\n");
				}
				out.print("  </testcase>\n");
			}
			

		}
		out.print("  <system-out/>\n");
		out.print("  <system-err/>\n");
		out.print("</testsuite>");
		
		out.close();
	}

	private void printProperties() {
		out.print("  <properties>\n");
		for (Object key:System.getProperties().keySet()){
			out.print("    <property name=\"");
			out.print(sanitize(key));
			out.print("\" value=\"");
			out.print(sanitize(System.getProperty(key.toString())));
			out.print("\"/>\n");
		}
		out.print("  </properties>\n");
	}

	private static String sanitize(Object in){
		return in.toString().replace("<", "&#x3c;").replace(">", "&#x3e;").replace("\"", "&#x22;");
	}
	
}
