package abstraction.equipe5;

import java.util.ArrayList;
import abstraction.commun.*;
import abstraction.fourni.*;
import java.util.List;

public class Lindt implements Acteur, ITransformateur{
	
	private HistoriqueCommandeDistri histCommandeDistri;
	private HistoriqueCommandeProduc histCommandeProduc;
	private HistoriqueCommandeDistri commandeDistriLivree;
	private Stock stockCacao;
	private Stock stockChocolat50;
	private Stock stockChocolat60;
	private Stock stockChocolat70;
	private Tresorerie treso;
	private AchatProd achatProd;
	private VenteDist venteDist;
	private ArrayList<IProducteur> producteurs;
	private ArrayList<IDistributeur> distributeurs;
	private Catalogue catalogue;
	private ArrayList<Stock> stocksChocolat;
	private TransformationCacaoChocolat transfo;
	

	public Lindt(){
		this.histCommandeDistri = new HistoriqueCommandeDistri();
		this.histCommandeProduc = new HistoriqueCommandeProduc();
		this.commandeDistriLivree = new HistoriqueCommandeDistri();
		this.stockCacao = new Stock("cacao",this,200.0);
		this.stockChocolat50 = new Stock(Constante.LISTE_PRODUIT[0].getNomProduit(),this,0.0);
		this.stockChocolat60 = new Stock(Constante.LISTE_PRODUIT[1].getNomProduit(),this,0.0);
		this.stockChocolat70 = new Stock(Constante.LISTE_PRODUIT[2].getNomProduit(),this,0.0);
		this.producteurs = new ArrayList<IProducteur>();
		this.distributeurs = new ArrayList<IDistributeur>();
		this.catalogue = new Catalogue();
		this.stocksChocolat= new ArrayList<Stock>();
		this.stocksChocolat.add(this.stockChocolat50);
		this.stocksChocolat.add(this.stockChocolat60);
		this.stocksChocolat.add(this.stockChocolat70);
		this.transfo=new TransformationCacaoChocolat(this);
	}

	public void creer() {
		this.histCommandeProduc.ajouter(new CommandeProduc(this, this.getProducteurs().get(0), 100.0, MarcheProducteur.LE_MARCHE.getCours()));
		this.histCommandeProduc.ajouter(new CommandeProduc(this, this.getProducteurs().get(1), 100.0, MarcheProducteur.LE_MARCHE.getCours()));
		this.achatProd = new AchatProd(this.histCommandeProduc,this.histCommandeDistri, this, this.stockCacao, this.treso);	
		this.treso = new Tresorerie(this.histCommandeDistri, this.histCommandeProduc, this, this.getProducteurs());
		this.venteDist = new VenteDist(this, this.getTreso());

	}


	/** Voila tout les getters*/
	public HistoriqueCommandeDistri getHistCommandeDistri() {
		return this.histCommandeDistri;
	}
	public HistoriqueCommandeProduc getHistCommandeProduc() {
		return this.histCommandeProduc;
	}
	public HistoriqueCommandeDistri getCommandeDistriLivree() {
		return this.commandeDistriLivree;
	}
	public void ajouterProducteur(IProducteur p) {
		this.producteurs.add(p);
	}
	public void ajouterDistributeur(IDistributeur d) {
		this.distributeurs.add(d);
	}
	public ArrayList<IProducteur> getProducteurs() {
		return this.producteurs;
	}
	public ArrayList<IDistributeur> getDistributeurs() {
		return this.distributeurs;
	}
	public String getNom() {
		return Constantes.NOM_TRANSFORMATEUR_2;
		}
	public Stock getStockCacao() {
		return this.stockCacao;
	}
	public Stock getStockChocolat50() {
		return this.stockChocolat50;
	}
	public Stock getStockChocolat60() {
		return this.stockChocolat60;
	}
	public Stock getStockChocolat70() {
		return this.stockChocolat70;
	}
	public ArrayList<Stock> getStocksChocolat(){
		return this.stocksChocolat;
	}
	public Tresorerie getTreso() {
		return this.treso;
	}
	public VenteDist getVenteDist() {
		return this.venteDist;
	}
	public TransformationCacaoChocolat getTransformationCacaoChocolat(){
		return this.transfo;
	}

	
	public void next() {
		// mise a jour de l'etat interne de Lindt du au troisieme producteur
		this.getStockCacao().ajouterStock(this.achatProd.quantiteProduc3());
		this.getTreso().retrait(this.achatProd.quantiteProduc3()*MarcheProducteur.LE_MARCHE.getCours()); //on achete au prix du marche
		
		// deplacement des commandes distributeurs dans l'historique des commandes livrees
		this.deplacerCommandeDist();
		
		this.getTransformationCacaoChocolat().Transformation();
		
		
		stockChocolat50.retirerStockChocolat(Monde.LE_MONDE.getStep()-3);
		stockChocolat60.retirerStockChocolat(Monde.LE_MONDE.getStep()-3);
		stockChocolat70.retirerStockChocolat(Monde.LE_MONDE.getStep()-3);
		
		treso.retrait(treso.coutStock()+treso.coutLivraison()+Constante.CHARGES_FIXES_STEP);
		treso.depot(treso.payeParDistrib());
		
		
	}
	
	
	// Fonctions finies
	public List<CommandeDistri> Offre(List<CommandeDistri> o) {
		return this.venteDist.Offre(o);
	}
	
	public List<CommandeDistri> CommandeFinale(List<CommandeDistri> cf) {
		return this.venteDist.CommandeFinale(cf);
	}
	
	public List<CommandeDistri> LivraisonEffective(List<CommandeDistri> livraison){
		this.deplacerCommandeDist();
		return this.venteDist.Offre (livraison);
	}

	public void notificationVente(CommandeProduc c) {
		this.achatProd.notificationVente(c);
	}
	
	public double annonceQuantiteDemandee() {
		return this.achatProd.annonceQuantiteDemandee();
	}

	public double annoncePrix() {
		return this.achatProd.getPrix();}
	
	public Catalogue getCatalogue() {
		List<Plage> listePlage = new ArrayList<Plage>();
		listePlage.add(new Plage(100, 150, 0.05));
		listePlage.add(new Plage(151, 200, 0.07));
		listePlage.add(new Plage(201, 0.12));
		this.catalogue.add(new Produit("50%", 0.5), new Tarif(this.getVenteDist().prixProduit(Constante.LISTE_PRODUIT[0]), listePlage));
		this.catalogue.add(new Produit("60%", 0.6), new Tarif(this.getVenteDist().prixProduit(Constante.LISTE_PRODUIT[1]), listePlage));
		this.catalogue.add(new Produit("70%", 0.5), new Tarif(this.getVenteDist().prixProduit(Constante.LISTE_PRODUIT[2]), listePlage));
		return this.catalogue;
	}
	
	public void deplacerCommandeDist() {
		// trouve le step maximum dans l'historique des commandes
		int stepMax = 0;
		for (CommandeDistri c : this.getHistCommandeDistri().getHist()) {
			if (c.getStepLivraison() > stepMax) {
				stepMax = c.getStepLivraison();
			}
		}
		for (int i=0; i<this.getHistCommandeDistri().getHist().size(); i++) {
			if ((stepMax>4) && (this.getHistCommandeDistri().getCommande(i).getStepLivraison() == stepMax-4)) {
				this.getCommandeDistriLivree().ajouter(this.getHistCommandeDistri().getCommande(i));
				this.getHistCommandeDistri().supprimer(this.getHistCommandeDistri().getCommande(i)); 
			}		
		}
	}

	// Ne plus coder celles la, elles vont disparaitre!
	public double annonceQuantiteDemandee(IProducteur p) {	return 0;	}
	public void notificationVente(IProducteur p){ 	}
	public double annonceQuantiteMiseEnVente(IDistributeur d){ return 0;}
}
