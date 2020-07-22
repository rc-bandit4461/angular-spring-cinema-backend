package enset.bdcc.jee.cinema.web;

import enset.bdcc.jee.cinema.dao.*;
import enset.bdcc.jee.cinema.entities.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

@RestController
@CrossOrigin("*")
public class CinemaRestController {
    @Autowired
    private SalleRepository salleRepository;
    @Autowired
    private SeanceRepository seanceRepository;
    @Autowired
    private ProjectionRepository projectionRepository;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @GetMapping(path = "/imageFilm/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] image(@PathVariable("id") Long id) throws Exception {
        Film film = filmRepository.getOne(id);
        String photoName = film.getPhoto();
        File file = new File(System.getProperty("user.home") + "/cinema/images/" + photoName + ".jpg");
        return Files.readAllBytes(file.toPath());
    }

    @PostMapping(path = "/imageFilm/{id}", consumes = MediaType.IMAGE_JPEG_VALUE)
    @Transactional
    public void image(@PathVariable("id") Long id, @RequestBody byte[] myFileBytes) throws Exception {
        Film film = filmRepository.getOne(id);
        film.setPhoto(film.getTitre().replaceAll(" ", "_").toLowerCase());
        String photoName = film.getPhoto();
        filmRepository.save(film);
        File file = new File(System.getProperty("user.home") + "/cinema/images/" + photoName + ".jpg");
        Files.write(file.toPath(), myFileBytes);

    }

    @PostMapping("/payerTickets")
    @Transactional
    public void payerTickets(@RequestBody TicketForm ticketForm) {
        List<Ticket> ticketList = new ArrayList<>();
        ticketForm.getTickets().forEach(id -> {
            Ticket ticket = ticketRepository.findById(id).get();
            ticket.setReserve(true);
            ticket.setNomClient(ticketForm.getNomClient());
            ticket.setCodePayment(ticketForm.getCodePayment());
            ticketRepository.save(ticket);
            ticketList.add(ticket);
        });
    }

    @PostMapping("/getAvailabeSalles")
    @Transactional
    public List<SalleSeances> getAvailableSalles(@RequestBody Request request) {
        System.out.println(request);
        List<SalleSeances> salleSeancesList = new ArrayList<>();
        List<Projection> projections = projectionRepository.getByCinemaAndDateProjection(request.getCinemaId(), request.getDate());
        projections.forEach(projection -> {
            System.out.print(projection.getId() + "-");
        });
        System.out.println();
        List<Salle> salles = salleRepository.getSalleByCinemaId(request.getCinemaId());
        List<Seance> seances = seanceRepository.findAll();
        for (Salle salle : salles) {
            SalleSeances salleSeances = new SalleSeances();
            salleSeances.setSalle(salle);
            salleSeances.getSeances().addAll(seances);
            salleSeancesList.add(salleSeances);
        }
        for (Projection projection : projections) {
            for (SalleSeances salleSeances : salleSeancesList) {
                if (projection.getSalle().getId() == salleSeances.getSalle().getId()) {
                    salleSeances.getSeances().removeIf(seance -> seance.getId() == projection.getSeance().getId());
                }
            }
        }
        salleSeancesList.removeIf(salleSeances -> salleSeances.getSeances().size() == 0);
        return salleSeancesList;
    }

    @Transactional
    @PostMapping("/addProjection")
    public void addProjection(@RequestBody ApiProjection apiProjection) {
        System.out.println(apiProjection);
        List<Ticket> tickets = new ArrayList<>();
        apiProjection.getSalleSeances().forEach(simpleSalleSeance -> {
            Salle salle = salleRepository.getOne(simpleSalleSeance.getSalleId());
            simpleSalleSeance.getSeances().forEach(seanceId -> {
                Projection projection = new Projection();
                Film film = filmRepository.getOne(apiProjection.getFilmId());
                projection.setFilm(film);
                projection.setPrix(apiProjection.getPrix());
                projection.setSalle(salle);
                projection.setSeance(seanceRepository.getOne(seanceId));
                projection.setDateProjection(apiProjection.getDateProjection());
                projectionRepository.save(projection);
                salle.getPlaces().forEach(place -> {
                    Ticket ticket = new Ticket();
                    ticket.setPlace(place);
                    ticket.setPrix(projection.getPrix());
                    ticket.setProjection(projection);
                    ticket.setReserve(false);
                    tickets.add(ticket);
                });
            });
        });
            ticketRepository.saveAll(tickets);
    }
}

@Data
@NoArgsConstructor
class TicketForm {
    private String nomClient;
    private Integer codePayment;
    private List<Long> tickets = new ArrayList<>();
}

@Data
@NoArgsConstructor
@ToString
class Request {
    private Long cinemaId;
    private Date date;
}

@Data
@NoArgsConstructor
class SalleSeances {
    private Salle salle;
    private List<Seance> seances = new ArrayList<>();
}

@Data
@NoArgsConstructor
@ToString
class ApiProjection {
    private Long filmId;
    private Date dateProjection;
    private List<SimpleSalleSeance> salleSeances = new ArrayList<>();
    private double prix;

}

@Data
@NoArgsConstructor
@ToString
class SimpleSalleSeance {
    private Long salleId;
    private List<Long> seances = new ArrayList<>();
}

