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

/**
 * @author Asres Gizaw Fantayeneh
 * @version 1.0.0, 20/08/2008, Luca Passani
 */
public class SongDescriptor {

	private String title;
	private String author;
	private String id;

	public SongDescriptor(String title, String author, String id) {

		this.title = title;
		this.author = author;
		this.id = id;

	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public String getId() {
		return id;
	}

}
