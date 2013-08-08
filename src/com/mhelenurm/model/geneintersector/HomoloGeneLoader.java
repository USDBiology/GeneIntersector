package com.mhelenurm.model.geneintersector;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 * This utility class contains specific static resources for other parts of the programs.
 *
 * @author Mark Helenurm
 * @version 1.0
 */
public class HomoloGeneLoader {

	/**
	 *
	 */
	public static int lastIntersectionPercent;
	private static final Logger LOG = Logger.getLogger(HomoloGeneLoader.class.getName());

	/**
	 *
	 * @return @throws Exception
	 */
    public static GeneTree load() throws Exception {
        JFrame frame = new JFrame("progress");
        JLabel label = new JLabel("loading homologene file...");
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
        b.setIndeterminate(true);

		//java.net.URL fileloc = HomoloGeneLoader.class.getClassLoader().getResource("classpath:/geneintersector/homout.data");
        URL url = null;
        try {
			URL baseUrl = HomoloGeneLoader.class.getResource(".");
            if (baseUrl != null) {
                url = new URL(baseUrl, "homout.data");
            } else {
				url = HomoloGeneLoader.class.getResource("homout.data");
            }
        } catch (MalformedURLException e) {
            // Do something appropriate
        }



		Scanner s = new Scanner(HomoloGeneLoader.class.getResourceAsStream("homout.data"));
        int lines = 0;
        while (s.hasNextLine()) {
            s.nextLine();
            lines++;
        }
        b.setMaximum(lines);
        b.setValue(0);
        b.setIndeterminate(false);


        GeneTree tree = new GeneTree();
		s = new Scanner(HomoloGeneLoader.class.getResourceAsStream("homout.data"));
        while (s.hasNextLine()) {
            try {
                Scanner nextscan = new Scanner(s.nextLine());

                int id = nextscan.nextInt();
                String name = nextscan.next();

                tree.add(name, id);
                b.setValue(b.getValue() + 1);
            } catch (Exception e) {
            }
        }
        frame.setVisible(false);
        frame.dispose();
        return tree;
    }

	/**
	 *
	 * @param whatevs
	 * @param tree
	 * @return
	 */
	public static ArrayList<String> getIntersection(ArrayList<Integer>[] whatevs, GeneTree tree) {
		ArrayList<String> ret = new ArrayList<String>(whatevs.length);
		ArrayList<Integer> control = new ArrayList<Integer>(whatevs.length);
		ArrayList<Integer> totalset = new ArrayList<Integer>(whatevs.length);
        //load data into control
        for (int i = 0; i < whatevs[0].size(); i++) {
            control.add(whatevs[0].get(i));
            if (!totalset.contains(whatevs[0].get(i))) {
                totalset.add(whatevs[0].get(i));
            }
        }

        for (int i = 1; i < whatevs.length; i++) {
			ArrayList<Integer> newdata = new ArrayList<Integer>(whatevs.length);
            for (int j = 0; j < whatevs[i].size(); j++) {
                if (control.contains(whatevs[i].get(j)) && !newdata.contains(whatevs[i].get(j))) {
                    newdata.add(whatevs[i].get(j));
                }
                if (!totalset.contains(whatevs[i].get(j))) {
                    totalset.add(whatevs[i].get(j));
                }
            }
            control = newdata;
        }
        int intercount = control.size();
        int totalcount = totalset.size();
        
		//lastIntersectionPercent = 100.0 * (double) intercount / (double) totalcount;
		lastIntersectionPercent = intercount;
                //finds the names for each resulting gene
        for (int i = 0; i < control.size(); i++) {
            ret.add(tree.getName(control.get(i)));
        }
        return ret;
    }

	private HomoloGeneLoader() {
	}
}
