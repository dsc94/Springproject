<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="javax.servlet.*"%>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*;" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
</head>
<body>
<form method="POST" action="http://localhost:8080/LoginSystem/handleupload" enctype="multipart/form-data">
<input type='file' name='file' />
<input type='submit' name='upload' value='Upload'/>
<%
if(!request.getAttribute("imagestodisplay").toString().equals(""))
{
	String dsc = request.getAttribute("imagestodisplay").toString();
%>
<div><%=dsc%></div>
<%
}
%>
</form>
</body>
</html>