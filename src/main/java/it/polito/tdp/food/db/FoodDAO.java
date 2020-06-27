package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Collegamento;
import it.polito.tdp.food.model.Condiment;

public class FoodDAO {

	public List<Food> listAllFood(){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_id"),
							res.getInt("food_code"),
							res.getString("display_name"), 
							res.getInt("portion_default"), 
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"),
							res.getDouble("calories")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> listAllCondiment(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_id"),
							res.getInt("food_code"),
							res.getString("display_name"), 
							res.getString("condiment_portion_size"), 
							res.getDouble("condiment_calories")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<String> prendiIngredientiCalorie(float cal){
		String sql = "SELECT c.condiment_id id, c.display_name nome, c.condiment_portion_size porzione, c.condiment_calories calo" + 
				" FROM condiment c" + 
				" WHERE c.condiment_calories < ? " +
				" ORDER BY c.display_name asc ";
		List<String> lista = new ArrayList<>();
		
		try {
			Connection con = DBConnect.getConnection();
			PreparedStatement st = con.prepareStatement(sql);
			st.setFloat(1, cal);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				String nome = res.getString("nome");
				lista.add(nome);
			}
			
			con.close();
			
		}catch (SQLException e) {
			throw new RuntimeException("ERRORE DB: impossibile prendere gli ingredienti.", e);
		}
		return lista;
	}
	
	public List<Condiment> prendiIngredientiCalorieeeee(float cal){
		String sql = "SELECT c.condiment_id id, fc.food_code cibo, c.display_name nome, c.condiment_portion_size porzione, c.condiment_calories calo" + 
				" FROM food_condiment fc, condiment c" + 
				" WHERE fc.condiment_code = c.condiment_code" + 
				"		AND c.condiment_calories < ? ";
		List<Condiment> lista = new ArrayList<>();
		
		try {
			Connection con = DBConnect.getConnection();
			PreparedStatement st = con.prepareStatement(sql);
			st.setFloat(1, cal);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				
			}
			
			con.close();
			
		}catch (SQLException e) {
			throw new RuntimeException("ERRORE DB: impossibile prendere gli ingredienti.", e);
		}
		return lista;
	}
	//float cal,
	public List<Collegamento> prendiCollegamenti( Map<Integer, Condiment> mappa){
		String sql = "SELECT fc.food_code cibo, fc.condiment_food_code ingr1, f2.condiment_food_code ingr2, COUNT(DISTINCT fc.food_code) peso" + 
				" FROM  condiment c1, condiment c2, food_condiment f2, food_condiment fc" + 
				//" WHERE c1.condiment_calories < ? " + 
				//"		AND c2.condiment_calories < ? " + 
				"		WHERE fc.condiment_food_code = c1.food_code" + 
				"		AND f2.condiment_food_code = c2.food_code" + 
				"		AND fc.food_code = f2.food_code" + 
				"		AND fc.condiment_food_code < f2.condiment_food_code" + 
				" GROUP BY fc.condiment_food_code, f2.condiment_food_code";
		List<Collegamento> lista = new ArrayList<>();
		
		try {
			Connection con = DBConnect.getConnection();
			PreparedStatement st = con.prepareStatement(sql);
			//st.setFloat(1, cal);
			//st.setFloat(2, cal);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				if(mappa.containsKey(res.getInt("ingr1")) && mappa.containsKey(res.getInt("ingr2"))) {
					Condiment co1 = mappa.get(res.getInt("ingr1"));
					Condiment co2 = mappa.get(res.getInt("ingr2"));
					Integer peso = res.getInt("peso");
					
					Collegamento ctemp = new Collegamento(co1, co2, peso);
					
					lista.add(ctemp);
				}
				
			}
			
			con.close();
			
		}catch (SQLException e) {
			throw new RuntimeException("ERRORE DB: impossibile prendere gli ingredienti.", e);
		}
		
		return lista;
	}
}

