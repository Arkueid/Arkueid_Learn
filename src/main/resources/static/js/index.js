$(document).ready(function(){
    /*注册*/
    $("#register").click(
        showRegister
    )
    
    $('#login').click(
        showLogin
    )

})

function showIndex(){
        $('.rg_center').html(
            '<div>\
            <a href="/dictionary">\
                <img id="avatar" src="../images/icon/favicon.ico" alt="">\
            </a>\
        </div>\
        <p id="name" style="font-family: \'Segoe UI\', Tahoma, Geneva, Verdana, sans-serif;font-size: 25px;font-style: oblique;"><b>Arkueid Learn</b></p>\
        <button id="login">登录</button>\
        <button id="register">注册</button>'
        )
        $("#register").click(
            showRegister
        )
        $('#login').click(
            showLogin
        )
}


function showRegister(){
    $(".rg_center").html(
        '<div>\
        <div class="back_div">\
            <button id="back_btn"><b>返回</b></button>\
        </div>\
        <div>\
            <img width="30" height="30" src="../images/icon/username.svg" alt=""><input id="username" type="text" placeholder="用户名">\
        </div>\
        <span id="tip_username" class="tip"></span>\
        <div>\
            <img width="30" height="30" src="../images/icon/password.svg" alt=""><input id="password1" type="password" placeholder="密码">\
        </div>\
        <span id="tip_password1" class="tip"></span>\
        <div>\
            <img width="30" height="30" src="../images/icon/password.svg" alt=""><input id="password2" type="password" placeholder="确认密码">\
        </div>\
        <span id="tip_password2" class="tip"></span>\
        <div>\
            <img width="30" height="30" src="../images/icon/email.svg" alt=""><input id="email" type="email" placeholder="输入验证邮箱, 仅支持QQ邮箱">\
        </div>\
        <span id="tip_email" class="tip"></span>\
        <div>\
            <img width="30" height="30" src="../images/icon/vericode.svg" alt=""><input id="vericode" type="tel" placeholder="验证码">\
            <button id="send_code">发送验证码</button>\
        </div>\
        <span id="tip_vericode" class="tip"></span>\
        <div>\
            <button id="register_btn">注册</button>\
        </div>\
    </div>'
    )
    $("#send_code").click(function(){
        const username = document.getElementById("username");
        const password1 = document.getElementById("password1");
        const password2 = document.getElementById("password2");
        const email = document.getElementById("email");

        //检测输入
        if (username.value.length === 0 || password1.value.length === 0 || password2.value.length === 0 || email.value.length === 0)
        {
            alert("表单未填写完整！");
        }
        const regexp_username = /^\S{6,30}$/;
        const regexp_password = /^[a-zA-Z0-9]{6,16}$/;
        const regexp_email = /^\w+@qq.com$/;
        if (!regexp_username.test(username.value))
        {
            $('#tip_username').text('用户名仅由6-30非空字符组成');
            return;
        }
        else{
            $('#tip_username').text('');
        }
        if (!regexp_password.test(password1.value))
        {
            $('#tip_password1').text('密码仅由6-16位数字、字母组成');
            return;
        }
        else{
            $('#tip_password1').text('');
        }
        if (password1.value != password2.value){
            $('#tip_password2').text('两次密码不一致');
            return;
        }
        else{
            $('#tip_password2').text('');
        }
        if (!regexp_email.test(email.value)){
            $('#tip_email').text('邮箱格式不正确');
            return;
        }
        else{
            $('#tip_email').text('');
        }
        console.log(email.value);
        $.post(
                "/register/verifyemail",
                {
                    mailAddress: email.value
                },
                function(data, status){
                    if (data.code == '0'){
                        alert('验证码发送成功');
                        let time = 60;
                        const secmeter = setInterval(function () {
                            time--;
                            $('#send_code').text("请" + time + "秒后再试");
                            if (time == 0) {
                                clearInterval(secmeter);
                                $('#send_code').text("发送验证码");
                                $('#send_code').attr("disabled", false);
                            } else {
                                $('#send_code').attr("disabled", true);
                            }
                        }, 1000);
                    }
                    else alert(data.message);
                },
            )
    })
    $("#register_btn").click(
        function(){
            const username = document.getElementById("username");
            const password1 = document.getElementById("password1");
            const password2 = document.getElementById("password2");
            const email = document.getElementById("email");
            const vericode = document.getElementById("vericode");

            //检测输入
            if (username.value.length === 0 || password1.value.length === 0 || password2.value.length === 0 || email.value.length === 0 || vericode.value.length === 0)
            {
                alert("表单未填写完整！");
            }
            const regexp_username = /^\S{6,30}$/;
            const regexp_password = /^[a-zA-Z0-9]{6,16}$/;
            const regexp_email = /^\w+@qq.com$/;
            const regexp_vericode = /^\d{6}$/;
            if (!regexp_username.test(username.value))
            {
                $('#tip_username').text('用户名仅由6-30非空字符组成');
                return;
            }
            else{
                $('#tip_username').text('');
            }
            if (!regexp_password.test(password1.value))
            {
                $('#tip_password1').text('密码仅由6-16位数字、字母组成');
                return;
            }
            else{
                $('#tip_password1').text('');
            }
            if (password1.value !== password2.value){
                $('#tip_password2').text('两次密码不一致');
                return;
            }
            else{
                $('#tip_password2').text('');
            }
            if (!regexp_email.test(email.value)){
                $('#tip_email').text('邮箱格式不正确');
                return;
            }
            else{
                $('#tip_email').text('');
            }
            if (!regexp_vericode.test(vericode.value)){
                $('#tip_vericode').text('验证码输入有误');
                return;
            }
            else{
                $('#tip_vericode').text('');
            }
            $.post(
                '/register/submit',
                {
                    username: username.value,
                    password: password1.value,
                    mail: email.value,
                    vericode: vericode.value
                },
                function(data, status){
                    if (data.message === "success"){
                        $("username").text("");
                        $("password1").text("");
                        $("password2").text("");
                        $("email").text("");
                        $("vericode").text("");
                        showIndex();
                    }
                    else alert(data.message);
                }
            )
        }
    )
    $("#back_btn").click(
        showIndex
    )

    /*检测输入框*/
    const inputs = document.getElementsByTagName("input");
    for (let i=0; i < inputs.length; i ++){
        inputs[i].onblur = function(){
            if (this.value.length === 0){
                const tip = document.getElementById('tip_' + this.id);
                tip.style = "height: 30px;text-align: center;font-size: 15px;font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;color: red;";
                if (this.id === 'username')
                    tip.innerHTML = "用户名不能为空！";
                else if (this.id === 'password1')
                    tip.innerHTML = '密码不能为空！';
                else if (this.id === 'password2')
                    tip.innerHTML = '请确认密码！';
                else if (this.id === 'email')
                    tip.innerHTML = '邮箱不能为空！';
                else if (this.id === 'vericode')
                    tip.innerHTML = '请输入验证码！';
            }
        }
    }
}

function showLogin(){
    $(".rg_center").html(
        '<div>\
        <div class="back_div">\
            <button id="back_btn"><b>返回</b></button>\
        </div>\
        <div>\
            <img width="30" height="30" src="../images/icon/username.svg" alt=""><input id="username" type="text" placeholder="登录邮箱">\
        </div>\
        <span id="tip_username" class="tip"></span>\
        <div>\
            <img width="30" height="30" src="../images/icon/password.svg" alt=""><input id="password" type="password" placeholder="密码">\
        </div>\
        <span id="tip_password" class="tip"></span>\
        <div>\
            <button id="login_btn">登录</button>\
        </div>\
    </div>'
    )
    $('#login_btn').click(

    )
    $('#back_btn').click(
        showIndex
    )
    //登录
    $("#login_btn").click(function(){
        var mail = document.getElementById("username");
        var password = document.getElementById("password");

        //检测输入
        if (username.value.length === 0 || password.value.length === 0)
        {
            alert("表单未填写完整！");
        }
        else{
            $.post(
                "/login/submit",
                {
                    mail : username.value,
                    password: password.value
                },
                function(data, status){
                    if (data.redirect != undefined) window.location.replace(data.redirect);
                    else alert(data.message);
                }).catch(
                    function(err){
                        //do nothing here
                    }
                )           
        } 
    })
    /*检测输入框*/
    var inputs = document.getElementsByTagName("input");
    for (var i=0; i < inputs.length; i ++){
        inputs[i].onblur = function(){
            if (this.value.length === 0){
                var tip = document.getElementsByClassName('tip_' + this.id)[0];
                tip.style = "height: 30px;text-align: center;font-size: 15px;font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;color: red;";
                if (this.id === 'username')
                    tip.innerHTML = "用户名不能为空！";
                else if (this.id === 'password')
                    tip.innerHTML = '密码不能为空！';
            }
        }
    }
}