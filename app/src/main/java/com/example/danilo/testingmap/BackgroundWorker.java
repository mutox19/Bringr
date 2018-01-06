package com.example.danilo.testingmap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;


import com.amazonaws.http.HttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Danilo on 2016-08-25.
 */
public class BackgroundWorker extends AsyncTask<String, Void, String>
{
    Context context;
    AlertDialog alertDialog;
    String type;
    String Name, Email, Login, Pass;
    BackgroundWorker(Context ctx){
        context = ctx;
    }
    @Override
    protected String doInBackground(String... params) {
        type = params[0];
        //String login_URL = "http://192.168.2.25:8080/android_login/login.php";

        //below is the php version of the app
        //notify_URL = "http://192.168.2.25:8080/firebase/notify.php";
        String notify_URL = "";

        if(type.equals("driverAccept"))
        {
            try {

                String token = params[1];
                String driverName = params[2];
                String driverID = params[3];
                String orderNum = params[4];
                String customerID = params[5];
                String message = "Driver " + driverName + " has accepted to deliver your order";
                String title = "Driver Accept";
                //URL url = new URL(login_URL);
                URL url = null; //Enter URL here URL url = null;
                url = new URL("http://192.168.2.25:3000/notify");
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String postData = URLEncoder.encode("Type","UTF-8") + "=" + URLEncoder.encode(type,"UTF-8")+ "&"
                        + URLEncoder.encode("Title","UTF-8") + "=" + URLEncoder.encode(title,"UTF-8")+ "&"
                        + URLEncoder.encode("FID","UTF-8") + "=" + URLEncoder.encode(token,"UTF-8")+ "&"
                        + URLEncoder.encode("Title","UTF-8") + "=" + URLEncoder.encode(title,"UTF-8")+ "&"
                        + URLEncoder.encode("driver","UTF-8") + "=" + URLEncoder.encode(driverID,"UTF-8")+ "&"
                        + URLEncoder.encode("Order","UTF-8") + "=" + URLEncoder.encode(orderNum,"UTF-8")+ "&"
                        + URLEncoder.encode("customer","UTF-8") + "=" + URLEncoder.encode(customerID,"UTF-8")+ "&"
                        + URLEncoder.encode("Message","UTF-8") + "=" + URLEncoder.encode(message,"UTF-8");

                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";

                while((line = bufferedReader.readLine()) != null)
                {
                    result += line;
                }
                Log.d("logDete", "doInBackground: " + result);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //this is the original one

        /*
        else if(type.equals("driverAccept2"))
        {
            try {

                String token = params[1];
                String driverName = params[2];
                String driverID = params[3];
                String orderNum = params[4];
                String customerID = params[5];
                String message = "Driver " + driverName + " has accepted to deliver your order";
                String title = "Driver Accept";
                //String user = params[3];
                //String last = params[4];
                //String userEmail = params[5];

                notify_URL = "http://192.168.2.25:8080/firebase/notify.php";
                //notify_URL = "http://192.168.2.25:3000/notify";
                //"http://192.168.5.3:8080/firebase/?regId=foQ7nhZGzVo%3AAPA91bHiTy0hwr8N17We4a42LBEBpWPiy3mITUUBFUe19M0Iuv7Y_ph87mMsnFbIJmdv63GQqMEV9wBRUJdSn6zRv4vx65P1JU0qXg-QXLC_fC-trGKMvEePzZEapPs1trnv-KzgzPWz&title="+userName+"&message="+first+"&push_type=individual";
                URL url = new URL(notify_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String postData = URLEncoder.encode("FID","UTF-8") + "=" + URLEncoder.encode(token,"UTF-8")+ "&"
                + URLEncoder.encode("Type","UTF-8") + "=" + URLEncoder.encode(type,"UTF-8")+ "&"
                        + URLEncoder.encode("Title","UTF-8") + "=" + URLEncoder.encode(title,"UTF-8")+ "&"
                        + URLEncoder.encode("driver","UTF-8") + "=" + URLEncoder.encode(driverID,"UTF-8")+ "&"
                        + URLEncoder.encode("Order","UTF-8") + "=" + URLEncoder.encode(orderNum,"UTF-8")+ "&"
                        + URLEncoder.encode("customer","UTF-8") + "=" + URLEncoder.encode(customerID,"UTF-8")+ "&"
                        + URLEncoder.encode("Message","UTF-8") + "=" + URLEncoder.encode(message,"UTF-8");



                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                Log.d("CODE", "doInBackground: " + httpURLConnection.getResponseCode());
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";

                while((line = bufferedReader.readLine()) != null)
                {
                    result += line + "\n";
                    //text += line + "\n";
                }
                Log.d("logDete", "doInBackground: " + result);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        else if(type.equals("customerChangeDriver"))
        {
            try {

                String driver = params[1];
                String title = params[2];
                String message = params[3];
                String customerID = params[4];
                //String user = params[3];
                //String last = params[4];
                //String userEmail = params[5];
                notify_URL = "http://192.168.2.25:3000/notify";
                //notify_URL = "http://192.168.2.25:8080/firebase/changeDriver.php";
                URL url = new URL(notify_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String postData = URLEncoder.encode("driver","UTF-8") + "=" + URLEncoder.encode(driver,"UTF-8")+ "&"
                        + URLEncoder.encode("Type","UTF-8") + "=" + URLEncoder.encode(type,"UTF-8")+ "&"
                        + URLEncoder.encode("Title","UTF-8") + "=" + URLEncoder.encode(title,"UTF-8")+ "&"
                        + URLEncoder.encode("customer","UTF-8") + "=" + URLEncoder.encode(title,"UTF-8")+ "&"
                        + URLEncoder.encode("Message","UTF-8") + "=" + URLEncoder.encode(message,"UTF-8");



                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                Log.d("CODE", "doInBackground: " + httpURLConnection.getResponseCode());
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";

                while((line = bufferedReader.readLine()) != null)
                {
                    result += line + "\n";
                    //text += line + "\n";
                }
                Log.d("logDete", "doInBackground: " + result);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("customerAcceptDriver"))
        {
            try {

                String driverToken = params[1];
                String title = params[2];
                //String first = params[4];
                String message = params[3];
                String order = params[4];
                String customer = params[5];

                notify_URL = "http://192.168.2.25:3000/notify";
                //"http://192.168.5.3:8080/firebase/?regId=foQ7nhZGzVo%3AAPA91bHiTy0hwr8N17We4a42LBEBpWPiy3mITUUBFUe19M0Iuv7Y_ph87mMsnFbIJmdv63GQqMEV9wBRUJdSn6zRv4vx65P1JU0qXg-QXLC_fC-trGKMvEePzZEapPs1trnv-KzgzPWz&title="+userName+"&message="+first+"&push_type=individual";
                URL url = new URL(notify_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String postData = URLEncoder.encode("driver","UTF-8") + "=" + URLEncoder.encode(driverToken,"UTF-8")+ "&"
                        + URLEncoder.encode("Title","UTF-8") + "=" + URLEncoder.encode(title,"UTF-8")+ "&"
                        + URLEncoder.encode("Type","UTF-8") + "=" + URLEncoder.encode(type,"UTF-8")+ "&"
                        + URLEncoder.encode("customer","UTF-8") + "=" + URLEncoder.encode(customer,"UTF-8")+ "&"
                        + URLEncoder.encode("Order","UTF-8") + "=" + URLEncoder.encode(order,"UTF-8")+ "&"
                        + URLEncoder.encode("Message","UTF-8") + "=" + URLEncoder.encode(message,"UTF-8");

                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                Log.d("CODE", "doInBackground: " + httpURLConnection.getResponseCode());
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";

                while((line = bufferedReader.readLine()) != null)
                {
                    result += line + "\n";
                    //text += line + "\n";
                }
                Log.d("logDete", "doInBackground: " + result);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("customerDeleteOrder"))
        {
            try {

                String driver = params[1];
                String title = params[2];
                String message = params[4];

                //notify_URL = "http://192.168.2.25:8080/firebase/notification.php";
                notify_URL = "http://192.168.2.25:3000/notify";
                //"http://192.168.5.3:8080/firebase/?regId=foQ7nhZGzVo%3AAPA91bHiTy0hwr8N17We4a42LBEBpWPiy3mITUUBFUe19M0Iuv7Y_ph87mMsnFbIJmdv63GQqMEV9wBRUJdSn6zRv4vx65P1JU0qXg-QXLC_fC-trGKMvEePzZEapPs1trnv-KzgzPWz&title="+userName+"&message="+first+"&push_type=individual";
                URL url = new URL(notify_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String postData = URLEncoder.encode("driver","UTF-8") + "=" + URLEncoder.encode(driver,"UTF-8")+ "&"
                        + URLEncoder.encode("Type","UTF-8") + "=" + URLEncoder.encode(type,"UTF-8")+ "&"
                        + URLEncoder.encode("Title","UTF-8") + "=" + URLEncoder.encode(title,"UTF-8")+ "&"
                        + URLEncoder.encode("Message","UTF-8") + "=" + URLEncoder.encode(message,"UTF-8");

                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                Log.d("CODE", "doInBackground: " + httpURLConnection.getResponseCode());
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";

                while((line = bufferedReader.readLine()) != null)
                {
                    result += line + "\n";
                    //text += line + "\n";
                }
                Log.d("logDete", "doInBackground: " + result);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("StripePaySuccess"))
        {
            try {

                String driver = params[1];
                String customer = params[2];
                String order = params[3];
                String stripe = params[4];
                String title = params[5];
                String message = params[6];
                //notify_URL = "http://192.168.2.25:8080/firebase/notify.php";
                notify_URL = "http://192.168.2.25:3000/notify";
                //"http://192.168.5.3:8080/firebase/?regId=foQ7nhZGzVo%3AAPA91bHiTy0hwr8N17We4a42LBEBpWPiy3mITUUBFUe19M0Iuv7Y_ph87mMsnFbIJmdv63GQqMEV9wBRUJdSn6zRv4vx65P1JU0qXg-QXLC_fC-trGKMvEePzZEapPs1trnv-KzgzPWz&title="+userName+"&message="+first+"&push_type=individual";
                URL url = new URL(notify_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String postData = URLEncoder.encode("driver","UTF-8") + "=" + URLEncoder.encode(driver,"UTF-8")+ "&"
                        + URLEncoder.encode("Type","UTF-8") + "=" + URLEncoder.encode(type,"UTF-8")+ "&"
                        + URLEncoder.encode("customer","UTF-8") + "=" + URLEncoder.encode(customer,"UTF-8")+ "&"
                        + URLEncoder.encode("Order","UTF-8") + "=" + URLEncoder.encode(order,"UTF-8")+ "&"
                        + URLEncoder.encode("dbz","UTF-8") + "=" + URLEncoder.encode(stripe,"UTF-8")+ "&"
                        + URLEncoder.encode("Title","UTF-8") + "=" + URLEncoder.encode(title,"UTF-8")+ "&"
                        + URLEncoder.encode("Message","UTF-8") + "=" + URLEncoder.encode(message,"UTF-8");

                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                Log.d("CODE", "doInBackground: " + httpURLConnection.getResponseCode());
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";

                while((line = bufferedReader.readLine()) != null)
                {
                    result += line + "\n";
                    //text += line + "\n";
                }
                Log.d("logDete", "doInBackground: " + result);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("ProcessTotal"))
        {
            try {

                String driver = params[1];
                String customer = params[2];
                String order = params[3];
                String custFID = params[4];
                String title = params[5];
                String message = params[6];
                //notify_URL = "http://192.168.2.25:8080/firebase/notify.php";
                notify_URL = "http://192.168.2.25:3000/notify";
                //"http://192.168.5.3:8080/firebase/?regId=foQ7nhZGzVo%3AAPA91bHiTy0hwr8N17We4a42LBEBpWPiy3mITUUBFUe19M0Iuv7Y_ph87mMsnFbIJmdv63GQqMEV9wBRUJdSn6zRv4vx65P1JU0qXg-QXLC_fC-trGKMvEePzZEapPs1trnv-KzgzPWz&title="+userName+"&message="+first+"&push_type=individual";
                URL url = new URL(notify_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String postData = URLEncoder.encode("driver","UTF-8") + "=" + URLEncoder.encode(driver,"UTF-8")+ "&"
                        + URLEncoder.encode("Type","UTF-8") + "=" + URLEncoder.encode(type,"UTF-8")+ "&"
                        + URLEncoder.encode("Title","UTF-8") + "=" + URLEncoder.encode(title,"UTF-8")+ "&"
                        + URLEncoder.encode("customer","UTF-8") + "=" + URLEncoder.encode(customer,"UTF-8")+ "&"
                        + URLEncoder.encode("FID","UTF-8") + "=" + URLEncoder.encode(custFID,"UTF-8")+ "&"
                        + URLEncoder.encode("Order","UTF-8") + "=" + URLEncoder.encode(order,"UTF-8")+ "&"
                        + URLEncoder.encode("Message","UTF-8") + "=" + URLEncoder.encode(message,"UTF-8");

                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                Log.d("CODE", "doInBackground: " + httpURLConnection.getResponseCode());
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";

                while((line = bufferedReader.readLine()) != null)
                {
                    result += line + "\n";
                    //text += line + "\n";
                }
                Log.d("logDete", "doInBackground: " + result);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("DriverProcessOrder"))
        {
            try {

                String driverToken = params[1];
                String title = params[2];
                //String first = params[4];
                String message = params[3];
                String order = params[4];
                String customer = params[5];

                notify_URL = "http://192.168.2.25:3000/notify";
                //"http://192.168.5.3:8080/firebase/?regId=foQ7nhZGzVo%3AAPA91bHiTy0hwr8N17We4a42LBEBpWPiy3mITUUBFUe19M0Iuv7Y_ph87mMsnFbIJmdv63GQqMEV9wBRUJdSn6zRv4vx65P1JU0qXg-QXLC_fC-trGKMvEePzZEapPs1trnv-KzgzPWz&title="+userName+"&message="+first+"&push_type=individual";
                URL url = new URL(notify_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String postData = URLEncoder.encode("driver","UTF-8") + "=" + URLEncoder.encode(driverToken,"UTF-8")+ "&"
                        + URLEncoder.encode("Title","UTF-8") + "=" + URLEncoder.encode(title,"UTF-8")+ "&"
                        + URLEncoder.encode("Type","UTF-8") + "=" + URLEncoder.encode(type,"UTF-8")+ "&"
                        + URLEncoder.encode("customer","UTF-8") + "=" + URLEncoder.encode(customer,"UTF-8")+ "&"
                        + URLEncoder.encode("Order","UTF-8") + "=" + URLEncoder.encode(order,"UTF-8")+ "&"
                        + URLEncoder.encode("Message","UTF-8") + "=" + URLEncoder.encode(message,"UTF-8");

                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                Log.d("CODE", "doInBackground: " + httpURLConnection.getResponseCode());
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";

                while((line = bufferedReader.readLine()) != null)
                {
                    result += line + "\n";
                    //text += line + "\n";
                }
                Log.d("logDete", "doInBackground: " + result);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Register Status");
    }
    @Override
    protected void onPostExecute(String result) {
        Log.d("alertLogg", "onPostExecute: " + result);
        alertDialog.setMessage(result);
        alertDialog.show();

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
