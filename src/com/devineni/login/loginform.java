package com.devineni.login;

import java.io.File;
import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.sun.scenario.effect.Blend.Mode;

//import com.devineni.registration.form;


@Controller
public class loginform {
	@Autowired
	DataSource dataSource;
	@RequestMapping(value="/loginform")
	public String  initialpage(@CookieValue(value = "sessionid",defaultValue = "initial") String sessionid,HttpServletRequest request,ModelMap model)
	{
		if(sessionid != null && !sessionid.equals("initial"))
		{
			System.out.println("dscccccccccc");
			model.addAttribute("sessionid",sessionid);
			HttpSession session = request.getSession();
			
			JdbcTemplate jdbc = new JdbcTemplate(dataSource);
			String sql = "select count(*) from userimages where username="+"'"+sessionid+"'" +"and profileflag=" + 1;
			if(jdbc.queryForInt(sql) >= 1)
			{
			    sql = "select filelocation from userimages where username="+"'"+sessionid+"'" +"and profileflag=" + 1;
				String filelocation = jdbc.queryForObject(sql,String.class);
			   	request.setAttribute("profileimage",filelocation);
			}
			else
			{
				
				request.setAttribute("profileimage","");
			}
			session.setAttribute("username", sessionid); 
			return "mainpage";
		}
		else
		{
		return "login";
		}
	}
	@RequestMapping(value="/loginform1")
	public String  login(@ModelAttribute("login")login login,ModelMap model,HttpServletRequest request,HttpServletResponse response)
	{
		String returned="";
		String usernames[] = null;
		String postkeys[] = null;
		String posts[] = null;
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);
		String username = login.getUserid();
		String password = login.getPassword();
		if(username == null || password == null)
		{
			model.addAttribute("errormessage","Something went  wrong");
			returned = "login";
		}
		String sql = "select passsword from users where username="+"'"+username+"'"; 
		String sql1 = "select count(*) from users where username="+"'"+username +"'"; 
		try
		{
			    System.out.println("try block");
				String password1 = jdbc.queryForObject(sql,String.class);
				int count = jdbc.queryForInt(sql1);
				if(password.equals(password1) && count >= 1)
				{
					request.getSession().setAttribute("username1",username);
					Cookie sessionid = new Cookie("sessionid",username);
					sessionid.setMaxAge(100000);
					sessionid.setHttpOnly(true);
					response.addCookie(sessionid);
					System.out.println("dsc3");
					sql = "select count(*) from userimages where username="+"'"+username+"'" +"and profileflag=" + 1;
					if(jdbc.queryForInt(sql) > 0)
					{
						sql = "select filelocation from userimages where username="+"'"+username+"'" +"and profileflag=" + 1;
					    String filename = jdbc.queryForObject(sql,String.class);
						System.out.println("dsc");
						String filelocation = jdbc.queryForObject(sql,String.class);
					   	request.setAttribute("profileimage",filelocation);
					}
					else
					{
						System.out.println("dsc1");
						request.setAttribute("profileimage","");
					}
					sql = "select count(*) from onlinestatus where username="+"'"+username+"'";
				    count = jdbc.queryForInt(sql);
				    System.out.println(count);
					if(count == 0)
					{
					DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					Date d = new Date();
				    String date = dateFormat.format(d);
				    System.out.println(date);
					sql = "insert into onlinestatus(username,onlinestatus,lastonlinetime) values (" + "'" + username + "'" + "," 
							 + "'" +  1 + "'" + "," + "'" + date + "'" + ");";
					jdbc.execute(sql);
					}
					else
					{
						DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						Date d = new Date();
					    String date = dateFormat.format(d);
						sql = "update onlinestatus set onlinestatus = 1  where username ="+"'"+username +"'";
						jdbc.execute(sql);
						sql = "update onlinestatus set lastonlinetime = " + "'" + date + "'" + "where username = "+"'"+username +"'";
						jdbc.execute(sql);
					}
					model.addAttribute("username", username);
					returned =  "mainpage";
				}
				else
				{
					System.out.println("username or password is wrong");
					model.addAttribute("errormessage","username or password entered  is wrong");
					returned = "login";
				}
		}
		catch(Exception e)
		{
			model.addAttribute("errormessage", e.getStackTrace());
		}
		System.out.println("returned " + returned);
		String userfriends[] = null;
		int count = 0;
	   	sql = "select count(*) from friends where user1="+"'"+username+"'"+" or user2="+"'"+username+"'";
		int friendscount = jdbc.queryForInt(sql);
		System.out.println(friendscount);
		if(friendscount > 0)
		{
			userfriends = new String[friendscount];
		}
		sql = "select count(*) from  friends where user1="+"'"+username+"'";
		int dsccount = jdbc.queryForInt(sql);
		System.out.println("souravvvv" + dsccount);
		if(dsccount > 0)
		{
		sql = "select user2 from friends where user1="+"'"+username+"'";
		List<Map<String, Object>> rows = jdbc.queryForList(sql);
		count = 0;
		for (Map row : rows) 
		{
			System.out.println("counttchaitanyaaaaaa"+count);
			userfriends[count] = (String) row.get("user2");
			count++;
		}
		}
		
		sql = "select count(*) from  friends where user2="+"'"+username+"'";
		dsccount = jdbc.queryForInt(sql);
		System.out.println("gangulyyy" + dsccount);
		if(dsccount > 0)
		{
		sql = "select user1 from friends where user2="+"'"+username+"'";
		List<Map<String, Object>> rows = jdbc.queryForList(sql);
		for (Map row : rows) 
		{
			System.out.println("counttttchaitanyaaaaaa"+count);
			userfriends[count] = (String) row.get("user1");
			count++;
		}
		}
		count = 0;
		
		usernames = new String[userfriends.length * 5];
		postkeys = new String[userfriends.length * 5];
		posts = new String[userfriends.length * 5];
		sql = "select count(*) from userposts where username = " +"'" + username +"'";
		int sqlcount = jdbc.queryForInt(sql);
		if(sqlcount > 0)
		{
		sql = "select username,postkey,status from userposts where username = " +"'" + username +"'";
		List<Map<String, Object>> rows = jdbc.queryForList(sql);
		System.out.println("okayyyy"+rows.get(0));
		for (Map row : rows) 
		{  
			usernames[count] = (String) row.get("username");
			postkeys[count] = (String) row.get("postkey");
			posts[count] = (String) row.get("status");
			count++;
		}
		}
		for(int i=0;i<userfriends.length;i++)
		{
			//System.out.println("chaitanyaaaaaa"+userfriends[i]);
			
			sql = "select count(*) from userposts where username = " +"'" + userfriends[i] +"'";
			sqlcount = jdbc.queryForInt(sql);
			if(sqlcount > 0)
			{
			sql = "select username,postkey,status from userposts where username = " +"'" + userfriends[i] +"'";
			List<Map<String, Object>> rows = jdbc.queryForList(sql);
			System.out.println("okayyyy"+rows.get(0));
			for (Map row : rows) 
			{  
				usernames[count] = (String) row.get("username");
				postkeys[count] = (String) row.get("postkey");
				posts[count] = (String) row.get("status");
				count++;
			}
			}
		}
		request.setAttribute("usernames", usernames);
		request.setAttribute("postkeys", postkeys);
		request.setAttribute("posts", posts);
		model.addAttribute("username",username);
		return returned;
		}
		/*else
		{
			JdbcTemplate jdbc = new JdbcTemplate(dataSource);
			String username =  request.getSession().getAttribute("username1").toString();
			String fname = userdetails.getFname();
			String lname = userdetails.getLname();
			String dob = userdetails.getDob();
			String univname = userdetails.getUnivname();
			String sql = "select count(*) from userdetails where username = "+ "'" + username +"'";
			int count = jdbc.queryForInt(sql);
			if (count > 0)
			{
				sql = "update userdetails set fname = " + fname;
				jdbc.execute(sql);
				sql = "update userdetails set lname = " + lname;
				jdbc.execute(sql);
				sql = "update userdetails set univname = " + univname;
				jdbc.execute(sql);
				sql = "update userdetails set dob = " + dob;
				jdbc.execute(sql);
			}
			else
			{
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date d = new Date();
				try
				{
				d = dateFormat.parse(dob);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				sql = "insert into userdetails(username,fname,lname,dov,univname) values(" +"'" + username + "'" + "," 
						+"'" + fname + "'" + ","
						+"'" + lname + "'" + ","
						+"'" + dob + "'" + ","
						+"'" + univname + "'";
				jdbc.execute(sql);
			}*/
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	@RequestMapping(value="/signupform")
	public ModelAndView  signup()
	{
		return new ModelAndView("signup","command1",new signup());
	}
	@RequestMapping(value="/{username}")
	public String aboutuser(@PathVariable(value="username") String user,HttpServletRequest request,HttpServletResponse response,
			ModelMap model)
		{
		int numberofimages = 0;
		String images = null;
		String username = null;
		int differenceintime = 0;
		String imagestodisplay = "";
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);
		String returned="";
		username =  request.getSession().getAttribute("username1").toString();
		model.addAttribute("user1",username);
		String user1friends[] = null;
		String user2friends[] = null;
		String commonfriends[] = null;
		int onlinestatus[] = null;
		model.addAttribute("user2",user);
		String sql = "select count(*)  from userimages where username="+"'"+user+"'";
		if(jdbc.queryForInt(sql) >= 1)
		{
			sql = "select filelocation  from userimages where username="+"'"+user+"'";
		List<Map<String, Object>> rows = jdbc.queryForList(sql);
		for (Map row : rows) 
		{
			images = (String) row.get("filelocation");
			System.out.println("virat kolhi" + images);
			try
			{
			imagestodisplay += "<img src='http://localhost:8080/" + images + "'" + "/>";
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		request.setAttribute("imagestodisplay",imagestodisplay);
		}
		else
		{
			request.setAttribute("imagestodisplay","");
		}
		sql = "select count(*) from friends where user1="+"'"+username+"'"+" or user2="+"'"+username+"'";
		int friendscount = jdbc.queryForInt(sql);
		if(friendscount > 0)
		{
			user1friends = new String[friendscount];
		}
		sql = "select count(*) from friends where user1="+"'"+user+"'"+" or user2="+"'"+user+"'";
		friendscount = jdbc.queryForInt(sql);
		if(friendscount > 0)
		{
			user2friends = new String[friendscount];
		}
		sql = "select user1,user2 from friends where user1="+"'"+username+"'"+" or user2="+"'"+username+"'";
		String sql1 = "select user1,user2 from friends where user1="+"'"+user+"'"+" or user2="+"'"+user+"'";
		List<Map<String, Object>> rows = jdbc.queryForList(sql);
		int count = 0;
		for (Map row : rows) 
		{
			if(row.get("user1").toString().equals(username))
			{
				user1friends[count] = (String) row.get("user2");	
				count++;
			}
			if(row.get("user2").toString().equals(username))
			{
				user1friends[count] = (String) row.get("user1");
				count++;
			}
		}
	    rows = jdbc.queryForList(sql1);
	    count = 0;
		for (Map row : rows) 
		{
			if(row.get("user1").toString().equals(user))
			{
				user2friends[count] = (String) row.get("user2");
				count++;
			}
			if(row.get("user2").toString().equals(user))
			{
				user2friends[count] = (String) row.get("user1");
				count++;
			}
		}
		if(user1friends.length < user2friends.length)
		{
			commonfriends = new String[user1friends.length];
			count = 0;
			for(int i=0;i<user1friends.length && user1friends[i] != null;i++)
			{
				for(int j=0;j<user2friends.length && user2friends[j] != null;j++)
				{
					if(user1friends[i].equals(user2friends[j]))
					{
						commonfriends[count] =  user1friends[i];
						System.out.println(user2friends[i]);
						count++;
					}
				}
			}
		}
		else
		{
			commonfriends = new String[user2friends.length];
			count = 0;
			for(int i=0;i<user2friends.length && user2friends[i] != null;i++)
			{
				for(int j=0;j<user1friends.length && user1friends[j] != null;j++)
				{
					if(user2friends[i].equals(user1friends[j]))
					{
						commonfriends[count] =  user2friends[i];
						System.out.println(user2friends[i]);
						count++;
					}
				}
			}
		}
		onlinestatus = new int[1];
		
			
				sql = "select count(*) from onlinestatus where username="+"'"+user+"'";
			    count = jdbc.queryForInt(sql);
			    if(count > 0)
			    {
			    	sql = "select lastonlinetime from onlinestatus where username="+"'"+user+"'";
			    	String lastonline = jdbc.queryForObject(sql, String.class);
			    	lastonline = lastonline.substring(0, lastonline.length()-2);
			    	System.out.println(lastonline);
			    	sql = "select onlinestatus from onlinestatus where username="+"'"+user+"'";
			    	Boolean status = jdbc.queryForObject(sql, Boolean.class);
			    	if(status)
			    	{
			    		onlinestatus[0] = 1;
			    	}
			    	else
			    	{
			    		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date d = new Date();
						try
						{
					    Date date1 = dateFormat.parse(lastonline);
					    long diff = d.getTime() - date1.getTime();
					    System.out.println("difference in time " + diff);
					    long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
					    differenceintime = (int) (minutes);
					    System.out.println("difference in time in int " + diff);
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
			    		onlinestatus[0] = 0;
			    	}
			    }
	    System.out.println(differenceintime);
		request.setAttribute("commonfriends",commonfriends);
		request.setAttribute("onlinefriends",onlinestatus);
		request.setAttribute("lastonlinetime", differenceintime);	
		sql = "select count(*) from friends where user1="+"'"+username+"'"+" and user2="+"'"+user+"'" + "or user1="+"'"+user+"'"+" and user2="+"'"+username+"'" ;
	    count = jdbc.queryForInt(sql);
		if(count == 0)
		{
			model.addAttribute("buttontype", "Add Friend");
		}
		else
		{
			String users1 = "";
			sql = "select pending from friends where user1="+"'"+username+"'"+" and user2="+"'"+user+"'" + "or user1="+"'"+user+"'"+" and user2="+"'"+username+"'" ;
		    sql1 = "select accept from friends where user1="+"'"+username+"'"+" and user2="+"'"+user+"'" + "or user1="+"'"+user+"'"+" and user2="+"'"+username+"'" ;
			String sql2 = "select count(*) from friends where user1="+"'"+username+"'"+" and user2="+"'"+user+"'";
			count = jdbc.queryForInt(sql2);
			if(count > 0)
			{
				sql2 = "select user1 from friends where user1="+"'"+username+"'"+" and user2="+"'"+user+"'";
				users1 = jdbc.queryForObject(sql2,String.class);
			}
			Boolean pending = jdbc.queryForObject(sql,Boolean.class);
			Boolean accept =  jdbc.queryForObject(sql1,Boolean.class);	
			if(pending)
			{
				if(users1.equals(username))
				{
				model.addAttribute("buttontype", "Friend Request Sent");
				}
				else
				{
					model.addAttribute("buttontype", "Accept Friend Request");
				}
			}
		    if(accept)
			{
				model.addAttribute("buttontype", "Friends");
			}	
		}
		return "userprofile";
		}
	@RequestMapping(value="/updatefriendstable")
	public @ResponseBody String  updatefriendstable(@RequestParam(value="user1")String user1,
			@RequestParam(value="user2")String user2,@RequestParam(value="requesttype")String requesttype)
	{
		System.out.println(requesttype);
		String returnvalue = "false";
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);
		String sql = "select count(*) from friends where user1="+"'"+user1+"'"+" and user2="+"'"+user2+"'" + "or user1="+"'"+user2+"'"+" and user2="+"'"+user1+"'" ;
		int count = jdbc.queryForInt(sql);
		System.out.println(user1);
		System.out.println(user2);
		System.out.println(count);
		if(count == 0)
		{
			if(!user1.equals(user2))
			{
			sql = "insert into friends(user1,user2,pending,accept) values("+"'"+user1+"'"+","+"'"+user2+"'"+","+1+","+0+")";
			jdbc.execute(sql);
			returnvalue = "true";
			}
		}
		else if(count == 1)
		{
			if(!user1.equals(user2))
			{
			System.out.println(requesttype);
			if(requesttype.equals("Accept Friend Request"))
			{
			sql = "update friends set accept = 1  where user1="+"'"+user1+"'"+" and user2="+"'"+user2+"'" + "or user1="+"'"+user2+"'"+" and user2="+"'"+user1+"'";
			jdbc.execute(sql);
			sql = "update friends set pending = 0  where user1="+"'"+user1+"'"+" and user2="+"'"+user2+"'" + "or user1="+"'"+user2+"'"+" and user2="+"'"+user1+"'";
			jdbc.execute(sql);
			returnvalue = "true";
			}
			}
		}
		else
		{
			
		}
		return returnvalue;
	}
	@RequestMapping(value="/aftersignup",method = RequestMethod.POST)
	public String  aftersignup(@ModelAttribute("signup")signup signup,ModelMap model,HttpServletRequest request)
	{
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);
		String username = signup.getSignupuserid();
		String password = signup.getSignuppassword();
		System.out.println(username);
		System.out.println(password);
		try
		{
		String sql1 = "select count(*) from users where username="+"'"+username +"'";	
		int count = jdbc.queryForInt(sql1);
		System.out.println(password);
		if(count > 0)
		{
			model.addAttribute("error","Username already exists!Hard Luck");
			return "signup";
			
		}
		else
		{
			System.out.println("inside else");
			String sql = "insert into users(username,passsword) values (" + "'" + username + "'" + ","
					 + "'" +  password + "'" + ");";
			jdbc.execute(sql);
			return "welcomenewuser";
		}
		}
		catch(Exception e)
		{
			model.addAttribute("error","dao error");
			return "signup";
		}
	}
	@RequestMapping(value="/mainpage",method = RequestMethod.GET)
	public String  mainpage(@ModelAttribute("signup")signup signup,ModelMap model,HttpServletRequest request)
	{
		System.out.println("insinup");
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);
		String username = signup.getSignupuserid();
		String password = signup.getSignuppassword();
		System.out.println(username);
		System.out.println(password);
		try
		{
		String sql1 = "select count(*) from users where username="+"'"+username +"'";	
		int count = jdbc.queryForInt(sql1);
		System.out.println(password);
		if(count > 0)
		{
			model.addAttribute("error","Username already exists!Hard Luck");
			return "signup";
			
		}
		else
		{
			System.out.println("inside else");
			String sql = "insert into users(username,passsword) values (" + "'" + username + "'" + ","
					 + "'" +  password + "'" + ");";
			jdbc.execute(sql);
			model.addAttribute("username", username);
			return "mainpage";
		}
		}
		catch(Exception e)
		{
			model.addAttribute("error","dao error");
			return "signup";
		}
	}
}
