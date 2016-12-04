package ru.ifmo.cs.bcomp.ui.io;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import ru.ifmo.cs.bcomp.IOCtrl;
import ru.ifmo.cs.bcomp.IOCtrl.ControlSignal;
import ru.ifmo.cs.elements.DataDestination;

class FlagIndicator extends JComponent {
    private static final Color LED_OFF = new Color(128, 128, 128);
    private static final Color LED_ON = new Color(0, 160, 0);
    private final Dimension DIMS;
    private final IOCtrl ioctrl;

    protected FlagIndicator(IOCtrl ioctrl, int size) {
        this.ioctrl = ioctrl;
        this.DIMS = new Dimension(size, size);
        this.setMinimumSize(this.DIMS);
        this.setMaximumSize(this.DIMS);
        this.setPreferredSize(this.DIMS);
        this.setSize(this.DIMS);
        this.setToolTipText("Готовность");
        ioctrl.addDestination(ControlSignal.SETFLAG, new DataDestination() {
            public void setValue(int value) {
                FlagIndicator.this.repaint();
            }
        });
    }

    public void paintComponent(Graphics g) {
        g.setColor(this.ioctrl.getFlag() == 1?LED_ON:LED_OFF);
        g.fillOval(this.DIMS.width / 4, this.DIMS.height / 4, this.DIMS.width / 2, this.DIMS.height / 2);
    }
}
