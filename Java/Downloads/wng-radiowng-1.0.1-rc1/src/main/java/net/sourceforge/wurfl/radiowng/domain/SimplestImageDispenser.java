/*
 * This file is released under the GNU General Public License.
 * Refer to the COPYING file distributed with this package.
 *
 * Copyright (c) 2008-2009 WURFL-Pro SRL
 */
package net.sourceforge.wurfl.radiowng.domain;

/* 
 * This file is released under the GNU General Public License. 
 * Refer to the COPYING file distributed with this package.
 *
 * Copyright (c) 2008 WURFL-Pro S.r.l.
 */

import net.sourceforge.wurfl.wng.ImageDispenser;
import net.sourceforge.wurfl.wng.component.Image;

/**
 * @author Asres Gizaw Fantayeneh
 * @version 1.0.0, 20/08/2008, Luca Passani
 */
public class SimplestImageDispenser implements ImageDispenser {

	private Image image;

	public SimplestImageDispenser(Image image) {
		this.image = image;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sourceforge.wurfl.wng.ImageDispenser#getImage()
	 */
	public Image getImage() {

		return image;
	}

}
