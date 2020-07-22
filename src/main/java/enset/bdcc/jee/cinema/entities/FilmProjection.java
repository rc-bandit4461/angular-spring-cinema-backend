package enset.bdcc.jee.cinema.entities;

import org.springframework.data.rest.core.config.Projection;

import java.util.Collection;
import java.util.Date;

@Projection(name = "p3", types = {enset.bdcc.jee.cinema.entities.Film.class})
public interface FilmProjection {
    public Long getId();
    public String getTitre();
    public double getDuree();
    public String getRealisateur();
    public String getDescription();
    public String getPhoto();
    public Date getDateSortie();
    public Categorie getCategorie();
}
