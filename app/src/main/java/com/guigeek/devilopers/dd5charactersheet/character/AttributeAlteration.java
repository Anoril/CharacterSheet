package com.guigeek.devilopers.dd5charactersheet.character;

/**
 * Created by ggallani on 19/02/2016.
 */
public class AttributeAlteration extends Fettle {
    public Enumerations.Attributes _subType;

    public AttributeAlteration(){}

    public AttributeAlteration(int iValue, Enumerations.Attributes iAttribute) {
        super (Enumerations.FettleType.ATTRIBUTE_MODIFIER, iValue, iAttribute.ordinal());
        _subType = iAttribute;
    }
}