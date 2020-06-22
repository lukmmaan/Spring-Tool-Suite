package com.cimb.tokolapak.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cimb.tokolapak.dao.ProductWeekendTaskRepo;
import com.cimb.tokolapak.entity.ProductWeekendTask;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
@RequestMapping("/documentsWeekendTask")
public class documentWeekendTask {
	private String uploadPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images\\";
	@Autowired
	private ProductWeekendTaskRepo productWeekendTaskRepo;
	
	@PostMapping
	public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("userData") String productString) throws JsonMappingException, JsonProcessingException {
		ProductWeekendTask productWeekendTask = new ObjectMapper().readValue(productString, ProductWeekendTask.class);
		Date date = new Date();
		String fileExtension = file.getContentType().split("/")[1];
		String newFileName = "PROD-" + date.getTime() + "." + fileExtension;
		String fileName = StringUtils.cleanPath(newFileName);
		Path path = Paths.get(StringUtils.cleanPath(uploadPath) + fileName);
		try {
			Files.copy(file.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/documents/download/").path(fileName).toUriString();
		productWeekendTask.setImage(fileDownloadUri);
		productWeekendTaskRepo.save(productWeekendTask);
		return fileDownloadUri;
	}
	@PutMapping("/{productId}")
	public String editProduct(@PathVariable int productId,@RequestParam("file") MultipartFile file, @RequestParam("editData") String productString) throws JsonMappingException, JsonProcessingException {
		ProductWeekendTask findProduct = productWeekendTaskRepo.findById(productId).get();
		findProduct = new ObjectMapper().readValue(productString, ProductWeekendTask.class);
		Date date = new Date();
		String fileExtension = file.getContentType().split("/")[1];
		String newFileName = "PROD-" + date.getTime() + "." + fileExtension;
		String fileName = StringUtils.cleanPath(newFileName);
		Path path = Paths.get(StringUtils.cleanPath(uploadPath) + fileName);
		try {
			Files.copy(file.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/documents/download/").path(fileName).toUriString();
		findProduct.setImage(fileDownloadUri);
		productWeekendTaskRepo.save(findProduct);
		return fileDownloadUri;
	}
}
