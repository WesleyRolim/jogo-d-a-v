package br.com.friendsecretandroid.wrstecnology.jogodav;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.ArrayList;
import java.util.Collections;

import br.com.friendsecretandroid.wrstecnology.model.Participantes;

public class MainActivity extends AppCompatActivity {

    private EditText nome;
    private EditText codArea;
    private EditText ddd;
    private EditText numero;
    private Button add;
    private Button limpar;
    private Button play;
    private Button newJogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nome = (EditText) findViewById(R.id.nomeText);
        codArea = (EditText) findViewById(R.id.codAreaText);
        ddd = (EditText) findViewById(R.id.dddText);
        numero = (EditText) findViewById(R.id.numeroText);
        add = (Button) findViewById(R.id.addButton);
        limpar = (Button) findViewById(R.id.limparButton);
        play = (Button) findViewById(R.id.playbutton);
        newJogo = (Button) findViewById(R.id.playbutton);
        final ArrayList<Participantes > participantes = new ArrayList<Participantes>();

        //Configurando mascaras para campos de texto
        SimpleMaskFormatter simpleMaskCodArea = new SimpleMaskFormatter("+NN");
        MaskTextWatcher maskCodArea = new MaskTextWatcher(codArea, simpleMaskCodArea);
        codArea.addTextChangedListener(maskCodArea);

        SimpleMaskFormatter simpleMaskDDD = new SimpleMaskFormatter("(NN)");
        MaskTextWatcher maskDDD = new MaskTextWatcher(ddd, simpleMaskDDD);
        ddd.addTextChangedListener(maskDDD);

        SimpleMaskFormatter simpleTelefone = new SimpleMaskFormatter("N.NNNN-NNNN");
        MaskTextWatcher maskTelefone = new MaskTextWatcher(numero, simpleTelefone);
        numero.addTextChangedListener(maskTelefone);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String participante = nome.getText().toString();
                String telefoneCompleto =
                        codArea.getText().toString() +
                                ddd.getText().toString() +
                                numero.getText().toString();

                String telefoneSemFormatacao = telefoneCompleto.replace("(", "");
                telefoneSemFormatacao = telefoneSemFormatacao.replace(")", "");
                telefoneSemFormatacao = telefoneSemFormatacao.replace(".", "");
                telefoneSemFormatacao = telefoneSemFormatacao.replace("-", "");

                //Salvando os dados no array;
                Participantes dado = new Participantes(nome.getText().toString(), telefoneSemFormatacao);
                participantes.add(dado);

                limparCampos();

            }
        });


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> numeros = new ArrayList<Integer>();
                numeros = sortear(participantes.size());
                boolean smsEnviado = false;

                for (int i = 0 ; i < numeros.size() ; i++){
                    switch (i){
                        case 0:
                            Log.i("NUMERO", participantes.get(numeros.get(i)).getNome() + " é DETETIVE" +" Telefone: "+ participantes.get(numeros.get(i)).getTelefone());
                            smsEnviado = enviarSMS(participantes.get(numeros.get(i)).getTelefone(),"Você é DETETIVE");
                            break;
                        case 1:
                            Log.i("NUMERO", participantes.get(numeros.get(i)).getNome() + " é ASSASSINO" +" Telefone: "+ participantes.get(numeros.get(i)).getTelefone());
                            smsEnviado = enviarSMS(participantes.get(numeros.get(i)).getTelefone(),"Você é ASSASSINO");
                            break;
                        default:
                            Log.i("NUMERO", participantes.get(numeros.get(i)).getNome() + " é VITIMA" + " Telefone: "+ participantes.get(numeros.get(i)).getTelefone());
                            smsEnviado = enviarSMS(participantes.get(numeros.get(i)).getTelefone(),"Você é VITIMA");
                    }

                }
            }
        });


        limpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limparCampos();
            }
        });

    }

    private boolean enviarSMS(String telefone, String mensagem){
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(telefone, null, mensagem, null, null);

            return true;
        }catch (Exception e ){
            e.printStackTrace();
            return false;
        }

    }

    public void onRequestPermissionsResult (int requestCode, String[] permission, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permission, grantResults);

        for (int resultado : grantResults){
            if(resultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }
    }

    private void alertaValidacaoPermissao (){
        AlertDialog.Builder builder = new AlertDialog.Builder( this);
        builder.setTitle("Permissoes Negadas");
        builder.setMessage("Para utilizar esse aplicativo é necessário aceitar todas as permissões");

        builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private boolean camposVazios(){
        // O campo presente não é obrigatório
        if (nome.getText().toString().isEmpty() || codArea.getText().toString().isEmpty() || ddd.getText().toString().isEmpty()|| numero.getText().toString().isEmpty()){
            Toast.makeText(MainActivity.this, "Entre com todo os campos", Toast.LENGTH_LONG).show();
            return false;
        }else {
            return true;
        }
    }

    public void limparCampos(){
    nome.setText("");
    codArea.setText("");
    ddd.setText("");
    numero.setText("");
    }

    private static ArrayList<Integer> sortear (int tamanhoVetor){
        ArrayList<Integer> numero = new ArrayList<Integer>();
        for (int i = 0 ; i < tamanhoVetor ; i++){
            numero.add(i);
        }
        Collections.shuffle(numero);
        return numero;
    }
}
