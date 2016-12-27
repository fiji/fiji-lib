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
package fiji.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Test;

/**
 * Tests the {@link NumberParser}.
 * <p>
 * It is a dirty set of tests because each test overrides the JVM's default
 * locale.
 * </p>
 *
 * @author Curtis Rueden
 */
public class NumberParserTest {

	@Test
	public void testFrance() {
		Locale.setDefault(Locale.FRANCE);
		assertDouble(902.3, "902,300");
		assertCommon();
	}

	@Test
	public void testGermany() {
		Locale.setDefault(Locale.GERMANY);
		assertDouble(902.3, "902,300");
		assertCommon();
	}

	@Test
	public void testUS() {
		Locale.setDefault(Locale.US);
		assertDouble(902300.0, "902,300");
		assertCommon();
	}

	private void assertCommon() {
		assertDouble(-203.9, "-203.9");
		assertDouble(902.3, "902.300");
		assertDouble(902300.0, "902300");
		assertDouble(Double.POSITIVE_INFINITY, "Infinity");
		assertDouble(Double.NEGATIVE_INFINITY, "-Infinity");
		final double value = NumberParser.parseDouble( "NaN" );
		assertTrue(Double.isNaN(value));
	}

	private void assertDouble(final double expect, final String string) {
		final double value = NumberParser.parseDouble(string);
		assertEquals(expect, value, 0.0);
	}
}
