package com.devineni.login;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Blob;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialBlob;
import javax.websocket.Session;

import org.apache.tomcat.util.http.Cookies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.google.gson.Gson;

import netscape.javascript.JSObject;

@Controller
@EnableWebMvc
public class systemproperties {
	@Autowired
	DataSource dataSource;
	@RequestMapping(value="/aboutme")
	public String aboutuser()
	{
		
		return "aboutuser";
	}
	@RequestMapping(value="/uploadpic")
	public ModelAndView uploadingpic(@CookieValue(value = "sessionid",defaultValue = "initial") String sessionid,HttpServletRequest request,HttpServletResponse response)
	{
		int numberofimages = 0;
		String images = null;
		String imagestodisplay = "";
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);
		String returned="";
		
		String username =  request.getSession().getAttribute("username1").toString();
		String sql = "select count(*)  from userimages where username="+"'"+username+"'";
		if(jdbc.queryForInt(sql) >= 1)
		{
			sql = "select filelocation  from userimages where username="+"'"+username+"'";
		List<Map<String, Object>> rows = jdbc.queryForList(sql);
		for (Map row : rows) 
		{
			images = (String) row.get("filelocation");
			System.out.println("virat kolhi" + images);
			try
			{
			imagestodisplay += "<img src='http://localhost:8080/" + images + "'" + "/><br><a href='http://localhost:8080/LoginSystem/ProfilePic?image="+images+"'"+">Make Profile Pic</a>";
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
		return new ModelAndView("pictureupload","command1",new FileUpload());
	}
	
	@RequestMapping(value="/ProfilePic",method = RequestMethod.GET)
	public String makeprofilepic(@RequestParam(value="image")String imagename,HttpServletRequest request)
	{
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);
		int profileflag = 1;
		String username =  request.getSession().getAttribute("username1").toString();
		String sql = "update userimages set profileflag="+0+" where username ="+ "'"+ username+ "'" + " and profileflag="+profileflag;
		jdbc.execute(sql);
	    sql = "update userimages set profileflag="+profileflag+" where username ="+ "'"+ username+ "'" + " and filelocation="+"'"+imagename+"'";
	    jdbc.execute(sql);
	    request.setAttribute("profileimage",imagename);
		return "mainpage";
	}
	@RequestMapping(value="/handleupload")
	public String handlingupload(@ModelAttribute("fileupload")FileUpload fileupload,ModelMap imageuploaded, @RequestParam("file") MultipartFile file,HttpServletRequest request)
	{
		String username = request.getSession().getAttribute("username1").toString();
		System.out.println("chaitanya" + file.getOriginalFilename());
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);
		int profileflag = 1;
		String filename = "";
		if(!file.isEmpty())
		{
			try
			{		
				filename = file.getOriginalFilename();
				String updatesql = "select count(*) from userimages where username="+ "'" + username + "'" +" and profileflag=" + profileflag;
				if(jdbc.queryForInt(updatesql) >= 1)
				{
					System.out.println("msdhoni ");
					updatesql = "update userimages set profileflag=" + 0 + " where username="+ "'" + username + "'"+" and profileflag="+profileflag;
					jdbc.execute(updatesql);
				}
				String sql1 = "insert into userimages(username,filelocation,profileflag)  values(" + "'" + username + "'" + "," + "'" + filename + "'" + "," +  profileflag + ")";
				jdbc.execute(sql1);
				String path = "C:\\Users\\devineni\\Desktop\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\ROOT\\";
				if(! new File(path).exists())
                {
                    new File(path).mkdir();
                }
				String wholepath = path + filename;
				System.out.println();
				System.out.println(wholepath);
				File destination = new File(wholepath);
				file.transferTo(destination);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
		request.setAttribute("profileimage", filename);
		return "mainpage";
	}
	@RequestMapping(value="/settings")
	public String settings()
	{
		
		return "settings";
	}
	public DataSource getDataSource()
	{
		return dataSource;
	}
	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}
	@RequestMapping(value="/logout")
	public String logout(HttpServletRequest request,HttpServletResponse response)
	{
	    String username = request.getSession().getAttribute("username1").toString();
	    JdbcTemplate jdbc = new JdbcTemplate(dataSource);
	    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date d = new Date();
	    String date = dateFormat.format(d);
		String	sql = "update onlinestatus set onlinestatus = 0  where username ="+"'"+username +"'";
		jdbc.execute(sql);
	    sql = "update onlinestatus set lastonlinetime = " + "'" + date + "'" + "where username ="+"'"+username +"'";
	    jdbc.execute(sql);
	    Cookie c = new Cookie("sessionid","");
	    response.addCookie(c);
		return "thankyou";
	}
	
	@RequestMapping(value="/getusers",method = RequestMethod.GET,produces="application/json")
	public @ResponseBody String getusers(@RequestParam(value="keyword")String keyword,HttpServletRequest request,HttpServletResponse response,
			ModelMap model) throws IOException
	{
		System.out.println("inside get user ");
		Users users = new Users();
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);
		String username[] = new String[5];
		ArrayList<String> users1 = new ArrayList();
		int count = 0;
		String sql = "select username from users where username like "+"'"+keyword+"%"+"'";
		List<Map<String, Object>> rows = jdbc.queryForList(sql);
		for (Map row : rows) 
		{
		    username[count] = (String) row.get("username");
		    users1.add((String) row.get("username"));
			count++;
		}
		users.setUsers(username);
		response.setContentType("application/json");
		return new Gson().toJson(users);
	}
	
	@RequestMapping(value="/saveuserdetails",method = RequestMethod.GET)
	public void  saveuserdetails(HttpServletRequest request,@RequestParam(value="fname")String fname,@RequestParam(value="lname")String lname,
			@RequestParam(value="dob")String dob,@RequestParam(value="univname")String univname)
	{
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);
		String username =  request.getSession().getAttribute("username1").toString();
		String sql = "select count(*) from userdetails where username = "+ "'" + username +"'";
		int count = jdbc.queryForInt(sql);
		if (count > 0)
		{
		    if(fname != null)
			sql = "update userdetails set fname = " + fname;
			jdbc.execute(sql);
			if(lname != null)
			sql = "update userdetails set lname = " + lname;
			jdbc.execute(sql);
			if(univname != null)
			sql = "update userdetails set univname = " + univname;
			jdbc.execute(sql);
			if(dob != null)
			sql = "update userdetails set dob = " + dob;
			jdbc.execute(sql);
		}
		else
		{
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date d = new Date();
			try
			{
			d = dateFormat.parse(dob);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			sql = "insert into userdetails(username,fname,lname,dob,univname) values(" +"'" + username + "'" + "," 
					+"'" + fname + "'" + ","
					+"'" + lname + "'" + ","
					+"'" + dob + "'" + ","
					+"'" + univname + "'" + ");";
			jdbc.execute(sql);
	    }
	}
	
	@RequestMapping(value="/processpost",method = RequestMethod.GET)
	public void  processpost(HttpServletRequest request,@RequestParam(value="post")String post)
	{
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);
		String username =  request.getSession().getAttribute("username1").toString();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date d = new Date();
		String postdate = dateFormat.format(d);
		String sql = "select count(postkey) from userposts where username = " + "'" +username+"'";
		int numberofposts = jdbc.queryForInt(sql);
		String generatepostkey = username+(numberofposts+1);
		sql = "insert into userposts(username,postkey,status,date) values("+"'" + username + "'" + "," + "'" + generatepostkey + "'" + "," 
		
+ "'" + post + "'" + ","  + "'" + postdate + "'" + " )";
		System.out.println(username + " " + generatepostkey + " " + postdate);
		jdbc.execute(sql);
		
	}
	
	@RequestMapping(value="/processlike",method = RequestMethod.GET)
	public void  processlike(HttpServletRequest request,@RequestParam(value="postkey")String postkey,@RequestParam(value="username")String username1)
	{
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);
		String username =  request.getSession().getAttribute("username1").toString();
		//boolean liked = true;
		System.out.println("in likeeeeeeeeee");
		String sql = "select count(*) from userpostslc where postkey = " + "'"+postkey+"'"+ "and username =" +"'"+username1+"'";
		int count = jdbc.queryForInt(sql);
		if(count > 0)
		{
		sql = "select comment from userpostslc where postkey = " + "'"+postkey+"'"+ "and username =" +"'"+username+"'";
		String comment = jdbc.queryForObject(sql,String.class);
		System.out.println("printing comment"+comment);
		if(comment == null)
		{
		comment = null;
		sql = "insert into userpostslc(postkey,username,liked,comment) values(" + "'" + postkey + "'" + "," + "'" + username + "'" + ","
		              + "'" + 1 + "'" + "," + "'" + comment + "'" +")";
		jdbc.execute(sql);
		}
		else
		{
			sql = "update userpostsllc set liked = 1 where postkey = " + "'"+postkey+"'"+ "and username =" +"'"+username+"'";
			jdbc.execute(sql);
		}
		}
		else
		{
			 String comment = null;
		     sql = "insert into userpostslc(postkey,username,liked,comment) values(" + "'" + postkey + "'" + "," + "'" + username + "'" + ","
		              + "'" + 1 + "'" + "," + "'" + comment + "'" +")";
		     jdbc.execute(sql);
		}
	}
	@RequestMapping(value="/processcomment",method = RequestMethod.GET,produces="application/json")
	public @ResponseBody String  processcomment(HttpServletRequest request,@RequestParam(value="postkey")String postkey,@RequestParam(value="username")String username1,
			HttpServletResponse response,@RequestParam(value="savecomment")String savecomment,@RequestParam(value="comment") String comment)
	{
		System.out.println("in process comment");
		System.out.println("okkkkkkkkk");
		Users users = new Users();
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);
		String username =  request.getSession().getAttribute("username1").toString();
		String sql = "select count(*) from userposts up,userpostslc uplc where up.postkey = uplc.postkey and up.postkey ="+"'"+postkey+"'" 
		              ;
		int count = jdbc.queryForInt(sql);
		String comments[] = new String[count];
		String userscommented[] = new String[count];
		if(savecomment.equals("false"))
		{
		if(count > 0)
		{
		sql = "select uplc.username,comment from userposts up,userpostslc uplc where up.postkey = uplc.postkey and up.postkey ="+"'"+postkey+"'" 
				+"and comment !="+"'"+ null+"'";
		List<Map<String,Object>> rows = jdbc.queryForList(sql);
		int counter  = 0;
		for(Map row : rows)
		{
			userscommented[counter] = (String) row.get("username");
			System.out.println(userscommented[counter]);
			comments[counter] = (String) row.get("comment");
			System.out.println(comments[counter]);
			counter++;
		}
		}
		}
		else
		{
			System.out.println("in true");
			System.out.println(comment);
			System.out.println(username);
			System.out.println(postkey);
			sql = "select liked from userpostslc where postkey = "+"'"+postkey+"'"+" and username="+"'"+username+"'"; 
			List<Map<String,Object>> rows = jdbc.queryForList(sql);
			String liked = "0";
			for(Map row : rows)
			{
				liked = (String) row.get("liked");
			}
		    if(liked.equals("1"))
		    {
		    	sql = "insert into userpostslc(postkey,username,liked,comment) values("+"'"+postkey+"'"+","+"'"+username+"'"+","+"'"+liked+"'"+","+"'"+comment+"'"+");";
		    	jdbc.execute(sql);
		    }
		    else
		    {
		    	sql = "insert into userpostslc(postkey,username,liked,comment) values("+"'"+postkey+"'"+","+"'"+username+"'"+","+"'"+liked+"'"+","+"'"+comment+"'"+");";
		    	jdbc.execute(sql);
		    }
			 sql = "select count(*) from userposts up,userpostslc uplc where up.postkey = uplc.postkey and up.postkey ="+"'"+postkey+"'" 
		              ;
	       count = jdbc.queryForInt(sql);
		comments = new String[count];
		userscommented = new String[count];
		if(count > 0)
		{
	    
		sql = "select uplc.username,comment from userposts up,userpostslc uplc where up.postkey = uplc.postkey and up.postkey ="+"'"+postkey+"'" 
		              +"and comment !="+"'"+null+"'";
		 rows = jdbc.queryForList(sql);
		int counter  = 0;
		for(Map row : rows)
		{
			userscommented[counter] = (String) row.get("username");
			System.out.println(comments[counter]);
			comments[counter] = (String) row.get("comment");
			System.out.println(userscommented[counter]);
			counter++;
		}
		}
		}
		users.setComments(comments);
		users.setUserscommented(userscommented);
	    response.setContentType("application/json");
		return new Gson().toJson(users);
	}
}