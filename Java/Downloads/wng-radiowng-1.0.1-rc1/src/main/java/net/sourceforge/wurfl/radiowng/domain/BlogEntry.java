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

import net.sourceforge.wurfl.wng.component.Image;

/**
 * @author Asres Gizaw Fantayeneh
 * @version 1.0.0, 20/08/2008, Luca Passani
 */
public class BlogEntry {

	private Image image;
	private SimplestImageDispenser imageDispenser;

	private String title;
	private String mainText;
	private String blogUrl;
	private String imageUrl;
	private String imageWidth;
	private String imageHeight;
	private String imageAlt;

	public BlogEntry(String title, String mainText, String blogUrl,
			String imageUrl, String imageWidth, String imageHeight,
			String imageAlt) {

		this.title = title;
		this.mainText = mainText;
		this.blogUrl = blogUrl;
		image = new Image(imageUrl, imageAlt, imageWidth, imageHeight);

		imageDispenser = new SimplestImageDispenser(image);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sourceforge.wurfl.wng.ImageDispenser#getImage()
	 */
	public Image getImage() {

		return image;
	}

	public SimplestImageDispenser getImageDispenser() {

		return imageDispenser;
	}

	public String getTitle() {
		return title;
	}

	public String getMainText() {
		return mainText;
	}

	public String getBlogUrl() {
		return blogUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getImageWidth() {
		return imageWidth;
	}

	public String getImageHeight() {
		return imageHeight;
	}

	public String getImageAlt() {
		return imageAlt;
	}

}
