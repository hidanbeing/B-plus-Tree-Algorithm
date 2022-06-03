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
			Collections.sort(this.
					root.getkeyList());
			// root node의 key가 3개가 되면,
			if (this.root.getkeyList().size()==(this.m)) {
				// split 해주어야 함 -> external node split(leaf 노드 split)
				this.root = splitExternalNode(this.root);
			}
		}
		// 그 외 나머지 경우
		else {
			ThreeWayBPlusTreeNode now =  new ThreeWayBPlusTreeNode();
			ThreeWayBPlusTreeNode temp =  new ThreeWayBPlusTreeNode();
			now=searchKey(this.root,e); // -> key가 어디에 추가되어야 하는지 확인후 삽
			// internal node에서 split이 필요할 경우!!! -> child에서 parent로 올라가야함..
			while (true) {
				// 필요하지 않으면 break
				if (now.getkeyList().size()<3) {
					break;
				}
				else {
					temp = splitInternalNode(now);
					if (now.getParent()!=null) {
						now=now.getParent();
						addChild2(now,temp); 
					}
					else {
						now=temp;
						break; 
					}
				}
			}
			// now를 root node로 돌려놓음
			while (now.getParent()!=null) {
				now=now.getParent();
			}
			this.root=now;
			
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
		// leaf node add
		addChild(now,i);
		
		return now;
		
	}
	//addchild2 ->internal node 의 split이 필요한 경우
	public void addChild2(ThreeWayBPlusTreeNode now, ThreeWayBPlusTreeNode temp) {
		// i=key가 들어가야 할 위치
		//       o  <- key값 추가
		//     / | \
		//    o  o  o  
		int i;
		for (i=0;i<now.getkeyList().size();i++) {
			if (temp.getkeyList().get(0)<now.getkeyList().get(i)) {
				break;
			}
		}
		now.getkeyList().add(temp.getkeyList().get(0));
		Collections.sort(now.getkeyList());
		now.getChildren().remove(i);// 기존에 있던 i번째 child를 지우고 새로운child 지
		now.getChildren().add(i,temp.getChildren().get(0));
		now.getChildren().add(i+1,temp.getChildren().get(1));

		now.getChildren().get(0).setParent(now);
		now.getChildren().get(1).setParent(now);
		now.getChildren().get(2).setParent(now);
	}
	
	//addchild ->external node 의 split이 필요한 경우
	public void addChild(ThreeWayBPlusTreeNode now, int i) {
		if (now.getChildren().get(i).getkeyList().size()<3) { // max key조건 어기지 않
			return;
		}
		ThreeWayBPlusTreeNode temp; // leafnode 에서 split
		
		temp = splitExternalNode(now.getChildren().get(i));
		// i=key가 들어가야 할 위치
		//        o  <- key값 추가
		//     /  |  \
		//    o - o - o  

		temp.setParent(now.getParent());
		now.getkeyList().add(temp.getkeyList().get(0));
		Collections.sort(now.getkeyList()); // key값을 넣고, 정렬 해주어야 함.
		now.getChildren().remove(i); // 기존에 있던 i번째 child를 지우고 새로운child 지
		now.getChildren().add(i,temp.getChildren().get(0));
		now.getChildren().add(i+1,temp.getChildren().get(1));
		
// 부모 도 지정해주어야 함 -->전부 now
		now.getChildren().get(0).setParent(now);
		now.getChildren().get(1).setParent(now);
		now.getChildren().get(2).setParent(now);
		
		now.getChildren().get(0).setNext(now.getChildren().get(1));
		now.getChildren().get(1).setNext(now.getChildren().get(2));
		
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
	
			root.getChildren().get(0).setParent(st);
			root.getChildren().get(1).setParent(st);
			
			mid.getkeyList().add(root.getkeyList().get(1));
			
			end.getkeyList().add(root.getkeyList().get(2));
			end.getChildren().add(root.getChildren().get(2));
			end.getChildren().add(root.getChildren().get(3));
			
			root.getChildren().get(2).setParent(end);
			root.getChildren().get(3).setParent(end);
			
			st.setParent(mid);
			end.setParent(mid);
			// 중간 노드의 자식은 st, end
			mid.getChildren().add(0,st);
			mid.getChildren().add(1,end);
			
			mid.getChildren().get(0).setParent(mid);
			mid.getChildren().get(1).setParent(mid);
			
			mid.setParent(root.getParent());
			
			
			
			return mid;
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
		
		mid.getChildren().get(0).setParent(mid);
		mid.getChildren().get(1).setParent(mid);
		
//		root =  mid;

		
		// leaf node 연결리스트 구현 
		st.setNext(end);
		return mid;
	}
	
	
	public ThreeWayBPlusTreeNode getNode(Integer key) {
		ThreeWayBPlusTreeNode now = this.root;
		System.out.println("start finding " + key);
		System.out.println();
		int i=0;
		while (!now.getChildren().isEmpty()) {
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
		//가장 첫번째 수는트리의 맨 left node의 keylist의 0번째 값.
		ThreeWayBPlusTreeNode now;
		now=this.root;
		while (!now.getChildren().isEmpty()) {
			now=now.getChildren().get(0);
		}
		return now.getKey(0);
	}

	
	@Override
	public Integer last() {
		//가장 첫번째 수는트리의 맨 right node의 keylist의 마지.
		ThreeWayBPlusTreeNode now;
		now=this.root;
		while (!now.getChildren().isEmpty()) {
			int k= now.getChildren().size();
			now=now.getChildren().get(k-1);
		}
		int p=now.getkeyList().size();
		return now.getKey(p-1);
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
		// 값이 같으면 지우기!!!
		ThreeWayBPlusTreeNode now;
		now=this.root;
		while (!now.getChildren().isEmpty()) {
			now=now.getChildren().get(0);
		}
		Iterator<Integer> iter = now.getkeyList().iterator();
		while (iter.hasNext()) {
			Object n = iter.next();
			if (n==o) {
				iter.remove();
			}
		}
		return true;
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
		ThreeWayBPlusTreeNode now;
		now=this.root;
		while (!now.getChildren().isEmpty()) {
			now=now.getChildren().get(0);
		}
		Iterator<Integer> iter = now.getkeyList().iterator();
		return iter;
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
