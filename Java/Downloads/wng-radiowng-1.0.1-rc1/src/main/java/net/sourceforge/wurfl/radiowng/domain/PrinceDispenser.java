/*
 * This file is released under the GNU General Public License.
 * Refer to the COPYING file distributed with this package.
 *
 * Copyright (c) 2008-2009 WURFL-Pro SRL
 */
package net.sourceforge.wurfl.radiowng.domain;

import net.sourceforge.wurfl.wng.ImageDispenser;
import net.sourceforge.wurfl.wng.WNGDevice;
import net.sourceforge.wurfl.wng.component.Image;

/**
 * @author Asres Gizaw Fantayeneh
 * @version 1.0.0, 20/08/2008, Luca Passani
 */
public class PrinceDispenser implements ImageDispenser {

	private Image image;

	private String dirName = "images/";

	public PrinceDispenser(WNGDevice device, String index) {

		// Dimension class work with int
		int width = device.getMaxImageWidth();
		int xhtml_support_level = Integer.parseInt(device
				.getCapability("xhtml_support_level"));

		if (xhtml_support_level >= 3) {

			if (width <= 165) {
				image = new Image(dirName + "prince" + index + "_60.jpg",
						"Prince " + index, "60", "60");
			} else if (width <= 223) {
				image = new Image(dirName + "prince" + index + "_84.jpg",
						"Prince " + index, "84", "84");
			} else if (width <= 299) {
				image = new Image(dirName + "prince" + index + "_112.jpg",
						"Prince " + index, "112", "112");
			} else {
				image = new Image(dirName + "prince" + index + "_150.jpg",
						"Prince " + index, "150", "150");
			}
		} else {

			if (width <= 165) {
				image = new Image(dirName + "prince" + index + "s_60.jpg",
						"Prince " + index, "60", "24");
			} else if (width <= 223) {
				image = new Image(dirName + "prince" + index + "s_84.jpg",
						"Prince " + index, "84", "34");
			} else if (width <= 299) {
				image = new Image(dirName + "prince" + index + "s_112.jpg",
						"Prince " + index, "112", "46");
			} else {
				image = new Image(dirName + "prince" + index + "s_150.jpg",
						"Prince " + index, "150", "61");
			}
		}
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
