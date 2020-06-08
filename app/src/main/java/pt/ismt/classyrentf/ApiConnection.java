package pt.ismt.classyrentf;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class ApiConnection extends AsyncTask<String, Void, Void> {

    public Activity _activity;
    private ProgressDialog _pdialog;
    public ArrayList<HashMap<String, String>> _listaUser;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        _pdialog = new ProgressDialog(_activity);
        _pdialog.setMessage("Aguardar os dados...");
        _pdialog.setCancelable(false);
        _pdialog.show();
    }

    @Override
    protected Void doInBackground(String... urls) {
        HttpURLConnection _conexao;
        InputStream _is;
        String _resJson;

        _pdialog.setMessage("Pedido a ser executado...");

        try {
            URL _endpoint = new URL("http://10.0.2.2:3001/registo");
            //URL _endpoint = new URL(urls[0]);

            _conexao = (HttpURLConnection) _endpoint.openConnection();

            if (urls[1] == "0") { //GET
                _conexao.setRequestMethod("GET");
            } else if (urls[1] == "1") { //POST
                String data = urls[2];
                Log.d("http", "Os dados enviados no body do pedido foram: " + data);

                byte[] dados = data.getBytes(StandardCharsets.UTF_8);

                _conexao.setDoOutput(true);
                _conexao.setRequestMethod("POST");
                _conexao.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                _conexao.setRequestProperty("charset", "utf-8");
                _conexao.setRequestProperty("Content-Length", Integer.toString(dados.length));
                try (DataOutputStream wr = new DataOutputStream(_conexao.getOutputStream())) {
                    wr.write(dados);
                }
            }

            _conexao.setReadTimeout(12000);
            _conexao.setConnectTimeout(12000);
            _conexao.connect();

            Log.d("http", "O código HTTP do pedido foi: " + _conexao.getResponseCode());

            int _httpStatus = _conexao.getResponseCode();

            if (_httpStatus != HttpURLConnection.HTTP_BAD_REQUEST && _httpStatus != HttpURLConnection.HTTP_INTERNAL_ERROR && _httpStatus != HttpURLConnection.HTTP_NOT_FOUND) {
                _is = _conexao.getInputStream();
            } else {
                _is = _conexao.getErrorStream();
            }

            _resJson = converteStreamParaString(_is);
            Log.d("http", "A resposta ao pedido HTTP foi: " + _resJson);

            if (_resJson != null) {
                try {
                    JSONArray _resposta = new JSONArray(_resJson);

                    //verifica se a API devolveu um array (JSONArray) ou um objeto (JSONObject)
                    if (_resposta.get(Integer.parseInt("dados")) instanceof JSONArray) {
                        JSONArray _listUtilizadorJson = _resposta.getJSONArray(Integer.parseInt("dados"));

                        for (int i = 0; i < _listUtilizadorJson.length(); i++) {
                            JSONObject _user = _listUtilizadorJson.getJSONObject(i);
                            String id = _user.getString("id");
                            String person = _user.getString("person");
                            String email = _user.getString("email");
                            String pass = _user.getString("pass");

                            HashMap<String, String> user = new HashMap();
                            user.put("id", String.valueOf(id));
                            user.put("person", String.valueOf(person));
                            user.put("email", String.valueOf(email));
                            user.put("pass", String.valueOf(pass));
                            _listaUser.add(user);
                        }
                    } else if (_resposta.get(Integer.parseInt("dados")) instanceof JSONObject) {
                        JSONObject _user = _resposta.getJSONObject(Integer.parseInt("dados"));
                        String id = _user.getString("id");
                        String person = _user.getString("person");
                        String email = _user.getString("email");
                        String pass = _user.getString("pass");

                        HashMap<String, String> user = new HashMap();
                        user.put("id", String.valueOf(id));
                        user.put("person", String.valueOf(person));
                        user.put("email", String.valueOf(email));
                        user.put("pass", String.valueOf(pass));
                        _listaUser.add(user);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            _is.close();
            _conexao.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    //este método converte de stream para string
    private static String converteStreamParaString(InputStream is) {
        StringBuffer buffer = new StringBuffer();

        try {
            BufferedReader br;
            String linha;

            br = new BufferedReader(new InputStreamReader(is));
            while ((linha = br.readLine()) != null) {
                buffer.append(linha);
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }

    @Override
    protected void onPostExecute(Void _resultado) {
        super.onPostExecute(_resultado);

        if (_pdialog.isShowing()) {
            _pdialog.dismiss();
        }

        //verifica qual das activities invocou a API
        if (_activity.getClass().getSimpleName().equals("InsertActivity")) {
            InsertActivity ia = (InsertActivity) _activity;
            ia.successMessage();
        }
    }

}










                      /*for (int i = 0; i < _registarJson.length(); i++) {
                            JSONObject _place = _registarJson.getJSONObject(i);
                            String id = _place.getString("id");
                            String alojamento = _place.getString("alojamento");
                            String cidade = _place.getString("cidade");
                            String uni = _place.getString("uni");
                            String distancia = _place.getString("distancia");
                            String preco = _place.getString("preco");
                            String mail = _place.getString("mail");
                            String foto = _place.getString("foto");
                            String user_id = _place.getString("user_id");

                            HashMap<String, String> place = new HashMap();
                            place.put("id", String.valueOf(id));
                            place.put("alojamento", String.valueOf(alojamento));
                            place.put("cidade", String.valueOf(cidade));
                            place.put("uni", String.valueOf(uni));
                            place.put("distancia", String.valueOf(distancia));
                            place.put("preco", String.valueOf(preco));
                            place.put("mail", String.valueOf(mail));
                            place.put("foto", String.valueOf(foto));
                            place.put("user_id", String.valueOf(user_id));
                            _registar.add(place);

                        }
                    } else if (_resposta.get("dados") instanceof JSONObject) {
                        JSONObject _place = _resposta.getJSONObject("dados");
                        String id = _place.getString("id");
                        String alojamento = _place.getString("alojamento");
                        String cidade = _place.getString("cidade");
                        String uni = _place.getString("uni");
                        String distancia = _place.getString("distancia");
                        String preco = _place.getString("preco");
                        String mail = _place.getString("mail");
                        String foto = _place.getString("foto");
                        String user_id = _place.getString("user_id");

                        HashMap<String, String> place = new HashMap();
                        place.put("id", String.valueOf(id));
                        place.put("alojamento", String.valueOf(alojamento));
                        place.put("cidade", String.valueOf(cidade));
                        place.put("uni", String.valueOf(uni));
                        place.put("distancia", String.valueOf(distancia));
                        place.put("preco", String.valueOf(preco));
                        place.put("mail", String.valueOf(mail));
                        place.put("foto", String.valueOf(foto));
                        place.put("user_id", String.valueOf(user_id));


*/