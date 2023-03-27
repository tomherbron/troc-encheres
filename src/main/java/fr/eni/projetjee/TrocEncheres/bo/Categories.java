package fr.eni.projetjee.TrocEncheres.bo;

import java.util.Objects;

public class Categories {

	int noCategorie;
	String libelle;

	public Categories() {
		super();
	}

	public Categories(String libelle) {
			super();
			this.libelle = libelle;
	}

	public Categories(int noCategorie, String libelle) {
		super();
		this.noCategorie = noCategorie;
		this.libelle = libelle;

	}

	public int getNoCategorie() {
		return noCategorie;
	}

	public void setNoCategorie(int noCategorie) {
		this.noCategorie = noCategorie;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	@Override
	public String toString() {
		return "Categories [noCategorie=" + noCategorie + ", libelle=" + libelle + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(libelle, noCategorie);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Categories other = (Categories) obj;
		return Objects.equals(libelle, other.libelle) && noCategorie == other.noCategorie;
	}

	
	
	
}
