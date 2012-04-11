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


   <wng:title text="Not Implemented. Please Go Back" align="center" text_color="#fff" background_color="#369">
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
        <b>OK, we told you to go back, but before you do...</b>
       </wng:text>
     <wng:br/>
     <wng:text>
        <wng:css>
         <wng:css_property name="margin-left" value="10px" />
        </wng:css>
     ...you may want to admire our fantastic WNG RackMenu.
    </wng:text>
   </wng:text_block>


    <wng:rack_menu background_color1="#ddd" background_color2="#fff">
     <wng:css>
       <wng:css_property name="font-size" value="12px" />
       <wng:css_property name="font-weight" value="bold" />
      </wng:css>
     <wng:link href="index.htm" text="Home"/>
     <wng:link href="blog.htm" text="Blog"/>
     <wng:link href="dontclick.htm" text="Juicy bits"/>
     <wng:link href="dedication.htm" text="Dedication"/>
    </wng:rack_menu>

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