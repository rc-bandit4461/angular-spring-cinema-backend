package enset.bdcc.jee.cinema;

import enset.bdcc.jee.cinema.entities.*;
import enset.bdcc.jee.cinema.services.ICinemaInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

import java.util.stream.Stream;

@SpringBootApplication
public class CinemaApplication implements CommandLineRunner {
    @Autowired
    private ICinemaInitService cinemaInitService;
    @Autowired
    RepositoryRestConfiguration restConfiguration;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    public static void main(String[] args) {
        SpringApplication.run(CinemaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Stream.of(Role.class,User.class,Ville.class,Categorie.class,Cinema.class, Salle.class,Projection.class,Seance.class,Film.class,Ticket.class,Place.class).forEach(aClass -> {
            restConfiguration.exposeIdsFor(aClass);
        });
        if(!ddlAuto.equals("create")) return;
        cinemaInitService.initRoles();
        cinemaInitService.initUsers();
        cinemaInitService.initVilles();
        cinemaInitService.initCinemas();
        cinemaInitService.initSalles();
        cinemaInitService.initPlaces();
        cinemaInitService.initSeances();
        cinemaInitService.initCategories();
        cinemaInitService.initFilms();
        cinemaInitService.initProjections();
        cinemaInitService.initTickets();
    }
}
