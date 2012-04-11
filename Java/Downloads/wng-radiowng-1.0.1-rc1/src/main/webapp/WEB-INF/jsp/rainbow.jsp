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


   <wng:title text="Test Grid Element" align="center" text_color="#fff" background_color="#369">
     <wng:css>
      <wng:css_property name="font-size" value="18px" />
      <wng:css_property name="font-weight" value="bold" />
     </wng:css>
   </wng:title>
  
   <wng:grid_menu>
     <wng:grid_cell>
       <wng:image imageRetriever="${prince1}"/>
     </wng:grid_cell>

     <wng:grid_cell>
       <wng:image imageRetriever="${prince2}"/>
     </wng:grid_cell>
     <wng:grid_cell>
       <wng:image imageRetriever="${prince1}"/>
     </wng:grid_cell>

     <wng:grid_cell>
       <wng:image imageRetriever="${prince2}"/>
     </wng:grid_cell>
     <wng:grid_cell>
       <wng:image imageRetriever="${prince1}"/>
     </wng:grid_cell>

     <wng:grid_cell>
       <wng:image imageRetriever="${prince2}"/>
     </wng:grid_cell>
     <wng:grid_cell>
       <wng:image imageRetriever="${prince1}"/>
     </wng:grid_cell>

     <wng:grid_cell>
       <wng:image imageRetriever="${prince2}"/>
     </wng:grid_cell>
     <wng:grid_cell>
       <wng:image imageRetriever="${prince1}"/>
     </wng:grid_cell>

     <wng:grid_cell>
       <wng:image imageRetriever="${prince2}"/>
     </wng:grid_cell>
     <wng:grid_cell>
       <wng:image imageRetriever="${prince1}"/>
     </wng:grid_cell>

     <wng:grid_cell>
       <wng:image imageRetriever="${prince2}"/>
     </wng:grid_cell>
     <wng:grid_cell>
       <wng:image imageRetriever="${prince1}"/>
     </wng:grid_cell>

     <wng:grid_cell>
       <wng:image imageRetriever="${prince2}"/>
     </wng:grid_cell>

   </wng:grid_menu>

   <%@ include file="footer.jsp" %>
    
  </wng:body>

</wng:document>