//    JFuncTest - Functional testig in Java
//    Copyright (C) 2010 G. Andrieu (subtenante gmail com)
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
