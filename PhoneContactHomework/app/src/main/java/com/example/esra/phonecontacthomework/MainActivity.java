package com.example.esra.phonecontacthomework;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import android.widget.RadioButton;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    ListView list;
    ArrayList<Person> phone_contacts;
    PersonAdapter person_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phone_contacts = new ArrayList<Person>();
        list = (ListView) findViewById((R.id.listView_contacts));
        readPhoneContacts();
        person_calling();
    }

    //Get the phone contacts
    public void readPhoneContacts() {

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);

        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                String person_name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                String person_phoneNumber = null;

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                if (hasPhoneNumber > 0) {

                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);

                    while (phoneCursor.moveToNext()) {

                        person_phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        phone_contacts.add(new Person(person_name, person_phoneNumber, R.drawable.contact_img));
                    }
                    phoneCursor.close();
                }
            }
        }
        person_adapter = new PersonAdapter(this, R.layout.listview_item, phone_contacts);
        if (list != null) {
            list.setAdapter(person_adapter);
        }
    }


    //Calling person in the phone contact
    public void person_calling(){
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> list, View row, int position, long rowID) {
                                            Uri personNumber = Uri.parse("tel:" + phone_contacts.get(position).getPersonNumber());
                                            Intent call_Intent = new Intent(Intent.ACTION_DIAL, personNumber);
                                            startActivity(call_Intent);
                                        }
                                    }
        );
    }


    //Filtering of operator (Turkcell,TurkTelekom,Vodafone)
    public void number_filtering(View view) {

        Person person = null;
        String phoneNumbers = null;
        ArrayList<Person> selected_operator = new ArrayList<Person>();

        boolean checked = ((RadioButton) view).isChecked();

        // I checked which radio button was clicked
        switch (view.getId()) {

            //If number is Turkcell operator
            case R.id.turkcell_number:
                if (checked)
                    for (int i = 0; i < phone_contacts.size(); i++) {
                        person = phone_contacts.get(i);
                        phoneNumbers = person.getPersonNumber();
                        String operator = phoneNumbers.substring(1, 3);
                        if (operator.contains("53") == true) {
                            selected_operator.add(new Person(person.getPersonName(), phoneNumbers, R.drawable.contact_img));
                        }
                    }
                person_adapter = new PersonAdapter(this, R.layout.listview_item, selected_operator);
                if (list != null) {
                    list.setAdapter(person_adapter);
                }
                break;

            //If number is vodafone operator
            case R.id.vodafone_number:
                if (checked)
                    for (int i = 0; i < phone_contacts.size(); i++) {
                        person = phone_contacts.get(i);
                        phoneNumbers = person.getPersonNumber();
                        String operator = phoneNumbers.substring(1, 3);
                        if (operator.contains("54") == true) {
                            selected_operator.add(new Person(person.getPersonName(), phoneNumbers, R.drawable.contact_img));
                        }
                    }
                person_adapter = new PersonAdapter(this, R.layout.listview_item, selected_operator);
                if (list != null) {
                    list.setAdapter(person_adapter);
                }
                break;

            //If number is TurkTelekom operator
            case R.id.avea_number:
                if (checked)
                    for (int i = 0; i < phone_contacts.size(); i++) {
                        person = phone_contacts.get(i);
                        phoneNumbers = person.getPersonNumber();
                        String operator = phoneNumbers.substring(1, 3);
                        if (operator.contains("50") == true || operator.contains("55") == true) {
                            selected_operator.add(new Person(person.getPersonName(), phoneNumbers, R.drawable.contact_img));
                        }
                    }
                person_adapter = new PersonAdapter(this, R.layout.listview_item, selected_operator);
                if (list != null) {
                    list.setAdapter(person_adapter);
                }
                break;

            //If I want to see all operators
            case R.id.all_number:
                if (checked)
                    for (int i = 0; i < phone_contacts.size(); i++) {
                        person = phone_contacts.get(i);
                        phoneNumbers = person.getPersonNumber();
                        selected_operator.add(new Person(person.getPersonName(), phoneNumbers, R.drawable.contact_img));
                    }
                person_adapter = new PersonAdapter(this, R.layout.listview_item, selected_operator);
                if (list != null) {
                    list.setAdapter(person_adapter);
                }
                break;
        }
        person_adapter = new PersonAdapter(this, R.layout.listview_item, phone_contacts);
    }


    //Back-up operation for exist the phone contacts.
    public void phoneContact_backup(View view) throws FileNotFoundException {

        try {
            //Writing contacts in the PhoneContact_file
            PrintStream fileOutputStream = new PrintStream(openFileOutput("PhoneContact_file", MODE_PRIVATE));

            for (int i = 0; i < phone_contacts.size(); i++) {
                fileOutputStream.println((phone_contacts.get(i).getPersonName().toString() + "\t" + phone_contacts.get(i).getPersonNumber().toString()));//It is writing with tab character.
                Toast.makeText(getApplicationContext(),"Back-up operation is successed!",Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Recovery operation for damaged phone contacts.
    public void phoneContact_recovery(View view) {
        try {
            String PersonName;
            String PhoneNumber;

            File file =getApplicationContext().getFileStreamPath("PhoneContact_file");//Finds the file path.

            //If PhoneContact_file is exist,I mean back-up operation is successed.
            if(file.exists()) {
                Scanner scan = new Scanner(openFileInput("PhoneContact_file"));
                while (scan.hasNext()) {
                    Person person = new Person(null, null, 0);
                    String splitString[];
                    splitString = (scan.nextLine().split("\t"));//I am splitting according to tab character.
                    person.setPersonName(splitString[0].toString());
                    PersonName = splitString[0].toString();
                    person.setPersonNumber(splitString[1].toString());
                    PhoneNumber =splitString[1].toString() ;

                    phone_contacts.add(person);

                    //This part update the phone contacts.
                    ArrayList<ContentProviderOperation> update = new ArrayList<ContentProviderOperation>();
                    update.add(ContentProviderOperation.newInsert(
                            ContactsContract.RawContacts.CONTENT_URI)
                            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                            .build());

                    //PersonName
                    if (PersonName != null) {
                        update.add(ContentProviderOperation.newInsert(
                                ContactsContract.Data.CONTENT_URI)
                                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                .withValue(ContactsContract.Data.MIMETYPE,
                                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                                .withValue(
                                        ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                                        PersonName).build());
                    }

                    //PersonPhoneNumber
                    if (PhoneNumber != null) {
                        update.add(ContentProviderOperation.
                                newInsert(ContactsContract.Data.CONTENT_URI)
                                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                .withValue(ContactsContract.Data.MIMETYPE,
                                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, PhoneNumber)
                                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                                .build());
                    }
                    // Asking the Contact provider to create a new contact_img
                    try {
                        getContentResolver().applyBatch(ContactsContract.AUTHORITY, update);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

           }else{//If I did not make back-up operation,I show a toast message.
                Toast.makeText(getApplicationContext(),"You should take a back-up!",Toast.LENGTH_SHORT).show();
            }

            PersonAdapter adapter = new PersonAdapter(this, R.layout.listview_item, phone_contacts);
            list.setAdapter(adapter);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}