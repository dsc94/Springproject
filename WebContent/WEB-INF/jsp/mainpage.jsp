<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page session="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome ${sessionScope.sessionid}</title>
<style>
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
#options
{
margin-left : 1200px;
}
#profileimage
{
background-color: white;
width: 90px;
height : 100px;
border: 1px solid black;
margin-left:5px;
z-index : 0;
}
#profiledisplayname
{
font-color : black;
font-size : 25px;
margin-left : 5px;
}
#image
{
	z-index : 0;
}
.intro
{
    height: 100px;
    width: 100px;
    background-color : white;
    display: block;
    border: 1px solid white;
}
#middle
{
height : 500px;
width : 400px;
background-color : red;
margin-left : 300px;
}
#post
{
height : 100px;
width : 400px;
background-color : yellow;
margin-left : 300px;
}
#status
{
height : 50px;
width : 400px;
background-color : white;
margin-left : 300px;
border : 2px solid black;
}
#postbutton
{
background-color : blue;
margin-left : 650px;
}
</style>
<script>
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
function changeimageoption()
{
	var div = document.createElement("div");
	div.style.width = "30px";
	div.style.height = "70px";
	div.style.background = "grey";
	div.style.color = "white";
	div.innerHTML = "Update profile pic";
	div.onclick = "changeimage";
	div.fontSize = "5px";
	div.zIndex = "1";
	document.getElementById('image').appendChild(div);
}

function changeimage()
{
	window.location = 'http://localhost:8080/LoginSystem/uploadpic';
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
			 // document.writeln(a);
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

function poststatus()
{
	var status = document.getElementById('statustextarea').value;
	xmlhttprequest.onreadystatechange = reloadpage;
	var url = "http://localhost:8080/LoginSystem/processpost?post="+status;
    xmlhttprequest.open('GET',url,true);
	xmlhttprequest.send();
}

function reloadpage()
{
	location.reload();
}

function processlike(idname)
{
	//document.writeln(idname);
	var usernameofpost = idname.textContent;
	var p = idname.parentNode.id;
	//document.writeln(p + "usernameofpost " + usernameofpost);
	xmlhttprequest.onreadystatechange = reloadpage;
	var url = "http://localhost:8080/LoginSystem/processlike?postkey="+p+"&username="+usernameofpost;
    xmlhttprequest.open('GET',url,true);
	xmlhttprequest.send();	
}
<%
String commentsforposts = "";
String userscommented[] = null;
String comments[] = null;
comments = (String[]) request.getAttribute("comments");
userscommented = (String[]) request.getAttribute("userscommented");
if(comments != null)
{
if(comments.length > 0)
{
for(int i=0;i<comments.length;i++)
{
	commentsforposts += "<h5><b>"+userscommented[i]+"</h5></b><h6>"+comments[i]+"</h6>";
}
}
else
{
	String userloggedin =  request.getSession().getAttribute("username").toString();
	commentsforposts = "<h5><b>"+userloggedin+"</h5></b><input type=text id=userloggedincomment placeholder = Your comment here.. />";
}
}
%>
var p;
function displaycomment(idname)
{
    p = idname.parentNode.id;
	var usernameofpost = idname.textContent;
	var node = document.getElementById(p);
	xmlhttprequest.onreadystatechange = getcommentslist;
	var url = "http://localhost:8080/LoginSystem/processcomment?postkey="+p+"&username="+usernameofpost+"&savecomment="+"false"+"&comment="+"";
    xmlhttprequest.open('GET',url,true);
	xmlhttprequest.send();
}
function getcommentslist()
{
	if(xmlhttprequest.readyState == 4 && xmlhttprequest.status == 200)
		{
		var node = document.getElementById(p);
		var divtobeinserted = xmlhttprequest.responseText;
		var json = JSON.parse(divtobeinserted);
		var noofusers = json['userscommented'].length;
		//document.writeln(noofusers);
		if(noofusers == 0)
				 {
			var newcomment = document.createElement("INPUT");
			newcomment.setAttribute('id','newcommentnode');
			newcomment.setAttribute('type','text');
			newcomment.placeholder= "your comment here ...";
			node.appendChild(newcomment);
			var commentbutton = document.createElement("button");
			var buttontext = document.createTextNode("Save Comment");
			commentbutton.appendChild(buttontext);
			commentbutton.onclick=savecomment;
			node.appendChild(commentbutton);
		
				 }
		else
			{
			      
				  for(var i =0;i<noofusers;i++)
					  {
					  if(json['comments'][i] != null)
						  {
						 // document.writeln(json["userscommented"][i]);
						  var whocommented = document.createElement("p");
						  whocommented.textContent = json["userscommented"][i];
						  whocommented.style.color = "blue";
						  node.appendChild(whocommented);
					     var newcomment = document.createElement("INPUT");
						newcomment.setAttribute('type','text');
						newcomment.value = json["comments"][i];
						newcomment.disabled = true;
						node.appendChild(newcomment);
						  }
					  }
				  if(document.getElementById("newcommentnode") != null)
					  {
					  var removenewcommentnode = document.getElementById('newcommentnode');
						removenewcommentnode.parentNode.removeChild(removenewcommentnode);
						var removesavecommentbutton = document.getElementById('savecomment');
						removesavecommentbutton.parentNode.removeChild(removesavecommentbutton);
					  }
				  var newcomment = document.createElement("INPUT");
				  newcomment.setAttribute('id','newcommentnode');
					newcomment.setAttribute('type','text');
					newcomment.placeholder= "your comment here ...";
					node.appendChild(newcomment);
					var commentbutton = document.createElement("button");
					commentbutton.id ="savecomment";
					var buttontext = document.createTextNode("Save Comment");
					commentbutton.appendChild(buttontext);
					commentbutton.onclick=savecomment;
					node.appendChild(commentbutton); 
		}
		}
}

function savecomment()
{
	
	p = this.parentNode.id;
	var childnodes = document.getElementById(p).childNodes;
	var usernameofpost = childnodes[0].textContent;
	var node = document.getElementById(p);
	var comment = document.getElementById("newcommentnode").value;
	//document.writeln(usernameofpost);
	xmlhttprequest.onreadystatechange = getcommentslist;
	var url = "http://localhost:8080/LoginSystem/processcomment?postkey="+p+"&username="+usernameofpost+"&savecomment="+"true"+"&comment="+comment;
    xmlhttprequest.open('GET',url,true);
	xmlhttprequest.send();
	
	
}

function commentslistaftersave()
{
	if(xmlhttprequest.readyState == 4 && xmlhttprequest.status == 200)
	{
		var removenewcommentnode = document.getElementById('newcommentnode');
		removenewcommentnode.parentNode.removeChild(removenewcommentnode);
		var removesavecommentbutton = document.getElementById('savecomment');
		removesavecommentbutton.parentNode.removeChild(removesavecommentbutton);
	var node = document.getElementById(p);
	var divtobeinserted = xmlhttprequest.responseText;
	var json = JSON.parse(divtobeinserted);
	var noofusers = json['userscommented'].length;
	var whocommented = document.createElement("p");
	  whocommented.textContent = json["userscommented"][noofusers-1];
	  whocommented.style.color = "blue";
	  node.appendChild(whocommented);
   var newcomment = document.createElement("INPUT");
	newcomment.setAttribute('type','text');
	newcomment.value = json["comments"][noofusers-1];
	newcomment.disabled = true;
	node.appendChild(newcomment);
	var newcomment = document.createElement("INPUT");
	  newcomment.setAttribute('id','newcommentnode');
		newcomment.setAttribute('type','text');
		newcomment.placeholder= "your comment here ...";
		node.appendChild(newcomment);
		var commentbutton = document.createElement("button");
		var buttontext = document.createTextNode("Save Comment");
		commentbutton.appendChild(buttontext);
		commentbutton.onclick=savecomment;
		node.appendChild(commentbutton); 
	}
}
</script>
</head>
<body>
<div id="verticalbar">
<input id= "searchbar" type="text" name = "searchbox" placeholder = "Enter Username" onkeyup="searchusers()"/>
<select id='options' onchange='selectoptions()'>
<option value='username'>${username}</option>
<option value='settings' >Settings</option>
<option value='logout' >Logout</option>
</select>
</div>
<p>${error}</p>
<div id='profileimage' onmouseover='changeimageoption()' onclick='changeimage()'>
<%
String dsc = null;
String usernames[] = null;
String userposts[] = null;
String postkeys[] = null;
//String userscommented[] = null;
//String comments[] = null;
String divforposts = "";
//String commentsforposts = "";
usernames = (String[]) request.getAttribute("usernames");
userposts = (String[]) request.getAttribute("posts");
//comments = (String[]) request.getAttribute("comments");
//userscommented = (String[]) request.getAttribute("userscommented");
postkeys = (String[]) request.getAttribute("postkeys");
int count = 0;
if(usernames != null)
{
for(int i=0;i<usernames.length;i++)
{
	if(usernames[i] != null)
	{
	String id = "usernameofpost"+count;
	divforposts = divforposts + "<div class = posts id="+postkeys[i]+">"+"<p id="+id+">"+usernames[i]+"</p><p>"+userposts[i]+"</p><input type=button value=like onclick=processlike("+id+") /><input type=button value=comment onclick=displaycomment("+id+") /></div>";
	}
	count++;
}
}
/*if(comments != null)
{
if(comments.length > 0)
{
for(int i=0;i<comments.length;i++)
{
	commentsforposts += "<h5><b>"+userscommented[i]+"</h5></b><h6>"+comments[i]+"</h6>";
}
}
else
{
	String userloggedin =  request.getSession().getAttribute("username").toString();
	commentsforposts = "<h5><b>"+userloggedin+"</h5></b><input type=text id=userloggedincomment placeholder = Your comment here.. />";
}
}*/
if(request.getAttribute("profileimage").equals(""))
{
	 dsc = "https://case.edu/medicine/admissions/media/school-of-medicine/admissions/classprofile.png";
}
else
{
	 dsc = "http://localhost:8080/"+request.getAttribute("profileimage");
}
%>
<img id ='image' src="<%=dsc %>" height="100" width="90" />
</div>
<div id="status">
<textarea id="statustextarea" placeholder = "your status here..." rows ="3" col ="300">
</textarea>
</div>
<input id ="postbutton" type = "button" value = "post" name ="postbutton" onclick="poststatus()"/>
<div id="middle">
<%=divforposts%>
</div>
<h3 id="profiledisplayname"><b>${sessionScope.username1}</b></h3>
<div class='intro'>
<h5 style="color:black">Intro</h5>
<p id='check'></p>
</div>
</body>
</html>