/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bluej.messageconsole;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * A class for display Env3D messages in a console type style
 * @author jmadar
 */
public class EnvConsole extends JDialog {
    
    private MessageConsole mc;
    
    private Thread t;
    
    public void setThread(Thread t) {
        this.t = t;
    }
    
    public EnvConsole(Frame frame, String string) {
        super(frame, string);

        addWindowListener(new WindowListener() {

            public void windowOpened(WindowEvent we) {                
            }

            public void windowClosing(WindowEvent we) {                
            }

            public void windowClosed(WindowEvent we) {
            }

            public void windowIconified(WindowEvent we) {
            }

            public void windowDeiconified(WindowEvent we) {
            }

            public void windowActivated(WindowEvent we) {
            }

            public void windowDeactivated(WindowEvent we) {
            }
        });
        
        setLocationByPlatform(true);

        JTextArea console = new JTextArea(30, 40);
        console.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(console);
        add(scrollPane);

        // Redirect System.out to EnvConsole
        mc = new MessageConsole(console);
        //mc.redirectOut(null, System.out);
        //mc.redirectErr(null, System.err);
        mc.redirectErr();
        mc.redirectOut();
        mc.setMessageLines(1000);

        add(new JButton("Dismiss") {
            {
                addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        exit();
                    }
                });
                
                setEnabled(true);
            }
        }, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }
    
    public void exit() {
        if (t != null) t.interrupt();
        reset();
        dispose();
    }
    
    public void reset() {
        mc.restore();
    }
}
