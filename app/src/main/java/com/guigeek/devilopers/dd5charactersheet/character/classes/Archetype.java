package com.guigeek.devilopers.dd5charactersheet.character.classes;

import android.content.Context;

import com.guigeek.devilopers.dd5charactersheet.character.Attack;
import com.guigeek.devilopers.dd5charactersheet.character.Character;
import com.guigeek.devilopers.dd5charactersheet.character.Fettle;
import com.guigeek.devilopers.dd5charactersheet.character.Power;

import java.io.Externalizable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ggallani on 19/02/2016.
 */
public interface Archetype {

    public String getName();

    public List<String> getLevelUpBenefits(int iNewCharacterLevel, Context context);
    public LinkedList<Power> getPowers(int iLevel, Character iCharac);
    public LinkedList<Fettle> getFettles(Character character, int classLevel);
    public List<Attack> getSpecialClassAttacks(Character iCharacter, int classLevel);

    public int getChoosableFeature(int iLevel);
    public void setArchetypeFeature(String iFeature);
}
