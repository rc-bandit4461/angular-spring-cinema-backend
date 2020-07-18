package enset.bdcc.jee.cinema.web;

import enset.bdcc.jee.cinema.dao.FilmRepository;
import enset.bdcc.jee.cinema.dao.TicketRepository;
import enset.bdcc.jee.cinema.entities.Film;
import enset.bdcc.jee.cinema.entities.Ticket;
import lombok.Data;
import lombok.NoArgsConstructor;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
public class CinemaRestController {
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

    @PostMapping("/payerTickets")
    @Transactional
    public List<Ticket> payerTickets(@RequestBody TicketForm ticketForm) {
        List<Ticket> ticketList = new ArrayList<>();
        ticketForm.getTickets().forEach(id -> {
            Ticket ticket = ticketRepository.findById(id).get();
            ticket.setReserve(true);
            ticket.setNomClient(ticketForm.getNomClient());
            ticket.setCodePayment(ticketForm.getCodePayment());
            ticketRepository.save(ticket);
            ticketList.add(ticket);
        });
        return  ticketList;
    }
}

@Data
@NoArgsConstructor
class TicketForm {
    private String nomClient;
    private Integer codePayment;
    private List<Long> tickets = new ArrayList<>();
}
