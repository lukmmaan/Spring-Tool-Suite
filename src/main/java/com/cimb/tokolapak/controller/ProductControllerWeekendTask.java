package com.cimb.tokolapak.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cimb.tokolapak.dao.ProductWeekendTaskRepo;
import com.cimb.tokolapak.entity.ProductWeekendTask;

@RestController
@RequestMapping("/productsWeekendTask")
@CrossOrigin
public class ProductControllerWeekendTask {
	@Autowired
	private ProductWeekendTaskRepo productWeekendTaskRepo;
	
	@GetMapping
	public Iterable<ProductWeekendTask> getProducts(){
		return productWeekendTaskRepo.findAll();
	}
	@GetMapping("/{productId}")
	public Optional<ProductWeekendTask> getProductById(@PathVariable int productId){
		return productWeekendTaskRepo.findById(productId);
	}
	@DeleteMapping("/delete/{productId}")
	public void deleteProduct(@PathVariable int productId) {
		ProductWeekendTask findProduct = productWeekendTaskRepo.findById(productId).get();
		if (findProduct.toString() == "Optional.empty") {
			throw new RuntimeException("Product Not Found");
        }
		productWeekendTaskRepo.deleteById(productId);
	}
}
