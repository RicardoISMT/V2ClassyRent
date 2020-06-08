package pt.ismt.classyrentf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    ListView _lv;
    ApiConnectionA _api;
    private EditText etAlojamento, etCidade, etUni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        etAlojamento = findViewById(R.id.alojamento_text);
        etCidade = findViewById(R.id.cidade_text);
        etUni = findViewById(R.id.uni_text);
        _lv = findViewById(R.id.aloView);


    }

    public void buscaAlojamentos(View v) {

        Log.d("Debug", "Botão de inserir um utilizador foi pressionado!");

        if (etAlojamento.getText().toString().length() == 0) {
            etAlojamento.setError("É necessário preencher o nome!");

            etAlojamento.requestFocus();
        } else if (etCidade.getText().toString().length() == 0) {
            etCidade.setError("É necessário preencher o email!");

            etCidade.requestFocus();
        } else if (etUni.getText().toString().length() == 0) {
            etUni.setError("É necessário preencher a palavra-passe!");

            etUni.requestFocus();
        } else {

            //dados a enviar para a API (formato BODY)
            String dados = "nome=" + etAlojamento.getText() + "&email=" + etCidade.getText() + "&pass=" + etUni.getText();

        //inicia o pedido à API
        _api = new ApiConnectionA();
        _api._activity = HomeActivity.this;
        _api._listaPlace = new ArrayList();
        _api.execute("http://10.0.2.2:3001/alojamentos","0");
        }
    }

    public void updateUIH() {
        //lista onde irá ficar armazenado a string para mostrar na lista (listView)
        final ArrayList<String> dadosLista = new ArrayList<>();

        //ciclo que percorre todos os alunos retornados pela API
        for (int i=0; i<_api._listaPlace.size(); i++) {
            //variável que guarda os dados do aluno (no formato key-value-pair)
            HashMap<String, String> place = _api._listaPlace.get(i);

            //string com os dados a mostrar na lista no formato - nome(id) e por baixo o email
            String dataALojamento = place.get("alojamento")+place.get("cidade")+place.get("uni")+place.get("preco")+place.get("descricao");
            dadosLista.add(dataALojamento);
        }
        _lv.setAdapter(new ArrayAdapter<String>(this, R.layout.adapter_view_layout, dadosLista));
    }


    public void successMessageH() {
        Toast.makeText(this, "Preenchimento realizado com sucesso!", Toast.LENGTH_LONG).show();

    }

}