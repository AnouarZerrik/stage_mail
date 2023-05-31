package stage_mail;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;




public class WebScraper {

	
	
	public static  List<String> GET_All_Domaine(String url) throws Exception {
	    List<String> domaines = new ArrayList<String>();

        // Charger la page web
		    Document doc = Jsoup.connect(url).get();

		    // Sélectionner le menu Annuaire et ses liens de niveau supérieur
		    Elements Domaines = doc.select("ul.sub-menu").first().select("> li.menu-item-has-children > a");

		    // Afficher le texte des liens pères de Annuaire
		    for (Element domaine : Domaines) {
		        //System.out.println(domaine.text());
		        domaines.add(domaine.text());
		    }

        return domaines;
    }
	
	
	
	public static List<String> GET_All_Activities(List<String> Domaines) throws Exception {
		
	    List<String> activities = new ArrayList<String>();
	    for (String domaine : Domaines) {
	    // Charger la page web
	    String url = "https://www.marocannuaire.org/Annuaire/annuaire.php?domaine=" + domaine;
	    Document doc = Jsoup.connect(url).get();

	    // Sélectionner les activités de domaine
	    Elements Activities = doc.select("div.latest-category-info > ul > li > a");
	   
	    // Afficher le texte des liens pères de Annuaire
	    for (Element activity : Activities) {
	      //  System.out.println(activity.text());
	        activities.add(activity.text());
	    }
	    }

	    return activities;
	}
	
	
	public static Elements GET_All_Entreprises(String Activitie) throws Exception {
	    // Charger la page web
	    String url = "https://www.marocannuaire.org/Annuaire/Activite.php?activite=" + Activitie;
	    Document doc = Jsoup.connect(url).get();

	    // Sélectionner les activités de domaine
	    Elements Entreprises = doc.select("div.content-nowon-newday> h3 > a");

	    // Afficher le texte des liens pères de Annuaire
	    for (Element entreprise : Entreprises) {
	       // System.out.println(entreprise.text());
	    }

	    return Entreprises;
	}
	
	public static Elements GET_All_Entreprises2(String url) throws Exception {
	    // Charger la page web
	    Document doc = Jsoup.connect(url).get();

	    // Sélectionner les entreprises de la page
	    Elements entreprises = doc.select("div.content-nowon-newday> h3 > a");

	    return entreprises;
	}
	
	public static List<String> GET_All_Entreprises_AubergesCamping(List<String> activities) throws Exception {
	    List<String> entreprises = new ArrayList<String>();
        for(String activity:activities) {
	    // URL de la première page de l'activité "Auberges et camping"
	    String url = "https://www.marocannuaire.org/Annuaire/Activite.php?pageNum_re_aff_dernier_anscri_index=1&activite="+activity;

	    // Extraire les entreprises de la première page
	    Elements entreprisesPage = GET_All_Entreprises(activity);
	    entreprises.addAll(entreprisesPage.eachText());

	    // Trouver le nombre total de pages pour cette activité
	    Document doc = Jsoup.connect(url).get();
	    Elements spans = doc.select("h2.nowon-title > span");
	    Element deuxiemeSpan = spans.get(1);
	    int last_page = Integer.parseInt(deuxiemeSpan.text());	
	    System.out.println(last_page);
	    int pageSize = 30; // nombre d'entreprises par page
	    int totalPages = (int) Math.ceil((double) last_page / pageSize);
	    
	    // Parcourir toutes les pages suivantes
	    for (int i = 1; i <= totalPages; i++) {
	        // URL de la page i
	        String url_page = "https://www.marocannuaire.org/Annuaire/Activite.php?pageNum_re_aff_dernier_anscri_index=" + i + "&activite="+activity;

	        // Extraire les entreprises de la page i
	        Elements entreprisesPage_i = GET_All_Entreprises2(url_page);
	        entreprises.addAll(entreprisesPage_i.eachText());
	    }
	    }

	    return entreprises;
	}
	
	public static List<Entreprise> GET_All_Entreprises_Infos(List<String> activities) throws IOException, InterruptedException {
	    List<Entreprise> entreprises = new ArrayList<>();
	    List<Thread> threads = new ArrayList<>();
	    for (String element : activities) {
	        String url = "https://www.marocannuaire.org/Annuaire/Activite.php?activite=" + element;
	        while (url != null) {
	            Document doc = Jsoup.connect(url).get();
	            Elements entrepriseDivs = doc.select("div.content-nowon-newday");

	            for (Element entrepriseDiv : entrepriseDivs) {
	                Thread t = new Thread(new Runnable() {
	                    @Override
	                    public void run() {
	                    	String name = entrepriseDiv.select("h3 > a").text();
				            String activity = entrepriseDiv.select("ul > li:nth-child(1)").text();
				            String city = entrepriseDiv.select("ul > li:nth-child(2)").text();
				            String address = entrepriseDiv.select("p").first().text();
				            String phone = entrepriseDiv.select("p br").first().nextSibling().toString().trim();               
				            String email = entrepriseDiv.select("p a").first().text();
				            String website = entrepriseDiv.select("p a").last().text();

				            // Création d'un objet Entreprise avec les informations extraites et ajout à la liste des entreprises
				            Entreprise entreprise = new Entreprise(name, activity, city, address, phone, email, website);
				            entreprises.add(entreprise);
	                    }
	                });
	                threads.add(t);
	                t.start();
	            }

	            for (Thread t : threads) {
	                t.join();
	            }

	            // Passer à la page suivante si elle existe
	            Elements nextLinks = doc.select("a:contains(»)");
	            if (nextLinks.size() > 0) {
	                url = "https://www.marocannuaire.org/Annuaire/" + nextLinks.first().attr("href");
	            } else {
	                url = null;
	            }
	        }
	    }
	    return entreprises;
	}
	
	public static void Export_of_all_company_data() {	
		try {
			
			GET_All_Entreprises_Infos(GET_All_Activities(GET_All_Domaine("https://www.marocannuaire.org/")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static Connection getConnection() throws SQLException {
        Connection conn = null;
        String url = "jdbc:mysql://localhost:3306/stageproject";
        String user = "root";
        String password = "anoirzerrik2003";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
          //  System.out.println("success");

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failed. Check output console.");
            e.printStackTrace();
        }

        return conn;
    }
	
	public static void insertEntreprises(List<Entreprise> entreprises) {
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    try {
	        conn = getConnection();
	        String query = "INSERT INTO Entreprise (name, activity, address, phone, email, site, city) VALUES (?, ?, ?, ?, ?, ?, ?)";
	        stmt = conn.prepareStatement(query);
	        for (Entreprise entreprise : entreprises) {
	            stmt.setString(1, entreprise.getName());
	            stmt.setString(2, entreprise.getActivity());
	            stmt.setString(3, entreprise.getAddress());
	            stmt.setString(4, entreprise.getPhone());
	            stmt.setString(5, entreprise.getEmail());
	            stmt.setString(6, entreprise.getSite());
	            stmt.setString(7, entreprise.getCity());
	            stmt.executeUpdate();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	       try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }
	}

	
	/**********************************************************************************************/
	
	
	
	public static void main(String[] args) throws Exception {
		
	//	insertEntreprises(GET_All_Entreprises_Infos(GET_All_Activities(GET_All_Domaine("https://www.marocannuaire.org/"))));
	  
	}

}