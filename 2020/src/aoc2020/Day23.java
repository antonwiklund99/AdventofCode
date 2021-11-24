package aoc2020;

import java.util.*;

public class Day23 {
	
	public void part1() {
		BadLinkedList cups = new BadLinkedList(4,1,8,9,7,6,2,3,5);
		BadLinkedList.Node current = cups.head;
		for (int i = 0; i < 100; i++) {
			BadLinkedList.Node rm1 = current.popRight();
			BadLinkedList.Node rm2 = current.popRight();
			BadLinkedList.Node rm3 = current.popRight();
			
			BadLinkedList.Node dest;
			int searchValue = current.value;
			do {
				searchValue = searchValue - 1;
				if (searchValue == 0) searchValue = 9;
				dest = cups.find(searchValue);
				
			} while (dest == rm1 || dest == rm2 || dest == rm3);
			
			dest.addRight(rm3);
			dest.addRight(rm2);
			dest.addRight(rm1);
			
			current = current.next;
		}
		
		BadLinkedList.Node n = cups.find(1).next;
		while (n.value != 1) {
			System.out.print(n.value);
			n = n.next;
		}
		System.out.println();
	}
	
	public void part2() {
		BadLinkedList cups = new BadLinkedList(4,1,8,9,7,6,2,3,5);
		for (int i = 10; i <= 1_000_000; i++) {
			cups.add(i);
		}
		BadLinkedList.Node current = cups.head;
		for (int i = 0; i < 10_000_000; i++) {
			BadLinkedList.Node rm1 = current.popRight();
			BadLinkedList.Node rm2 = current.popRight();
			BadLinkedList.Node rm3 = current.popRight();
			
			BadLinkedList.Node dest;
			int searchValue = current.value;
			do {
				searchValue = searchValue - 1;
				if (searchValue == 0) searchValue = 1_000_000;
				dest = cups.find(searchValue);
				
			} while (dest == rm1 || dest == rm2 || dest == rm3);
			
			dest.addRight(rm3);
			dest.addRight(rm2);
			dest.addRight(rm1);
			
			current = current.next;
		}
		
		BadLinkedList.Node node1 = cups.find(1);
		System.out.println(((long) node1.next.value) * ((long) node1.next.next.value));
	}
	
	public static void main(String[] args) {
		Day23 day = new Day23();
		day.part1();
		day.part2();
	}

}

class BadLinkedList {
	HashMap<Integer,Node> nodeMap = new HashMap<Integer,Node>();
	Node head;
	Node last;
	
	BadLinkedList(int... values) {
		for (int value : values) {
			add(value);
		}
	}
	
	public void add(int value) {
		Node newNode = new Node(value);
		nodeMap.put(value, newNode);
		if (head == null) {
			head = newNode;
			last = newNode;
		} else {
			last.next = newNode;
			last = newNode;
			newNode.next = head;
		}
	}
	
	public Node find(int value) {
		return nodeMap.get(value);
	}
	
	public class Node {
		int value;
		Node next;
		
		Node(int value) {
			this.value = value;
		}
		
		public Node popRight() {
			Node old = this.next;
			if (this.next == head) {
				head = head.next;
			}
			this.next = this.next.next;
			return old;
		}
		
		public void addRight(Node node) {
			node.next = this.next;
			this.next = node;
		}
	}
}
