package be.vdab.luigi.services;

import be.vdab.luigi.domain.Pizza;
import be.vdab.luigi.dto.AantalPizzasPerPrijs;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PizzaService {
    long create(Pizza pizza);
    void update(Pizza pizza);
    void delete(long id);
    List<Pizza> findAll();
    Optional<Pizza> findById(long id);
    List<Pizza> findByPrijsBetween(BigDecimal van, BigDecimal tot);
    long findAantal();
    List<BigDecimal> findUniekePrijzen();
    List<Pizza> findByPrijs(BigDecimal prijs);
    List<Pizza> findByIds(Set<Long> ids);
    List<AantalPizzasPerPrijs> findAantalPizzasPerPrijs();
}
