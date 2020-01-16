package com.guigeek.devilopers.dd5charactersheet.character;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.guigeek.devilopers.dd5charactersheet.App;
import com.guigeek.devilopers.dd5charactersheet.R;
import com.guigeek.devilopers.dd5charactersheet.android.StatsScreen;
import com.guigeek.devilopers.dd5charactersheet.android.StringListAdapter;
import com.guigeek.devilopers.dd5charactersheet.character.classes.Warlock_pact_blade;
import com.guigeek.devilopers.dd5charactersheet.character.classes.Warlock_pact_tome;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by totou on 15/06/2016.
 */
public abstract class BaseClass implements Class, Externalizable {
    static final long serialVersionUID = 200L;

    protected int _version = 1;
    protected LinkedList<Archetype> _archetypes = new LinkedList<>();

    public int getChoosableArchetypes(int iNewlevel) {
        return -1;
    }

    protected int[][] _spellSlots = {
            // spell level 0-9
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //character lv 1
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},//lv 5
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},//lv 10
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},//lv 15
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}//ln 20
    };

    protected int[][] _spellsKnown = {
            // cantrips, spells
            {0, 0},
            {0, 0}, //character lv 1
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},//lv 5
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},//lv 10
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},//lv 15
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0},
            {0, 0}//ln 20
    };

    @Override
    public int[] getSpellSlots(int iCharacterLevel) {
        return _spellSlots[Math.min(20, iCharacterLevel)];
    }

    @Override
    public boolean isCaster() {
        return false;
    }

    @Override
    public int[] getSpellsKnown(int iCharacterLevel) {
        return _spellsKnown[Math.min(20, iCharacterLevel)];
    }

    @Override
    public int getAC(Character character) {
        int ac = character._equippedArmor.getAC(character);

        if (character._equippedShield != null && character._equippedShield._type == Enumerations.ArmorTypes.SHIELD) {
            ac+= character._equippedShield.getAC(character);
        }

        return ac;
    }

    @Override
    public Enumerations.Attributes getMainSpellAttribute() {
        return Enumerations.Attributes.CHA;
    }

    @Override
    public void addArchetype(Archetype iArchetype) {
        if (_archetypes == null) {
            _archetypes = new LinkedList<>();
        }
        _archetypes.add(iArchetype);
    }

    @Override
    public String getQualifiedClassName() {
        String name = getClassName();

        if (_archetypes != null && _archetypes.size() > 0) {
            name += " (";
            for (int i = 0; i < _archetypes.size(); ++i) {
                name += _archetypes.get(i).getName();
                if (i < _archetypes.size() -1) {
                    name += ", ";
                }
            }
            name += ")";
        }
        return name;
    }

    @Override
    public int getAttacksPerRound(Character iCharacter) {
        return 1;
    }

    @Override
    public List<String> getAllLevelUpBenefits(int iNewCharacterLevel, Context context) {
        List<String> allItems = getLevelUpBenefits(iNewCharacterLevel, context);
        if (_archetypes != null) {
            for (Archetype arc: _archetypes) {
                allItems.addAll(arc.getLevelUpBenefits(iNewCharacterLevel, context));
            }
        }

        clearArchetypesOnLevelDown(iNewCharacterLevel);
        checkArchetypeSelection(iNewCharacterLevel, context);

        return allItems;
    }

    private void checkArchetypeSelection(int iNewCharacterLevel, Context context) {
        int choosableArchetypesArray = getChoosableArchetypes(iNewCharacterLevel);
        if (choosableArchetypesArray != -1) {
            AlertDialog.Builder b = new AlertDialog.Builder(context);
            b.setTitle("Select a class feature");

            final String[] allOptions = context.getResources().getStringArray(choosableArchetypesArray);
            final List<String> allOptionsList = Arrays.asList(allOptions);

            b.setAdapter(new StringListAdapter(context, R.layout.list_string, allOptionsList), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    String selectedOption = allOptions[which];
                    addArchetype(getArchetypeByName(selectedOption));
                }
            });

            b.show();
        }
    }

    @Override
    public void clearArchetypesOnLevelDown(int iNewlevel) {
        if (iNewlevel < 3 && _archetypes.size() > 0) {
            _archetypes.clear();
        }
    }

    @Override
    public Archetype getArchetypeByName(String iName) {
        return null;
    }

    @Override
    public LinkedList<Power> getAllPowers(int iLevel, Character iCharac) {
        LinkedList<Power> allItems = getPowers(iLevel, iCharac);
        if (_archetypes != null) {
            for (Archetype arc: _archetypes) {
                allItems.addAll(arc.getPowers(iLevel, iCharac));
            }
        }

        return allItems;
    }

    @Override
    public LinkedList<Fettle> getAllFettles(Character character) {
        LinkedList<Fettle> allItems = getFettles(character);
        if (_archetypes != null) {
            for (Archetype arc: _archetypes) {
                allItems.addAll(arc.getFettles(character));
            }
        }

        return allItems;
    }


    @Override
    public void writeExternal(ObjectOutput oo) throws IOException
    {
        oo.writeInt(_version);
        oo.writeObject(_archetypes);
        oo.writeObject(_spellSlots);
        oo.writeObject(_spellsKnown);
    }

    @Override
    public void readExternal(ObjectInput oi) throws IOException, ClassNotFoundException
    {
        int version = oi.readInt();
        _version = version;
        if (version >= 1) {
            _archetypes = (LinkedList<Archetype>) oi.readObject();
            _spellSlots = (int[][])oi.readObject();
            _spellsKnown = (int[][])oi.readObject();
        }
    }
}
