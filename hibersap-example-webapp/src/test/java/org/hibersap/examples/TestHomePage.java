package org.hibersap.examples;

import junit.framework.TestCase;
import org.apache.wicket.util.tester.WicketTester;
import org.hibersap.examples.ui.SearchCustomersPage;
import org.hibersap.examples.ui.WicketApplication;
import org.junit.Before;
import org.junit.Test;

/**
 * Simple test using the WicketTester
 */
public class TestHomePage
{
	private WicketTester tester;

	@Before
	public void setUp()
	{
		tester = new WicketTester(new WicketApplication());
	}

    @Test
	public void testRenderMyPage()
	{
		//start and render the test page
		tester.startPage(SearchCustomersPage.class);

		//assert rendered page class
		tester.assertRenderedPage(SearchCustomersPage.class);

		//assert rendered label component
//		tester.assertLabel("message", "If you see this message wicket is properly configured and running");
	}
}
