package pt.ismt.classyrentf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ListView _lv;
    ApiConnection _api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        _lv = findViewById(R.id.aloView);

    }

    public void viewInserirAluno(View v)  {
        Intent novoAluno = new Intent(this, InsertActivity.class);
        startActivity(novoAluno);
    }

    public void pedidoApiDeTodosAlunos(View v) {
        //inicia o pedido à API
        _api = new ApiConnection();
        _api._activity = MainActivity.this;
        _api._listaAlunos = new ArrayList();
        _api.execute("http://10.0.2.2:3001/registo","0");
    }

    public void updateUI() {
        //lista onde irá ficar armazenado a string para mostrar na lista (listView)
        final ArrayList<String> dadosLista = new ArrayList<>();

        //ciclo que percorre todos os alunos retornados pela API
        for (int i=0; i<_api._listaAlunos.size(); i++) {
            //variável que guarda os dados do aluno (no formato key-value-pair)
            HashMap<String, String> user = _api._listaAlunos.get(i);

            //string com os dados a mostrar na lista no formato - nome(id) e por baixo o email
            String nome_e_id = user.get("person")+" ("+user.get("id")+") \n"+user.get("email");
            dadosLista.add(nome_e_id);
        }
        _lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, dadosLista));
    }
}






