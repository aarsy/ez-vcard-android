package com.ezvcard.android.testActivity;

import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.ezvcard.android.ContactOperations;
import com.ezvcard.android.R;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.io.text.VCardReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

/*
 Copyright (c) 2013, Michael Angstadt
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 The views and conclusions contained in the software and documentation are those
 of the authors and should not be interpreted as representing official policies,
 either expressed or implied, of the FreeBSD Project.
 */

/**
 * A sample Android activity which demonstrates on how to use this Vcard Android mapping library
 * There are two ways to parse and insert the vcard
 * <p/>
 * If the vcf file is small then take a look at
 * parseVcardList() which takes a list of vcards and inserts them
 * <p/>
 * If the VCF file is huge then stream the vcf file using
 * VCardReader and each vcard can be inserted.
 * <p/>
 * For this take a look at parseVcardStream()
 * <p/>
 * ContactOperations.ContactsRestoreCallback() can be used to get the call back
 * after each contact getting inserted into android native addressbook
 * <p/>
 * This callback can be used to calculate the number of contacts successfully inserted vs
 * the contacts failed and also to show the progress bar kind of stuff.
 *
 * @author Pratyush
 */


public class VcardParser extends ActionBarActivity {


    private static final String TAG = VcardParser.class.getSimpleName();
    private static String cacheFolderLocation = getExternalVCFFile();


    private static String getExternalVCFFile() {
        String state = Environment.getExternalStorageState();
        String filelocationvalue = null;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            filelocationvalue = Environment.getExternalStorageDirectory().toString() + "/" + "1517814042cards.vcf";
            Log.i("VCardparseActivity", "The location of the vcf file is " + filelocationvalue);
        }
        return filelocationvalue;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vcard_parser);
        try {
            // parseVcardStream();
            parseVcardList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void parseVcardList() throws IOException {
        File file = new File(cacheFolderLocation);
        List<VCard> vCards = Ezvcard.parse(file).all();
        ContactOperations opeartions = new ContactOperations(getApplicationContext(), "Phone", "com.motorola.android.buacontactadapter");
        for (VCard card : vCards) {
            opeartions.insertContact(card, new ContactOperations.ContactsRestoreCallback() {
                @Override
                public void error(Exception e) {

                }

                @Override
                public void processing() {

                }

                @Override
                public void processed() {

                }
            });
        }
    }

    private void parseVcardStream() throws IOException {
        Reader reader = new FileReader(new File(cacheFolderLocation));
        final VCardReader vCardReader = new VCardReader(reader);
        final ContactOperations opeartions = new ContactOperations(getApplicationContext());
        try {
            VCard vCard = null;
            while ((vCard = vCardReader.readNext()) != null) {
                opeartions.insertContact(vCard, new ContactOperations.ContactsRestoreCallback() {
                    @Override
                    public void error(Exception e) {

                    }

                    @Override
                    public void processing() {

                    }

                    @Override
                    public void processed() {

                    }
                });
            }

          /*  new Thread(new Runnable() {
                @Override
                public void run() {
                    VCard vCard = null;
                    try {
                        while ((vCard = vCardReader.readNext()) != null) {
                            opeartions.addContact(vCard);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vcard_parser, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
