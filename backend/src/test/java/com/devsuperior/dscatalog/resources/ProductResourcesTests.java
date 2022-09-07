package com.devsuperior.dscatalog.resources;

import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import com.devsuperior.dscatalog.Resources.ProductResources;
import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.service.ProductService;
import com.devsuperior.dscatalog.tests.Factory;

@WebMvcTest(ProductResources.class)
public class ProductResourcesTests {

	//Chamar os endpoints
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProductService service;
	
	private ProductDTO productDTO;
	private PageImpl<ProductDTO> page;
	
	@BeforeEach
	void setUp() throws Exception {
		
		productDTO = Factory.createProductDTO();
		page = new PageImpl<>(List.of(productDTO));
		
		when(service.findAllPaged(ArgumentMatchers.any())).thenReturn(page);
	}
}
