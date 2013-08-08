package com.mhelenurm.model.geneintersector;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * This class handles the logic to the program.
 *
 * @author Mark Helenurm
 * @version 1.0
 */
public class GeneTree {

	private static final Logger LOG = Logger.getLogger(GeneTree.class.getName());
	private static int lastgroup;

	/**
	 * Gets a UID for the next available inferred gene group.
	 *
	 * @return The next available UID.
	 */
	public static int getUID() {
		lastgroup++;
		return lastgroup;
	}

	/**
	 * Sets the UID for the next available inferred gene group.
	 *
	 * @param uid The new UID (the highest number) available to use.
	 */
	public static void setUID(int uid) {
		lastgroup = uid;
	}
	private int _depth;
	private int _nodecount;
	private GeneNode _root;
	private ArrayList<Integer> _uids;
	private ArrayList<String> _names;

	/**
	 * Initializes GeneTree.
	 */
	public GeneTree() {
		_depth = -1;
		_nodecount = 0;
		lastgroup = 0;
		_root = null;

		_uids = new ArrayList<Integer>(100);
		_names = new ArrayList<String>(100);
	}

	/**
	 * Adds a gene to the tree.
	 *
	 * @param genename Name of the gene.
	 * @param group The gene group for the gene.
	 * @return Whether the gene was added.
	 */
	public boolean add(String genename, int group) {
		return add(genename, hashString(genename), group);
	}

	/**
	 * Adds a gene to the tree.
	 *
	 * @param genename Name of the gene.
	 * @return Whether the gene was added.
	 */
	public boolean add(String genename) {
		return add(genename, hashString(genename), getUID());
	}

	private boolean add(String name, int hash, int group) {
		if (_root == null) {
			_depth = 1;
			_nodecount++;
			_root = new GeneNode(hash, group, 1);
			_uids.add(group);
			_names.add(name);
		} else {
			int compdepth = _root.addGene(hash, group);
			if (compdepth > _depth) {
				_depth = compdepth;
			}
			if (compdepth != -1) {
				_nodecount++;

				_uids.add(group);
				_names.add(name);
			} else {
				return false;
			}
		}
		if (group > lastgroup) {
			lastgroup = group;
		}
		return true;
	}

	/**
	 * Gets
	 *
	 * @param group
	 * @return
	 */
	public String getName(int group) {
		int nameind = _uids.indexOf(group);
		return _names.get(nameind);
	}

	private int hashString(String s) {
		return s.toLowerCase().trim().hashCode();
	}

	public int geneGroup(String genename) {
		//if it's in there, don't add it
		//if it's not in there, add it and return the new group

		if (_root == null) {
			return -1;
		} else {
			int group = _root.search(hashString(genename));
			if (group == -1) {
				add(genename);
				return lastgroup;
			} else {
				return group;
			}
		}
	}
}