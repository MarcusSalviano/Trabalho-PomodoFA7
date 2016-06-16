package br.edu.fa7.pomodorofa7.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.edu.fa7.pomodorofa7.R;
import br.edu.fa7.pomodorofa7.persistence.Pomodoro;
import br.edu.fa7.pomodorofa7.persistence.PomodoroDao;

/**
 * Created by MF on 10/06/16.
 */
public class PomodoroAdapter extends RecyclerView.Adapter<PomodoroAdapter.PomodoroViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Pomodoro> pomodoros;
    private Context context;
    private int lastIdStarted;

    public PomodoroAdapter(Context context, List<Pomodoro> pomodoros) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.pomodoros = pomodoros;
        this.context = context;
    }

    @Override
    public PomodoroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_pomodoro_layout, parent, false);
        PomodoroViewHolder pomodoroViewHolder = new PomodoroViewHolder(view);

        return pomodoroViewHolder;
    }

    @Override
    public void onBindViewHolder(PomodoroViewHolder holder, int position) {
        Pomodoro pomodoro = pomodoros.get(position);
        holder.image.setImageResource(R.drawable.icon_pomodoro);
        holder.titulo.setText(pomodoro.getTitulo());
        holder.descricao.setText(pomodoro.getDescricao());
        holder.numPomodoros.setText("Num. Pomodoros: " + pomodoro.getNumPomodoros().toString());
        holder.pomodoro = pomodoro;
    }

    @Override
    public int getItemCount() {
        return pomodoros.size();
    }

    public class PomodoroViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView image;
        TextView titulo;
        TextView descricao;
        TextView numPomodoros;
        Button btnIniciar;
        Button btnConcluido;
        boolean isStarted = false;
        Pomodoro pomodoro;

        public PomodoroViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.item_cardview);
            image = (ImageView) itemView.findViewById(R.id.imageView);
            titulo = (TextView) itemView.findViewById(R.id.item_titulo);
            descricao = (TextView) itemView.findViewById(R.id.item_descricao);
            numPomodoros = (TextView) itemView.findViewById(R.id.item_num_pomodoros);
            btnIniciar = (Button) itemView.findViewById(R.id.item_btn_iniciar);
            btnConcluido = (Button) itemView.findViewById(R.id.item_btn_concluido);

            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                        alertDialogBuilder.setMessage("O que você deseja fazer?");

                        alertDialogBuilder.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent it = new Intent(context, CadastroActivity.class);
                                it.putExtra("pomodoro", pomodoro);
                                context.startActivity(it);
                            }
                        });

                        alertDialogBuilder.setNegativeButton("Excluir",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PomodoroDao pomodoroDao = new PomodoroDao(context);
                                pomodoroDao.delete(pomodoro);
                                ((MainActivity) context).listarItensPomodoro();
                            }
                        });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    return true;
                }
            });

            btnIniciar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isStarted && !((MainActivity) v.getContext()).isCronometroStarted()) {
                        boolean start = false;
                        isStarted = true;
                        if(lastIdStarted != pomodoro.getId()){
                            lastIdStarted = pomodoro.getId();
                            start = true;
                        }

                        btnIniciar.setText("Pausar");

                        ((MainActivity) v.getContext()).startCronometro(start);
                    } else if(isStarted && ((MainActivity) v.getContext()).isCronometroStarted()){
                        isStarted = false;
                        btnIniciar.setText("Iniciar");
                        ((MainActivity) v.getContext()).stopCronometro();
                    }
                }
            });

            btnConcluido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }
}
