package domain;

import javafx.beans.property.SimpleStringProperty;

import javax.persistence.*;

/**
 * The type Ingredient.
 */
@Entity
@NamedQuery(name = "Ingredient.findByName", query = "SELECT i FROM Ingredient i WHERE i.name = :ingredientName")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "name", nullable = false)
    private String name;
    @ManyToOne
    @JoinColumn(name = "Allergenid", referencedColumnName = "id")
    private Allergen allergen;

    /**
     * Instantiates a new Ingredient.
     */
    public Ingredient() {
        name = "";
    }

    /**
     * Instantiates a new Ingredient.
     *
     * @param name     the name
     * @param allergen the allergen
     */
    public Ingredient(String name, Allergen allergen) {
        if (name.isEmpty())
            // TODO - Language
            throw new NullPointerException("Name can't be empty.");
        this.name = name;
        this.allergen = allergen;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        if (name.isEmpty())
            // TODO - Language
            throw new NullPointerException("Name can't be empty.");
        this.name = name;
    }

    /**
     * Gets allergen.
     *
     * @return the allergen
     */
    public Allergen getAllergen() {
        return allergen;
    }

    /**
     * Sets allergen.
     *
     * @param allergen the allergen
     */
    public void setAllergen(Allergen allergen) {
        this.allergen = allergen;
    }

    /**
     * Has allergen boolean.
     *
     * @return the boolean
     */
    public boolean hasAllergen() {
        return allergen != null;
    }

    /**
     * Name property simple string property.
     *
     * @return the simple string property
     */
    public SimpleStringProperty nameProperty() {
        return new SimpleStringProperty(name);
    }

    /**
     * Allergen property simple string property.
     *
     * @return the simple string property
     */
    public SimpleStringProperty allergenProperty() {
        SimpleStringProperty allergenProperty = new SimpleStringProperty();
        if (hasAllergen())
            allergenProperty.set(allergen.getName());
        return allergenProperty;

    }

    @Override
    public String toString() {
        return name;
    }
}
