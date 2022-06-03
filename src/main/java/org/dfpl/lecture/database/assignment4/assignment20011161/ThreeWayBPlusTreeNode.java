package org.dfpl.lecture.database.assignment4.assignment20011161;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

//@SuppressWarnings("unused")
public class ThreeWayBPlusTreeNode {

	// root node의 parent node는 존재X
	private ThreeWayBPlusTreeNode parent;
	private List<Integer> keyList;
	private List<ThreeWayBPlusTreeNode> children;

	// prev와 next는 only leaf node에만 존재한다. LinkedList 구현을 위해 필요
	private ThreeWayBPlusTreeNode prev;
	private ThreeWayBPlusTreeNode next;
	
//	private int m = 3; // 3-way 
	
	public ThreeWayBPlusTreeNode() {
		this.keyList = new ArrayList<>(); // node의 keylist (크기-> 2)
		this.children = new ArrayList<>(); // 자식의 수는 m개
		this.parent =  null;
		this.prev = null;
		this.next = null; 
	}
	
	// get keyList
	public List<Integer> getkeyList(){
		return keyList;
	}
	// get key값
	public int getKey(int index) {
		return keyList.get(index);
	}
	
	public void setKeyList(List<Integer> keyList) {
		Iterator<Integer> iter = keyList.iterator();
		while(iter.hasNext()) {
			this.keyList.add(iter.next());
		}
	}
	// parent
	public ThreeWayBPlusTreeNode getParent() {
		return parent;
	}
	public void setParent(ThreeWayBPlusTreeNode parent) {
		this.parent = parent;
	}
	
	// children
	public List<ThreeWayBPlusTreeNode> getChildren(){
		return children;
	}
	
	public void setChildren(List<ThreeWayBPlusTreeNode> children) {
		this.children = children;
	}
	
	// prev
	public ThreeWayBPlusTreeNode getPrev() {
		return prev;
	}
	public void setPrev(ThreeWayBPlusTreeNode prev) {
		this.prev = prev;
	}
	
	// next 
	public ThreeWayBPlusTreeNode getNext() {
		return next;
	}
	public void setNext(ThreeWayBPlusTreeNode next) {
		this.next = next;
	}

	
}

