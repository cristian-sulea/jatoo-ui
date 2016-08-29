package jatoo.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class WorkerWithProgressDialogTest {

  public static void main(String[] args) throws Exception {

    JPanel cp = new JPanel();

    final JFrame frame = new JFrame(WorkerWithProgressDialogTest.class.getSimpleName());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(cp);
    frame.setSize(100, 100);
    frame.setLocationRelativeTo(null);
    frame.setLocation(600, 500);
    frame.setVisible(true);

    new WorkerWithProgressDialog(cp, "xxx") {

      @Override
      protected void work() {

        for (int i = 0; i < 100; i++) {

          if (isCancelled()) {
            break;
          }

          try {
            Thread.sleep(100);
          } catch (Exception e) {}

          setProgress(100 * (i + 1) / 100);
          setMessage(i + " xxxxx");
        }
      }

      @Override
      protected void done() {
        System.out.println("done");
      }
    }.start();
  }

}
