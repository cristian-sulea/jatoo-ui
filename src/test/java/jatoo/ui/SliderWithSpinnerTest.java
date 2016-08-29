package jatoo.ui;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SliderWithSpinnerTest {

  public static void main(String[] args) throws Exception {

    JPanel cp = new JPanel(new GridLayout());
    cp.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    cp.add(new SliderWithSpinner());

    final JFrame frame = new JFrame(SliderWithSpinnerTest.class.getSimpleName());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(cp);
    frame.pack();
    frame.setSize(500, frame.getHeight());
    frame.setLocationRelativeTo(null);
    frame.setLocation(600, 500);
    frame.setVisible(true);
  }

}
