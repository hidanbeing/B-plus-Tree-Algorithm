package org.dfpl.lecture.database.assignment4.assignment20011161;

import java.util.Collections;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.TreeSet;

@SuppressWarnings("unused")
public class ThreeWayBPlusTree implements NavigableSet<Integer> {

	// all leaf node must be at same level
	// leaf nodes formulate a linked list
	private ThreeWayBPlusTreeNode root;
	private LinkedList<ThreeWayBPlusTreeNode> leafList;
	
	private int m = 3; // 3-way 

	// add
	@Override
	public boolean add(Integer e) {
		// B+tree가 비어있는 경우 -> root node가 비어있는 경
		if (this.root == null) {
			// 새로운 노드를 만들고, key를 넣어줌 -> root node로 지
			ThreeWayBPlusTreeNode newNode  = new ThreeWayBPlusTreeNode();
			newNode.getkeyList().add(e); // key값 add
			this.root = newNode;
			this.root.setParent(null); //root node의 부모는 존재X

		}
		// root node에 child가 없는 경우 -1 : root node의 key가 한개만 채워진 경우
		else if (this.root.getChildren().isEmpty()&&this.root.getkeyList().size()<=(this.m-1)) {
			// key 값 추가해줌
			this.root.getkeyList().add(e);
			//sorted 되어야함
			Collections.sort(root.getkeyList());
			// root node의 key가 3개가 되면,
			if (this.root.getkeyList().size()==(this.m)) {
				// split 해주어야 함 -> external node split(leaf 노드 split)
				this.root = splitExternalNode(this.root);
			}

		}
		// 그 외 나머지 경우
		else {
			System.out.println("root"+this.root.getKey(0));
			ThreeWayBPlusTreeNode now,temp;
			now=searchKey(this.root,e); // -> key가 어디에 추가되어야 하는지 확인후 삽
			System.out.println("now 0-> "+now.getkeyList().get(0));
			
			while (true) {
				if (now.getkeyList().size()<3) {
					break;
				}
				else {
					System.out.println("hi-->"+now.getkeyList().size());
					for (int k:now.getkeyList()) {
						System.out.println(k);
					}
					temp = splitInternalNode(now);
					if (now.getParent()!=null) {
						System.out.println("now parent 0-> "+now.getParent().getkeyList().get(0));
						now=now.getParent();
						addChild2(now,temp);
					}
					else {
						addChild2(now,temp);
						now=temp;
						break;
					}
				}
			}
			System.out.println("key"+now.getkeyList().get(0));
			while (now.getParent()!=null) {
				System.out.println("hi1 "+now.getKey(0));
				System.out.println("hi2 "+now.getParent().getKey(0));
				now=now.getParent();
			}
			System.out.println("now root"+this.root.getKey(0));
			System.out.println("root"+now.getKey(0));
			this.root=now;
			
		}
		if (e==4) {
			System.out.println("-----4-----");
			System.out.println(this.root.getkeyList().get(0));
			System.out.println(this.root.getkeyList().get(1));
			System.out.println(this.root.getChildren().get(0).getkeyList().get(0));
			System.out.println(this.root.getChildren().get(1).getkeyList().get(0));
			System.out.println(this.root.getChildren().get(2).getkeyList().get(0));
			System.out.println(this.root.getChildren().get(2).getkeyList().get(1));
		}
		if (e==5) {
			System.out.println("-----5-----");
			System.out.println(this.root.getkeyList().get(0));

			System.out.println(this.root.getChildren().get(0).getkeyList().get(0));
			System.out.println(this.root.getChildren().get(1).getkeyList().get(0));
			
			System.out.println(this.root.getChildren().get(0).getChildren().get(0).getkeyList().get(0));
			System.out.println(this.root.getChildren().get(0).getChildren().get(1).getkeyList().get(0));
			System.out.println(this.root.getChildren().get(1).getChildren().get(0).getkeyList().get(0));
			System.out.println(this.root.getChildren().get(1).getChildren().get(1).getkeyList().get(0));
			System.out.println(this.root.getChildren().get(1).getChildren().get(1).getkeyList().get(1));
		}
		if (e==6) {
			System.out.println("-----6-----");
			System.out.println(this.root.getkeyList().get(0));
			System.out.println(this.root.getChildren().get(0).getkeyList().get(0));
			System.out.println(this.root.getChildren().get(1).getkeyList().get(0));
			System.out.println(this.root.getChildren().get(1).getkeyList().get(1));
			
			System.out.println(this.root.getChildren().get(0).getChildren().get(0).getkeyList().get(0));
			System.out.println(this.root.getChildren().get(0).getChildren().get(1).getkeyList().get(0));
			
			System.out.println(this.root.getChildren().get(1).getChildren().get(0).getkeyList().get(0));
			System.out.println(this.root.getChildren().get(1).getChildren().get(1).getkeyList().get(0));
			System.out.println(this.root.getChildren().get(1).getChildren().get(2).getkeyList().get(0));
			System.out.println(this.root.getChildren().get(1).getChildren().get(2).getkeyList().get(1));
			
		}
		if (e==7) {
			System.out.println("-----7-----");
			System.out.println(this.root.getkeyList().get(0));
			System.out.println(this.root.getkeyList().get(1));
			System.out.println(this.root.getChildren().get(0).getkeyList().get(0));
			System.out.println(this.root.getChildren().get(1).getkeyList().get(0));
			System.out.println(this.root.getChildren().get(2).getkeyList().get(0));
			
			System.out.println(this.root.getChildren().get(0).getChildren().get(0).getkeyList().get(0));
			System.out.println(this.root.getChildren().get(0).getChildren().get(1).getkeyList().get(0));
			
			System.out.println(this.root.getChildren().get(1).getChildren().get(0).getkeyList().get(0));
			System.out.println(this.root.getChildren().get(1).getChildren().get(1).getkeyList().get(0));
			System.out.println(this.root.getChildren().get(2).getChildren().get(0).getkeyList().get(0));
			System.out.println(this.root.getChildren().get(2).getChildren().get(1).getkeyList().get(0));
			System.out.println(this.root.getChildren().get(2).getChildren().get(1).getkeyList().get(1));	
		}
		
		if (e==8) {
			System.out.println("-----8-----");
			System.out.println(this.root.getkeyList().get(0));
//			System.out.println(this.root.getkeyList().get(1));
			System.out.println(this.root.getChildren().get(0).getkeyList().get(0));
			System.out.println(this.root.getChildren().get(1).getkeyList().get(0));
			System.out.println(this.root.getChildren().get(2).getkeyList().get(0));
			System.out.println(this.root.getChildren().get(2).getkeyList().get(1));
			
			System.out.println(this.root.getChildren().get(0).getChildren().get(0).getkeyList().get(0));
			System.out.println(this.root.getChildren().get(0).getChildren().get(1).getkeyList().get(0));
			
			System.out.println(this.root.getChildren().get(1).getChildren().get(0).getkeyList().get(0));
			System.out.println(this.root.getChildren().get(1).getChildren().get(1).getkeyList().get(0));
			System.out.println(this.root.getChildren().get(2).getChildren().get(0).getkeyList().get(0));
			System.out.println(this.root.getChildren().get(2).getChildren().get(1).getkeyList().get(0));
			System.out.println(this.root.getChildren().get(2).getChildren().get(1).getkeyList().get(1));	
		}
		return true;
	}
	
	//search key
	public ThreeWayBPlusTreeNode searchKey(ThreeWayBPlusTreeNode now,Integer e) {
		int i; // i=key가 들어가야 할 위치
		for (i=0;i<now.getkeyList().size();i++) {
			if (e<now.getkeyList().get(i)) {
				break;
			}
		}
		// leaf node의 전 노드까지 이동해주어야 함.
		
		while (!now.getChildren().get(i).getChildren().isEmpty()) {
			now=now.getChildren().get(i);
			for (i=0;i<now.getkeyList().size();i++) { // leaf node 전 노드에서 i=key가 들어가야 할 위치
				if (e<now.getkeyList().get(i)) {
					break;
				}
			}
		}
		// key 값 추가
		now.getChildren().get(i).getkeyList().add(e);
		Collections.sort(now.getChildren().get(i).getkeyList()); // sort
		addChild(now,i);

		return now;
		
	}
	
	public void addChild2(ThreeWayBPlusTreeNode now, ThreeWayBPlusTreeNode temp) {
		int i;
		for (i=0;i<now.getkeyList().size();i++) {
			if (temp.getkeyList().get(0)<now.getkeyList().get(i)) {
				break;
			}
		}
		System.out.println("-->" +i);
		now.getkeyList().add(temp.getkeyList().get(0));
		Collections.sort(now.getkeyList());

		now.getChildren().add(i,temp.getChildren().get(0));
//		now.getChildren().get(i).setParent(now);
		now.getChildren().add(i+1,temp.getChildren().get(1));
//		now.getChildren().get(i+1).setParent(now);
	}
	
	public void addChild(ThreeWayBPlusTreeNode now, int i) {
		if (now.getChildren().get(i).getkeyList().size()<3) {
			return;
		}
		ThreeWayBPlusTreeNode temp; // leafnode 에서 split
		temp = splitExternalNode(now.getChildren().get(i));
		now.getkeyList().add(temp.getkeyList().get(0));
		Collections.sort(now.getkeyList());
		now.getChildren().remove(i);
		now.getChildren().add(i,temp.getChildren().get(0));
		now.getChildren().get(i).setParent(now);
		now.getChildren().add(i+1,temp.getChildren().get(1));
		now.getChildren().get(i+1).setParent(now);
	}
	
	
	
	
	// splitInternal node
		public ThreeWayBPlusTreeNode splitInternalNode(ThreeWayBPlusTreeNode root) {
			
			//      o (mid)
			//     / \
			//(st)o   o (end)로 변환 시켜줌 세개다 key는 한개
			//   /     \
			// 아래 children도 연결 해주어야 함!!!!1
			
			ThreeWayBPlusTreeNode st = new ThreeWayBPlusTreeNode(); //처음
			ThreeWayBPlusTreeNode mid = new ThreeWayBPlusTreeNode();// 중간
			ThreeWayBPlusTreeNode end = new ThreeWayBPlusTreeNode();// 마지막
			
			st.getkeyList().add(root.getkeyList().get(0));
			st.getChildren().add(root.getChildren().get(0));
			st.getChildren().add(root.getChildren().get(1));
			
			mid.getkeyList().add(root.getkeyList().get(1));
			
			end.getkeyList().add(root.getkeyList().get(2));
			end.getChildren().add(root.getChildren().get(2));
			end.getChildren().add(root.getChildren().get(3));
			
			// 중간 노드의 자식은 st, end
			mid.getChildren().add(st);
			mid.getChildren().add(end);

			st.setParent(mid);
			end.setParent(mid);
			
			root =  mid;
			return root;
		}

	
	// splitExternal node
	public ThreeWayBPlusTreeNode splitExternalNode(ThreeWayBPlusTreeNode root) {
		//      o (mid)
		//     / \
		//(st)o   o (end-> key두개)  로 변환 시켜
		ThreeWayBPlusTreeNode st = new ThreeWayBPlusTreeNode();
		ThreeWayBPlusTreeNode mid = new ThreeWayBPlusTreeNode();
		ThreeWayBPlusTreeNode end = new ThreeWayBPlusTreeNode();
		
		st.getkeyList().add(root.getkeyList().get(0));
		mid.getkeyList().add(root.getkeyList().get(1));
		end.getkeyList().add(root.getkeyList().get(1));
		end.getkeyList().add(root.getkeyList().get(2));
		
		mid.getChildren().add(st);
		mid.getChildren().add(end);
		
		st.setParent(mid);
		end.setParent(mid);
		
		root =  mid;
		
		// leaf node 연결리스트 구현 
		st.setNext(end);
		return root;
	}
	
	
	public ThreeWayBPlusTreeNode getNode(Integer key) {
		ThreeWayBPlusTreeNode now = this.root;
		System.out.println("start finding " + key);
		System.out.println();
		int i=0;
		while (!now.getChildren().isEmpty()) {
//			System.out.println(now.getkeyList().size());
			for (i=0;i<now.getkeyList().size();i++) {
				if (now.getkeyList().get(i)>key) {
					break;
				}
			}
			// key값 비교 i-> 아래로 내려가는 방향 
			CompareKey(i,now.getkeyList().size(),now.getkeyList().get(0));
			now=now.getChildren().get(i);
		}
		
		for (int j=0;j<now.getkeyList().size();j++) {
			
			if (now.getkeyList().get(j)==key) {
				System.out.println(key + " found");
				return null;
			}
		}
		CompareKey(i,now.getkeyList().size(),now.getkeyList().get(0));
		System.out.println(key + " not found");
		return null;
	}
	
	public void CompareKey(int i, int size,int key) {
		// size가 1일 경우 내려갈 방향은 2개 -> left, right
		if (size==1) {
			if (i==0) {
				System.out.println("less than " + key);
			}
			else if (i==1) {
				System.out.println("larger than or equal to " + key);
			}
		}
		// size가 2일 경우 내려갈 방향은 3개 -> left, middle, right
		else if (size==2) {
			if (i==0) {
				System.out.println("less than " + key);
			}
			else if (i==1) {
				System.out.println("larger than or equal to " + key);
			}
			else if (i==2) {
				System.out.println("larger than or equal to " + key);
			}
		}
		System.out.println();
	}
	
	public void inorderTraverse() {
		ThreeWayBPlusTreeNode now = this.root;
		inorder(now);
	}
		
	public void inorder(ThreeWayBPlusTreeNode now) {
		// Left
		if (!now.getChildren().isEmpty()) {
			if (now.getChildren().get(0)!=null) {
				inorder(now.getChildren().get(0));
			}
		}
		// Node
		if (now.getChildren().isEmpty()) {
			if (!now.getkeyList().isEmpty()) {
				for (int i=0;i<now.getkeyList().size();i++) {
					System.out.println(now.getkeyList().get(i));
				}
			}
		}
		// Right
		if (!now.getChildren().isEmpty()) {
			if (now.getChildren().get(1)!=null) {
				inorder(now.getChildren().get(1));
			}
		}	
	}


	@Override
	public Comparator<? super Integer> comparator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer first() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer last() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public boolean add(Integer e) {
//		// TODO Auto-generated method stub
//		return false;
//	}

	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends Integer> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public Integer lower(Integer e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer floor(Integer e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer ceiling(Integer e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer higher(Integer e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer pollFirst() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer pollLast() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Integer> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NavigableSet<Integer> descendingSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Integer> descendingIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NavigableSet<Integer> subSet(Integer fromElement, boolean fromInclusive, Integer toElement,
			boolean toInclusive) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NavigableSet<Integer> headSet(Integer toElement, boolean inclusive) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NavigableSet<Integer> tailSet(Integer fromElement, boolean inclusive) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SortedSet<Integer> subSet(Integer fromElement, Integer toElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SortedSet<Integer> headSet(Integer toElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SortedSet<Integer> tailSet(Integer fromElement) {
		// TODO Auto-generated method stub
		return null;
	}

}
