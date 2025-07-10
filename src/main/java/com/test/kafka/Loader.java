package com.test.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

//CommandLineRunner — это интерфейс Spring Boot
//автоматически выполняются после полной инициализации Spring-контекста
//Loader запустится только после того, как все сервисы (Kafka, база данных, producer) будут готовы к работе
@Component
public class Loader implements CommandLineRunner {
    private final MessageProducer messageProducer;
    private final String messageFolderPattern;

    public Loader(MessageProducer messageProducer,
                  @Value("classpath:message/*.json") String messageFolderPattern) {
        this.messageProducer = messageProducer;
        this.messageFolderPattern = messageFolderPattern;
    }

    @Override
    public void run(String... args) throws Exception {
        // PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // Resource[] resources = resolver.getResources(messageFolderPattern);
        // System.out.println("Found " + resources.length + " message files.");
        // Arrays.sort(resources, (a, b) -> a.getFilename().compareTo(b.getFilename()));
        // for (Resource resource : resources) {
        //     String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        //     long start = System.nanoTime();
        //     messageProducer.sendMessageSync(content);
        //     long end = System.nanoTime();
        //     System.out.println("Sent " + resource.getFilename() + " in " + ((end - start) / 1_000_000) + " ms");
        // }
    }
} 