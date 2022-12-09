package cn.edu.upc.dzj.HelloWorld.DAO;

import cn.edu.upc.dzj.HelloWorld.Initializer;
import java.sql.*;


//操作用户信息的DAO类
public class User {
    String email; //唯一表示符
    Connection connection;
    public User(String email) throws SQLException {
        this.email = email;
        this.connection = DriverManager.getConnection(Initializer.DB_USERS_URL);
    }
    //输入字段名和字段值修改对应字段
    private void updateValueByColumn(String column, String value) throws SQLException{
        try{
            Statement statement = connection.createStatement();
            String sql = "update userinfo set " + column + "='" + value + "' where email='" + email + "'";
            statement.execute(sql);
            statement.close();
        }
        catch (Exception e){
            connection.close();
        }
    }
    //输入字段名获取字典值
    private String getValueByColumn(String colunm) throws SQLException {
        String res="";
        try{
            Statement statement = connection.createStatement();
            String sql = "select " + colunm + " from userinfo where email='" + email + "'";
            ResultSet rs = statement.executeQuery(sql);
            rs.next();
            res = rs.getString(colunm);
            rs.close();
            statement.close();
        }
        catch (Exception e) {
            connection.close();
        }
        return res;
    }
    //设置用户名称
    public void setUsername(String username) throws SQLException{
        updateValueByColumn("username", username);
    }
    //设置密码（未使用）
    public void setPassword(String password) throws SQLException{
        updateValueByColumn("password", password);
    }
    //设置性别
    public void setGender(String gender) throws SQLException{
        updateValueByColumn("gender", gender);
    }
    //设置个人简介
    public void setSelfintro(String selfintro) throws SQLException{
        updateValueByColumn("selfintro", selfintro);
    }
    //设置生日
    public void setBirthday(String birthday) throws SQLException{
        updateValueByColumn("birthday", birthday);
    }
    //设置头像路径
    public void setAvatar(String url) throws SQLException {
        updateValueByColumn("avatar", url);
    }
    //设置邮箱（未使用）
    public void setEmail(String email) throws SQLException {
        this.email = email;
        updateValueByColumn("email", email);
    }
    //获取邮箱
    public String getEmail() throws SQLException{
        return getValueByColumn("email");
    }
    //获取用户名
    public String getUsername() throws SQLException{
        return getValueByColumn("username");
    }
    //获取生日
    public String getBirthday() throws SQLException{
        return getValueByColumn("birthday");
    }
    //获取性别
    public String getGender() throws SQLException{
        return getValueByColumn("gender");
    }
    //获取个人简介
    public String getSelfintro() throws SQLException{
        return getValueByColumn("selfintro");
    }
    //获取用户名
    public String getAvatar() throws SQLException{
        return getValueByColumn("avatar");
    }
    //关闭数据库连接
    public void close() throws SQLException {
        this.connection.close();
    }
}
