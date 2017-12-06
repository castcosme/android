package com.example.fernando.SyncTaskClass;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.session.MediaSession;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.alimento_api.AlimentoApi;
import com.alimento_api.model.MessagesAlimentoInput;
import com.alimento_api.model.MessagesCodeMessage;

/**
 * Created by adsoft on 14/11/17.
 */

public class tweetSyncTask extends AsyncTask<String,Void,MessagesCodeMessage> {

    Context context;
    private ProgressDialog pd;
    MessagesCodeMessage respuesta;

    public tweetSyncTask(Context context) {this.context = context; }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        pd = new ProgressDialog(context);
        pd.setMessage("Login");
        pd.show();
    }




    @Override
    protected MessagesCodeMessage doInBackground(String... params) {

        respuesta = new MessagesCodeMessage();
        try
        {
            AlimentoApi.Builder builder =
                    new AlimentoApi.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
            AlimentoApi service = builder.build();
            MessagesAlimentoInput log = new MessagesAlimentoInput();
            //params es una lista de strings que funciona como argv
            //[0] = email, [1] = password
            log.setTitle(params[0]);
            log.setDescription(params[1]);
            log.setToken(params[2]);
            log.setUrlImage(params[3]);
            respuesta = service.alimento().insert(log).execute();
        }
        catch (Exception e)
        {
            Log.d("Error al insertar alim.", e.getMessage(), e);
        }
        finally
        {
            return respuesta;
        }
    }


    @Override
    protected void onPostExecute(MessagesCodeMessage messagesTokenMessage)
    {
        pd.dismiss();
        if(respuesta.getCode() == 1)
            Toast.makeText(this.context, "Insert succesfully " + respuesta.getMessage(), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this.context,"Error at insert tweet !!!",Toast.LENGTH_SHORT).show();
    }
}

