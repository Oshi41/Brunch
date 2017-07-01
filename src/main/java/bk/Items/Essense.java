package bk.Items;

import bk.Base.BaseVanilla.BkItem;
import bk.BookCraft;

/**
 * Created by User on 01.07.2017.
 */
public class Essense extends BkItem {
    
    public Essense(Essenses essenses){
        super(essenses.name);
        setCreativeTab(BookCraft.mixTab);
    }
    
    public enum  Essenses{
        knowledge("knowledge"),
        forgotten("forgotten"),
        wisdom("wisdom"),
        cosmic("cosmic");
        
        public final String name;
        
        Essenses(String name){
            this.name = name;
        }
    }
}
