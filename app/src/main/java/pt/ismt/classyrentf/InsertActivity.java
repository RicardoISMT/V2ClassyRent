package pt.ismt.classyrentf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;


public class InsertActivity extends AppCompatActivity {

    private ApiConnection _api;
    private EditText etNomeR, etEmailR, etPalavrapasseR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registo);

        etNomeR = findViewById(R.id.etNome);
        etEmailR = findViewById(R.id.etEmail);
        etPalavrapasseR = findViewById(R.id.etPalavrapasse);
    }

        public void inserirAluno(View v) {

                Log.d("Debug", "Botão de procura de alojamento foi pressionado!");

                if (etNomeR.getText().toString().length() == 0) {
                    etNomeR.setError("É necessário preencher o nome!");

                    etNomeR.requestFocus();
                } else if (etEmailR.getText().toString().length() == 0) {
                    etEmailR.setError("É necessário preencher o email!");

                    etEmailR.requestFocus();
                } else if (etPalavrapasseR.getText().toString().length() == 0) {
                    etPalavrapasseR.setError("É necessário preencher a palavra-passe!");

                    etPalavrapasseR.requestFocus();
                } else {

                    //dados a enviar para a API (formato BODY)
                    String dados = "nome=" + etNomeR.getText() + "&email=" + etEmailR.getText() + "&palavrapasse=" + etPalavrapasseR.getText();

                    //executa o pedido à API
                    _api = new ApiConnection();
                    _api._activity = InsertActivity.this;
                    _api._listaAlunos = new ArrayList();

                    //Teoricamente passa os 'dados' para uma string que vai para a ApiConnection
                    Log.d("Data", _api._listaAlunos.toString());

                    _api.execute("http://10.0.2.2:3001/registo", "1", dados);


                }
            }


    public void successMessage() {
        Toast.makeText(this, "Preenchimento realizado com sucesso!", Toast.LENGTH_LONG).show();
        etNomeR.setText("");
        etEmailR.setText("");
        etPalavrapasseR.setText("");

    }

    public void voltarInicio(View v) {
        Intent voltar = new Intent(this, MainActivity.class);
        startActivity(voltar);
    }

}





