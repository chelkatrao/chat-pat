package uz.chelkatrao.chatpat;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Utils {

    private Utils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * @return generate random string value
     */
    public static String getRandom() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Profile image cropper function
     *
     * @param profileImageURL location of the image that uploads to profile
     */
    public static void cropProfilePhoto(String profileImageURL) {
        File file = new File(profileImageURL);
        try {
            final BufferedImage bi = ImageIO.read(file);

            int width = bi.getWidth();
            int height = bi.getHeight();

            if (width > height)
                width = height;
            else
                height = width;

            Thumbnails
                    .of(profileImageURL)
                    .crop(Positions.CENTER)
                    .size(width, height)
                    .toFile(profileImageURL);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
