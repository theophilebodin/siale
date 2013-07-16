/**
 * 
 */
package nc.mairie.temp;

import java.util.ArrayList;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Div;
import org.zkoss.zul.Window;


/**
 * @author boulu72
 *
 */
public class TestModel extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8608406922690897596L;

	AnnotateDataBinder binder;
	
	ArrayList<Word> listWord;
	
	public ArrayList<Word> getListWord() {
		return listWord;
	}
	
	ArrayList <Lang> listLang = new ArrayList<>();

	public ArrayList<Lang> getListLang () {
		return listLang;
	}

	
	public class Lang {
		String name;
		String language;
		
		Lang(String name, String language) {
			this.name = name;
			this.language = language;
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getLanguage() {
			return language;
		}
		public void setLanguage(String language) {
			this.language = language;
		}
		
	}
	
	
	Word currentWord;
	
	public class Word {
  		String word;
  		Lang lang;
  		
  		Word(String word, Lang lang) {
  			this.word = word;
  			this.lang = lang;
  		}
  		
  		public String getWord() {
  			return word;
  		}
  		
  		public void setWord(String word) {
  			this.word = word;
  		}
  		
  		public Lang getLang() {
  			return lang;
  		}
  		
  		public void setLang(Lang lang) {
  			this.lang=lang;
  		}
  		
  		public String getFullName () {
  			return getWord() + " "+ getLang().getLanguage();
  		}
  		
  		
  	}
	
	boolean visible = false;

	public Word getCurrentWord(){
		return currentWord;
	}
	
	public void setCurrentWord(Word word){
		this.currentWord= word;
	}
	
	public boolean isVisible() {
		return visible;
	}

	
	@Wire
	Div detailWord;
	
	@Wire
	Window test;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	
		Lang l1 = new Lang("eng","ENGLISH");
		Lang l2 = new Lang("fr","FRANCAIS");
		
		listLang.add(l1);
		listLang.add(l2);
		
		Word p1 = new Word("Hello", listLang.get(0));
	    Word p2 = new Word("Coucou", listLang.get(1));
	    
	    listWord = new ArrayList<Word>();
	    listWord.add(p1);
	    listWord.add(p2);
	    
		comp.setAttribute(comp.getId(), this, true);
				
		binder = new AnnotateDataBinder(comp);
		System.out.println(binder.isLoadOnSave());
		//OBLIGE DE METTRE à FALSE pour ne pas avoir le bug de save-whn... pas terrible
		binder.setLoadOnSave(false);
		binder.loadAll();

	}

	@Listen("onClick = #listBoxWord;")
	public void onClick$listBoxWord() {
		visible = true;
		binder.loadComponent(detailWord);
	}
	
	@Listen("onClick = #btnOk;")
	public void onClick$btnOk() {
		//visible = false;
		//OBLIGE DE raffraichir TOUTE la fenêtre car loadOnSave est à faux :(
		binder.loadComponent(test);
		//binder.loadComponent(detailWord);
	}
	
}
