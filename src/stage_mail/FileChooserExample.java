package stage_mail;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class FileChooserExample {

	static String path = null;

	public static void main(String[] args) {
		// Création de la fenêtre principale
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Création de l'objet JFileChooser
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(true); // permet la sélection multiple de fichiers

		// Affichage de la boîte de dialogue pour la sélection des fichiers
		int result = fileChooser.showOpenDialog(frame);

		// Traitement des fichiers sélectionnés
		if (result == JFileChooser.APPROVE_OPTION) {
			File[] files = fileChooser.getSelectedFiles(); // récupère les fichiers sélectionnés

			StringBuilder stringBuilder = new StringBuilder();

			for (File file : files) {
				String path = file.getAbsolutePath();
				stringBuilder.append(path);
				stringBuilder.append(";");
			}

			String combinedPaths = stringBuilder.toString();
			String c[] = combinedPaths.split(";");
			int i = 0;
			for (String d : c) {
				i++;
				System.out.println(i);
				System.out.println(d);
			}
		}

	}
}
