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
public class BannerDispenser implements ImageDispenser {
	
	
    private Image image;
    
    private String dirName = "images/"; 
    
    private int small_x = 120;
    private int small_y = 21;

    private int medium_x = 168;    
    private int medium_y = 29;    

    private int large_x = 224;
    private int large_y = 38;

    private int xlarge_x = 300;    
    private int xlarge_y = 51;    

    public BannerDispenser(WNGDevice device) {
		
	// Dimension class work with int
	int width = device.getMaxImageWidth();

	if (width <= 165) {
	    image = new Image(dirName+"banner120b.gif", "Design4Mobile", "120","26"); 
	} else if (width <= 223) {
	    image = new Image(dirName+"banner168b.gif", "Design4Mobile", "168","36"); 
	} else if (width <= 299) {
	    image = new Image(dirName+"banner224b.gif", "Design4Mobile", "224","48"); 
	} else {
	    image = new Image(dirName+"banner300b.gif", "Design4Mobile", "300","64"); 
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
