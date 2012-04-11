<%--

    This file is released under the GNU General Public License.
    Refer to the COPYING file distributed with this package.

    Copyright (c) 2008-2009 WURFL-Pro SRL

--%>
    <wng:header>
     <wng:css>
       <wng:css_property name="color" value="#006699" />
       <wng:css_property name="background-image" value="url(${logo_bkg.image.src})"
                applies_to="${capabilities.max_image_width gt 223}" />
       <wng:css_property name="background-repeat" value="repeat-x"
                applies_to="${capabilities.max_image_width gt 223}" />
     </wng:css>
     <wng:image imageRetriever="${logo}">
       <wng:link href="index.htm"/>
     </wng:image>
    </wng:header>

