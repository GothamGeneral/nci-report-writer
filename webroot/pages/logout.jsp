
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
HttpSession httpSession=request.getSession(false);
httpSession.invalidate();
%>

<%@ page contentType="text/html;charset=windows-1252"%>

<html>
<head>

<base href="<%=basePath%>"/>

<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0"/>    
<meta http-equiv="keywords" content="NCI Report Writer"/>
<meta http-equiv="description" content="NCI Report Writer"/>

<title>Home</title>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/styleSheet.css" />
<script src="script.js" type="text/javascript"></script>
</head>
<body>

<f:view>

<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">
	
  <%@ include file="/pages/templates/nciHeader.html" %>
	
  <tr>
      <td height="100%" valign="top">
      
           <table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">
			
               <%@ include file="/pages/templates/applicationHeader.xhtml" %>
				
               <tr>
                    <td width="190" valign="top" class="subMenu">
          
<%@ include file="/pages/templates/sideMenu.html" %>
            
                    </td>
                    <td valign="top" width="100%">
                         <table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">
                              <tr>
                                    <td height="20" width="100%" class="mainMenu">
                
<%@ include file="/pages/templates/menuBar1.xhtml" %>
                  
                                    </td>
                              </tr>
                              
              
<!--_____ main content begins _____-->


                              <tr>
                                    <td width="100%" valign="top">
                  
                  <!-- target of anchor to skip menus --><a name="content" />
                  
                                          <table summary="" cellpadding="0" cellspacing="0" border="0" width="600" height="100%">
                    
					        <!-- banner begins -->
					        <tr>
					              <td class="bannerHome"><img src="<%= request.getContextPath() %>/images/bannerHome.gif" height="140"></td>
					        </tr>
					        <!-- banner begins -->

                    <tr>
                      <td height="100%">
                        <!-- target of anchor to skip menus --><a name="content" />
                        <table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">
                          <tr>
                            <td>


<%@ include file="/pages/templates/welcome.html" %>

                            
                            </td>
                            <td valign="top">
                              
                              <!-- sidebar begins -->
                              <table summary="" cellpadding="0" cellspacing="0" border="0" height="100%">
                                
                                
                                
                                <!-- login begins -->
                                <tr>
                                  <td valign="top">
                                  
 
 
<h:form id="loginForm">                                  
                                    <table summary="" cellpadding="2" cellspacing="0" border="0" width="100%" class="sidebarSection">
                                      <tr>
                                        <td class="sidebarTitle" height="20">LOGOUT REPORT WRITER</td>
                                      </tr>
                                      
                                      <tr>
                                        <td class="txtHighlight">You have successfully logged out.</td>
                                      </tr>                                      
                                      
 
                                    </table>
                                    
</h:form>                                    
                                    
                                    
                                  </td>
                                </tr>
                                <!-- login ends -->
                                
                                
                                
<%@ include file="/pages/templates/whatsNews.html" %>
                                
                                
                                
                                <!-- spacer cell begins (keep for dynamic expanding) -->
                                <tr>
                                  <td valign="top" height="100%">
                                    <table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%" class="sidebarSection">
                                       <tr>
                                          <td class="sidebarContent" valign="top">&nbsp;</td>
                                       </tr>
                                    
                                    </table>
				  </td>
			        </tr>
                                <!-- spacer cell ends -->
																
                                    </table>
                                     <!-- sidebar ends -->
                              
                              </td>
                            </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>
<!--_____ main content ends _____-->
              
              <tr>
                <td height="20" width="100%" class="footerMenu">
                
                     <%@ include file="/pages/templates/applicationFooter.html" %>
                  
                </td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td>
    
        <%@ include file="/pages/templates/nciFooter.html" %>
    
    </td>
  </tr>
</table>

</f:view>
</body>
</html>