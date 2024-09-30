package com.Nikhil.spring.thymeleaf.file.upload;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.Nikhil.spring.thymeleaf.file.upload.service.FilesStorageService;

@SpringBootApplication
public class ThymeleafFileUploadApplication implements CommandLineRunner {

  @Resource
  FilesStorageService storageService;

  public static void main(String[] args) {
    SpringApplication.run(ThymeleafFileUploadApplication.class, args);
  }

  @Override
  public void run(String... arg) throws Exception {
//    storageService.deleteAll();
    storageService.init();
  }
}
