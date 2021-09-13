/*-
 * #%L
 * Fiji distribution of ImageJ for the life sciences.
 * %%
 * Copyright (C) 2009 - 2021 Fiji developers.
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

import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

/**
 * A very simple plugin for use with the {@link DebugTest}.
 * 
 * @author Johannes Schindelin
 */
public class Dimension_Test_PlugInFilter implements PlugInFilter {
	private int[] dimensions;

	@Override
	public int setup(final String arg, final ImagePlus image) {
		dimensions = image.getDimensions();
		return DOES_ALL;
	}

	@Override
	public void run(final ImageProcessor ip) {
		final GenericDialog gd = new GenericDialog("Test");
		gd.addStringField("output", "");
		gd.showDialog();
		if (gd.wasCanceled()) return;

		final String output = gd.getNextString();
		try {
			final PrintStream out = new PrintStream(output);
			out.println(Arrays.toString(dimensions));
			out.close();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
