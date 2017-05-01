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
package fiji.util.gui;

import fiji.util.gui.OverlayedImageCanvas.Overlay;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

/**
 * Abstract class dedicated to draw overlayed annotations on a {@link OverlayedImageCanvas}.
 * <p>
 * This simple class offer facilities to specify the {@link Color}, {@link Composite} and
 * {@link Stroke} that will be used to draw the annotation. There is a simple method to implement:
 * {@link #draw(Graphics2D)}, that receive a {@link Graphics2D} that will paint the annotation
 * in image coordinates, so that the annotation "follows" the image when its display canvas
 * is panned or zoomed in/out.
 * 
 * @see OverlayedImageCanvas
 * @see Overlay
 * @author Jean-Yves Tinevez - Sep 2010
 *
 */
public abstract class AbstractAnnotation implements Overlay {

	protected Composite composite = AlphaComposite.getInstance(AlphaComposite.DST);
	protected Color color = Color.YELLOW;
	protected Stroke stroke = new BasicStroke(1);

	@Override
	public void paint(Graphics g, int x, int y, double magnification) {
		final Graphics2D g2d = (Graphics2D)g;
		// Save graphic device original settings
		final AffineTransform originalTransform = g2d.getTransform();
		final Composite originalComposite = g2d.getComposite();
		final Stroke originalStroke = g2d.getStroke();
		final Color originalColor = g2d.getColor();
		g2d.setStroke(stroke);
		// Move graphic device to image coordinates
		final AffineTransform at = new AffineTransform();
		at.scale( magnification, magnification );
		at.translate(-x, -y);
		at.concatenate( originalTransform );
		g2d.setTransform( at );
		// Change graphic device settings 
		g2d.setComposite(composite);
		g2d.setColor(color);
		// Delegate drawing to concrete implementation
		draw(g2d);
		// Restore graphic device original settings
		g2d.setTransform( originalTransform );
		g2d.setComposite(originalComposite);
		g2d.setStroke(originalStroke);
		g2d.setColor(originalColor);
	}

	/*
	 * GETTERS / SETTERS 
	 */
	
	@Override
	public void setComposite(Composite composite) { this.composite = composite; }
	public void setColor(Color color) { this.color = color; }
	public void setStroke(Stroke stroke) { this.stroke = stroke; }
	
	/*
	 * ABSTRACT METHODS
	 */
	
	/**
	 * Draw this annotation on the {@link OverlayedImageCanvas}.
	 * <p>
	 * The {@link Graphics2D} received in argument is transformed so that drawing is made
	 * in image coordinates (taking into account image magnification and panning), and 
	 * its color, stroke and composite are set with the fields of this annotation.
	 * When this method returns, the graphic device has its original settings restored.
	 * 
	 * @param g2d  the transformed graphic device to draw with
	 */
	public abstract void draw(Graphics2D g2d);

}
