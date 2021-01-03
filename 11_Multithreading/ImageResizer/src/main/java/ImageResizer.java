import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;

public class ImageResizer implements Runnable {

  File[] files;
  int newWidth;
  String dstFolder;
  long start;

  public ImageResizer(File[] files, int newWidth, String dstFolder, long start) {
    this.files = files;
    this.newWidth = newWidth;
    this.dstFolder = dstFolder;
    this.start = start;
  }

  @Override
  public void run() {

    try {
      for (File file : files) {

        if (file == null) {
          continue;
        }

        BufferedImage image = ImageIO.read(file);

        int newHeight = (int) Math.round(
            image.getHeight() / (image.getWidth() / (double) newWidth)
        );

        //меняем размер изображения с использованием библиотеки Imgscalr
        BufferedImage newImage = Scalr.resize(image, newWidth, newHeight);

        File newFile = new File(dstFolder + "/" + file.getName());
        ImageIO.write(newImage, "jpg", newFile);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    System.out.println("Duration: " + (System.currentTimeMillis() - start) + " ms");
  }
}
