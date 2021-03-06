package com.guigeek.devilopers.dd5charactersheet.character.classes.sorcerer;

import android.content.Context;

import com.guigeek.devilopers.dd5charactersheet.App;
import com.guigeek.devilopers.dd5charactersheet.R;
import com.guigeek.devilopers.dd5charactersheet.character.classes.BaseArchetype;
import com.guigeek.devilopers.dd5charactersheet.character.Character;
import com.guigeek.devilopers.dd5charactersheet.character.Enumerations;
import com.guigeek.devilopers.dd5charactersheet.character.Power;

import java.util.LinkedList;
import java.util.List;


public class Sorcerer_wild extends BaseArchetype {
    static final long serialVersionUID = 213L;

    public Sorcerer_wild(){}

    @Override
    public String getName() {
        return App.getResString(R.string.sorcerer_wild);
    }

    @Override
    public List<String> getLevelUpBenefits(int iNewCharacterLevel, Context context) {
        List<String> levelUp = new LinkedList<>();

        if (iNewCharacterLevel == 1) {
            levelUp.add("You gained Magic Surge!");
            levelUp.add("You gained Tides of Chaos!");
        }
        if (iNewCharacterLevel == 6) {
            levelUp.add("You gained Bend Luck!");
        }
        if (iNewCharacterLevel == 14) {
            levelUp.add("You gained Controlled Chaos!");
        }
        if (iNewCharacterLevel == 18) {
            levelUp.add("You gained Spell Bombardment!");
        }

        return levelUp;
    }

    public LinkedList<Power> getPowers(int iLevel, Character iCharac) {
        LinkedList<Power> powers = new LinkedList<>();


        if (iLevel >= 1) {
            powers.add(new Power("Magic Surge", "The DM can have you roll a D20 after casting a spell. On a 1, apply a random Magic Surge effect.", "", -1, -1, true, Enumerations.ActionType.PASSIVE));
            powers.add(new Power("Tides of Chaos", "Gain advantage on an attack roll, ability check or saving throw. While this feature is exhausted, the DM can have you roll a Magic Surge - you then regain this feature. Otherwise, you regain it on a long rest.", "", 1, -1, true, Enumerations.ActionType.PASSIVE));
        }

        if (iLevel >= 6) {
        	powers.add(new Power("Bend Luck", "When a creature that you can see makes an attack roll, ability check or saving throw, you may spend 2 Sorcery points as a reaction to roll 1D4 and apply the result as a bonus or malus. May be done after the creature's roll.", "", -1, -1, true, Enumerations.ActionType.REACTION));
        }

        if (iLevel >= 14) {
    		powers.add(new Power("Controlled Chaos", "You can roll twice and choose the roll to keep for Magic Surges.", "", -1, -1, true, Enumerations.ActionType.PASSIVE));
    	}

        if (iLevel >= 18) {
            powers.add(new Power("Spell Bombardment", "When you roll damage for a spell and a dice rolls its maximum, roll it again and add the result to the damage. Use only once per turn.", "Melee", -1, -1, true, Enumerations.ActionType.PASSIVE));
        }

        return powers;
    }
}
