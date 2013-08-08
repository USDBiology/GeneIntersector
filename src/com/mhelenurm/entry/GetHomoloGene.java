/*
 * Copyright © 2013 Mark Helenurm
 * 
 * This code is copyrighted by Mark Helenurm.
 * Do not steal this code under the threat of legal
 * prosecution.
 * 
 * If you have suggestions, comments, or requests to
 * borrow code, email me at <mhelenurm@gmail.com>
 */
package com.mhelenurm.entry;
/*
 import java.util.logging.Logger;
//import org.apache.commons.net.ftp.FTPClient;
//import org.apache.commons.net.ftp.FTPFile;
//import org.apache.commons.net.ftp.FTPReply;

/**
 *
 * @author Mark Helenurm <mhelenurm@gmail.com>
 * @version 1.0
 * @since Jun 19, 2013
 */
/*
 public class GetHomoloGene {

	private static final Logger LOG = Logger.getLogger(GetHomoloGene.class.getName());

	/**
	 * Gets and parses the newest Homologene file.
	 *
	 * @param args
	 */
/*
	public static void main(String[] args) {
		FTPClient ftp = new FTPClient();
		try {
			///pub/HomoloGene/
			ftp.connect("ftp.ncbi.nih.gov"); //connect to the URL.
			if (!ftp.login("anonymous", "")) { //logging in as "anonymous" doesn't require a password.
				ftp.logout(); //if you can't login, say so and give up
				LOG.log(Level.SEVERE, "Can't login!");
				System.exit(1);
			}
			int reply = ftp.getReplyCode(); //checks to see the login get reply for the FTP.
			if (!FTPReply.isPositiveCompletion(reply)) { //if it was not a positive completion, give up
				ftp.disconnect(); //disconnect
				LOG.log(Level.OFF, "Can't get good reply!");
				System.exit(1);
			}

			ftp.changeWorkingDirectory("/pub/HomoloGene/current/"); //sets the subdirectory to the current directory of the HomoloGene newest build.
			LOG.log(Level.INFO, "Connection established!"); //new StringBuilder("Current directory is ").append(ftp.printWorkingDirectory()).toString()

			FTPFile f = ftp.mlistFile("homologene.data"); //gets the file from the directory

			OutputStream output; //sets up outputstream to writer tool output
			output = new FileOutputStream("/Users/mhelenurm/Desktop/homout.data");

			InputStream input = ftp.retrieveFileStream("homologene.data"); //sets up the input stream for the scanner

			JFrame frame = new JFrame("progress");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JLabel label = new JLabel("downloading homologene file...");
			JProgressBar b = new JProgressBar();

			frame.setLayout(null);
			b.setSize(200, 20);
			b.setLocation(5, 25);
			label.setSize(200, 20);
			label.setLocation(5, 5);

			frame.setSize(210, 50);
			frame.setUndecorated(true);

			frame.add(b);
			frame.add(label);

			Toolkit tk = Toolkit.getDefaultToolkit();
			Dimension d = tk.getScreenSize();
			frame.setLocation(d.width / 2 - frame.getWidth() / 2, d.height / 2 - frame.getHeight() / 2);
			frame.setVisible(true);
			frame.repaint();

			b.setMaximum((int) f.getSize());
			Scanner instreamscanner = new Scanner(input);

			PrintWriter writer = new PrintWriter(output);

			ArrayList<String> names = new ArrayList<String>(20000);
			while (instreamscanner.hasNextLine()) {
				String line = instreamscanner.nextLine();

				Scanner nextscan = new Scanner(line);

				int id = nextscan.nextInt();
				nextscan.next();
				nextscan.next();
				String name = nextscan.next().toLowerCase().trim();
				if (!names.contains(name)) {
					names.add(name);
					writer.println(new StringBuilder(32).append(id).append("\t").append(name).toString());
				}
				nextscan.close();

				b.setValue(b.getValue() + line.length() + 1);
				label.setText(String.format("%.0f%c", (b.getPercentComplete() * 100.0), '%'));
			}



			if (b.getValue() == f.getSize()) {
				LOG.log(Level.INFO, "Download/convert completed.");
				b.setIndeterminate(true);
			}
			instreamscanner.close();
			writer.flush();
			writer.close();

			frame.setVisible(false);
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Can't connect!");
			LOG.log(Level.INFO, e.getMessage());
			System.exit(1);
		}
	}
}

	 */
