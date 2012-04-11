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
  
    <wng:list_item>
        <wng:css>
         <wng:css_property name="font-size" value="10px" />
         <wng:css_property name="font-family" value="arial, sans-serif" />
         <wng:css_property name="color" value="#222" />
         <wng:css_property name="border-bottom" value="1px solid #dedede" />
       </wng:css>
       <wng:link href="dontclick.htm" text="On Air: Morning Show">
        <wng:css>
         <wng:css_property name="margin-left" value="10px" />

         <c:if test="${capabilities.max_image_width gt 164}">
            <wng:css_property name="font-size" value="12px" />
         </c:if>
         <c:if test="${capabilities.max_image_width gt 219}">
            <wng:css_property name="font-size" value="14px" />
         </c:if>
         <c:if test="${capabilities.max_image_width gt 299}">
            <wng:css_property name="font-size" value="18px" />
         </c:if>

        </wng:css>
       </wng:link>
       <wng:br/>

       <wng:text>
         <wng:css>
           <wng:css_property name="margin-left" value="10px" />
           <wng:css_property name="padding" value="2px" />
           <wng:css_property name="font-weight" value="bold" />
           <wng:css_property name="font-family" value="Verdana, Arial" />
         </wng:css>
        WNG Live...
      </wng:text>
   </wng:list_item>

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



    <wng:list_item>
        <wng:css>
         <wng:css_property name="font-size" value="10px" />
         <wng:css_property name="font-family" value="arial, sans-serif" />
         <wng:css_property name="color" value="#222" />
         <wng:css_property name="border-bottom" value="1px solid #dedede" />
       </wng:css>
       <wng:link href="blog.htm" text="WNG Blog">
        <wng:css>
         <wng:css_property name="margin-left" value="10px" />
         <wng:css_property name="font-size" value="large" />
        </wng:css>
       </wng:link>
       <wng:br/>

       <wng:text>
         <wng:css>
           <wng:css_property name="margin-left" value="10px" />
           <wng:css_property name="padding" value="2px" />
           <wng:css_property name="font-weight" value="bold" />
           <wng:css_property name="font-family" value="Verdana, Arial" />
         </wng:css>
        We'll make you laugh. We'll make you cry....
      </wng:text>
   </wng:list_item>


    <wng:list_item>
        <wng:css>
         <wng:css_property name="font-size" value="10px" />
         <wng:css_property name="font-family" value="arial, sans-serif" />
         <wng:css_property name="color" value="#222" />
         <wng:css_property name="border-bottom" value="1px solid #dedede" />
       </wng:css>
       <wng:link href="dontclick.htm" text="Fun Parts">
        <wng:css>
         <wng:css_property name="margin-left" value="10px" />
         <wng:css_property name="font-size" value="large" />
        </wng:css>
       </wng:link>
       <wng:br/>

       <wng:text>
         <wng:css>
           <wng:css_property name="margin-left" value="10px" />
           <wng:css_property name="padding" value="2px" />
           <wng:css_property name="font-weight" value="bold" />
           <wng:css_property name="font-family" value="Verdana, Arial" />
         </wng:css>
       Listen to the juciest bits again...
      </wng:text>
   </wng:list_item>


    <wng:list_item>
        <wng:css>
         <wng:css_property name="font-size" value="10px" />
         <wng:css_property name="font-family" value="arial, sans-serif" />
         <wng:css_property name="color" value="#222" />
         <wng:css_property name="border-bottom" value="1px solid #dedede" />
       </wng:css>
       <wng:link href="dedication.htm" text="This one goes out to...">
        <wng:css>
         <wng:css_property name="margin-left" value="10px" />
         <wng:css_property name="font-size" value="large" />
        </wng:css>
       </wng:link>
       <wng:br/>

       <wng:text>
         <wng:css>
           <wng:css_property name="margin-left" value="10px" />
           <wng:css_property name="padding" value="2px" />
           <wng:css_property name="font-weight" value="bold" />
           <wng:css_property name="font-family" value="Verdana, Arial" />
         </wng:css>
       Dedicate a song to your loved ones...
      </wng:text>
   </wng:list_item>

	
	<wng:list_item>
        <wng:css>
         <wng:css_property name="font-size" value="10px" />
         <wng:css_property name="font-family" value="arial, sans-serif" />
         <wng:css_property name="color" value="#222" />
         <wng:css_property name="border-bottom" value="1px solid #dedede" />
       </wng:css>
       <wng:link href="rainbow.htm" text="Rainbow">
        <wng:css>
         <wng:css_property name="margin-left" value="10px" />
         <wng:css_property name="font-size" value="large" />
        </wng:css>
       </wng:link>
       <wng:br/>

       <wng:text>
         <wng:css>
           <wng:css_property name="margin-left" value="10px" />
           <wng:css_property name="padding" value="2px" />
           <wng:css_property name="font-weight" value="bold" />
           <wng:css_property name="font-family" value="Verdana, Arial" />
         </wng:css>
       Some picture of the king...
      </wng:text>
   </wng:list_item>
	


   <wng:title text="Log in to WNG" align="center" text_color="#fff" background_color="#369">
     <wng:css>
      <wng:css_property name="font-size" value="18px" />
      <wng:css_property name="font-weight" value="bold" />
     </wng:css>
   </wng:title>

   <wng:form action="dontclick.htm" method="post">
     <wng:css>
      <wng:css_property name="border-bottom" value="1px solid #dedede;" />
     </wng:css>
     <wng:text css_ref="label" text="Username:"/>
     <wng:input css_ref="input" type="text" value="" name="username" />
     <wng:br />

     <wng:text css_ref="label" text="Password:"/>
     <wng:input css_ref="input" type="text" value="" name="password" />
     <wng:br />
     <wng:input type="submit" value="Log in" css_ref=".input"/>
   </wng:form>
   <%@ include file="footer.jsp" %>
    
  </wng:body>

</wng:document>