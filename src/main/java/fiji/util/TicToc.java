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

import ij.IJ;

public class TicToc {
	public static long startTime;

	public static void tic() {
		startTime = System.currentTimeMillis();
	}

	public static void toc() {
		long elapsedTime = System.currentTimeMillis() - startTime;
		IJ.log(String.format("Elapsed time: %d.%03dsec",
					elapsedTime / 1000, elapsedTime % 1000));
	}

	public static void main(String[] args) {
		tic();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		toc();
	}
}
