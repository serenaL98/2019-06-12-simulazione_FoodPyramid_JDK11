package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDAO;

public class Model {
	
	private FoodDAO dao;
	private List<String> elencoIngredienti;
	private List<Condiment> allCondiments;
	
	//grafo semplice, pesato, non orientato
	private Graph<Condiment, DefaultWeightedEdge> grafo;
	private List<Condiment> vertici;
	private Map<Integer, Condiment> mappa;
	private List<Collegamento> collegamenti;
	
	public Model() {
		this.dao = new FoodDAO();
		this.elencoIngredienti = new ArrayList<>();
		this.allCondiments = this.dao.listAllCondiment();
		this.mappa = new HashMap<>();
		this.collegamenti = new ArrayList<>();
	}
	
	public List<String> ingredienti(float cal){
		this.elencoIngredienti = this.dao.prendiIngredientiCalorie(cal);
		return this.elencoIngredienti;
	}
	
	public void creaGrafo() {
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.vertici = new ArrayList<>();
		
		for(Condiment c: this.allCondiments) {
			for(String s: this.elencoIngredienti) {
				if(c.getDisplay_name().equals(s)) {
					this.vertici.add(c);
				}
			}
		}
		
		//VERTICI
		Graphs.addAllVertices(this.grafo, this.vertici);
		
		for(Condiment c: vertici) {
			mappa.put(c.getFood_code(), c);
		}
		
		//ARCHI
		this.collegamenti = this.dao.prendiCollegamenti(mappa);
		//System.out.println(this.dao.prendiCollegamenti(mappa).get(0));
		for(Collegamento c: collegamenti) {
		/*	DefaultWeightedEdge e = this.grafo.getEdge(c.getIngr1(), c.getIngr2());
			
			if(e == null) {
				Graphs.addEdge(this.grafo, c.getIngr1(), c.getIngr2(), c.getPeso());
			}
			*/
			if(!this.grafo.containsEdge(c.getIngr1(), c.getIngr2())) {
				Graphs.addEdge(this.grafo, c.getIngr1(), c.getIngr2(), c.getPeso());
			}
		}
		
	}

	public int numeroVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int numeroArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public String calorieCibi() {
		
		List<Condiment> ordinata = new ArrayList<>(this.vertici);
		Collections.sort(ordinata);
		
		String stampa = "";
		for(Condiment c: ordinata) {
			int peso = 0;
			for(DefaultWeightedEdge e: this.grafo.edgesOf(c)) {
				peso += this.grafo.getEdgeWeight(e);
			}
			stampa += c.getDisplay_name().toUpperCase()+": calorie "+c.getCondiment_calories()+", presente in "+peso+" cibi.\n";
		}
		
		return stampa;
	}
	
	//-------PUNTO 2-------
	private List<Condiment> soluzione;
	private Float maxcal;
	
	public Condiment cercaCondimento(String scelto) {
		for(Condiment c: this.vertici) {
			if(c.getDisplay_name().equals(scelto)) {
				return c;
			}
		}
		return null;
	}
	
	public String dieta(String scelto) {
		
		this.soluzione = new ArrayList<>();
		this.maxcal = (float) 0;
		
		List<Condiment> parziale = new ArrayList<>();
		parziale.add(this.cercaCondimento(scelto));
		List<Condiment> vicini = new ArrayList<>(Graphs.neighborListOf(this.grafo, cercaCondimento(scelto)));
		
		List<Condiment> disponibili = new ArrayList<>(this.vertici);
		disponibili.removeAll(vicini);
		ricorsione(parziale, 0, disponibili);
		
		String stampa = "";
		for(Condiment c: this.soluzione) {
			stampa += c.getDisplay_name()+"\n";
		}
		return stampa;
	}
	
	public void ricorsione(List<Condiment> parziale, int livello, List<Condiment> disponibili) {
		//caso finale
		if(disponibili.size()==0) {
			if(this.contaCalorie(parziale)> this.maxcal) {
				maxcal = contaCalorie(parziale);
				this.soluzione = new ArrayList<>(parziale);
			}
		}
		
		//caso intermedio
		Condiment ultimo = parziale.get(parziale.size()-1);
		
		
		for(Condiment co: disponibili) {
			parziale.add(co);
			
			List<Condiment> copia = new ArrayList<>(disponibili);
			copia.removeAll(Graphs.neighborListOf(this.grafo, co));
			copia.remove(co);
			
			ricorsione(parziale, livello+1, copia);
			parziale.remove(co);
		}
	}
	
	public float contaCalorie(List<Condiment> parziale) {
		float conta = 0;
		for(Condiment c: parziale) {
			conta += c.getCondiment_calories();
		}
		return conta;
	}
}
