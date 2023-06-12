package com.chicmic.eNaukri;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync(proxyTargetClass = true)
public class ENaukriApplication {
	@Value("${stripe.api.key}")
	private String stripeApiKey;
	@PostConstruct
	public void setup(){
		Stripe.apiKey= stripeApiKey;
	}

	public static void main(String[] args) {
		SpringApplication.run(ENaukriApplication.class, args);
	}


	@Bean(name = "threadPoolTaskExecutor")
	public Executor executor(){
		ThreadPoolTaskExecutor taskExecutor=new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(3);
		taskExecutor.setMaxPoolSize(5);
		taskExecutor.setQueueCapacity(700);
		taskExecutor.initialize();
		return taskExecutor;
	}

	 public static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
