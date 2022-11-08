package it.ghismo.corso1.priceart.tests.ControllerTests;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import it.ghismo.corso1.priceart.Application;
import it.ghismo.corso1.priceart.entities.DettaglioListino;
import it.ghismo.corso1.priceart.entities.Listino;
import it.ghismo.corso1.priceart.repository.ListiniRepo;

@TestPropertySource(locations="classpath:application-list100.properties")
@ContextConfiguration(classes = Application.class)
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class PrezziControllerTest {
	 	private MockMvc mockMvc;

	    @Autowired
		private WebApplicationContext wac;
	    
	    @Autowired
	    private ListiniRepo listinoRepository;
	    
	    String IdList100 = "100";
	    String IdList101 = "101";
	    String CodArt = "002000301";
	    Double Prezzo = 1.00;
	    Double Prezzo2 = 2.00;

	    
	    
	    @BeforeEach
		public void setup() {
			this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
			InsertDatiListino(IdList100,"Listino Test 100",CodArt,Prezzo);
			InsertDatiListino(IdList101,"Listino Test 101",CodArt,Prezzo2);
	    }
		
		private void InsertDatiListino(String IdList, String Descrizione, String CodArt, Double Prezzo) {
			Listino listinoTest = new Listino(IdList,Descrizione,"No");
	    	Set<DettaglioListino> dettListini = new HashSet<>();
	    	DettaglioListino dettListTest = new DettaglioListino(CodArt,Prezzo, listinoTest);
	    	dettListini.add(dettListTest);
	    	listinoTest.setDettagli(dettListini);
	    	listinoRepository.save(listinoTest);
		}
		
		@Test
		@Order(1)
		public void testGetPrzCodArt() throws Exception {
			mockMvc
			.perform(
				MockMvcRequestBuilders
				.get("/api/prezzi/cerca/" + CodArt)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$").value("1.0")) 
			.andReturn();
		}

		@Test
		@Order(2)
		public void testGetPrzCodArt2() throws Exception {
			String Url = String.format("/api/prezzi/cerca/%s/%s",CodArt,IdList101);
			mockMvc
			.perform(
				MockMvcRequestBuilders
				.get(Url)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$").value("2.0")) 
			.andReturn();
		}
		
		@Test
		@Order(3)
		public void testDelPrezzo() throws Exception {
			String Url = String.format("/api/prezzi/elimina/%s/%s/",CodArt,IdList100);
			mockMvc
			.perform(
				MockMvcRequestBuilders
				.delete(Url)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("0"))
			.andExpect(jsonPath("$.message").value("Operazione eseguita correttamente sull'elemento [" + CodArt + ", " + IdList100 + "]"))
			.andDo(print());
		}
		
		
		@AfterEach
		public void unsetup() {
			Optional<Listino> listinoTest = listinoRepository.findById(IdList100);
			if(listinoTest.isPresent()) listinoRepository.delete(listinoTest.get());
	    	
	    	listinoTest = listinoRepository.findById(IdList101);
	    	if(listinoTest.isPresent()) listinoRepository.delete(listinoTest.get());
		}
		
		

}
