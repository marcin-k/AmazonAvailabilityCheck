package com.marcink.webhomeone.service;

import com.marcink.webhomeone.schedule.AmazonAvailabilityCheckSchedule;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Random;

@Service
public class AmazonAvailabilityService {

    private static Logger logger = LoggerFactory.getLogger(AmazonAvailabilityService.class);

    private int decider = 0;
    private String[] userAgents = {"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36",
                                   "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0",
                                   "Mozilla/5.0 (iPhone; CPU iPhone OS 12_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148",
                                   "Mozilla/5.0 (iPhone; CPU iPhone OS 14_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Mobile/15E148 Safari/604.1",
                                   "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36 Edg/87.0.664.66",
                                   "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36 OPR/73.0.3856.329",
                                   "Mozilla/5.0 (X11; Linux i686; rv:84.0) Gecko/20100101 Firefox/84.0",
                                   "Mozilla/5.0 (Windows NT 6.3; Trident/7.0; rv:11.0) like Gecko"};

    public boolean checkPS5AvailabilityOnAmazon(){
        Random rn = new Random();
        int userAgentIndex = rn.nextInt(8) + 0;

        String url = "";
        if (decider==0){
            url = "https://www.amazon.co.uk/gp/product/B08LLZ2CWD?pf_rd_r=G1KRWMS6XQCJFB04Z59Y&pf_rd_p=6e878984-68d5-4fd2-b7b3-7bc79d9c8b60&pd_rd_r=f0cc8ee2-9280-411b-9f8e-9e2308220600&pd_rd_w=7oyPc&pd_rd_wg=zPORa&ref_=pd_gw_unk";
            decider=1;
        }
        else {
            //ps5
            url = "https://www.amazon.co.uk/PlayStation-9395003-5-Console/dp/B08H95Y452/ref=sr_1_1?dchild=1&keywords=ps5&qid=1607805429&sr=8-1";
            decider=0;
        }
        Document doc = null;
        try {
            doc = Jsoup.connect(url)
                    .userAgent(userAgents[userAgentIndex])
                    //.userAgent()
                    .get();
            Elements availabilityDiv = doc.select("#availability_feature_div");
            if (availabilityDiv.size() != 0) {
                logger.info(LocalTime.now() + " - " + availabilityDiv.get(0).text());

                if (!(availabilityDiv.get(0).text().contains("out of stock")||availabilityDiv.get(0).text().contains("unavailable"))) {
                    return true;
                }
            }
            else {
                logger.info(LocalTime.now()+" - Empty response from server");
            }

            System.gc();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
