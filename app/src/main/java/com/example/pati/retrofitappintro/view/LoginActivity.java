package com.example.pati.retrofitappintro.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.pati.retrofitappintro.R;
import com.example.pati.retrofitappintro.dagger.App;
import com.example.pati.retrofitappintro.model.LoginCredentials;
import com.example.pati.retrofitappintro.service.LoginRestApi;
import com.example.pati.retrofitappintro.service.TransactionService;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    @Inject
    Retrofit retrofit;

    private LoginRestApi loginRestApi;
    private Button signButton;
    private EditText loginEdit, passEdit;
    private TransactionViewModel transactionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        ((App) getApplication()).getNetComponent().inject(this);
        transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        signButton = findViewById(R.id.signButton);
        loginEdit = findViewById(R.id.login);
        passEdit = findViewById(R.id.pass);
        loginRestApi = retrofit.create(LoginRestApi.class);
        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCredentials();
               // Log.i("statussss", "sentttt");
            }
        });
    }

    private void checkCredentials() {

        LoginCredentials loginCredentials = new LoginCredentials(loginEdit.getText().toString(), passEdit.getText().toString());
        Call<ResponseBody> call = loginRestApi.checkLoginCredentials(loginCredentials);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.body().string().equals("success")) {
//                        Intent intentService = new Intent(getApplicationContext(), TransactionService.class);
//                        startService(intentService);
                        transactionViewModel.saveToSharedPref(loginCredentials.getLogin());
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(getApplicationContext(), "Wrong login or password", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
