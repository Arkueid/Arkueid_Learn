import pymysql
import requests
from selenium.webdriver import Chrome
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options

playlist = "https://music.163.com/#/my/m/music/playlist?id=7772506208"


def getSongIds():
    url = "https://music.163.com/weapi/v6/playlist/detail?csrf_token=a162eade42a89c7873eb34850b4e4318"
    headers = {
        "user-agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36",
        "cookie": '_ga=GA1.1.176381923.1658998641; Qs_lvt_382223=1658998641; _clck=1jmo2nd|1|f3j|0; Qs_pv_382223=2378594076153549300%2C4034814229432429600; _ga_C6TGHFPQ1H=GS1.1.1658998641.1.1.1658998822.0; __bid_n=18398ac847816eb5be4207; _ntes_nnid=1cce89f53d2fa5d9f9134359902b1279,1669511682817; _ntes_nuid=1cce89f53d2fa5d9f9134359902b1279; NMTID=00OT3TynpmL91wbN0PDkjhsdXby-xQAAAGEtqabBA; WEVNSM=1.0.0; WNMCID=mxhfpv.1669511683501.01.0; JSESSIONID-WYYY=b1TNmj6SbcE2S3bB5poKQ8yyEKhYbT5rjMu%2FYgsAc%2FluZUlujrW4SPOHrxxyFdf2RH2u4OkPbF5n6HvlQOIdrYZ%5CWWNsI6%2BC20C%2B6%2Ff9iq%2BBT%2FDWAJX%5CdNvc3io1InZr3dgOSGpa7BlyZlHiHpQj5Opmz%5Cg3FZwpW3CIxjsedgPhYbGC%3A1669513499799; _iuqxldmzr_=33; __snaker__id=n1JQG5kj71jWpJNT; gdxidpyhxdE=ie8U9Iuvf1lnlh%2BdkwNGEKEThfn0Ms0c2fk%2B%2BcXyAA48TJ%2FVBh7KTjLP3J4RMyic%5Cn4Xkh30OC3lQwLTiRVRgqPh8QVIhCYzLipxcjQ67yWdzXbDdozEYNC0a6HnnVjqPi2EB2X%5Cv8onAa%2FJ%2FpvHp2SDozz%5C%2Fv33MpQgPunYqN87G4d%2B%3A1669512600175; YD00000558929251%3AWM_NI=oEBZ2CPh%2BwjF0Tla99DhmYiwkEC%2Fn83BiYdWKo6VIabJ%2F6u3VYerOx0LpeftndcKekp8AR3s432UQKRm4ewnArHGJ9CxKp0iNq%2BthJb6cyZHQhcEtKsn5hnHwnc6yLZMNU8%3D; YD00000558929251%3AWM_NIKE=9ca17ae2e6ffcda170e2e6eed4fc7ca1bf8c98b53bf69a8bb6c55f978a9f87d5478fada19bd262f2aaa4d3bc2af0fea7c3b92aad9b8ebbb14994b8e598e94ef48ca482bc3388b8a3baec7da99b8dd0ed34f19ea7bae75d89b1fab7cc6ba296e1d7f8399687ab93ce74f494c086e580aebcadace861b4e88fb3b279f8edfeb6d44192b3fc86f66683ae88a8c93381a88591b3638eafe595eb42a2eaad9ad94493b8ada6f741b1b2fb89ed44b5b2add7fc489a9382b6bb37e2a3; YD00000558929251%3AWM_TID=t2wFZpV4cvVEFFAQAVKRNJpp3Xm3vwm9; WM_TID=C4sr5k1KTUdBRREUBQLVYN5thuRjpj8i; __csrf=a162eade42a89c7873eb34850b4e4318; MUSIC_U=9180146909a1e349ed172b6282ccb4904b1264753eeb225f3392cdb30725472b993166e004087dd334c737298ff8e8beefbe8a63bc41a38dc0355979d95af9110afe702dc8a5ca35a0d2166338885bd7; ntes_kaola_ad=1; playerid=45629280'
    }
    data = {
        "params": "FhEmqPdzeEQrKk/Sh87mn77anKyoRELjXbKxWotxEfjsYo4Sw9zQV0btzkhR+TBUlf0hhSllkQUwH/P4piwq5VVk57DJtY69U8UTySS2op3eYm69ZQmUziN1ZsJGtyMe/tuFLE72NXWeyKmzo4Tkg/k001qp64D6olQDyiVL88wOv3VhD5eFT4i4NaoyVpq+Wju6DbCPWlXjF+9V2AU+J9ARa0tXqQhHsN7vT/ionwM=",
        "encSecKey": "523d80e9b6189b19ca905f5660a98664262e20dd7cf1482ec1df7edfc1237360c6217c73ca77ed082cbe93aa12d578d6a1bd254c0dc161d5f3c879579d54fc9db0de39ba0d90cccedaee9de26e88a1d93b9a2731ee565c0d2f7575cd1586ad654d92a321898e578f87f9ecb106ff8c6656bcf73477a41a86acda024bd1fd07e3"
    }
    rsp = requests.post(url, headers=headers, data=data).json()
    return list(map(lambda x: x['id'], rsp['playlist']['trackIds']))


def create_browser(headless=False, local_user_data=False):
    options = Options()
    if headless:
        # 无可视化界面操作
        options.add_argument('--headless')
        options.add_argument('--dissable-gpu')
        # 实现规避检测
        options.add_experimental_option('excludeSwitches', ['enable-outomation'])
    if local_user_data:
        # 使用本地用户配置
        options.add_argument(r'user-data-dir=C:\Users\ArcueidBrunestud\AppData\Local\Google\Chrome\User Data')
    service = Service('../chromedriver.exe')
    browser_ = Chrome(service=service, options=options)
    return browser_


def getSong(browser: Chrome, songId):
    url = "https://music.163.com/song?id=" + str(songId)
    browser.get(url)
    iframe = browser.find_element(By.TAG_NAME, 'iframe')
    browser._switch_to.frame(iframe)
    songName = browser.find_element(By.XPATH, '//em[@class="f-ff2"]').text
    # print(songName)
    lyrics = browser.find_element(By.CSS_SELECTOR, "#lyric-content").text
    lyrics = lyrics.split("\n")
    ls = []
    a = ''
    for i in lyrics:
        if i != '' and i != "\n":
            ls.append(i)
    ls.remove("展开") if "展开" in ls else None
    newLyrics = '\n'.join(ls)
    # print(newLyrics)

    browser.find_element(By.CSS_SELECTOR, "a[id='flag_ctrl']").click()

    a = browser.find_element(By.CSS_SELECTOR, 'div[id="flag_more"]').text
    # print(a)
    lyrics = songName + "\n" + newLyrics + a
    return lyrics


if __name__ == '__main__':
    idls = getSongIds()
    browser = create_browser(False, False)
    conn = pymysql.connect(host="localhost", user="root", password="kita", db="arkueid_learn")
    cursor = conn.cursor()
    cursor.execute("create table if not exists songs(id int primary key auto_increment,song_id int not null,lyrics text not null)")
    for ID in idls:
        try:
            lyrics = getSong(browser, ID)
        except Exception as e:
            print(e)
            print(ID)
        cursor.execute("insert into songs(id, song_id, lyrics) values(0, %s, '%s')" % (ID, lyrics.replace("'", "''")))
        conn.commit()
    browser.close()