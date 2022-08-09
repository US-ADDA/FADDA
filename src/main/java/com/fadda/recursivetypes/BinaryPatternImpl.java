package com.fadda.recursivetypes;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.fadda.common.extension.Map2;
import com.fadda.common.Preconditions;


public class BinaryPatternImpl<E> implements BinaryPattern<E> {

	public static <E> BinaryPattern<E> binary(E label, BinaryPattern<E> left, BinaryPattern<E> right) {
		Preconditions.checkNotNull(label);
		return new BinaryPatternImpl<E>(label, left, right);
	}

	public static <E> BinaryPattern<E> binary_variable(String variable_name, BinaryPattern<E> left, BinaryPattern<E> right) {
		return new BinaryPatternImpl<E>(variable_name, left, right);
	}

	public static <E> BinaryPattern<E> leaf(E label) {
		Preconditions.checkNotNull(label);
		return new BinaryPatternImpl<E>(label);
	}

	public static <E> BinaryPattern<E> empty() {
		return new BinaryPatternImpl<E>();
	}

	public static <E> BinaryPattern<E> variable(String name) {
		return new BinaryPatternImpl<E>(name);
	}

	public static <E> BinaryPattern<E> parse(String s, Function<String,E> f) {
		Preconditions.checkNotNull(s);
		BinaryTree<String> tree = BinaryTreeImpl.parse(s);
		BinaryPattern<E> r = BinaryPatternImpl.toPattern(tree, f);
		return r;
	}

	private E label;
	private String variable_name;
	private BinaryPattern<E> left;
	private BinaryPattern<E> right;
	private PatternType type;


	public BinaryPatternImpl() {
		super();
		this.label = null;
		this.variable_name = null;
		this.left = null;
		this.right = null;
		this.type = PatternType.Empty;
	}

	public BinaryPatternImpl(E label) {
		super();
		this.label = label;
		this.variable_name = null;
		this.left = null;
		this.right = null;
		this.type = PatternType.Leaf;
	}

	public BinaryPatternImpl(String name) {
		super();
		this.label = null;
		this.variable_name = name;
		this.left = null;
		this.right = null;
		this.type = PatternType.Variable;
	}

	public BinaryPatternImpl(String variable_name, BinaryPattern<E> left, BinaryPattern<E> right) {
		super();
		this.label = null;
		this.variable_name = variable_name;
		this.left = left;
		this.right = right;
		this.type = PatternType.Binary_Variable;
	}

	public BinaryPatternImpl(E label, BinaryPattern<E> left, BinaryPattern<E> right) {
		super();
		this.label = label;
		this.variable_name = null;
		this.left = left;
		this.right = right;
		this.type = PatternType.Binary;
	}

	@Override
	public boolean isEmpty() {
		return type.equals(PatternType.Empty);
	}


	@Override
	public boolean isLeaf() {
		return type.equals(PatternType.Leaf);
	}

	@Override
	public boolean isBinary() {
		return type.equals(PatternType.Binary);
	}

	@Override
	public boolean isBinary_Variable() {
		return type.equals(PatternType.Binary_Variable);
	}

	@Override
	public boolean isVariable() {
		return type.equals(PatternType.Variable);
	}


	@Override
	public E getLabel() {
		Preconditions.checkArgument(this.isBinary() || this.isLeaf(), "No permitido");
		return label;
	}


	@Override
	public BinaryPattern<E> getLeft() {
		Preconditions.checkArgument(this.isBinary() || this.isBinary_Variable(), "No permitido");
		return left;
	}


	@Override
	public BinaryPattern<E> getRight() {
		Preconditions.checkArgument(this.isBinary() || this.isBinary_Variable(), "No permitido");
		return right;
	}


	@Override
	public PatternType getType() {
		return type;
	}

	@Override
	public String getVariable_Name() {
		Preconditions.checkArgument(this.isBinary_Variable() || this.isVariable(), "No permitido");
		return variable_name;
	}


	@Override
	public String toString(){
		String r = null;;
		switch(this.getType()) {
		case Empty: r ="_"; break;
		case Leaf: r = getLabel().toString(); break;
		case Binary: r = getLabel().toString() +
				"(" + this.getLeft().toString() + "," + this.getRight().toString() + ")";
			break;
		case Binary_Variable:  r = this.getVariable_Name() +
				"(" + this.getLeft().toString() + "," + this.getRight().toString() + ")";
			break;
		case Variable: r = getVariable_Name(); break;
		}
		return r;
	}

	public static <E> Matches<E> match(BinaryTree<E> tree, BinaryPattern<E> pt) {
		Matches<E> r = Matches.of();
		switch(pt.getType()) {
		case Empty: break;
		case Leaf:  r.match = tree.isLeaf() && pt.getLabel().equals(tree.getLabel()); break;
		case Variable: r.treeMatches = Map2.of(pt.getVariable_Name(), tree); break;
		case Binary:
			r.match = tree.isBinary() && pt.getLabel().equals(tree.getLabel());
			if(r.match) r = match(tree.getLeft(), pt.getLeft()); else break;
			if(r.match)  r.add(match(tree.getRight(), pt.getRight()));
			break;
		case Binary_Variable:
			r.match = tree.isBinary();
			if(r.match) r = match(tree.getLeft(), pt.getLeft()); else break;
			Matches<E> ml = Matches.ofLabels(Map2.of(pt.getVariable_Name(), tree.getLabel()));
			r.add(ml);
			if(r.match) r.add(match(tree.getRight(), pt.getRight()));
			break;
		}
		return r;
	}


	public static <R> BinaryPattern<R> toPattern(BinaryTree<String> tree, Function<String,R> f){
		BinaryPattern<R> r = null;
		switch(tree.getType()) {
		case Empty: r = BinaryPattern.empty(); break;
		case Leaf:
			String label = tree.getLabel();
			if (label.charAt(0) != '_') {
				r = BinaryPattern.leaf(f.apply(label));
			} else {
				r = BinaryPattern.variable(label);
			}
			break;
		case Binary:
			label = tree.getLabel();
			if (label.charAt(0) != '_') {
				r = BinaryPattern.binary(f.apply(label), toPattern(tree.getLeft(),f), toPattern(tree.getRight(),f));
			} else {
				r = BinaryPattern.binary_variable(label, toPattern(tree.getLeft(),f), toPattern(tree.getRight(),f));
			}
			break;
		}
		return r;
	}


	@Override
	public <R> BinaryPattern<R> map(Function<E,R> f){
		BinaryPattern<R> r = null;
		switch(this.getType()) {
		case Empty: r = BinaryPattern.empty();; break;
		case Leaf: r = BinaryPattern.leaf(f.apply(this.getLabel())); break;
		case Variable: r = BinaryPattern.variable(this.getVariable_Name()); break;
		case Binary:
			r = BinaryPattern.binary(f.apply(this.getLabel()),this.getLeft().map(f), this.getRight().map(f)); break;
		case Binary_Variable:
			r = BinaryPattern.binary_variable(this.getVariable_Name(),this.getLeft().map(f), this.getRight().map(f)); break;
		}
		return r;
	}

	@Override
	public BinaryTree<E> toBinaryTree(Matches<E> matches) {
		BinaryTree<E> r = null;
		switch(this.getType()) {
		case Empty: r = BinaryTree.empty(); break;
		case Leaf: r = BinaryTree.leaf(this.getLabel()); break;
		case Variable: r = matches.treeMatches.get(this.getVariable_Name()); break;
		case Binary: r = BinaryTree.binary(this.getLabel(),this.getLeft().toBinaryTree(matches),this.getRight().toBinaryTree(matches)); break;
		case Binary_Variable: r = BinaryTree.binary(
				matches.labelsMatches.get(this.getVariable_Name()),
				this.getLeft().toBinaryTree(matches),
				this.getRight().toBinaryTree(matches));
				break;
		}
		return r;
	}

	public static <E> BinaryTree<E> transform(BinaryTree<E> tree, BinaryPattern<E> pattern, BinaryPattern<E> result){
		Matches<E> m = tree.match(pattern);
		BinaryTree<E> r = tree;
		if(m.match) r = result.toBinaryTree(m);
		return r;
	}


	public static class Matches<E> {
		public Map<String,E> labelsMatches;
		public Map<String,BinaryTree<E>> treeMatches;
		public Boolean match;
		public static <E> Matches<E> of(Map<String, E> labelsMatches, Map<String, BinaryTree<E>> treeMatches, Boolean match){
			return new Matches<>(labelsMatches, treeMatches, match);
		}
		public static <E> Matches<E> of(){
			return new Matches<E>(new HashMap<String,E>(), new HashMap<String, BinaryTree<E>>(), true);
		}
		public static <E> Matches<E> ofLabels(Map<String, E> labelsMatches){
			return new Matches<E>(labelsMatches, new HashMap<String, BinaryTree<E>>(), true);
		}
		public static <E> Matches<E> ofTrees(Map<String, BinaryTree<E>> treeMatches){
			return new Matches<E>(new HashMap<String,E>(), treeMatches, true);
		}
		public Matches(Map<String, E> labelsMatches, Map<String, BinaryTree<E>> treeMatches, Boolean match) {
			super();
			this.labelsMatches = labelsMatches;
			this.treeMatches = treeMatches;
			this.match = match;
		}
		public void add(Matches<E> rr) {
			this.match = this.match && rr.match;
			this.labelsMatches.putAll(rr.labelsMatches);
			this.treeMatches.putAll(rr.treeMatches);
		}
		@Override
		public String toString() {
			return "match=" + match +
					"\nlabelsMatches=" + labelsMatches +
					"\ntreeMatches=" + treeMatches;
		}

	}
}
