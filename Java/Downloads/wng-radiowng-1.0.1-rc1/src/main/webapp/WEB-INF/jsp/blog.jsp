<%--

    This file is released under the GNU General Public License.
    Refer to the COPYING file distributed with this package.

    Copyright (c) 2008-2009 WURFL-Pro SRL

--%>
<%@ taglib uri="http://wurfl.sourceforge.net/wng" prefix="wng"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<wng:document>
  <%@ include file="header.jsp" %>
  <wng:body>
    <%@ include file="top.jsp" %>
  

   <wng:title text="WNG Blog" align="center" text_color="#fff" background_color="#369">
     <wng:css>
      <wng:css_property name="font-size" value="18px" />
      <wng:css_property name="font-weight" value="bold" />
     </wng:css>
   </wng:title>


 <c:forEach var="blog_entry" items="${blog_posts}">
    <wng:illustrated_item>
        <wng:css>
         <wng:css_property name="font-size" value="14px" />
         <wng:css_property name="margin" value="3px 0px 3px 0px" />
         <wng:css_property name="color" value="#369" />
         <wng:css_property name="border-bottom" value="1px solid #dedede" />
       </wng:css>
       <%-- wng:image imageRetriever="${blog_entry.imageDispenser}" / --%>
       <wng:image src="${blog_entry.image.src}" alt="${blog_entry.image.alt}"
            width="${blog_entry.image.width}" height="${blog_entry.image.height}" />

       <wng:text text_color="#030">
        <wng:css>
         <wng:css_property name="font-weight" value="bold" />
        </wng:css>
        ${blog_entry.title}</wng:text>
       <wng:br />
       <wng:text>${blog_entry.mainText}</wng:text>
       <wng:link href="${blog_entry.blogUrl}" text="...More..."/>

   </wng:illustrated_item>
 </c:forEach>


   <wng:billboard>
     <wng:css>
       <wng:css_property name="border-bottom" value="1px solid #dedede" />
       <wng:css_property name="text-align" value="center" />
       <wng:css_property name="padding" value="0" />
       <wng:css_property name="spacing" value="0" />
     </wng:css>
     <wng:image imageRetriever="${banner}">
       <wng:css>
         <wng:css_property name="margin-left" value="10px" />
         <wng:css_property name="padding" value="0" />
         <wng:css_property name="spacing" value="0" />
       </wng:css>
       <wng:link href="dontclick.htm" />
     </wng:image>
   </wng:billboard>
    
  </wng:body>

</wng:document>