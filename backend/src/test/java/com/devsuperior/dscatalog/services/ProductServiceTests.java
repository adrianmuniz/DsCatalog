package com.devsuperior.dscatalog.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.service.ProductService;
import com.devsuperior.dscatalog.service.exceptions.DataBaseException;
import com.devsuperior.dscatalog.service.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private PageImpl<Product> page;
	private Product product;
	
	
	@BeforeEach
	void setup() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
		dependentId = 4L;
		product = Factory.createProduct();
		page = new PageImpl<>(List.of(product));
		
		//Simulando o comportamento que o deleteById deve fazer quando existe Id
		doNothing().when(repository).deleteById(existingId);
		
		//Simulando o comportamento que o deleteById deve fazer quando não existe Id
		doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
		
		//Simulando quando tenta deletar objeto que tem associação
		doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
		
		//Simulando o comportamento do findAll
		when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		
		//Simulando comportamento do save
		when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		
		//Simulando comportamento do findById
		when(repository.findById(existingId)).thenReturn(Optional.of(product));
		when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExist() {
		
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
		
		verify(repository, Mockito.times(1)).deleteById(existingId);
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
		
		verify(repository, Mockito.times(1)).deleteById(nonExistingId);
	}
	
	@Test
	public void deleteShouldThrowDataBaseExceptionWhenDependentId() {
		
		Assertions.assertThrows(DataBaseException.class, () -> {
			service.delete(dependentId);
		});
		
		verify(repository, Mockito.times(1)).deleteById(dependentId);
	}
}
