package fr.jfunctest.render;

import java.io.PrintWriter;

import fr.jfunctest.BaseTestCase;

public interface Renderer {

	void setWriter(PrintWriter writer);
	void render(BaseTestCase testCase);
	
}
