/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluej;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.MouseInfo;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 *
 * @author jmadar
 */
public class ProgressBar {

    private JLabel label;
    private JProgressBar progress;
    private JFrame frame;

    public ProgressBar(Component parent, String message) {
        progress = new JProgressBar(0, 100);
        progress.setBorderPainted(true);

        label = new JLabel("                                        ");

        frame = new JFrame();
        frame.setLocationRelativeTo(parent);
        frame.setResizable(false);
        frame.setAlwaysOnTop(true);
        frame.setTitle(message);

        frame.add(progress, BorderLayout.CENTER);
        frame.add(label, BorderLayout.NORTH);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation ( JFrame.DO_NOTHING_ON_CLOSE );
    }



    public void setProgress(int percent, String desc) {
        progress.setValue(percent);
        if (desc != null) {
            label.setText(desc);
        }
        frame.update(frame.getGraphics());
        if (percent >= 100) {
            frame.dispose();
        }
    }

}
