package com.romaskull.summarytgbot;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.model.HttpApiV2ProxyRequest;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Initializer implements RequestHandler<HttpApiV2ProxyRequest, AwsProxyResponse> {

    private static SpringBootLambdaContainerHandler<HttpApiV2ProxyRequest, AwsProxyResponse> handler;

    static {
        try {
            //handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(SummaryTgBotApplication.class);
            handler = SpringBootLambdaContainerHandler.getHttpApiV2ProxyHandler(SummaryTgBotApplication.class);
        } catch (ContainerInitializationException e) {
            log.error("Ошибка инициализации лямбда-контекста", e);
        }
    }

    @SneakyThrows
    @Override
    public AwsProxyResponse handleRequest(HttpApiV2ProxyRequest input, Context context) {
        return handler.proxy(input, context);
    }
}
