package cn.edu.upc.dzj.HelloWorld.controller;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class WebCrawler {

    @RequestMapping("/todayinhistory")
    @ResponseBody
    public String whatsTodayInHistory(){
        Connection connection = Jsoup.connect("https://www.baidu.com/s?wd=历史上的今天");
        connection.header("cookie", "BIDUPSID=F195DC616116B330BFFEB19F592B9149; PSTM=1636172469; BD_UPN=12314753; __yjs_duid=1_1acb4ec39e49c5893cf11aeb226694bd1636173906228; BDUSS=TJPanVhVH5FV3JuVGVxUm5vY2FJbWt3aXdxdE9VdnFidlNEcFA2T2d2WGFTYTloRVFBQUFBJCQAAAAAAAAAAAEAAABvoPmGVGhlR3JhcGVmcnVpdAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAANq8h2HavIdhO; BDUSS_BFESS=TJPanVhVH5FV3JuVGVxUm5vY2FJbWt3aXdxdE9VdnFidlNEcFA2T2d2WGFTYTloRVFBQUFBJCQAAAAAAAAAAAEAAABvoPmGVGhlR3JhcGVmcnVpdAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAANq8h2HavIdhO; H_WISE_SIDS=107311_110085_127969_176399_179347_184716_189256_189659_191068_191242_192384_193246_194085_194530_195329_195343_195467_195757_196051_196427_196514_197242_197711_197958_198271_198930_199023_199082_199152_199466_199490_199579_199842_200150_200350_200744_200993_201054_201108_201233_201549_201553_201702_201970_201978_202297_202476_202563_202759_202848_202906_203172_203195_203316_203495_203519_203525_203543_203605_203629_204100_204113_204131_204155_204265_204431_204667_204816_204908_204966_205009_205218_205220_205238_205239; BAIDUID=C43AC3FA7408D87D73CEB4B0D20A490E:SL=0:NR=10:FG=1; BDORZ=B490B5EBF6F3CD402E515D22BCDA1598; BAIDUID_BFESS=C43AC3FA7408D87D73CEB4B0D20A490E:SL=0:NR=10:FG=1; BA_HECTOR=a40004252l2g808k3d1h6ibku0r; channel=baidusearch; COOKIE_SESSION=34_1_5_5_17_13_0_2_5_5_8_2_33_0_5_0_1651117011_1651116976_1651117006|9#247292_53_1651116972|9; BD_HOME=1; H_PS_PSSID=36184_36309_31660_34813_35912_36165_34584_36339_35978_35802_36234_26350_36349_36311_36061; delPer=1; BD_CK_SAM=1; PSINO=1; sug=0; sugstore=0; ORIGIN=2; bdime=0; H_PS_645EC=ba9doKUMhcK643hMqAKtT6or8OH2E7+Zg3M8TnaqciK5bqzY/NJ6q3sbC4A; baikeVisitId=a62d9ad6-cc7e-43e8-8cd1-2b26fb2baff8; ab_sr=1.0.1_ZmU1ZGQwMTVmYWZmN2RhZGE1NjMzY2QwNjA2YjliYzJjM2Q5MDY3MTEyMTVhMzcwODczYTQ1NDg2MmEzYTMxZGE1OTY1MTQwZTg3MTUyNTg2NjI5NzkxMmU1MDMwYjcwMDVjMmRkZTMxZGJhMmUyY2RkMGRmMTlhNTIyN2FmYTFhMDg5NDIyMDMwNTA4ZGM0ODRmOGU0MjBiNGFjNDI5YjdhYmM2ZTJiOTk2MjI1ZThjYWUxMTIwZmFhNGIyZThh; RT=z=1&dm=baidu.com&si=pcbdyazod3&ss=l2ih1azg&sl=0&tt=0&bcn=https://fclog.baidu.com/log/weirwood?type=perf&ul=26i&hd=28f");
        try{
            Document document = connection.get();
            Elements els1 = document.select("p[class=\"title_3qCGt\"]");
            Elements els2 = document.select("div[class=\"year-tag_3Iqeg\"]");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
            StringBuilder res = new StringBuilder("<h3 class=\"whatstoday-headline\">今天是" + simpleDateFormat.format(new Date()) + ", 历史上的今天</h3>");
            for (int i=0; i < els1.size() ; i++)
            {
                res.append("<p class=\"whatstoday\">");
                res.append(els2.get(i).text()).append(", ");
                if (els1.get(i).attr("title").isEmpty()) {
                    res.append(els1.get(i).text());
                }
                else res.append(els1.get(i).attr("title"));
                res.append("</p>");
            }
            return res.toString();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return "null";
    }

}
