package com.mhelenurm.model.geneintersector;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * This class runs the entry point application view.
 *
 * @author Mark Helenurm
 * @version 1.0
 */
public class GeneIntersector extends JFrame implements DropTargetListener {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(GeneIntersector.class.getName());

	/**
	 * Runs the GeneIntersector Application.
	 *
	 * @param args The arguments to the program. (unused)
	 * @throws Exception The exception thrown.
	 */
	public static void main(String[] args) {

		GeneIntersector gi = new GeneIntersector();
		for (int i = 0; i < args.length; i++) {
			File ff = new File(args[i]);
			gi.stripFile(ff);

		}
		if (args.length == 0) {
			gi.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			gi.setVisible(true);
		}

	}
	private GeneTree _tree;
	private JLabel openCSVLabel;
	private JTextArea outputArea;
	private JButton exportButton;
	private DropTarget dropTarget;
	private File lastFile;
	private String lastOutput;
	private JScrollPane scrollPane;

	/**
	 * Initializes the GeneIntersector program.
	 */
	public GeneIntersector() {
		loadStuff();
		loadGUI();
	}

	/**
	 * Loads the background file.
	 */
	private void loadStuff() {
		try {
			_tree = HomoloGeneLoader.load();
		} catch (Exception e) {
			LOG.log(Level.OFF, "Homologene couldn't be loaded! :(");
			System.exit(1);
		}
	}

	/**
	 * Loads the GUI.
	 */
	private void loadGUI() {
		setTitle("Homologene Compare Utility");
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screen = tk.getScreenSize();
		setSize(410, 491);
		this.setMinimumSize(new Dimension(410, 491));
		setLocation(screen.width / 2 - getWidth() / 2, screen.height / 2 - getHeight() / 2);
		this.getContentPane().setLayout(null);

		openCSVLabel = new JLabel("<html><font size=5 color=black>Drop CSV Here</font>", JLabel.CENTER);
		openCSVLabel.setLocation(5, 5);
		openCSVLabel.setSize(400, 50);
		openCSVLabel.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
		this.getContentPane().add(openCSVLabel);

		outputArea = new JTextArea();
		outputArea.setEditable(false);
		dropTarget = new DropTarget(openCSVLabel, this);

		scrollPane = new JScrollPane(outputArea);
		scrollPane.setLocation(5, 60);
		scrollPane.setSize(400, 350);

		exportButton = new JButton("<html><font size=5>Export Results as CSV</font>");
		exportButton.setLocation(5, 415);
		exportButton.setSize(400, 50);
		exportButton.setEnabled(false);
		this.getContentPane().add(exportButton);

		exportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCSV();
			}
		});

		this.addComponentListener(new ComponentListener() {
			@Override
			public void componentResized(ComponentEvent ce) {
				openCSVLabel.setSize(getWidth() - 10, 50);
				scrollPane.setSize(getWidth() - 10, getHeight() - 60 - 21 - 60);
				scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				exportButton.setLocation(5, getHeight() - 76);
				exportButton.setSize(getWidth() - 10, 50);
			}

			@Override
			public void componentMoved(ComponentEvent ce) {
			}

			@Override
			public void componentShown(ComponentEvent ce) {
			}

			@Override
			public void componentHidden(ComponentEvent ce) {
			}
		});
		this.getContentPane().add(scrollPane);
	}

	private void doFile() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Comma Separated Values", "csv");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File f = chooser.getSelectedFile();
			if (f.getName().endsWith(".csv")) {
				stripFile(f);
			}

		}
	}

	private void saveCSV() {
		File directory = new File(lastFile.getParent() + "/" + lastFile.getName().substring(0, lastFile.getName().length() - 4) + "_intersection.csv");
		try {
			PrintWriter p = new PrintWriter(directory);
			p.println(lastOutput);
			p.close();
		} catch (FileNotFoundException ex) {
			LOG.log(Level.OFF, "Can't write file! :(");
		}
	}

	private void stripFile(File csv) {
		try {
			ArrayList<String> names = new ArrayList<String>(10);
			Scanner s = new Scanner(csv);

			String header = s.nextLine();
			String word = "";
			for (int i = 0; i < header.length(); i++) {
				char c = header.charAt(i);
				if (c == ',') {
					names.add(word);
					word = "";
				} else {
					word += c;
				}
				if (i == header.length() - 1) {
					names.add(word);
				}
			}

			if (names.size() == 1) {
				throw new Exception();
			}

			ArrayList<Integer> array[] = new ArrayList[names.size()];
			for (int i = 0; i < array.length; i++) {
				array[i] = new ArrayList<Integer>(10);
			}

			while (s.hasNextLine()) {
				String next = s.nextLine();
				int commas = 0;
				for (int i = 0; i < next.length(); i++) {
					if (next.charAt(i) == ',') {
						commas++;
					}
				}
				if (commas >= array.length) {
					continue;
				}
				String nextword = "";
				int col = 0;
				for (int i = 0; i < next.length(); i++) {
					char c = next.charAt(i);
					if (c == ',') {
						if (!nextword.isEmpty()) {
							array[col].add(_tree.geneGroup(nextword));
						}
						col++;
						nextword = "";
					} else {
						nextword += c;
					}
					if (i == next.length() - 1) {
						if (!nextword.isEmpty()) {
							array[col].add(_tree.geneGroup(nextword));
						}
						col++;
					}
					if (col == array.length) {
						break;
					}
				}
			}
			lastFile = csv;
			exportButton.setEnabled(true);
			lastOutput = "Intersection Name,Percent of Intersection,,Intersecting Genes...\n";
			outputArea.setText("");
			ArrayList<String> inter = HomoloGeneLoader.getIntersection(array, _tree);
			outputArea.append("All (" + String.format("%d", HomoloGeneLoader.lastIntersectionPercent) + "): " + inter + "\n");
			lastOutput += "All," + String.format("%d", HomoloGeneLoader.lastIntersectionPercent) + ",";
			for (int i = 0; i < inter.size(); i++) {
				lastOutput += "," + inter.get(i);
			}


			if (names.size() == 2) {
				return;
			}
			for (int i = 0; i < array.length; i++) {
				for (int j = i + 1; j < array.length; j++) {
					inter = HomoloGeneLoader.getIntersection(new ArrayList[]{array[i], array[j]}, _tree);
					outputArea.append("\n" + names.get(i) + " vs. " + names.get(j) + " (" + String.format("%d out of %d and %d", HomoloGeneLoader.lastIntersectionPercent, array[i].size(), array[j].size()) + "): ");
					lastOutput += "\n" + names.get(i) + " vs. " + names.get(j) + "," + String.format("%d out of %d and %d", HomoloGeneLoader.lastIntersectionPercent, array[i].size(), array[j].size()) + ",";
					for (int k = 0; k < inter.size(); k++) {
						lastOutput += "," + inter.get(k);
					}
					outputArea.append("" + inter);
				}
			}
		} catch (Exception e) {
			openCSVLabel.setText("<html><font size=5 color=red>Error With Input Formatting</font>");
		}
	}

	/**
	 *
	 * @param dtde
	 */
	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
	}

	/**
	 *
	 * @param dtde
	 */
	@Override
	public void dragOver(DropTargetDragEvent dtde) {
	}

	/**
	 *
	 * @param dtde
	 */
	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
	}

	/**
	 *
	 * @param dte
	 */
	@Override
	public void dragExit(DropTargetEvent dte) {
	}

	/**
	 *
	 * @param dtde
	 */
	@Override
	public void drop(DropTargetDropEvent dtde) {
		try {
			// Ok, get the dropped object and try to figure out what it is
			Transferable tr = dtde.getTransferable();
			DataFlavor[] flavors = tr.getTransferDataFlavors();
			for (int i = 0; i < flavors.length; i++) {
				if (flavors[i].isFlavorJavaFileListType()) {
					dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
					java.util.List list = (java.util.List) tr.getTransferData(flavors[i]);
					File f = (File) list.get(0);
					if (f.getName().endsWith(".csv")) {
						stripFile(f);
						openCSVLabel.setText("<html><font size=5 color=black>Drop CSV Here</font>");
					} else {
						openCSVLabel.setText("<html><font size=5 color=red>Only CSV Format Accepted</font>");
					}

					dtde.dropComplete(true);
					return;
				}
			}
			openCSVLabel.setText("<html><font size=5 color=red>Only CSV Files Accepted</font>");
			dtde.rejectDrop();
		} catch (Exception e) {
			dtde.rejectDrop();
		}
	}
}