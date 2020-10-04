package com.project.primeiro_projeto.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.orhanobut.hawk.Hawk;
import com.project.primeiro_projeto.R;
import com.project.primeiro_projeto.model.MaskType;
import com.project.primeiro_projeto.model.MaskUtil;
import com.project.primeiro_projeto.model.Usuario;
import com.project.primeiro_projeto.model.UsuarioViewModel;

public class CadastroActivity extends AppCompatActivity {
    private UsuarioViewModel usuarioViewModel;
    private Usuario usuarioCorrente;
    private EditText editTextNome;
    private EditText editTextCPF;
    private EditText editTextEmail;
    private EditText editTextSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        Hawk.init(this).build();

        editTextNome = findViewById(R.id.editTextNome);
        editTextCPF = findViewById(R.id.editTextCpf);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSenha = findViewById(R.id.editTextSenha);
        editTextCPF.addTextChangedListener(MaskUtil.insert(editTextCPF, MaskType.CPF));
        usuarioCorrente = new Usuario();
        usuarioViewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
        usuarioViewModel.getUsuario().observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(@Nullable final Usuario usuario) {
                updateView(usuario);
            }
        });
    }

    private void updateView(Usuario usuario){
        if(usuario != null && usuario.getId() > 0){
            usuarioCorrente = usuario;
            editTextNome.setText(usuario.getNome());
            editTextCPF.setText(usuario.getCpf());
            editTextEmail.setText(usuario.getEmail());
            editTextSenha.setText(usuario.getSenha());
        }
    }

    public void salvar(View view) {
        Hawk.put("tem_cadastro", true);
        if(usuarioCorrente == null){
            usuarioCorrente = new Usuario();
        }
        usuarioCorrente.setNome(editTextNome.getText().toString());
        usuarioCorrente.setCpf(editTextCPF.getText().toString());
        usuarioCorrente.setEmail(editTextEmail.getText().toString());
        usuarioCorrente.setSenha(editTextSenha.getText().toString());
        usuarioViewModel.insert(usuarioCorrente);
        Toast.makeText(this,"Usuário salvo com sucesso",
                Toast.LENGTH_SHORT).show();
        Hawk.put("tem_cadastro",true);
        finish();
    }
}