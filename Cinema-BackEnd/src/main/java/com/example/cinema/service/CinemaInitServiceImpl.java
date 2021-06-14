package com.example.cinema.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cinema.dao.CategorieRepository;
import com.example.cinema.dao.CinemaRepository;
import com.example.cinema.dao.FilmRepository;
import com.example.cinema.dao.PlaceRepository;
import com.example.cinema.dao.ProjectionRepository;
import com.example.cinema.dao.SalleRepository;
import com.example.cinema.dao.SeanceRepository;
import com.example.cinema.dao.TicketRepository;
import com.example.cinema.dao.VilleRepository;
import com.example.cinema.entities.Categorie;
import com.example.cinema.entities.Cinema;
import com.example.cinema.entities.Film;
import com.example.cinema.entities.Place;
import com.example.cinema.entities.Projection;
import com.example.cinema.entities.Salle;
import com.example.cinema.entities.Seance;
import com.example.cinema.entities.Ticket;
import com.example.cinema.entities.Ville;

@Service
@Transactional
public class CinemaInitServiceImpl implements IcinemaInitService{
	@Autowired
	private VilleRepository villeRepository;
	@Autowired
	private CinemaRepository cinemaRepository;
	@Autowired
	private SalleRepository salleRepository;
	@Autowired
	private PlaceRepository placeRepository;
	@Autowired
	private FilmRepository filmRepository;
	@Autowired
	private ProjectionRepository projectionRepository;
	@Autowired
	private SeanceRepository seanceRepository;
	@Autowired
	private CategorieRepository categorieRepository;
	@Autowired
	private TicketRepository ticketRepository;

	
	@Override
	public void initVilles() {
		Stream.of("Casablanca","Rabat","Fes","Tanger").forEach(nameVille->{
			Ville ville = new Ville();
			ville.setName(nameVille);
			villeRepository.save(ville);
		});
		
	}

	@Override
	public void initCinemas() {
		villeRepository.findAll().forEach(v->{
			Stream.of("MEGARAMA","IMAX","FOUNOUN","CHAHRAZAD").forEach(
					nameCinema->{
						Cinema cinema = new Cinema();
						cinema.setName(nameCinema);
						//donner nombre aleatoire des nbrSalles 
						//math.random return nbr reel entre 1 et 0 donc les nbrsalles sera entre 3 ou 10 maximum
						cinema.setNombreSalles(3+(int)(Math.random()*7));
						cinema.setVille(v);
						cinemaRepository.save(cinema);
					});
		});
	}

	@Override
	public void initSalles() {
		cinemaRepository.findAll().forEach(cinema->{
			for(int i=0;i<cinema.getNombreSalles();i++) {
				Salle salle = new Salle();
				salle.setName("Salle" +(i+1));
				salle.setCinema(cinema);
				salle.setNombrePlace(15+(int)(Math.random()*20));	
				salleRepository.save(salle);
			}
		});
	}


	@Override
	public void initPlaces() {
		salleRepository.findAll().forEach(salle->{
			for(int i=0;i<salle.getNombrePlace();i++) {
				Place place = new Place();
				place.setNumero(i+1);
				place.setSalle(salle);
				placeRepository.save(place);
			}
		});
	}

	@Override
	public void initSeances() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		Stream.of("11:00","15:30","18:00","20:00").forEach(s->{
			Seance seance = new Seance();
			try {
				seance.setHeureDebut(dateFormat.parse(s));
				seanceRepository.save(seance);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	@Override
	public void initCategories() {
		Stream.of("Drame","Romance","Thriller","Documentary").forEach(cat->{
			Categorie categorie = new Categorie();
			categorie.setName(cat);
			categorieRepository.save(categorie);
		});
	}

	@Override
	public void initFilms() {
		double[] durees=new double[] {2.5,3,1.15,2};
		List<Categorie> categories = categorieRepository.findAll();
		Stream.of("Forest Gump","Game of Thrones","Inception","Invisible Guest","Papillon").forEach(filmName->{
			Film film = new Film();
			film.setTitre(filmName);
			film.setDuree(durees[new Random().nextInt(durees.length)]);
			film.setPhoto(filmName.replaceAll(" ",""));
			film.setCategorie(categories.get(new Random().nextInt(categories.size())));
			filmRepository.save(film);
		});
	}

	@Override
	public void initProjection() {
		double[] prix=new double[] {50,100,120,80};
		List<Film> films = filmRepository.findAll(); 
		villeRepository.findAll().forEach(ville->{
			 ville.getCinemas().forEach(cinema->{
				 cinema.getSalles().forEach(salle->{
					int index = new Random().nextInt(films.size());
					Film film = films.get(index);
					seanceRepository.findAll().forEach(seance->{
							Projection projection = new Projection();
							projection.setFilm(film);
							projection.setDateProjection(new Date());
							projection.setSalle(salle);
							projection.setPrix(prix[new Random().nextInt(prix.length)]);
							projection.setSeance(seance);
							projectionRepository.save(projection);
						}) ;
				 });
			 });
		 });
	}

	@Override
	public void initTickets() {
		int[] projections=new int[] {1,2,3,4,5};

		projectionRepository.findAll().forEach(projection ->{
			projection.getSalle().getPlaces().forEach(place->{
				Ticket ticket = new Ticket();
				ticket.setPlace(place);
				ticket.setPrix(projection.getPrix());
				ticket.setReserve(false);
				ticket.setProjection(projection);
				ticketRepository.save(ticket);
			});
		});
	}
}
