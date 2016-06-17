package br.edu.fa7.pomodorofa7.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import java.util.concurrent.TimeUnit;

import br.edu.fa7.pomodorofa7.persistence.Pomodoro;
import br.edu.fa7.pomodorofa7.persistence.PomodoroDao;

/**
 * Created by MF on 12/06/16.
 */
public class PomodoroService  extends Service implements ServiceNotifier {

    private static final int PENDING_INTENT_ID = 1;

    ListenValue obj;

    private IBinder binder;
    private boolean stop;
    private String cronometroValue;

    public PomodoroService() {
        this.stop = false;
        this.binder = new LocalBinder();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void startCronometro(boolean start, final int idPomodoroRunning) {
        if (start) {
            setCronometroValue("25:00");
            notifyValue("25:00");
            stop = false;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String cronometroValueArray[] = cronometroValue.split(":");
                int minutos = Integer.parseInt(cronometroValueArray[0]);
                int segundos = Integer.parseInt(cronometroValueArray[1]);
                long elapsedTime = 0;
                String returnValue = "";
                boolean firstLoop = true;

                while (!stop) {
                    try {
                        elapsedTime = System.nanoTime();
                        if (segundos == 0) {
                            segundos = 59;
                            if (firstLoop)
                                --minutos;
                        } else {
                            --segundos;
                            if (segundos == 0 && minutos != 0)
                                --minutos;
                        }

                        returnValue = lPad(minutos) + ":" + lPad(segundos);
                        elapsedTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - elapsedTime);
                        Thread.sleep(1000 - elapsedTime);

                        if (!stop)
                            notifyValue(returnValue);

                        if (minutos == 0 && segundos == 0) {
                            finalizaPomodoro(idPomodoroRunning);
                        }

                        firstLoop = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                stop = false;
                stopSelf();
            }
        }).start();
    }

    public void stopCronometro() {
        this.stop = true;
    }

    public void setCronometroValue(String value) {
        cronometroValue = value;
    }

    @Override
    public void add(ListenValue obj) {
        this.obj = obj;
    }

    @Override
    public void notifyValue(String value) {
        obj.newValue(value);
    }

    public class LocalBinder extends Binder {
        public PomodoroService getService() {
            return PomodoroService.this;
        }
    }

    private String lPad(int value) {
        String stringValue = String.valueOf(value);
        if (value < 10) {
            stringValue =  "0" + stringValue;
        }
        return stringValue;
    }

    private void playAlarm() {
        MediaPlayer mp = new MediaPlayer();

        try {
            AssetFileDescriptor descriptor = getAssets().openFd("audio/old-clock-ringing-short.mp3");
            mp.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getDeclaredLength());
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNotification() {
        Intent it = new Intent("POMODORO_ALARM");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(PomodoroService.this, PENDING_INTENT_ID, it, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
    }

    private void decreaseNumPomodoros(int idPomodoroRunning) {
        PomodoroDao pomodoroDao = new PomodoroDao(this);
        Pomodoro pomodoro = pomodoroDao.find(idPomodoroRunning);
        int numPomodoros = pomodoro.getId();

        if(numPomodoros > 1 ) {
            pomodoro.setNumPomodoros(numPomodoros - 1);
            pomodoroDao.update(pomodoro);
        } else {
            pomodoroDao.delete(pomodoro);
        }
    }


    private void finalizaPomodoro(int idPomodoroRunning) {
        stopCronometro();
        playAlarm();
        showNotification();
        decreaseNumPomodoros(idPomodoroRunning);
    }


}
