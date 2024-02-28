package com.attributes;

import org.jetbrains.annotations.Contract;

public class Attribute {

	/**
	 * How much health do we get for each strength point.
	 */
	final public static float HEALTH_PER_STRENGTH = 7.0f;

	/**
	 * How much damage to we get for each agility point.
	 */
	final public static float DAMAGE_PER_AGILITY = 0.5f;

	/**
	 * How much mana do we get for each intelligence point.
	 */
	final public static float MANA_PER_INTELLIGENCE = 7.0f;

	/**
	 * The base amount of life.
	 */
	private int baseHealth;

	/**
	 * The base amount of mana.
	 */
	private int baseMana;

	/**
	 * The base amount of damage.
	 */
	private int baseDamage;

	/**
	 * The base amount of strength.
	 */
	private int baseStrength;

	/**
	 * The base amount of agility.
	 */
	private int baseAgility;

	/**
	 * The base amount of intelligence.
	 */
	private int baseIntelligence;

	/**
	 * The base amount of armor.
	 */
	private int baseArmor;

	/**
	 * The base amount of magic resistance.
	 */
	private int baseMagicResistance;

	/**
	 * Creates a new Attribute instance.
	 */
	public Attribute() {
		this(0, 0, 0, 0, 0, 0, 0, 0);
	}

	/**
	 * Creates a new Attribute instance.
	 *
	 * @param baseHealth Base life to set
	 * @param baseMana Base mana to set
	 * @param baseDamage Base damage to set
	 * @param baseStrength Base strength to set
	 * @param baseAgility Base agility to set
	 * @param baseIntelligence Base intelligence to set
	 * @param baseArmor Base armor to set
	 * @param baseMagicResistance Base magic resistance to set
	 */
	public Attribute(final int baseHealth, final int baseMana, final int baseDamage, final int baseStrength, final int baseAgility, final int baseIntelligence, final int baseArmor, final int baseMagicResistance) {
		this.setBaseHealth(baseHealth);
		this.setBaseMana(baseMana);
		this.setBaseDamage(baseDamage);
		this.setBaseStrength(baseStrength);
		this.setBaseAgility(baseAgility);
		this.setBaseIntelligence(baseIntelligence);
		this.setBaseArmor(baseArmor);
		this.setBaseMagicResistance(baseMagicResistance);
	}

	/**
	 * Returns the base health.
	 *
	 * @return Attribute.baseHealth
	 */
	@Contract(pure = true)
	final public int getBaseHealth() {
		return this.baseHealth;
	}

	/**
	 * Returns the total health.
	 *
	 * @return Attribute.baseHealth + Attribute.baseStrength * Attribute::HEALTH_PER_STRENGTH
	 */
	@Contract(pure = true)
	final public int getTotalHealth() {
		return this.baseHealth + this.baseStrength * (int) Attribute.HEALTH_PER_STRENGTH;
	}

	/**
	 * Returns the base mana.
	 *
	 * @return Attribute.baseMana
	 */
	@Contract(pure = true)
	final public int getBaseMana() {
		return this.baseMana;
	}

	/**
	 * Returns the total mana.
	 *
	 * @return Attribute.baseMana + Attribute.baseIntelligence * Attribute::MANA_PER_INTELLIGENCE
	 */
	@Contract(pure = true)
	final public int getTotalMana() {
		return this.baseMana + this.baseIntelligence * (int) Attribute.MANA_PER_INTELLIGENCE;
	}

	/**
	 * Returns the base damage.
	 *
	 * @return Attribute.baseDamage
	 */
	@Contract(pure = true)
	final public int getBaseDamage() {
		return this.baseDamage;
	}

	/**
	 * Returns the base strength.
	 *
	 * @return Attribute.baseStrength
	 */
	@Contract(pure = true)
	final public int getBaseStrength() {
		return this.baseStrength;
	}

	/**
	 * Returns the base agility.
	 *
	 * @return Attribute.baseAgility
	 */
	@Contract(pure = true)
	final public int getBaseAgility() {
		return this.baseAgility;
	}

	/**
	 * Returns the base intelligence.
	 *
	 * @return Attribute.baseIntelligence
	 */
	@Contract(pure = true)
	final public int getBaseIntelligence() {
		return this.baseIntelligence;
	}

	/**
	 * Returns the base armor.
	 *
	 * @return Attribute.baseArmor
	 */
	@Contract(pure = true)
	final public int getBaseArmor() {
		return this.baseArmor;
	}

	/**
	 * Returns the base magic resistance.
	 *
	 * @return Attribute.baseMagicResistance
	 */
	@Contract(pure = true)
	final public int getBaseMagicResistance() {
		return this.baseMagicResistance;
	}

	/**
	 * Sets the base life.
	 *
	 * @param baseHealth Base life to set
	 */
	final public void setBaseHealth(final int baseHealth) {
		this.baseHealth = baseHealth;
	}

	/**
	 * Sets the base mana.
	 *
	 * @param baseMana Base mana to set
	 */
	final public void setBaseMana(final int baseMana) {
		this.baseMana = baseMana;
	}

	/**
	 * Sets the base damage.
	 *
	 * @param baseDamage Base damage to set
	 */
	final public void setBaseDamage(final int baseDamage) {
		this.baseDamage = baseDamage;
	}

	/**
	 * Sets the base strength.
	 *
	 * @param baseStrength Base strength to set
	 */
	final public void setBaseStrength(final int baseStrength) {
		this.baseStrength = baseStrength;
	}

	/**
	 * Sets the base agility.
	 *
	 * @param baseAgility Base agility to set
	 */
	final public void setBaseAgility(final int baseAgility) {
		this.baseAgility = baseAgility;
	}

	/**
	 * Sets the base intelligence.
	 *
	 * @param baseIntelligence Base intelligence to set
	 */
	final public void setBaseIntelligence(final int baseIntelligence) {
		this.baseIntelligence = baseIntelligence;
	}

	/**
	 * Sets the base armor.
	 *
	 * @param baseArmor Base armor to set
	 */
	final public void setBaseArmor(final int baseArmor) {
		this.baseArmor = baseArmor;
	}

	/**
	 * Sets the base magic resistance.
	 *
	 * @param baseMagicResistance Base magic resistance to set
	 */
	final public void setBaseMagicResistance(final int baseMagicResistance) {
		this.baseMagicResistance = baseMagicResistance;
	}

	/**
	 * Adds some base life.
	 *
	 * @param baseLife Base life to add
	 */
	final public void addBaseLife(final int baseLife) {
		this.baseHealth += baseLife;
	}

	/**
	 * Adds some base mana.
	 *
	 * @param baseMana Base mana to add
	 */
	final public void addBaseMana(final int baseMana) {
		this.baseMana += baseMana;
	}

	/**
	 * Adds some base damage.
	 *
	 * @param baseDamage Base damage to add
	 */
	final public void addBaseDamage(final int baseDamage) {
		this.baseDamage += baseDamage;
	}

	/**
	 * Adds some base strength.
	 *
	 * @param baseStrength Base strength to add
	 */
	final public void addBaseStrength(final int baseStrength) {
		this.baseStrength += baseStrength;
	}


	/**
	 * Adds some base agility.
	 *
	 * @param baseAgility Base agility to add
	 */
	final public void addBaseAgility(final int baseAgility) {
		this.baseAgility += baseAgility;
	}

	/**
	 * Adds some base intelligence.
	 *
	 * @param baseIntelligence Base intelligence to add
	 */
	final public void addBaseIntelligence(final int baseIntelligence) {
		this.baseIntelligence += baseIntelligence;
	}

	/**
	 * Adds some base armor.
	 *
	 * @param baseArmor Base armor to add
	 */
	final public void addBaseArmor(final int baseArmor) {
		this.baseArmor += baseArmor;
	}

	/**
	 * Adds some base magic resistance.
	 *
	 * @param baseMagicResistance Base magic resistance to add
	 */
	final public void addBaseMagicResistance(final int baseMagicResistance) {
		this.baseMagicResistance += baseMagicResistance;
	}

}