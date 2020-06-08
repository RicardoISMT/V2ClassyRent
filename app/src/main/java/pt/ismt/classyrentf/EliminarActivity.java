package pt.ismt.classyrentf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class EliminarActivity extends AppCompatActivity {

    private ApiConnection _api;
    private EditText etEmailR, etPalavrapasseR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eliminar);

        etEmailR = findViewById(R.id.etEmail);
        etPalavrapasseR = findViewById(R.id.etPalavrapasse);
    }

    public void eliminarUtilizador(View v) {

        Log.d("Debug", "Botão de remover um utilizador foi pressionado!");

        if (etEmailR.getText().toString().length() == 0) {
            etEmailR.setError("É necessário preencher o email!");

            etEmailR.requestFocus();
        } else if (etPalavrapasseR.getText().toString().length() == 0) {
            etPalavrapasseR.setError("É necessário preencher a palavra-passe!");

            etPalavrapasseR.requestFocus();
        } else {

            //dados a enviar para a API (formato BODY)
            String dados = "email=" + etEmailR.getText() + "&pass=" + etPalavrapasseR.getText();

            //executa o pedido à API
            _api = new ApiConnection();
            _api._activity = EliminarActivity.this;
            _api._listaUser = new ArrayList();

            //Teoricamente passa os 'dados' para uma string que vai para a ApiConnection
            Log.d("Data", _api._listaUser.toString());

            _api.execute("http://10.0.2.2:3001/eliminar/", "1", dados);


            Toast.makeText(this, "Utilizador removido com sucesso!", Toast.LENGTH_LONG).show();
            etEmailR.setText("");
            etPalavrapasseR.setText("");
            Intent eliminadoUtilizador = new Intent(this, InsertActivity.class);
            startActivity(eliminadoUtilizador);

        }
    }
}

