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
public class LogoDispenser implements ImageDispenser {
	
	
    private Image image;
    
    private String dirName = "images/";     

    public LogoDispenser(WNGDevice device) {
		
	int width = device.getMaxImageWidth();

	if (width <= 165) {
	    image = new Image(dirName+"logo120.gif", "Radio WNG", "120","24"); 
	} else if (width <= 223) {
	    image = new Image(dirName+"logo168.gif", "Radio WNG", "168","32"); 
	} else if (width <= 299) {
	    image = new Image(dirName+"logo224.gif", "Radio WNG", "224","43"); 
	} else {
	    image = new Image(dirName+"logo300.gif", "Radio WNG", "300","60"); 
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
