package com.guigeek.devilopers.dd5charactersheet.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.guigeek.devilopers.dd5charactersheet.App;
import com.guigeek.devilopers.dd5charactersheet.R;
import com.guigeek.devilopers.dd5charactersheet.character.*;
import com.guigeek.devilopers.dd5charactersheet.character.Character;
import com.guigeek.devilopers.dd5charactersheet.character.Class;
import com.guigeek.devilopers.dd5charactersheet.character.classes.Barbarian_totem;
import com.guigeek.devilopers.dd5charactersheet.character.classes.BloodHunter;
import com.guigeek.devilopers.dd5charactersheet.character.classes.BloodHunter_lycan;
import com.guigeek.devilopers.dd5charactersheet.character.classes.Paladin_vengeance;
import com.guigeek.devilopers.dd5charactersheet.character.classes.Rogue_assassin;
import com.guigeek.devilopers.dd5charactersheet.character.classes.Rogue;
import com.guigeek.devilopers.dd5charactersheet.character.classes.Rogue_swashbuckler;
import com.guigeek.devilopers.dd5charactersheet.character.classes.Sorcerer;
import com.guigeek.devilopers.dd5charactersheet.character.classes.Sorcerer_dragon;
import com.guigeek.devilopers.dd5charactersheet.character.classes.Sorcerer_storm;
import com.guigeek.devilopers.dd5charactersheet.character.classes.Sorcerer_wild;
import com.guigeek.devilopers.dd5charactersheet.character.classes.Warlock_tome_oldOne;
import com.guigeek.devilopers.dd5charactersheet.character.classes.Warlock_blade_fiend;
import com.guigeek.devilopers.dd5charactersheet.character.races.Dragonborn;
import com.guigeek.devilopers.dd5charactersheet.character.races.Elf;
import com.guigeek.devilopers.dd5charactersheet.character.races.HalfElf;
import com.guigeek.devilopers.dd5charactersheet.character.races.HalfOrc;
import com.guigeek.devilopers.dd5charactersheet.character.races.Human;
import com.guigeek.devilopers.dd5charactersheet.character.races.MountainDwarf;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CreateCharacter extends AppCompatActivity {

    Button btnCreate;
    Spinner spRace, spClass;
    EditText inName;

    EditText inSTR, inDEX, inCON, inINT, inWIS, inCHA;
    TextView bonusSTR, bonusDEX, bonusCON, bonusINT, bonusWIS, bonusCHA;
    TextView attributesHelp;

    Race aRace = new Dragonborn();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_character);

        btnCreate = (Button) findViewById(R.id.btnCreate);
        spRace = (Spinner) findViewById(R.id.spinnerRace);
        spClass = (Spinner) findViewById(R.id.spinnerClass);
        inName = (EditText)findViewById(R.id.inName);

        inSTR = findViewById(R.id.inSTR);
        inDEX = findViewById(R.id.inDEX);
        inCON = findViewById(R.id.inCON);
        inINT = findViewById(R.id.inINT);
        inWIS = findViewById(R.id.inWIS);
        inCHA = findViewById(R.id.inCHA);

        bonusSTR = findViewById(R.id.bonusSTR);
        bonusDEX = findViewById(R.id.bonusDEX);
        bonusCON = findViewById(R.id.bonusCON);
        bonusINT = findViewById(R.id.bonusINT);
        bonusWIS = findViewById(R.id.bonusWIS);
        bonusCHA = findViewById(R.id.bonusCHA);
        attributesHelp = findViewById(R.id.attributesHelp);
        updateStatsBonuses();


        ArrayAdapter<CharSequence> adapterRace = ArrayAdapter.createFromResource(this, R.array.races, android.R.layout.simple_spinner_item);
        adapterRace.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRace.setAdapter(adapterRace);

        spRace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String aRaceName = (String)spRace.getSelectedItem();
                if (aRaceName.equals(App.getResString(R.string.race_half_elf))) {
                    aRace = new HalfElf();
                } else if (aRaceName.equals(App.getResString(R.string.race_half_orc))) {
                    aRace = new HalfOrc();
                } else if (aRaceName.equals(App.getResString(R.string.race_human))) {
                    aRace = new Human();
                } else if (aRaceName.equals(App.getResString(R.string.race_mtn_dwarf))) {
                    aRace = new MountainDwarf();
                } else if (aRaceName.equals(App.getResString(R.string.race_elf))) {
                    aRace = new Elf();
                } else if (aRaceName.equals(App.getResString(R.string.race_dragonborn))) {
                    aRace = new Dragonborn();
                }

                updateStatsBonuses();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        ArrayAdapter<CharSequence> adapterClass = ArrayAdapter.createFromResource(this, R.array.classes, android.R.layout.simple_spinner_item);
        adapterClass.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spClass.setAdapter(adapterClass);

        btnCreate.setOnClickListener(new CreateListener());
    }

    private void updateStatsBonuses() {
        HashMap<Integer, String> boosts = new HashMap<>();
        boosts.put(Enumerations.Attributes.STR.ordinal(), "");
        boosts.put(Enumerations.Attributes.DEX.ordinal(), "");
        boosts.put(Enumerations.Attributes.CON.ordinal(), "");
        boosts.put(Enumerations.Attributes.INT.ordinal(), "");
        boosts.put(Enumerations.Attributes.WIS.ordinal(), "");
        boosts.put(Enumerations.Attributes.CHA.ordinal(), "");

        for (Fettle boost: aRace.getAttributeBoost()) {
            boosts.put(boost._describer, "+" + boost._value);
        }

        bonusSTR.setText(boosts.get(Enumerations.Attributes.STR.ordinal()));
        bonusDEX.setText(boosts.get(Enumerations.Attributes.DEX.ordinal()));
        bonusCON.setText(boosts.get(Enumerations.Attributes.CON.ordinal()));
        bonusINT.setText(boosts.get(Enumerations.Attributes.INT.ordinal()));
        bonusWIS.setText(boosts.get(Enumerations.Attributes.WIS.ordinal()));
        bonusCHA.setText(boosts.get(Enumerations.Attributes.CHA.ordinal()));

        attributesHelp.setText(aRace.getAttributeBoostDescription());
    }

    private class CreateListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            Class aClass = null;
            String aClassName = (String)spClass.getSelectedItem();
            if (aClassName.equals(App.getResString(R.string.class_barbarian))) {
                aClass = new Barbarian_totem();
            } else if (aClassName.equals(App.getResString(R.string.class_paladin_vengeance))) {
                aClass = new Paladin_vengeance();
            } else if (aClassName.equals(App.getResString(R.string.class_warlock_tome_oldOne))) {
                aClass = new Warlock_tome_oldOne();
            } else if (aClassName.equals(App.getResString(R.string.class_warlock_blade_fiend))) {
                aClass = new Warlock_blade_fiend();
            } else if (aClassName.equals(App.getResString(R.string.class_rogue))) {
                aClass = new Rogue();
            } else if (aClassName.equals(App.getResString(R.string.class_sorcerer))) {
                aClass = new Sorcerer();
            } else if (aClassName.equals(App.getResString(R.string.class_blood_hunter))) {
                aClass = new BloodHunter();
            }

            Log.d("Create", "Selected race: " + aRace.getName());
            Log.d("Create", "Selected class: " + aClass.getQualifiedClassName());

            handleSubrace(aRace, aClass);
        }
    }

    private void handleSubrace(final Race aRace, final Class aClass) {
        if (aRace instanceof Dragonborn) {
            // Choose ancestry
            AlertDialog.Builder b = new AlertDialog.Builder(CreateCharacter.this);
            b.setTitle("Select a subrace");

            final String[] allAncestriesArray = getResources().getStringArray(R.array.draconicAncestries);
            List<String> allAncestries = Arrays.asList(allAncestriesArray);

            b.setAdapter(new StringListAdapter(CreateCharacter.this, R.layout.list_string, allAncestries), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    String subrace = allAncestriesArray[which];
                    Log.d("Create", "sub-race: " + subrace);
                    aRace.setSubRace(subrace);
                    handleArchetype(aRace, aClass);
                }
            });

            b.show();
        } else {
            handleArchetype(aRace, aClass);
        }
    }

    private void handleArchetype(final Race aRace, final Class aClass) {
        // Classes with implemented archetypes
        if (aClass instanceof BloodHunter || aClass instanceof Rogue || aClass instanceof Sorcerer) {
            AlertDialog.Builder b = new AlertDialog.Builder(CreateCharacter.this);
            b.setTitle("Select an Archetype for this class");

            int archetypesArray = aClass.getChoosableArchetypes();
            final String[] allOptions = getResources().getStringArray(archetypesArray);
            final List<String> allOptionsList = Arrays.asList(allOptions);

            b.setAdapter(new StringListAdapter(CreateCharacter.this, R.layout.list_string, allOptionsList), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    String selectedOption = allOptions[which];
                    Log.d("Create", "Archetype: " + selectedOption);

                    if (selectedOption.equals(App.getResString(R.string.bloodhunter_lycan))) {
                        aClass.addArchetype(new BloodHunter_lycan());
                    }
                    else if (selectedOption.equals(App.getResString(R.string.rogue_assassin))) {
                        aClass.addArchetype(new Rogue_assassin());
                    }
                    else if (selectedOption.equals(App.getResString(R.string.rogue_swashbuckler))) {
                        aClass.addArchetype(new Rogue_swashbuckler());
                    }
                    else if (selectedOption.equals(App.getResString(R.string.sorcerer_dragon))) {
                        aClass.addArchetype(new Sorcerer_dragon());
                    }
                    else if (selectedOption.equals(App.getResString(R.string.sorcerer_storm))) {
                        aClass.addArchetype(new Sorcerer_storm());
                    }
                    else if (selectedOption.equals(App.getResString(R.string.sorcerer_wild))) {
                        aClass.addArchetype(new Sorcerer_wild());
                    }

                    createCharacter(aRace, aClass);
                }
            });

            b.show();
        }
        else {
            createCharacter(aRace, aClass);
        }
    }

    private void createCharacter(Race aRace, Class aClass) {
        int[] attributes = {
            (bonusSTR.getText().length() > 0 ? Integer.parseInt(bonusSTR.getText().toString()):0) + Integer.parseInt(inSTR.getText().toString()),
            (bonusDEX.getText().length() > 0 ? Integer.parseInt(bonusDEX.getText().toString()):0) + Integer.parseInt(inDEX.getText().toString()),
            (bonusCON.getText().length() > 0 ? Integer.parseInt(bonusCON.getText().toString()):0) + Integer.parseInt(inCON.getText().toString()),
            (bonusINT.getText().length() > 0 ? Integer.parseInt(bonusINT.getText().toString()):0) + Integer.parseInt(inINT.getText().toString()),
            (bonusWIS.getText().length() > 0 ? Integer.parseInt(bonusWIS.getText().toString()):0) + Integer.parseInt(inWIS.getText().toString()),
            (bonusCHA.getText().length() > 0 ? Integer.parseInt(bonusCHA.getText().toString()):0) + Integer.parseInt(inCHA.getText().toString())};
        Character aHero = new Character(inName.getText().toString(), aClass, aRace, 1, attributes, null, 0);

        Log.d("Create", aHero.toString());

        Intent intent = new Intent(CreateCharacter.this, MainActivity.class);
        intent.putExtra(Constants.CHARACTER, aHero);
        setResult(RESULT_OK, intent);

        List<String> levelUpBoons = aHero._class.getAllLevelUpBenefits(aHero._level);

        String boons = "";
        for (String s : levelUpBoons) {
            if (s != null && s.length() > 0) {
                boons += s + "\n";
            }
        }

        AlertDialog alertDialog = new AlertDialog.Builder(CreateCharacter.this).create();
        alertDialog.setTitle("Level up");
        alertDialog.setMessage(boons);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.show();
    }
}
