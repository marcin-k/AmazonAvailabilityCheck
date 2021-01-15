package com.marcink.webhomeone.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;


@Service
public class SMSService {

    private static Logger logger = LoggerFactory.getLogger(SMSService.class);

    public static boolean notificationSend = false;

    @Value( "${aws.accessKey}" )
    private String accessKey;

    @Value( "${aws.secretKey}" )
    private String secretKey;

    public void sendText(String msg){
        if (notificationSend == false){

            System.setProperty("aws.accessKeyId",accessKey);
            System.setProperty("aws.secretAccessKey",secretKey);

            pubTopic(SnsClient.builder()
                            .region(Region.EU_WEST_1)
                            .credentialsProvider(SystemPropertyCredentialsProvider.create() )
                            .build(),
                            msg,
                    "arn:aws:sns:eu-west-1:331049122939:PriceMonitor");

            notificationSend = true;
        }
    }

    private void pubTopic(SnsClient snsClient, String message, String topicArn) {

        try {
            PublishRequest request = PublishRequest.builder()
                    .message(message)
                    .topicArn(topicArn)
                    .build();

            PublishResponse result = snsClient.publish(request);
            logger.info(result.messageId() + " Message sent. Status was " + result.sdkHttpResponse().statusCode());

        } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static boolean isNotificationSend() {
        return notificationSend;
    }
}
