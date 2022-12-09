package cn.edu.upc.dzj.HelloWorld.controller;

import cn.edu.upc.dzj.HelloWorld.Initializer;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;
import java.util.Random;


@Controller
@RequestMapping("/register")
public class RegisterController {
    //邮箱验证
    @RequestMapping(value = "verifyemail", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject sendEmail(@RequestParam("mailAddress") String mailAddress) {
        try {
            Random rdm = new Random();
            StringBuilder vericode = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                vericode.append(rdm.nextInt(0, 9));
            }
            Connection conn = DriverManager.getConnection(Initializer.DB_USERS_URL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select count(1) from userinfo where email='" + mailAddress + "'");
            if (rs.next() && rs.getInt("count(1)") > 0) {
                JSONObject res = new JSONObject();
                res.put("code", 1);
                res.put("type", "register");
                res.put("message", "email already in use");
                res.put("ct", new Date());
                return res;
            }
            else {
                rs = stmt.executeQuery("select count(1) from registerinfo where email='" + mailAddress + "'");
                if (rs.next() && rs.getInt("count(1)") > 0) stmt.execute("update registerinfo set vericode='" + vericode+ "' where email='" + mailAddress + "'");
                else stmt.execute("insert into registerinfo(email, vericode, active) values('" + mailAddress + "'," + "'" + vericode + "'," + "0)");
            }
            stmt.close();
            rs.close();
            conn.close();
            Properties properties = new Properties();
            properties.setProperty("mail.host", "smtp.qq.com");
            properties.setProperty("mail.transport.protocol", "smtp");
            properties.setProperty("mail.smtp.auth", "true");
            Session session = Session.getInstance(properties);
            session.setDebug(true);
            Transport transport = session.getTransport();
            transport.connect("smtp.qq.com", "arcueid_official@qq.com", "xedwdtctygqyciie");
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom("arcueid_official@qq.com");
            msg.setSubject("邮箱验证码");
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(mailAddress));
            String content = """
                <div style="text-align: left;">
            </div>
            <div style="text-align: center;">
                <br><hr><br>
                <p>您的邮箱验证码到啦！请查收：</p>
                <br>
                <p style="font-size: 20px;"><b>""" + vericode + "</b></p><br><hr><br><b>Arcueid Learn</b></div>";
            msg.setContent(content, "text/html;charset=UTF-8");
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
            JSONObject res = new JSONObject();
            res.put("code", 0);
            res.put("type", "register");
            res.put("message", "send success");
            res.put("ct", new Date());
            return res;
        }
        catch (Exception e){
            e.printStackTrace();
            JSONObject res = new JSONObject();
            res.put("code", 1);
            res.put("type", "register");
            res.put("message", "unknown error");
            res.put("ct", new Date());
            return res;
        }
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject register(@RequestParam("username") String username, @RequestParam("password") String password,
                               @RequestParam("mail") String mail, @RequestParam("vericode") String vericode){
        JSONObject res = new JSONObject();
        try{
            Connection conn = DriverManager.getConnection(Initializer.DB_USERS_URL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select count(1) from userinfo where email='" + mail + "'");
            if (rs.next() && rs.getInt("count(1)") > 0){
                res.put("code", 1);
                res.put("message", "mail is already used!");
                res.put("ct", new Date());
            }
            rs = stmt.executeQuery("select vericode from registerinfo where email='" + mail + "'");
            if (!rs.next() || !rs.getString("vericode").equals(vericode)){
                res.put("code", 1);
                res.put("type", "register");
                res.put("message", "invalid mail");
                res.put("ct", new Date());
            }
            else {
                try {
                    stmt.execute("insert into userinfo(username, password, email) values('" + username + "','" + password + "','" + mail + "')");
                    stmt.execute("delete from registerinfo where email='" + mail + "'");
                }
                catch (Exception e){
                    e.printStackTrace();
                    System.out.println("Failed to insert username/password!");
                    res.put("code", 1);
                    res.put("type", "register");
                    res.put("message", "Unknown error occurred while registering");
                    res.put("ct", new Date());
                }
                System.out.println(username + " successfully registered!");
                res.put("code", 0);
                res.put("type", "register");
                res.put("message", "success");
                res.put("ct", new Date());
            }
            stmt.close();
            conn.close();
        }
        catch (Exception e){
            System.out.println("Failed to connect to database!");
            e.printStackTrace();
            res.put("code", 1);
            res.put("type", "register");
            res.put("message", "unknown error");
            res.put("ct", new Date());
        }
        return res;
    }
}
