package br.edu.fa7.pomodorofa7.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import br.edu.fa7.pomodorofa7.R;
import br.edu.fa7.pomodorofa7.persistence.Pomodoro;
import br.edu.fa7.pomodorofa7.persistence.PomodoroDao;

public class CadastroActivity extends AppCompatActivity {
    private Button btnSalvar;
    private TextView tvTitulo;
    private TextView tvDescricao;
    private TextView tvNumPomodoros;
    private Pomodoro pomodoro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        btnSalvar = (Button) findViewById(R.id.cadastro_btn_salvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvar();
                finish();
            }
        });

        tvTitulo = (TextView) findViewById(R.id.cadastro_titulo);
        tvDescricao = (TextView) findViewById(R.id.cadastro_descricao);
        tvNumPomodoros = (TextView) findViewById(R.id.cadastro_num_pomodoros);

        pomodoro = (Pomodoro) getIntent().getSerializableExtra("pomodoro");

        if(pomodoro != null) {
            tvTitulo.setText(pomodoro.getTitulo());
            tvDescricao.setText(pomodoro.getDescricao());
            tvNumPomodoros.setText(pomodoro.getNumPomodoros().toString());
        }
    }



    private void salvar() {
        PomodoroDao pomodoroDao = new PomodoroDao(this);
        if(pomodoro == null) {
            pomodoro = new Pomodoro(tvTitulo.getText().toString(),
                                    tvDescricao.getText().toString(),
                                    Integer.parseInt(tvNumPomodoros.getText().toString()));
            pomodoroDao.insert(pomodoro);
        } else {
            pomodoro.setDescricao(tvDescricao.getText().toString());
            pomodoro.setTitulo(tvTitulo.getText().toString());
            pomodoro.setNumPomodoros(Integer.parseInt(tvNumPomodoros.getText().toString()));
            pomodoroDao.update(pomodoro);
        }


    }

}
