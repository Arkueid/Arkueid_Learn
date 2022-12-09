$(document).ready(
    function (){
        $('#word-query-tab').click(
            function(){
                if ($('#word-query').attr('class') === "dict-frame") {
                    $('#word-query').attr('class', 'dict-frame-show');
                    $('#word-query-tab').css('color', 'rgba(255,188,154,0.8)')
                        .css('border-bottom-color', 'rgba(255,188,154,0.8)')
                        .css('border-bottom-style', 'solid');
                }
                else {
                    // $('#word-query').hide(50);
                    $('#word-query').attr('class', 'dict-frame');
                    $('#word-query-tab').css('color', 'black')
                        .css('border-bottom-color', 'rgba(0, 0, 0, 0)')
                        .css('border-bottom-style', 'none');
                }
            }
        );
        $('#daily-listening-tab').click(
            function(){
                $('.main-item-current').attr('class', 'main-item');
                $('#daily-listening').attr('class', 'main-item-current');
                $('.nav-item-current').attr('class', 'nav-item');
                $('#daily-listening-tab').attr('class', 'nav-item-current');
                $('.home').css('display', 'none');

            }
        );
        $('#daily-quotation-tab').click(
            function(){
                $('.main-item-current').attr('class', 'main-item');
                $('#daily-quotation').attr('class', 'main-item-current');
                $('.nav-item-current').attr('class', 'nav-item');
                $('#daily-quotation-tab').attr('class', 'nav-item-current');
                $('.home').css('display', 'none');
            }
        )
        $('#daily-song-tab').click(
            function(){
                $('.main-item-current').attr('class', 'main-item');
                $('#daily-song').attr('class', 'main-item-current');
                $('.nav-item-current').attr('class', 'nav-item');
                $('#daily-song-tab').attr('class', 'nav-item-current');
                $('.home').css('display', 'none');
            }
        );
        $('#search-input').bind('keypress', function (e) {
            if (e.keyCode == '13' && $('#search-input').is(':focus') == true){
                queryWord($('#search-input').val());
                $('#word-query').attr('class', 'dict-frame-show');
                    $('#word-query-tab').css('color', 'rgba(255,188,154,0.8)')
                        .css('border-bottom-color', 'rgba(255,188,154,0.8)')
                        .css('border-bottom-style', 'solid');
            }
        }).autocomplete(
            {
                position:{ my: "left top", at: "left bottom", collision: "none"},
                autoFocus: false,
                focus: function (event, ui) {
                    $('#search-input').val(ui.item.word);
                    $('.word').val(ui.item.id)
                        .attr('name', ui.item.word);
                    return false;
                },
                select: function (event, ui) {
                    queryWord(ui.item.word);
                    $('#search-input').val(ui.item.word);
                    $('.word').val(ui.item.id)
                        .attr('name', ui.item.word);
                    $('#word-query').attr('class', 'dict-frame-show');
                    $('#word-query-tab').css('color', 'rgba(255,188,154,0.8)')
                        .css('border-bottom-color', 'rgba(255,188,154,0.8)')
                        .css('border-bottom-style', 'solid');
                    return false;
                },
                source: function (request, response){
                    $.ajax(
                        {
                            url: "/dictionary/word_id",
                            data: {
                                word: $('#search-input').val(),
                                dict: $('#choose-dict').val()
                            },
                            success: function (data, status){
                                console.log(data.code + ' ' + data.message);
                                response(data.words);
                            }
                        }
                    )
                }
            }
        ).autocomplete("instance")._renderItem = function (ul, item){
            if ($(ul).children().length >= 10) return $("");
            return $("<li>").css('width', '150px')
                .append( "<div>" + item.word + "<br>").appendTo(ul.css("z-index", "1000000"));
        };
        $('#choose-dict').change(
            function (){
                $('.book-cover').css('background-image', 'url(\'../images/cover/' + $('#choose-dict').val() + '.jpg\')')
                    .css('background-size',  'cover');
                $('.home').css('background', 'none');
                queryWord($('.word').attr('name'));
            }
        );
        $('.headline').click(
            function(){
                $('.main-item-current').attr('class', 'main-item');
                $('.home').css('display', 'unset');
                $('.nav-item-current').attr('class', 'nav-item');
                startSakura();
                clearInterval(curIntervalId);
                $.get(
                    "/dictionary/title", null, function(data, status){
                        let text = data.src;
                        if (data.trans != null) text += "\n" + data.trans;
                        $("#src-text").text(text);
                    }
                );
                index = 0;
                document.getElementById("home-title-text").innerHTML = blank;
                setInterval(innertype, 100);
            }
        );
        $('.add2book').click(
            function(){
                word = $('.word').attr('name');
                if (word==null || word == "") {alert("没有输入任何单词哦"); return;}
                $.get(
                    '/collection/addword',
                    {
                        word: word
                    },
                    function (data, status){
                        if (data.code == 0) alert("添加成功");
                        else alert(data.message);
                    }
                )
            }
        );
        $('.collect').click(
          function (){
              let id = $(this).attr('id');
              if (id === 'song'){
                  let ls = $('#neteaseplayer').attr('src').match(/id=(\d+)/);
                  if (ls.length > 0){
                      let songId = ls[1];
                      $.get(
                          '/collection/addsong',
                          {
                              songId: songId
                          },
                          function (response, status) {
                              if (response.code == '0'){
                                  alert("收藏成功");
                              }
                          }
                      )
                  }
              }
              else if (id === 'sentence'){
                  let senId = $('#sen-en').attr('value');
                  $.get(
                      '/collection/addsentence',
                      {id: senId},
                      function (response, status){
                          if (response.code == '0'){
                              alert('收藏成功');
                          }
                      }
                  );
              }
              else if (id === 'practice'){
                let pracId = $('#practice').attr('name');
                console.log(pracId);
                $.get(
                    '/collection/addpractice',
                    {id: pracId},
                    function (response, status){
                        if (response.code == '0'){
                            alert('收藏成功');
                        }
                    }
                );
            }
          }
        );

        //导航栏动画
        $(document).scroll(function (){
            let header = $('.header');
           if (header.offset().top > 40) header.css('background-color', 'rgba(255, 255, 255, 0.9)');
           else header.css('background-color', 'rgba(255, 255, 255, 0)');
        });

        //加载樱花动画
        var index=0;
        document.getElementById("home-title-text").innerHTML = "";
        function innertype(){
            if (index >= 100) clearInterval(curIntervalId);
            document.getElementById("home-title-text").innerText = document.getElementById("src-text").innerHTML.substring(0,index++);
        }
        var curIntervalId = setInterval(innertype, 100);

        //加载用户名
        $.get("/users/userinfo", {email: $.cookie('id')},
            function (data, textStatus, jqXHR) {
                $('#username').text(data.username);
                $('.avatar').attr("src", data.avatar + "?x=" + Math.random());
            }
        );
        //加载每日一曲
        $.get(
            "/dictionary/dailysong",
            null,
            function (data, status){
                $('#song-name').text(data.song_name);
                $('#lyrics').html(data.lyrics.replaceAll("\n", "<br>"));
                $('#neteaseplayer').attr("src", "//music.163.com/outchain/player?type=2&id=" + data.song_id + "&auto=1&height=66")
            }
        );
        //加载每日一句
        $.get(
            '/dictionary/dailysentence',
            null,
            function (data, status) {
                $('#sen-en').text(data.en).attr('value', data.id);
                $('#sen-cn').text(data.cn)
                    .append($("<br>" + "<p>" + data.editor + "</p>"));
            }
        );
        $.get(
            '/dictionary/dailylistening',
            null,
            function(data, status){
                $("#listeningsource").attr('src', data.mp3_url);
                $('#listeningmaterial').html(data.answer);
                $('#practice').attr('name', data.id);
            }
        )
    }
)


function setFocus(){
    $('#search-input').css('width', '150px').css('padding', '1px').focus();
    $('.search_box').css('border-color', 'rgba(255,188,154,0.8)').css('border-style', 'solid');

}
function setBlur(){
    $('#search-input').css('width', '0').css('padding', '0');
    $('.search_box').css('border-style', 'none');
}
function queryWord(word_en){
    if (word_en == "") {alert("没有输入任何单词哦！"); return;}
    const dict = $("#choose-dict").val();
    console.log(dict);
    console.log(word_en);
    let wq_frame = $('#wq-frame');
    wq_frame.attr('src', '/dictionary/query?dict='+ dict + '&word=' + word_en);
    wq_frame.css('background', 'white');
    $('.word').attr('name', word_en);
}
function getWords(word, dict){
    let res;
    $.get(
        "/dictionary/word_id",
        {
            word: word,
            dict: dict
        },
        function (data, status){
            res = data;
        }
    );
    return res;
}
function prevent(ev){
    ev.preventDefault();
}

