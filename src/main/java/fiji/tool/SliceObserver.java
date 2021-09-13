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
package fiji.tool;

import ij.IJ;
import ij.ImageListener;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.ImageWindow;
import ij.process.ImageProcessor;

import java.awt.Component;
import java.awt.Container;
import java.awt.Scrollbar;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class SliceObserver implements AdjustmentListener, ImageListener, WindowListener, MouseWheelListener {
	protected SliceListener listener;

	protected ImagePlus image;
	protected ImageWindow window;
	protected ImageProcessor ip;
	protected int slice;

	public SliceObserver(ImagePlus image, SliceListener listener) {
		this.image = image;
		this.listener = listener;

		register();
	}

	protected boolean notifyIfChanged() {
		boolean changed = false;

		ImageWindow window = image.getWindow();
		if (window != this.window) {
			this.window = window;
			changed = true;
		}

		ImageProcessor ip = image.getProcessor();
		if (ip != this.ip) {
			this.ip = ip;
			changed = true;
		}

		int slice = image.getCurrentSlice();
		if (slice != this.slice) {
			this.slice = slice;
			changed = true;
		}
		if (changed)
			listener.sliceChanged(image);
		return changed;
	}

	public static void register(SliceListener listener) {
		int[] ids = WindowManager.getIDList();
		if (ids != null)
			for (int id : ids)
				new SliceObserver(WindowManager.getImage(id), listener);
	}

	protected void register() {
		ImagePlus.addImageListener(this);

		notifyIfChanged();

		if (window == null)
			return;

		window.addMouseWheelListener(this);

		for (Component child : window.getComponents())
			if (child instanceof Scrollbar)
				((Scrollbar)child).addAdjustmentListener(this);
			else if (child instanceof Container)
				for (Component child2 : ((Container)child).getComponents())
					if (child2 instanceof Scrollbar)
						((Scrollbar)child2).addAdjustmentListener(this);
	}

	public ImagePlus getImagePlus() {
		return image;
	}

	public void unregister() {
		ImagePlus.removeImageListener(this);

		if (window == null)
			return;

		window.removeWindowListener(this);

		window.removeMouseWheelListener(this);

		for (Component child : window.getComponents())
			if (child instanceof Scrollbar)
				((Scrollbar)child).removeAdjustmentListener(this);
			else if (child instanceof Container)
				for (Component child2 : ((Container)child).getComponents())
					if (child2 instanceof Scrollbar)
						((Scrollbar)child2).removeAdjustmentListener(this);
	}

	public void adjustmentValueChanged(AdjustmentEvent e) {
		notifyIfChanged();
	}

	public void imageOpened(ImagePlus image) { }

	public void imageClosed(ImagePlus image) {
		if (image == this.image)
			unregister();
	}

	public void imageUpdated(ImagePlus image) {
		notifyIfChanged();
	}

        public void windowActivated(WindowEvent e) {
		notifyIfChanged();
        }

        public void windowClosed(WindowEvent e) { }
        public void windowClosing(WindowEvent e) { }
        public void windowDeactivated(WindowEvent e) {}
        public void windowDeiconified(WindowEvent e) {}
        public void windowIconified(WindowEvent e) {}
        public void windowOpened(WindowEvent e) {}

	public final void mouseWheelMoved(MouseWheelEvent e) {
		notifyIfChanged();
	}

	public static void main(String[] args) {
		register(new SliceListener() {
			public void sliceChanged(ImagePlus image) {
				IJ.log("position in '" + image.getTitle() + "': " + image.getChannel() + ", " + image.getSlice() + ", " + image.getFrame());
			}
		});
	}
}
