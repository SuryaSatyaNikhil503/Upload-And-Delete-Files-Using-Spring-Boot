package com.Nikhil.spring.thymeleaf.file.upload.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.Nikhil.spring.thymeleaf.file.upload.model.FileInfo;
import com.Nikhil.spring.thymeleaf.file.upload.service.FilesStorageService;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@RestController // RestController to provide JSON responses by default
@RequestMapping("/api/files")
public class FileController {

  @Autowired
  FilesStorageService storageService;

  @GetMapping
  public ResponseEntity<List<FileInfo>> listFiles() {
    List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
      String filename = path.getFileName().toString();
      String url = MvcUriComponentsBuilder
              .fromMethodName(FileController.class, "getFile", path.getFileName().toString()).build().toString();

      return new FileInfo(filename, url);
    }).collect(Collectors.toList());

    return ResponseEntity.ok(fileInfos);
  }

  @PostMapping("/upload")
  public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
    try {
      storageService.save(file);
      String message = "Uploaded the file successfully: " + file.getOriginalFilename();
      return ResponseEntity.ok(message);
    } catch (Exception e) {
      String message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
      return ResponseEntity.status(500).body(message);
    }
  }

  @GetMapping("/{filename:.+}")
  public ResponseEntity<Resource> getFile(@PathVariable String filename) {
    Resource file = storageService.load(filename);
    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }

  @DeleteMapping("/delete/{filename:.+}")
  public ResponseEntity<String> deleteFile(@PathVariable String filename) {
    try {
      boolean existed = storageService.delete(filename);
      if (existed) {
        return ResponseEntity.ok("Deleted the file successfully: " + filename);
      } else {
        return ResponseEntity.status(404).body("The file does not exist!");
      }
    } catch (Exception e) {
      String message = "Could not delete the file: " + filename + ". Error: " + e.getMessage();
      return ResponseEntity.status(500).body(message);
    }
  }
}
