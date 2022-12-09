$(document).ready(
    function (){
        $(".man").click(function(){
            $('.woman').prop("checked", false);
        });
        $(".woman").click(function(){
            $(".man").prop('checked', false);
        });
        $('.fileInput').change(uploadAvatar);
        $('input[type="button"]').click(function () {
            if ($('input[type="button"]').attr('class') === 'submit'){
                if (!test_input()) return;
                $('.submit').attr('value', '编辑');
                $('.submit').attr('class', 'edit');
                $('input').attr('disabled', 'disabled');
                $('input[type="button"]').attr('disabled', null);
                $('textarea').attr('disabled', 'disabled');
                let username = $("input[name='name']");
                let gender = $('input[type="radio"]:checked');
                let email = $('input[name="email"]');
                let birthday = $('input[name="date"]');
                let selfintro = $('#message');
                $.post(
                    '/users/updateinfo',
                    {
                        username: username.val(),
                        gender: gender.val(),
                        email: email.val(),
                        birthday: birthday.val(),
                        selfintro: selfintro.val(),
                    },
                    function(data, status){
                        if (data.code == '0')
                        alert('修改成功');
                    }
                )
            }
            else {
                $('.edit').attr('value', '确认修改');
                $('.edit').attr('class', 'submit');
                $('input').attr('disabled', null);
                $('input[type="email"]').attr('disabled', 'disabled');
                $('textarea').attr('disabled', null);
            }
        });
        $.get("/users/userinfo", {email: $.cookie('id')},
            function (data, textStatus, jqXHR) {
                $("input[name='name']").attr('value', data.username);
                if (data.gender == 0) $('input[value="male"]').prop('checked', true);
                else $('input[value="female"]').prop('checked', true);
                $('input[name="email"]').attr('value', data.email);
                $('input[name="date"]').attr('value', data.birthday);
                $('#message').text(data.selfintro);
                //设置随机无用参数来刷新本地缓存
                $(".avatar").attr('src',data.avatar + "?x=" + Math.random());

            }
        );
    }
)
//检测输入内容是否规范
function test_input(){
    const regexp_username = /^\S{6,30}$/;
    const regexp_email = /^\w+@\w+.com$/;
    if (!regexp_username.test($('input[name="name"]').val()))
    {
        alert('用户名格式不正确');
        return false;
    }
    if (!regexp_email.test($('input[name="email"]').val()))
    {
        alert('邮箱格式不正确');
        return false;
    }
    return true;
}
//上传头像
function uploadAvatar(){
    let file = document.getElementById("fileInput").files[0];
    if (file.size >= 1048576*5) //大于5M
    {
        alert("文件大于5M!");
        return;
    }
    else{
        let formData = new FormData();
        formData.append('file', file);
        formData.append('fileType', '.' + file.type.split('/')[1]);
        formData.append('id', $.cookie('id'));
        $.ajax({
            type: "POST",
            url: "/users/uploadavatar",
            data: formData,
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            success: function (response, status) {
                //设置随机无用参数来刷新本地缓存
                $(".avatar").attr('src',response.url + "?rdm=" + Math.random());
            }
        })
    }
    
}
