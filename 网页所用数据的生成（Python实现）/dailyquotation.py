# 美句欣赏，资源来自爱词霸每日一句，爬取并保存到.db文件
import sqlite3

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
    conn = sqlite3.connect("dailysentences.db")
    cursor = conn.cursor()
    cursor.execute("create table if not exists sentences("
                   "id integer primary key,"
                   "en text not null,"
                   "cn text not null,"
                   "editor text not null)")
    for i in range(247, 1000):
        print('\r%d' % i , end='')
        try:
            ls = getQuotation(i)
            for en, cn, editor in ls:
                cursor.execute("insert into sentences(en, cn, editor) values('%s', '%s', '%s')" % (en.replace("'", "''"), cn.replace("'", "''"), editor.replace("'", "''")))
            conn.commit()
        except Exception as e:
            print(e)
            print(i)
            exit()
