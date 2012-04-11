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


   <wng:title text="${capabilities.brand_name}  ${capabilities.model_name}" align="center" text_color="#fff" background_color="#369">
     <wng:css>
      <wng:css_property name="font-size" value="18px" />
      <wng:css_property name="font-weight" value="bold" />
     </wng:css>
   </wng:title>
  

  <wng:list_item>
    <wng:css>
      <wng:css_property name="font-size" value="10px" />
      <wng:css_property name="font-weight" value="bold" />
      <wng:css_property name="border-bottom" value="1px solid #dedede" />
    </wng:css>
    <wng:image src="images/arrow.gif" width="9" height="8" alt=""/>
    <wng:text><b>resolution_width</b> = ${capabilities.resolution_width} </wng:text>
  </wng:list_item>

  <wng:list_item>
    <wng:css>
      <wng:css_property name="font-size" value="10px" />
      <wng:css_property name="font-weight" value="bold" />
      <wng:css_property name="border-bottom" value="1px solid #dedede" />
    </wng:css>
    <wng:image src="images/arrow.gif" width="9" height="8" alt=""/>
    <wng:text><b>max_image_width</b> = ${capabilities.max_image_width} </wng:text>
  </wng:list_item>

  <wng:list_item>
    <wng:css>
      <wng:css_property name="font-size" value="10px" />
      <wng:css_property name="font-weight" value="bold" />
      <wng:css_property name="border-bottom" value="1px solid #dedede" />
    </wng:css>
    <wng:image src="images/arrow.gif" width="9" height="8" alt=""/>
    <wng:text><b>xhtml_support_level</b> = ${capabilities.xhtml_support_level} </wng:text>
  </wng:list_item>


  <wng:list_item>
    <wng:css>
      <wng:css_property name="font-size" value="10px" />
      <wng:css_property name="font-weight" value="bold" />
      <wng:css_property name="border-bottom" value="1px solid #dedede" />
    </wng:css>
    <wng:image src="images/arrow.gif" width="9" height="8" alt=""/>
    <wng:text><b>wng markup</b> = ${device.markUp} </wng:text><wng:br/>
  </wng:list_item>

   <%@ include file="footer.jsp" %>
    
  </wng:body>

</wng:document>