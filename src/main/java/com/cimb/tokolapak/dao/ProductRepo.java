	package com.cimb.tokolapak.dao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.cimb.tokolapak.entity.Product;
public interface ProductRepo extends CrudRepository<Product, Integer> {
	public Product findByProductName(String productName);
	@Query(value="SELECT * FROM Product WHERE PRICE >?1 and product_name =?2 ", nativeQuery = true)
	public Iterable<Product> findProductByMinPrice(double minPrice, String productName);
	@Query(value="SELECT * FROM Product WHERE price < :hargaMax and product_name like %:namaProduk%", nativeQuery = true)
	public Iterable<Product> findProductByMaxPrice(@Param("hargaMax") double maxPrice,@Param("namaProduk") String productName);
}