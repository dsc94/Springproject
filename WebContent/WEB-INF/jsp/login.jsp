<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page session="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login System</title>
<script type="text/javascript">
function loginfunction()
{
	var x = document.getElementById("user").value;
	var y = document.getElementById("pass").value;
	if((x != "") && (y != ""))
   {
		return true;
   }
	else
   {
	if(document.getElementById("user").value == "")
		{
		document.getElementById("loginbelow").innerHTML = "* Please enter Username";
		return false;
		}
	else
		{
		document.getElementById("passwordbelow").innerHTML = "* Please Enter Password";
		return false;
		}
		}
   }

function signupfunction()
{
	window.location.href="/LoginSystem/signupform";
}
function userfunction()
{
	if(document.getElementById("user").value != "")
		{ 
		document.getElementById("loginbelow").innerHTML="";
		}
}
function passwordfunction()
{
	if(document.getElementById("pass").value != "")
	{ 
	document.getElementById("passwordbelow").innerHTML="";
	}
}
</script>
<style>
#user
{
margin-left : 600px;
margin-top : 250px;
height :25px;
}
#pass
{
margin-left : 600px;
margin-top : 20px;
height : 25px;
}
#loginbut
{
margin-left : 600px;
margin-top : 20px;
width : 180px;
}
#signupbut
{
margin-left : 600px;
margin-top : 20px;
width : 180px;
}
#loginbelow
{
margin-left : 600px;
margin-top : 2px;
color : red
}
#passwordbelow
{
margin-left : 600px;
margin-top : 2px;
color:red
}
</style>
</head>
<body>
<p style="color:red">${errormessage}</p>
<form action="http://localhost:8080/LoginSystem/loginform1" method="post" onsubmit="return loginfunction()">
<div>
<input id="user" type="text"  name="userid" onclick="userfunction()"/>
<div id="loginbelow"></div>
<input id="pass" type="password"  name="password" onclick="passwordfunction()"/>
<div id="passwordbelow"></div>
<input id="loginbut" type="submit" name="loginbutton" value="LOGIN" />
</br>
<input type="button" id="signupbut"  name="signupbutton" value="SIGNUP" onclick="signupfunction()"/>
</div>
</form>
</body>
</html>