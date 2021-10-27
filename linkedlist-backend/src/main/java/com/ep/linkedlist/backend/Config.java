package com.ep.linkedlist.backend;

import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mangofactory.swagger.configuration.SpringSwaggerConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;

import javax.annotation.PostConstruct;

/**
 * Created by jiwon on 2016-09-21.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {
        "com.ep.linkedlist.backend"
})
@Import(SpringSwaggerConfig.class)
public class Config extends WebMvcConfigurerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(Config.class);

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("*.html").addResourceLocations("/");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @PostConstruct
    public void firebaseInit() throws IOException {
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setServiceAccount(new ClassPathResource("LinkedList-b7f2a1c3ed67.json").getInputStream())
                .setDatabaseUrl("https://linkedlist-566ea.firebaseio.com/")
                .build();

        FirebaseApp.initializeApp(options);
    }

    @Bean
    public FirebaseDatabase getFirebaseDatabase() throws IOException {
        return FirebaseDatabase.getInstance();
    }

    @Bean
    public Gson getPrettyGson(){
        return new GsonBuilder().setPrettyPrinting().create();
    }

    @Bean
    public JacksonFactory getJacksonFactory(){
        return new JacksonFactory();
    }

}
