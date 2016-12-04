package ru.ifmo.cs.bcomp.ui.io;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import ru.ifmo.cs.bcomp.IOCtrl;
import ru.ifmo.cs.bcomp.IOCtrl.ControlSignal;
import ru.ifmo.cs.bcomp.ui.io.IODevice;
import ru.ifmo.cs.elements.DataDestination;

public class Numpad extends IODevice {
    private final Numpad.NumButton[] buttons = new Numpad.NumButton[16];

    public Numpad(IOCtrl ioctrl) {
        super(ioctrl, "Цифровая клавиатура");
    }

    protected Component getContent() {
        JPanel content = new JPanel(new GridLayout(4, 4, 0, 0));
        content.add(this.buttons[0] = new Numpad.NumButton("7", 7));
        content.add(this.buttons[1] = new Numpad.NumButton("8", 8));
        content.add(this.buttons[2] = new Numpad.NumButton("9", 9));
        content.add(this.buttons[3] = new Numpad.NumButton("/", 12));
        content.add(this.buttons[4] = new Numpad.NumButton("4", 4));
        content.add(this.buttons[5] = new Numpad.NumButton("5", 5));
        content.add(this.buttons[6] = new Numpad.NumButton("6", 6));
        content.add(this.buttons[7] = new Numpad.NumButton("*", 13));
        content.add(this.buttons[8] = new Numpad.NumButton("1", 1));
        content.add(this.buttons[9] = new Numpad.NumButton("2", 2));
        content.add(this.buttons[10] = new Numpad.NumButton("3", 3));
        content.add(this.buttons[11] = new Numpad.NumButton("-", 10));
        content.add(this.buttons[12] = new Numpad.NumButton("0", 0));
        content.add(this.buttons[13] = new Numpad.NumButton(".", 14));
        content.add(this.buttons[14] = new Numpad.NumButton("=", 15));
        content.add(this.buttons[15] = new Numpad.NumButton("+", 11));
        this.ioctrl.addDestination(ControlSignal.SETFLAG, new DataDestination() {
            public void setValue(int value) {
                Numpad.NumButton[] arr$ = Numpad.this.buttons;
                int len$ = arr$.length;

                for(int i$ = 0; i$ < len$; ++i$) {
                    Numpad.NumButton button = arr$[i$];
                    button.setForeground(value == 0?Color.black:Color.red);
                }

            }
        });
        return content;
    }

    private void buttonPressed(int value) {
        try {
            this.ioctrl.setData(value);
            this.ioctrl.setFlag();
        } catch (Exception var3) {
            ;
        }

    }

    private class NumButton extends JButton {
        private final int value;

        public NumButton(String title, final int value) {
            super(title);
            this.value = value;
            this.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Numpad.this.buttonPressed(value);
                }
            });
        }
    }
}