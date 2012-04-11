/*
 * This file is released under the GNU General Public License.
 * Refer to the COPYING file distributed with this package.
 *
 * Copyright (c) 2008-2009 WURFL-Pro SRL
 */
package net.sourceforge.wurfl.radiowng.web;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.wurfl.radiowng.domain.BannerDispenser;
import net.sourceforge.wurfl.radiowng.domain.BlogEntry;
import net.sourceforge.wurfl.radiowng.domain.LogoBkgDispenser;
import net.sourceforge.wurfl.radiowng.domain.LogoDispenser;
import net.sourceforge.wurfl.radiowng.domain.PrinceDispenser;
import net.sourceforge.wurfl.radiowng.domain.SongDescriptor;
import net.sourceforge.wurfl.wng.ImageDispenser;
import net.sourceforge.wurfl.wng.WNGDevice;

import org.apache.commons.lang.StringUtils;

/**
 * Servlet implementation class for Servlet: RadioWng
 * 
 */
public class RadioWng extends javax.servlet.http.HttpServlet implements
		javax.servlet.Servlet {

	static final long serialVersionUID = 1L;

	private static final String JSP_PREFIX = "WEB-INF/jsp";

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public RadioWng() {
		super();
	}

	public void init() throws ServletException {

	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
	 * HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		
		WNGDevice device = resolveDevice(request);
		
		String jspFile = request.getServletPath();
		String fullName = null;
		
		

		if (StringUtils.isEmpty(jspFile)) {
			jspFile = "index.htm";
		}

		int endIndex = jspFile.lastIndexOf(".htm");
		jspFile = jspFile.substring(0, endIndex);

		log("jspFile: " + jspFile);

		fullName = JSP_PREFIX + jspFile + ".jsp";

		log("fullName: " + fullName);

		// logo and banner pix
		ImageDispenser logo = new LogoDispenser(device);
		ImageDispenser logo_bkg = new LogoBkgDispenser(device);
		ImageDispenser banner = new BannerDispenser(device);
		request.setAttribute("logo", logo);
		request.setAttribute("logo_bkg", logo_bkg);
		request.setAttribute("banner", banner);

		// "Business Logic" for the Blog part ;)
		if (jspFile.contains("blog")) {

			ArrayList blogPosts = new ArrayList();

			BlogEntry be = new BlogEntry(
					"Beppe's Viewpoint",
					"How many blogs must a man write up, before they start reading them? the answer my friend is",
					"dontclick.htm?id=1", "images/beppe36.jpg", "36", "36",
					"Beppe");
			blogPosts.add(be);

			be = new BlogEntry(
					"Ilona has the story",
					"I was shaking my ass in the streets this morning. Just walked in and it's early mornin. Bump bump till the break of dawning. It don't stop till the early morning",
					"dontclick.htm?id=2", "images/ilona36.jpg", "36", "36",
					"Ilona");
			blogPosts.add(be);

			be = new BlogEntry(
					"Will someone ever let me speak?",
					"What the hell am I doing here? I dont belong here.I dont care if it hurts. I want to have control. I want a perfect body. I want a perfect soul.",
					"dontclick.htm?id=3", "images/silvio36.jpg", "36", "36",
					"Silvio");
			blogPosts.add(be);

			be = new BlogEntry(
					"Entertainment?",
					"Here is the place where the feeling grows, You gotta get high before you taste the lows, so come on. Let me-e entertain you.",
					"dontclick.htm?id=4", "images/romano36.jpg", "36", "36",
					"Romano");
			blogPosts.add(be);

			be = new BlogEntry(
					"Fame",
					"Baby, look at me and tell me what you see. You ain't seen the best of me yet. Give me time, I'll make you forget the rest. I got more in me, and you can set it free. I can catch the moon in my hand. Don't you know who I am?",
					"dontclick.htm?id=5", "images/sofia36.jpg", "36", "36",
					"Sofia");
			blogPosts.add(be);

			request.setAttribute("blog_posts", blogPosts);
		}

		// "Business Logic" for the Song selection part ;)
		if (jspFile.contains("picksong")) {

			ArrayList songList = new ArrayList();

			SongDescriptor sd = new SongDescriptor("Different Kind of Love",
					"Hard Rain", "34244");
			songList.add(sd);
			sd = new SongDescriptor("Light Rain", "Dark Fire", "6465");
			songList.add(sd);
			sd = new SongDescriptor("Howlin Rain",
					"Dancers at the end of time", "75886");
			songList.add(sd);
			sd = new SongDescriptor("Prince", "Purple Rain", "8655685");
			songList.add(sd);
			sd = new SongDescriptor("Madonna", "Rain", "85858");
			songList.add(sd);
			sd = new SongDescriptor("Enya", "Rain", "856867");
			songList.add(sd);
			sd = new SongDescriptor("Eurythmics", "Here comes the Rain Again",
					"858668");
			songList.add(sd);

			request.setAttribute("song_list", songList);

		}

		// "Business Logic" for the Purple Rain part
		if (jspFile.contains("rain")) {

			ImageDispenser prince1 = new PrinceDispenser(device, "1");
			ImageDispenser prince2 = new PrinceDispenser(device, "2");
			request.setAttribute("prince1", prince1);
			request.setAttribute("prince2", prince2);
		}
		
		
		request.setAttribute("sample", new String("Foo"));

		request.getRequestDispatcher(fullName).forward(request, response);
	}

	/**
	 * @param request
	 * @return
	 * @throws ServletException 
	 */
	private WNGDevice resolveDevice(HttpServletRequest request) throws ServletException {
		WNGDevice device = (WNGDevice)request.getAttribute("device");
		
		if(device == null){
			throw new ServletException("The request do not contain device");
		}
		
		return device;
	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
	 * HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}