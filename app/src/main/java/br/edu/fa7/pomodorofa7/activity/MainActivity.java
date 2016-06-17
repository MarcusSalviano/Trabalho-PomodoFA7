package br.edu.fa7.pomodorofa7.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import br.edu.fa7.pomodorofa7.R;
import br.edu.fa7.pomodorofa7.persistence.Pomodoro;
import br.edu.fa7.pomodorofa7.persistence.PomodoroDao;
import br.edu.fa7.pomodorofa7.service.ListenValue;
import br.edu.fa7.pomodorofa7.service.PomodoroService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ListenValue {

    private TextView cronometro;
    private RecyclerView recyclerView;
    private Button addTarefaButton;

    private boolean isServiceBound;
    private PomodoroService pomodoroService;
    private Intent pomodoroServiceIntent;
    private Handler handler;

    private PomodoroDao pomodoroDao;
    private boolean isCronometroStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pomodoroDao = new PomodoroDao(this);

        cronometro = (TextView) findViewById(R.id.main_cronometro);
        cronometro.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                cronometro.setText("00:05");
                return true;
            }
        });

        addTarefaButton = (Button) findViewById(R.id.main_add_tarefa);
        addTarefaButton.setOnClickListener(this);

        pomodoroServiceIntent = new Intent(this, PomodoroService.class);
        startService(pomodoroServiceIntent);

        handler = new Handler();

        isCronometroStarted = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        listarItensPomodoro();
    }

    public void listarItensPomodoro(){
        List<Pomodoro> pomodoros = pomodoroDao.findAll();

        PomodoroAdapter adapter = new PomodoroAdapter(this, pomodoros);
        LinearLayoutManager llm = new LinearLayoutManager(this);

        recyclerView = (RecyclerView) findViewById(R.id.main_recyclerview);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

        Intent it = null;

        switch(v.getId()) {

            case R.id.main_add_tarefa:
                it = new Intent(this, CadastroActivity.class);
                startActivity(it);
                break;
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PomodoroService.LocalBinder binder = (PomodoroService.LocalBinder) service;
            pomodoroService = binder.getService();
            pomodoroService.add(MainActivity.this);
            isServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        bindService(pomodoroServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(isServiceBound) {
            unbindService(serviceConnection);
        }
    }

    @Override
    public void newValue(final String value) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                cronometro.setText(value);
            }
        });
    }

    public void startCronometro(boolean start, Integer idPomodoroRunning) {
        if(isServiceBound && !isCronometroStarted) {
            isCronometroStarted = true;
            pomodoroService.setCronometroValue(cronometro.getText().toString());
            pomodoroService.startCronometro(start, idPomodoroRunning);
        }
    }

    public void stopCronometro() {
        isCronometroStarted = false;
        pomodoroService.stopCronometro();
    }

    public boolean isCronometroStarted() {
        return isCronometroStarted;
    }

}
