import java.io.IOException;

/**
 * @author Simon Larsen (simla13) & Jacob Andersen (jacan13)
 *
 */
public class SokobanTester {

	public static void main(String[] args) {
		try {
			MainFrame m = new MainFrame();
		} catch (IOException e) {
			System.out.println("IO Exception occured");
			e.printStackTrace();
		}
	}

}
