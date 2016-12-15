<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>About ${SessionScope.username1}</title>
<style>
#fname
{
margin-left : 400px;
margin-top : 250px;
height :25px;
}
#lname
{
margin-left : 400px;
margin-top : 20px;
height : 25px;
}
#date
{
margin-left : 400px;
margin-top : 20px;
height : 25px;
}
#univname
{
margin-left : 400px;
margin-top : 20px;
height : 25px;
}
#edit
{
margin-left : 500px;
margin-top : 20px;
height : 25px;
}
#submit
{
margin-left : 20px;
margin-top : 20px;
height : 25px;
}
</style>
<script>
var xmlhttprequest = new XMLHttpRequest();
function disable()
{
		document.getElementById('fname1').disabled = false;
		document.getElementById('lname1').disabled = false;
		document.getElementById('dob1').disabled = false;
		document.getElementById('univname1').disabled = false;
}

function save()
{
	var  fname = document.getElementById('fname1').value;
	var lname = document.getElementById('lname1').value;
	var dob = document.getElementById('dob1').value;
	var univname = document.getElementById('univname1').value;
	xmlhttprequest.onreadystatechange = saveuserdetails;
	var url = "http://localhost:8080/LoginSystem/saveuserdetails?fname="+fname+"&lname="+lname+"&dob="+dob+"&univname="+univname;
	xmlhttprequest.open('GET',url,true);
	xmlhttprequest.send();
}

function saveuserdetails()
{
	var usernames = new Array();
	if(xmlhttprequest.readyState == 4)
		{
		if(xmlhttprequest.status == 200)
			{
	          window.location = 'http://localhost:8080/LoginSystem/loginform1';
			}
		}
}
</script>
</head>
<body>
<form >
<div id= "fname">First name :&nbsp&nbsp&nbsp&nbsp<input type="text" id= "fname1" placeholder = "sai" name="fname" disabled/>&nbsp&nbsp&nbsp&nbsp</div>
<div id= "lname">Last name :&nbsp&nbsp&nbsp&nbsp<input type="text" id= "lname1" name="lname" placeholder = "devineni" disabled/>&nbsp&nbsp&nbsp&nbsp</div>
<div id= "date">Date of Birth :&nbsp&nbsp&nbsp&nbsp<input type="date" id= "dob1" name="dob" disabled/>&nbsp&nbsp&nbsp&nbsp</div>
<div id= "univname">Studies at :&nbsp&nbsp&nbsp&nbsp<input type="text" id= "univname1" name="univname" placeholder = "western illinois university" disabled/>&nbsp&nbsp&nbsp&nbsp</div>
<input type="hidden" name="hiddenvalue" value="saveuserdetails" />
<input type="button" id="edit" name="edit" value = "Edit"  onclick="disable()"/>
<input type='button' id="submit" name='File' value='Save Changes' onclick="save()"/>
</form>
</body>
</html>