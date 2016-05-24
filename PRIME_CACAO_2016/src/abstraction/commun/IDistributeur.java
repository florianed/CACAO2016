 package abstraction.commun;
import java.util.HashMap;
import java.util.List;

public interface IDistributeur {
	
	
	public List<CommandeDistri> Demande (ITransformateur t, Catalogue c);
	
	public List<CommandeDistri> ContreDemande (List<CommandeDistri> cd);
	
	public List<CommandeDistri> commandeFinale(List<CommandeDistri> list);
}
