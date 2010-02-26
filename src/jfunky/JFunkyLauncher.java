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
package jfunky;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jfunky.render.JunitXmlRenderer;
import jfunky.render.Renderer;
import jfunky.util.StringUtil;


/**
 * 
 * This class allows to launch jfunctest from the command line,
 * or from ant.
 * 
 * Call {@link #main(String[])} with the arguments as described in {@link #usage()}.
 * 
 * @author G.Andrieu
 *
 */
public class JFunkyLauncher {

	//+++++ ATTRIBUTES
	private BaseTestSuite suite;
	private final List<Class> classes = new ArrayList<Class>();;
	private File folderOut = new File("");
	private Renderer renderer = new JunitXmlRenderer();
	//----- ATTRIBUTES
	
	//+++++ PUBLIC
	/**
	 * Describes how to use this class from the command line
	 */
	public static void usage(){
		System.out.println("USAGE:\n");
		System.out.println(" -suite:pack.age.ClassName : specify the classes to test\n");
		System.out.println(" -classes:pack.age.ClassName[;pack.age.OtherClassName;...] : specify the classes to test\n");
		System.out.println(" [-out:D:/path/to/out/folder] : specify where to put the results ; defaults to execution path\n");
		System.out.println(" [-renderer:pack.age.:Classname] : specify the renderer class ; default to JUnitXmlRenderer\n");
	}

	public static void main(String[] args) {
		new JFunkyLauncher().process(args);
	}
	//----- PUBLIC
	
	//+++++ PRIVATE
	private void process(String[] args) {
		Map<String,String> mapargs = mapifyArguments(args);
		System.out.println(mapargs);
		if (!valid(mapargs)){
			usage();
		} else {
			
			this.suite.setRenderer(this.renderer);
			this.suite.setFolderOut(this.folderOut);
			for (Class c:this.classes){
				this.suite.addTest((Class<? extends BaseTestCase>)c);
			}
			
			this.suite.beforeSuite();
			this.suite.runTests();
			this.suite.afterSuite();
			
		}
	}
	
	private boolean valid(Map<String, String> mapargs) {
		
		// SUITE OR CLASSES
		if (!mapargs.containsKey("suite") && !mapargs.containsKey("classes")){
			System.out.println("Either suite or classes argument required.");
			return false;
		}
		
		//  SUITE
		if (mapargs.containsKey("suite")){
			try{
				this.suite = (BaseTestSuite)Class.forName(mapargs.get("suite")).newInstance();
			}
			catch(Exception e){
				System.out.println("Could not instanciate the given suite ('"+mapargs.get("suite")+"')");
				return false;
			}
		} else {
			this.suite = new BaseTestSuite();
		}
		
		// CLASSES
		if (mapargs.containsKey("classes")){
			for (String className : mapargs.get("classes").split(";")){
				className = className.replaceAll("[/\\\\]", ".").replaceFirst("\\.class$","");
				try{
					Class<? extends BaseTestCase> clazz = (Class<? extends BaseTestCase>)Class.forName(className);
					this.classes.add(clazz);
				}
				catch(Exception e){
					System.out.println("Could not load given class ('"+className+"')");
					return false;
				}
			}
		}
		
		// RENDERER
		if (mapargs.containsKey("renderer")){
			try{
				this.renderer = (Renderer)Class.forName(mapargs.get("renderer")).newInstance();
			}
			catch(Exception e){
				System.out.println("Could not instanciate the given renderer ('"+mapargs.get("renderer")+"')");
				return false;
			}
		}
		
		// OUT FOLDER
		if (mapargs.containsKey("out")){
			try{
				this.folderOut = new File(mapargs.get("out"));
				if (!this.folderOut.isDirectory()){
					System.out.println("Output folder is not a folder ('"+mapargs.get("out")+"')");
					return false;
				}
				if (!this.folderOut.canWrite()){
					System.out.println("Output folder is not writable ('"+mapargs.get("out")+"')");
					return false;
				}
			}
			catch(Exception e){
				System.out.println("Could not access given output folder ('"+mapargs.get("out")+"')");
				return false;
			}
		}
		
		// OK !
		return true;
	}	

    private Map<String, String> mapifyArguments(String[] args) {
    	String allArgs = StringUtil.implode(args, " ");
    	System.out.println(allArgs);
        Map<String, String> result = new HashMap<String, String>();

        List<String> params = StringUtil.findAll(allArgs, "-[a-z]+:[^ ]+");

        for (String s : params) {
            String key = StringUtil.findGroup(s, "^-([a-z]+):", 1);
            String value = StringUtil.findGroup(s, "^-[a-z]+:([^ ]+)$", 1);
            result.put(key, value);
        }

        return result;
    }
	//----- PRIVATE
	
}
