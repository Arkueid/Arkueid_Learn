import json
import sqlite3

import pymysql


class LocalDictionary:

    def __init__(self):
        self.__Dictionary = json.loads(open('../oxford_e9.json', encoding='utf-8-sig').read())
        self.__index_word = [*self.__Dictionary['words'].keys()]
        self.__index_idm = [*self.__Dictionary['idms'].keys()]
        self.__index_link = [*self.__Dictionary['links'].keys()]

    def query(self, word):
        if word in self.__index_word:
            result = ('word', self.__Dictionary['words'][word])
        elif word in self.__index_idm:
            result = ('idm', self.__Dictionary['idms'][word])
        elif word in self.__index_link:
            reword = self.__Dictionary['links'][word]
            if reword in self.__index_word:
                result = ('word', self.__Dictionary['words'][reword])
            elif reword in self.__index_idm:
                result = ('idm', self.__Dictionary['idms'][reword])
            else:
                result = None
        else:
            result = None
        return result

    def strf_result(self, result, show_definition=True, show_context=True, show_pron=True, show_pos=True, chn_only=False, show_label=True, show_gram=True, enn_only=False, show_examples=True) -> str:
        type_, query = result
        stf = ''
        if type_ == 'idm':
            idm = query['idm']
            stf += idm
            for i in query['usages']:
                stf += '\n*' + i['label'] if i['label'] and show_label else ''
                stf += '\n' + i['gram'] if i['gram'] and show_gram else ''
                stf += '\n' + i['definition'] if i['definition'] else ''
                stf += '\n' + i['examples'] if i['examples'] and show_examples else ''
            return stf
        if type_ == 'word':
            word = query['query']
            stf += word
            BrE = query['pron']['BrE']
            NAmE = query['pron']['NAmE']
            stf += ('\n' + BrE if BrE and show_pron else '') + (' ' + NAmE if NAmE and show_pron else "")
            if not show_definition:
                return stf
            for i in query['meanings']:
                context = i['context']
                stf += '\n' + context if context and show_context else ''
                for idx, j in enumerate(i['usages']):
                    pos = j['pos']
                    label = j['label']
                    gram = j['gram']
                    if chn_only and not enn_only:
                        definition = j['chn']
                    elif enn_only and not chn_only:
                        definition = j['enn']
                    else:
                        definition = j['definition']
                    examples = j['examples']
                    stf += "\n" + (" " + pos if pos and show_pos else "") + (" " + label if label and show_label else "") + (" " + gram if gram and show_gram else "") + \
                        (" " + definition if definition else "") + \
                        ("\n例句:" + examples if examples and show_examples else "")
            return stf

    def index(self):
        return self.__index_word + self.__index_idm + self.__index_link


if __name__ == '__main__':
    md = LocalDictionary()
    conn = pymysql.connect(host="localhost", port=3306, db="arkueid_learn", user="root", password="kita")
    cursor = conn.cursor()
    cursor.execute("create table if not exists words("
                 "id int primary key auto_increment,"
                 "word_en text not null,"
                 "exp text not null)")
    print(len(md.index()))
    for idx, i in enumerate(md.index()):
        print("\r%s" % idx, end='')
        a = i.replace("'", "''")
        rs = md.query(i)
        if not rs: continue
        exp = md.strf_result(rs,
                             show_examples=False,
                             chn_only=True,
                             enn_only=False,
                             show_label=False,
                             show_context=True,
                             show_gram=False,
                             show_pos=False,
                             show_pron=False,
                             # show_definition=False
                             ).replace("'", "''")
        # print(exp)
        cursor.execute("insert into words (id, word_en, exp) values(0, '%s', '%s')" % (a, exp))
        conn.commit()
