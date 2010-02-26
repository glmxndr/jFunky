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
package jfunky.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import jfunky.BaseTestCase;



/**
 * 
 * Use this annotation on methods of the {@link BaseTestCase} classes
 * when you want the method to be run as a test.
 * 
 * Note that the method must be public, and that if it already has
 * either a {@link Before}, an {@link After} or an {@link Ignore} annotation,
 * it will not be skipped.
 * 
 * @author G.Andrieu
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Test {

}
