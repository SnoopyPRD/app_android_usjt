package com.project.primeiro_projeto.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.orhanobut.hawk.Hawk;

import com.project.primeiro_projeto.R;
import com.project.primeiro_projeto.R.id;
import com.project.primeiro_projeto.R.layout;
import com.project.primeiro_projeto.model.Usuario;
import com.project.primeiro_projeto.model.UsuarioViewModel;

public class LoginActivity extends AppCompatActivity {

    private Button buttonLogin;
    private TextView textViewNovoCadastro;
    private EditText editTextEmail;
    private EditText editTextSenha;
    private UsuarioViewModel usuarioViewModel;
    private Usuario usuarioCorrente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_login);
        Hawk.init(this).build();
        editTextEmail = findViewById(R.id.editTextUsuario);
        editTextSenha = findViewById(R.id.editTextSenha);
        buttonLogin=findViewById(id.buttonLogin);
        textViewNovoCadastro=findViewById(id.textViewNovoCadastro);
        usuarioViewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
        usuarioViewModel.getUsuario().observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(@Nullable final Usuario usuario) {
                updateUsuario(usuario);
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onResume(){
        super.onResume();
        if(Hawk.contains("tem_cadastro")){
            if(Hawk.get("tem_cadastro")){
                desbloquear();
            }else{
                bloquear();
            }

        }else{
            bloquear();
        }
    }
    private void updateUsuario(Usuario usuario){
        usuarioCorrente = usuario;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void bloquear(){
        textViewNovoCadastro.setVisibility(View.VISIBLE);
        buttonLogin.setEnabled(false);
        buttonLogin.setBackground(getDrawable(R.drawable.custom_button_off));
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void desbloquear(){
        textViewNovoCadastro.setVisibility(View.GONE);
        buttonLogin.setEnabled(true);
        buttonLogin.setBackground(getDrawable(R.drawable.custom_button));
    }

    public void novoCadastro(View view) {
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }

    public void login(View view) {

        if(usuarioCorrente != null){
            if(usuarioCorrente.getEmail().equalsIgnoreCase(editTextEmail.getText().toString())
                    && usuarioCorrente.getSenha().equals(editTextSenha.getText().toString())){
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(this,"Usuário ou senha invalidos!", Toast.LENGTH_SHORT).show();
            }
        }


    }
}