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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class JFrameCommandFinder extends CommandFinderBase {
	JMenuBar menuBar;

	public JFrameCommandFinder(String title, JFrame frame) {
		super(title);
		this.menuBar = frame.getJMenuBar();
	}

	@Override
	public void populateActions() {
		for (int i = 0; i < menuBar.getMenuCount(); i++) {
			JMenu menu = menuBar.getMenu(i);
			populateActions(menu, menu.getLabel());
		}
	}

	protected void populateActions(JMenu menu, String menuLocation) {
		for (int i = 0; i < menu.getItemCount(); i++) {
			JMenuItem item = menu.getItem(i);
			if (item == null)
				continue;
			String location = menuLocation + ">" + item.getLabel();
			if (item instanceof JMenu)
				populateActions((JMenu)item, location);
			else
				actions.add(new JMenuItemAction(item, location));
		}
	}

	protected class JMenuItemAction extends Action {
		JMenuItem menuItem;

		public JMenuItemAction(JMenuItem menuItem, String menuLocation) {
			super(menuItem.getLabel(), menuLocation);
			this.menuItem = menuItem;
		}

		public void run() {
			ActionEvent event = new ActionEvent(menuItem, ActionEvent.ACTION_PERFORMED, label);
			for (ActionListener listener : menuItem.getActionListeners())
				listener.actionPerformed(event);
		}
	}

	public static void main(String[] args) {
		JFrame frame = null;
		for (java.awt.Frame f : ij.WindowManager.getNonImageWindows())
			if (f instanceof JFrame)
				frame = (JFrame)f;
		new JFrameCommandFinder("JFrame Command Finder Demo", frame).setVisible(true);
	}
}
