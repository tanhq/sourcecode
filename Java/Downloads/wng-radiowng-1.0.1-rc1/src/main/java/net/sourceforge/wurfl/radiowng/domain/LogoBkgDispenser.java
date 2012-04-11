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
public class LogoBkgDispenser implements ImageDispenser {
	
	
    private Image image;
    
    private String dirName = "images/"; 
    

    public LogoBkgDispenser(WNGDevice device) {
		
	// Dimension class work with int
	int width = device.getMaxImageWidth();

	if (width <= 165) {
	    image = null; 
	} else if (width <= 223) {
	    image = null; 
	} else if (width <= 299) {
	    image = new Image(dirName+"logo224_bkg.gif", "", "2","43"); 
	} else {
	    image = new Image(dirName+"logo300_bkg.gif", "", "2","60"); 
	}
    }


    /*
     * (non-Javadoc)
     * @see net.sourceforge.wurfl.wng.ImageDispenser#getImage()
     */
    public Image getImage() {
	
	return image;
    }
       
}
