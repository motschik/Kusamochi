package kusamochi;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
 
public class DesktopSample1 {
    public DesktopSample1(String url) throws URISyntaxException, IOException {
        Desktop desktop = Desktop.getDesktop();
 
        desktop.browse(new URI(url));
    }
 
    public static void main(String[] args) {
        try {
            new DesktopSample1(args[0]);
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}