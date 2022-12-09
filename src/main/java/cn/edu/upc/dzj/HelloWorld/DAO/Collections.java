package cn.edu.upc.dzj.HelloWorld.DAO;

import cn.edu.upc.dzj.HelloWorld.Initializer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;


//操作用户收藏夹
public class Collections {
    String email; //通过用户邮箱标识收藏夹
    String md5; //"user"+用户邮箱生成的md5值标识唯一表

    Connection connection;

    public Collections(String email) throws NoSuchAlgorithmException, SQLException {
        this.email = email;
        this.md5 = toMD5();
        this.connection = DriverManager.getConnection(Initializer.DB_USER_WORDBOOK);
        checkDB();
    }
    public void close() throws SQLException {
        this.connection.close();
    }
    private void checkDB() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("create table if not exists " + md5 + " (id integer primary key, type text not null, content text unique not null)");
    }
    private String bytes2Hex(byte[] src){
        if (src == null || src.length == 0) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder("");
        for (byte b : src) {
            String str = Integer.toHexString(b & 0xff);
            if (str.length() < 2) { // 不足两位要补0
                stringBuilder.append(0);
            }
            stringBuilder.append(str);
        }
        return stringBuilder.toString();
    }
    //生成md5标识
    private String toMD5() throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(this.email.getBytes());
        return "user" + bytes2Hex(md5.digest());
    }
    //添加单词
    public void addWord(String word) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("insert or ignore into " + md5 + "(type, content) values('word', '" + word +"')");
        statement.close();
    }
    //获取单词本
    public ArrayList<HashMap<String, String>> getBook() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select content from " + md5 + " where type='word'");
        ArrayList<HashMap<String, String>> res = new ArrayList<HashMap<String, String>>();
        Connection conn = DriverManager.getConnection(Initializer.DB_SIMPLE_DICT);
        Statement stmt = conn.createStatement();
        while (resultSet.next()){
            String word = resultSet.getString("content");
            ResultSet rs = stmt.executeQuery("select exp from words where word_en = '" + word + "'");
            if (rs.next()) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("word", word);
                hashMap.put("exp", rs.getString("exp"));
                res.add(hashMap);
            }
        }
        conn.close();
        return res;
    }
    //获取收藏的歌曲列表
    public ArrayList<HashMap<String, String>> getAlbum() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select content from " + md5 + " where type='song'");
        ArrayList<HashMap<String, String>> res = new ArrayList<HashMap<String, String>>();
        Connection conn = DriverManager.getConnection(Initializer.DB_DAILY_SONGS);
        Statement stmt = conn.createStatement();
        while (resultSet.next()){
            String song_id = resultSet.getString("content");
            ResultSet rs = stmt.executeQuery("select lyrics from songs where song_id = '" + song_id + "'");
            if (rs.next()) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("song_id", song_id);
                hashMap.put("song_name", rs.getString("lyrics").split("\n")[0]);
                res.add(hashMap);
            }
        }
        conn.close();
        return res;
    }
    //获取收藏的句子
    public ArrayList<HashMap<String, String>> getSentences() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select content from " + md5 + " where type='sentence'");
        ArrayList<HashMap<String, String>> res = new ArrayList<HashMap<String, String>>();
        Connection conn = DriverManager.getConnection(Initializer.DB_DAILY_SENTENCE);
        Statement stmt = conn.createStatement();
        while (resultSet.next()){
            String sen_id = resultSet.getString("content");
            ResultSet rs = stmt.executeQuery("select * from sentences where id = '" + sen_id + "'");
            if (rs.next()) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("id", sen_id);
                hashMap.put("en", rs.getString("en"));
                hashMap.put("cn", rs.getString("cn"));
                res.add(hashMap);
            }
        }
        conn.close();
        return res;
    }
    //获取收藏的听力练习
    public ArrayList<HashMap<String, String>> getPractices() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select content from " + md5 + " where type='practice'");
        ArrayList<HashMap<String, String>> res = new ArrayList<HashMap<String, String>>();
        Connection conn = DriverManager.getConnection(Initializer.DB_DAILY_LISTENING);
        Statement stmt = conn.createStatement();
        while (resultSet.next()){
            String practice_id = resultSet.getString("content");
            ResultSet rs = stmt.executeQuery("select * from materials where id = '" + practice_id + "'");
            if (rs.next()) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("id", practice_id);
                hashMap.put("mp3_url", rs.getString("mp3_url"));
                hashMap.put("answer", rs.getString("answer"));
                res.add(hashMap);
            }
        }
        conn.close();
        return res;
    }
    //收藏歌曲
    public void addSong(String songId) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("insert into " + md5 + "(type, content) values('song','" + songId + "')");
        statement.close();
    }
    //收藏句子
    public void addSentence(String sentenceId) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("insert into " + md5 + "(type, content) values('sentence','" + sentenceId + "')");
        statement.close();
    }
    //添加听力练习
    public void addPractice(String practice_id) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("insert or ignore into " + md5 + "(type, content) values('practice', '" + practice_id +"')");
        statement.close();
    }
}
