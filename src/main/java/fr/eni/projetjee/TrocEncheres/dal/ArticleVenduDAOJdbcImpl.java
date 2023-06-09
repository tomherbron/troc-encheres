package fr.eni.projetjee.TrocEncheres.dal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import fr.eni.projetjee.TrocEncheres.bo.ArticleVendu;
import fr.eni.projetjee.TrocEncheres.bo.Categorie;
import fr.eni.projetjee.TrocEncheres.bo.Retrait;
import fr.eni.projetjee.TrocEncheres.bo.Utilisateur;


public class ArticleVenduDAOJdbcImpl implements IArticleVenduDAO{

	
	private static final String INSERT_ARTICLE_VENDU = "INSERT INTO article_vendu(nom_article, description, date_debut_enchere, date_fin_enchere, prix_initial, prix_vente, no_utilisateur, no_categorie, etat_vente) VALUES(?,?,?,?,?,?,?,?,?)";
	private static final String UPDATE = "UPDATE article_vendu SET nom_article=?, description=?, date_debut_enchere=?,date_fin_enchere=?, prix_initial=?,prix_vente=?, etat_vente =? WHERE no_article=?";
	private static final String DELETE = "DELETE FROM article_vendu WHERE no_article=?";
	private static final String SELECT_BY_ID  = "SELECT * FROM  article_vendu LEFT JOIN categorie on article_vendu.no_categorie = categorie.no_categorie LEFT JOIN utilisateur on article_vendu.no_utilisateur = utilisateur.no_utilisateur WHERE no_article = ?";
	private static final String SELECT_ALL = "SELECT * FROM article_vendu "
													+ "LEFT JOIN retrait ON article_vendu.no_article = retrait.no_article "
													+ "LEFT JOIN categorie on article_vendu.no_categorie = categorie.no_categorie "
													+ "LEFT JOIN utilisateur on article_vendu.no_utilisateur = utilisateur.no_utilisateur"; 
	private static final String SELECT_BY_CATEGORIE = "SELECT * FROM `article_vendu` "
			+ "LEFT JOIN retrait ON article_vendu.no_article = retrait.no_article "
			+ "LEFT JOIN categorie on article_vendu.no_categorie = categorie.no_categorie "
			+ "LEFT JOIN utilisateur on article_vendu.no_utilisateur = utilisateur.no_utilisateur WHERE libelle = ? "; 

	private static final String UPDATE_PDV =  "UPDATE article_vendu SET prix_vente=? WHERE no_article=?";
//	private static final String SELECT_BY_UTILISATEUR =  "UPDATE article_vendu SET prix_vente=? WHERE no_article=?";
	private static final String SELECT_BY_ETAT_VENTE =  "SELECT * FROM  article_vendu WHERE etat_vete = 1";
	private static final String SELECT_BY_DATE_DEBUT =  "SELECT * FROM  article_vendu WHERE date_debut_enchere = ?";
	private static final String SELECT_BY_DATE_FIN =  "SELECT * FROM  article_vendu WHERE date_fin_enchere = ?";
	
	
	
	
	public void insertArticleVendu(ArticleVendu article) throws DALException, SQLException {
		
		try (Connection con = ConnectionProvider.getConnection()) {

			PreparedStatement pstmt = con.prepareStatement(INSERT_ARTICLE_VENDU, PreparedStatement.RETURN_GENERATED_KEYS);

			pstmt.setString(1, article.getNomArticle());
			pstmt.setString(2, article.getDescription());
			pstmt.setDate(3, Date.valueOf(article.getDateDebutEnchere()));
			pstmt.setDate(4, Date.valueOf(article.getDateFinEnchere()));
			pstmt.setInt(5, article.getMiseAPrix());
			pstmt.setInt(6, article.getPrixDeVente());
			System.out.println("coucou" + article.getUtilisateur().getNoUtilisateur());
			pstmt.setInt(7, article.getUtilisateur().getNoUtilisateur());
			System.out.println("cat article : " + article.getCategorie().getNoCategorie());
			pstmt.setInt(8, article.getCategorie().getNoCategorie());
			pstmt.setBoolean(9, article.getEtatVente());
			
			pstmt.executeUpdate();
			
			ResultSet rs = pstmt.getGeneratedKeys();
			
			if (rs.next()) {
				article.setNoArticle(rs.getInt(1));
			}
			
			rs.close();
			pstmt.close();
			
			Retrait retrait = new Retrait();

			retrait.setRue(article.getRetrait().getRue());
			retrait.setCodePostal(article.getRetrait().getCodePostal());
			retrait.setVille(article.getRetrait().getVille());
			
			 DAOFactory.getRetraitDAO() .insertRetrait(retrait, article.getNoArticle());

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DALException("Insert failed");
		}

	}

	@Override
	public void updateArticleVendu(ArticleVendu article) throws DALException, SQLException {

		try (Connection con = ConnectionProvider.getConnection()) {

			PreparedStatement pstmt = con.prepareStatement(UPDATE);
			pstmt.setString(1, article.getNomArticle());
			pstmt.setString(2, article.getDescription());
			pstmt.setDate(3, Date.valueOf(article.getDateDebutEnchere()));
			pstmt.setDate(4, Date.valueOf(article.getDateFinEnchere()));
			pstmt.setInt(5, article.getMiseAPrix());
			pstmt.setInt(6, article.getPrixDeVente());
			pstmt.setInt(7, article.getUtilisateur().getNoUtilisateur());
			pstmt.setInt(8, article.getCategorie().getNoCategorie());
			pstmt.setBoolean(9, article.getEtatVente());
			pstmt.executeUpdate();
			pstmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DALException("Update failed");
		}

	}

	@Override
	public void deleteArticleVendu(Integer noArticle) throws DALException, SQLException {

		try (Connection con = ConnectionProvider.getConnection()) {

			PreparedStatement pstmt = con.prepareStatement(DELETE);
			pstmt.setInt(1, noArticle);
			pstmt.executeUpdate();
			pstmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DALException("Delete failed");
		}

	}

	@Override
	public ArticleVendu selectById(Integer noArticle) throws DALException, SQLException {
	
		ArticleVendu article = null;
		
		try (Connection con = ConnectionProvider.getConnection()) {

			PreparedStatement pstmt = con.prepareStatement(SELECT_BY_ID);
			pstmt.setInt(1, noArticle);
			System.out.println(noArticle);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {

				noArticle = rs.getInt("no_article");
				String nomArticle = rs.getString("nom_article");
				String description = rs.getString("description");
				LocalDate dateDebutEnchere = rs.getDate("date_debut_enchere").toLocalDate();
				LocalDate dateFinEnchere = rs.getDate("date_fin_enchere").toLocalDate();
				Integer miseAPrix = rs.getInt("prix_initial");
				Integer prixDeVente = rs.getInt("prix_vente");
				Integer noUtilisateur = rs.getInt("no_utilisateur");
				Integer noCategorie = rs.getInt("no_categorie");
				Boolean etatVente = rs.getBoolean("etat_vente");
				String rue = rs.getNString("rue");
				String codePostal = rs.getString("code_postal");
				String ville = rs.getNString("ville");
				String libelle = rs.getString("libelle");
				String pseudo = rs.getString("pseudo");
				String nom = rs.getString("nom");
				String prenom = rs.getString("prenom");
				String email = rs.getString("email");
				String telephone = rs.getString("telephone");
				String motDePasse = rs.getString("mot_de_passe");
				Integer credit = rs.getInt("credit");
				Boolean administrateur = rs.getBoolean("administrateur");
				
				Categorie cat = new Categorie (noCategorie, libelle);
				Utilisateur user = new Utilisateur (noUtilisateur, pseudo, nom, prenom, email, telephone, rue, codePostal, ville, motDePasse, credit, administrateur);
				Retrait ret = new Retrait (rue, codePostal, ville);
				
				article = new ArticleVendu(noArticle, nomArticle, description, dateDebutEnchere, dateFinEnchere, miseAPrix, prixDeVente, etatVente, ret, user, cat);
				
			}
	
			pstmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DALException("selectById failed");
		}
		

		return article;
	}
	

	public List<ArticleVendu> selectAll() throws DALException, SQLException {
		List<ArticleVendu> listeArticleVendu = null;

		try (Connection con = ConnectionProvider.getConnection()) {

			PreparedStatement pstmt = con.prepareStatement(SELECT_ALL);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				ArticleVendu article = null;
				if (listeArticleVendu == null)
					listeArticleVendu = new ArrayList<ArticleVendu>();
				Integer noArticle = rs.getInt("no_article");
				String nomArticle = rs.getString("nom_article");
				String description = rs.getString("description");
				LocalDate dateDebutEnchere = rs.getDate("date_debut_enchere").toLocalDate();
				LocalDate dateFinEnchere = rs.getDate("date_fin_enchere").toLocalDate();
				Integer miseAPrix = rs.getInt("prix_initial");
				Integer prixDeVente = rs.getInt("prix_vente");
				Integer noUtilisateur = rs.getInt("no_utilisateur");
				Integer noCategorie = rs.getInt("no_categorie");
				Boolean etatVente = rs.getBoolean("etat_vente");
				String rue = rs.getNString("rue");
				String codePostal = rs.getString("code_postal");
				String ville = rs.getNString("ville");
				String libelle = rs.getString("libelle");
				String pseudo = rs.getString("pseudo");
				String nom = rs.getString("nom");
				String prenom = rs.getString("prenom");
				String email = rs.getString("email");
				String telephone = rs.getString("telephone");
				String motDePasse = rs.getString("mot_de_passe");
				Integer credit = rs.getInt("credit");
				Boolean administrateur = rs.getBoolean("administrateur");
				
				Retrait retrait2 = new Retrait (rue, codePostal, ville);
				Categorie categorie2 = new Categorie (noCategorie, libelle);
				Utilisateur utilisateur2 = new Utilisateur (noUtilisateur, pseudo, nom, prenom, email, telephone, rue, codePostal, ville, motDePasse, credit, administrateur);
				article = new ArticleVendu (noArticle, nomArticle, description, dateDebutEnchere, dateFinEnchere, miseAPrix, prixDeVente, etatVente, retrait2, utilisateur2, categorie2); 	
				
				listeArticleVendu.add(article);
			}
			
			pstmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DALException("Select All");
		}

		return listeArticleVendu;
		
	}
	

	@Override
	public List<ArticleVendu> selectByCategorie(String type) throws DALException, SQLException {
		List<ArticleVendu> listeArticleVenduParCateg = null;
		ArticleVendu article = null;

		try (Connection con = ConnectionProvider.getConnection()) {

			PreparedStatement pstmt = con.prepareStatement(SELECT_BY_CATEGORIE);
			pstmt.setString(1,type);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				if (listeArticleVenduParCateg == null)
					listeArticleVenduParCateg = new ArrayList<ArticleVendu>();
				Integer noArticle = rs.getInt("no_article");
				String nomArticle = rs.getString("nom_article");
				String description = rs.getString("description");
				LocalDate dateDebutEnchere = rs.getDate("date_debut_enchere").toLocalDate();
				LocalDate dateFinEnchere = rs.getDate("date_fin_enchere").toLocalDate();
				Integer miseAPrix = rs.getInt("prix_initial");
				Integer prixDeVente = rs.getInt("prix_vente");
				Integer noUtilisateur = rs.getInt("no_utilisateur");
				Integer noCategorie = rs.getInt("no_categorie");
				Boolean etatVente = rs.getBoolean("etat_vente");
				String rue = rs.getNString("rue");
				String codePostal = rs.getString("code_postal");
				String ville = rs.getNString("ville");
				String libelle = rs.getString("libelle");
				String pseudo = rs.getString("pseudo");
				String nom = rs.getString("nom");
				String prenom = rs.getString("prenom");
				String email = rs.getString("email");
				String telephone = rs.getString("telephone");
				String motDePasse = rs.getString("mot_de_passe");
				Integer credit = rs.getInt("credit");
				Boolean administrateur = rs.getBoolean("administrateur");
				
				Retrait retrait2 = new Retrait (rue, codePostal, ville);
				Categorie categorie2 = new Categorie (noCategorie, libelle);
				Utilisateur utilisateur2 = new Utilisateur (noUtilisateur, pseudo, nom, prenom, email, telephone, rue, codePostal, ville, motDePasse, credit, administrateur);
				article = new ArticleVendu (noArticle, nomArticle, description, dateDebutEnchere, dateFinEnchere, miseAPrix, prixDeVente, etatVente, retrait2, utilisateur2, categorie2); 	
				
				listeArticleVenduParCateg.add(article);
			}
			
			pstmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DALException("Select by categorie");
		}

		return listeArticleVenduParCateg;
		
	}

	@Override
	public void updatePdv(ArticleVendu article) throws DALException, SQLException {
		
		try (Connection con = ConnectionProvider.getConnection()) {

			PreparedStatement pstmt = con.prepareStatement(UPDATE_PDV);
			
			pstmt.setInt(1, article.getPrixDeVente());
			pstmt.setInt(2, article.getNoArticle());
			
			pstmt.executeUpdate();
			
			pstmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DALException("Update Pdv failed");
		}
		
	}
	
	public List<ArticleVendu> selectByEtatVente() throws DALException, SQLException {
		List<ArticleVendu> listeArticleVendu = null;

		try (Connection con = ConnectionProvider.getConnection()) {

			PreparedStatement pstmt = con.prepareStatement(SELECT_BY_ETAT_VENTE);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				ArticleVendu article = null;
				if (listeArticleVendu == null)
					listeArticleVendu = new ArrayList<ArticleVendu>();
				Integer noArticle = rs.getInt("no_article");
				String nomArticle = rs.getString("nom_article");
				String description = rs.getString("description");
				LocalDate dateDebutEnchere = rs.getDate("date_debut_enchere").toLocalDate();
				LocalDate dateFinEnchere = rs.getDate("date_fin_enchere").toLocalDate();
				Integer miseAPrix = rs.getInt("prix_initial");
				Integer prixDeVente = rs.getInt("prix_vente");
				Integer noUtilisateur = rs.getInt("no_utilisateur");
				Integer noCategorie = rs.getInt("no_categorie");
				Boolean etatVente = rs.getBoolean("etat_vente");
				String rue = rs.getNString("rue");
				String codePostal = rs.getString("code_postal");
				String ville = rs.getNString("ville");
				String libelle = rs.getString("libelle");
				String pseudo = rs.getString("pseudo");
				String nom = rs.getString("nom");
				String prenom = rs.getString("prenom");
				String email = rs.getString("email");
				String telephone = rs.getString("telephone");
				String motDePasse = rs.getString("mot_de_passe");
				Integer credit = rs.getInt("credit");
				Boolean administrateur = rs.getBoolean("administrateur");
				
				Retrait retrait2 = new Retrait (rue, codePostal, ville);
				Categorie categorie2 = new Categorie (noCategorie, libelle);
				Utilisateur utilisateur2 = new Utilisateur (noUtilisateur, pseudo, nom, prenom, email, telephone, rue, codePostal, ville, motDePasse, credit, administrateur);
				article = new ArticleVendu (noArticle, nomArticle, description, dateDebutEnchere, dateFinEnchere, miseAPrix, prixDeVente, etatVente, retrait2, utilisateur2, categorie2); 	
				
				listeArticleVendu.add(article);
			}
			
			pstmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DALException("Select ");
		}

		return listeArticleVendu;
		
	}
}
