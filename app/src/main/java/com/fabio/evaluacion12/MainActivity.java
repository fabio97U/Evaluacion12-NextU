package com.fabio.evaluacion12;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.content.pm.Signature;

public class MainActivity extends Activity {
    private AdView adView;
    private CallbackManager cM;
    private LoginButton lB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Codigo para poder hacer el enlace con Fb
        
        FacebookSdk.sdkInitialize(getApplicationContext());//Se manda como argumento el contexto
        cM = CallbackManager.Factory.create();
    
        getFbKeyHash("Jy9MckBLlPqi4PN5ABN0rLHkmQc=");//Llave Hash
        setContentView(R.layout.activity_main);
    
        lB = (LoginButton)findViewById(R.id.login_facebook);//Se enlaza la parte grafica con la logica del boton
    
        lB.registerCallback(cM, new FacebookCallback<LoginResult>() {//Se registra el callback para poder utilizar sus metodos
            @Override
            public void onSuccess(LoginResult loginResult) {//Si el logeo de es exitoso
                mensajeToast("¡Inicio de sesión exitoso!");
            }
            @Override
            public void onCancel() {//Si se cancela el logeo
                mensajeToast("¡Inicio de sesión cancelado!");
            }
            @Override
            public void onError(FacebookException error) {//Si ocurre un error inesperado
                mensajeToast("¡Inicio de sesión NO exitoso!");
            }
        });
        adView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);
    }
    //Metodo para imprimir los mensaje Toast
    private void mensajeToast(String mensaje){
        Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
    }
    //Metodo para validar la clave Hash
    public void getFbKeyHash(String packageName){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            for(Signature signature : info.signatures){//Ciclo para evaluar el Hash
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash :", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                System.out.println("KeyHash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e){//Excecion1 si ocure una error no encontrado se manda un mensaje
            mensajeToast(e.toString());
        }catch (NoSuchAlgorithmException e){//Excecion2
            mensajeToast(e.toString());
        }
        
    }
    protected void onActivityResult(int reqCode, int resCode, Intent i){
        cM.onActivityResult(reqCode, resCode, i);
    }
    
    /*==Metodos parte del ciclo de vidad de la app==*/
    //Cuando se pausa
    @Override
    protected void onPause() {
        if(adView != null)
            adView.pause();//se utiliza el metodo .pause(); para detener el objeto de tipo AdView
        super.onPause();
    }
    //Cuando esta activa
    @Override
    protected void onResume() {
        if(adView != null)
            adView.resume();
        super.onResume();
    }
    //Cuando se cierra
    @Override
    protected void onDestroy() {
        if(adView != null)
            adView.destroy();//se utiliza el metodo .destroy(); para destruir el objeto de tipo AdView
        super.onDestroy();
    }
    
}
