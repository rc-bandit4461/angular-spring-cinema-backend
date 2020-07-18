package enset.bdcc.jee.cinema.projections;

import enset.bdcc.jee.cinema.entities.Film;
import enset.bdcc.jee.cinema.entities.Salle;
import enset.bdcc.jee.cinema.entities.Seance;
import enset.bdcc.jee.cinema.entities.Ticket;
import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.config.Projection;

import java.util.Collection;
import java.util.Date;

@Projection(name = "p1", types = {enset.bdcc.jee.cinema.entities.Projection.class})
public interface ProjectionProjection {
    public Long getId();

    public double getPrix();

    public Date getDateProjection();

    public Salle getSalle();

    public Film getFilm();

    public Seance getSeance();

    public Collection<Ticket> getTickets();
}
