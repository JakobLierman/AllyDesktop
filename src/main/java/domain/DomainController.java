package domain;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import java.util.ResourceBundle;

/**
 * The type Domain controller.
 */
public class DomainController {
    private final ProductManager productManager;
    private ResourceBundle resourceBundle;
    private PDFCreator pdf;
    private Subject<String> typeObservable = PublishSubject.create();

    /**
     * Instantiates a new Domain controller.
     *
     * @param resourceBundle the initial resource bundle
     */
    public DomainController(final ResourceBundle resourceBundle) {
        productManager = new ProductManager();
        this.resourceBundle = resourceBundle;
        pdf = new PDFCreator();
    }

    /**
     * Gets product manager.
     *
     * @return the product manager
     */
    public ProductManager getProductManager() {
        return productManager;
    }

    /**
     * Gets text from the resource bundle in the correct language.
     *
     * @param textToGet the text to get
     * @return the text
     */
    public String getText(String textToGet) {
        return resourceBundle.getString(textToGet);
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        typeObservable.onNext(type);
    }

    /**
     * Gets type observable.
     *
     * @return the type observable
     */
    public Subject<String> getTypeObservable() {
        return typeObservable;
    }

    /**
     * Export items to pdf in directory.
     *
     * @param directory the directory
     */
    public void export(String directory) {
        // TODO - Implement
        System.out.println("Exporting to " + directory);
    }

//     /**
//      * Changes the resource bundle to the desired language.
//      *
//      * @param language the language symbols (e.g. "en")
//      */
//     public void changeLanguage(String language) {
//         resourceBundle = ResourceBundle.getBundle("Bundle", new Locale(language));
//     }

//    /**
//     * Gets language.
//     *
//     * @return the language
//     */
//    public String getLanguage() {
//        return resourceBundle.getLocale().getLanguage();
//    }
}