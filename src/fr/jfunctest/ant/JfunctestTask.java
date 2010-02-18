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
package fr.jfunctest.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.Resources;

import fr.jfunctest.BaseTestCase;
import fr.jfunctest.BaseTestSuite;
import fr.jfunctest.render.JunitXmlRenderer;
import fr.jfunctest.render.Renderer;

public class JfunctestTask extends Task {

	private Project project;
	private Resources resources = new Resources();
	private Renderer renderer = new JunitXmlRenderer();
	private File pathOut = new File("");

	public JfunctestTask(Project project) {
		this.project = project;
	}

	public void addFileSet(FileSet fs) {
		add(fs);

		if (fs.getProject() == null)
			fs.setProject(this.project);
	}

	public void add(ResourceCollection rc) {
		this.resources.add(rc);
	}

	public void setRenderer(String rendererName) {
		try {
			this.renderer = (Renderer) Class.forName(rendererName)
					.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setOut(String outPath) {
		this.pathOut = new File(outPath);
		this.pathOut.mkdirs();
	}

	@Override
	public void execute() throws BuildException {

		List<String> classNames = new ArrayList<String>();
		
		Iterator resourceIterator =  this.resources.iterator();
		while (resourceIterator.hasNext()){
			Resource r = (Resource)resourceIterator.next();
			String name = r.getName().replaceFirst("\\.(java|class)$", "");
			name = name
				.replace(File.separatorChar, '.')
				.replace('/', '.')
				.replace('\\', '.');
			classNames.add(name);
		}

		BaseTestSuite suite = new BaseTestSuite();
		suite.setFolderOut(this.pathOut);
		suite.setRenderer(this.renderer);
		for (String className : classNames) {
			try{
				suite.addTest((Class<? extends BaseTestCase>) Class.forName(className));
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		suite.runTests();

	}

}
