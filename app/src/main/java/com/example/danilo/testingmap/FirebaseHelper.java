package com.example.danilo.testingmap;

import com.google.android.gms.common.api.BooleanResult;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Danilo on 2016-07-22.
 */

public class FirebaseHelper {

    DatabaseReference db;

    Boolean saved = null;

    public FirebaseHelper(DatabaseReference db){

        this.db = db;
    }

    public boolean SavedDriverData(Driverdb driverdb){

        if(driverdb == null)
        {
            saved = false;
        }else {
            try {
                db.child("DriverReg").setValue(driverdb);
                saved =true;
            }catch (DatabaseException ex){
                ex.printStackTrace();
            }
        }
        return  saved;
    }
}
