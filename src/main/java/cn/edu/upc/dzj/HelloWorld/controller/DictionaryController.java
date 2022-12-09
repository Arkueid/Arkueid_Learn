package cn.edu.upc.dzj.HelloWorld.controller;

import cn.edu.upc.dzj.HelloWorld.Initializer;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/dictionary")
public class DictionaryController {

    //访问词典首页
    @RequestMapping("")
    public String gotoDictionary(HttpServletRequest request, HttpServletResponse response){
        return "/web/dictionary.html";
    }

    //获取对应单词的ID
    @RequestMapping("/word_id")
    @ResponseBody
    public JSONObject getWordID(@RequestParam("word") String word, @RequestParam("dict") String dict) {
        JSONObject res = new JSONObject();
        Connection conn = null;
        try {
            switch (dict) {
                case "oxforde9" -> conn = DriverManager.getConnection(Initializer.DB_OXFORD_URL);
                case "collins" -> conn = DriverManager.getConnection(Initializer.DB_COLLINS_URL);
                case "ldoce" -> conn = DriverManager.getConnection(Initializer.DB_LDOCE_URL);
                case "simpledict" -> conn = DriverManager.getConnection(Initializer.DB_SIMPLE_DICT);
                default -> {
                    res.put("code", 1);
                    res.put("message", "dict not found");
                    return res;
                }
            }
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("select id,word_en from words where word_en like '" + word + "%' limit 0,5");
            ArrayList<HashMap<String, String>> words = new ArrayList<HashMap<String, String>>();
            while (rs.next()){
                HashMap<String, String> w = new HashMap<String, String>();
                w.put("word", rs.getString("word_en"));
                w.put("id", rs.getString("id"));
                words.add(w);
            }
            res.put("code", 0);
            res.put("message", "success");
            res.put("words", words);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    //查词功能
    @RequestMapping(value = "query", produces = org.springframework.http.MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String query(@RequestParam("word") String word,  @RequestParam("dict") String dict) {
        String html = "";
        Connection conn = null;
        try {//选择词典
            switch (dict) {
                case "oxforde9":
                    conn = DriverManager.getConnection(Initializer.DB_OXFORD_URL);
                    break;
                case "collins":
                    conn = DriverManager.getConnection(Initializer.DB_COLLINS_URL);
                    break;
                case "ldoce":
                    conn = DriverManager.getConnection(Initializer.DB_LDOCE_URL);
                    break;
                case "simpledict":
                    conn = DriverManager.getConnection(Initializer.DB_SIMPLE_DICT);
                    break;
                default:
                    return "<b>词典不存在</b>";
            }
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select exp from words where word_en='" + word + "'");
            if (rs.next())
            {
                //正则表达式替换网页资源路径
                Pattern linkPattern = Pattern.compile("@@@LINK=.+");
                html = rs.getString("exp").replace("\\n", "").replace("\n", "");
                Matcher linkMatcher = linkPattern.matcher(html);
                if (linkMatcher.find()) {
                    ResultSet newrs = stmt.executeQuery("select exp from words where word_en='" + linkMatcher.group(0).split("=")[1] + "'");
                    newrs.next();
                    html = newrs.getString("exp");
                }
                if (dict.equals("ldoce")) html = html.replace("jquery-3.2.1.min.js", "../js/jquery-3.6.1.min.js");
                Pattern js_pattern = Pattern.compile("([-A-Za-z0-9_\\.]*?\\.(js|ini))");
                Pattern css_pattern = Pattern.compile("(\\w*?\\.css)");
                Pattern img_pattern = Pattern.compile("\\w*?\\.(png|jpg|svg)");
                Matcher js_matcher = js_pattern.matcher(html);
                Matcher css_matcher = css_pattern.matcher(html);
                Matcher img_matcher = img_pattern.matcher(html);
                while (js_matcher.find()) {
                    for (int len = js_matcher.groupCount(); len > 0; len --){
                        html = html.replace(js_matcher.group(len - 1), "../js/" + js_matcher.group(len - 1));
                    }
                }
                while (css_matcher.find()) {
                    for (int len = css_matcher.groupCount(); len > 0; len--) {
                        html = html.replace(css_matcher.group(len - 1), "../css/" + css_matcher.group(len - 1));
                    }
                }
                while (img_matcher.find()){
                    for (int len=img_matcher.groupCount(); len > 0; len --){
                        html = html.replace(img_matcher.group(len - 1), "../images/" + img_matcher.group(len - 1));
                    }
                }
            }
            else html = "<b>没找到单词! w(ﾟДﾟ)w</b>";
        } catch (Exception e) {
            e.printStackTrace();
            return "<b>出现未知错误!</b>";
        }
        return html;
    }

    //音乐时刻资源
    @RequestMapping("dailysong")
    @ResponseBody
    public JSONObject getDailySong() throws SQLException {
        //随机生成歌曲id并返回资源链接和歌词
        Random rdm  = new Random();
        int id = rdm.nextInt(1, 30);
        Connection connection = DriverManager.getConnection(Initializer.DB_DAILY_SONGS);
        JSONObject res = new JSONObject();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from songs where id=" + id);
        if (rs.next()){
            res.put("song_name", rs.getString("lyrics").split("\n")[0]);
            res.put("song_id", rs.getString("song_id"));
            res.put("lyrics", rs.getString("lyrics"));
        }
        res.put("code", 0);
        connection.close();
        return res;
    }

    //美句欣赏资源
    @RequestMapping("dailysentence")
    @ResponseBody
    public JSONObject getDailySentence() throws SQLException {
        //随机生成句子id并返回内容
        JSONObject res = new JSONObject();
        Random rdm = new Random();
        int id = rdm.nextInt(1, 702);
        Connection connection = DriverManager.getConnection(Initializer.DB_DAILY_SENTENCE);
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from sentences where id=" + id);
        if (rs.next()){
            res.put("id", rs.getString("id"));
            res.put("en", rs.getString("en"));
            res.put("cn", rs.getString("cn"));
            res.put("editor", rs.getString("editor"));
        }
        res.put("code", 0);
        connection.close();
        return res;
    }

    //听力练习
    @RequestMapping("dailylistening")
    @ResponseBody
    public JSONObject getDailyListening() throws SQLException {
        JSONObject res = new JSONObject();
        Random rdm = new Random();
        int id = rdm.nextInt(1, 164);
        Connection connection = DriverManager.getConnection(Initializer.DB_DAILY_LISTENING);
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from materials where id=" + id);
        if (rs.next()){
            res.put("id", rs.getString("id"));
            res.put("mp3_url", rs.getString("mp3_url"));
            res.put("answer", rs.getString("answer").replace("\n", "<br>"));
        }
        res.put("code", 0);
        connection.close();
        return res;
    }

}
