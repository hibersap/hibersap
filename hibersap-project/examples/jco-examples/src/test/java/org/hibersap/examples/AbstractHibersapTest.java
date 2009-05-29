package org.hibersap.examples;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.junit.Before;
import org.w3c.dom.Document;

/*
 * Copyright (C) 2008 akquinet tech@spree GmbH
 *
 * This file is part of Hibersap.
 *
 * Hibersap is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Hibersap is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Hibersap. If
 * not, see <http://www.gnu.org/licenses/>.
 */

/**
 * @author Carsten Erker
 */
public abstract class AbstractHibersapTest {
	@Before
	public void setUp() throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		factory.setNamespaceAware(true); // never forget this!
		DocumentBuilder builder = factory.newDocumentBuilder();
		File file = new File(System.getProperty("user.home"),
				".m2/settings.xml");

		Document doc = builder.parse(file);

		XPath path = XPathFactory.newInstance().newXPath();

		XPathExpression expression = path.compile("//sap.jco.lib.path/text()");

		String result = ((String) expression
				.evaluate(doc, XPathConstants.STRING)).trim();

		String libraryPath = System.getProperty("java.library.path");
		
		if (libraryPath.indexOf(result) < 0) {
			libraryPath += File.pathSeparator + result;
			
			System.setProperty("java.library.path",
					libraryPath);
		}
		
		System.out.println(System.getProperty("java.library.path"));
		// // here, you may set the directory containing the JCo DLL's or Shared
		// Libraries
		// // alternatively, you can add them to this project's root folder
		// File file = new File( "..." );
		// String libPath = System.getProperty( "java.library.path" );
		// libPath = libPath + ";" + file.getPath();
		// System.setProperty( "java.library.path", libPath );

	}
}
