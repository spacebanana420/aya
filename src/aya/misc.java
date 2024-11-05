package aya;

public class misc {
  public static void sleep(int delay) {
    try {
      Thread.sleep(delay);
    }
    catch (InterruptedException e) {e.printStackTrace();}
  }
}
