  $(document).ready(
      function (){
        getWordBook();
        getAlbum();
        getSentences();
        getPractices();
        $('.confirmBtn').click(
          startTest
        );
      }
  )
  function showPopup(){
    var overlay = document.getElementById("overlay");
    overlay.style.display = "block";
  }
  function hidePopup(){
    var overlay = document.getElementById("overlay");
    overlay.style.display = "none";
  }
  function getWordBook(){
    $.get(
        '/collection/wordbook',
        null,
        function (data, status){
            myWordBook = data.words;
            $('.inp').text(myWordBook[0].word);
          for (let i=0; i < data.words.length; i ++){
              $('#wordls').append($('<div class="nav_left_about one_leve2_div" onclick="getWord(' + i + ')"><div class="two_div">' + data.words[i].word + '</div></div>'));
          }
        }
    )
  }
  function getAlbum(){
    $.get(
        '/collection/album',
        null,
        function (response, status){
            for (let i=0; i < response.songs.length; i++){
                $('#songls').append($('<div class="nav_left_about one_leve2_div song" onclick="getSong(' + response.songs[i].song_id + ')">\
                    <div class="two_div">' +
                    response.songs[i].song_name +
                    '</div>\
            </div>'));
            }
        }
    );
  }
  function getSentences(){
    $.get(
        '/collection/sentences',
        null,
        function (response, status) {
            mySentences = response.sentences;
            for (let i=0; i < response.sentences.length; i ++){
                $('#sentencels').append(
                    $('<div class="nav_left_about one_leve2_div sentence" onclick="getSentence(' + i + ')">\
                        <div class="two_div">' +
                            response.sentences[i].en.substring(0, 20) + "..." +
                        '</div>\
                    </div>')
                )
            }
        }
    );
  }
  function getPractices(){
    $.get(
        '/collection/practices',
        null,
        function (response, status) {
            myPractices = response.practices;
            for (let i=0; i < response.practices.length; i ++){
                $('#practicels').append(
                    $('<div class="nav_left_about one_leve2_div sentence" onclick="getPractice(' + i + ')">\
                        <div class="two_div">' +
                            "听力练习" + response.practices[i].id +
                        '</div>\
                    </div>')
                )
            }
        }
    );
  }
  function startTest(){
    myWordBook = shuffle(myWordBook);
    wordIndex = 0;
    $('.inp').html($('<p style="font-family:Comic Sans MS; font-size:18px; font-weight:bold;">' + myWordBook[0].word + '</p>'));
  }
  function shuffle(array) {
      let len = array.length;
      for (let i=0; i < len; i ++){
          let idx = Math.floor(Math.random() * (len));
          let temp = array[i];
          array[i] = array[idx];
          array[idx] = temp;
      }
      return array;
  }
  function testPrintArray(array){
      for (let i=0; i < array.length; i ++){
          console.log(array[i].word);
      }
  }
  function getSong(songId) {
      $('.inp').html(
          $('<iframe id="neteaseplayer" frameborder="no" border="0" marginwidth="0" marginheight="0" width=330 height=86 src="http://music.163.com/outchain/player?type=2&id=' + songId +'&auto=1&height=66"></iframe>\n')
      );
  }
  function getSentence(id) {
    $('.inp').html(
        $(
            '<p id="sen-en" value="" style="font-family:Comic Sans MS; font-size:18px; font-weight:bold">' + mySentences[id].en +'</p>\n' +
            '<p id="sen-cn" style="font-family:Comic Sans MS; font-size:18px;">\n' + mySentences[id].cn.replaceAll('\n', '<br>') +
            '</p>'
        )
    );
  }
  function getWord(idx){
    $('.inp').html($('<p style="font-family:Comic Sans MS; font-size:18px;">' + myWordBook[idx].exp.replaceAll("\n", "<br>") + '</p>'));
  }
  function getPractice(idx){
    $('.inp').html($('<div class="listening-item">\
    <audio controls>\
        <source id="listeningsource" src="'+ myPractices[idx].mp3_url +'">\
        </audio>\
    <p id="listeningmaterial" style="font-family:Comic Sans MS; font-size:18px;">' + myPractices[idx].answer.replaceAll('\n', '<br>') + '</p>\
    </div>'));
  }
  function nextWord(){
    if (wordIndex >= myWordBook.length - 1) return;
    $('.inp').html($('<p style="font-family:Comic Sans MS; font-size:18px; font-weight:bold;">' + myWordBook[++wordIndex].word + '</p>'));
  }
  function previousWord(){
    if (wordIndex <= 0) return;
    $('.inp').html($('<p style="font-family:Comic Sans MS; font-size:18px; font-weight:bold;">' + myWordBook[--wordIndex].word + '</p>'));
  }
  function showTip(){
    $('.inp').html($('<p style="font-family:Comic Sans MS; font-size:18px;">' + myWordBook[wordIndex].exp.replaceAll("\n", "<br>") + '</p>'));
  }

