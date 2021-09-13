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
package fiji.util.gui;

import ij.IJ;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

/** Manual test for {@link GenericDialogPlus}. */
public class GenericDialogPlusMain {

	public static void main(final String[] args) {
		final GenericDialogPlus gd =
			new GenericDialogPlus("GenericDialogPlus Test");
		gd.addFileField("A_file", System.getProperty("ij.dir") + "/jars/ij.jar");
		gd.addDirectoryField("A_directory", System.getProperty("ij.dir") +
			"/plugins");
		gd.addButton("Click me!", new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				IJ.showMessage("You clicked me!");
			}
		});
		final JLabel label = new JLabel("Hello, Ignacio! You're the BEST!");
		final JPanel jp = new JPanel();
		jp.add(label);
		gd.addComponent(jp);
		gd.addMessage("(blush)");
		gd.showDialog();
		if (!gd.wasCanceled()) IJ.showMessage("You chose the file " +
			gd.getNextString() + "\nand the directory " + gd.getNextString());
	}

}
