import sqlite3

import pymysql
import requests
from lxml.etree import HTML


def getQuotation(page):
    url = "http://news.iciba.com/appv3/wwwroot/ds.php?action=history&ob=1&order=2&page=" + str(page) + "#nav"
    rsp = requests.get(url)
    # print(rsp.text)
    tree = HTML(rsp.text)
    en = tree.xpath('//p[@class="c_l_m_en"]/a/text()')
    cn = tree.xpath('//p[@class="c_l_m_cn"]/a/text()')
    editor = tree.xpath('//p[@class="c_l_m_editor"]/text()')
    ls = list(zip(en, cn, editor))
    return ls


if __name__ == '__main__':
    conn = pymysql.connect(host="localhost", port=3306, db="arkueid_learn", user="root", password="kita")
    cursor = conn.cursor()
    cursor.execute("create table if not exists sentences("
                   "id int primary key auto_increment,"
                   "en text not null,"
                   "cn text not null,"
                   "editor text not null)")
    for i in range(257, 1000):
        print('\r%d' % i , end='')
        try:
            ls = getQuotation(i)
            for en, cn, editor in ls:
                cursor.execute("insert into sentences(id, en, cn, editor) values(0, '%s', '%s', '%s')" % (en.replace("'", "''"), cn.replace("'", "''"), editor.replace("'", "''")))
            conn.commit()
        except Exception as e:
            print(e)
            print(i)
            exit()
