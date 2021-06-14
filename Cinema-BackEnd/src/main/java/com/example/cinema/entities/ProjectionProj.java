package com.example.cinema.entities;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.rest.core.config.Projection;

import com.example.cinema.entities.Film;
import com.example.cinema.entities.Salle;
import com.example.cinema.entities.Seance;
import com.example.cinema.entities.Ticket;

//types cette projection est appliquer pour notre classe Projection
//on a ajouter.class pour ne pas le confondre avec la classe projection

@Projection(name="p1",types= {com.example.cinema.entities.Projection.class})
public interface ProjectionProj {
	public Long getId();
	public double getPrix();
	public Date getDateProjection();
	public Salle getSalle();
	public Film getFilm();
	public Seance getSeance();
	public Collection<Ticket> getTickets();
	
}

 