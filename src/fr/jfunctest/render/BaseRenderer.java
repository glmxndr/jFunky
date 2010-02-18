package fr.jfunctest.render;

import java.io.PrintWriter;

public abstract class BaseRenderer implements Renderer {

	protected PrintWriter out;

	public void setWriter(PrintWriter writer) {
		this.out = writer;
	}

}
