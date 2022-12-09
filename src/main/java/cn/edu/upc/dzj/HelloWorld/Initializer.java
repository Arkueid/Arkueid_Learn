package cn.edu.upc.dzj.HelloWorld;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
//初始化模块
public class Initializer {
    //用户数据库
    static final String method = "jdbc:sqlite:";
    //文件存放目录
    static final String root = "db";
    public static final String DB_USERS_URL = method + root + "/users.db";
    //牛津词典数据库
    public static final String DB_OXFORD_URL = method + root + "/oxforde9.db";
    //柯林斯词典数据
    public static final String DB_COLLINS_URL = method + root + "/collins.db";
    //朗文词典数据
    public static final String DB_LDOCE_URL = method + root + "/ldoce.db";
    //简单词典
    public static final String DB_SIMPLE_DICT = method + root + "/simpledict.db";
    //每日一曲数据库
    public static final String DB_DAILY_SONGS = method + root + "/dailysongs.db";
    //每日一句数据库
    public static final String DB_DAILY_SENTENCE = method + root + "/dailysentences.db";
    //每日一听数据库
    public static final String DB_DAILY_LISTENING = method + root + "/dailylistening.db";
    //单词本数据库
    public static final String DB_USER_WORDBOOK = method + root + "/collections.db";
    //数据库驱动
    public static final String JDBC_DRIVER = "org.sqlite.JDBC";
    //初始化用户公共数据表
    public static final String CREATE_TABLE_LOGIN_VERIFICATION = "create table if not exists userinfo (" +
            "id integer primary key not null," +
            "username text not null," +
            "password text not null," +
            "email text not null unique," +
            "gender bit default 0," +
            "selfintro text default '这个人很懒，什么也没留下...'," +
            "birthday text default '1900-01-01'," +
            "avatar text default '/images/default/avatar.svg')";
    //创建存放验证码数据表
    public static final String CREATE_TABLE_REGISTER_VERIFICATION = "create table if not exists registerinfo(" +
            "id integer primary key not null," +
            "email text unique not null," +
            "vericode char(6) not null," +
            "active bool default 0)";
    //检测数据表是否存在，不存在创建
    public static void checkDB(){
        try {
            System.out.println("Checking database...");
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_USERS_URL);
            Statement stmt = conn.createStatement();
            stmt.execute(CREATE_TABLE_LOGIN_VERIFICATION);
            stmt.execute(CREATE_TABLE_REGISTER_VERIFICATION);
            stmt.close();
            conn.close();
            conn = DriverManager.getConnection(DB_USER_WORDBOOK);
            conn.close();
            System.out.println("Check finished!");
        }
        catch (Exception e){
            System.out.println("Failed to connect to database!");
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
