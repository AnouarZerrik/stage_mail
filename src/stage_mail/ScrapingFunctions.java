package stage_mail;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class ScrapingFunctions {
	
	public static Elements GET_All_Ville(String url) throws Exception {
        // Charger la page web
		    Document doc = Jsoup.connect(url).get();

		    // Sélectionner le menu Annuaire et ses liens de niveau supérieur
		    Elements villes = doc.select("div.item-extra-menu > ul > li > a");

		    // Afficher le texte des liens pères de Annuaire
		  /*  for (Element menu : Domaines) {
		        System.out.println(menu.text());
		    }*/

        return villes;
    }
	
	
	public static void insertVillesToDatabase(String url) throws Exception {
	    // Charger la page web et récupérer toutes les villes
	    Elements villes = GET_All_Ville(url);

	    // Établir une connexion à la base de données
	    Connection conn =getConnection();

	    // Préparer la requête SQL d'insertion des villes
	    PreparedStatement stmt = conn.prepareStatement("INSERT INTO ville (nom) VALUES (?)");
        int cpt=0;
	    // Parcourir la liste des villes et les insérer dans la base de données
	    for (Element ville : villes) {
	        String nomVille = ville.text();
	        stmt.setString(1, nomVille);
	        stmt.executeUpdate();
	        cpt++;
	        System.out.println(cpt);
	    }

	    // Fermer les ressources
	    stmt.close();
	    conn.close();
	}

	
	public static Elements GET_All_Domaine_Ville(Elements villes) throws InterruptedException {
	    List<Thread> threads = new ArrayList<>();
	    Elements domainesTot = new Elements();
	    for (Element ville : villes) {
	        Thread t = new Thread(() -> {
	            try {
	                // Charger la page web
	                String url = "https://www.marocannuaire.org/Annuaire/annuaire_ville.php?ville=" + ville.text();
	                Document doc = Jsoup.connect(url).get();

	                // Sélectionner les activités de domaine
	                Elements domaines = doc.select("ul.featurred-cat-newday > li > a");
	                synchronized (domainesTot) {
	                    domainesTot.addAll(domaines);
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        });
	        threads.add(t);
	        t.start();
	    }
	    for (Thread t : threads) {
	        t.join();
	    }
	    return domainesTot;
	}
	
	
	public static Elements GET_All_Activities_Villes(Elements domaines) throws InterruptedException {
	    List<Thread> threads = new ArrayList<>();
	    Elements ActivitiesTot = new Elements();
	    for (Element domaine : domaines) {
	        Thread t = new Thread(() -> {
	            try {
	                // Charger la page web
	                String url = "https://www.marocannuaire.org/Annuaire/detail_annuaire_ville.php?domaine=" + domaine.text();
	                Document doc = Jsoup.connect(url).get();

	                // Sélectionner les activités de domaine
	                Elements Activities = doc.select("div.latest-category-info > ul > li > a");
	                synchronized (ActivitiesTot) {
	                    ActivitiesTot.addAll(Activities);
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        });
	        threads.add(t);
	        t.start();
	    }
	    for (Thread t : threads) {
	        t.join();
	    }
	    return ActivitiesTot;
	}

	
	public static Connection getConnection() throws SQLException {
        Connection conn = null;
        String url = "jdbc:mysql://localhost:3306/stageproject";
        String user = "root";
        String password = "";

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

	
	public static void insertActivite(List<Element> activities) {
	    Connection conn = null;
	    int cpt=0;
	    PreparedStatement stmt = null;
	    try {
	        conn = getConnection();
	        String query = "INSERT INTO activite_v (nom) VALUES (?)";
	        stmt = conn.prepareStatement(query);
	        for (Element activite : activities) {
	        	cpt++;
	        	System.out.println(cpt);
	            stmt.setString(1,activite.text());
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
	
	
	public static Elements GET_All_Entreprises(String Activitie) throws Exception {
	    // Charger la page web
	    String url = "https://www.marocannuaire.org/Annuaire/Activite.php?activite=" + Activitie;
	    Document doc = Jsoup.connect(url).get();

	    // Sélectionner les activités de domaine
	    Elements Entreprises = doc.select("div.content-nowon-newday> h3 > a");

	    // Afficher le texte des liens pères de Annuaire
	    for (Element entreprise : Entreprises) {
	        System.out.println(entreprise.text());
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
	
	public static List<String> GET_All_Entreprises_AubergesCamping(String Activitie) throws Exception {
	    List<String> entreprises = new ArrayList<String>();

	    // URL de la première page de l'activité "Auberges et camping"
	    String url = "https://www.marocannuaire.org/Annuaire/Activite.php?pageNum_re_aff_dernier_anscri_index=1&activite="+Activitie;

	    // Extraire les entreprises de la première page
	    Elements entreprisesPage = GET_All_Entreprises(Activitie);
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
	        String url_page = "https://www.marocannuaire.org/Annuaire/Activite.php?pageNum_re_aff_dernier_anscri_index=" + i + "&activite="+Activitie;

	        // Extraire les entreprises de la page i
	        Elements entreprisesPage_i = GET_All_Entreprises2(url_page);
	        entreprises.addAll(entreprisesPage_i.eachText());
	    }

	    return entreprises;
	}
	
	
	public static List<Entreprise> GET_All_Entreprises_Infos(String activitie) throws IOException, InterruptedException {
	    List<Entreprise> entreprises = new ArrayList<>();	    
	        String url = "https://www.marocannuaire.org/Annuaire/Activite.php?activite=" + activitie;
	            Document doc = Jsoup.connect(url).get();
	            Elements entrepriseDivs = doc.select("div.content-nowon-newday");
	            for (Element entrepriseDiv : entrepriseDivs) {
	                    	String name = entrepriseDiv.select("h3 > a").text();
				            String activity = entrepriseDiv.select("ul > li:nth-child(1)").text();
				            String city = entrepriseDiv.select("ul > li:nth-child(2)").text();
				            String texte = entrepriseDiv.select("p").first().html();
	                        // Récupération de l'adresse à partir du texte en utilisant la méthode split
	                        String address = texte.split("<br>")[0];
				            String phone = entrepriseDiv.select("p br").first().nextSibling().toString().trim();               
				            String email = entrepriseDiv.select("p a").first().text();
				            String website = entrepriseDiv.select("p a").last().text();

				            // Création d'un objet Entreprise avec les informations extraites et ajout à la liste des entreprises
				            Entreprise entreprise = new Entreprise(name, address, phone, email, activity, city, website);
				            entreprises.add(entreprise);
	                    }

	            // Trouver le nombre total de pages pour cette activité
			    Elements spans = doc.select("h2.nowon-title > span");
			    Element deuxiemeSpan = spans.get(1);
			    int last_page = Integer.parseInt(deuxiemeSpan.text());	
			    int pageSize = 30; // nombre d'entreprises par page
			    int totalPages = (int) Math.ceil((double) last_page / pageSize); 
			    // Parcourir toutes les pages suivantes
			    for (int i = 1; i <= totalPages; i++) {
			        // URL de la page i
			        String url_page = "https://www.marocannuaire.org/Annuaire/Activite.php?pageNum_re_aff_dernier_anscri_index=" + i + "&activite="+activitie;
			        Document doc2 = Jsoup.connect(url_page).get();
			        // Extraire les infos de la page i
			        Elements entrepriseDivs2 = doc2.select("div.content-nowon-newday");
		            for (Element entrepriseDiv : entrepriseDivs2) {  
		            	String name = entrepriseDiv.select("h3 > a").text();
			            String activity = entrepriseDiv.select("ul > li:nth-child(1)").text();
			            String city = entrepriseDiv.select("ul > li:nth-child(2)").text();
			            String texte = entrepriseDiv.select("p").first().html();
                        // Récupération de l'adresse à partir du texte en utilisant la méthode split
                        String address = texte.split("<br>")[0];
			            String phone = entrepriseDiv.select("p br").first().nextSibling().toString().trim();               
			            String email = entrepriseDiv.select("p a").first().text();
			            String website = entrepriseDiv.select("p a").last().text();
                        // Création d'un objet Entreprise avec les informations extraites et ajout à la liste des entreprises
                        Entreprise entreprise = new Entreprise(name, address, phone, email, activity, city, website);
                        entreprises.add(entreprise);
		                    }
		               
			        
			       
			    }
	            
	    return entreprises;
	}
	
	
	
	
	public static List<Entreprise> GET_All_Entreprises_Infos2(String activitie) throws InterruptedException {
	    List<Entreprise> entreprises = new ArrayList<>();

	    String url = "https://www.marocannuaire.org/Annuaire/Activite.php?activite=" + activitie;
	    Document doc = null;
	    try {
	        doc = Jsoup.connect(url).get();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    Elements entrepriseDivs = doc.select("div.content-nowon-newday");
	    int nbThreads = 4; // Nombre de threads utilisés
	    int nbElementsParThread = entrepriseDivs.size() / nbThreads;
	    CountDownLatch latch = new CountDownLatch(nbThreads);

	    for (int i = 0; i < nbThreads; i++) {
	        int debut = i * nbElementsParThread;
	        int fin = (i == nbThreads - 1) ? entrepriseDivs.size() : (i + 1) * nbElementsParThread;
	        List<Element> subList = entrepriseDivs.subList(debut, fin);
	        new Thread(() -> {
	            for (Element entrepriseDiv : subList) {
	                String name = entrepriseDiv.select("h3 > a").text();
	                String activity = entrepriseDiv.select("ul > li:nth-child(1)").text();
	                String city = entrepriseDiv.select("ul > li:nth-child(2)").text();
	                String texte = entrepriseDiv.select("p").first().html();
	                // Récupération de l'adresse à partir du texte en utilisant la méthode split
	                String address = texte.split("<br>")[0];
	                String phone = entrepriseDiv.select("p br").first().nextSibling().toString().trim();
	                String email = entrepriseDiv.select("p a").first().text();
	                String website = entrepriseDiv.select("p a").last().text();

	                // Création d'un objet Entreprise avec les informations extraites et ajout à la liste des entreprises
	                Entreprise entreprise = new Entreprise(name, address, phone, email, activity, city, website);
	                entreprises.add(entreprise);
	            }
	            latch.countDown();
	        }).start();
	    }

	    // Attendre que tous les threads aient fini avant de continuer
	    latch.await();

	    // Trouver le nombre total de pages pour cette activité
	    Elements spans = doc.select("h2.nowon-title > span");
	    Element deuxiemeSpan = spans.get(1);
	    int last_page = Integer.parseInt(deuxiemeSpan.text());
	    int pageSize = 30; // nombre d'entreprises par page
	    int totalPages = (int) Math.ceil((double) last_page / pageSize);

	    // Parcourir toutes les pages suivantes
	    for (int i = 1; i <= totalPages; i++) {
	        // URL de la page i
	        String url_page = "https://www.marocannuaire.org/Annuaire/Activite.php?pageNum_re_aff_dernier_anscri_index=" + i + "&activite=" + activitie;
	        Document doc2 = null;
	        try {
	            doc2 = Jsoup.connect(url_page).get();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        // Extraire les infos de la page i
	        Elements entrepriseDivs2 = doc2.select("div.content-nowon-newday");
	        for (Element entrepriseDiv : entrepriseDivs2) {
	            String name = entrepriseDiv.select("h3 > a").text();
	            String activity = entrepriseDiv.select("ul > li:nth-child(1)").text();
	            String city = entrepriseDiv.select("ul > li:nth-child(2)").text();
	            String texte = entrepriseDiv.select("p").first().html();
	            // Récupération de l'adresse à partir du texte en utilisant la méthode split
	            String address = texte.split("<br>")[0];
	            String phone = entrepriseDiv.select("p br").first().nextSibling().toString().trim();
	            String email = entrepriseDiv.select("p a").first().text();
	            String website = entrepriseDiv.select("p a").last().text();
	            // Création d'un objet Entreprise avec les informations extraites et ajout à la liste des entreprises
	            Entreprise entreprise = new Entreprise(name, address, phone, email, activity, city, website);
	            entreprises.add(entreprise);
	            
	        }
        
    }
		return entreprises;
}

	public static void Export_database_data_activities() throws ClassNotFoundException {
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    
	    try {
	       
	        conn = getConnection();
	        
	        // Exécuter la requête SQL pour récupérer les informations de la table
	        String query = "SELECT nom FROM activite_v";
	        stmt = conn.prepareStatement(query);
	        rs = stmt.executeQuery();
	        
	        // Parcourir les résultats de la requête et afficher les informations
	        while (rs.next()) {
	            String nom = rs.getString("nom");  
	           // System.out.println(nom);
	       // Utiliser les informations récupérées comme paramètres pour une autre fonction
	            try {
					GET_All_Entreprises_Infos2(nom);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        // Fermer les ressources JDBC pour libérer la mémoire et éviter les fuites de connexion
	        if (rs != null) {
	            try {
	                rs.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        if (stmt != null) {
	            try {
	                stmt.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        if (conn != null) {
	            try {
	                conn.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}

	
	public static void insertEntreprises(List<Entreprise> entreprises) {
	    Connection conn = null;
	    int cpt=0;
	    PreparedStatement stmt = null;
	    try {
	        conn = getConnection();
	        String query = "INSERT INTO entreprise (nom, activité, address, téléphone, email, site, ville) VALUES (?, ?, ?, ?, ?, ?, ?)";
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
	            cpt++;
	            System.out.println(cpt);
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

	public static void main(String[] args) throws InterruptedException, Exception {
		
	/*	try {
			insertVillesToDatabase("https://www.marocannuaire.org/");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	/*	try {
			Export_database_data_activities();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		int cpt=0;
		
			for(Element activity: GET_All_Activities_Villes(GET_All_Domaine_Ville(GET_All_Ville("https://www.marocannuaire.org/")))  ) {
				cpt++;
				
				System.out.println(cpt +" : "+activity.text());
				insertEntreprises(GET_All_Entreprises_Infos2(activity.text()));
				
			}
				
				/*for(Entreprise entr:GET_All_Entreprises_Infos2(activity.text())) {
				                  System.out.println(entr.getName());
				
				}
				
				//System.out.println("======================================================================");
		/*	}
			
			System.out.println(cpt);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
	}


}