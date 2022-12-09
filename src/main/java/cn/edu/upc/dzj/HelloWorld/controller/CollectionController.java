package cn.edu.upc.dzj.HelloWorld.controller;

import cn.edu.upc.dzj.HelloWorld.DAO.Collections;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


@Controller
@RequestMapping("/collection")
//用户收藏夹控制层
public class CollectionController {

    //获取生词本
    @RequestMapping("/wordbook")
    @ResponseBody
    public JSONObject getWords(HttpServletRequest request) throws SQLException, NoSuchAlgorithmException {
        JSONObject res = new JSONObject();
        String email = getIdFromCookies(request.getCookies());  //从请求中携带的cookie获取用户id
        if (email == null){ //返回为null说明没有找到该用户或无字段为id的cookie
            res.put("code", 1);
            res.put("message", "unknown user");
            return res;
        }
        Collections collections = new Collections(email);
        ArrayList<HashMap<String, String>> words = collections.getBook();
        res.put("code", 0);
        res.put("message", "success");
        res.put("words", words);
        return res;
    }
    //获取收藏歌曲列表
    @RequestMapping("/album")
    @ResponseBody
    public JSONObject getSongs(HttpServletRequest request) throws SQLException, NoSuchAlgorithmException {
        JSONObject res = new JSONObject();
        String email = getIdFromCookies(request.getCookies());
        if (email == null){
            res.put("code", 1);
            res.put("message", "unknown user");
            return res;
        }
        Collections collections = new Collections(email);
        ArrayList<HashMap<String, String>> songs = collections.getAlbum();
        res.put("code", 0);
        res.put("message", "success");
        res.put("songs", songs);
        return res;
    }
    //获取收藏句子
    @RequestMapping("/sentences")
    @ResponseBody
    public JSONObject getSentences(HttpServletRequest request) throws SQLException, NoSuchAlgorithmException {
        JSONObject res = new JSONObject();
        String email = getIdFromCookies(request.getCookies());
        if (email == null){
            res.put("code", 1);
            res.put("message", "unknown user");
            return res;
        }
        Collections wordBook = new Collections(email);
        ArrayList<HashMap<String, String>> sentences = wordBook.getSentences();
        res.put("code", 0);
        res.put("message", "success");
        res.put("sentences", sentences);
        return res;
    }
    //获取收藏练习
    @RequestMapping("/practices")
    @ResponseBody
    public JSONObject getPractices(HttpServletRequest request) throws SQLException, NoSuchAlgorithmException {
        JSONObject res = new JSONObject();
        String email = getIdFromCookies(request.getCookies());
        if (email == null){
            res.put("code", 1);
            res.put("message", "unknown user");
            return res;
        }
        Collections wordBook = new Collections(email);
        ArrayList<HashMap<String, String>> practices = wordBook.getPractices();
        res.put("code", 0);
        res.put("message", "success");
        res.put("practices", practices);
        return res;
    }
    //添加单词到生词本
    @RequestMapping("/addword")
    @ResponseBody
    public JSONObject addWord(@RequestParam("word") String word, HttpServletRequest request) throws SQLException, NoSuchAlgorithmException {
        JSONObject res = new JSONObject();
        String email = getIdFromCookies(request.getCookies());
        if (email == null) {
            res.put("code", 1);
            res.put("message", "unknown user");
            return res;
        }
        try {
            Collections wordBook = new Collections(email);
            wordBook.addWord(word);
            wordBook.close();
        }catch (Exception e){
            res.put("code", 1);
            res.put("message", "unknown error");
            return res;
        }
        res.put("code", 0);
        res.put("message", "success");
        return res;
    }
    //添加选中歌曲
    @RequestMapping("/addsong")
    @ResponseBody
    public JSONObject addSong(@RequestParam("songId") String songId, HttpServletRequest request) throws SQLException, NoSuchAlgorithmException {
        JSONObject res = new JSONObject();
        String email = getIdFromCookies(request.getCookies());
        if (email == null) {
            res.put("code", 1);
            res.put("message", "unknown user");
            return res;
        }
        try {
            Collections wordBook = new Collections(email);
            wordBook.addSong(songId);
            wordBook.close();
        }catch (Exception e){
            res.put("code", 1);
            res.put("message", "unknown error");
            return res;
        }
        res.put("code", 0);
        res.put("message", "success");
        return res;
    }
    //添加选中句子
    @RequestMapping("/addsentence")
    @ResponseBody
    public JSONObject addSentence(@RequestParam("id") String id, HttpServletRequest request) throws SQLException, NoSuchAlgorithmException {
        JSONObject res = new JSONObject();
        String email = getIdFromCookies(request.getCookies());
        if (email == null) {
            res.put("code", 1);
            res.put("message", "unknown user");
            return res;
        }
        try {
            Collections wordBook = new Collections(email);
            wordBook.addSentence(id);
            wordBook.close();
        }catch (Exception e){
            res.put("code", 1);
            res.put("message", "unknown error");
            return res;
        }
        res.put("code", 0);
        res.put("message", "success");
        return res;
    }
    //收藏选中听力练习
    @RequestMapping("/addpractice")
    @ResponseBody
    public JSONObject addPractice(@RequestParam("id") String id, HttpServletRequest request) throws SQLException, NoSuchAlgorithmException {
        JSONObject res = new JSONObject();
        String email = getIdFromCookies(request.getCookies());
        if (email == null) {
            res.put("code", 1);
            res.put("message", "unknown user");
            return res;
        }
        try {
            Collections wordBook = new Collections(email);
            wordBook.addPractice(id);
            wordBook.close();
        }catch (Exception e){
            res.put("code", 1);
            res.put("message", "unknown error");
            return res;
        }
        res.put("code", 0);
        res.put("message", "success");
        return res;
    }
    //从cookie中获取id字段，没有id字段就返回null
    private String getIdFromCookies(Cookie[] cookies){
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie: cookies){
            if (cookie.getName().equals("id")) return cookie.getValue();
        }
        return null;
    }
}
