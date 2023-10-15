package br.com.todoist.todoist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class TodoistApplication {

	/// /usr/bin/env C:\\Program\ Files\\Java\\jdk-17\\bin\\java.exe @C:\\Users\\USURIO~2\\AppData\\Local\\Temp\\cp_9judyd7u9nlu1azgdwa037ajf.argfile br.com.todoist.todoist.TodoistApplication 
	public static void main(String[] args) {
		SpringApplication.run(TodoistApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/users").allowedOrigins("http://localhost:3000");
			}
		};
	}
}
