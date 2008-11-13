package org.hibersap.examples;

import org.junit.Before;

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
	public void setUp() {
		// // here, you may set the directory containing the JCo DLL's or Shared
		// Libraries
		// // alternatively, you can add them to this project's root folder
		// File file = new File( "..." );
		// String libPath = System.getProperty( "java.library.path" );
		// libPath = libPath + ";" + file.getPath();
		// System.setProperty( "java.library.path", libPath );
	}
}
