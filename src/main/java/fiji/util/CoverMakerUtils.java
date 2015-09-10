
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
