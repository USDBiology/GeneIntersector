package com.mhelenurm.entry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class exists solely to parse the Homologene data dumps.
 *
 * @author mhelenurm
 * @version 1.0
 */
public class HomParser {

	private static final Logger LOG = Logger.getLogger(HomParser.class.getName());

	/**
	 * Runs the parser.
	 *
	 * @param args Arguments.
	 * @throws Exception None thrown.
	 */
	public static void main(String[] args) throws Exception {
		HomParser s = new HomParser();
		s.parseFile();
	}

	/**
	 * Parses the actual raw file.
	 *
	 * @throws Exception
	 */
	public void parseFile() {
		try {
			java.net.URL fileloc = HomParser.class.getResource("homologene.data");
			File ff = new File(fileloc.getFile());
			File output = new File(ff.getParentFile() + "/homnum.data");
			Scanner s = new Scanner(ff);
			PrintWriter p = new PrintWriter(output);

			ArrayList<Integer> ids = new ArrayList<Integer>(16);
			ArrayList<String> names = new ArrayList<String>(16);
			while (s.hasNextLine()) {
				Scanner nextscan = new Scanner(s.nextLine());

				int id = nextscan.nextInt();
				nextscan.next();
				nextscan.next();
				String name = nextscan.next();
				name = name.toLowerCase().trim();
				if (!names.contains(name)) {
					ids.add(id);
					names.add(name);
				}
			}
			LOG.log(Level.OFF, "Done.");
			for (int i = 0; i < ids.size(); i++) {
				p.println(ids.get(i) + "\t" + names.get(i));
			}
			p.close();
		} catch (FileNotFoundException e) {
			LOG.log(Level.OFF, e.getMessage());
		}
	}
}