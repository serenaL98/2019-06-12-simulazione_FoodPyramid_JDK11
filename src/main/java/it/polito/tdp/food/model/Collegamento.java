package it.polito.tdp.food.model;

public class Collegamento {
	
	private Condiment ingr1;
	private Condiment ingr2;
	private Integer peso;
	
	
	public Collegamento(Condiment ingr1, Condiment ingr2, Integer peso) {
		super();
		this.ingr1 = ingr1;
		this.ingr2 = ingr2;
		this.peso = peso;
	}
	
	
	public Condiment getIngr1() {
		return ingr1;
	}
	public void setIngr1(Condiment ingr1) {
		this.ingr1 = ingr1;
	}
	public Condiment getIngr2() {
		return ingr2;
	}
	public void setIngr2(Condiment ingr2) {
		this.ingr2 = ingr2;
	}
	public Integer getPeso() {
		return peso;
	}
	public void setPeso(Integer peso) {
		this.peso = peso;
	}
	
	

}
