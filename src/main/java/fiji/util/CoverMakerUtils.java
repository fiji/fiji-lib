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

package fiji.util;

import java.util.List;

/**
 * Utility methods for Fiji's Cover Maker script.
 * 
 * @author Pavel Tomancak
 * @author Curtis Rueden
 */
public class CoverMakerUtils {

	public static double tileTemplateDifference(final List<int[]> arrays) {
		final int[] pixelst = arrays.get(0);
		final int[] pixelsd = arrays.get(1);
		double sum = 0;
		for (int i = 0; i < pixelst.length; i++) {
			final int t = pixelst[i];
			final int d = pixelsd[i];
			final int red = ((t >> 16) & 0xff) - ((d >> 16) & 0xff);
			final int green = ((t >> 8) & 0xff) - ((d >> 8) & 0xff);
			final int blue = (t & 0xff) - (d & 0xff);
			sum += red * red + green * green + blue * blue;
		}
		return sum;
	}

}
