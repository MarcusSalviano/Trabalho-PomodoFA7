package br.edu.fa7.pomodorofa7.service;

import br.edu.fa7.pomodorofa7.service.ListenValue;

/**
 * Created by MF on 12/06/16.
 */
public interface ServiceNotifier {

    void add(ListenValue obj);
    void notifyValue(String value);
}
