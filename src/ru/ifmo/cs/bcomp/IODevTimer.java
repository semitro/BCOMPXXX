package ru.ifmo.cs.bcomp;

import ru.ifmo.cs.bcomp.IOCtrl;

public class IODevTimer {
    private final IOCtrl ctrl;
    private Thread timer;
    private volatile boolean running = true;

    public IODevTimer(IOCtrl ctrl) {
        this.ctrl = ctrl;
    }

    public void start(String name) {
        this.timer = new Thread(new Runnable() {
            public void run() {
                int countdown = 0;

                while(IODevTimer.this.running) {
                    try {
                        Thread.sleep(100L);
                    } catch (Exception var4) {
                        ;
                    }

                    int value = IODevTimer.this.ctrl.getData();
                    if(countdown != 0 && countdown <= value) {
                        --countdown;
                        if(countdown != 0) {
                            continue;
                        }

                        IODevTimer.this.ctrl.setFlag();
                    }

                    countdown = value;
                }

            }
        }, name);
        this.timer.start();
    }

    public void done() {
        this.running = false;

        try {
            this.timer.join();
        } catch (Exception var2) {
            System.out.println("Can\'t join thread: " + var2.getMessage());
        }

    }
}