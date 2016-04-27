package abstraction.equipe6;

import abstraction.fourni.Monde;

public class MondeV1 extends Monde {
	/**
	 * Methode appelee juste a la creation de l'unique 
	 * instance du Monde. C'est le bon endroit pour creer 
	 * vos instances d'acteurs et les ajouter au monde.
	 * 
	 * Vous aurez bien evidemment a modifier 
	 * le corps de cette methode
	 */
	public void peupler() {
		Producteur p1 = new Producteur(Constantes.NOM_PRODUCTEUR_1, this);
		Producteur p2 = new Producteur(Constantes.NOM_PRODUCTEUR_2, this);
		
		this.ajouterActeur(p1);
		this.ajouterActeur(p2);
		
		Transformateur t1 = new Transformateur(Constantes.NOM_TRANSFORMATEUR_1, this);
		Transformateur t2 = new Transformateur(Constantes.NOM_TRANSFORMATEUR_2, this);
		Transformateur t3 = new Transformateur("Others", this);

		this.ajouterActeur(t1);
		this.ajouterActeur(t2);
		this.ajouterActeur(t3);
		
		Marche.LE_MARCHE = new Marche();
		Marche.LE_MARCHE.addProducteur(p1);
		Marche.LE_MARCHE.addProducteur(p2);
		Marche.LE_MARCHE.addTransformateur(t1);
		Marche.LE_MARCHE.addTransformateur(t2);
		Marche.LE_MARCHE.addTransformateur(t3);

		this.ajouterActeur(Marche.LE_MARCHE);
		
		Carrefour d1 = new Carrefour(Constantes.NOM_DETAILLANT_1, this, 15 , 20, 50000);
		Carrefour d2 = new Carrefour(Constantes.NOM_DETAILLANT_2, this, 15, 20, 50000);
		
		
		d1.ajouterVendeur(t1);
		d1.ajouterVendeur(t2);
		d1.ajouterVendeur(t3);
		d2.ajouterVendeur(t1);
		d2.ajouterVendeur(t2);
		d2.ajouterVendeur(t3);
		
		this.ajouterActeur(d1);
		this.ajouterActeur(d2);
		
	}

}