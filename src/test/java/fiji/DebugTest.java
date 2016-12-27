/*-
 * #%L
 * Fiji distribution of ImageJ for the life sciences.
 * %%
 * Copyright (C) 2009 - 2016 Fiji development team.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package fiji;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assume.assumeTrue;
import ij.plugin.PlugIn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.junit.Test;

/**
 * Verifies that the {@link Debug} class' {@code run} methods work as advertised.
 * 
 * @author Johannes Schindelin
 */
public class DebugTest {
	@Test
	public void runFilterTest() throws Exception {
		final File image = new File("../../images/fiji-logo-1.0-128x128.png");
		assumeTrue(image.exists());
		final File tmp = File.createTempFile("fiji-debug-", ".txt");
		Debug.runFilter(image.getAbsolutePath(), "Dimension Test PlugInFilter", "output=[" + tmp.getAbsolutePath() + "]", true);

		final BufferedReader reader = new BufferedReader(new FileReader(tmp));
		try {
			final String line = reader.readLine();
			assertEquals("[128, 128, 1, 1, 1]", line);
			assertNull(reader.readLine());
		} finally {
			reader.close();
		}
	}

	@Test
	public void runPlugInTest() throws Exception {
		assertEquals("[Hello, world!]", Debug.runPlugIn(MyTest.class.getName(), "Hello, world!", true).toString());
	}

	public static class MyTest implements PlugIn {
		private String value;

		@Override
		public void run(final String arg) {
			value = arg;
		}

		public String toString() {
			return "[" + value + "]";
		}
	}
}
