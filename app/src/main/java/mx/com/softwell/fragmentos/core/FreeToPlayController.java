package mx.com.softwell.fragmentos.core;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import mx.com.softwell.fragmentos.api.API;
import mx.com.softwell.fragmentos.api.apiservices.FreeToPlayService;
import mx.com.softwell.fragmentos.gui.FreeToPlay;
import mx.com.softwell.fragmentos.gui.MainActivity;
import mx.com.softwell.fragmentos.gui.TopJuegos;
import mx.com.softwell.fragmentos.model.Juego;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FreeToPlayController {
    private static FreeToPlayController instance=null;
    private MiscController miscController = MiscController.Instance();
    private boolean status = false;
    private String message = "";
    private String data = "";
    private List<Juego> Juego;
    Type JuegoType = new TypeToken<List<Juego>>(){}.getType();
    private static String TAG = "FreeToPlayController";

    private FreeToPlayController() {

    }

    public static FreeToPlayController Instance(){
        if(instance == null)
            instance = new FreeToPlayController();
        return instance;
    }

    public void GetAll(){
        API.getApi().create(FreeToPlayService.class).getAll().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    status = jsonObject.getBoolean("status");
                    message = jsonObject.getString("message");
                    if(status){
                        data = jsonObject.getJSONArray("data").toString();
                        Juego = new Gson().fromJson(data, JuegoType);
                        //miscController.CloseWait();
                        Log.e(TAG, data);
                        Log.e(TAG, Juego.toString());
                        ((FreeToPlay) MainActivity.GLOBALS.get("freeToPlay")).actualizar(Juego);
                    }else{
                        miscController.CloseWait();
                        Log.e(TAG, message);
                    }
                }catch (JSONException e){
                    miscController.CloseWait();
                    Log.e(TAG, e.getMessage());

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, t.getMessage()  );

            }
        });
    }
}
