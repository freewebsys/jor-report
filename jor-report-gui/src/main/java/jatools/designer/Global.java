package jatools.designer;

import jatools.designer.config.Configuration;

import java.awt.Insets;
import java.util.Iterator;



public class Global {
    public static Insets GBC_INSETS = new Insets(1,5,1,5);
    public static boolean DEBUG = true;
    public static DatasetReaderList getFaviroteDatasetReaders() {
        return new FavouriteDatasetReaderList();
    }
}


class FavouriteDatasetReaderList implements DatasetReaderList {
    Iterator it;

    FavouriteDatasetReaderList() {
        this.it = Configuration.getInstance().getReaders().iterator();
    }

    public Iterator iterator() {
        return it;
    }

    public String getName() {
        return App.messages.getString("res.120");
    }
}
