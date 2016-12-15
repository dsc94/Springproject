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
<script>
var a;
var xmlhttprequest = new XMLHttpRequest();
function selectoptions()
{
	var x = document.getElementById('options').value;
	if(x == 'username')
		{
		window.location = 'http://localhost:8080/LoginSystem/aboutme'
		}
	if(x == 'settings')
		{
		window.location = 'http://localhost:8080/LoginSystem/settings'
		}
	if(x == 'logout')
		{
		window.location = 'http://localhost:8080/LoginSystem/logout'
		}
}
function sendrequest()
{
	var buttontype = document.getElementById('buttontype').value;
	if(buttontype == "Add Friend")
		{
		var user1 = document.getElementById('user1').value;
		var user2 = document.getElementById('user2').value;
	    xmlhttprequest.onreadystatechange = getstatus;
		//document.writeln("http://localhost:8080/LoginSystem/updatefriendstable?user1="+user1+"&user2="+user2+"&requesttype=Add Friend");
		var url = "http://localhost:8080/LoginSystem/updatefriendstable?user1="+user1+"&user2="+user2+"&requesttype=Add Friend";
		xmlhttprequest.open('GET',url,true);
		xmlhttprequest.send();
		}
	if(buttontype == "Accept Friend Request")
		{
		var user1 = document.getElementById('user1').value;
		var user2 = document.getElementById('user2').value;
		xmlhttprequest.onreadystatechange = getstatus;
		var url = "http://localhost:8080/LoginSystem/updatefriendstable?user1="+user1+"&user2="+user2+"&requesttype=Accept Friend Request";
		xmlhttprequest.open('GET',url,true);
		xmlhttprequest.send();
		}
}

function getstatus()
{
	if(xmlhttprequest.readyState == 4)
	{
	if(xmlhttprequest.status == 200)
		{
		a = xmlhttprequest.responseText;
		if(a == "true")
			{
			  var b = document.getElementById('buttontype').value;
			  switch(b)
			  {
			  case "Add Friend":
				  document.getElementById('friendrequest').innerHTML = "Friend Request Sent";
				  break;
			  case "Accept Friend Request":
				  document.getElementById('friendrequest').innerHTML = "Friends";
				  break;
			  } 
			}
		}
	}
		}
function searchusers()
{
	var searchkeyword = document.getElementById('searchbar').value;
	if(searchkeyword.length > 0)
		{
		   
		   xmlhttprequest.onreadystatechange = getusers;
		   var url = "http://localhost:8080/LoginSystem/getusers?keyword="+searchkeyword;
		   xmlhttprequest.open('GET',url,true);
		   xmlhttprequest.send();
		}
	else
		{
		//delete the dom
		var divblocks = document.getElementsByClassName('divblocks');
		for(i=0;i<divblocks.length;i++)
			{
			divblocks[i].parentNode.removeChild(divblocks[i]);
			}
		}
}
function getusers()
{
	var usernames = new Array();
	if(xmlhttprequest.readyState == 4)
		{
		if(xmlhttprequest.status == 200)
			{
			  var a  = xmlhttprequest.responseText;
			  var json = JSON.parse(a);
			  var noofusers = json['users'].length;
			  for(var i =0;i<noofusers;i++)
				  {
				  if(json['users'][i] != null)
					  {
					  usernames.push(json['users'][i]);
					  }
				  }
			}
		createdivdom(usernames);
		}
}

function createdivdom(usernames)
{
	var noofdivs = usernames.length;
	for(var i=0;i<noofdivs;i++)
		{
		 var divele = document.createElement("div");
		 divele.className = "divblocks";
		 document.body.appendChild(divele);
		// if(i == 0)
			// {
			 var searchdiv = document.getElementById('searchbar');
			 searchdiv.parentNode.insertBefore(divele,searchdiv.nextSibling);
			 //}
		 divele.style.position = "relative";
		 divele.style.width = "300px";
		 divele.style.marginLeft = "300px"
	     divele.style.top = "5px"
		 divele.style.height = "40px";
		 divele.style.background = "white";
		 divele.style.color = "black";
		 divele.innerHTML = usernames[i];
		 divele.addEventListener('click', function(){
			 document.writeln(this.innerHTML);
			    redirecttouser(this.innerHTML);
			    
			});
		 divele.style.border = "thick solid #FFFFFF";
		 
		}
}

function redirecttouser(username)
{
	window.location = 'http://localhost:8080/LoginSystem/'+username;
}
</script>
<style type="text/css">
#verticalbar
{
background-color: blue;
width: 1400px;
border:6px solid blue;
margin-left : 0px;
martin-top :0px;
}
#searchbar
{
width : 300px;
margin-left : 300px;
position : relative;
}
html,body,head,a,h1,h2,h3,h4,h5,h6,h7,pre,sup,sub { margin: 0px;
    padding: 0px;}
.circlegreen
{
    height: 100px;
    width: 100px;

    display: block;
    border: 1px solid green;

    border-radius: 100px;
}
.circlered
{
    height: 100px;
    width: 100px;

    display: block;
    border: 1px solid red;

    border-radius: 100px;
}
#options
{
margin-left : 1200px;
}
</style>
</head>
<body>
<%
String dsc;
String displaycommonfriends = null;
String displayonline = null;
String commonfriends[] = null;
int onlinefriends[] = null;
int lastonlinetime = 0;
commonfriends = (String[]) request.getAttribute("commonfriends");
onlinefriends = (int[]) request.getAttribute("onlinefriends");
lastonlinetime = Integer.parseInt(request.getAttribute("lastonlinetime").toString());
for(int i=0;i<commonfriends.length ;i++)
{
	if(commonfriends[i] != null)
	{
	displaycommonfriends += "<a href=http://localhost:8080/LoginSystem/"+commonfriends[i]+">"+commonfriends[i]+"</a>";	
	}
}
if(onlinefriends[0] == 1)
{
  displayonline = 	"<div class='circlegreen'></div>";
}
else
{
if(lastonlinetime/(60 * 24) > 0)
{
	lastonlinetime = lastonlinetime/(24 * 60);
	displayonline = 	"<div class='circlered'></div><h3>"+lastonlinetime+" day ago</h3>";
}
else if(lastonlinetime/60 > 0)
{
	lastonlinetime = lastonlinetime/60;
	displayonline = 	"<div class='circlered'></div><h3>"+lastonlinetime+" hour ago</h3>";
}
else
{
	displayonline = 	"<div class='circlered'></div><h3>"+lastonlinetime+" minutes ago</h3>";
}
}
if(!request.getAttribute("imagestodisplay").toString().equals(""))
{
	 dsc = request.getAttribute("imagestodisplay").toString();
	 
}
else
{
    dsc = "There are no images associated";
    
}

%>
<div id="verticalbar">
<input id= "searchbar" type="text" name = "searchbox" placeholder = "Enter Username" onkeyup="searchusers()"/>
<select id='options' onchange='selectoptions()'>
<option value='username'>${user1}</option>
<option value='settings' >Settings</option>
<option value='logout' >Logout</option>
</select>
</div>
<p>Uploaded images of this user are</p>
<div><%=dsc%></div>
<h5>Online Status : </h5><p><%=displayonline%></p>
<input type="hidden" id="user1" value='${user1}'/>
<input type="hidden" id="user2" value='${user2}'/>
<input type="hidden" id="buttontype" value='${buttontype}'/>
<p>Common friends:</p></br>
<div><%=displaycommonfriends%></div>
<button id="friendrequest" type="button" onclick="sendrequest()">${buttontype}</button>
</body>
</html>