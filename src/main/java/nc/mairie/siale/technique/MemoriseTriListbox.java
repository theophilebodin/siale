package nc.mairie.siale.technique;

import java.util.Hashtable;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.SortEvent;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Window;

public class MemoriseTriListbox {
	
	//En clé : gestionMission				saisirMission
	//En valeur		listbox1	sort1			listboxx sortx
	//				listbox2	sort2
	
	Hashtable<String, Hashtable<String, ComponentSorted>> hashComposer = new Hashtable<String, Hashtable<String, ComponentSorted>>();
	
	private Hashtable<String, Hashtable<String, ComponentSorted>> getHashComposer() {
		return hashComposer;
	}

	static class ComponentSorted {
		String listBoxId;
		String sortDirection;
		int index;
		
		public String getListBoxId() {
			return listBoxId;
		}

		public String getSortDirection() {
			return sortDirection;
		}

		public int getIndex() {
			return index;
		}

		public ComponentSorted(String listBoxId, String sortDirection, int index) {
			super();
			this.listBoxId = listBoxId;
			this.sortDirection = sortDirection;
			this.index = index;
		}
		
	}
	

	/**
	 * Appelé avec en paramètre le component Window associé ainsi que le Sort Event
	 * Le tri encours est mémorisé dans la session.
	 * 
	 * @param window window
	 * @param event event
	 * @throws Exception Exception
	 */
	
	public static void memoriseTri(Window window, SortEvent event) throws Exception {
		
		MemoriseTriListbox memo = (MemoriseTriListbox)Executions.getCurrent().getDesktop().getAttribute("memoriseSort");
		
		if (memo == null) {
			memo = new MemoriseTriListbox();
			Executions.getCurrent().getDesktop().setAttribute("memoriseSort", memo);
		}
		
		Listheader target = (Listheader)event.getTarget();
		Listbox listbox = (Listbox)target.getParent().getParent();
		
		if (listbox.getId() == null) throw new Exception ("La listbox doit avoir un Id");
		
		int index = target.getParent().getChildren().indexOf(target);
		String sortDirection= target.getSortDirection();
		if ("natural".equals(sortDirection) || "descending".equals(sortDirection) ) {
			sortDirection = "ascending";
		} else {
			sortDirection="descending";
		}
		
		//on récupère la hashtable du composer en cours
		Hashtable<String, ComponentSorted> hCompSorted = memo.getHashComposer().get(window.getId());
		if (hCompSorted == null) {
			hCompSorted = new Hashtable<String, ComponentSorted>(); 
			memo.getHashComposer().put(window.getId(),hCompSorted);
		}
		
		ComponentSorted cs = new ComponentSorted(listbox.getId(), sortDirection, index);
		hCompSorted.put(cs.getListBoxId(), cs);
		
	}	

	/**
	 * Récupère dans la session tous les tris mémorisés pour le Component window passé en paramètre et applique les tris
	 * 
	 * @param window window
	 */
	public static void recupereTri(Window window) {
		
		MemoriseTriListbox memo = (MemoriseTriListbox)Executions.getCurrent().getDesktop().getAttribute("memoriseSort");
		
		if (memo == null) return;
		
		//recup des composants triés
		Hashtable<String, ComponentSorted> hCompSorted = memo.getHashComposer().get(window.getId());
		if (hCompSorted == null) return;
		
		for (ComponentSorted cs : hCompSorted.values()) {
			Listbox listbox = (Listbox)window.getFellow(cs.listBoxId);
			Listhead listhead =(Listhead)listbox.getChildren().get(0);
			Listheader listheader = (Listheader)listhead.getChildren().get(cs.getIndex());
			listheader.setSortDirection(cs.sortDirection);
		}
	}
	
	
}
