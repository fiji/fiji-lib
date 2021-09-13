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
package ij.gui;

import java.awt.Graphics;
import java.awt.Shape;


/**
 * Wrapper to extract the java.awt.Shape from a ShapeRoi
 *  
 * @author Ignacio Arganda-Carreras and Johannes Schindelin
 *
 */
public class ShapeRoiHelper extends ShapeRoi {
	/** Generated serial version uid */
	private static final long serialVersionUID = 3683518238872064558L;

	private ShapeRoiHelper() { super((Roi)null); }

	// unfortunately, this method is not public in (old) ImageJ
	public static Shape getShape(ShapeRoi roi) { return roi.getShape(); }

	public static Shape getShape(ShapeRoi roi, Graphics g, int x, int y, double magnification) {
		return roi.getShape();
	}
}
