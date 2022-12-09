import sqlite3
import time
import urllib.request, urllib.parse

import pymysql
from lxml.etree import HTML
from pip import main
import os


def process(url):
    rsp = urllib.request.urlopen(url)
    tree = HTML(rsp.read())
    # 下一页链接
    next_page = tree.xpath('//div[@class="info-relative"]/div[@class="fr"]/a/@href')
    next_page = 'http://www.kekenet.com' + next_page[0] if next_page else None

    # 答案链接
    answer_page = 'http://www.kekenet.com' + tree.xpath('//div[@class="cp_fenye"]/a[3]/@href')[0]
    answer_text = HTML(urllib.request.urlopen(answer_page).read()).xpath('//span[@id="article_eng"]/p/text()')
    answer_text = '\n'.join(answer_text)

    # 当前页音频
    mp3_page = url.replace('kouyi', 'mp3')
    rsp = urllib.request.urlopen(mp3_page)
    tree = HTML(rsp.read())
    mp3_url = tree.xpath('//td[@height="34"]/a/@href')[0]
    return next_page, mp3_url, answer_text


# def save(id, mp3_url, answer_text):

    # root = f'./nums/day{id}/'
    # mp3_path = root + f'day{id}.mp3'
    # answer_path = root + f'day{id}.txt'

    # if os.path.exists(mp3_path) and os.path.exists(answer_path):
    #     print('\n',id, 'already exists.')
    #     return 0
    # if not os.path.exists(root):
        # os.makedirs(root)
    # with open(mp3_path, 'wb') as f:
    #     f.write(urllib.request.urlopen(mp3_url).read())
    # with open(answer_path, 'w') as f:
    #     f.write(answer_text)


if __name__ == "__main__":
    next_page = 'http://www.kekenet.com/kouyi/201306/243200.shtml'  # day1链接
    conn = pymysql.connect(host="localhost", port=3306, db="arkueid_learn", user="root", password="kita")
    cursor = conn.cursor()
    cursor.execute("create table if not exists materials("
                   "id int primary key auto_increment,"
                   "mp3_url text not null,"
                   "answer text not null)")
    for i in range(49):
        next_page, mp3_url, answer_text = process(next_page)
        # save(i+1, mp3_url, answer_text)
        cursor.execute("insert into materials (id, mp3_url, answer) values(0 ,'%s', '%s')" % (mp3_url, answer_text.replace("'", "''")))
        print(f'\r{i+1}/164', end='')
        # time.sleep(1)
        conn.commit()
    next_page = 'https://www.kekenet.com/kouyi/201308/253292.shtml'  # day50链接
    for i in range(49, 164):
        next_page, mp3_url, answer_text = process(next_page)
        # save(i+1, mp3_url, answer_text)
        cursor.execute("insert into materials (id, mp3_url, answer) values(0, '%s', '%s')" % (mp3_url, answer_text.replace("'", "''")))
        conn.commit()
        print(f'\r{i+1}/164', end='')
        time.sleep(1)
        
    
    

