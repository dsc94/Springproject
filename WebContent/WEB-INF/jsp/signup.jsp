<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page session="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sign Up</title>
<style>
</style>
<script type="text/javascript">
function check()
{
if(document.getElementById("pass").value != "" && document.getElementById("pass1").value != "")
	{
	document.getElementById("passwordbelow").innerHTML ="";
	document.getElementById("passwordbelow1").innerHTML="";
	}
else
	{
	if(document.getElementById("pass").value != "")
		document.getElementById("passwordbelow").innerHTML ="";
	if(document.getElementById("pass1").value != "")
		document.getElementById("passwordbelow1").innerHTML ="";
	}
}
function check1()
{
if(document.getElementById("user1").value != "" && document.getElementById("pass1").value != "")
	{
	document.getElementById("userbelow").innerHTML ="";
	document.getElementById("passwordbelow1").innerHTML="";
	}
else
	{
	if(document.getElementById("user1").value != "")
		document.getElementById("userbelow").innerHTML ="";
	if(document.getElementById("pass1").value != "")
		document.getElementById("passwordbelow1").innerHTML ="";
	}
}
function check2()
{
if(document.getElementById("pass").value != "" && document.getElementById("user1").value != "")
	{
	document.getElementById("passwordbelow").innerHTML ="";
	document.getElementById("userbelow").innerHTML="";
	}
else
	{
	if(document.getElementById("pass").value != "")
		document.getElementById("passwordbelow").innerHTML ="";
	if(document.getElementById("user1").value != "")
		document.getElementById("userbelow").innerHTML ="";
	}
}
function signupfunction()
{
	
	if(document.getElementById("user1").value != "")
		{
		
		if(document.getElementById("pass").value != "" && passwordcheck())
		{
			
		if(document.getElementById("pass1").value != "")
	    {
			
			if(document.getElementById("pass").value == document.getElementById("pass1").value )
				{
				return true;
				}
			else
				{
				document.getElementById("passwordbelow1").innerHTML= "* Passwords do not match";
				return false;
				}
	    }
			else
				{
				document.getElementById("passwordbelow1").innerHTML ="* Please enter password again";
				return false;
				}
		}
		else
			{
			if(document.getElementById("pass").value != "")
				{
				document.getElementById("passwordbelow").innerHTML ="Password must be greater than 8 characters";
				return false;
				}
			else
				{
			document.getElementById("passwordbelow").innerHTML ="* Please enter password";
			return false;
				}
			}
		}
	else
		{
		
		document.getElementById("userbelow").innerHTML ="* Please enter Username";
		return false;
		}
}

function passwordcheck()
{
	var x = document.getElementById("pass").value;
	if(x.length > 8)
		{
		return true;
		}
	else
		return false;
}
</script>
<style>
#user1
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
#pass1
{
margin-left : 600px;
margin-top : 20px;
height : 25px;
}
#signupbutton1
{
margin-left : 600px;
margin-top : 20px;
width : 180px;
}
#userbelow
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
#passwordbelow1
{
margin-left : 600px;
margin-top : 2px;
color:red
}
p
{
margin-left : 600px;
}
</style>
</head>
<body>
<p style="color:red;">${error}<p>
<div>
<form method="post" action="http://localhost:8080/LoginSystem/aftersignup" onsubmit="return signupfunction()">
<input id="user1" type="text"  name="signupuserid" onselect="check()"/>
<div id="userbelow"></div>
<input id="pass" type="password"  name="signuppassword" onselect="check1()""/>
<div id="passwordbelow"></div>
<input id="pass1" type="password"  name="signuppassword1" onselect="check2()""/>
<div id="passwordbelow1"></div>
<input id="signupbutton1" type="submit" name="signupbutton1" value="SIGNUP" />
</div>
</form>
</body>
</html>