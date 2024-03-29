<%--

    This file is released under the GNU General Public License.
    Refer to the COPYING file distributed with this package.

    Copyright (c) 2008-2009 WURFL-Pro SRL

--%>
<%@ taglib uri="http://wurfl.sourceforge.net/wng" prefix="wng"%>
<wng:document>
  <%@ include file="header.jsp" %>
  <wng:body>
    <%@ include file="top.jsp" %>


   <wng:title text="Pick a Song" align="center" text_color="#fff" background_color="#369">
     <wng:css>
      <wng:css_property name="font-size" value="18px" />
      <wng:css_property name="font-weight" value="bold" />
     </wng:css>
   </wng:title>
  
   <wng:text_block>
      <wng:css>
       <wng:css_property name="font-size" value="14px" />
       <wng:css_property name="border-bottom" value="1px solid #dedede" />
      </wng:css>
     <wng:text>
        <wng:css>
         <wng:css_property name="margin-left" value="10px" />
        </wng:css>
        <b>Dedicate a song to your loved ones</b></wng:text>
     <wng:br/>
     <wng:text>
        <wng:css>
         <wng:css_property name="margin-left" value="10px" />
        </wng:css>
     Choose a song from our archive and send a dedication. 
     After 11PM  we will play as many as possible of the songs requested by 
     listeners and read their dedications to the whole nation.</wng:text>
   </wng:text_block>


   <wng:form action="picksong.htm" method="post">
     <wng:css>
      <wng:css_property name="border-bottom" value="1px solid #dedede;" />
     </wng:css>
     <wng:text css_ref="label" text="Artist or Title"/><wng:br/>

     <wng:input css_ref="input" type="text" value="" name="artist_or_title" />
     <wng:input type="submit" value="Search" css_ref="input"/>
   </wng:form>



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