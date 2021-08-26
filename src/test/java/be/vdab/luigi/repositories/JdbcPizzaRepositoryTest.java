package be.vdab.luigi.repositories;

import be.vdab.luigi.domain.Pizza;
import be.vdab.luigi.exception.PizzaNietGevondenException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat; import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
@JdbcTest
@Import(JdbcPizzaRepository.class)
@Sql("/insertPizzas.sql")
class JdbcPizzaRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static final String PIZZAS = "pizzas";
    private final JdbcPizzaRepository repository;

    JdbcPizzaRepositoryTest(JdbcPizzaRepository repository) {
        this.repository = repository;
    }
    @Test
    void findAantal(){
        assertThat(repository.findAantal()).isEqualTo(countRowsInTable(PIZZAS));
    }
    @Test
    void findAllGeeftAllePizzasGesorteerdOpID(){
        assertThat(repository.findAll()).hasSize(countRowsInTable(PIZZAS)).extracting(Pizza::getId).isSorted();
    }
    @Test
    void create(){
        var id = repository.create(new Pizza(0,"test2", BigDecimal.TEN,false));
        assertThat(id).isPositive();
        assertThat(countRowsInTableWhere(PIZZAS,"id = "+id)).isOne();
    }
    private long idVanTestPizza(){
        return jdbcTemplate.queryForObject("select id from pizzas where naam = 'test'",Long.class);
    }
    private long idVanTestPizza2(){
        return jdbcTemplate.queryForObject("select id from pizzas where naam = 'test2'",Long.class);
    }
    @Test
    void delete(){
        var id = idVanTestPizza();
        repository.delete(id);
        assertThat(countRowsInTableWhere(PIZZAS,"id = "+id)).isZero();
    }
    @Test
    void findByID(){
        assertThat(repository.findById(idVanTestPizza())).hasValueSatisfying(pizza ->assertThat(pizza.getNaam()).isEqualTo("test"));
    }
    @Test
    void findByOnbestaandeIDVindtGeenPizza(){
        assertThat(repository.findById(-1)).isEmpty();
    }
    @Test
    void update(){
        var id=idVanTestPizza();
        var pizza = new Pizza (id,"test",BigDecimal.TEN,false);
        repository.update(pizza);
        assertThat(countRowsInTableWhere(PIZZAS,"prijs=10 and id="+id)).isOne();
    }
    @Test
    void updateOnbestaandePizzaGeeftEenFout(){
        assertThatExceptionOfType(PizzaNietGevondenException.class).isThrownBy(()->repository.update(new Pizza(-1,"test",BigDecimal.TEN,false)));
    }
    @Test
    void findByPrijsBetween(){
        var van = BigDecimal.ONE;
        var tot = BigDecimal.TEN;
        assertThat(repository.findByPrijsBetween(van,tot)).hasSize(countRowsInTableWhere(PIZZAS,"prijs between 1 and 10")).extracting(Pizza::getPrijs).allSatisfy(prijs->assertThat(prijs).isBetween(van,tot)).isSorted();
    }
    @Test
    void findUniekePrijzenGeeftPrijzenOplopend(){
        assertThat(repository.findUniekePrijzen()).hasSize(jdbcTemplate.queryForObject("select count(distinct prijs) from pizzas",Integer.class)).doesNotHaveDuplicates().isSorted();
    }
    @Test
    void findByPrijs(){
        assertThat(repository.findByPrijs(BigDecimal.TEN)).hasSize(countRowsInTableWhere(PIZZAS,"prijs=10")).allSatisfy(pizza->assertThat(pizza.getPrijs()).isEqualByComparingTo(BigDecimal.TEN)).extracting(Pizza::getNaam).isSortedAccordingTo(String::compareToIgnoreCase);
    }
    @Test
    void findByIDs(){
        long id1 = idVanTestPizza();
        long id2 = idVanTestPizza2();
        assertThat(repository.findByIds(Set.of(id1,id2))).extracting(Pizza::getId).containsOnly(id1,id2).isSorted();
    }
    @Test
    void findByIDsGeeftLegeVerzamelingPizzasBijLegeVerzamelingIDs(){
        assertThat(repository.findByIds(Set.of())).isEmpty();
    }
    @Test
    void findByIDsGeeftLegeVerzamelingBijOnbestaandeIDs(){
        assertThat(repository.findByIds(Set.of(-1L))).isEmpty();
    }
    @Test
    void findAantalPizzasPerPrijs(){
        var aantalPizzasPerPrijs = repository.findAantalPizzasPerPrijs();
        assertThat(aantalPizzasPerPrijs).hasSize(jdbcTemplate.queryForObject("select count(distinct prijs) from pizzas",Integer.class));
        var eersteRij = aantalPizzasPerPrijs.get(0);
        assertThat(eersteRij.getAantal()).isEqualTo(countRowsInTableWhere(PIZZAS,"prijs="+eersteRij.getPrijs()));
    }
}
