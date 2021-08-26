package be.vdab.luigi.controllers;

import be.vdab.luigi.domain.Pizza;
import be.vdab.luigi.exception.KoersClientException;
import be.vdab.luigi.services.EuroService;
import be.vdab.luigi.services.PizzaService;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("pizzas")
class PizzaController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final EuroService euroService;
    private final PizzaService pizzaService;


    PizzaController(EuroService euroService, PizzaService pizzaService) {
        this.euroService = euroService;
        this.pizzaService = pizzaService;
    }


    @GetMapping
    public ModelAndView pizzas() {
        return new ModelAndView("pizzas", "pizzas", pizzaService.findAll());
    }

    @GetMapping("{id}")
    public ModelAndView pizza(@PathVariable long id){
        var modelAndView = new ModelAndView("pizza");
        pizzaService.findById(id).ifPresent(pizza-> {
                modelAndView.addObject("pizza",pizza);
                try {
                modelAndView.addObject("inDollar",euroService.naarDollar(pizza.getPrijs()));}
                catch (KoersClientException ex){
                    logger.error("Kan dollar niet lezen",ex);
                }
        });
        return modelAndView;
    }
    @GetMapping("prijzen")
    public ModelAndView prijzen(){
        return new ModelAndView("prijzen","prijzen",pizzaService.findUniekePrijzen());
    }
    @GetMapping("prijzen/{prijs}")
    public ModelAndView pizzasMetEenPrijs(@PathVariable BigDecimal prijs){
        return new ModelAndView("prijzen","pizzas",pizzaService.findByPrijs(prijs)).addObject("prijzen",pizzaService.findUniekePrijzen());
    }
    @GetMapping("aantalpizzasperprijs")
    public ModelAndView aantalPizzasPerPrijs(){
        return new ModelAndView("aantalpizzasperprijs","aantalpizzasperprijs",pizzaService.findAantalPizzasPerPrijs());
    }
}
