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



   <wng:title text="Pick Your Song" align="center" text_color="#fff" background_color="#369">
     <wng:css>
      <wng:css_property name="font-size" value="18px" />
      <wng:css_property name="font-weight" value="bold" />
     </wng:css>
   </wng:title>


 <c:forEach var="song" items="${song_list}">
  <wng:list_item>
    <wng:css>
      <wng:css_property name="font-size" value="12px" />
      <wng:css_property name="font-weight" value="bold" />
      <wng:css_property name="border-bottom" value="1px solid #dedede" />
    </wng:css>
    <wng:image src="images/arrow.gif" width="9" height="8" alt="">
     <wng:css>
      <wng:css_property name="margin-left" value="10px" />
     </wng:css>       
    </wng:image>
    <wng:link href="rain.htm?id=${song.id}" text=" ${song.author} - ${song.title}" />
  </wng:list_item>
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

   <%@ include file="footer.jsp" %>
    
  </wng:body>

</wng:document>