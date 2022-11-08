package it.ghismo.corso1.priceart.tests.RepositoryTests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import it.ghismo.corso1.priceart.Application;
import it.ghismo.corso1.priceart.entities.DettaglioListino;
import it.ghismo.corso1.priceart.entities.Listino;
import it.ghismo.corso1.priceart.repository.DettagliListinoRepo;
import it.ghismo.corso1.priceart.repository.ListiniRepo;
 
@SpringBootTest
//@TestPropertySource(locations="classpath:application-list1.properties")
@ContextConfiguration(classes = Application.class)
@TestMethodOrder(OrderAnnotation.class)
public class PrezziRepositoryTest {
    @Autowired
    private DettagliListinoRepo prezziRepository;
    
    @Autowired
    private ListiniRepo listinoRepository;
    
    String IdList = "100";
    String CodArt = "002000301";
    Double Prezzo = 1.00;
    
    @Test
    @Order(1)
    public void testInsListino() {
    	Listino listinoTest = new Listino(IdList, "Listino Test 100", "No");
    	
    	Set<DettaglioListino> dettListini = new HashSet<>();
    	DettaglioListino dettListTest = new DettaglioListino(CodArt, Prezzo, listinoTest);
    	dettListini.add(dettListTest);
    	listinoTest.setDettagli(dettListini);
    	listinoRepository.save(listinoTest);
    	
    	assertThat(listinoRepository
        		.findById(IdList))
    			.isNotEmpty();
    	
    }
    
    @Test
    @Order(2)
	public void testfindByCodArtAndIdList1() {
		//.SelByCodArtAndList(CodArt, IdList))
        assertThat(prezziRepository.findByCodArtAndListinoId(CodArt, IdList).orElse(null))
        		.extracting(DettaglioListino::getPrezzo)
				.isEqualTo(Prezzo);
    }
    
    @Test
    @Transactional
    @Order(3)
	public void testDeletePrezzo() {
    	//prezziRepository.DelRowDettList(CodArt, IdList);
    	prezziRepository.deleteByCodArtAndListinoId(CodArt, IdList);
    	
        assertThat(prezziRepository
        		.findByCodArtAndListinoId(CodArt, IdList))
        		.isEmpty();
    }
    
    @Test
    @Order(4)
	public void testDeleteListino() {
    	Optional<Listino> listinoTest = listinoRepository.findById(IdList);
    	assertThat(listinoTest).isPresent();
    	
    	listinoRepository.delete(listinoTest.get());
    	
    	assertThat(listinoRepository.findById(IdList))
    		.isEmpty();
    			
        assertThat(prezziRepository.findByCodArtAndListinoId(CodArt, IdList))
        	.isEmpty();
    }
    
}




















