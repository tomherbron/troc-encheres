<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>

<%@page import="fr.eni.projetjee.TrocEncheres.bo.ArticleVendu"%>
<%@page import="fr.eni.projetjee.TrocEncheres.bo.Categorie"%>
<%@page import="fr.eni.projetjee.TrocEncheres.bo.Utilisateur"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>


<%
ArrayList<Categorie> listeCategorie = (ArrayList) request.getAttribute("listeCategorie");
%>


<%
if (session.getAttribute("utilisateur") == null) {
%>
<html>
<head>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Kaushan+Script&display=swap" rel="stylesheet">
<link rel="stylesheet"
	href="https://unpkg.com/@picocss/pico@1.*/css/pico.min.css">
<meta charset="UTF-8">
<title>Enchères-ENI | Accueil</title>
</head>
<body>
	<div class="container">
		<article>

			<header>


				<h1 style = "font-family: 'Kaushan Script', cursive;">
					<a href="./ServletListeEnchere">Enchères-ENI</a>
				</h1>
				<nav aria-label="breadcrumb">
					<ul>

						<li><a href="./ServletInscriptionUtilisateur">S'inscrire</a>
						</li>
						<li><a href="./ServletConnexion">Se Connecter</a></li>



					</ul>
				</nav>
			</header>



			<div>
				<h2>Liste des enchères</h2>
			</div>

			<div>

				<form method="post" action="ServletListeEnchere">

					<label for="site-search">Filtres :</label> <input type="text"
						name="query" placeholder="Le nom de l'article contient" /> <br>
					<label for="categories">Catégories:</label> <select
						name="categories">
						<option value="toutes">toutes</option>
						<%
						for (int i = 0; i < listeCategorie.size(); i++) {
							String libelle = listeCategorie.get(i).getLibelle();
						%>
						<option value="<%=libelle%>"><%=libelle%></option>
						<%
						}
						%>
					</select> <input type="submit" value="Rechercher" />
				</form>

			</div>


			<%
			List<ArticleVendu> listeArticle = (List<ArticleVendu>) request.getAttribute("listeArticle");
			for (ArticleVendu articleCourant : listeArticle) {
			%>


			<br>
			<article>
			<h3><a href="./ServletDetailVente?id=<%=articleCourant.getNoArticle()%>"><%=articleCourant.getNomArticle()%></a></h3>
			
			<br>
			<p>
			<strong>	Description </strong>
				<%=articleCourant.getDescription()%>
				
			</p>
				<p>
				<strong>Catégorie :</strong>
				<%=articleCourant.getCategorie().getLibelle()%>	
			</p>
			<p>
				<strong> Prix : </strong>
				<%=articleCourant.getMiseAPrix()%>
				points
				
			</p>
			<p>
				<strong> Fin de l'enchère : </strong>
				<%=articleCourant.getDateFinEnchere()%></p>
			<br>
			<p>
				<strong> Vendeur : </strong>
				<%=articleCourant.getUtilisateur().getPseudo()%></p>
			</article>
			<%
			session.setAttribute("articleCourant", articleCourant);
			}
			
			%>
			


		</article>
	</div>
</body>
</html>






<%
} else {
%>

<html>
<head>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Kaushan+Script&display=swap" rel="stylesheet">
<link rel="stylesheet"
	href="https://unpkg.com/@picocss/pico@1.*/css/pico.min.css">
<meta charset="UTF-8">
<title>Enchères-ENI | Accueil</title>
</head>
<body>
<%@page import="fr.eni.projetjee.TrocEncheres.bo.Utilisateur"%>
	<div class="container">
		<article>

			<header>
				
				<%
				Utilisateur utilisateurCourant = (Utilisateur) session.getAttribute("utilisateur");
				%>
				

				<h1 style = "font-family: 'Kaushan Script', cursive;">
					<a href="./ServletListeEnchere">Enchères-ENI</a>
				</h1>
				<div dir="rtl">
					<h3>Bienvenue, <%=utilisateurCourant.getPseudo()%></h3>
				</div>
				<nav aria-label="breadcrumb">
					<ul>
	
						<li><a href="./ServletNouvelleVente">Vendre un article</a></li>
						<li><a href="./ServletAffichageProfilUtilisateurCourant">Modifier
								mon profil</a></li>	
						<li><a href="./ServletDeconnexion">Se déconnecter</a></li>
			


					</ul>
				</nav>
			</header>



			<div>
				<h2>Liste des enchères</h2>
			</div>

			<div>

				<form method="post" action="ServletListeEnchere">

					<fieldset>
						<label for="achats"> 
							<input type="radio" id="achats"	name="radioAchatVente" value="achats" checked/>Achats</label>
							 
						<label for="mes-ventes"> 
							<input type="radio" id="mes-ventes" name="radioAchatVente" value="ventes"/>Mes ventes</label>
					
					</fieldset>
						
					<label for="site-search">Filtres :</label>
						<input type="text" name="query" placeholder="Le nom de l'article contient"/> 
							<br>
					<label for="categories">Catégories:</label> 
						<select name="categories">
							<option value="toutes">toutes</option>
								<%
								for (int i = 0; i < listeCategorie.size(); i++) {
									String libelle = listeCategorie.get(i).getLibelle();
								%>
							<option value="<%=libelle%>"><%=libelle%></option>
								<%
								}
								%>
						</select> 
							<input type="submit" value="Rechercher" />
				</form>

			</div>


			<%
			List<ArticleVendu> listeArticle = (List<ArticleVendu>) request.getAttribute("listeArticle");
			for (ArticleVendu articleCourant : listeArticle) {
			%>


			<br>
			<article>
			<h3><a href="./ServletDetailVente?id=<%=articleCourant.getNoArticle()%>"><%=articleCourant.getNomArticle()%></a></h3>
			<br>
			<br>
			<p>
				<strong>Description :</strong>
				<%=articleCourant.getDescription()%>
				
			</p>
			<p>
				<strong>Catégorie :</strong>
				<%=articleCourant.getCategorie().getLibelle()%>
				
			</p>
			<p>
				<strong> Prix : </strong>
				<%=articleCourant.getMiseAPrix()%>
				points
			</p>
			<p>
				<strong> Fin de l'enchère : </strong>
				<%=articleCourant.getDateFinEnchere()%></p>
			<br>

			<p>
				<strong> Vendeur : </strong>
				<a href="./ServletAffichageProfilUtilisateur?pseudo=<%=articleCourant.getUtilisateur().getPseudo() %>">
				<%=articleCourant.getUtilisateur().getPseudo() %></a>
				
				
				</p>
			</article>
			<%
			session.setAttribute("articleCourant", articleCourant);
			}
			%>



		</article>
	</div>
</body>
</html>
<%
}
%>