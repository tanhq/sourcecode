/*
 * This file is released under the GNU General Public License.
 * Refer to the COPYING file distributed with this package.
 *
 * Copyright (c) 2008-2009 WURFL-Pro srl
 */
package net.sourceforge.wurfl.wng.renderer;

/**
 * @author Filippo De Luca
 * 
 * @version $Id$
 */
public interface RenderedComponent {
	
	RenderedComponent EMPTY = new RenderedComponent() {
		public String getMarkup() {
			return "";
		}
	};

	String getMarkup();

}
