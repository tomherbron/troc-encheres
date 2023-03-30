package fr.eni.projetjee.TrocEncheres.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fr.eni.projetjee.TrocEncheres.bll.IUtilisateurManager;
import fr.eni.projetjee.TrocEncheres.bll.SingletonUtilisateurManager;
import fr.eni.projetjee.TrocEncheres.bll.UtilisateurManagerException;
import fr.eni.projetjee.TrocEncheres.bo.Utilisateur;
import fr.eni.projetjee.TrocEncheres.dal.DALException;

/**
 * Servlet implementation class ServletConnexion
 */
@WebServlet("/ServletConnexion")
public class ServletConnexion extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private IUtilisateurManager utilisateurManager = SingletonUtilisateurManager.getInstance();

    public ServletConnexion() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 RequestDispatcher rd = request.getRequestDispatcher("./SeConnecter.jsp");
	     rd.forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Utilisateur utilisateur = null;
		
		String pseudo = request.getParameter("pseudo-utilisateur");
		String motDePasse = request.getParameter("mdp-utlisateur");
		
		try {
			
			utilisateur = utilisateurManager.selectByLogin(pseudo, motDePasse);
			System.out.println(utilisateur.toString());
						
		} catch (DALException e) {
			e.printStackTrace();
		} catch (UtilisateurManagerException e) {
			e.printStackTrace();
		}
	
		
		RequestDispatcher rd = request.getRequestDispatcher("./AccueilListeEncheres.jsp");
		rd.forward(request, response);	
		

	}

}