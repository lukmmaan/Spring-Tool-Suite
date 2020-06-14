package com.cimb.tokolapak.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cimb.tokolapak.dao.ProductRepo;
import com.cimb.tokolapak.entity.Product;
import com.cimb.tokolapak.service.ProductService;
@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRepo productRepo;
	
	@Override
	@Transactional //untuk undo changes ketika salah satu query gagal
	public Iterable<Product> getProducts() {
		return productRepo.findAll();
	}

	@Override
	@Transactional
	public Optional<Product> getProductById(int id) {
		return productRepo.findById(id);
	}

	@Override
	@Transactional
	public Product addProduct(Product product) {
		product.setId(0);
		return productRepo.save(product);
	}

	@Override
	@Transactional
	public Product updateProduct(Product product) {
		Optional<Product> findProduct = productRepo.findById(product.getId());
		if(findProduct.toString()== "Optional.empty")
			throw new RuntimeException("Product with id " + product.getId() + " doesn't exist");
		return productRepo.save(product);
	}

	@Override
	public void deleteProductById(int id) {
		Optional<Product> findProduct = productRepo.findById(id);
		if(findProduct.toString()== "Optional.empty")
			throw new RuntimeException("Product with id " + id + " doesn't exist");
		productRepo.deleteById(id);
	}

}
