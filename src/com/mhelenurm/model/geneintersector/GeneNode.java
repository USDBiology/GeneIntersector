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
package com.mhelenurm.model.geneintersector;

import java.util.logging.Logger;

/**
 *
 * @author Mark Helenurm <mhelenurm@gmail.com>
 * @version 1.0
 * @since Jun 19, 2013
 */
public class GeneNode {

	private static final Logger LOG = Logger.getLogger(GeneNode.class.getName());
	private int _hash;
	private int _genegroup;
	private int _level;
	private GeneNode _left, _right;

	/**
	 * Initializes GeneNode.
	 *
	 * @param hash
	 * @param group
	 * @param level
	 */
	public GeneNode(int hash, int group, int level) {
		_hash = hash;
		_genegroup = (group == -1) ? GeneTree.getUID() : group;
		_left = null;
		_right = null;
		_level = level;
	}

	public int search(int hash) {
		if (hash == _hash) {
			return _genegroup;
		} else if (hash < _hash) {
			if (_left == null) {
				return -1;
			} else {
				return _left.search(hash);
			}
		} else {
			if (_right == null) {
				return -1;
			} else {
				return _right.search(hash);
			}
		}
	}

	public int addGene(int hash, int group) {
		if (hash > _hash) {
			if (_right != null) {
				return _right.addGene(hash, group);
			} else {
				_right = new GeneNode(hash, group, _level + 1);
				return _level + 1;
			}
		} else if (hash < _hash) {
			if (_left != null) {
				return _left.addGene(hash, group);
			} else {
				_left = new GeneNode(hash, group, _level + 1);
				return _level + 1;
			}
		} else {
			return -1;
		}
	}
}
