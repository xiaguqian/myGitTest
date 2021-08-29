package com.zlst.demo.video.utils;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

/**
 * Created by xuyh at 2017/11/6 14:03.
 */
@Component
public class HtmlUtil{
    public String getHtml(String url,int i) {
        i++;
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);//新建一个模拟谷歌Chrome浏览器的浏览器客户端对象

        webClient.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常, 这里选择不需要
//        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常, 这里选择不需要
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setCssEnabled(false);//是否启用CSS, 因为不需要展现页面, 所以不需要启用
        webClient.getOptions().setJavaScriptEnabled(true); //很重要，启用JS
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX
        webClient.setJavaScriptEngine(new MyJavaScriptEngine(webClient));

        HtmlPage page = null;
        try {
//            page = webClient.getPage("http://ent.sina.com.cn/film/");//尝试加载上面图片例子给出的网页
            page = webClient.getPage(url);//尝试加载上面图片例子给出的网页

        } catch (Exception e) {
            System.out.println("出现了错误,重试一次" +url);
//            e.printStackTrace();
            try {
                Thread.sleep(6000L);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            if (i >= 60) {
                return "不合法url:" + url;
            }
            return this.getHtml(url,i);
        }finally {
            webClient.close();
        }

        webClient.waitForBackgroundJavaScript(30000);//异步JS执行需要耗时,所以这里线程要阻塞30秒,等待异步JS执行结束

        String pageXml = page.asXml();//直接将加载完成的页面转换成xml格式的字符串

        //TODO 下面的代码就是对字符串的操作了,常规的爬虫操作,用到了比较好用的Jsoup库
        return pageXml;
//        Document document = Jsoup.parse(pageXml);//获取html文档
//        System.out.println(document.html());
//        List<Element> infoListEle = document.getElementById("feedCardContent").getElementsByAttributeValue("class", "feed-card-item");//获取元素节点等
//        infoListEle.forEach(element -> {
//            System.out.println(element.getElementsByTag("h2").first().getElementsByTag("a").text());
//            System.out.println(element.getElementsByTag("h2").first().getElementsByTag("a").attr("href"));
//        });
    }
//    这个输入的是视频播放链接，输出的结果也需要验证，
    public String getHtml(String url) {
        int i  = 0;
        String s =  this.getHtml(url,i);
        return s;
    }
//    这个方法，输入链接，和类型，获取m3u8地址，结果不需要验证
    public String getM3u8(String string,String type){
        Document document = Jsoup.parse(string);//获取html文档
        String result = "";
        Element element = null;
//        System.out.println("输入的url" +string +"类型是" + type);
        switch (type){
            case Constant.DONGMAN:{
                //                如果是动漫板块，
                element = document.getElementById("playleft");
                result = element.select("iframe").first().attr("src");
                //            播放器中放的不是m3u8的地址
//            从改地址获取网站地址和m3u8
                String website = result.substring(0,result.indexOf("com") + 3);
//            System.out.println("网站地址是" + website);
                result = this.getM3u8Second(result);
                return "url?" +website + result;
            }
//            case Constant.SANJI:{
//                element = document.getElementById("playleft");
//                result = element.select("iframe").first().attr("src");
////                此时得到的result，仅仅是website
//                String website = result.substring(0,result.indexOf("com") + 3);
//                result = this.getM3u8Second(result);
//                return "url?" +website + result;
//            }
            case Constant.QITA:{
               element = document.getElementById("playleft");
               result = element.select("iframe").first().attr("src");
                if (!result.endsWith("index.m3u8")) {
                    String website = result.substring(0,result.indexOf("com") + 3);
//            System.out.println("网站地址是" + website);
                    result = this.getM3u8Second(result);
                    return "url?" +website + result;
                }
            }
        }
//        暂定返回无
        return result;
    }

    private String getM3u8Second(String result) {
//        result理论上是链接，暂时不添加验证
        String s = this.getHtml(result);
//        从s中获取m3u8
//        先找到index.m3u8第一次出现的位置
        int end = s.indexOf("index.m3u8") + 10;
        if (end - 50  < 0) {
            s= s.substring(0,end);
        }else {
            s = s.substring(end-50,end);
        }
        int begin = s.lastIndexOf("\"");
        s= s.substring(begin+1);
//        System.out.println(s);
        return s;
    }
}
