package com.crawler.core;

import com.common.utils.Valid;
import com.crawler.mapper.jpa.HchenStatisticalRepository;
import com.crawler.model.HchenStatistical;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import com.crawler.model.HchenStatistical;

@Service
public class MeipaiProccessor implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(3000)
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");

    private Set<Cookie> cookies;
    @Autowired
    private HchenStatisticalRepository hchenStatisticalRepository;

    @Override
    public void process(Page page) {


    }

    public void login() {
//        System.setProperty("webdriver.chrome.driver", "/Users/lee/chromedriver");
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        try {
            Thread.sleep(1000);
            driver.get("http://172.18.104.167:8080/h7/");
            driver.findElement(By.id("userID")).sendKeys("fyadmin");
            driver.findElement(By.id("password")).sendKeys("123456");
            driver.findElement(By.className("loginBtn")).click();
            cookies = driver.manage().getCookies();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver.get("http://172.18.104.167:8080/SpagoBIQbeEngine/servlet/AdapterHTTP?SBICONTEXT=%2Fh7&ACTION_NAME=WORKSHEET_ENGINE_START_ACTION&SBI_EXECUTION_ROLE=%2Fspagobi%2Fuser&SBI_COUNTRY=CN&SPAGOBI_AUDIT_ID=8606&document=156&NEW_SESSION=TRUE&SBI_LANGUAGE=zh&SBI_HOST=http%3A%2F%2F172.18.104.167%3A8080&SBI_SPAGO_CONTROLLER=%2Fservlet%2FAdapterHTTP&user_id=fyadmin&SBI_EXECUTION_ID=85cde9d1bc7511e8aa33ad36287dab50&isFromCross=false&SBI_ENVIRONMENT=DOCBROWSER");

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            var b = driver.findElement(By.id("ext-comp-1032")).getText();
            var count = 0;
            if(b != null && !b.equals("")){
                count = Integer.parseInt(b);
            }
            for (var i = 0; i < count-1; i++) {
                WebElement webElement = driver.findElement(By.id("ext-gen18"));

                String str = webElement.getAttribute("outerHTML");
                System.out.println(str);
                Thread.sleep(10000);
                Html html = new Html(str);
                System.out.println(html.xpath("//tbody/tr").all());
                var departments = html.xpath("//tbody/tr/td[3]").all();
                var names = html.xpath("//tbody/tr/td[4]").all();
                var queryDatas = html.xpath("//tbody/tr/td[10]").all();
                save(departments,names, queryDatas);


                Thread.sleep(10000);
                driver.findElement(By.id("ext-gen91")).click();

            }


        } catch (Exception e) {
            System.out.println(e);
        }
    }


    private void save(List<String> departments,List<String> names, List<String> queryDatas) {
        Date date = new Date();
        List<HchenStatistical> list = new ArrayList<>();
        for (var j = 0; j <= departments.size(); j++) {
            try {
                var department = departments.get(j);
                var name =  names.get(j);
                var queryData = queryDatas.get(j);

                var start = getCharIndex(department, ">", 2);
                department = department.substring(start + 1);
                department = department.substring(0, getCharIndex(department, "<", 1));
                System.out.println(department);

                start = getCharIndex(name, ">", 2);
                name = name.substring(start+1);
                name = name.substring(0, getCharIndex(name, "<", 1));
                System.out.println(name);

                start = getCharIndex(queryData, ">", 3);
                queryData = queryData.substring(start + 1);
                queryData = queryData.substring(0, getCharIndex(queryData, "<", 1));
                System.out.println(queryData);
                if(queryData.equals("")||queryData==null){
                    continue;
                }
                if (Valid.isNumeric(queryData.trim())) {
                    list.add(new HchenStatistical(department.trim(),Long.parseLong(queryData.trim()),name.trim(),date));
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        hchenStatisticalRepository.saveAll(list);
    }

    private Integer getCharIndex(String str, String ch, Integer count) {
        Matcher slashMatcher = Pattern.compile(ch).matcher(str);
        int mIdx = 0;
        while (slashMatcher.find()) {
            mIdx++;
            if (mIdx == count) {
                break;
            }
        }
        return slashMatcher.start();
    }

    @Override
    public Site getSite() {
        for (Cookie cookie : cookies) {
            site.addCookie(cookie.getName().toString(), cookie.getValue().toString());
        }
        return site.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1");
    }

//    public static void main(String[] args) {
//        List<SpiderListener> spiderListeners = new ArrayList<>();
//        SpiderListener spiderListener = new SpiderListener() {
//            @Override
//            public void onSuccess(Request request) {
//                System.out.println("sucsess");
//            }
//
//            @Override
//            public void onError(Request request) {
//
//            }
//        };
//        MeipaiProccessor meipaiProccessor = new MeipaiProccessor();
//        meipaiProccessor.login();
//        spiderListeners.add(spiderListener);
//        Spider.create(meipaiProccessor)
//                .setSpiderListeners(spiderListeners)
//                .addUrl("http://172.18.104.167:8080/SpagoBIQbeEngine/servlet/AdapterHTTP?SBICONTEXT=%2Fh7&ACTION_NAME=WORKSHEET_ENGINE_START_ACTION&SBI_EXECUTION_ROLE=%2Fspagobi%2Fuser&SBI_COUNTRY=CN&SPAGOBI_AUDIT_ID=8589&document=156&NEW_SESSION=TRUE&SBI_LANGUAGE=zh&SBI_HOST=http%3A%2F%2F172.18.104.167%3A8080&SBI_SPAGO_CONTROLLER=%2Fservlet%2FAdapterHTTP&user_id=fyadmin&SBI_EXECUTION_ID=d429eb2fbc0711e8aa33ad36287dab50&isFromCross=false&SBI_ENVIRONMENT=DOCBROWSER")
//                .thread(5)
//                .start();
//
//    }
}
